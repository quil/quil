(ns quil.test-util
  #?(:clj (:import java.awt.GraphicsEnvironment)))

(defn- display-density []
  #?(:clj (.. GraphicsEnvironment (getLocalGraphicsEnvironment) (getDefaultScreenDevice) (getScaleFactor))
     :cljs (.-devicePixelRatio js/window)))

(defn path-to-snippet-snapshots [platform]
  (str "snippet-snapshots/" platform
    (if (= 2 (display-density))
      "/retina/"
      "/normal/")))
