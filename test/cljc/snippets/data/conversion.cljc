(ns snippets.data.conversion
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

; These snippets test non-graphic functions.
; Draw results as strings in sketch.

(defsnippet binary {}
  (q/background 255)
  (q/fill 0)
  (q/text (q/binary 42) 10 10)

  (q/text (q/binary 42 5) 10 30))

(defsnippet hex {}
  (q/background 255)
  (q/fill 0)
  (q/text (q/hex 42) 10 10)

  (q/text (q/hex 42 5) 10 30))

(defsnippet unbinary {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/unbinary "0101010")) 10 10))

(defsnippet unhex {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/unhex "2A")) 10 10))
