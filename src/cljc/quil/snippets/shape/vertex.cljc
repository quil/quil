(ns quil.snippets.shape.vertex
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet begin-contour-end-contour
  ["begin-contour" "end-contour"]
  {:renderer :p2d}

  (q/stroke 255 0 0)
  (q/fill 0 127 0)
  (q/begin-shape)
  (comment "outer shape")
  (q/vertex 250 20)
  (q/vertex 400 400)
  (q/vertex 50 400)

  (comment "inner shape")
  (q/begin-contour)
  (q/vertex 200 200)
  (q/vertex 250 380)
  (q/vertex 300 200)
  (q/end-contour)
  (q/end-shape :close))

(defsnippet begin-shape-end-shape
  ["begin-shape" "end-shape"]
  {:renderer :p2d}

  (q/stroke 255 0 0)
  (comment "try all kinds of begin modes")
  (doseq [[ind begin-mode close-mode]
          [[0 nil nil]
           [1 nil :close]
           [2 :points]
           [3 :lines]
           [4 :triangles]
           [5 :triangle-fan]
           [6 :triangle-strip]
           [7 :quads]
           [8 :quad-strip]]
          :let [base-x (* 150 (mod ind 3))
                base-y (* 150 (quot ind 3))]]
    (if begin-mode
      (q/begin-shape begin-mode)
      (q/begin-shape))
    (q/vertex (+ base-x 50) (+ base-y 10))
    (q/vertex (+ base-x 80) (+ base-y 30))
    (q/vertex (+ base-x 80) (+ base-y 70))
    (q/vertex (+ base-x 50) (+ base-y 90))
    (q/vertex (+ base-x 20) (+ base-y 70))
    (q/vertex (+ base-x 20) (+ base-y 30))
    (if close-mode
      (q/end-shape close-mode)
      (q/end-shape))))

(defsnippet begin-shape-end-shape-in-graphics
  ["begin-shape" "end-shape"]
  {:renderer :p2d}

  (let [gr (q/create-graphics 400 400 :p2d)]
    (q/with-graphics gr
      (q/stroke 255 0 0)
      (q/begin-shape :triangles)
      (q/vertex 200 40)
      (q/vertex 320 120)
      (q/vertex 320 280)
      (q/vertex 200 360)
      (q/vertex 80 280)
      (q/vertex 80 120)
      (q/end-shape))
    (q/image gr 0 0)))

(defsnippet bezier-vertex
  "bezier-vertex"
  {:renderer :p3d}

  (q/background 255)
  (q/camera -400 250 -100 500 250 0 0 0 1)

  (comment "draw first shape")
  (q/begin-shape)
  (q/vertex 30 20)
  (q/bezier-vertex 480 0 480 475 30 475)
  (q/bezier-vertex 250 380 360 125 30 20)
  (q/end-shape :close)

  (comment "draw second shape")
  (q/begin-shape)
  (q/vertex 30 20 0)
  (q/bezier-vertex 480 0 20 480 475 30 30 475 40)
  (q/bezier-vertex 250 380 40 360 125 10 30 20 0)
  (q/end-shape :close))

(defsnippet curve-vertex
  "curve-vertex"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 50 200 50 50 0 0 0 0 1)

  (comment "draw first shape")
  (q/begin-shape)
  (q/curve-vertex 0 0)
  (q/curve-vertex 0 0)
  (q/curve-vertex 100 20)
  (q/curve-vertex 100 80)
  (q/curve-vertex 20 80)
  (q/curve-vertex 0 0)
  (q/curve-vertex 0 0)
  (q/end-shape :close)

  (comment "draw second shape")
  (q/begin-shape)
  (q/curve-vertex 0 0 0)
  (q/curve-vertex 0 0 0)
  (q/curve-vertex 100 0 20)
  (q/curve-vertex 100 0 80)
  (q/curve-vertex 20 0 80)
  (q/curve-vertex 0 0 0)
  (q/curve-vertex 0 0 0)
  (q/end-shape :close))

(defsnippet quadratic-vertex
  "quadratic-vertex"
  {:renderer :p3d}

  (q/background 255)

  (comment "setup camera and draw axes")
  (q/camera 50 200 50 50 0 0 0 0 -1)
  (q/line 0 0 0 0 0 100)
  (q/line 0 0 0 0 100 0)
  (q/line 0 0 0 100 0 0)

  (comment "draw first shape")
  (q/begin-shape)
  (q/vertex 0 0)
  (q/quadratic-vertex 30 50 10 100)
  (q/quadratic-vertex 50 -50 90 100)
  (q/quadratic-vertex 80 50 100 0)
  (q/end-shape :close)
  (comment "draw second shape")
  (q/begin-shape)
  (q/vertex 0 0 0)
  (q/quadratic-vertex 30 0 50 10 0 100)
  (q/quadratic-vertex 50 0 -50 90 0 100)
  (q/quadratic-vertex 80 0 50 100 0 0)
  (q/end-shape :close))

#?(:clj
   (defsnippet texture
     "texture"
     {:renderer :p2d}

     (let [gr (q/create-graphics 100 100)]
       (comment "draw something on graphics that will be texture")
       (q/with-graphics gr
         (q/background 255)
         (q/fill 255 0 0)
         (q/rect 0 60 100 40)
         (q/fill 0 150 0)
         (q/rect 0 0 100 60))
       (comment "draw graphics to see what we got")
       (q/image gr 0 0)

       (comment "draw custom shape using texture we created above")
       (q/with-translation [250 250]
         (q/begin-shape)
         (q/texture gr)
         (q/vertex 50 100 75 100)
         (q/vertex 100 50 100 75)
         (q/vertex 100 -50 100 25)
         (q/vertex 50 -100 75 0)
         (q/vertex -50 -100 25 0)
         (q/vertex -100 -50 0 25)
         (q/vertex -100 50 0 75)
         (q/vertex -50 100 25 100)
         (q/end-shape :close))))

   :cljs
   (defsnippet texture
     "texture"
     {:renderer :p3d
      :setup (q/set-state! :image (q/load-image "https://placekitten.com/100/100"))}

     (q/background 255)

     (if (zero? (.-width (q/state :image)))
       (q/text "Loading" 10 10)
       (let [gr (q/state :image)]
         (q/with-translation [50 0]
           (q/texture gr)
           (q/plane 200 200))))))

#?(:clj
   (defsnippet texture-mode
     "texture-mode"
     {:renderer :p2d}

     (let [gr (q/create-graphics 100 100)]
       (q/with-graphics gr
         (q/background 255)
         (q/fill 255 0 0)
         (q/rect 0 60 100 40)
         (q/fill 0 150 0)
         (q/rect 0 0 100 60))
       (q/image gr 0 0)

       (q/with-translation [375 125]
         (q/begin-shape)
         (q/texture gr)
         (q/texture-mode :image)
         (q/vertex 0 0 0 0)
         (q/vertex 100 100 100 100)
         (q/vertex 0 100 0 100)
         (q/end-shape :close))

       (q/with-translation [125 375]
         (q/begin-shape)
         (q/texture gr)
         (q/texture-mode :normal)
         (q/vertex 0 0 0 0)
         (q/vertex 100 100 1 1)
         (q/vertex 0 100 0 1)
         (q/end-shape :close)))))

#?(:clj
   (defsnippet texture-wrap
     "texture-wrap"
     {:renderer :p2d}

     (let [txtr (q/create-graphics 100 100)
           mode (if (even? (q/frame-count)) :clamp :repeat)]
       (q/with-graphics txtr
         (q/background 255)
         (q/fill 255 0 0)
         (q/rect 0 60 100 40)
         (q/fill 0 150 0)
         (q/rect 0 0 100 60))
       (q/image txtr 0 0)
       (q/texture-wrap mode)
       (q/with-translation [200 200]
         (q/begin-shape)
         (q/texture txtr)
         (q/vertex 0 0 0 0)
         (q/vertex 200 0 200 0)
         (q/vertex 200 200 200 200)
         (q/vertex 0 200 0 200)
         (q/end-shape :close)))))

(defsnippet vertex
  "vertex"
  {:renderer :p3d}

  (q/background 255)
  (q/camera 100 400 200 100 0 0 0 0 -1)
  (q/line 0 0 0 0 0 150)
  (q/line 0 0 0 0 150 0)
  (q/line 0 0 0 150 0 0)

  (let [txtr (q/create-graphics 100 100)]
    (q/with-graphics txtr
      (q/background 255)
      (comment "draw belarusian flag, kinda")
      (q/fill 255 0 0)
      (q/rect 0 60 100 40)
      (q/fill 0 150 0)
      (q/rect 0 0 100 60))

    (comment "draw blue rect")
    (q/fill 0 0 127)
    (q/begin-shape)
    (q/vertex 0 0)
    (q/vertex 100 0)
    (q/vertex 100 100)
    (q/vertex 0 100)
    (q/end-shape :close)

    (comment "draw another blue rect")
    (q/begin-shape)
    (q/vertex 0 0 0)
    (q/vertex 100 0 0)
    (q/vertex 100 0 100)
    (q/vertex 0 0 100)
    (q/end-shape :close)

    (comment "draw rect using our custom texture")
    (q/begin-shape)
    (q/texture txtr)
    (q/vertex 100 0 0 0)
    (q/vertex 200 0 100 0)
    (q/vertex 200 100 100 100)
    (q/vertex 100 100 0 100)
    (q/end-shape :close)

    (comment "draw another rect using our custom texture")
    (q/begin-shape)
    (q/texture txtr)
    (q/vertex 100 0 0 0 0)
    (q/vertex 200 0 0 100 0)
    (q/vertex 200 0 100 100 100)
    (q/vertex 100 0 100 0 100)
    (q/end-shape :close)))
