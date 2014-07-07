(ns snippets.state
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

(defsnippet set-state-state-s
  {:setup (q/set-state! :text "I'm state!"
                      :year (q/year))}
  (q/fill 0)
  (q/text (q/state :text) 10 20)
  (q/text (str "Full state: " (q/state)) 10 40))
