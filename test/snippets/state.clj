(ns snippets.state
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet set-state-state-s
  {:setup (set-state! :text "I'm state!"
                      :year (year))}
  (fill 0)
  (text (state :text) 10 20)
  (text (str "Full state: " (state)) 10 40))
