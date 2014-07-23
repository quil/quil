#+clj
(ns snippets.image
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

#+cljs
(ns snippets.image)

#+clj
(defsnippet create-image {}
  (q/background 255)
  (let [im (q/create-image 100 100 :rgb)]
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    (q/image im 0 0)))
