(ns snippets.lights-camera.lights
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet ambient-light-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 1)
  (ambient-light 200 190 230)
  (sphere 50))

(defsnippet directional-light-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (directional-light 255 150 150 -1 -0.76 -0.5)
  (box 50))

(defsnippet light-falloff-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (light-falloff 1 0.008 0)
  (point-light 255 150 150 100 100 100)
  (box 50))

(defsnippet light-specular-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (light-specular 100 100 255)
  (directional-light 255 150 150 -1 -0.76 -0.5)
  (box 50))

(defsnippet lights-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (lights)
  (box 50))

(defsnippet no-lights-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (directional-light 255 150 150 -1 -0.76 -0.5)
  (box 50)
  (no-lights)
  (translate 0 50 0)
  (sphere 20))

(defsnippet normal-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 40 40 0 0 0 -1)
  (point-light 150 250 150 10 30 50)
  (begin-shape)
  (normal 0 0 1)
  (doseq [v [[20 20 -10]
             [80 20 10]
             [80 80 -10]
             [20 80 10]]]
    (apply vertex v))
  (end-shape :close))

(defsnippet point-light-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (point-light 255 150 150 100 100 100)
  (box 50))

(defsnippet spot-light-s {:renderer :p3d}
  (background 0)
  (camera 100 100 100 0 0 0 0 0 -1)
  (spot-light 255 0 0 50 0 50 -1 0 -1 PI 1)
  (spot-light [0 0 255] [0 100 100] [0 -1 -1] PI 1)
  (box 50))


