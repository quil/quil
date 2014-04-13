(ns snippets.transform.utility-macros
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet with-rotation-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (box 50)
  (translate 0 0 100)
  (with-rotation [1]
    (box 50))

  (translate 100 0 -100)
  (with-rotation [1 1 0 0]
    (box 50))

  (translate -100 100 0)
  (with-rotation [1 0 1 0]
    (box 50)))

(defsnippet with-translation-s {}
  (translate 10 10)
  (rect 0 0 50 50)
  (with-translation [150 0]
    (rect 0 0 50 50))
  (with-translation [0 150]
    (rect 0 0 50 50)))
