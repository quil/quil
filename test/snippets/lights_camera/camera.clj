(ns snippets.lights-camera.camera
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet begin-camera-end-camera-camera-s {:renderer :p3d}
  (background 255)
  (begin-camera)
  (camera)
  (translate 250 0 -250)
  (rotate-z (/ PI 4))
  (rotate-x (/ PI 8))
  (rotate-y (/ PI -8))
  (end-camera)

  (stroke-weight 2)
  (stroke 0)
  (fill 127)
  (box 100)
  (stroke 255 0 0) ; red - X axis
  (line 0 0 0 100 0 0)
  (stroke 0 255 0) ; green - Y axis
  (line 0 0 0 0 100 0)
  (stroke 0 0 255) ; blue - Z axis
  (line 0 0 0 0 0 100))

(defsnippet camera-s {:renderer :p3d}
  (background 255)
  (camera 200 200 200 0 0 0 0 0 -1)

  (stroke-weight 2)
  (stroke 0)
  (fill 127)
  (box 100)
  (stroke 255 0 0) ; red - X axis
  (line 0 0 0 100 0 0)
  (stroke 0 255 0) ; green - Y axis
  (line 0 0 0 0 100 0)
  (stroke 0 0 255) ; blue - Z axis
  (line 0 0 0 0 0 100))

(defsnippet frustum-s {:renderer :p3d}
  (background 255)
  (camera 200 200 200 0 0 0 0 0 -1)
  (frustum -100 100 -100 100 200 330)

  (stroke-weight 2)
  (stroke 0)
  (fill 127)
  (box 100)
  (stroke 255 0 0) ; red - X axis
  (line 0 0 0 100 0 0)
  (stroke 0 255 0) ; green - Y axis
  (line 0 0 0 0 100 0)
  (stroke 0 0 255) ; blue - Z axis
  (line 0 0 0 0 0 100))

(defsnippet ortho-s {:renderer :p2d}
  (let [ortho-params [[] [0 300 0 300] [0 300 0 300 0 170]]
        pos [[0 0] [250 0] [127 250]]]
    (dotimes [ind (count ortho-params)]
      (let [gr (create-graphics 240 240 :p3d)]
        (with-graphics gr
          (background 255)
          (camera 100 100 100 0 0 0 0 0 -1)
          (apply ortho (nth ortho-params ind))
          (fill 127)
          (box 100))
        (apply image gr (nth pos ind))))))

(defsnippet perspective-s {:renderer :p2d}
  (let [perspective-params [[] [(/ PI 2) 0.5 50 300]]
        pos [[0 0] [250 0] [127 250]]]
    (dotimes [ind (count perspective-params)]
      (let [gr (create-graphics 240 240 :p3d)]
        (with-graphics gr
          (background 255)
          (camera 100 100 100 0 0 0 0 0 -1)
          (apply perspective (nth perspective-params ind))
          (fill 127)
          (box 100))
        (apply image gr (nth pos ind))))))

(defsnippet print-camera-s {:renderer :p3d}
  (print-camera))

(defsnippet print-projection-s {:renderer :p3d}
  (print-projection))
