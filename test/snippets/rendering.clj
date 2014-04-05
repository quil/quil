(ns snippets.rendering
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet hint-s {}
  (let [hints [:enable-native-fonts
               :disable-native-fonts
               :enable-depth-test
               :disable-depth-test
               :enable-depth-sort
               :disable-depth-sort
               :enable-opengl-errors
               :disable-opengl-errors
               :enable-depth-mask
               :disable-depth-mask
               :enable-optimized-stroke
               :disable-optimized-stroke
               :enable-retina-pixels
               :disable-retina-pixels
               :enable-stroke-perspective
               :disable-stroke-perspective
               :enable-stroke-pure
               :disable-stroke-pure
               :enable-texture-mipmaps
               :disable-texture-mipmaps]]
    (doseq [h hints]
      (hint h)))
  (ellipse 250 250 400 200))

(defsnippet with-graphics-s {}
  (let [gr (create-graphics 250 250)]
    (with-graphics gr
      (background 255)
      (fill 255 0 0)
      (triangle 50 30 220 120 20 180))
    (image gr 0 0)
    (image gr 250 0)
    (image gr 0 250)
    (image gr 250 250)))
