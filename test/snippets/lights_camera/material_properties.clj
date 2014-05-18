(ns snippets.lights-camera.material-properties
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet ambient-s {:renderer :p3d}
  (background 0)
  (camera 150 150 150 0 0 0 0 0 -1)
  (ambient-light 255 127 127)
  (ambient 255 127 0)
  (box 100)
  (translate 0 100 0)
  (ambient 127)
  (sphere 40))

(defsnippet emissive-s {:renderer :p3d}
  (background 0)
  (camera 150 150 150 0 0 0 0 0 -1)
  (ambient-light 255 127 127)
  (emissive 0 0 255)
  (box 100)
  (translate 0 100 0)
  (emissive (color 0 127 0))
  (sphere 40))

(defsnippet shininess-s {:renderer :p3d}
  (background 0)
  (camera 150 150 150 0 25 0 0 0 -1)
  (fill 127 0 255)
  (no-stroke)
  (light-specular  204 204 204)
  (directional-light 102 102 102 -1 -1 -1)
  (specular 255 255 255)
  (shininess 2)
  (sphere 50)
  (translate 0 100 0)
  (shininess 10)
  (sphere 40))

(defsnippet specular-s {:renderer :p3d}
  (background 0)
  (camera 150 150 150 0 25 0 0 0 -1)
  (no-stroke)
  (fill 0 51 102)
  (light-specular  255 255 255)
  (directional-light 204 204 204 -1 -1 -1)
  (specular 255)
  (sphere 50)
  (translate 0 100 0)
  (specular 204 102 0)
  (sphere 40))
