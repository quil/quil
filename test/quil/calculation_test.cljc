(ns quil.calculation-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.test-helpers :as qth]
   [quil.core :as q :include-macros true]))

(deftest absolute-value
  (is (= 1 (abs -1)))
  (q/with-sketch (qth/test-sketch)
    (is (= 0 (q/abs 0)))
    (is (= 1 (q/abs -1)))
    (is (= 2 (q/abs 2)))
    (is (= 0.5 (q/abs -0.5)))
    (is (= 1.5 (q/abs 1.5)))
    (q/exit)))
