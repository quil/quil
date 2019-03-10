(ns quil.snippets.image.loading-and-displaying
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet image
  "image"
  {}

  (q/background 255)
  (comment "create graphics with circle")
  (let [gr (q/create-graphics 70 70)]
    (q/with-graphics gr
      (q/ellipse 35 35 70 70))

    (comment "draw graphics twice")
    (q/image gr 0 0)
    (q/image gr 100 0 100 70)))

(defsnippet image-mode
  "image-mode"
  {}

  (q/background 255)
  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (q/fill 0 0 255)
      (q/stroke 0 0 255)
      (q/rect 0 0 100 100))
    (q/stroke-weight 10)

    (comment "use :corner mode")
    (q/image-mode :corner)
    (q/image gr 50 50)
    (q/point 50 50)

    (comment "use :center mode")
    (q/image-mode :center)
    (q/image gr 250 100)
    (q/point 250 100)

    (comment "use :corners mode")
    (q/image-mode :corners)
    (q/image gr 350 50 400 150)
    (q/point 350 50)
    (q/point 400 150)))

(defsnippet load-image
  "load-image"
  {:setup (let [_ (comment "create url to load image 100x100")
                url #?(:clj "https://dummyimage.com/100x100/2c3e50/ffffff.png"
                       :cljs "https://placekitten.com/100/100")]
            (q/set-state! :image (q/load-image url)))}

  (let [im (q/state :image)]
    (comment "check if image is loaded using function loaded?")
    (when (q/loaded? im)
      (q/image im 0 0))))

(defsnippet mask-image
  "mask-image"
  {}

  (q/background 255)
  (comment "define 2 images and a mask to apply to them")
  (let [im (q/create-image 100 100 #?(:clj :argb))
        im2 (q/create-image 100 100 #?(:clj :argb))
        mask (q/create-image 100 100 #?(:clj :argb))]

    (comment "first image is a blue square with a red cross")
    (dotimes [x 100]
      (dotimes [y 100]
        (if (or (< 0 (- x y) 5) (< 95 (+ x y) 100))
          (q/set-pixel im x y (q/color 255 0 0 255))
          (q/set-pixel im x y (q/color 0 0 255)))))
    #?(:cljs (q/update-pixels im))

    (comment "second image is a green cross")
    (dotimes [x 100]
      (dotimes [y 5]
        (q/set-pixel im2 (+ y 47) x (q/color 0 255 0))
        (q/set-pixel im2 x (+ y 47) (q/color 0 255 0))))
    #?(:cljs (q/update-pixels im2))

    (comment "mask is rectangles with different shades of gray")
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel mask x y (q/color 0 (* 40 (+ (quot x 20) (quot y 20)))))))
    #?(:cljs (q/update-pixels mask))

    (comment "draw first image, mask and image with mask applied")
    (q/image im 20 20)
    (q/image mask 140 20)
    (q/mask-image im mask)
    (q/image im 260 20)

    (comment "draw second image, mask and image with mask applied")
    (q/image im2 20 140)
    (q/image mask 140 140)
    (q/mask-image im2 mask)
    (q/image im2 260 140)))

(defsnippet no-tint
  "no-tint"
  {}

  (q/background 255)
  (comment "create graphics with white circle")
  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (q/background 0 0)
      (q/fill 255)
      (q/ellipse 50 50 70 70))

    (comment "apply cyan tint")
    (q/image gr 0 0)
    (q/tint 127 255 255)
    (q/image gr 100 0)

    (comment "remove tint")
    (q/no-tint)
    (q/image gr 200 0)))

(defsnippet tint
  "tint"
  {}

  (q/background 127)

  (let [gr (q/create-graphics 100 100)]
    (comment "draw 4 circles of different color on the graphics")
    (q/with-graphics gr
      (q/background 0 0)
      (q/fill 255)
      (q/ellipse 25 25 40 40)
      (q/fill 255 0 0)
      (q/ellipse 75 25 40 40)
      (q/fill 0 255 0)
      (q/ellipse 25 75 40 40)
      (q/fill 0 0 255)
      (q/ellipse 75 75 40 40))

    (comment "apply different types of tint")
    (q/no-tint)
    (q/image gr 0 0)
    (q/tint 127)
    (q/image gr 120 0)
    (q/tint 255 127)
    (q/image gr 240 0)
    (q/tint 200 127 180)
    (q/image gr 0 120)
    (q/tint 200 127 180 127)
    (q/image gr 120 120)))

