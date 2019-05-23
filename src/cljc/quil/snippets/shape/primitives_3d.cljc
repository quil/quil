(ns quil.snippets.shape.primitives-3d
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet box
  "box"
  {:renderer :p3d}

  (q/background 255)

  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/with-translation [100 0 0]
    (q/box 70))
  (q/with-translation [0 100 0]
    (q/box 70 100 50)))

#?(:cljs
   (defsnippet cone
     "cone"
     {:renderer :p3d}

     (q/background 255)
     (q/rotate-x (* q/PI 1.25))
     (q/cone 80 140)))

#?(:cljs
   (defsnippet cylinder
     "cylinder"
     {:renderer :p3d}

     (q/background 255)
     (q/rotate-x (* q/PI 1.25))
     (q/cylinder 40 140)))

#?(:cljs
   (defsnippet ellipsoid
     "ellipsoid"
     {:renderer :p3d}

     (q/background 255)
     (q/ellipsoid 40 60 80)))

(defsnippet sphere
  "sphere"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/sphere 150))

#?(:clj
   (defsnippet sphere-detail
     "sphere-detail"
     {:renderer :p3d}

     (q/camera 200 200 200 0 0 0 0 0 -1)
     (q/sphere-detail 30) ; default
     (q/with-translation [0 0 100]
       (q/sphere 70))
     (q/sphere-detail 15)
     (q/with-translation [100 0 0]
       (q/sphere 70))
     (q/sphere-detail 30 5)
     (q/with-translation [0 100 0]
       (q/sphere 70))))

#?(:cljs
   (defsnippet torus
     "torus"
     {:renderer :p3d}

     (q/background 255)
     (q/rotate-x (/ q/PI 4))
     (q/torus 70 20)))
