(ns snippets.shape.3d-primitives
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet box-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (with-translation [100 0 0]
    (box 70))
  (with-translation [0 100 0]
    (box 70 100 50)))

(defsnippet sphere-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (sphere 150))

(defsnippet sphere-detail-s {:renderer :p3d}
  (camera 200 200 200 0 0 0 0 0 -1)
  (sphere-detail 30) ; default
  (with-translation [0 0 100]
    (sphere 70))
  (sphere-detail 15)
  (with-translation [100 0 0]
    (sphere 70))
  (sphere-detail 30 5)
  (with-translation [0 100 0]
    (sphere 70)))
