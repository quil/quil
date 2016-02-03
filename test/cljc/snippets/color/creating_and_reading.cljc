(ns snippets.color.creating-and-reading
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet alpha {}
  (q/background 255)

  ; check that no exception thrown
  (q/alpha 0xFF00FF00)

  (let [semi-red (q/color 255 0 0 120)
        alph (q/alpha semi-red)
        semi-blue (q/color 0 0 255 alph)]
    (q/fill semi-red)
    (q/rect 0 0 100 100)

    (q/fill semi-blue)
    (q/rect 70 70 100 100)))

(defsnippet blend-color {}
  (q/background 255)

  ; check that no exception thrown
  (q/blend-color 0xFF00FF00 0xFFFF0000 :blend)

  (let [c1 (q/color 255 100 20 50) ; very transparent red
        c2 (q/color 40 200 255 200) ; not very transparent blue
        modes [:blend :add :subtract :darkest :lightest :difference :exclusion
               :multiply :screen :overlay :hard-light :soft-light :dodge :burn]
        splitted (partition-all 3 modes)]
    ; Draw 2 rectangles with colors c1 and c2
    (q/fill c1)
    (q/rect 0 0 70 70)
    (q/fill c2)
    (q/rect 100 0 70 70)

    ; Draw all possible blended colors.
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (q/fill (q/blend-color c1 c2 mode)))
        (q/rect (* col 100) (* (inc row) 100) 70 70)))))

(defsnippet red-green-blue {}
  (q/background 255)

  ; check that no exception thrown
  (q/red 0xFF112233)
  (q/green 0xFF112233)
  (q/blue 0xFF112233)

  (let [col (q/color 123 50 220)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill (q/red col) 0 0)
    (q/rect 70 70 100 100)

    (q/fill 0 (q/green col) 0)
    (q/rect 140 140 100 100)

    (q/fill 0 0 (q/blue col))
    (q/rect 210 210 100 100)))

(defsnippet hue-saturation-brightness {}
  (q/background 255)

  ; check that no exception thrown
  (q/hue 0xFF112233)
  (q/saturation 0xFF112233)
  (q/brightness 0xFF112233)

  (q/color-mode :hsb)
  (let [col (q/color 100 230 100)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill (q/hue col) 255 255)
    (q/rect 70 70 100 100)

    (q/fill 255 (q/saturation col) 255)
    (q/rect 140 140 100 100)

    (q/fill 255 255 (q/brightness col))
    (q/rect 210 210 100 100)))

(defsnippet color {}
  (q/background 255)

  (q/fill (q/color 0)) ; black
  (q/rect 0 0 100 100)

  (q/fill (q/color 128 128)) ; semitransparent gray
  (q/rect 70 70 100 100)

  (q/fill (q/color 255 0 255)) ; purple
  (q/rect 140 140 100 100)

  (q/fill (q/color 0 255 255 120)) ; semitransparent cyan
  (q/rect 210 210 100 100))

(defsnippet color-mode {}
  (q/color-mode :rgb 255)
  (q/background 255)

  ; Result image is total mess, as apparently color-mode is global
  ; and only last (q/color-mode) call will be used for all drawings.
  ; Still we can ensure that no error is thrown.
  (q/color-mode :hsb)
  (q/fill 255 255 255)
  (q/rect 0 0 100 100)

  (q/color-mode :rgb 42)
  (q/fill 42 42 0 20)
  (q/rect 70 70 100 100)

  (q/color-mode :hsb 5 10 20)
  (q/fill 5 10 20)
  (q/rect 140 140 100 100)

  (q/color-mode :rgb 5 10 20 30)
  (q/fill 5 10 0 15)
  (q/rect 210 210 100 100))

#?(:clj
   (defsnippet current-fill {}
     (q/background 255)

     (q/fill 255 0 0)
     (q/rect 0 0 100 100)

     (let [cur-fill (q/current-fill)]
       (q/fill 0 0 255)
       (q/rect 70 70 100 100)

       (q/fill cur-fill)
       (q/rect 140 140 100 100))))

#?(:clj
   (defsnippet current-stroke {}
     (q/background 255)

     (q/stroke 255 0 0)
     (q/rect 0 0 100 100)

     (let [cur-stroke (q/current-stroke)]
       (q/stroke 0 0 255)
       (q/rect 70 70 100 100)

       (q/stroke cur-stroke)
       (q/rect 140 140 100 100))))

(defsnippet lerp-color {}
  (q/background 255)

  ; check that no exception thrown
  (q/lerp-color 0xFF00FF00 0xFF0000FF 0.5)

  (let [c1 (q/color 255 0 0)
        c2 (q/color 0 0 255)]
    (dotimes [i 6]
      (q/fill (q/lerp-color c1 c2 (/ i 5)))
      (q/rect (* i 70) (* i 70) 100 100))))
