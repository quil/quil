(ns quil.test-util
  (:import java.awt.GraphicsEnvironment))

(defn- display-density []
  (.. GraphicsEnvironment (getLocalGraphicsEnvironment) (getDefaultScreenDevice) (getScaleFactor)))

(defn path-to-snippet-snapshots [platform]
  (str "snippet-snapshots/" platform
    (if (= 2 (display-density))
      "/retina/"
      "/normal/")))
