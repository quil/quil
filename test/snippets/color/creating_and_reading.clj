(ns snippets.color.creating-and-reading
  (:require [quil.snippet :refer [defsnippet]]
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

    ; Draw all possible blended colors.
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (fill (blend-color c1 c2 mode)))
        (rect (* col 100) (* (inc row) 100) 70 70)))))


(defsnippet red-green-blue-s {}
  (background 255)

  (let [col (color 123 50 220)]
    (fill col)
    (rect 0 0 100 100)

    (fill (red col) 0 0)
    (rect 70 70 100 100)

    (fill 0 (green col) 0)
    (rect 140 140 100 100)

    (fill 0 0 (blue col))
    (rect 210 210 100 100)))

(defsnippet hue-saturation-brightness-s {}
  (background 255)
  (color-mode :hsb)

  (let [col (color 100 230 100)]
    (fill col)
    (rect 0 0 100 100)

    (fill (hue col) 255 255)
    (rect 70 70 100 100)

    (fill 255 (saturation col) 255)
    (rect 140 140 100 100)

    (fill 255 255 (brightness col))
    (rect 210 210 100 100)))

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

(defsnippet color-mode-s {}
  (background 255)

  ; Result image is total mess, as apparently color-mode is global
  ; and only last (color-mode) call will be used for all drawings.
  ; Still we can ensure that no error is thrown.
  (color-mode :hsb)
  (fill 255 255 255)
  (rect 0 0 100 100)

  (color-mode :rgb 42)
  (fill 42 42 0 20)
  (rect 70 70 100 100)

  (color-mode :hsb 5 10 20)
  (fill 5 10 20)
  (rect 140 140 100 100)

  (color-mode :rgb 5 10 20 30)
  (fill 5 10 0 15)
  (rect 210 210 100 100))

(defsnippet current-fill-s {}
  (background 255)

  (fill 255 0 0)
  (rect 0 0 100 100)

  (let [cur-fill (current-fill)]
    (fill 0 0 255)
    (rect 70 70 100 100)

    (fill cur-fill)
    (rect 140 140 100 100)))

(defsnippet current-stroke-s {}
  (background 255)

  (stroke 255 0 0)
  (rect 0 0 100 100)

  (let [cur-stroke (current-stroke)]
    (stroke 0 0 255)
    (rect 70 70 100 100)

    (stroke cur-stroke)
    (rect 140 140 100 100)))

(defsnippet lerp-color-s {}
  (background 255)
  (let [c1 (color 255 0 0)
        c2 (color 0 0 255)]
    (dotimes [i 6]
      (fill (lerp-color c1 c2 (/ i 5)))
      (rect (* i 70) (* i 70) 100 100))))
