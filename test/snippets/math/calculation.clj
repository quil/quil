(ns snippets.math.calculation
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet abs-s {}
  (background 255)
  (fill 0)
  (text (str "(abs -1) = " (abs -1)) 10 20)
  (text (str "(abs -0.5) = " (abs -0.5)) 10 40))

(defsnippet ceil-s {}
  (background 255)
  (fill 0)
  (text (str "(ceil 9.03) = " (ceil 9.03)) 10 20))

(defsnippet constrain-s {}
  (background 255)
  (fill 0)
  (text (str "(constrain 4 10 20) = " (constrain 4 10 20)) 10 20)
  (text (str "(constrain 4.5 1.5 3.9) = " (constrain 4.5 1.5 3.9)) 10 40))

(defsnippet dist-s {}
  (background 255)
  (fill 0)
  (text (str "(dist 0 0 3 4) = " (dist 0 0 3 4)) 10 20)
  (text (str "(dist 0 0 0 5 5 5) = " (dist 0 0 0 5 5 5)) 10 40))

(defsnippet exp-s {}
  (background 255)
  (fill 0)
  (text (str "(exp 2) = " (exp 2)) 10 20))

(defsnippet lerp-s {}
  (background 255)
  (fill 0)
  (text (str "(lerp 2 5 0.5) = " (lerp 2 5 0.5)) 10 20))

(defsnippet log-s {}
  (background 255)
  (fill 0)
  (text (str "(log Math/E) = " (log Math/E)) 10 20))

(defsnippet mag-s {}
  (background 255)
  (fill 0)
  (text (str "(mag 3 4) = " (mag 3 4)) 10 20)
  (text (str "(mag 3 4 5) = " (mag 3 4 5)) 10 40))

(defsnippet map-range-s {}
  (background 255)
  (fill 0)
  (text (str "(map-range 2 0 5 10 20) = " (map-range 2 0 5 10 20)) 10 20))

(defsnippet norm-s {}
  (background 255)
  (fill 0)
  (text (str "(norm 20 0 50) = " (norm 20 0 50)) 10 20))

(defsnippet pow-s {}
  (background 255)
  (fill 0)
  (text (str "(pow 2 10) = " (pow 2 10)) 10 20))

(defsnippet round-s {}
  (background 255)
  (fill 0)
  (text (str "(round 9.2) = " (round 9.2)) 10 20))

(defsnippet sq-s {}
  (background 255)
  (fill 0)
  (text (str "(sq 5) = " (sq 5)) 10 20))

(defsnippet sqrt-s {}
  (background 255)
  (fill 0)
  (text (str "(sqrt 25) = " (sqrt 25)) 10 20))
