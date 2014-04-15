(ns snippets.displaying.metrics
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet text-ascent-s {}
  (background 255)
  (fill 0)
  (text (str "Ascent is " (text-ascent)) 20 20))

(defsnippet text-descent-s {}
  (background 255)
  (fill 0)
  (text (str "Descent is " (text-descent)) 20 20))
