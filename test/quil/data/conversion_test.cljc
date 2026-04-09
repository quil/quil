(ns quil.data.conversion-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.test-helpers :as qth :include-macros true]
   [quil.core :as q :include-macros true]))

(deftest binary
  (qth/with-test-sketch (qth/test-sketch)
    (is (= #?(:clj "00000000000000000000000000101010"
              :cljs "101010")
           (q/binary 42)))
    (is (= "01010" (q/binary 42 5)) "limit length")))

(deftest hex
  (qth/with-test-sketch (qth/test-sketch)
    (is (= "0000002A" (q/hex 42)))
    (is (= "02A" (q/hex 42 3)) "limit length")))

(deftest unbinary
  (qth/with-test-sketch (qth/test-sketch)
    (is (= 42 (q/unbinary "0101010")))))

(deftest unhex
  (qth/with-test-sketch (qth/test-sketch)
    (is (= 42 (q/unhex "2A")))))
