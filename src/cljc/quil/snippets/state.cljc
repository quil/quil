(ns quil.snippets.state
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet set-state-state
  ["set-state!" "state"]
  {:setup (q/set-state! :text "I'm state!"
                        :year (q/year))}

  (q/background 255)
  (q/fill 0)
  (q/text (q/state :text) 10 20)
  (q/text (str "Full state: " (q/state)) 10 40))
