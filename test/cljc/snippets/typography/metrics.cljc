(ns snippets.typography.metrics
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet text-ascent {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "Ascent is " (q/text-ascent)) 20 20))

(defsnippet text-descent {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "Descent is " (q/text-descent)) 20 20))
