;; processing example

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
  (size 200 200)
  (smooth)
  (no-stroke))


(defn mouse-moved [evt]
  (let [x (.getX evt) y (.getY evt)]
    (reset! mouse-position [x y])))

;; Now we just need to define an applet:

(defapplet mouse-example :title "Mouse example."
  :size [200 200]
  :setup setup :draw draw
  :mouse-moved mouse-moved)

(run mouse-example)

;; (stop mouse-example)