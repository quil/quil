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

(defmacro defapplet
  "Define an applet. Takes an app-name and a map of options."
  [app-name & opts]
  (let [options (assoc (apply hash-map opts) :name (str app-name))
        fns (dissoc options :name :title :size)
        methods (reduce with-binding {} fns)]
    `(def ~app-name
          (let [frame# (atom nil)
                prx# (proxy [processing.core.PApplet
                             clojure.lang.IMeta] []
                       (meta [] (assoc ~options :frame frame#)))]
            (update-proxy prx# ~methods)
            prx#))))

(defn run
  "Launches the applet. If given the flag :interactive, it won't exit
  on clicking the close button - it will only dispose the window."
  [applet & interactive?]
  (.init applet)
  (let [m (.meta applet)
        [width height & mode] (or (:size m) [200 200])
        mode (if-let [kw (first mode)]
               (tosymb kw)
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
  (.destroy applet)
  (.hide @(:frame ^applet)))

(comment ;; Usage:
  (defapplet growing-triangle
    :draw (fn [] (line 10 10 (frame-count) 100)))

  (run growing-triangle)
  (stop growing-triangle))