(ns quil.snippets.lights-camera.lights
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet ambient-light
  "ambient-light"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 1)
  (q/ambient-light 200 190 230)
  (q/sphere 50))

(defsnippet directional-light
  "directional-light"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)

  (comment "use red-ish color with direction [-1, -0.76, -0.5]")
  (q/directional-light 255 150 150 -1 -0.76 -0.5)
  (q/box 50))

(defsnippet light-falloff
  "light-falloff"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/light-falloff 1 0.008 0)
  (q/point-light 255 150 150 100 100 100)
  (q/box 50))

#?(:clj
   (defsnippet light-specular
     "light-specular"
     {:renderer :p3d}

     (q/background 0)
     (q/camera 100 100 100 0 0 0 0 0 -1)
     (q/light-specular 100 100 255)
     (q/directional-light 255 150 150 -1 -0.76 -0.5)
     (q/box 50)))

(defsnippet lights
  "lights"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/lights)
  (q/box 50))

#?(:clj
   (defsnippet no-lights
     "no-lights"
     {:renderer :p3d}

     (q/background 0)
     (q/camera 100 100 100 0 0 0 0 0 -1)
     (comment "draw box with lights")
     (q/directional-light 255 150 150 -1 -0.76 -0.5)
     (q/box 50)
     (comment "draw sphere without lights")
     (q/no-lights)
     (q/translate 0 50 0)
     (q/sphere 20)))

(defsnippet point-light
  "point-light"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (comment "set light from the same point as camera [100, 100, 100]")
  (q/point-light 255 150 150 100 100 100)
  (q/box 50))

#?(:clj
   (defsnippet spot-light
     "spot-light"
     {:renderer :p3d}

     (q/background 0)
     (q/camera 100 100 100 0 0 0 0 0 -1)

     (comment "use two different ways to call spot-light")
     (comment "one light is red and another is blue")
     (q/spot-light 255 0 0 50 0 50 -1 0 -1 q/PI 1)
     (q/spot-light [0 0 255] [0 100 100] [0 -1 -1] q/PI 1)
     (q/box 50)))

