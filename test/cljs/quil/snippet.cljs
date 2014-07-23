(ns quil.snippet
  (:require [quil.core :as q :include-macros true]
            [dommy.utils :as utils]
            [dommy.core :as dommy])
  (:use-macros [quil.snippet :only [defsnippet]]
               [dommy.macros :only [node sel sel1]]))

(def test-data (atom (list)))

(def test-interval (atom nil))
(def test-indx (atom 0))

(def failed (atom 0))
(def total (atom 0))

(defn log [& body]
  (.log js/console (apply str body)))

(defn ^:export print-data []
  (.log js/console (str @test-data)))


(defn log-sketch-test [sketch]
  (log (str "Testing " (:ns sketch) "/" (:name sketch))))


(defn end-of-tests []
  (log "end of tests")
  (log "Total tested: " @total "   Accepted: " (- @total @failed) "   Failed: " @failed)

  (dommy/set-text! (sel1 :#results)
                   (str "Total tested: " @total ";   Accepted: " (- @total @failed) ";   Failed: " @failed))
  (reset! test-indx 0)
  (reset! failed 0)
  (reset! total 0)

  (reset! test-interval (js/clearInterval @test-interval)))

(defn run-single-test []
  (js/clearInterval @test-interval)
  (if (< @test-indx (count @test-data))
    (let [sketch (nth @test-data @test-indx)]
      (log-sketch-test sketch)
      (swap! total inc)
      ((:fn sketch))

      (swap! test-indx inc)
      (reset! test-interval (js/setInterval run-single-test 1000)))

    (reset! test-interval (js/setInterval end-of-tests 500))))


(defn ^:export run-tests []
  (log "run tests...")

  (dommy/set-text! (sel1 :#results) "Test started")
  (reset! test-indx 0)
  (reset! test-interval (js/setInterval run-single-test 100)))


(defsnippet hello2 {:size [300 300]}
  (q/background 90 102 0)
	(q/fill 124)
	(q/rect 56 46 55 55))


(defsnippet create-image {}
  (q/background 255)
  (let [im (q/create-image 100 100 :rgb)]
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    (q/image im 0 0)))
