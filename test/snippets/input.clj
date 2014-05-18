(ns snippets.input
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet keyboard-s {}
  (background 255)
  (fill 0)
  (doseq [[ind capt fn] [[0 "key-as-keyword" key-as-keyword]
                         [1 "key-code" key-code]
                         [2 "key-coded?" #(key-coded? (raw-key))]
                         [3 "key-pressed?" key-pressed?]
                         [4 "raw-key" raw-key]]]
    (text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))

(defsnippet mouse-s {}
  (background 255)
  (fill 0)
  (doseq [[ind capt fn] [[0 "mouse-button" mouse-button]
                         [1 "mouse-pressed?" mouse-pressed?]
                         [2 "mouse-x" mouse-x]
                         [3 "mouse-y" mouse-y]
                         [4 "pmouse-x" pmouse-x]
                         [5 "pmouse-y" pmouse-y]]]
    (text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))

(defsnippet time-and-date-s {}
  (background 255)
  (fill 0)
  (doseq [[ind capt fn] [[0 "millis" millis]
                         [1 "seconds" seconds]
                         [2 "minute" minute]
                         [3 "hour" hour]
                         [4 "day" day]
                         [5 "month" month]
                         [6 "year" year]]]
    (text (str capt " " (fn)) 10 (+ (* 20 ind) 20))))
