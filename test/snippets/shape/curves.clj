(ns snippets.shape.curves
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet bezier-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (bezier 0 0 50 100 100 -100 150 0)
  (bezier 0 0 0 0 100 0 0 100 0 100 0 0))

(defsnippet bezier-detail-s {:renderer :p3d}
  (camera 0 0 300 0 0 0 0 1 0)
  (no-fill)
  (bezier-detail 5)
  (bezier 0 0 50 100 100 -100 150 0)
  (bezier-detail 20)
  (bezier 0 0 -50 100 -100 -100 -150 0))

(defsnippet bezier-point-s {}
  (fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (bezier-point 0 5 7 0 v)
          txt (format "(bezier-point 0 5 7 0 %s) = %s" v res)]
      (text txt 10 (+ 20 (* i 20))))))

(defsnippet bezier-tangent-s {}
  (fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (bezier-tangent 0 5 7 0 v)
          txt (format "(bezier-tangent 0 5 7 0 %s) = %s" v res)]
      (text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (no-fill)
  (curve 0 0
         50 100
         100 -100
         150 0)
  (curve 0 0 0
         0 100 0
         0 0 100
         100 0 0))

(defsnippet curve-detail-s {:renderer :p3d}
  (camera 0 0 300 0 0 0 0 1 0)
  (no-fill)
  (curve-detail 5)
  (curve 0 0 50 100 100 -100 150 0)
  (curve-detail 20)
  (curve 0 0 -50 100 -100 -100 -150 0))

(defsnippet curve-point-s {}
  (fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (curve-point 0 5 7 0 v)
          txt (format "(curve-point 0 5 7 0 %s) = %s" v res)]
      (text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tangent-s {}
  (fill 0)
  (dotimes [i 5]
    (let [v (/ i 4)
          res (curve-tangent 0 5 7 0 v)
          txt (format "(curve-tangent 0 5 7 0 %s) = %s" v res)]
      (text txt 10 (+ 20 (* i 20))))))

(defsnippet curve-tightness-s {}
  (no-fill)
  (doseq [[ind t] [[0 -5] [1 -1] [2 0] [3 1] [4 5]]]
    (curve-tightness t)
    (with-translation [100 (+ 50 (* ind 70))]
      (curve 0 0 0 0 50 30 100 -30)
      (curve 0 0 50 30 100 -30 150 0)
      (curve 50 30 100 -30 150 0 150 0))))
