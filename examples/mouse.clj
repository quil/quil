(ns mouse-example
  (:use [processing.core]
        [processing.core.applet]))

(def mouse-position (atom [0 0]))

(defn draw
  []
  (background-float 125)
  (stroke-weight 20)
  (stroke-float 10)
  (let [[x y] @mouse-position]
    (point x y)))

(defn setup []
  (smooth)
  (no-stroke))

(defn mouse-moved []
  (let [x (mouse-x)  y (mouse-y)]
    (reset! mouse-position [x y])))

(defapplet mouse-example
  :title "Mouse example."
  :size [200 200]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved)

(applet-start mouse-example)
