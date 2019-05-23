(ns quil.snippets.transform
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet apply-matrix
     "apply-matrix"
     {:renderer :p3d}

     (comment "setup camera")
     (q/camera 200 200 200 0 0 0 0 0 -1)
     (q/no-fill)

     (comment "draw box at [0 0 0]")
     (q/box 50)

     (comment "move positiion by [50 -50 0] and draw box")
     (q/apply-matrix 1 0 50
                     0 1 -50)
     (q/box 50)

     (comment "rotate position and move by [0 50 -50] and draw box")
     (let [s (q/sin 1)
           c (q/cos 1)]
       (q/apply-matrix 1 0 0 0
                       0 c s 50
                       0 (- s) c -50
                       0 0 0 1))
     (q/box 50)))

#?(:cljs
   (defsnippet apply-matrix
     "apply-matrix"
     {}

     (q/no-fill)
     (q/rect 10 10 50 50)
     (comment "move position by 100, 100 and shear")
     (q/apply-matrix 1 0 1 1 100 100)
     (q/rect 10 10 50 50)))

(defsnippet push-matrix-pop-matrix
  ["push-matrix" "pop-matrix"]
  {}

  (q/fill 255 0 0)
  (comment "draw rect at [20, 20]")
  (q/translate 20 20)
  (q/rect 0 0 50 50)

  (comment "move by 150 to right and save matrix")
  (q/translate 150 0)
  (q/push-matrix)

  (comment "rotate matrix and draw rect")
  (q/rotate 1)
  (q/rect 0 0 50 50)

  (comment "pop matrix should revert rotation")
  (q/pop-matrix)

  (comment "move by another 150 pixels and draw rect")
  (q/translate 150 0)
  (q/rect 0 0 50 50))

#?(:clj
   (defsnippet print-matrix
     "print-matrix"
     {}

     (q/translate 250 250)
     (q/rotate 1)
     (q/rect 0 0 100 100)
     (q/print-matrix)))

(defsnippet reset-matrix
  "reset-matrix"
  {}

  (q/rect 0 0 100 50)
  (q/translate 250 250)
  (q/rect 0 0 50 50)
  (comment "resetting brings us back to [0, 0]")
  (q/reset-matrix)
  (q/rect 0 0 50 100))

#?(:clj
   (defsnippet rotate
     "rotate"
     {:renderer :p3d}

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
     (q/pop-matrix)))

#?(:cljs
   (defsnippet rotate
     "rotate"
     {}

     (q/stroke 100)
     (q/no-fill)
     (q/rect 0 0 52 52)

     (q/translate (/ (q/width) 4) (/ (q/height) 4))
     (q/rotate (/ q/PI 3))
     (q/rect 0 0 52 52)))

(defsnippet rotate-x-y-z
  ["rotate-x" "rotate-y" "rotate-z"]
  {:renderer :p3d}

  (q/background 255)

  (comment "setup camera and draw box at [0 0 0]")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/push-matrix)

  (comment "move, rotate x axis and draw box")
  (q/translate 100 0 0)
  (q/rotate-x 0.5)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (comment "move, rotate y axis and draw box")
  (q/translate 0 100 0)
  (q/rotate-y 0.5)
  (q/box 50)
  (q/pop-matrix)
  (q/push-matrix)

  (comment "move, rotate z axis and draw box")
  (q/translate 0 0 100)
  (q/rotate-z 0.5)
  (q/box 50)
  (q/pop-matrix))

(defsnippet scale
  "scale"
  {:renderer :p3d}

  (q/background 255)

  (comment "setup camera and draw box at [0 0 0]")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)

  (comment "draw box 50% smaller")
  (q/with-translation [100 0 0]
    (q/scale 0.5)
    (q/box 50)
    (q/scale 2))

  (comment "draw box 50% narrower but same length/height")
  (q/with-translation [0 100 0]
    (q/scale 1 0.5)
    (q/box 50)
    (q/scale 1 2))

  (comment "draw box 50% shorter and 150% taller, but same width")
  (q/with-translation [0 0 100]
    (q/scale 0.5 1 1.5)
    (q/box 50)
    (q/scale 2 1 0.75)))

(defsnippet shear-x-y
  ["shear-x" "shear-y"]
  {}

  (comment "draw normal rect")
  (q/with-translation [125 125]
    (q/rect 0 0 100 50))

  (comment "draw rect sheared by y")
  (q/with-translation [375 125]
    (q/shear-y 0.5)
    (q/rect 0 0 100 50))

  (comment "draw rect sheared by x")
  (q/with-translation [125 375]
    (q/shear-x 0.5)
    (q/rect 0 0 100 50)))

(defsnippet translate
  "translate"
  {:renderer :p3d}

  (q/background 255)

  (comment "setup camera and draw box at [0 0 0]")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)

  (comment "draw 3 identical boxes in 3 different positions")
  (comment "using different translate calls")
  (q/translate 100 0)
  (q/box 50)
  (q/translate [-100 100])
  (q/box 50)
  (q/translate 0 -100 100)
  (q/box 50))

