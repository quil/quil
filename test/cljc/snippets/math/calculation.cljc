(ns snippets.math.calculation
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet abs {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/abs -1) = " (q/abs -1)) 10 20)
  (q/text (str "(q/abs -0.5) = " (q/abs -0.5)) 10 40))

(defsnippet ceil {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/ceil 9.03) = " (q/ceil 9.03)) 10 20))

(defsnippet constrain {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/constrain 4 10 20) = " (q/constrain 4 10 20)) 10 20)
  (q/text (str "(q/constrain 4.5 1.5 3.9) = " (q/constrain 4.5 1.5 3.9)) 10 40))

(defsnippet dist {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/dist 0 0 3 4) = " (q/dist 0 0 3 4)) 10 20)
  (q/text (str "(q/dist 0 0 0 5 5 5) = " (q/dist 0 0 0 5 5 5)) 10 40))

(defsnippet exp {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/exp 2) = " (q/exp 2)) 10 20))

(defsnippet floor {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/floor 9.03) = " (q/floor 9.03)) 10 20))

(defsnippet lerp {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/lerp 2 5 0.5) = " (q/lerp 2 5 0.5)) 10 20))

(defsnippet log {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/log Math/E) = " (q/log Math/E)) 10 20))

(defsnippet mag {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/mag 3 4) = " (q/mag 3 4)) 10 20)
  (q/text (str "(q/mag 3 4 5) = " (q/mag 3 4 5)) 10 40))

(defsnippet map-range {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/map-range 2 0 5 10 20) = " (q/map-range 2 0 5 10 20)) 10 20))

(defsnippet norm {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/norm 20 0 50) = " (q/norm 20 0 50)) 10 20))

(defsnippet pow {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/pow 2 10) = " (q/pow 2 10)) 10 20))

(defsnippet round {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/round 9.2) = " (q/round 9.2)) 10 20))

(defsnippet sq {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/sq 5) = " (q/sq 5)) 10 20))

(defsnippet sqrt {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/sqrt 25) = " (q/sqrt 25)) 10 20))
