(ns snippets.image
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet create-image-s {}
  (background 255)
  (let [im (create-image 100 100 :rgb)]
    (dotimes [x 100]
      (dotimes [y 100]
        (set-pixel im x y (color (* 2 x) (* 2 y) (+ x y)))))
    (image im 0 0)))
