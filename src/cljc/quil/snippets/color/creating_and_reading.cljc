(ns quil.snippets.color.creating-and-reading
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet alpha
  "alpha"
  {}

  (q/background 255)

  (let [_ (comment "create semitransparent red")
        semi-red (q/color 255 0 0 120)
        _ (comment "extract alpha value from it")
        alph (q/alpha semi-red)
        _ (comment "create semitransparent blue using the alpha")
        semi-blue (q/color 0 0 255 alph)]
    (q/fill semi-red)
    (q/rect 0 0 100 100)

    (q/fill semi-blue)
    (q/rect 70 70 100 100)))

(defsnippet blend-color
  "blend-color"
  {}

  (q/background 255)

  (let [_ (comment "very transparent red")
        c1 (q/color 255 100 20 50)
        _ (comment "not very transparent blue")
        c2 (q/color 40 200 255 200)
        modes [:blend :add :subtract :darkest :lightest :difference :exclusion
               :multiply :screen :overlay :hard-light :soft-light :dodge :burn]
        splitted (partition-all 3 modes)]
    (comment "draw 2 rectangles with colors c1 and c2")
    (q/fill c1)
    (q/rect 0 0 70 70)
    (q/fill c2)
    (q/rect 100 0 70 70)

    (comment "draw all possible blended colors")
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (q/fill (q/blend-color c1 c2 mode)))
        (q/rect (* col 100) (* (inc row) 100) 70 70)))))

(defsnippet red
  "red"
  {}

  (q/background 255)
  (let [col (q/color 123 50 220)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill (q/red col) 0 0)
    (q/rect 70 70 100 100)))

(defsnippet green
  "green"
  {}

  (q/background 255)
  (let [col (q/color 123 50 220)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill 0 (q/green col) 0)
    (q/rect 70 70 100 100)))

(defsnippet blue
  "blue"
  {}

  (q/background 255)
  (let [col (q/color 123 50 220)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill 0 0 (q/blue col))
    (q/rect 70 70 100 100)))

(defsnippet hue
  "hue"
  {}

  (q/background 255)

  (q/color-mode :hsb)
  (let [col (q/color 100 230 100)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill (q/hue col) 255 255)
    (q/rect 70 70 100 100)))

(defsnippet saturation
  "saturation"
  {}

  (q/background 255)

  (q/color-mode :hsb)
  (let [col (q/color 100 230 100)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill 255 (q/saturation col) 255)
    (q/rect 70 70 100 100)))

(defsnippet brightness
  "brightness"
  {}

  (q/background 255)

  (q/color-mode :hsb)
  (let [col (q/color 100 230 100)]
    (q/fill col)
    (q/rect 0 0 100 100)

    (q/fill 255 255 (q/brightness col))
    (q/rect 70 70 100 100)))


(defsnippet color
  "color"
  {}

  (q/background 255)

  (comment "black")
  (q/fill (q/color 0))
  (q/rect 0 0 100 100)

  (comment "semitransparent gray")
  (q/fill (q/color 128 128))
  (q/rect 70 70 100 100)

  (comment "purple")
  (q/fill (q/color 255 0 255))
  (q/rect 140 140 100 100)

  (comment "semitransparent cyan")
  (q/fill (q/color 0 255 255 120))
  (q/rect 210 210 100 100))

(defsnippet color-mode-hsb
  "color-mode"
  {}

  (q/color-mode :rgb 255)
  (q/background 255)

  ; Result image is total mess, as apparently color-mode is global
  ; and only last (q/color-mode) call will be used for all drawings.
  ; Still we can ensure that no error is thrown.
  (q/color-mode :hsb)
  (q/fill 255 255 255)
  (q/rect 0 0 100 100))

(defsnippet color-mode
  "color-mode"
  {}

  (q/color-mode :rgb 255)
  (q/background 255)

  (comment "use HSB and draw red")
  (q/color-mode :hsb)
  (q/fill 255 255 255)
  (q/rect 0 0 100 100)

  (comment "use HSB with different max and draw dark green")
  (q/color-mode :hsb 5 10 20)
  (q/fill 2 10 5)
  (q/rect 70 70 100 100)

  (comment "use RGB with 42 max value and draw 75% transparent blue")
  (q/color-mode :rgb 40)
  (q/fill 0 0 40 30)
  (q/rect 140 140 100 100)

  (comment "use RGB with different max values and draw semitransparent cyan")
  (q/color-mode :rgb 5 10 20 30)
  (q/fill 0 10 20 15)
  (q/rect 210 210 100 100))

#?(:clj
   (defsnippet current-fill
     "current-fill"
     {}

     (q/background 255)

     (comment "set to red")
     (q/fill 255 0 0)
     (q/rect 0 0 100 100)

     (let [_ (comment "remember current color")
           cur-fill (q/current-fill)]

       (comment "change to blue")
       (q/fill 0 0 255)
       (q/rect 70 70 100 100)

       (comment "change back to the original color")
       (q/fill cur-fill)
       (q/rect 140 140 100 100))))

#?(:clj
   (defsnippet current-stroke
     "current-stroke"
     {}

     (q/background 255)

     (comment "set to red")
     (q/stroke 255 0 0)
     (q/rect 0 0 100 100)

     (let [_ (comment "remember current color")
           cur-stroke (q/current-stroke)]
       (comment "change to blue")
       (q/stroke 0 0 255)
       (q/rect 70 70 100 100)

       (comment "change back to the original color")
       (q/stroke cur-stroke)
       (q/rect 140 140 100 100))))

(defsnippet lerp-color
  "lerp-color"
  {}

  (q/background 255)

  (let [c1 (q/color 255 0 0)
        c2 (q/color 0 0 255)]
    (comment "draw colors that transition from c1 to c2")
    (dotimes [i 6]
      (q/fill (q/lerp-color c1 c2 (/ i 5)))
      (q/rect (* i 70) (* i 70) 100 100))))

; snippet to make sure that using hex colors doesn't throw error.
(defsnippet use-hex-colors
  ; specify no function so it doesn't get added as snippet in API.
  []
  {}

  (q/lerp-color 0xFF00FF00 0xFF0000FF 0.5)
  (q/hue 0xFF112233)
  (q/saturation 0xFF112233)
  (q/brightness 0xFF112233)
  (q/red 0xFF112233)
  (q/green 0xFF112233)
  (q/blue 0xFF112233)
  (q/blend-color 0xFF00FF00 0xFFFF0000 :blend)
  (q/background 0xFF112233)
  (q/fill 0xFF00FF00)
  (q/stroke 0xFF00FF00)


)
