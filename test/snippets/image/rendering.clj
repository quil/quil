(ns snippets.image.rendering
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet blend-mode-s {:renderer :p2d}
  (background 255)
  (let [modes [:replace :blend :add :subtract :darkest
               :lightest :exclusion :multiply :screen]
        splitted (partition-all 4 modes)]
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              gr (create-graphics 100 100 :p2d)]
          (with-graphics gr
            (background 127)
            (blend-mode mode)
            (no-stroke)
            (fill 255 0 0)
            (rect 10 20 80 20)
            (fill 50 170 255 127)
            (rect 60 10 20 80)
            (fill 200 130 150 200)
            (rect 10 60 80 20)
            (fill 20 240 50 50)
            (rect 20 10 20 80))
          (image gr (* col 120) (* row 120)))))))

(defsnippet create-graphics-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 127)
      (ellipse 50 50 80 40))
    (image gr 0 0))
  (let [gr (create-graphics 100 100 :java2d)]
    (with-graphics gr
      (background 127)
      (ellipse 50 50 40 80))
    (image gr 100 100))
  (let [gr (create-graphics 100 100 :pdf "generated/create-graphics.pdf")]
    (with-graphics gr
      (background 127)
      (ellipse 50 50 80 40)
      (.dispose gr))))
