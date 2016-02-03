(ns snippets.math.trigonometry
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet acos {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/acos 0) = " (q/acos 0)) 10 20)
  (q/text (str "(q/acos 1) = " (q/acos 1)) 10 40))

(defsnippet asin {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/asin 0) = " (q/asin 0)) 10 20)
  (q/text (str "(q/asin 1) = " (q/asin 1)) 10 40))

(defsnippet atan {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/atan 0) = " (q/atan 0)) 10 20)
  (q/text (str "(q/atan 1) = " (q/atan 1)) 10 40))

(defsnippet atan2-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/atan2 2 1) = " (q/atan2 2 1)) 10 20)
  (q/text (str "(q/atan2 1 2) = " (q/atan2 1 2)) 10 40))

(defsnippet cos {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/cos 0) = " (q/cos 0)) 10 20)
  (q/text (str "(q/cos q/HALF-PI) = " (q/cos q/HALF-PI)) 10 40))

(defsnippet degrees {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/degrees 0) = " (q/degrees 0)) 10 20)
  (q/text (str "(q/degrees q/HALF-PI) = " (q/degrees q/HALF-PI)) 10 40))

(defsnippet radians {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/radians 0) = " (q/radians 0)) 10 20)
  (q/text (str "(q/radians 90) = " (q/radians 90)) 10 40))

(defsnippet sin {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/sin 0) = " (q/sin 0)) 10 20)
  (q/text (str "(q/sin q/HALF-PI) = " (q/sin q/HALF-PI)) 10 40))

(defsnippet tan {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/tan 0) = " (q/tan 0)) 10 20)
  (q/text (str "(q/tan  q/QUARTER-PI) = " (q/tan  q/QUARTER-PI)) 10 40))
