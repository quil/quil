(ns mouse-example
  (:use quil.core))

(defn setup []
  (smooth)
  (no-stroke)
  (set-state! :mouse-position (atom [0 0])))

(defn draw
  []
  (background-float 125)
  (stroke-weight 20)
  (stroke-float 10)
  (let [[x y] @(state :mouse-position)]
    (point x y)))

(defn mouse-moved []
  (let [x (mouse-x)  y (mouse-y)]
    (reset! (state :mouse-position) [x y])))

(defsketch mouse-example
  :title "Mouse example."
  :size [200 200]
  :setup setup
  :draw draw
  :mouse-moved mouse-moved)
