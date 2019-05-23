(ns quil.snippets.shape.curves
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet bezier-2d
  "bezier"
  {}

  (q/no-fill)
  (q/with-translation [100 100]
    (q/bezier 0 0 50 100 100 -100 150 0)))

(defsnippet bezier-3d
  "bezier"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/bezier 0 0 0 0 100 0 0 100 0 100 0 0))

(defsnippet bezier-detail
  ["bezier-detail" "bezier"]
  {:renderer :p3d}

  (q/background 255)
  (q/camera 0 0 300 0 0 0 0 1 0)
  (q/no-fill)
  (q/bezier-detail 5)
  (q/bezier 0 0 0 0 100 0 0 100 0 100 0 0))

(defsnippet bezier-point
  "bezier-point"
  {}

  (comment "see https://p5js.org/reference/#/p5/bezierPoint for better example")
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/bezier-point 0 5 7 0 v)
          txt (str "(q/bezier-point 0 5 7 0 " v ") = " res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet bezier-tangent
  "bezier-tangent"
  {}

  (comment "see https://p5js.org/reference/#/p5/bezierTangent for better example")
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/bezier-tangent 0 5 7 0 v)
          txt (str "(q/bezier-tangent 0 5 7 0 " v ") = " res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve
  "curve"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/curve 0 0
           50 100
           100 -100
           150 0)
  (q/curve 0 0 0
           0 100 0
           0 0 100
           100 0 0))

(defsnippet curve-detail
  "curve-detail"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 0 0 300 0 0 0 0 1 0)
  (q/no-fill)
  (q/curve-detail 5)
  (q/curve 0 0 50 100 100 -100 150 0)
  (q/curve-detail 20)
  (q/curve 0 0 -50 100 -100 -100 -150 0))

(defsnippet curve-point
  "curve-point"
  {}

  (comment "see https://p5js.org/reference/#/p5/curvePoint for better example")
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/curve-point 0 5 7 0 v)
          txt (str "(q/curve-point 0 5 7 0 " v ") = " res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tangent
  "curve-tangent"
  {}

  (comment "see https://p5js.org/reference/#/p5/curveTangent for better example")
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/curve-tangent 0 5 7 0 v)
          txt (str "(q/curve-tangent 0 5 7 0 " v ") = " res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tightness
  "curve-tightness"
  {}

  (q/no-fill)
  (comment "try different tightnesses")
  (doseq [[ind t] [[0 -5] [1 -1] [2 0] [3 1] [4 5]]]
    (q/curve-tightness t)
    (q/with-translation [100 (+ 50 (* ind 70))]
      (q/curve 0 0 0 0 50 30 100 -30)
      (q/curve 0 0 50 30 100 -30 150 0)
      (q/curve 50 30 100 -30 150 0 150 0))))
