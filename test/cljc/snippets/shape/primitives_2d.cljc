(ns snippets.shape.primitives-2d
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet arc {}
  (q/background 255)
  (q/stroke 0)
  (q/fill 220 200 255)
  (q/arc 50 100 200 170 0  q/QUARTER-PI)
  (doseq [[ind mode] [[0 :open]
                      [1 :chord]
                      [2 :pie]]]
    (q/arc (+ 50 (* ind 150)) 300 200 170 0  q/QUARTER-PI #?(:clj mode))))

(defsnippet ellipse {}
  (q/background 255)
  (q/stroke 0)
  (q/fill 220 200 255)
  (q/ellipse 250 250 400 200))

(defsnippet line {:renderer :p3d}
  (q/background 255)
  (q/camera 250 250 250 0 0 0 0 0 -1)
  (q/stroke 255 0 0)
  (q/line [0 0] [100 100])
  (q/stroke 0 255 0)
  (q/line 100 0 0 100)
  (q/stroke 0 0 255)
  (q/line 0 0 0 170 120 100)
  (q/line 170 120 100 50 -50 20)
  (q/line 50 -50 20 0 0 0))

(defsnippet point {:renderer :p3d}
  (q/background 255)
  (q/stroke-weight 10)
  (q/stroke 255 0 0)
  (q/point 10 10)
  (q/stroke 0 0 255)
  (q/point 50 50 -200))

(defsnippet quad {}
  (q/background 255)
  (q/fill 220 200 255)
  (q/quad 100 120 450 70 260 390 10 380))

(defsnippet rect {}
  (q/background 255)
  (q/fill 220 200 255)
  (q/rect 50 50 150 100)
  (q/rect 300 50 150 100 20)
  (q/rect 50 300 150 100 5 10 20 30))

(defsnippet triangle {}
  (q/background 255)
  (q/fill 220 200 255)
  (q/triangle 50 50 450 200 100 390))
