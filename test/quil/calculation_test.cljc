(ns quil.calculation-test
  (:require
   [clojure.test :as t :refer [deftest is] :include-macros true]
   [quil.core :as q :include-macros true]))

#?(:cljs
   (defn make-canvas [id]
     (let [canvas (.createElement js/document "canvas")]
       (.setAttribute canvas "id" id)
       (.appendChild (.-body js/document) canvas)
       canvas)))

;; invocation works for cljs with browser, fix for CLJ invocation
(deftest absolute-value
  (is (= 1 (abs -1)))
  (q/with-sketch (q/sketch :host (make-canvas "abs-test"))
    (is (= 0 (q/abs 0)))
    (is (= 1 (q/abs -1)))
    (is (= 2 (q/abs 2)))
    (is (= 0.5 (q/abs -0.5)))
    (is (= 1.5 (q/abs 1.5)))
    (q/exit)))
