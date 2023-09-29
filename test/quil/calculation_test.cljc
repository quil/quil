(ns quil.calculation-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   #_[quil.core :as q :include-macros true]
   ))

(deftest verify-browser
  (is (empty? (.-userAgent js/navigator)))
  (is (empty? (.-appName js/navigator)))
  (is (empty? (.-appVersion js/navigator))))

(deftest absolute-value
  (is (= 1 (abs -1)))
  #_(q/with-sketch (q/sketch :host "abs-test")
      (is (= 0 (q/abs 0)))
      (is (= 1 (q/abs -1)))
      (is (= 2 (q/abs 2)))
      (is (= 0.5 (q/abs -0.5)))
      (is (= 1.5 (q/abs 1.5)))
      (q/close)))
