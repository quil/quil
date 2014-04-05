(ns snippets.image.pixels
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet blend-s {}
  (background 255 100 20 50)

  (let [gr (create-graphics 50 50)
        modes [:blend :add :subtract :darkest :lightest :difference :exclusion
               :multiply :screen :overlay :hard-light :soft-light :dodge :burn]
        splitted (partition-all 5 modes)]
    (with-graphics gr
      (background 40 200 255 200)
      (fill 255 0 0)
      (ellipse 12 12 20 20)
      (fill 0 255 0)
      (ellipse 38 12 20 20)
      (fill 0 0 255)
      (ellipse 25 38 20 20))

    ; Draw all possible blended modes.
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          ; blend with sketch itself
          (blend 400 0 50 50 (* col 55) (* row 55) 50 50 mode)
          ; blend with graphics
          (blend gr 0 0 50 50 (* col 55) (+ 170 (* row 55)) 50 50 mode)
          ; blend graphics to graphics
          (blend gr (current-graphics) 0 0 50 50 (* col 55) (+ 340 (* row 55)) 50 50 mode))))))

(defsnippet copy-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 0 0 255)
      (stroke-weight 5)
      (stroke 0 255 0)
      (no-fill)
      (ellipse 50 50 10 10)
      (ellipse 50 50 50 50)
      (ellipse 50 50 90 90)
      (stroke 255 0 0)
      (line 0 0 100 100)
      (line 100 0 0 100))

    (image gr 0 0)
    (copy gr gr [0 0 50 50] [50 0 50 50])
    (copy gr [0 0 100 100] [120 0 100 100])
    (copy [0 0 50 50] [240 0 100 100])))

(defsnippet image-filter-s {}
  (background 255)
  (let [orig (create-graphics 100 100)
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
    (with-graphics orig
      (color-mode :rgb 1.0)
      (background 1)
      (no-stroke)
      (ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (fill r 0 b)
        (ellipse (* r 100) (* b 100) 10 10)))

    (image orig 0 0)
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              clone (get-pixel orig)]
          (apply image-filter clone mode)
          (image clone (* col 120) (* 120 (inc row))))))))

(defsnippet display-filter-s {}
  (background 255)
  (let [orig (create-graphics 100 100)
        dest (create-graphics 100 100)
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
    (with-graphics orig
      (color-mode :rgb 1.0)
      (background 1)
      (no-stroke)
      (ellipse-mode :corner)
      (doseq [r (range 0 1 0.1)
              b (range 0 1 0.1)]
        (fill r 0 b)
        (ellipse (* r 100) (* b 100) 10 10)))

    (image orig 0 0)
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)]
          (copy orig dest [0 0 100 100] [0 0 100 100])
          (with-graphics dest
            (apply display-filter mode))
          (image dest (* col 120) (* 120 (inc row))))))))

(defsnippet get-pixel-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 255)
      (fill 127 255 180)
      (ellipse 50 50 70 70))

    (image gr 0 0)

    (image (get-pixel gr) 0 120)
    (fill (get-pixel gr 50 50))
    (rect 120 120 100 100)
    (image (get-pixel gr 0 0 50 50) 240 120)

    (image (get-pixel) 400 400)
    (fill (get-pixel 50 50))
    (rect 120 240 100 100)
    (image (get-pixel 0 0 50 50) 240 240)))

(defsnippet pixels-update-pixels-s {:renderer :p2d}
  (background 255)
  (let [size 50
        gr (create-graphics size size :p2d)]
    (with-graphics gr
      (background 255)
      (fill 255 0 0)
      (ellipse (/ size 2) (/ size 3) (* size 2/3) (* size 2/3)))

    (image gr 0 0)
    (let [px (pixels gr)
          half (/ (* size size) 2)]
      (dotimes [i half]
        (aset-int px (+ i half) (aget px i))))
    (update-pixels gr)
    (image gr (+ size 20) 0)

    (let [px (pixels)
          half (/ (* (width) (height)) 10)]
      (dotimes [i half]
        (aset-int px (+ i half) (aget px i))))
    (update-pixels)))

(defsnippet set-image-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 255)
      (fill 255 0 0)
      (ellipse 50 50 90 90))

    (set-image 10 10 gr)))

(defsnippet set-pixel-s {:renderer :p2d}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 255))

    (doseq [i (range 30)
            j (range 30)]
      (set-pixel gr i j (color (* 7 i) (* 7 j) 0)))
    (image gr 0 0)

    (doseq [i (range 30)
            j (range 30)]
      (set-pixel (+ 40 i) (+ 40 j) (color 0 (* 7 i) (* 7 j))))))
