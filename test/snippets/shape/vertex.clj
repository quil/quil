(ns snippets.shape.vertex
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet begin-contour-end-contour-s {:renderer :p2d}
  (stroke 255 0 0)
  (begin-shape)
  (vertex 250 20)
  (vertex 400 400)
  (vertex 50 400)
  (begin-contour)
  (vertex 200 200)
  (vertex 300 200)
  (vertex 250 380)
  (end-contour)
  (end-shape :close))

(defsnippet begin-shape-end-shape-s {:renderer :p2d}
  (stroke 255 0 0)
  (doseq [[ind begin-mode close-mode]
          [[0 nil nil]
           [1 nil :close]
           [2 :points]
           [3 :lines]
           [4 :triangles]
           [5 :triangle-fan]
           [6 :triangle-strip]
           [7 :quads]
           [8 :quad-strip]]
          :let [base-x (* 150 (mod ind 3))
                base-y (* 150 (quot ind 3))]]
    (if begin-mode
      (begin-shape begin-mode)
      (begin-shape))
    (vertex (+ base-x 50) (+ base-y 10))
    (vertex (+ base-x 80) (+ base-y 30))
    (vertex (+ base-x 80) (+ base-y 70))
    (vertex (+ base-x 50) (+ base-y 90))
    (vertex (+ base-x 20) (+ base-y 70))
    (vertex (+ base-x 20) (+ base-y 30))
    (if close-mode
      (end-shape close-mode)
      (end-shape))))

(defsnippet bezier-vertex-s {:renderer :p3d}
  (camera -400 250 -100 500 250 0 0 0 1)
  (begin-shape)
  (vertex 30 20)
  (bezier-vertex 480 0 480 475 30 475)
  (bezier-vertex 250 380 360 125 30 20)
  (end-shape :close)

  (begin-shape)
  (vertex 30 20 0)
  (bezier-vertex 480 0 20 480 475 30 30 475 40)
  (bezier-vertex 250 380 40 360 125 10 30 20 0)
  (end-shape :close))

(defsnippet curve-vertex-s {:renderer :p3d}
  (camera 50 200 50 50 0 0 0 0 1)
  (begin-shape)
  (curve-vertex 0 0)
  (curve-vertex 0 0)
  (curve-vertex 100 20)
  (curve-vertex 100 80)
  (curve-vertex 20 80)
  (curve-vertex 0 0)
  (curve-vertex 0 0)
  (end-shape :close)

  (begin-shape)
  (curve-vertex 0 0 0)
  (curve-vertex 0 0 0)
  (curve-vertex 100 0 20)
  (curve-vertex 100 0 80)
  (curve-vertex 20 0 80)
  (curve-vertex 0 0 0)
  (curve-vertex 0 0 0)
  (end-shape :close))

(defsnippet quadratic-vertex-s {:renderer :p3d}
  (camera 50 200 50 50 0 0 0 0 -1)
  (line 0 0 0 0 0 100)
  (line 0 0 0 0 100 0)
  (line 0 0 0 100 0 0)
  (begin-shape)
  (vertex 0 0)
  (quadratic-vertex 30 50 10 100)
  (quadratic-vertex 50 -50 90 100)
  (quadratic-vertex 80 50 100 0)
  (end-shape :close)
  (begin-shape)
  (vertex 0 0 0)
  (quadratic-vertex 30 0 50 10 0 100)
  (quadratic-vertex 50 0 -50 90 0 100)
  (quadratic-vertex 80 0 50 100 0 0)
  (end-shape :close))

(defsnippet texture-s {:renderer :p2d}
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 255)
      (fill 255 0 0)
      (rect 0 60 100 40)
      (fill 0 150 0)
      (rect 0 0 100 60))
    (image gr 0 0)

    (with-translation [250 250]
      (begin-shape)
      (texture gr)
      (vertex 50 100 75 100)
      (vertex 100 50 100 75)
      (vertex 100 -50 100 25)
      (vertex 50 -100 75 0)
      (vertex -50 -100 25 0)
      (vertex -100 -50 0 25)
      (vertex -100 50 0 75)
      (vertex -50 100 25 100)
      (end-shape :close))))

(defsnippet texture-mode-s {:renderer :p2d}
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 255)
      (fill 255 0 0)
      (rect 0 60 100 40)
      (fill 0 150 0)
      (rect 0 0 100 60))
    (image gr 0 0)

    (with-translation [375 125]
      (begin-shape)
      (texture gr)
      (texture-mode :image)
      (vertex 0 0 0 0)
      (vertex 100 100 100 100)
      (vertex 0 100 0 100)
      (end-shape :close))

    (with-translation [125 375]
      (begin-shape)
      (texture gr)
      (texture-mode :normal)
      (vertex 0 0 0 0)
      (vertex 100 100 1 1)
      (vertex 0 100 0 1)
      (end-shape :close))))

(defsnippet texture-wrap-s {:renderer :p2d}
  (let [txtr (create-graphics 100 100)
        mode (if (even? (frame-count)) :clamp :repeat)]
    (with-graphics txtr
      (background 255)
      (fill 255 0 0)
      (rect 0 60 100 40)
      (fill 0 150 0)
      (rect 0 0 100 60))
    (image txtr 0 0)
    (texture-wrap mode)
    (with-translation [200 200]
      (begin-shape)
      (texture txtr)
      (vertex 0 0 0 0)
      (vertex 200 0 200 0)
      (vertex 200 200 200 200)
      (vertex 0 200 0 200)
      (end-shape :close))))

(defsnippet vertex-s {:renderer :p3d}
  (camera 100 400 200 100 0 0 0 0 -1)
  (line 0 0 0 0 0 100)
  (line 0 0 0 0 100 0)
  (line 0 0 0 100 0 0)

  (let [txtr (create-graphics 100 100)]
    (with-graphics txtr
      (background 255)
      (fill 255 0 0)
      (rect 0 60 100 40)
      (fill 0 150 0)
      (rect 0 0 100 60))

    (begin-shape)
    (vertex 0 0)
    (vertex 100 0)
    (vertex 100 100)
    (vertex 0 100)
    (end-shape :close)

    (begin-shape)
    (vertex 0 0 0)
    (vertex 100 0 0)
    (vertex 100 0 100)
    (vertex 0 0 100)
    (end-shape :close)

    (begin-shape)
    (texture txtr)
    (vertex 100 0 0 0)
    (vertex 200 0 100 0)
    (vertex 200 100 100 100)
    (vertex 100 100 0 100)
    (end-shape :close)

    (begin-shape)
    (texture txtr)
    (vertex 100 0 0 0 0)
    (vertex 200 0 0 100 0)
    (vertex 200 0 100 100 100)
    (vertex 100 0 100 0 100)
    (end-shape :close)))
