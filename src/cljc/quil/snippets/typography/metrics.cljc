(ns quil.snippets.typography.metrics
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet text-ascent
  "text-ascent"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "Ascent is " (q/text-ascent)) 20 20))

(defsnippet text-descent
  "text-descent"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "Descent is " (q/text-descent)) 20 20))
