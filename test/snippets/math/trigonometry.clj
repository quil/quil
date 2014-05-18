(ns snippets.math.trigonometry
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet acos-s {}
  (background 255)
  (fill 0)
  (text (str "(acos 0) = " (acos 0)) 10 20)
  (text (str "(acos 1) = " (acos 1)) 10 40))

(defsnippet asin-s {}
  (background 255)
  (fill 0)
  (text (str "(asin 0) = " (asin 0)) 10 20)
  (text (str "(asin 1) = " (asin 1)) 10 40))

(defsnippet atan-s {}
  (background 255)
  (fill 0)
  (text (str "(atan 0) = " (atan 0)) 10 20)
  (text (str "(atan 1) = " (atan 1)) 10 40))

(defsnippet atan2-s {}
  (background 255)
  (fill 0)
  (text (str "(atan2 2 1) = " (atan2 2 1)) 10 20)
  (text (str "(atan2 1 2) = " (atan2 1 2)) 10 40))

(defsnippet cos-s {}
  (background 255)
  (fill 0)
  (text (str "(cos 0) = " (cos 0)) 10 20)
  (text (str "(cos HALF-PI) = " (cos HALF-PI)) 10 40))

(defsnippet degrees-s {}
  (background 255)
  (fill 0)
  (text (str "(degrees 0) = " (degrees 0)) 10 20)
  (text (str "(degrees HALF-PI) = " (degrees HALF-PI)) 10 40))

(defsnippet radians-s {}
  (background 255)
  (fill 0)
  (text (str "(radians 0) = " (radians 0)) 10 20)
  (text (str "(radians 90) = " (radians 90)) 10 40))

(defsnippet sin-s {}
  (background 255)
  (fill 0)
  (text (str "(sin 0) = " (sin 0)) 10 20)
  (text (str "(sin HALF-PI) = " (sin HALF-PI)) 10 40))

(defsnippet tan-s {}
  (background 255)
  (fill 0)
  (text (str "(tan 0) = " (tan 0)) 10 20)
  (text (str "(tan QUARTER-PI) = " (tan QUARTER-PI)) 10 40))
