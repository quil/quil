(ns snippets.shape.2d-primitives
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet arc-s {}
  (background 255)
  (stroke 0)
  (fill 220 200 255)
  (arc 50 100 200 170 0 QUARTER-PI)
  (doseq [[ind mode] [[0 :open]
                      [1 :chord]
                      [2 :pie]]]
    (arc (+ 50 (* ind 150)) 300 200 170 0 QUARTER-PI mode)))

(defsnippet ellipse-s {}
  (background 255)
  (stroke 0)
  (fill 220 200 255)
  (ellipse 250 250 400 200))

(defsnippet line-s {:renderer :p3d}
  (background 255)
  (camera 250 250 250 0 0 0 0 0 -1)
  (stroke 255 0 0)
  (line [0 0] [100 100])
  (stroke 0 255 0)
  (line 100 0 0 100)
  (stroke 0 0 255)
  (line 0 0 0 170 120 100)
  (line 170 120 100 50 -50 20)
  (line 50 -50 20 0 0 0))

(defsnippet point-s {:renderer :p3d}
  (background 255)
  (stroke-weight 10)
  (stroke 255 0 0)
  (point 10 10)
  (stroke 0 0 255)
  (point 50 50 -200))

(defsnippet quad-s {}
  (background 255)
  (fill 220 200 255)
  (quad 100 120 450 70 260 390 10 380))

(defsnippet rect-s {}
  (background 255)
  (fill 220 200 255)
  (rect 50 50 150 100)
  (rect 300 50 150 100 20)
  (rect 50 300 150 100 5 10 20 30))

(defsnippet triangle-s {}
  (background 255)
  (fill 220 200 255)
  (triangle 50 50 450 200 100 390))
