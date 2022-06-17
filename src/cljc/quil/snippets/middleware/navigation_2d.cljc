(ns quil.snippets.middleware.navigation-2d
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            [quil.middleware :as m :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet with-navigation-2d
  ["with-navigation-2d" "world->screen-coords"]
  {:setup (let [_ (comment "This sets up the navigation-2d state.")
                _ (comment "Ordinarily this will be handled by the
                           navigation-2d middleware.")
                nav-2d {:width 500 :height 500 :zoom 2
                        :position [(- (/ (q/width) 2.) 175)
                                   (- (/ (q/height) 2) 175)]}]
            (comment "These settings should give the illusion that our")
            (comment "\"camera\" has moved left by 175 pixels, up by")
            (comment "175 pixels, and then zoomed in until everything")
            (comment "is doubled in size.")
            (q/set-state! :navigation-2d nav-2d))}
  (let [_ (comment "Ordinarily the state will be passed to your draw function")
        _ (comment "by the fun-mode middleware, but for this example
                   we get it ourselves.")
        state (q/state)
        [pos-x pos-y] (get-in state [:navigation-2d :position])]
    (q/background 255)

    (comment "Fist we draw a red square without navigation.")
    (q/with-fill [255 0 0]
      (q/rect 50 50 100 100))

    (comment "For reference, we mark the point the center of")
    (comment "our camera will be moving to.")
    (q/with-fill [0 0 0]
      (q/ellipse pos-x pos-y 10 10))

    (comment "Now lets see what that square looks like in")
    (comment "our camera (i.e. with navigation applied.)")
    (m/with-navigation-2d state
      (comment "We'll color it blue this time to distinguish")
      (comment "it from the original.")
      (q/with-fill [0 0 255]
        (q/rect 50 50 100 100))

      (comment "Lets also apply navigation to a copy of the dot we")
      (comment "drew earlier. We expect it to end up in the center")
      (comment "of the screen.")
      (q/with-fill [0 0 0]
        (q/ellipse pos-x pos-y 10 10)))
    (comment "Notice how the square has appeared to move")
    (comment "down and to the right, the opposite of")
    (comment "the way our \"camera\" moved. The square has")
    (comment "also doubled in size.")

    (comment "Suppose we wanted to know where the top-left corner of")
    (comment "the blue square is on the screen. We know we tried to")
    (comment "draw it at [x,y]=[50,50], so lets pass that to")
    (comment "m/world->screen-coords and draw a green circle at the")
    (comment "output coordinates.")
    (let [[screen-x screen-y] (m/world->screen-coords state [50 50])]
      (q/with-fill [0 255 0]
        (q/ellipse screen-x screen-y 100 100)))
    (comment "Note that the green dot is at the top left corner of")
    (comment "the blue square, as expected.")))

(defsnippet mouse-world-coords
  "mouse-world-coords"
  {:setup (let [_ (comment "This sets up the navigation-2d state.")
                _ (comment "Ordinarily this will be handled by the
                           navigation-2d middleware.")
                nav-2d {:width 500 :height 500 :zoom 0.75
                        :position [(- (/ (q/width) 2.) 200)
                                   (+ (/ (q/height) 2) 100)]}]
            (comment "These settings should give the illusion that our")
            (comment "\"camera\" has moved left by 200 pixels, down by")
            (comment "100 pixels, and then zoomed out until everything")
            (comment "appears to be 75% of its original size.")
            (q/set-state! :navigation-2d nav-2d))}

  (let [_ (comment "Ordinarily the state will be passed to your draw function")
        _ (comment "by the fun-mode middleware, but for this example
                   we get it ourselves.")
        state (q/state)]
    (q/background 255)

    (comment "Lets draw a square using `m/with-navigation-2d` and try to tell")
    (comment "when the mouse is over it. The square should be green when")
    (comment "the mouse is over it and red otherwise.")
    (m/with-navigation-2d state
      (let [world-x -125
            world-y 200
            length 200
            mouse-x (q/mouse-x)
            mouse-y (q/mouse-y)
            _ (comment "If the mouse is between the left and right bounds")
            _ (comment "of the square as well as between the top and bottom")
            _ (comment "bounds of the square, then the color should be green.")
            color (if (and (< world-x mouse-x (+ world-x length))
                           (< world-y mouse-y (+ world-y length)))
                    [0 255 0]
                    [255 0 0])]
        (q/with-fill color
          (q/rect world-x world-y length length))))
    (comment "Uh-oh! If you run this example you'll see that the above code")
    (comment "doesn't actually work. The problem is that we are comparing")
    (comment "the screen coordinates of the mouse to the world coordinates")
    (comment "of the ellipse.")

    (comment "Let's try again using `m/mouse-world-coords` instead.")
    (m/with-navigation-2d state
      (let [world-x 175
            world-y 200
            length 200
            [mouse-wx mouse-wy] (m/mouse-world-coords state)
            color (if (and (< world-x mouse-wx (+ world-x length))
                           (< world-y mouse-wy (+ world-y length)))
                    [0 255 0]
                    [255 0 0])]
        (q/with-fill color
          (q/rect world-x world-y length length))))

    (comment "For reference, we print the mouse's screen coordinates and")
    (comment "world coordinates to the screen.")
    (q/with-fill [0 0 0]
      (q/text (str "Mouse screen coordinates: " [(q/mouse-x) (q/mouse-y)]) 10 20)
      (q/text (str "Mouse world coordinates: " (m/mouse-world-coords state)) 10 40))))

(defsnippet screen-to-world-coords
  "screen->world-coords"
  {:setup (let [_ (comment "This sets up the navigation-2d state.")
                _ (comment "Ordinarily this will be handled by the
                           navigation-2d middleware.")
                nav-2d {:width 250 :height 250 :zoom 0.25 :position [0 250]}]
            (comment "We set the width and height both to 250 so we can")
            (comment "use navigation-2d on 250x250 images. The zoom and")
            (comment "position settings should give the illusion that our")
            (comment "\"camera\" is centered on pixel [0 250] in the image")
            (comment "and zoomed out until everything appears to be a")
            (comment "quarter its original size.")
            (q/set-state! :navigation-2d nav-2d))}
  (let [_ (comment "Ordinarily the state will be passed to your draw function")
        _ (comment "by the fun-mode middleware, but for this example
                   we get it ourselves.")
        state (q/state)
        no-nav-im (q/create-image 250 250 #?(:clj :rgb))
        nav-im (q/create-image 250 250 #?(:clj :rgb))]
    (q/background 255)

    (comment "First lets draw a gradient without navigation applied.")
    (doseq [x (range 250) y (range 250)]
      (q/set-pixel no-nav-im x y (q/color (* 2 x) (* 2 y) (+ x y))))
    #?(:cljs (q/update-pixels no-nav-im))
    (q/set-image 0 0 no-nav-im)

    (comment "Now lets draw a gradient with navigation.")
    (comment "Since we are indexing the nav-px array by")
    (comment "the pixel-coordinates, we must convert them")
    (comment "to world coordinates and calculate the")
    (comment "color based off those.")
    (doseq [x (range 250) y (range 250)]
      (let [[wx wy] (m/screen->world-coords state [x y])]
        (q/set-pixel nav-im x y (q/color (* 2 wx) (* 2 wy) (+ wx wy)))))
    #?(:cljs (q/update-pixels nav-im))
    (q/set-image 250 250 nav-im)))
