(ns snippets.lights-camera.coordinates
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet model-x-y-z-s {:renderer :p2d}
  (let [gr3d (create-graphics 240 240 :p3d)
        gr-text (create-graphics 240 240 :java2d)]
    (with-graphics gr3d
      (background 255)
      (camera  50 25 13 0 0 0 0 0 -1)
      (stroke-weight 2)
      (stroke 255 0 0) ; red - X axis
      (line 0 0 0 20 0 0)
      (stroke 0 255 0) ; green - Y axis
      (line 0 0 0 0 20 0)
      (stroke 0 0 255) ; blue - Z axis
      (line 0 0 0 0 0 20)
      (stroke 255 0 0)
      (stroke-weight 5)
      (translate 10 20 5)
      (point 0 0 0)
      (let [x (model-x 0 0 0)
            y (model-y 0 0 0)
            z (model-z 0 0 0)]
        (with-graphics gr-text
          (background 255)
          (fill 0)
          (doseq [[ind capt val] [[1 "(model-x 0 0 0)" x]
                                  [2 "(model-y 0 0 0)" y]
                                  [3 "(model-z 0 0 0)" z]]]
            (text (str capt " is " val) 20 (* ind 20))))))
    (image gr3d 0 0)
    (image gr-text 250 0)))

(defsnippet screen-x-y-z-s {:renderer :p2d}
  (let [gr3d (create-graphics 240 240 :p3d)
        gr-text (create-graphics 240 240 :java2d)]
    (with-graphics gr3d
      (background 255)
      (camera  13 25 50 0 0 0 0 0 -1)
      (stroke-weight 2)
      (stroke 255 0 0) ; red - X axis
      (line 0 0 0 20 0 0)
      (stroke 0 255 0) ; green - Y axis
      (line 0 0 0 0 20 0)
      (stroke 0 0 255) ; blue - Z axis
      (line 0 0 0 0 0 20)
      (stroke 255 0 0)
      (stroke-weight 7)
      (point 0 0 0)
      (point 10 5 7)
      (let [x1 (screen-x 0 0)
            x2 (screen-x 10 5 7)
            y1 (screen-y 0 0)
            y2 (screen-y 10 5 7)
            z1 (model-z 0 0 0)
            z2 (model-z 10 5 7)]
        (with-graphics gr-text
          (background 255)
          (fill 0)
          (doseq [[ind capt val] [[1 "(screen-x 0 0)" x1]
                                  [2 "(screen-x 10 5 6)" x2]
                                  [3 "(screen-y 0 0)" y1]
                                  [4 "(screen-y 10 5 6)" y2]
                                  [5 "(screen-z 0 0 0)" x1]
                                  [6 "(screen-z 10 5 6)" z2]]]
            (text (str capt " is " val) 20 (* ind 20))))))
    (image gr3d 0 0)
    (image gr-text 250 0)))
