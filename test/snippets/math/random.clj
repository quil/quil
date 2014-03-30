(ns snippets.math.random
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet noise-s {}
  (background 255)
  (fill 0)
  (text (str "(noise 42) = " (noise 42)) 10 20)
  (text (str "(noise 1 2) = " (noise 1 2)) 10 40)
  (text (str "(noise 50 10 3) = " (noise 50 10 3)) 10 60))

(defsnippet noise-detail-s {}
  (background 255)
  (fill 0)
  (noise-detail 3)
  (text (str "(noise 42) = " (noise 42)) 10 20)
  (noise-detail 5 0.5)
  (text (str "(noise 42) = " (noise 42)) 10 40))

(defsnippet noise-seed-s {}
  (background 255)
  (fill 0)
  (noise-seed 42)
  (text (str "(noise 42) = " (noise 42)) 10 20))

(defsnippet random-s {}
  (background 255)
  (fill 0)
  (text (str "(random 42) = " (random 42)) 10 20)
  (text (str "(random Math/E PI) = " (random Math/E PI)) 10 40))

(defsnippet random-gaussian-s {}
  (background 255)
  (fill 0)
  (text (str "(random-gaussian) = " (random-gaussian)) 10 20))

(defsnippet random-seed-s {}
  (background 255)
  (fill 0)
  (random-seed 42)
  (text (str "(random 42) = " (random 42)) 10 20))
