(ns quil.snippets.lights-camera.lights
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet ambient-light
  "ambient-light"
  {:renderer :p3d
   :settings (fn [] (q/smooth 4))}

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

;; HACK: delay-frames to try and account for SEGV failure in dispose on ubuntu-latest but not osx or cljs
;; C  0x00007f84e4000090
;; C  [libgallium-24.2.8-1ubuntu1~24.04.1.so+0x2dd78e]
;; C  [libjogl_desktop.so+0x62279]  Java_jogamp_opengl_gl4_GL4bcImpl_dispatch_1glDeleteBuffers1__ILjava_lang_Object_2IZJ+0xa2
;; j  jogamp.opengl.gl4.GL4bcImpl.dispatch_glDeleteBuffers1(ILjava/lang/Object;IZJ)V+0
;; j  jogamp.opengl.gl4.GL4bcImpl.glDeleteBuffers(ILjava/nio/IntBuffer;)V+90
;; j  processing.opengl.PJOGL.deleteBuffers(ILjava/nio/IntBuffer;)V+6
;; j  processing.opengl.PGraphicsOpenGL$GLResourceVertexBuffer.disposeNative()V+34
;; J 7927 c1 processing.opengl.PGraphicsOpenGL.drainRefQueueBounded()V (36 bytes) @ 0x00007f8569ef9f1c [0x00007f8569ef99a0+0x000000000000057c]
;; J 7838 c1 processing.opengl.PGraphicsOpenGL$Disposable.<init>(Ljava/lang/Object;)V (22 bytes) @ 0x00007f8569ecef6c [0x00007f8569eced80+0x00000000000001ec]
;; j  processing.opengl.PGraphicsOpenGL$GLResourceFrameBuffer.<init>(Lprocessing/opengl/FrameBuffer;)V+2
;; j  processing.opengl.FrameBuffer.allocate()V+21
;; j  processing.opengl.FrameBuffer.<init>(Lprocessing/opengl/PGraphicsOpenGL;IIIIIIZZ)V+209
;; j  processing.opengl.FrameBuffer.<init>(Lprocessing/opengl/PGraphicsOpenGL;IIZ)V+11
;; j  processing.opengl.PGraphicsOpenGL.beginOnscreenDraw()V+33
;; j  processing.opengl.PGraphicsOpenGL.beginDraw()V+139
;; j  processing.core.PApplet.handleDraw()V+52
;; j  quil.Applet.handleDraw()V+37
;;
;; This is pretty consistently causing a SEGV on github ci, but some other
;; snippet also causes a SEGV, so just disabling this is not sufficient.
(defsnippet point-light
  "point-light"
  {:renderer :p3d
   :delay-frames 10}

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

