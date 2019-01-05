(ns quil.snippets.data.conversion
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

; These snippets test non-graphic functions.
; Draw results as strings in sketch.

(defsnippet binary
  "binary"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/binary 42) = " (q/binary 42)) 10 10)

  (q/text (str "(q/binary 42 5) = " (q/binary 42 5)) 10 30))

(defsnippet hex
  "hex"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/hex 42) = " (q/hex 42)) 10 10)

  (q/text (str "(q/hex 42 5) = " (q/hex 42 5)) 10 30))

(defsnippet unbinary
  "unbinary"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/unbinary \"0101010\") = " (q/unbinary "0101010")) 10 10))

(defsnippet unhex
  "unhex"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/unhex \"2A\") = " (q/unhex "2A")) 10 10))
