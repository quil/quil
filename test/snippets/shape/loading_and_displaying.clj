(ns snippets.shape.loading-and-displaying
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet load-shape-s {:renderer :p2d}
  (let [sh (load-shape "http://upload.wikimedia.org/wikipedia/en/2/22/Heckert_GNU_white.svg")]
    (shape sh 0 0 500 500)))

(defsnippet shape-s {:renderer :p2d}
  (let [sh (load-shape "http://upload.wikimedia.org/wikipedia/en/2/22/Heckert_GNU_white.svg")]
    (shape sh)
    (shape sh 100 100)
    (shape sh 300 300 200 200)))

(defsnippet shape-mode-s {:renderer :p2d}
  (let [sh (load-shape "http://upload.wikimedia.org/wikipedia/en/2/22/Heckert_GNU_white.svg")]
    (stroke-weight 5)
    (stroke 255 0 0)

    (shape-mode :corner)
    (shape sh 20 20 200 200)
    (point 20 20)

    (shape-mode :corners)
    (shape sh 270 20 370 120)
    (point 270 20)
    (point 370 120)

    (shape-mode :center)
    (shape sh 100 350 150 150)
    (point 100 350)))
