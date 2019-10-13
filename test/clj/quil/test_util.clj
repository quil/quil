(ns quil.test-util
  (:import java.awt.GraphicsEnvironment))

(defn- display-density []
  (try
    (.. GraphicsEnvironment (getLocalGraphicsEnvironment) (getDefaultScreenDevice) (getScaleFactor))
    (catch IllegalArgumentException e
      ; getScaleFactor() method is present only on osx.
      ; For other systems return 1 for now.
      1)))

(defn path-to-snippet-snapshots [platform]
  (str "snippet-snapshots/" platform
       (if (= 2 (display-density))
         "/retina/"
         "/normal/")))
