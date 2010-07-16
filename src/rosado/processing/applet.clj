(ns rosado.processing.applet
  (:use [rosado.processing :except (size)])
  (:import (javax.swing JFrame)))

(defn- with-binding
  "Turn the method map into something one that update-proxy can use."
  [methods [method-name f]]
  (assoc methods (name method-name)
         `(fn [this# & args#]
            (binding [*applet* this#]
              (apply ~f args#)))))

(defn- fix-mname
  "Changes :method-name to :methodName."
  [[mname fun]]
  (let [mname (name mname)
        mr (re-matcher #"\-[a-zA-z]" mname)
        replace-fn (comp #(.replaceFirst mr %) toupper #(.substring % 1))
        fixed-name (if-let [matched (re-find mr)]
                     (replace-fn matched)
                     mname)]
    [(keyword fixed-name) fun]))

(defmacro defapplet
  "Define an applet. Takes an app-name and a map of options."
  [app-name & opts]
  (let [options (assoc (apply hash-map opts) :name (str app-name))
        fns (dissoc options :name :title :size)
        methods (reduce with-binding {} (into {} (map fix-mname fns)))]
    `(def ~app-name
          (let [frame# (atom nil)
                prx# (proxy [processing.core.PApplet
                             clojure.lang.IMeta] []
                       (meta [] (assoc ~options :frame frame#)))]
            (update-proxy prx# ~methods)
            prx#))))

(def ^{:private true}
     modes {:JAVA2D JAVA2D :OPENGL OPENGL
            :P3D P3D :P2D P2D :PDF PDF})

(defn run
  "Launches the applet. If given the flag :interactive, it won't exit
  on clicking the close button - it will only dispose the window."
  [applet & interactive?]
  (.init applet)
  (let [m (.meta applet)
        [width height & mode] (or (:size m) [200 200])
        mode (if-let [kw (first mode)]
               (modes (-> kw name toupper keyword))
               JAVA2D)
        close-op (if (first interactive?)
                   JFrame/DISPOSE_ON_CLOSE
                   JFrame/EXIT_ON_CLOSE)]
    (.size applet width height mode)
    (reset! (:frame m)
            (doto (JFrame. (or (:title m) (:name m)))
              (.setDefaultCloseOperation close-op)
              (.setSize width height)
              (.add applet)
              (.pack)
              (.show)))))


(defn stop [applet]
  (let [closing-fn (fn []
                     (let [frame @(:frame (meta applet))]
                       (.destroy applet)
                       (doto frame
                         (.hide)
                         (.dispose))))]
    (javax.swing.SwingUtilities/invokeAndWait closing-fn)))

(comment ;; Usage:
  (defapplet growing-triangle
    :draw (fn [] (line 10 10 (frame-count) 100)))

  (run growing-triangle)
  (stop growing-triangle))
