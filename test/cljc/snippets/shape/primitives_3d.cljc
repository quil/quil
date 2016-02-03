(ns snippets.shape.primitives-3d
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet box {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/with-translation [100 0 0]
    (q/box 70))
  (q/with-translation [0 100 0]
    (q/box 70 100 50)))

(defsnippet sphere {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/sphere 150))

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
