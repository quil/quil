(ns snippets.color.creating-and-reading
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

; These snippets test non-graphic functions.
; Draw results as strings in sketch.

(defsnippet binary-s {}
  (background 255)
  (fill 0)
  (text (binary 42) 10 10)

  (text (binary 42 5) 10 30))

(defsnippet hex-s {}
  (background 255)
  (fill 0)
  (text (hex 42) 10 10)

  (text (hex 42 5) 10 30))

(defsnippet unbinary-s {}
  (background 255)
  (fill 0)
  (text (str (unbinary "0101010")) 10 10))

(defsnippet unhex-s {}
  (background 255)
  (fill 0)
  (text (str (unhex "2A")) 10 10))
