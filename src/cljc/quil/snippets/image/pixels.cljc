(ns quil.snippets.image.pixels
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet blend
  "blend"
  {:setup (q/no-loop)}

  (q/background 255 100 20 50)

  (let [im (q/create-image 50 50 #?(:clj :rgb))
        modes [#?(:cljs :replace) :blend :add #?(:clj :subtract) :darkest
               :lightest :difference :exclusion :multiply :screen
               :overlay :hard-light :soft-light :dodge :burn]
        splitted (partition-all 5 modes)]
    (comment "draw 3 squares of different color on the graphics")
    (dotimes [x 10]
      (dotimes [y 10]
        (q/set-pixel im (+ x 10) (+ y 10) (q/color 255 0 0))
        (q/set-pixel im (+ x 30) (+ y 10) (q/color 0 255 0))
        (q/set-pixel im (+ x 20) (+ y 30) (q/color 0 0 255))))
    #?(:cljs (q/update-pixels im))

    (comment "draw all possible blended modes")
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (comment "blend with sketch itself")
          (q/blend 400 0 50 50 (* col 55) (* row 55) 50 50 mode)
          (comment "blend with image")
          (q/blend im 0 0 50 50 (* col 55) (+ 170 (* row 55)) 50 50 mode)
          (comment "blend image to image")
          (q/blend im (q/current-graphics) 0 0 50 50 (* col 55) (+ 340 (* row 55)) 50 50 mode))))))

(defsnippet copy
  "copy"
  {}

  (q/background 255)
  (let [im (q/create-image 100 100 #?(:clj :rgb))]
    (comment " gradient on the image")
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    #?(:cljs (q/update-pixels im))

    (comment "draw original image")
    (q/image im 0 0)
    (comment "copy left top quarter to the right top quarter")
    (q/copy im im [0 0 50 50] [50 0 50 50])
    (comment "copy the whole image to the sketch, essentially just draw it")
    (q/copy im [0 0 100 100] [120 0 100 100])
    (comment "copy top left 50x50 square of sketch ")
    (comment "to the 100x100 square at [240, 0] position")
    (q/copy [0 0 50 50] [240 0 100 100])))

(defsnippet image-filter
  "image-filter"
  {}

  (q/background 255)
  (let [orig (q/create-graphics 100 100)
        modes [[:threshold]
               [:threshold 0.2]
               [:gray]
               [:invert]
               [:posterize 20]
               [:blur]
               [:blur 3]
               [:opaque]
               [:erode]
               [:dilate]]
        splitted (partition-all 4 modes)]
    (comment "draw 10x10 square from circles of different color")
    (q/with-graphics orig
      (q/color-mode :rgb 1.0)
      (q/background 1)
      (q/no-stroke)
      (q/ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (q/fill r 0 b)
        (q/ellipse (* r 100) (* b 100) 10 10)))

    (comment "apply different filters, four filters per row")
    (q/image orig 0 0)
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              clone (q/get-pixel orig)]
          (apply q/image-filter clone mode)
          (q/image clone (* col 120) (* 120 (inc row))))))))

(defsnippet display-filter
  "display-filter"
  {}

  (q/background 255)
  (let [orig (q/create-graphics 100 100)
        modes [[:threshold]
               [:threshold 0.2]
               [:gray]
               [:invert]
               [:posterize 20]
               [:blur]
               [:blur 3]
               [:opaque]
               [:erode]
               [:dilate]]
        splitted (partition-all 4 modes)]
    (comment "draw 10x10 square from circles of different color")
    (q/with-graphics orig
      (q/color-mode :rgb 1.0)
      (q/background 1)
      (q/no-stroke)
      (q/ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (q/fill r 0 b)
        (q/ellipse (* r 100) (* b 100) 10 10)))

    (comment "apply different filters, four filters per row")
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
   (defsnippet filter-shader
     "filter-shader"
     {:renderer :p2d}

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

(defsnippet get-pixel
  "get-pixel"
  {}

  (q/background 255)
  (let [gr (q/create-graphics 100 100)]
    (comment "draw circle on the graphics")
    (q/with-graphics gr
      (q/background 255)
      (q/fill 127 255 180)
      (q/ellipse 50 50 70 70))

    (comment "draw original graphics")
    (q/image gr 0 0)

    (comment "copy graphics and draw it")
    (q/image (q/get-pixel gr) 0 120)

    (comment "use get-pixel to get color of specific pixel")
    (comment "and draw square")
    (q/fill (q/get-pixel gr 50 50))
    (q/rect 120 120 100 100)

    (comment "use get-pixel to copy part of the graphics")
    (q/image (q/get-pixel gr 0 0 50 50) 240 120)

    (comment "use get-pixel to copy part of the sketch itself")
    (q/image (q/get-pixel) 400 400)
    (q/fill (q/get-pixel 50 50))
    (q/rect 120 240 100 100)
    (q/image (q/get-pixel 0 0 50 50) 240 240)))

(defsnippet pixels-update-pixels
  ["pixels" "update-pixels"]
  {:renderer :p2d}

  (q/background 255)
  (let [size 50
        gr (q/create-graphics size size :p2d)]

    (comment "draw red circle on the graphics")
    (q/with-graphics gr
      (q/background 255)
      (q/fill 255 0 0)
      (q/ellipse (/ size 2) (/ size 2) (* size (/ 2 3)) (* size (/ 2 3))))

    (comment "draw original graphics")
    (q/image gr 0 0)
    (comment "get pixels of the graphics and copy")
    (comment "the first half of all pixels to the second half")
    (let [px (q/pixels gr)
          half #?(:clj (/ (* size size) 2)
                  :cljs (* 4 (* (q/display-density) size) (/ (* (q/display-density) size) 2)))]
      (dotimes [i half]
        #?(:clj (aset-int px (+ i half) (aget px i))
           :cljs (aset px (+ i half) (aget px i)))))
    (q/update-pixels gr)
    (q/image gr (+ size 20) 0)

    (comment "get pixels of the sketch itself and copy")
    (comment "the first half of all pixels to the second half")
    (let [px (q/pixels)
          half #?(:clj (/ (* (q/width) (q/height)) 10)
                  :cljs (/ (* 4 (* (q/display-density) (q/width)) (* (q/display-density) (q/height))) 10))]
      (dotimes [i half]
        #?(:clj (aset-int px (+ i half) (aget px i))
           :cljs (aset px (+ i half) (aget px i)))))
    (q/update-pixels)))

(defsnippet set-image
  ["set-image" "set-pixel"]
  {}

  (q/background 255)
  (let [im (q/create-image 100 100 #?(:clj :rgb))]
    (comment "draw gradient on the image")
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    #?(:cljs (q/update-pixels im))

    (comment "draw image on sketch")
    (q/set-image 10 10 im)))

