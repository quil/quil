(ns snippets.color.setting
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet background-s {}
  (background 255 0 0)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 120))
    (image gr 0 0)

    (with-graphics gr
      (background 70 120))
    (image gr 70 70)

    (with-graphics gr
      (background 0 255 255))
    (image gr 140 140)

    (with-graphics gr
      (background 0 0 255 120))
    (image gr 210 210)))

(defsnippet background-image-s {}
  (let [gr (create-graphics (width) (height))]
    (with-graphics gr
      (background 0 120 120)
      (ellipse 250 250 300 300))

    (background-image gr)))

(defsnippet fill-s {}
  (background 0 0 255)

  (fill 120)
  (rect 0 0 100 100)

  (fill 80 120)
  (rect 70 70 100 100)

  (fill 0 255 0)
  (rect 140 140 100 100)

  (fill 255 0 0 120)
  (rect 210 210 100 100))

(defsnippet no-fill-s {}
  (background 255)

  (stroke 0)
  (fill 120)
  (rect 0 0 100 100)

  (no-fill)
  (rect 70 70 100 100))

(defsnippet no-stroke-s {}
  (background 255)

  (fill 120)
  (stroke 0)
  (rect 0 0 100 100)

  (no-stroke)
  (rect 70 70 100 100))

(defsnippet stroke-s {}
  (background 255)
  (stroke-weight 10)

  (stroke 120)
  (rect 0 0 100 100)

  (stroke 80 120)
  (rect 70 70 100 100)

  (stroke 0 255 0)
  (rect 140 140 100 100)

  (stroke 255 0 0 120)
  (rect 210 210 100 100))
