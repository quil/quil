(ns quil.examples.mouse-wheel
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.calc :only [mul-add]]))

(def radius 100)

(defn setup []
  (background 255)
  (set-state! :zoom (atom 1))
  (stroke 0))

(defn draw []
  (background 255)
  (translate (/ (width) 2.0) (/ (height) 2.0))
  (scale @(state :zoom))
  (rotate-y (* (mouse-x) 0.02))
  (rotate-x (* (mouse-y) 0.02))
  (let [line-args (for [t (range 0 180)]
                    (let [s        (* t 18)
                          radian-s (radians s)
                          radian-t (radians t)
                          x (* radius  (cos radian-s) (sin radian-t))
                          y (* radius  (sin radian-s) (sin radian-t))
                          z (* radius (cos radian-t))]
                      [x y z]))]
    (dorun
     (map #(apply line %) (line-join-points line-args)))))

(defn mouse-wheel [rotation]
  (swap! (state :zoom) #(max 1 (- % (* 0.1 rotation)))))

(defsketch gen-art-29
  :title "Spiral Sphere"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p3d
  :mouse-wheel mouse-wheel)



