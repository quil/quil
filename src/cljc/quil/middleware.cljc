(ns ^{:doc "Quil middleware."}
 quil.middleware
  (:require [quil.middlewares.fun-mode :as fun-mode]
            #?(:clj [quil.middlewares.pause-on-error :as pause-on-error])
            [quil.middlewares.navigation-3d :as navigation-3d]
            [quil.middlewares.navigation-2d :as navigation-2d]
            [quil.core :as q]))

(defn ^{:requires-bindings false
        :category "Middleware"
        :subcategory nil
        :ns "quil.middleware"
        :added "2.1.0"}
  fun-mode
  "Introduces `function mode`. Adds `update` function which takes current state
  and returns new state. Makes all other functions (`setup`, `draw`,
  `mouse-click`, etc) state-aware.
  See [wiki](https://github.com/quil/quil/wiki/Functional-mode-%28fun-mode%29)
  for more details."
  [options]
  (fun-mode/fun-mode options))

#?(:clj
   (defn ^{:requires-bindings false
           :category "Middleware"
           :subcategory nil
           :ns "quil.middleware"
           :added "2.2.0"}
     pause-on-error
     "Pauses sketch if any of the user-provided handlers throws an error.
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
  This middleware requires [[fun-mode]].

  Navigation

  * Drag mouse to look around. You can change settings to bind
    mouse-moved instead of mouse-dragged to look around. See
    customization info below.

  * Keyboard:
    * `w` - go forward
    * `s` - go backward
    * `a` - strafe left
    * `d` - strafe right
    * `space` - go up
    * `z` - go down, can't bind to `ctrl`, limitation of Processing

  Customization

  You can customize this middleware by providing a map as
  `:navigation-3d` option in [[quil.sketch/defsketch]]/[[quil.sketch/sketch]].
  The map can have the following optional keys:

  * `:position` - vector of 3 numbers, initial camera position. Default
                  is the same as in [[quil.core/camera]] function.

  * `:straight` - vector of 3 numbers, direction you'll be looking at.
                  Default is `[0 0 -1]` (looking down).

  * `:up` - vector of 3 numbers, 'up' direction. Default is `[0 1 0]`.

  * `:pixels-in-360` - number, mouse sensitivity. Defines how many pixels
                       you need to move/drag your mouse to rotate 360 degrees.
                       The less the number the more sensitive the mouse.
                       Default is `1000`.

  * `:step-size` - number, number of pixels you move on each key event (wasd).
                   Default is `20`.

  * `:rotate-on` - keyword, either `:mouse-dragged` or `:mouse-moved`. Specifies
                   on which mouse event camera should rotate. Default is
                   `:mouse-dragged`.

  Accessing position information from a sketch

  [[navigation-3d]] uses [[fun-mode]] under the hood so all position-related
  information is stored in the state map. It means that you can access in
  draw/update/any handler and modify it if you need to. Position
  information is a map which is stored under `:navigation-3d` key in the
  state map. Position consists of 3 values: `:position`, `:straight` and `:up`.
  See \"Customization\" section above for more details.

  Example:
  ```
  (q/defsketch my-sketch
    ...
    :middleware [m/fun-mode m/navigation-3d])
  ```

  See wiki article for more(?) details:
  https://github.com/quil/quil/wiki/Navigation-3D"
  [options]
  (navigation-3d/navigation-3d options))

(defn ^{:requires-bindings false
        :category "Middleware"
        :subcategory "2D Navigation"
        :ns "quil.middleware"
        :added "2.2.6"}
  navigation-2d
  "Enables navigation over 2D sketch. Drag mouse to change the center of the
  sketch and mouse wheel controls zoom. This middleware requires [[fun-mode]].

  Customization

  You can customize this middleware by providing a map as
  `:navigation-2d` option in [[quil.sketch/defsketch]]/[[quil.sketch/sketch]].
  The map can have the following optional keys:

  * `:position` - vector of 2 numbers, x and y - center of the screen.
                  Default is `(q/width)/2`, `(q/height)/2`.

  * `:zoom` - number indicating current zoom level. Default is `1`.

  * `:mouse-buttons` - set containing zero or more of the keys `:left`,
                       `:right`, and `:center` indicating which mouse
                       buttons are used for panning the screen. Default is
                       `#{:left :right :center}`.

  * `:wrap-draw` - boolean indicating whether [[navigation-2d]] should apply
                   its transformation to the user's draw function. If
                   this is set to false, then only calls wrapped in the
                   [[with-navigation-2d]] macro will be affected by
                   navigation. So set this to false if you have things
                   like UI elements that shouldn't be affected by
                   zooming or panning. Default is `true`.

  * `:width` - the width of the screen-region or image that navigation-2d
               is to be applied to. Default is `(q/width)`.

  * `:height` - the height of the screen-region or image that navigation-2d
                is to be applied to. Default is `(q/height)`.

  Accessing navigation information from a sketch

  [[navigation-2d]] uses [[fun-mode]] under the hood so all navigation-related
  information is stored in the state map. It means that you can access it in
  draw/update/any handler and modify it if you need to. Navigation
  information is a map which is stored under `:navigation-2d` key in the
  state map. It consists of 6 values: `:position`, `:zoom`, `:mouse-buttons`,
  `:wrap-draw`, `:width`, and `:height`. See \"Customization\" section above
  for more details.

  Handling mouse input

  [[navigation-2d]] adds the keys `:world-x` and `:world-y` to the `event`
  maps of the following handlers: `:mouse-entered`, `:mouse-exited`,
  `:mouse-pressed`, `:mouse-released`, `:mouse-clicked`, `:mouse-moved`,
  and `:mouse-dragged`. These keys are used to store the \"world-coordinates\"
  of the mouse. Use these instead of the usual screen-coordinates of the
  mouse in order to compare the mouse's position to on-screen objects whose
  positions are affected by navigation-2d. In addition, the keys `:p-world-x`
  and `:p-world-y` are added to the `event` maps of the `:mouse-moved` and
  `:mouse-dragged` handlers. These are used to store the world-coordinates
  of the mouse from the previous frame. The world-coordinates of the mouse
  can also be accessed via the function [[mouse-world-coords]].

  Example:
  ```
  (q/defsketch my-sketch
    ...
    :middleware [m/fun-mode m/navigation-2d])
  ```"
  [options]
  (navigation-2d/navigation-2d options))

(defmacro ^{:requires-bindings true
            :processing-name nil
            :category "Middleware"
            :subcategory "2D Navigation"
            :ns "quil.middleware"
            :added "3.1.1"}
  with-navigation-2d
  "Performs body with translation and scaling as determined by
  [[navigation-2d]]. Restores current transformation on exit. This macro
  requires [[navigation-2d]] to be enabled. Only use this if navigation-2d's
  `:wrap-draw` option is set to false. Then this macro can be used to
  apply navigation-2d's transformation to part of your draw function
  while leaving other parts (for instance, parts drawing UI elements)
  unaffected."
  [state & body]
  `(navigation-2d/with-navigation-2d ~state ~@body))

(defn ^{:requires-bindings false
        :processing-name nil
        :category "Middleware"
        :subcategory "2D Navigation"
        :ns "quil.middleware"
        :added "3.1.1"}
  world->screen-coords
  "Returns the pixel coordinates that `[x y]` is sent to by
  [[navigation-2d]]. i.e. if the user's draw function tries to
  draw something at `[x y]`, then, after acounting for navigation,
  it will actually get drawn at (world->screen-coords state `[x y]`)
  on the screen. This function requires [[navigation-2d]] to be enabled."
  [state [x y]]
  (navigation-2d/world->screen-coords state [x y]))

(defn ^{:requires-bindings false
        :processing-name nil
        :category "Middleware"
        :subcategory "2D Navigation"
        :ns "quil.middleware"
        :added "3.1.1"}
  screen->world-coords
  "Returns coordinates that [[navigation-2d]] sends to [x y].
  i.e. if something is being drawn at pixel coordinates [x y]
  on the screen after navigation, then the user's draw function
  must have tried to draw it at (screen->world-coords state [x y]).
  This function requires [[navigation-2d]] to be enabled."
  [state [x y]]
  (navigation-2d/screen->world-coords state [x y]))

(defn ^{:requires-bindings true
        :processing-name nil
        :category "Middleware"
        :subcategory "2D Navigation"
        :ns "quil.middleware"
        :added "3.1.1"}
  mouse-world-coords
  "Returns coordinates `[x y]` that [[navigation-2d]] sends to the
  current mouse coordinates. Use this to compare the mouse's position
  with the position of objects that are affected by [[navigation-2d]].
  This function requires [[navigation-2d]] to be enabled."
  [state]
  (navigation-2d/screen->world-coords state [(q/mouse-x) (q/mouse-y)]))
