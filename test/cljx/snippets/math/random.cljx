#+clj
(ns snippets.math.random
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

#+clj
(defsnippet noise {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20)
  (q/text (str "(q/noise 1 2) = " (q/noise 1 2)) 10 40)
  (q/text (str "(q/noise 50 10 3) = " (q/noise 50 10 3)) 10 60))

#+clj
(defsnippet noise-detail {}
  (q/background 255)
  (q/fill 0)
  (q/noise-detail 3)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20)
  (q/noise-detail 5 0.5)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 40))

#+clj
(defsnippet noise-seed {}
  (q/background 255)
  (q/fill 0)
  (q/noise-seed 42)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20))

#+clj
(defsnippet random {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random 42) = " (q/random 42)) 10 20)
  (q/text (str "(q/random Math/E q/PI) = " (q/random Math/E q/PI)) 10 40))

#+clj
(defsnippet random-gaussian {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random-gaussian) = " (q/random-gaussian)) 10 20))

#+clj
(defsnippet random-seed {}
  (q/background 255)
  (q/fill 0)
  (q/random-seed 42)
  (q/text (str "(q/random 42) = " (q/random 42)) 10 20))
