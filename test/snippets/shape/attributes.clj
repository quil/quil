(ns snippets.shape.attributes
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet ellipse-mode-s {}
  (stroke-weight 5)
  (ellipse-mode :center)
  (with-translation [125 125]
   (ellipse 0 0 100 70)
   (point 0 0))

  (ellipse-mode :radius)
  (with-translation [375 125]
    (ellipse 0 0 100 70)
    (point 0 0))

  (ellipse-mode :corner)
  (with-translation [125 375]
    (ellipse 0 0 100 70)
    (point 0 0))

  (ellipse-mode :corners)
  (with-translation [375 375]
    (ellipse -50 -35 50 35)
    (point -50 -35)
    (point 50 35)))

(defsnippet smooth-no-smooth-s {:renderer :p2d}
  (smooth)
  (with-translation [125 125]
   (ellipse 0 0 200 200))

  (smooth 2)
  (with-translation [125 375]
   (ellipse 0 0 200 200))

  (no-smooth)
  (with-translation [375 125]
    (ellipse 0 0 200 200)))

(defsnippet rect-mode-s {}
  (stroke-weight 5)
  (rect-mode :center)
  (with-translation [125 125]
    (stroke 0)
    (rect 0 0 100 70)
    (stroke 255 0 0)
    (point 0 0))

  (rect-mode :radius)
  (with-translation [375 125]
    (stroke 0)
    (rect 0 0 100 70)
    (stroke 255 0 0)
    (point 0 0))

  (rect-mode :corner)
  (with-translation [125 375]
    (stroke 0)
    (rect 0 0 100 70)
    (stroke 255 0 0)
    (point 0 0))

  (rect-mode :corners)
  (with-translation [375 375]
    (stroke 0)
    (rect -50 -35 50 35)
    (stroke 255 0 0)
    (point -50 -35)
    (point 50 35)))

(defsnippet stroke-cap-s {}
  (stroke-weight 12)
  (stroke-cap :square)
  (line 230 200 270 200)

  (stroke-cap :project)
  (line 230 250 270 250)

  (stroke-cap :round)
  (line 230 300 270 300))

(defsnippet stroke-join-s {}
  (rect-mode :center)
  (stroke-weight 12)
  (stroke-join :miter)
  (rect 125 125 100 100)

  (stroke-join :bevel)
  (rect 375 125 100 100)

  (stroke-join :round)
  (rect 125 375 100 100))

(defsnippet stroke-weight-s {}
  (doseq [i (range 1 10)]
    (stroke-weight i)
    (line 230 (+ (* i 30) 100)
          270 (+ (* i 30) 100))))

