(ns snippets.transform
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet apply-matrix-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (apply-matrix 1 0 50
                0 1 -50)
  (box 50)
  (let [s (sin 1)
        c (cos 1)]
   (apply-matrix 1 0 0 0
                 0 c s 50
                 0 (- s) c -50
                 0 0 0 1))
  (box 50))

(defsnippet push-matrix-pop-matrix-s {}
  (fill 255 0 0)
  (translate 20 20)
  (rect 0 0 50 50)
  (translate 150 0)
  (push-matrix)
  (rotate 1)
  (rect 0 0 50 50)
  (pop-matrix)
  (translate 150 0)
  (rect 0 0 50 50))

(defsnippet print-matrix-s {}
  (translate 250 250)
  (rotate 1)
  (rect 0 0 100 100)
  (print-matrix))

(defsnippet reset-matrix-s {}
  (rect 0 0 100 50)
  (translate 250 250)
  (rect 0 0 50 50)
  (reset-matrix)
  (rect 0 0 50 100))

(defsnippet rotate-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (push-matrix)

  (translate 100 0 0)
  (rotate 0.5)
  (box 50)
  (pop-matrix)
  (push-matrix)

  (translate 0 100 0)
  (rotate 0.5 1 0 0)
  (box 50)
  (pop-matrix)
  (push-matrix)

  (translate 0 0 100)
  (rotate 0.5 0 1 0)
  (box 50)
  (pop-matrix))

(defsnippet rotate-x-y-z-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (push-matrix)

  (translate 100 0 0)
  (rotate-x 0.5)
  (box 50)
  (pop-matrix)
  (push-matrix)

  (translate 0 100 0)
  (rotate-y 0.5)
  (box 50)
  (pop-matrix)
  (push-matrix)

  (translate 0 0 100)
  (rotate-z 0.5)
  (box 50)
  (pop-matrix))

(defsnippet scale-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (with-translation [100 0 0]
    (scale 0.5)
    (box 50)
    (scale 2))

  (with-translation [0 100 0]
    (scale 1 0.5)
    (box 50)
    (scale 1 2))

  (with-translation [0 0 100]
    (scale 0.5 1 1.5)
    (box 50)
    (scale 2 1 0.75)))

(defsnippet shear-x-y-s {}
  (with-translation [125 125]
    (rect 0 0 100 50))
  (with-translation [375 125]
    (shear-y 0.5)
    (rect 0 0 100 50))
  (with-translation [125 375]
    (shear-x 0.5)
    (rect 0 0 100 50)))

(defsnippet translate-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (translate 100 0)
  (box 50)
  (translate [-100 100])
  (box 50)
  (translate 0 -100 100)
  (box 50))

