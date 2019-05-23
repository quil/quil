(ns quil.snippets.lights-camera.material-properties
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet ambient
  "ambient"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 150 150 150 0 0 0 0 0 -1)
  (comment "set light to kinda pink")
  (q/ambient-light 255 127 127)

  (comment "first box has full reflectance")
  (q/ambient 255)
  (q/translate 0 -50 0)
  (q/box 50)

  (comment "second box has abmient material with blue-only component")
  (q/translate 0 75 0)
  (q/ambient 0 0 255)
  (q/box 50)

  (comment "third box has gray ambient material")
  (q/translate 0 75 0)
  (q/ambient 127)
  (q/box 50))

#?(:clj
   (defsnippet emissive
     "emissive"
     {:renderer :p3d}

     (q/background 0)
     (q/camera 150 150 150 0 0 0 0 0 -1)
     (q/ambient-light 255 127 127)
     (q/emissive 0 0 255)
     (q/box 100)
     (q/translate 0 100 0)
     (q/emissive (q/color 0 127 0))
     (q/sphere 40)))

(defsnippet shininess
  "shininess"
  {:renderer :p3d}

  (q/background 0)
  (q/camera 150 150 150 0 25 0 0 0 -1)
  (q/fill 127 0 255)
  (q/no-stroke)
  #?(:clj (q/light-specular 204 204 204))
  (q/directional-light 102 102 102 -1 -1 -1)
  (q/specular 255 255 255)
  (comment "draw two spheres with different shininess")
  (q/shininess 2)
  (q/sphere 50)
  (q/translate 0 100 0)
  (q/shininess 10)
  (q/sphere 40))

(defsnippet specular
  "specular"
  {:renderer :p3d}

  (q/fill 255)
  (q/background 0)
  (q/camera 150 150 150 0 25 0 0 0 -1)
  (q/ambient-light 60 60 60)
  (q/no-stroke)
  #?(:clj (q/fill 0 51 102))
  #?(:clj (q/light-specular  255 255 255))
  #?(:clj (q/directional-light 255 255 255 0 0 -1)
     :cljs (q/point-light 25 255 255 150 150 150))
  (q/specular 255)
  (q/sphere 50)
  (q/translate 0 100 0)
  (q/specular 204 0 0)
  (q/sphere 40))
