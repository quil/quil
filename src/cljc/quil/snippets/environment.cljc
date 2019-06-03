(ns quil.snippets.environment
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet current-frame-rate-target-frame-rate
  ["current-frame-rate" "target-frame-rate"]
  {:skip-image-diff? true}

  (q/background 255)
  (q/fill 0)
  (q/text (str "(q/current-frame-rate) = " (q/current-frame-rate)) 10 20)
  (q/text (str "(q/target-frame-rate) = " (q/target-frame-rate)) 10 40))

(defsnippet current-graphics
  "current-graphics"
  {}

  (q/background 255)
  (q/fill 0 0 255)
  (.rect (q/current-graphics) 1 1 100 100)

  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (.fill (q/current-graphics) 255 255 0)
      (.ellipse (q/current-graphics) 50 50 100 100))
    (q/image gr 70 70)))

(defsnippet cursor-no-cursor
  ["cursor" "no-cursor"]
  {}

  (q/background 255)
  (q/rect-mode :corners)
  (q/text-align :center :center)
  (comment "iterate through all types of cursors")
  (let [types [:arrow :cross :hand :move :text :wait :no-cursor :default]
        box-width (/ (q/width) 4.1)
        box-height (/ (q/height) 2.1)]
    (dotimes [ind (count types)]
      (let [type (nth types ind)
            _ (comment "calculate corners of current box")
            x (* box-width (rem ind 4))
            y (* box-height (quot ind 4))
            n-x (+ x box-width)
            n-y (+ y box-height)]
        (comment "draw a box for current type with its name in center")
        (q/stroke 127)
        (q/no-fill)
        (q/rect x y n-x n-y)
        (q/no-stroke)
        (q/fill 0)
        (q/text (str type)
                (/ (+ x n-x) 2)
                (/ (+ y n-y) 2))
        (comment "if mouse is inside the box - enable this type")
        (when (and (<= x (q/mouse-x) n-x)
                   (<= y (q/mouse-y) n-y))
          (condp = type
            :default (q/cursor)
            :no-cursor (q/no-cursor)
            (q/cursor type)))))))

#?(:clj
   (defsnippet cursor-image
     "cursor-image"
     {:setup (q/set-state! :image (q/load-image "test/html/cursor.jpg"))}

     (if (zero? (.-width (q/state :image)))
       (q/text "Loading" 10 10)
       (do
         (q/cursor-image (q/state :image))
         (q/cursor-image (q/state :image) 16 16)
         (q/image (q/state :image) 0 0)))))

(defsnippet focused
  "focused"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "Focused: " (q/focused)) 10 20))

(defsnippet frame-count
  "frame-count"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "Frame count: " (q/frame-count)) 10 20))

(defsnippet frame-rate
  "frame-rate"
  {:skip-image-diff? true}

  (q/background 255)
  (q/fill 0)
  (q/text (str "Frame rate: " (q/target-frame-rate)) 10 20)

  (let [frame (q/frame-count)]
    (comment "draw moving box")
    (q/rect (rem frame (q/width)) 50 50 50)
    (comment "every 10 frames change frame rate")
    (comment "frame rate cycles through [1, 6, 11, 16, 21]")
    (when (zero? (rem frame 10))
      (q/frame-rate (inc (* 5 (rem (quot frame 10) 5)))))))

(defsnippet height-width
  ["height" "width"]
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "width: " (q/width) ", height: " (q/height)) 10 20))

#?(:clj
   (defsnippet screen-height-screen-width
     ["screen-width" "screen-height"]
     {}

     (q/background 255)
     (q/fill 0)
     (let [w (q/screen-width)
           h (q/screen-height)]
       (q/text (str w "x" h) 10 20))))

(defsnippet display-density
  "display-density"
  {}

  (q/background 255)
  (q/fill 0)
  (q/text (str "display density: " (q/display-density)) 10 20))

(defsnippet pixel-density
  "pixel-density"
  {:settings #(q/pixel-density 1)}

  (q/background 255)
  (q/fill 0)
  (q/ellipse 102 102 200 200)
  (q/triangle 200 200 400 300 300 400))

(defsnippet resize-sketch
  "resize-sketch"
  {:skip-image-diff? true}

  (q/frame-rate 1)
  (comment "each frame increase size of sketch")
  (q/resize-sketch (inc (q/width)) (inc (q/height)))
  (q/background 255)
  (q/fill 0)
  (q/text (str "width: " (q/width) ", height: " (q/height)) 10 20))

