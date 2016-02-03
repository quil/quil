(ns snippets.state
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet set-state-state
  {:setup (q/set-state! :text "I'm state!"
                      :year (q/year))}
  (q/fill 0)
  (q/text (q/state :text) 10 20)
  (q/text (str "Full state: " (q/state)) 10 40))
