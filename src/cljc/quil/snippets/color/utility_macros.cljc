(ns quil.snippets.color.utility-macros
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet with-fill
  "with-fill"
  {}

  (comment "set fill to white and draw a square")
  (q/fill 255)
  (q/rect 30 30 60 60)

  (comment "set fill to red and draw 2 squares")
  (q/with-fill [255 0 0]
    (q/rect 60 60 90 90)
    (q/rect 90 90 120 120))

  (comment "draw square, it'll use white color")
  (q/rect 120 120 150 150))

(defsnippet with-stroke
  "with-stroke"
  {}

  (comment "set fill to white, stroke to black and draw a square")
  (q/fill 255)
  (q/stroke-weight 10)
  (q/stroke 0)
  (q/rect 30 30 60 60)

  (comment "set border to red and draw 2 squares")
  (q/with-stroke [255 0 0]
    (q/rect 60 60 90 90)
    (q/rect 90 90 120 120))

  (comment "draw another square, should have black borders")
  (q/rect 120 120 150 150))
