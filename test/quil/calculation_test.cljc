(ns quil.calculation-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.test-helpers :as qth :include-macros true]
   [quil.core :as q :include-macros true]))

(deftest absolute-value
  (is (= 1 (abs -1)))
  (qth/with-sketch (qth/test-sketch)
    (is (= 0 (q/abs 0)))
    (is (= 1 (q/abs -1)))
    (is (= 2 (q/abs 2)))
    (is (= 0.5 (q/abs -0.5)))
    (is (= 1.5 (q/abs 1.5)))
    (q/exit)))

(deftest ceiling
  (qth/with-sketch (qth/test-sketch)
    (is (= 10 (q/ceil 9.4)))
    (is (= 9 (q/ceil 8.01)))
    (is (= 8 (q/ceil 8)))
    (is (= 0 (q/ceil -0.1)))
    (is (= -1 (q/ceil -1.1)))
    (q/exit)))

(deftest constrain
  (qth/with-sketch (qth/test-sketch)
    (is (= 14 (q/constrain 14 10 20)))
    (is (= 10 (q/constrain 4 10 20)))
    (is (= 20 (q/constrain 23 10 20)))
    (is (= -7 (q/constrain -7 -10 -5)))
    (is (= -10 (q/constrain -11 -10 -5)))
    (is (= -5 (q/constrain -4 -10 -5)))
    (q/exit)))

(deftest dist
  (qth/with-sketch (qth/test-sketch)
    (is (= 2.0 (q/dist -1 0 1 0)))
    (is (= 4.0 (q/dist 0 -2 0 2)))
    (is (= 1.0 (q/dist 0 0 0 0 0 1)))
    (q/exit)))

(deftest exp
  (qth/with-sketch (qth/test-sketch)
    (is (= 1.0 (q/exp 0)))
    (is (qth/delta= Math/E (q/exp 1)))
    (q/exit)))

(deftest floor
  (qth/with-sketch (qth/test-sketch)
    (is (= 1 (q/floor 1.5)))
    (is (= 1 (q/floor 1.0)))
    (is (= -1 (q/floor -0.5)))
    (is (= -2 (q/floor -1.5)))
    (q/exit)))

(deftest lerp
  (qth/with-sketch (qth/test-sketch)
    (is (= 2.0 (q/lerp 2 4 0)))
    (is (= 0.0 (q/lerp 2 4 -1)))
    (is (= 10.0 (q/lerp 2 6 2)))
    (is (= 6.0 (q/lerp 4 8 0.5)))
    (is (= -1.0 (q/lerp -2 2 0.25)))
    (q/exit)))

(deftest log
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 1.0 (q/log Math/E)))
    (is (= 4.0 (/ (q/log 16) (q/log 2))))
    (q/exit)))

(deftest mag
  (qth/with-sketch (qth/test-sketch)
    (is (= 2.0 (q/mag 0 2)))
    (is (= 4.0 (q/mag 4 0)))
    (q/exit)))

(deftest map-range
  (qth/with-sketch (qth/test-sketch)
    (is (= 15.0 (q/map-range 4 0 8 10 20)))
    (q/exit)))

(deftest norm
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 0.2 (q/norm 2 0 10)))
    (is (qth/delta= 1.2 (q/norm 12 0 10)))
    (is (qth/delta= -0.2 (q/norm -2 0 10)))
    (q/exit)))

(deftest pow
  (qth/with-sketch (qth/test-sketch)
    (is (= 16.0 (q/pow 2 4)))
    (is (= 1024.0 (q/pow 2 10)))
    (q/exit)))

(deftest round
  (qth/with-sketch (qth/test-sketch)
    (is (= 1 (q/round 1.4)))
    (is (= 2 (q/round 1.5)))
    (is (= 2 (q/round 1.6)))
    (is (= -1 (q/round -1.4)))
    (is (= -1 (q/round -1.5)))
    (is (= -2 (q/round -1.6)))
    (q/exit)))

(deftest sq
  (qth/with-sketch (qth/test-sketch)
    (is (= 0.0 (q/sq 0)))
    (is (= 4.0 (q/sq -2)))
    (is (= 25.0 (q/sq 5)))
    (q/exit)))

(deftest sqrt
  (qth/with-sketch (qth/test-sketch)
    (is (= 5.0 (q/sqrt 25)))
    (is (qth/delta= 1.4142135 (q/sqrt 2)))
    (q/exit)))

(deftest fract
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 0.0 (q/fract -2.0)))
    (is (qth/delta= 0.55 (q/fract -2.45)))
    (is (qth/delta= 0.0 (q/fract 0.0)))
    (is (qth/delta= 0.0 (q/fract 0)))
    (is (qth/delta= 0.4 (q/fract 0.4)))
    (is (qth/delta= 0.0 (q/fract 1.0)))
    (is (qth/delta= 0.58 (q/fract 1.58)))
    (q/exit)))
