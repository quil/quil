(ns snippets.image.loading-and-displaying
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet image-s {}
  (background 255)
  (let [gr (create-graphics 70 70)]
    (with-graphics gr
      (ellipse 35 35 70 70))

    (image gr 0 0)
    (image gr 100 0 100 70)))

(defsnippet image-mode-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (fill 0 0 255)
      (stroke 0 0 255)
      (rect 0 0 100 100))
    (stroke-weight 10)

    (image-mode :corner)
    (image gr 50 50)
    (point 50 50)

    (image-mode :center)
    (image gr 250 100)
    (point 250 100)

    (image-mode :corners)
    (image gr 350 50 400 150)
    (point 350 50)
    (point 400 150)))

(defsnippet load-image-s {}
  (let [im (load-image "http://cloud.github.com/downloads/quil/quil/quil.png")]
    (image im 0 0)))

(defsnippet mask-image-s {:renderer :p3d}
  (background 255)
  (let [gr (create-graphics 100 100 :p3d)
        gr2 (create-graphics 100 100 :p3d)
        mask (create-graphics 100 100 :p3d)]
    (with-graphics gr
      (background 0 0 255)
      (stroke-weight 3)
      (stroke 255 0 0)
      (line 0 0 100 100)
      (line 0 100 100 0))

    (with-graphics gr2
      (background 255)
      (stroke 0 255 0)
      (stroke-weight 5)
      (line 0 50 100 50)
      (line 50 0 50 100))

    (with-graphics mask
      (background 0)
      (stroke-weight 5)
      (no-fill)
      (stroke 255)
      (ellipse 50 50 10 10)
      (stroke 200)
      (ellipse 50 50 30 30)
      (stroke 150)
      (ellipse 50 50 50 50)
      (stroke 100)
      (ellipse 50 50 70 70)
      (stroke 50)
      (ellipse 50 50 90 90))

    (image gr 20 20)
    (image mask 140 20)
    (mask-image gr mask)
    (image gr 260 20)

    (image gr2 20 140)
    (image mask 140 140)
    (with-graphics gr2
      (mask-image mask))
    (image gr2 260 140)))

(defsnippet no-tint-s {}
  (background 255)
  (let [gr (create-graphics 100 100)]
    (with-graphics (create-graphics 100 100)
      (background 0 255)
      (fill 255)
      (ellipse 25 25 20 20)
      (fill 255 0 0)
      (ellipse 75 25 20 20)
      (fill 0 255 0)
      (ellipse 25 75 20 20)
      (fill 0 0 255)
      (ellipse 75 75 20 20))

    (with-graphics gr
      (background 0 0)
      (fill 255)
      (ellipse 50 50 70 70))

    (image gr 0 0)
    (tint 127 255 255)
    (image gr 100 0)
    (no-tint)
    (image gr 200 0)))

(defsnippet request-image-s
  {:setup (set-state! :image (request-image "http://cloud.github.com/downloads/quil/quil/quil.png"))}
  (if (zero? (.-width (state :image)))
    (text "Loading" 10 10)
    (image (state :image) 0 0)))

(defsnippet tint-s {}
  (background 127)
  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (background 0 0)
      (fill 255)
      (ellipse 25 25 40 40)
      (fill 255 0 0)
      (ellipse 75 25 40 40)
      (fill 0 255 0)
      (ellipse 25 75 40 40)
      (fill 0 0 255)
      (ellipse 75 75 40 40))

    (no-tint)
    (image gr 0 0)
    (tint 127)
    (image gr 120 0)
    (tint 255 127)
    (image gr 240 0)
    (tint 200 127 180)
    (image gr 0 120)
    (tint 200 127 180 127)
    (image gr 120 120)))

