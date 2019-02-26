(ns quil.snippets.transform.utility-macros
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet with-rotation-2d
  "with-rotation"
  {}

  (q/background 255)
  (comment "draw rectangle without rotation")
  (q/with-translation [50 50]
    (q/rect 0 0 100 200))

  (comment "draw rectangle with 45° rotation")
  (q/with-translation [300 50]
    (q/with-rotation [(/ q/PI 4)]
      (q/rect 0 0 100 200))))

(defsnippet with-rotation-3d-around-x
  "with-rotation"
  {:renderer :p3d}

  (q/background 255)
  (q/stroke 0)
  (comment "set camera and draw XYZ axis")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/line 0 0 0 100 0 0)
  (q/line 0 0 0 0 100 0)
  (q/line 0 0 0 0 0 100)
  (q/fill 0)
  #?(:clj (q/text "x" 100 0 0))
  #?(:clj (q/text "y" 0 100 0))
  #?(:clj (q/text "z" 0 0 100))
  (q/no-fill)
  (q/stroke 255 0 0)

  (comment "draw box without rotation")
  (q/box 50)

  (comment "draw box with 45° rotation around ")
  (comment "[1, 0, 0] vector.")
  (q/with-translation [100 0 0]
    (q/with-rotation [(/ q/PI 4) 1 0 0]
      (q/box 50))))

(defsnippet with-rotation-3d-around-xy
  "with-rotation"
  {:renderer :p3d}

  (q/background 255)
  (q/stroke 0)
  (comment "set camera and draw XYZ axis")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/line 0 0 0 100 0 0)
  (q/line 0 0 0 0 100 0)
  (q/line 0 0 0 0 0 100)
  (q/fill 0)
  #?(:clj (q/text "x" 100 0 0))
  #?(:clj (q/text "y" 0 100 0))
  #?(:clj (q/text "z" 0 0 100))
  (q/no-fill)
  (q/stroke 255 0 0)

  (comment "draw box without rotation")
  (q/box 50)

  (comment "draw box with 45° rotation around ")
  (comment "[1, 1, 0] vector.")
  (q/with-translation [100 0 0]
    (q/with-rotation [(/ q/PI 4) 1 1 0]
      (q/box 50))))

(defsnippet with-translation-2d
  "with-translation"
  {}

  (q/background 255)
  (q/rect 0 0 50 50)
  (q/with-translation [150 0]
    (q/rect 0 0 50 50))
  (q/with-translation [150 150]
    (q/rect 0 0 50 50)))

(defsnippet with-translation-3d
  "with-translation"
  {:renderer :p3d}

  (q/background 255)
  (q/stroke 0)
  (comment "set camera and draw XYZ axis")
  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/line 0 0 0 100 0 0)
  (q/line 0 0 0 0 100 0)
  (q/line 0 0 0 0 0 100)
  (q/fill 0)
  #?(:clj (q/text "x" 100 0 0))
  #?(:clj (q/text "y" 0 100 0))
  #?(:clj (q/text "z" 0 0 100))
  (q/no-fill)
  (q/stroke 255 0 0)

  (comment "draw 3 boxes using with-translation")
  (q/box 50)

  (q/with-translation [100 0 0]
    (q/box 50))

  (q/with-translation [100 0 100]
    (q/box 50)))
