(ns quil.shape.curve-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.test-helpers :as qth :include-macros true]
   [quil.core :as q :include-macros true]))

(deftest bezier-point
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 0.0 (q/bezier-point 0 5 7 0 0)))
    (is (qth/delta= 3.09375 (q/bezier-point 0 5 7 0 (/ 1.0 4))))
    (is (qth/delta= 4.5 (q/bezier-point 0 5 7 0 (/ 2.0 4))))
    (is (qth/delta= 3.65625 (q/bezier-point 0 5 7 0 (/ 3.0 4))))
    (is (qth/delta= 0.0 (q/bezier-point 0 5 7 0 (/ 4.0 4))))
    (q/exit)))

(deftest bezier-tangent
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 15.0 (q/bezier-tangent 0 5 7 0 0)))
    (is (qth/delta= 9.375 (q/bezier-tangent 0 5 7 0 (/ 1.0 4))))
    (is (qth/delta= 1.5 (q/bezier-tangent 0 5 7 0 (/ 2.0 4))))
    (is (qth/delta= -8.625 (q/bezier-tangent 0 5 7 0 (/ 3.0 4))))
    (is (qth/delta= -21.0 (q/bezier-tangent 0 5 7 0 (/ 4.0 4))))
    (q/exit)))

(deftest curve-point
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 5.0 (q/curve-point 0 5 7 0 0)))
    (is (qth/delta= 5.921875 (q/curve-point 0 5 7 0 (/ 1.0 4))))
    (is (qth/delta= 6.75 (q/curve-point 0 5 7 0 (/ 2.0 4))))
    (is (qth/delta= 7.203125 (q/curve-point 0 5 7 0 (/ 3.0 4))))
    (is (qth/delta= 7.0 (q/curve-point 0 5 7 0 (/ 4.0 4))))
    (q/exit)))

(deftest curve-tangent
  (qth/with-sketch (qth/test-sketch)
    (is (qth/delta= 3.5 (q/curve-tangent 0 5 7 0 0)))
    (is (qth/delta= 3.6875 (q/curve-tangent 0 5 7 0 (/ 1.0 4))))
    (is (qth/delta= 2.75 (q/curve-tangent 0 5 7 0 (/ 2.0 4))))
    (is (qth/delta= 0.6875 (q/curve-tangent 0 5 7 0 (/ 3.0 4))))
    (is (qth/delta= -2.5 (q/curve-tangent 0 5 7 0 (/ 4.0 4))))
    (q/exit)))
