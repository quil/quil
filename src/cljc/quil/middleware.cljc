(ns ^{:doc "Quil middleware."}
 quil.middleware
  (:require [quil.middlewares.fun-mode :as fun-mode]
            #?(:clj [quil.middlewares.pause-on-error :as pause-on-error])
            [quil.middlewares.navigation-3d :as navigation-3d]
            [quil.middlewares.navigation-2d :as navigation-2d]))

(defn ^{:requires-bindings false
        :category "Middleware"
        :subcategory nil
        :ns "quil.middleware"
        :added "2.1.0"}
  fun-mode
  "Introduces function mode. Adds 'update' function which takes current
  state and returns new state. Makes all other functions (setup, draw,
  mouse-click, etc) state-aware. See wiki for more details."
  [options]
  (fun-mode/fun-mode options))

#?(:clj
   (defn ^{:requires-bindings false
           :category "Middleware"
           :subcategory nil
           :ns "quil.middleware"
           :added "2.2.0"}
     pause-on-error
     "Pauses sketch if any of user-provided handlers throws error.
  It allows to fix the error on the fly and continue sketch.
  May be good alternative to default '500ms pause if exception'
  behaviour."
     [options]
     (pause-on-error/pause-on-error options)))

(defn ^{:requires-bindings false
        :category "Middleware"
        :subcategory nil
        :ns "quil.middleware"
        :added "2.2.0"}
  navigation-3d
  "Enables navigation in 3D space. Similar to how it is done in
  shooters: WASD navigation, space is go up, drag mouse to look around.
  This middleware requires fun-mode.


  Navigation

  * Drag mouse to look around. You can change settings to bind
    mouse-moved instead of mouse-dragged to look around. See
    customization info below.

  * Keyboard:
    * w - go forward
    * s - go backward
    * a - strafe left
    * d - strafe right
    * space - go up
    * z - go down, can't bind to ctrl, limitation of Processing


  Customization

  You can customize this middleware by providing map as
  :navigation-3d option in defsketch/sketch. Map can have following
  optional keys:

  :position - vector of 3 numbers, initial camera position. Default
              is the same as in 'camera' function.

  :straight - vector of 3 numbers, direction you'll be looking at.
              Default is [0 0 -1] (looking down).

  :up - vector of 3 numbers, 'up' direction. Default is [0 1 0].

  :pixels-in-360 - number, mouse sensitivity. Defines how many pixels
                   you need to move/drag you mouse to rotate 360 degrees.
                   The less the number the more sensitive is mouse.
                   Default is 1000.

  :step-size - number, number of pixels you move on each key event (wasd).
               Default is 20.

  :rotate-on - keyword, either :mouse-dragged or :mouse-moved. Specifies
               on which mouse event camera should rotate. Default is
               :mouse-dragged.


  Accessing position information from sketch

  navigation-3d uses fun-mode under the hood  so all position-related
  information is stored in the state map. It means that you can access in
  draw/update/any handler and modify it if you need to. Position
  information is a map which is stored under :navigation-3d key in the
  state map. Position consists of 3 values: :position, :straight and :up.
  See \"Customization\" section above for more details.

  Usage example:

  (q/defsketch my-sketch
    ...
    :middleware [m/fun-mode m/navigation-3d])

  See wiki article for more(?) details:
  https://github.com/quil/quil/wiki/Navigation-3D"
  [options]
  (navigation-3d/navigation-3d options))

(defn ^{:requires-bindings false
        :category "Middleware"
        :subcategory nil
        :ns "quil.middleware"
        :added "2.2.6"}
  navigation-2d
  "Enables navigation over 2D sketch. Drag mouse to change the center of the
  sketch and mouse wheel controls zoom. This middleware requires fun-mode.

  Customization

  You can customize this middleware by providing map as
  :navigation-2d option in defsketch/sketch. Map can have following
  optional keys:

  :position - vector of 2 numbers, x and y - center of the screen.
              Default is width/2, height/2.

  :zoom - number indicating current zoom level. Default is 1.

  Accessing position information from sketch

  navigation-2d uses fun-mode under the hood so all position-related
  information is stored in the state map. It means that you can access in
  draw/update/any handler and modify it if you need to. Position
  information is a map which is stored under :navigation-2d key in the
  state map. Position consists of 2 values: :position and :zoom.
  See \"Customization\" section above for more details.

  Usage example:

  (q/defsketch my-sketch
    ...
    :middleware [m/fun-mode m/navigation-2d])
"
  [options]
  (navigation-2d/navigation-2d options))
