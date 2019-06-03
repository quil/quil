(ns quil.snippets.rendering
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet hint
     "hint"
     {:renderer :p3d}

     (let [hints [:enable-async-saveframe
                  :disable-async-saveframe
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
                  :enable-stroke-perspective
                  :disable-stroke-perspective
                  :enable-stroke-pure
                  :disable-stroke-pure
                  :enable-texture-mipmaps
                  :disable-texture-mipmaps]]
       (doseq [h hints]
         (q/hint h)))
     (q/ellipse 250 250 400 200)))

(defsnippet with-graphics
  "with-graphics"
  {}

  (let [gr (q/create-graphics 250 250)]
    (q/with-graphics gr
      (q/background 255)
      (q/fill 255 0 0)
      (q/triangle 50 30 220 120 20 180))
    (q/image gr 0 0)
    (q/image gr 250 0)
    (q/image gr 0 250)
    (q/image gr 250 250)))

#?(:clj
   (defsnippet load-shader
     ["load-shader" "loaded?" "reset-shader" "shader"]
     {:renderer :p2d
      :skip-image-diff? true
      :setup (let [path (clojure.java.io/resource "SimpleShader.glsl")
                   shd (q/load-shader (.getPath path))]
               (q/set-state! :shader shd))}

     (let [gr (q/create-graphics 250 250)
           shd (q/state :shader)]
       (when (q/loaded? shd)
         (q/with-graphics gr
           (q/background 255)
           (q/fill 255 0 0)
           (q/triangle 50 30 220 120 20 180))
         (q/image gr 0 0)
         (q/image gr 250 0)
         (q/shader shd)
         (q/image gr 0 250)
         (q/reset-shader)
         (q/image gr 250 250)))))

#?(:cljs
   (defsnippet load-shader
     ["load-shader" "loaded?" "shader"]
     {:renderer :p3d
      :skip-image-diff? true
      :setup (let [shd (q/load-shader "shader.frag" "shader.vert")]
               (q/set-state! :shader shd))}

     (let [shd (q/state :shader)]
       (when (q/loaded? shd)
         (q/shader shd)
         (q/set-uniform shd "p" (array -0.74364388703 0.13182590421))
         (q/set-uniform shd "r" (* 1.5 (q/exp (* -6.5 (+ 1 (q/sin (/ (q/millis) 2000)))))))
         (q/quad -1 -1 1 -1 1 1 -1 1)))))

#?(:clj
   (defsnippet clip-no-clip
     ["clip" "no-clip"]
     {}

     (q/background 255)
     (q/fill 0)
     (comment "clip rendering so that triangle will be incomplete")
     (q/clip 50 100 100 100)
     (q/triangle 100 70 170 180 30 180)

     (comment "draw normal unclipped triangle")
     (q/no-clip)
     (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
       (q/triangle 100 70 170 180 30 180))))
