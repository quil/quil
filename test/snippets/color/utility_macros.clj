(ns snippets.color.utility-macros
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet with-fill-s {}
  (fill 255) ; global white fill

  (rect 30 30 60 60) ; white rect

  (with-fill [255 0 0]
    (rect 60 60 90 90)
    (rect 90 90 120 120)) ; two red rects

  (rect 120 120 150 150)) ; white rect again

(defsnippet with-stroke-s {}
  (fill 255) ; global white fill
  (stroke-weight 10) ; make borders thicker

  (rect 30 30 60 60) ; white rect

  (with-stroke [255 0 0]
    (rect 60 60 90 90)
    (rect 90 90 120 120)) ; two red rects

  (rect 120 120 150 150))
