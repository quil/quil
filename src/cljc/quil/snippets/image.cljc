(ns quil.snippets.image
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet create-image
  ["create-image" "set-pixel"]
  {}

  (q/background 255)
  (comment "create image and draw gradient on it")
  (let [im (q/create-image 100 100 #?(:clj :rgb))]
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    #?(:cljs (q/update-pixels im))

    (comment "draw image twice")
    (q/image im 0 0)
    (q/image im 50 50)))

(defsnippet resize-image
  "resize"
  {}

  (comment "create image and draw gradient on it")
  (let [im (q/create-image 100 100 #?(:clj :rgb))]
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    #?(:cljs (q/update-pixels im))
    (q/image im 0 0)

    (comment "resize image from 100x100 to 50x50 and draw again")
    (q/resize im 50 50)
    (q/image im 100 100)))
