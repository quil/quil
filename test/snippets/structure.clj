(ns snippets.structure
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet push-style-pop-style-s {}
  (background 255)
  (fill 255 0 0)
  (ellipse 125 125 100 100)
  (push-style)
  (fill 0 0 255)
  (ellipse 250 250 100 100)
  (pop-style)
  (ellipse 375 375 100 100))
