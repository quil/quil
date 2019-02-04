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
                url (str "https://dummyimage.com/100x100/2c3e50/ffffff.png")]
            (q/set-state! :image (q/load-image url)))}

  (let [im (q/state :image)]
    (comment "image is loaded once its width is non-zero")
    (when-not (zero? (.-width im))
      (q/image im 0 0))))

#?(:clj
   (defsnippet mask-image
     "mask-image"
     {:renderer :p3d}

     (q/background 255)
     (comment "define 2 graphics and mask to apply to them")
     (let [gr (q/create-graphics 100 100 :p3d)
           gr2 (q/create-graphics 100 100 :p3d)
           mask (q/create-graphics 100 100 :p3d)]

       (comment "first graphic is blue square with red crossing")
       (q/with-graphics gr
         (q/background 0 0 255)
         (q/stroke-weight 3)
         (q/stroke 255 0 0)
         (q/line 0 0 100 100)
         (q/line 0 100 100 0))

       (comment "second graphic is green cross")
       (q/with-graphics gr2
         (q/background 255)
         (q/stroke 0 255 0)
         (q/stroke-weight 5)
         (q/line 0 50 100 50)
         (q/line 50 0 50 100))

       (comment "mask is grey circles")
       (q/with-graphics mask
         (q/background 0)
         (q/stroke-weight 5)
         (q/no-fill)
         (q/stroke 255)
         (q/ellipse 50 50 10 10)
         (q/stroke 200)
         (q/ellipse 50 50 30 30)
         (q/stroke 150)
         (q/ellipse 50 50 50 50)
         (q/stroke 100)
         (q/ellipse 50 50 70 70)
         (q/stroke 50)
         (q/ellipse 50 50 90 90))

       (comment "draw first graphic, mask and graphic with mask applied")
       (q/image gr 20 20)
       (q/image mask 140 20)
       (q/mask-image gr mask)
       (q/image gr 260 20)

       (comment "draw second graphic, mask and graphic with mask applied")
       (q/image gr2 20 140)
       (q/image mask 140 140)
       (q/with-graphics gr2
         (q/mask-image mask))
       (q/image gr2 260 140))))

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

#?(:clj
   (defsnippet request-image
     "request-image"
     {:setup
      (q/set-state! :image
                    (q/request-image "https://dummyimage.com/100x100/2c3e50/ffffff.png"))}

     (if (zero? (.-width (q/state :image)))
       (q/text "Loading" 10 10)
       (q/image (q/state :image) 0 0))))

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

