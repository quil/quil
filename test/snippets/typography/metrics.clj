(ns snippets.typography.metrics
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

(defsnippet text-ascent-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "Ascent is " (q/text-ascent)) 20 20))

(defsnippet text-descent-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "Descent is " (q/text-descent)) 20 20))
