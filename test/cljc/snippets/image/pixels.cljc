(ns snippets.image.pixels
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

#?(:clj
   (defsnippet blend {}
     (q/background 255 100 20 50)

     (let [gr (q/create-graphics 50 50)
           modes [:blend :add :subtract :darkest :lightest :difference :exclusion
                  :multiply :screen :overlay :hard-light :soft-light :dodge :burn]
           splitted (partition-all 5 modes)]
       (q/with-graphics gr
         (q/background 40 200 255 200)
         (q/fill 255 0 0)
         (q/ellipse 12 12 20 20)
         (q/fill 0 255 0)
         (q/ellipse 38 12 20 20)
         (q/fill 0 0 255)
         (q/ellipse 25 38 20 20))

       ; Draw all possible blended modes.
       (dotimes [row (count splitted)]
         (dotimes [col (count (nth splitted row))]
           (let [mode (nth (nth splitted row) col)]
             ; blend with sketch itself
             (q/blend 400 0 50 50 (* col 55) (* row 55) 50 50 mode)
             ; blend with graphics
             (q/blend gr 0 0 50 50 (* col 55) (+ 170 (* row 55)) 50 50 mode)
             ; blend graphics to graphics
             (q/blend gr (q/current-graphics) 0 0 50 50 (* col 55) (+ 340 (* row 55)) 50 50 mode)))))))

#?(:clj
   (defsnippet copy {}
     (q/background 255)
     (let [gr (q/create-graphics 100 100)]
       (q/with-graphics gr
         (q/background 0 0 255)
         (q/stroke-weight 5)
         (q/stroke 0 255 0)
         (q/no-fill)
         (q/ellipse 50 50 10 10)
         (q/ellipse 50 50 50 50)
         (q/ellipse 50 50 90 90)
         (q/stroke 255 0 0)
         (q/line 0 0 100 100)
         (q/line 100 0 0 100))
       ; Processing 3 can't treat PGraphics as PImage
       ; so we have to get create PImage from PGraphics ourselves.
       (let [im (.copy gr)]
         (q/image im 0 0)
         (q/copy im im [0 0 50 50] [50 0 50 50])
         (q/copy im [0 0 100 100] [120 0 100 100])
         (q/copy [0 0 50 50] [240 0 100 100])))))

(defsnippet image-filter {}
  (q/background 255)
  (let [orig (q/create-graphics 100 100)
        modes [[:threshold]
               [:threshold 0.7]
               [:gray]
               [:invert]
               [:posterize 20]
               [:blur]
               [:blur 3]
               [:opaque]
               [:erode]
               [:dilate]]
        splitted (partition-all 4 modes)]
    (q/with-graphics orig
      (q/color-mode :rgb 1.0)
      (q/background 1)
      (q/no-stroke)
      (q/ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (q/fill r 0 b)
        (q/ellipse (* r 100) (* b 100) 10 10)))

    (q/image orig 0 0)
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              clone (q/get-pixel orig)]
          (apply q/image-filter clone mode)
          (q/image clone (* col 120) (* 120 (inc row))))))))

(defsnippet display-filter {}
  (q/background 255)
  (let [orig (q/create-graphics 100 100)
        modes [[:threshold]
               [:threshold 0.7]
               [:gray]
               [:invert]
               [:posterize 20]
               [:blur]
               [:blur 3]
               [:opaque]
               [:erode]
               [:dilate]]
        splitted (partition-all 4 modes)]
    (q/with-graphics orig
      (q/color-mode :rgb 1.0)
      (q/background 1)
      (q/no-stroke)
      (q/ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (q/fill r 0 b)
        (q/ellipse (* r 100) (* b 100) 10 10)))

    (q/image orig 0 0)
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              dest (q/create-graphics 100 100)]
          (q/with-graphics dest
            (q/image orig 0 0)
            (apply q/display-filter mode))
          (q/image dest (* col 120) (* 120 (inc row))))))))

#?(:clj
   (defsnippet filter-shader {:renderer :p2d}
     (q/background 255)
     (let [orig (q/create-graphics 100 100)
           shd (q/load-shader (.getPath (clojure.java.io/resource "SimpleShader.glsl")))]
       (q/with-graphics orig
         (q/color-mode :rgb 1.0)
         (q/background 1)
         (q/no-stroke)
         (q/ellipse-mode :corner)
         (doseq [r (range 0 1 0.1)
                 b (range 0 1 0.1)]
           (q/fill r 0 b)
           (q/ellipse (* r 100) (* b 100) 10 10)))
       (q/image orig 0 0)

       (q/filter-shader shd)
       (q/image orig 100 100))))

#?(:clj
   (defsnippet get-pixel {}
     (q/background 255)
     (let [gr (q/create-graphics 100 100)]
       (q/with-graphics gr
         (q/background 255)
         (q/fill 127 255 180)
         (q/ellipse 50 50 70 70))

       (q/image gr 0 0)

       (q/image (q/get-pixel gr) 0 120)
       (q/fill (q/get-pixel gr 50 50))
       (q/rect 120 120 100 100)
       (q/image (q/get-pixel gr 0 0 50 50) 240 120)

       (q/image (q/get-pixel) 400 400)
       (q/fill (q/get-pixel 50 50))
       (q/rect 120 240 100 100)
       (q/image (q/get-pixel 0 0 50 50) 240 240))))

#?(:clj
   (defsnippet pixels-update-pixels {:renderer :p2d}
     (q/background 255)
     (let [size 50
           gr (q/create-graphics size size :p2d)]
       (q/with-graphics gr
         (q/background 255)
         (q/fill 255 0 0)
         (q/ellipse (/ size 2) (/ size 3) (* size 2/3) (* size 2/3)))

       (q/image gr 0 0)
       (let [px (q/pixels gr)
             half (/ (* size size) 2)]
         (dotimes [i half]
           (aset-int px (+ i half) (aget px i))))
       (q/update-pixels gr)
       (q/image gr (+ size 20) 0)

       (let [px (q/pixels)
             half (/ (* (q/width) (q/height)) 10)]
         (dotimes [i half]
           (aset-int px (+ i half) (aget px i))))
       (q/update-pixels))))

(defsnippet set-image {}
  (q/background 255)
  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (q/background 255)
      (q/fill 255 0 0)
      (q/ellipse 50 50 90 90))

    (q/set-image 10 10 gr)))

#?(:clj
   (defsnippet set-pixel {:renderer :p2d}
     (q/background 255)
     (let [gr (q/create-graphics 100 100)]
       (q/with-graphics gr
         (q/background 255))

       (doseq [i (range 30)
               j (range 30)]
         (q/set-pixel gr i j (q/color (* 7 i) (* 7 j) 0)))
       (q/image gr 0 0)

       (doseq [i (range 30)
               j (range 30)]
         (q/set-pixel (+ 40 i) (+ 40 j) (q/color 0 (* 7 i) (* 7 j)))))))
