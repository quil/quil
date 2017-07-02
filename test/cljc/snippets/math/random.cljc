(ns snippets.math.random
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet noise {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20)
  (q/text (str "(q/noise 1 2) = " (q/noise 1 2)) 10 40)
  (q/text (str "(q/noise 50 10 3) = " (q/noise 50 10 3)) 10 60))

(defsnippet noise-detail {}
  (q/background 255)
  (q/fill 0)
  (q/noise-detail 3)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20)
  (q/noise-detail 5 0.5)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 40))

(defsnippet noise-seed {}
  (q/background 255)
  (q/fill 0)
  (q/noise-seed 42)
  (q/text (str "(q/noise 42) = " (q/noise 42)) 10 20))

(defsnippet random {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random 42) = " (q/random 42)) 10 20)
  (q/text (str "(q/random Math/E q/PI) = " (q/random Math/E q/PI)) 10 40))

(defsnippet random-gaussian {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random-gaussian) = " (q/random-gaussian)) 10 20))

(defsnippet random-seed {}
  (q/background 255)
  (q/fill 0)
  (q/random-seed 42)
  (q/text (str "(q/random 42) = " (q/random 42)) 10 20))

(defn- unit-vector? [v]
  (let [n (->> v (map q/sq) (apply +))]
    (< (Math/abs (- n 1.0)) 0.001)))

(defsnippet random-2d {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random-2d) = " (q/random-2d)) 10 20)
  (dotimes [_ 100]
    (when-not (unit-vector? (q/random-2d))
      (throw (ex-info "random-2d doesn't return a unit vector" {})))))

(defsnippet random-3d {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/random-3d) = " (q/random-3d)) 10 20)
  (dotimes [_ 100]
    (when-not (unit-vector? (q/random-3d))
      (throw (ex-info "random-3d doesn't return a unit vector" {})))))
