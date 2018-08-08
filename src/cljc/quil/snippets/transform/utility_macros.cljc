(ns quil.snippets.transform.utility-macros
  (:require #?(:cljs quil.snippet
               :clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
quil.snippets.all-snippets)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet with-rotation
  "with-rotation"
  {:renderer :p3d}

  (q/camera 200 200 200 0 0 0 0 0 -1)
  (q/no-fill)
  (q/box 50)
  (q/translate 0 0 100)
  (q/with-rotation [1]
    (q/box 50))

  (q/translate 100 0 -100)
  (q/with-rotation [1 1 0 0]
    (q/box 50))

  (q/translate -100 100 0)
  (q/with-rotation [1 0 1 0]
    (q/box 50)))

(defsnippet with-translation
  "with-translation"
  {}

  (q/translate 10 10)
  (q/rect 0 0 50 50)
  (q/with-translation [150 0]
    (q/rect 0 0 50 50))
  (q/with-translation [0 150]
    (q/rect 0 0 50 50)))
