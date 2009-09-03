(ns rosado.processing.applet
  (:use [rosado.processing]
        [clojure.contrib.java-utils :only [as-str]])
  (:import (java.awt Frame)))

(defn bind-applets
  "Turn the method map into something one that update-proxy can use."
  [methods [method-name f]]
  (assoc methods (name method-name)
         (fn [this & args]
           (binding [*applet* this]
             (apply f args)))))

(defmacro defapplet
  "Define an applet. Takes an app-name and a map of options."
  [app-name & opts]
  (let [options (assoc (apply hash-map opts) :name (str app-name))
        fns (dissoc options :name :title :height :width)]
    `(def ~app-name
          (let [frame# (atom nil)
                methods# (reduce bind-applets {} ~fns)
                prx# (proxy [processing.core.PApplet
                             clojure.lang.IMeta] []
                       (meta [] (assoc ~options :frame frame#)))]
            (update-proxy prx# methods#)
            prx#))))

(defn run [applet]
  (.init applet)
  (let [m (.meta applet)
        width (or (:width m) 200)
        height (or (:height m) 200)]
    (.size applet width height)
    (reset! (:frame m)
            (doto (Frame. (or (:title m) (:name m)))
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