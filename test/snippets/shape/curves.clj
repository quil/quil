(ns snippets.shape.curves
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

(defsnippet bezier-s {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/bezier 0 0 50 100 100 -100 150 0)
  (q/bezier 0 0 0 0 100 0 0 100 0 100 0 0))

(defsnippet bezier-detail-s {:renderer :p3d}
  (q/camera 0 0 300 0 0 0 0 1 0)
  (q/no-fill)
  (q/bezier-detail 5)
  (q/bezier 0 0 50 100 100 -100 150 0)
  (q/bezier-detail 20)
  (q/bezier 0 0 -50 100 -100 -100 -150 0))

(defsnippet bezier-point-s {}
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/bezier-point 0 5 7 0 v)
          txt (format "(q/bezier-point 0 5 7 0 %s) = %s" v res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet bezier-tangent-s {}
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/bezier-tangent 0 5 7 0 v)
          txt (format "(q/bezier-tangent 0 5 7 0 %s) = %s" v res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-s {:renderer :p3d}
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

(defsnippet curve-detail-s {:renderer :p3d}
  (q/camera 0 0 300 0 0 0 0 1 0)
  (q/no-fill)
  (q/curve-detail 5)
  (q/curve 0 0 50 100 100 -100 150 0)
  (q/curve-detail 20)
  (q/curve 0 0 -50 100 -100 -100 -150 0))

(defsnippet curve-point-s {}
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/curve-point 0 5 7 0 v)
          txt (format "(q/curve-point 0 5 7 0 %s) = %s" v res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tangent-s {}
  (q/fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (q/curve-tangent 0 5 7 0 v)
          txt (format "(q/curve-tangent 0 5 7 0 %s) = %s" v res)]
      (q/text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tightness-s {}
  (q/no-fill)
  (doseq [[ind t] [[0 -5] [1 -1] [2 0] [3 1] [4 5]]]
    (q/curve-tightness t)
    (q/with-translation [100 (+ 50 (* ind 70))]
      (q/curve 0 0 0 0 50 30 100 -30)
      (q/curve 0 0 50 30 100 -30 150 0)
      (q/curve 50 30 100 -30 150 0 150 0))))
