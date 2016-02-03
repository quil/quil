(ns snippets.lights-camera.coordinates
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet model-x-y-z {:renderer :p2d}
  (let [gr3d (q/create-graphics 240 240 :p3d)
        gr-text (q/create-graphics 240 240 :java2d)]
    (q/with-graphics gr3d
      (q/background 255)
      (q/camera  50 25 13 0 0 0 0 0 -1)
      (q/stroke-weight 2)
      (q/stroke 255 0 0) ; red - X axis
      (q/line 0 0 0 20 0 0)
      (q/stroke 0 255 0) ; green - Y axis
      (q/line 0 0 0 0 20 0)
      (q/stroke 0 0 255) ; blue - Z axis
      (q/line 0 0 0 0 0 20)
      (q/stroke 255 0 0)
      (q/stroke-weight 5)
      (q/translate 10 20 5)
      (q/point 0 0 0)
      (let [x (q/model-x 0 0 0)
            y (q/model-y 0 0 0)
            z (q/model-z 0 0 0)]
        (q/with-graphics gr-text
          (q/background 255)
          (q/fill 0)
          (doseq [[ind capt val] [[1 "(q/model-x 0 0 0)" x]
                                  [2 "(q/model-y 0 0 0)" y]
                                  [3 "(q/model-z 0 0 0)" z]]]
            (q/text (str capt " is " val) 20 (* ind 20))))))
    (q/image gr3d 0 0)
    (q/image gr-text 250 0)))

(defsnippet screen-x-y-z {:renderer :p2d}
  (let [gr3d (q/create-graphics 240 240 :p3d)
        gr-text (q/create-graphics 240 240 :java2d)]
    (q/with-graphics gr3d
      (q/background 255)
      (q/camera  13 25 50 0 0 0 0 0 -1)
      (q/stroke-weight 2)
      (q/stroke 255 0 0) ; red - X axis
      (q/line 0 0 0 20 0 0)
      (q/stroke 0 255 0) ; green - Y axis
      (q/line 0 0 0 0 20 0)
      (q/stroke 0 0 255) ; blue - Z axis
      (q/line 0 0 0 0 0 20)
      (q/stroke 255 0 0)
      (q/stroke-weight 7)
      (q/point 0 0 0)
      (q/point 10 5 7)
      (let [x1 (q/screen-x 0 0)
            x2 (q/screen-x 10 5 7)
            y1 (q/screen-y 0 0)
            y2 (q/screen-y 10 5 7)
            z1 (q/model-z 0 0 0)
            z2 (q/model-z 10 5 7)]
        (q/with-graphics gr-text
          (q/background 255)
          (q/fill 0)
          (doseq [[ind capt val] [[1 "(q/screen-x 0 0)" x1]
                                  [2 "(q/screen-x 10 5 6)" x2]
                                  [3 "(q/screen-y 0 0)" y1]
                                  [4 "(q/screen-y 10 5 6)" y2]
                                  [5 "(q/screen-z 0 0 0)" x1]
                                  [6 "(q/screen-z 10 5 6)" z2]]]
            (q/text (str capt " is " val) 20 (* ind 20))))))
    (q/image gr3d 0 0)
    (q/image gr-text 250 0)))
