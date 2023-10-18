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
