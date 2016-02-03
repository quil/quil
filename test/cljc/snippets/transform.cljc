(ns snippets.transform
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet apply-matrix {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  #?(:clj (q/apply-matrix 1 0 50
                          0 1 -50))
  #?(:clj (q/box 50))
  (let [s (q/sin 1)
        c (q/cos 1)]
   (q/apply-matrix 1 0 0 0
                   0 c s 50
                   0 (- s) c -50
                   0 0 0 1))
  (q/box 50))

(defsnippet push-matrix-pop-matrix {}
  (q/fill 255 0 0)
  (q/translate 20 20)
  (q/rect 0 0 50 50)
  (q/translate 150 0)
  (q/push-matrix)
  (q/rotate 1)
  (q/rect 0 0 50 50)
  (q/pop-matrix)
  (q/translate 150 0)
  (q/rect 0 0 50 50))

(defsnippet print-matrix {}
  (q/translate 250 250)
  (q/rotate 1)
  (q/rect 0 0 100 100)
  (q/print-matrix))

(defsnippet reset-matrix {}
  (q/rect 0 0 100 50)
  (q/translate 250 250)
  (q/rect 0 0 50 50)
  (q/reset-matrix)
  (q/rect 0 0 50 100))

(defsnippet rotate {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/push-matrix)

  (q/translate 100 0 0)
  (q/rotate 0.5)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (q/translate 0 100 0)
  (q/rotate 0.5 1 0 0)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (q/translate 0 0 100)
  (q/rotate 0.5 0 1 0)
  (q/box 50)
  (q/pop-matrix))

(defsnippet rotate-x-y-z {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/push-matrix)

  (q/translate 100 0 0)
  (q/rotate-x 0.5)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (q/translate 0 100 0)
  (q/rotate-y 0.5)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (q/translate 0 0 100)
  (q/rotate-z 0.5)
  (q/box 50)
  (q/pop-matrix))

(defsnippet scale {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/with-translation [100 0 0]
    (q/scale 0.5)
    (q/box 50)
    (q/scale 2))

  (q/with-translation [0 100 0]
    (q/scale 1 0.5)
    (q/box 50)
    (q/scale 1 2))

  (q/with-translation [0 0 100]
    (q/scale 0.5 1 1.5)
    (q/box 50)
    (q/scale 2 1 0.75)))

(defsnippet shear-x-y {}
  (q/with-translation [125 125]
    (q/rect 0 0 100 50))
  (q/with-translation [375 125]
    (q/shear-y 0.5)
    (q/rect 0 0 100 50))
  (q/with-translation [125 375]
    (q/shear-x 0.5)
    (q/rect 0 0 100 50)))

(defsnippet translate {:renderer :p3d}
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/translate 100 0)
  (q/box 50)
  (q/translate [-100 100])
  (q/box 50)
  (q/translate 0 -100 100)
  (q/box 50))

