(ns quil.snippets.lights-camera.lights
  (:require #?(:cljs quil.snippet
               :clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true])
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

(defsnippet light-specular
  "light-specular"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/light-specular 100 100 255)
  (q/directional-light 255 150 150 -1 -0.76 -0.5)
  (q/box 50))

(defsnippet lights
  "lights"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/lights)
  (q/box 50))

(defsnippet no-lights
  "no-lights"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/directional-light 255 150 150 -1 -0.76 -0.5)
  (q/box 50)
  (q/no-lights)
  (q/translate 0 50 0)
  (q/sphere 20))

(defsnippet normal
  "normal"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 40 40 0 0 0 -1)
  (q/point-light 150 250 150 10 30 50)
  (q/begin-shape)
  (q/normal 0 0 1)
  (doseq [v [[20 20 -10]
             [80 20 10]
             [80 80 -10]
             [20 80 10]]]
    (apply q/vertex v))
  (q/end-shape :close))

(defsnippet point-light
  "point-light"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/point-light 255 150 150 100 100 100)
  (q/box 50))

(defsnippet spot-light
  "spot-light"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 100 100 100 0 0 0 0 0 -1)
  (q/spot-light 255 0 0 50 0 50 -1 0 -1 q/PI 1)
  (q/spot-light [0 0 255] [0 100 100] [0 -1 -1] q/PI 1)
  (q/box 50))

