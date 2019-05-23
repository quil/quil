(ns quil.snippets.color.setting
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet background
  "background"
  {}

  (comment "red")
  (q/background 255 0 0)

  (let [_ (comment "create graphics to test backgrounds")
        gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (comment "grey")
      (q/background 120))
    (q/image gr 0 0)

    (q/with-graphics gr
      (comment "light-grey transparent")
      (q/background 70 120))
    (q/image gr 70 70)

    (q/with-graphics gr
      (comment "cyan")
      (q/background 0 255 255))
    (q/image gr 140 140)

    (q/with-graphics gr
      (comment "semitransparent blue")
      (q/background 0 0 255 120))
    (q/image gr 210 210)))

(defsnippet background-image
  "background-image"
  {:setup (let [_ (comment "create url to image to used as background")
                url #?(:clj (str "https://dummyimage.com/" (q/width) "x"  (q/height) "/2c3e50/ffffff.png")
                       :cljs (str "https://placekitten.com/" (q/width) "/" (q/height)))]
            (q/set-state! :image (q/load-image url)))}

  (let [im (q/state :image)]
    (comment "check if image is loaded by checking its size matches sketch size")
    (when (= (.-width im) (q/width))
      (q/background-image im))))

(defsnippet fill
  "fill"
  {}

  (comment "blue background")
  (q/background 0 0 255)

  (comment "grey")
  (q/fill 120)
  (q/rect 1 1 100 100)

  (comment "semitransparent light grey")
  (q/fill 80 120)
  (q/rect 70 70 100 100)

  (comment "green")
  (q/fill 0 255 0)
  (q/rect 140 140 100 100)

  (comment "semitransparent red")
  (q/fill 255 0 0 120)
  (q/rect 210 210 100 100))

(defsnippet no-fill
  "no-fill"
  {}

  (q/background 255)

  (comment "set background to grey")
  (q/stroke 0)
  (q/fill 120)
  (q/rect 1 1 100 100)

  (comment "remove background, only border left")
  (q/no-fill)
  (q/rect 70 70 100 100))

(defsnippet no-stroke
  "no-stroke"
  {}

  (q/background 255)
  (q/stroke-weight 10)

  (comment "set stroke to black")
  (q/fill 120)
  (q/stroke 0)
  (q/rect 30 30 100 100)

  (comment "remove stroke, no border around square")
  (q/no-stroke)
  (q/rect 100 100 100 100))

(defsnippet stroke
  "stroke"
  {}

  (q/background 255)
  (q/stroke-weight 10)

  (comment "grey")
  (q/stroke 120)
  (q/rect 10 10 100 100)

  (comment "semitransparent light grey")
  (q/stroke 80 120)
  (q/rect 80 80 100 100)

  (comment "green")
  (q/stroke 0 255 0)
  (q/rect 150 150 100 100)

  (comment "semitransparent red")
  (q/stroke 255 0 0 120)
  (q/rect 220 220 100 100))

(defsnippet clear
  "clear"
  {:renderer :p2d}

  (comment "reusing the same graphics to draw 3 things")
  (let [g (q/create-graphics 200 200 :p2d)]
    (q/background 255)
    (comment "draw red square")
    (q/with-graphics g
      (q/fill 255 0 0)
      (q/rect 25 25 150 150))
    (q/image g 0 0)
    (comment "draw circle without clearing")
    (comment "red square is still present")
    (q/with-graphics g
      (q/fill 0 255 0)
      (q/ellipse 100 100 150 150))
    (q/image g 50 50)
    (comment "draw triangle but with clear")
    (q/with-graphics g
      (q/clear)
      (q/fill 0 0 255)
      (q/triangle 25 25 175 25 25 175))
    (q/image g 100 100)))
