(ns snippets.lights-camera.camera
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet begin-camera-end-camera-camera {:renderer :p3d}
  (q/background 255)
  (q/begin-camera)
  (q/camera)
  (q/translate 250 0 -250)
  (q/rotate-z (/ q/PI 4))
  (q/rotate-x (/ q/PI 8))
  (q/rotate-y (/ q/PI -8))
  (q/end-camera)

  (q/stroke-weight 2)
  (q/stroke 0)
  (q/fill 127)
  (q/box 100)
  (q/stroke 255 0 0) ; red - X axis
  (q/line 0 0 0 100 0 0)
  (q/stroke 0 255 0) ; green - Y axis
  (q/line 0 0 0 0 100 0)
  (q/stroke 0 0 255) ; blue - Z axis
  (q/line 0 0 0 0 0 100))

(defsnippet camera {:renderer :p3d}
  (q/background 255)
  (q/camera 200 200 200 0 0 0 0 0 -1)

  (q/stroke-weight 2)
  (q/stroke 0)
  (q/fill 127)
  (q/box 100)
  (q/stroke 255 0 0) ; red - X axis
  (q/line 0 0 0 100 0 0)
  (q/stroke 0 255 0) ; green - Y axis
  (q/line 0 0 0 0 100 0)
  (q/stroke 0 0 255) ; blue - Z axis
  (q/line 0 0 0 0 0 100))

(defsnippet frustum {:renderer :p3d}
  (q/background 255)
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/frustum -100 100 -100 100 200 330)

  (q/stroke-weight 2)
  (q/stroke 0)
  (q/fill 127)
  (q/box 100)
  (q/stroke 255 0 0) ; red - X axis
  (q/line 0 0 0 100 0 0)
  (q/stroke 0 255 0) ; green - Y axis
  (q/line 0 0 0 0 100 0)
  (q/stroke 0 0 255) ; blue - Z axis
  (q/line 0 0 0 0 0 100))

(defsnippet ortho {:renderer :p2d}
  (let [ortho-params [[] [0 300 0 300] [0 300 0 300 0 170]]
        pos [[0 0] [250 0] [127 250]]]
    (dotimes [ind (count ortho-params)]
      (let [gr (q/create-graphics 240 240 :p3d)]
        (q/with-graphics gr
          (q/background 255)
          (q/camera 100 100 100 0 0 0 0 0 -1)
          (apply q/ortho (nth ortho-params ind))
          (q/fill 127)
          (q/box 100))
        (apply q/image gr (nth pos ind))))))

(defsnippet perspective {:renderer :p2d}
  (let [perspective-params [[] [(/ q/PI 2) 0.5 50 300]]
        pos [[0 0] [250 0] [127 250]]]
    (dotimes [ind (count perspective-params)]
      (let [gr (q/create-graphics 240 240 :p3d)]
        (q/with-graphics gr
          (q/background 255)
          (q/camera 100 100 100 0 0 0 0 0 -1)
          (apply q/perspective (nth perspective-params ind))
          (q/fill 127)
          (q/box 100))
        (apply q/image gr (nth pos ind))))))

(defsnippet print-camera {:renderer :p3d}
  (q/print-camera))

(defsnippet print-projection {:renderer :p3d}
  (q/print-projection))
