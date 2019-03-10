(ns quil.snippets.shape.primitives-2d
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet arc
  "arc"
  {}

  (q/background 255)
  (q/stroke 0)
  (q/fill 220 200 255)

  (comment "draw default arc")
  (q/arc 50 100 200 170 0 q/QUARTER-PI)

  (comment "draw different arc modes")
  (doseq [[ind mode] [[0 :open]
                      [1 :chord]
                      [2 :pie]]]
    (q/arc (+ 50 (* ind 150)) 300 200 170 0 q/QUARTER-PI mode)))

(defsnippet ellipse
  "ellipse"
  {}

  (q/background 255)
  (q/stroke 0)
  (q/fill 220 200 255)
  (q/ellipse 250 250 400 200))

(defsnippet line
  "line"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 250 250 250 0 0 0 0 0 -1)

  (comment "red line")
  (q/stroke 255 0 0)
  (q/line [0 0] [100 100])

  (comment "green line")
  (q/stroke 0 255 0)
  (q/line 100 0 0 100)

  (comment "three blue lines")
  (q/stroke 0 0 255)
  (q/line 0 0 0 170 120 100)
  (q/line 170 120 100 50 -50 20)
  (q/line 50 -50 20 0 0 0))

(defsnippet point
  "point"
  {:renderer :p3d}

  (q/background 255)
  (q/stroke-weight 10)
  (comment "red ponit")
  (q/stroke 255 0 0)
  (q/point 10 10)
  (comment "blue point")
  (q/stroke 0 0 255)
  (q/point 50 50 -200))

(defsnippet quad
  "quad"
  {}

  (q/background 255)
  (q/fill 220 200 255)
  (q/quad 100 120 450 70 260 390 10 380))

(defsnippet rect
  "rect"
  {}

  (q/background 255)
  (q/fill 220 200 255)
  (comment "normal rect")
  (q/rect 50 50 150 100)
  (comment "rect with rounded corners")
  (q/rect 300 50 150 100 20)
  (comment "rect with rounded corners where each corner is different")
  (q/rect 50 300 150 100 5 10 20 30))

(defsnippet triangle
  "triangle"
  {}

  (q/background 255)
  (q/fill 220 200 255)
  (q/triangle 50 50 450 200 100 390))
