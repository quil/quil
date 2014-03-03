(ns snippets.color.creating-and-reading
  (:require [quil.snippet :refer [defsnippet show-snippet]]
            [quil.core :refer :all]))


(defsnippet alpha-s {}
  (background 255)

  (let [semi-red (color 255 0 0 120)
        alph (alpha semi-red)
        semi-blue (color 0 0 255 alph)]
    (fill semi-red)
    (rect 0 0 100 100)

    (fill semi-blue)
    (rect 70 70 100 100)))


(defsnippet blend-color-s {}
  (background 255)

  (let [c1 (color 255 100 20 50) ; very transparent red
        c2 (color 40 200 255 200) ; not very transparent blue
        modes [:blend :add :subtract :darkest :lightest :difference :exclusion
               :multiply :screen :overlay :hard-light :soft-light :dodge :burn]
        splitted (partition-all 3 modes)]
    ; Draw 2 rectangles with colors c1 and c2
    (fill c1)
    (rect 0 0 70 70)
    (fill c2)
    (rect 100 0 70 70)

    ; Draw all possible blending colors.
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (fill (blend-color c1 c2 mode)))
        (rect (* col 100) (* (inc row) 100) 70 70)))))


(defsnippet blue-s {}
  (background 255)

  (let [purple (color 255 0 255)
        bl (blue purple)
        cyan (color 0 255 bl)]
    (fill purple)
    (rect 0 0 100 100)

    (fill cyan)
    (rect 70 70 100 100)))

;(show-snippet blue-s)

(defsnippet color-s {}
  (background 255)

  (fill (color 0)) ; black
  (rect 0 0 100 100)

  (fill (color 128 128)) ; semitransparent gray
  (rect 70 70 100 100)

  (fill (color 255 0 255)) ; purple
  (rect 140 140 100 100)

  (fill (color 0 255 255 120)) ; semitransparent cyan
  (rect 210 210 100 100))
