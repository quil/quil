(ns quil.snippets.input
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet keyboard
  ["key-as-keyword" "key-code" "key-coded?" "key-pressed?" "raw-key" "key-modifiers"]
  {}

  (q/background 255)
  (q/fill 0)
  (comment "iterate through all key-related functions")
  (comment "and print their values")
  (doseq [[ind capt fn] [[0 "key-as-keyword" q/key-as-keyword]
                         [1 "key-code" q/key-code]
                         [2 "key-coded?" #(q/key-coded? (q/raw-key))]
                         [3 "key-pressed?" q/key-pressed?]
                         [4 "raw-key" q/raw-key]
                         #?(:clj [5 "key-modifiers" q/key-modifiers])]]
    (q/text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))

(defsnippet mouse
  ["mouse-button" "mouse-pressed?" "mouse-x" "mouse-y" "pmouse-x" "pmouse-y"]
  {}

  (q/background 255)
  (q/fill 0)
  (comment "iterate through all mouse-related functions")
  (comment "and print their values")
  (doseq [[ind capt fn] [[0 "mouse-button" q/mouse-button]
                         [1 "mouse-pressed?" q/mouse-pressed?]
                         [2 "mouse-x" q/mouse-x]
                         [3 "mouse-y" q/mouse-y]
                         [4 "pmouse-x" q/pmouse-x]
                         [5 "pmouse-y" q/pmouse-y]]]
    (q/text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))

(defsnippet time-and-date
  ["millis" "seconds" "minute" "hour" "day" "month" "year"]
  {:skip-image-diff? true}

  (q/background 255)
  (q/fill 0)
  (comment "iterate through all time-related functions")
  (comment "and print their values")
  (doseq [[ind capt fn] [[0 "millis" q/millis]
                         [1 "seconds" q/seconds]
                         [2 "minute" q/minute]
                         [3 "hour" q/hour]
                         [4 "day" q/day]
                         [5 "month" q/month]
                         [6 "year" q/year]]]
    (q/text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))
