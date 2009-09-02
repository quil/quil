(ns rosado.processing.applet
  (:use [rosado.processing]
        [clojure.contrib.java-utils :only [as-str]])
  (:import (java.awt Frame)))

(defn bind-applet [f]
  (fn [this & args]
    (binding [*applet* this] (apply f args))))

(defmacro defapplet
  "Define an applet. Takes an app-name and a map of options."
  [app-name & opts]
  (let [options (assoc (apply hash-map opts) :name (str app-name))
        fns (dissoc options :name :title :height :width)
        ;; TODO: fix this to automatically bind *applet* in fns
        ;; methods (zipmap (map name (keys fns))
        ;;                 (map bind-applet (vals fns)))
        ]
    `(def ~app-name
          (let [frame# (atom nil)
                prx# (proxy [processing.core.PApplet
                             clojure.lang.IMeta] []
                       (meta [] (assoc ~options :frame frame#)))]
            (update-proxy prx# ~fns)
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
