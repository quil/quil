(ns quil.snippets.image
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet create-image
  "create-image"
  {}

  (q/background 255)
  (let [im (q/create-image 100 100 :rgb)]
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    (q/image im 0 0)
    (q/image im 50 50)))
