(ns snippets.rendering
  (:require #+cljs quil.snippet
            #+clj [quil.snippet :refer [defsnippet]]
            [quil.core :as q :include-macros true])
  #+cljs
  (:use-macros [quil.snippet :only [defsnippet]]))

(defsnippet hint {}
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
      (q/hint h)))
  (q/ellipse 250 250 400 200))

(defsnippet with-graphics {}
  (let [gr (q/create-graphics 250 250)]
    (q/with-graphics gr
      (q/background 255)
      (q/fill 255 0 0)
      (q/triangle 50 30 220 120 20 180))
    (q/image gr 0 0)
    (q/image gr 250 0)
    (q/image gr 0 250)
    (q/image gr 250 250)))

#+clj
(defsnippet load-shader {:renderer :p2d}
  (let [gr (q/create-graphics 250 250)
        path (clojure.java.io/resource "SimpleShader.glsl")
        shd (q/load-shader (str path))]
    (q/with-graphics gr
      (q/background 255)
      (q/fill 255 0 0)
      (q/triangle 50 30 220 120 20 180))
    (q/image gr 0 0)
    (q/image gr 250 0)
    (q/shader shd)
    (q/image gr 0 250)
    (q/reset-shader)
    (q/image gr 250 250)))
