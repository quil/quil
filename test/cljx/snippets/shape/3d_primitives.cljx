#+clj
(ns snippets.shape.3d-primitives
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

#+cljs
(ns snippets.shape.3d-primitives)

#+clj
(defsnippet box {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/with-translation [100 0 0]
    (q/box 70))
  (q/with-translation [0 100 0]
    (q/box 70 100 50)))

#+clj
(defsnippet sphere {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/sphere 150))

#+clj
(defsnippet sphere-detail {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/sphere-detail 30) ; default
  (q/with-translation [0 0 100]
    (q/sphere 70))
  (q/sphere-detail 15)
  (q/with-translation [100 0 0]
    (q/sphere 70))
  (q/sphere-detail 30 5)
  (q/with-translation [0 100 0]
    (q/sphere 70)))
