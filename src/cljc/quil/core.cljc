(ns ^{:doc "Wrappers and extensions around the core Processing.org API."}
 quil.core
  #?(:clj
     (:import [processing.core PApplet PImage PGraphics PFont PConstants PShape]
              [processing.opengl PShader]
              [java.awt.event KeyEvent]))
  #?(:clj
     (:require quil.sketch
               [clojure.set]
               [quil.helpers.docs :as docs]
               [quil.util :as u]
               [quil.applet :as ap])

     :cljs
     (:require clojure.string
               cljsjs.p5
               [quil.sketch :as ap :include-macros true]
               [quil.util :as u :include-macros true])))

(def ^{:dynamic true}
  *graphics* nil)

(def ^{:private true} no-fill-prop "no-fill-quil")

(declare pixels)

(defn
  ^{:requires-bindings true
    :category "Environment"
    :subcategory nil
    :added "2.0"
    :tag PGraphics}
  current-graphics
  "Graphics currently used for drawing. By default it is sketch graphics,
  but if called inside [[with-graphics]] macro - graphics passed to the macro
  is returned. This method should be used if you need to call some methods
  that are not implemented by quil.

  Example:
  ```
  (.beginDraw (current-graphics))
  ```"
  []
  (or *graphics*
      #?(:clj (.-g (ap/current-applet))
         :cljs (ap/current-applet))))

;; -------------------- PConstants section -----------------------

(u/generate-quil-constants
 #?(:clj :clj :cljs :cljs)
 arc-modes (:open :chord :pie)
 #?@(:cljs (angle-modes (:radians :degrees)))
 shape-modes (:points :lines :triangles :triangle-fan :triangle-strip :quads :quad-strip)
 blend-modes (:blend :add :subtract :darkest :lightest :difference :exclusion :multiply
                     :screen :overlay :replace :hard-light :soft-light :dodge :burn)
 #?@(:clj (color-modes (:rgb :hsb))
     :cljs (color-modes (:rgb :hsb :hsl)))
 image-formats (:rgb :argb :alpha)
 ellipse-modes (:center :radius :corner :corners)
 hint-options (:enable-async-saveframe :disable-async-saveframe
                                       :enable-depth-test :disable-depth-test
                                       :enable-depth-sort :disable-depth-sort
                                       :enable-depth-mask :disable-depth-mask
                                       :enable-opengl-errors :disable-opengl-errors
                                       :enable-optimized-stroke :disable-optimized-stroke
                                       :enable-stroke-perspective :disable-stroke-perspective
                                       :enable-stroke-pure :disable-stroke-pure
                                       :enable-texture-mipmaps :disable-texture-mipmaps)
 image-modes (:corner :corners :center)
 rect-modes (:corner :corners :center :radius)
 p-shape-modes (:corner :corners :center)
 stroke-cap-modes (:square :round :project :model)
 stroke-join-modes (:miter :bevel :round)
 horizontal-alignment-modes (:left :center :right)
 vertical-alignment-modes (:top :bottom :center :baseline)
 #?@(:clj (text-modes (:model :shape)))
 #?@(:cljs (text-styles (:normal :italic :bold)))
 texture-modes (:image :normal)
 texture-wrap-modes (:clamp :repeat)
 filter-modes (:threshold :gray :invert :posterize :blur :opaque :erode :dilate)
 cursor-modes (:arrow :cross :hand :move :text :wait))

;;; Useful trig constants
#?(:clj (def PI  (float Math/PI))
   :cljs (def PI  (.-PI js/Math)))
(def HALF-PI    (/ PI (float 2.0)))
(def THIRD-PI   (/ PI (float 3.0)))
(def QUARTER-PI (/ PI (float 4.0)))
(def TWO-PI     (* PI (float 2.0)))

(def DEG-TO-RAD (/ PI (float 180.0)))
(def RAD-TO-DEG (/ (float 180.0) PI))

#?(:clj
   (def  ^{:private true}
     KEY-CODES {KeyEvent/VK_UP      :up
                KeyEvent/VK_DOWN    :down
                KeyEvent/VK_LEFT    :left
                KeyEvent/VK_RIGHT   :right
                KeyEvent/VK_ALT     :alt
                KeyEvent/VK_CONTROL :control
                KeyEvent/VK_SHIFT   :shift
                KeyEvent/VK_WINDOWS :command
                KeyEvent/VK_META    :command
                KeyEvent/VK_F1      :f1
                KeyEvent/VK_F2      :f2
                KeyEvent/VK_F3      :f3
                KeyEvent/VK_F4      :f4
                KeyEvent/VK_F5      :f5
                KeyEvent/VK_F6      :f6
                KeyEvent/VK_F7      :f7
                KeyEvent/VK_F8      :f8
                KeyEvent/VK_F9      :f9
                KeyEvent/VK_F10     :f10
                KeyEvent/VK_F11     :f11
                KeyEvent/VK_F12     :f12
                KeyEvent/VK_F13     :f13
                KeyEvent/VK_F14     :f14
                KeyEvent/VK_F15     :f15
                KeyEvent/VK_F16     :f16
                KeyEvent/VK_F17     :f17
                KeyEvent/VK_F18     :f18
                KeyEvent/VK_F19     :f19
                KeyEvent/VK_F20     :f20
                KeyEvent/VK_F21     :f21
                KeyEvent/VK_F22     :f22
                KeyEvent/VK_F23     :f23
                KeyEvent/VK_F24     :f24})

   :cljs
   (def ^{:private true}
     KEY-CODES {38 :up
                40 :down
                37 :left
                39 :right
                18 :alt
                17 :control
                16 :shift
                157 :command
                112 :f1
                113 :f2
                114 :f3
                115 :f4
                116 :f5
                117 :f6
                118 :f7
                119 :f8
                120 :f9
                121 :f10
                122 :f11
                123 :f12}))

(def ^{:private true}
  KEY-MAP {" " :space})

;; ------------------ end PConstants section ---------------------

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "getSketchById()"
       :category nil
       :subcategory nil
       :added "1.0"}
     get-sketch-by-id
     "Returns sketch object by id of canvas element of sketch."
     [id]
     (if-let [elem (.getElementById js/document id)]
       (.-processing-obj elem)
       nil)))

(defmacro with-sketch [applet & body]
  (when-not (u/clj-compilation?)
    `(quil.sketch/with-sketch ~applet ~@body)))

(defn
  ^{:requires-bindings true
    :category "State"
    :subcategory nil
    :added "1.0"}
  state-atom
  "Retrieve sketch-specific state-atom. All changes to the
  atom will be reflected in the state.

  (set-state! :foo 1)
  (state :foo) ;=> 1
  (swap! (state-atom) update-in [:foo] inc)
  (state :foo) ;=> 2"
  []
  #?(:clj (-> (ap/current-applet) meta :state)
     :cljs (. (ap/current-applet) -quil)))

(defn- internal-state
  "Returns atom representing internal sketch state. Can be used by
  functions to save implementation-specific state. This state is
  supposed to be visible to users."
  []
  #?(:clj (-> (ap/current-applet) meta :internal-state)
     :cljs (. (ap/current-applet) -quil-internal-state)))

(defn
  ^{:requires-bindings true
    :category "State"
    :subcategory nil
    :added "1.0"}
  state
  "Retrieve sketch-specific state by key. Must initially call
  set-state! to store state. If no parameter passed whole
  state map is returned.

  (set-state! :foo 1)
  (state :foo) ;=> 1
  (state) ;=> {:foo 1}"
  ([] @(state-atom))

  ([key] (let [state (state)]
           (when-not (contains? state key)
             (throw #?(:clj (Exception. (str "Unable to find state with key: " key))
                       :cljs (js/Error (str "Unable to find state with key: " key)))))
           (get state key))))

(defn
  ^{:requires-bindings true
    :category "State"
    :subcategory nil
    :added "1.0"}
  set-state!
  "Set sketch-specific state. May only be called once (ideally in the
  setup function). Subsequent calls have no effect.

  Example:
  ```
  (set-state! :foo 1 :bar (atom true) :baz (/ (width) 2))
  ```"
  [& state-vals]
  (let [state* (state-atom)]
    (when-not @state*
      (let [state-map (apply hash-map state-vals)]
        (reset! state* state-map)))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :category "Shader"
       :subcategory nil
       :added "3.0.0"}
     set-uniform
     "Set a uniform variables inside a shader to modify the effect
     while the program is running."
     [shader uniform-name data]
     (.setUniform shader uniform-name data)))

(defn
  ^{:requires-bindings false
    :processing-name "abs()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  abs
  "Calculates the absolute value (magnitude) of a number. The
  absolute value of a number is always positive. Dynamically casts to
  an `int` or `float` appropriately for Clojure."
  [n]
  #?(:clj
     (if (u/int-like? n)
       (PApplet/abs (int n))
       (PApplet/abs (float n)))
     :cljs
     (.abs (ap/current-applet) n)))

(defn
  ^{:requires-bindings false
    :processing-name "acos()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  acos
  "The inverse of [[cos]], returns the arc cosine of a value. This
  function expects the values in the range of -1 to 1 and values are
  returned in the range 0 to `Math/PI` (3.1415927)."
  [n]
  #?(:clj (PApplet/acos (float n))
     :cljs (.acos (ap/current-applet) n)))

(defn
  ^{:requires-bindings true
    :processing-name "alpha()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  alpha
  "Extracts the alpha value from a color."
  [color]
  (.alpha (current-graphics) color))

(defn
  ^{:requires-bindings true
    :processing-name "ambient()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  ambient
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The
  color components set through the parameters define the
  reflectance. For example in the default [[color-mode]], setting `r=255,
  g=126, b=0`, would cause all the red light to reflect and half of the
  green light to reflect. Used in combination with [[emissive]], [[specular]],
  and [[shininess]] in setting the material properties of shapes."
  ([gray]
   #?(:clj (.ambient (current-graphics) (float gray))
      :cljs (.ambientMaterial (current-graphics) (float gray))))
  ([r g b]
   #?(:clj (.ambient (current-graphics) (float r) (float g) (float b))
      :cljs (.ambientMaterial (current-graphics) (float r) (float g) (float b)))))

(defn
  ^{:requires-bindings true
    :processing-name "ambientLight()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  ambient-light
  "Adds an ambient light. Ambient light doesn't come from a specific direction,
  the rays of light have bounced around so much that objects are
  evenly lit from all sides. Ambient lights are almost always used in
  combination with other types of lights. Lights need to be included
  in the draw to remain persistent in a looping program. Placing them
  in the setup of a looping program will cause them to only have an
  effect the first time through the loop. The effect of the
  parameters is determined by the current [[color-mode]]."
  ([red green blue]
   (.ambientLight (current-graphics) (float red) (float green) (float blue)))
  ([red green blue x y z]
   (.ambientLight (current-graphics) (float red) (float green) (float blue)
                  (float x) (float y) (float z))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "angleMode()"
       :category "Math"
       :subcategory "Trigonometry"
       :added "2.8.0"}
     angle-mode
     "Sets the current mode of p5 to given `mode`.
     Options are:
     * `:radians` **(default)**
     * `:degrees`"
     ([mode]
      (let [mode (u/resolve-constant-key mode angle-modes)]
        (.angleMode (current-graphics) mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "applyMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  apply-matrix
  "Multiplies the current matrix by the one specified through the
  parameters. This is very slow because it will try to calculate the
  inverse of the transform, so avoid it whenever possible. The
  equivalent function in OpenGL is `glMultMatrix()`.

  Note that cljs has only 2d version and arguments differ see
  https://p5js.org/reference/#/p5/applyMatrix"
  #?(:clj
     ([n00 n01 n02 n10 n11 n12]
      (.applyMatrix (current-graphics)
                    (float n00) (float n01) (float n02)
                    (float n10) (float n11) (float n12))))
  #?(:clj
     ([n00 n01 n02 n03
       n10 n11 n12 n13
       n20 n21 n22 n23
       n30 n31 n32 n33]
      (.applyMatrix (current-graphics)
                    (float n00) (float n01) (float n02) (float n03)
                    (float n10) (float n11) (float n12) (float n13)
                    (float n20) (float n21) (float n22) (float n23)
                    (float n30) (float n31) (float n32) (float n33))))
  #?(:cljs
     ([a b c d e f]
      (.applyMatrix (current-graphics)
                    (float a) (float b) (float c)
                    (float d) (float e) (float f)))))

(defn
  ^{:requires-bindings true
    :processing-name "arc()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  arc
  "Draws an arc in the display window. Arcs are drawn along the outer
  edge of an ellipse defined by the `x`, `y`, `width` and `height`
  parameters. The origin or the arc's ellipse may be changed with the
  [[ellipse-mode]] function. The `start` and `stop` parameters specify the
  angles at which to draw the arc. The `mode` is either `:open`, `:chord` or `:pie`."
  ([x y width height start stop]
   (.arc (current-graphics) (float x) (float y) (float width) (float height)
         (float start) (float stop)))

  ([x y width height start stop mode]
   (let [arc-mode (u/resolve-constant-key mode arc-modes)]
     (.arc (current-graphics) (float x) (float y) (float width) (float height)
           (float start) (float stop) arc-mode))))

(defn
  ^{:requires-bindings false
    :processing-name "asin()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  asin
  "The inverse of [[sin]], returns the arc sine of a value. This function
  expects the values in the range of -1 to 1 and values are returned
  in the range `-PI/2` to `PI/2`."
  [n]
  #?(:clj (PApplet/asin (float n))
     :cljs (.asin (ap/current-applet) n)))

(defn
  ^{:requires-bindings false
    :processing-name "atan()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  atan
  "The inverse of [[tan]], returns the arc tangent of a value. This
  function expects the values in the range of -Infinity to
  Infinity (exclusive) and values are returned in the range `-PI/2` to
  `PI/2`."
  [n]
  #?(:clj (PApplet/atan (float n))
     :cljs (.atan (ap/current-applet) n)))

(defn
  ^{:requires-bindings false
    :processing-name "atan2()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  atan2
  "Calculates the angle (in radians) from a specified point to the
  coordinate origin as measured from the positive x-axis. Values are
  returned as a `float` in the range from `PI` to `-PI`. The [[atan2]] function
  is most often used for orienting geometry to the position of the
  cursor. Note: The y-coordinate of the point is the first parameter
  and the x-coordinate is the second due to the structure of
  calculating the tangent."
  [y x]
  #?(:clj (PApplet/atan2 (float y) (float x))
     :cljs (.atan2 (ap/current-applet) y x)))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name "PFont.list()"
       :category "Typography"
       :subcategory "Loading & Displaying"
       :added "1.0"}
     available-fonts
     "A sequence of strings representing the fonts on this system
    available for use.

    Because of limitations in Java, not all fonts can be used and some
    might work with one operating system and not others. When sharing a
    sketch with other people or posting it on the web, you may need to
    include a `.ttf` or `.otf` version of your font in the data directory of
    the sketch because other people might not have the font installed on
    their computer. Only fonts that can legally be distributed should be
    included with a sketch."
     []
     (seq (PFont/list))))

(defn
  ^{:requires-bindings true
    :processing-name "background()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  background
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  [[create-graphics]]. Converts args to `floats`."
  ([gray] (.background (current-graphics) (float gray)))
  ([gray alpha] (.background (current-graphics) (float gray) (float alpha)))
  ([r g b] (.background (current-graphics) (float r) (float g) (float b)))
  ([r g b a] (.background (current-graphics) (float r) (float g) (float b) (float a))))

(defn
  ^{:requires-bindings true
    :processing-name "background()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  background-image
  "Specify an image to be used as the background for a sketch. Its
  width and height must be the same size as the sketch window. Images
  used as background will ignore the current [[tint]] setting."
  [^PImage img]
  (.background (current-graphics) img))

(defn
  ^{:requires-bindings true
    :processing-name "beginContour()"
    :category "Shape"
    :subcategory "Vertex"
    :added "2.0"}
  begin-contour
  "Use the [[begin-contour]] and [[end-contour]] function to create negative
  shapes within shapes. These functions can only be used within a
  [[begin-shape]]/[[end-shape]] pair and they only work with the `:p2d` and `:p3d`
  renderers."
  []
  (.beginContour (current-graphics)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "beginRaw()"
       :category "Output"
       :subcategory "Files"
       :added "1.0"}
     begin-raw
     "Enables the creation of vectors from 3D data. Requires
  corresponding [[end-raw]] command. These commands will grab the shape
  data just before it is rendered to the screen. At this stage, your
  entire scene is nothing but a long list of individual lines and
  triangles. This means that a shape created with sphere method will
  be made up of hundreds of triangles, rather than a single object. Or
  that a multi-segment line shape (such as a curve) will be rendered
  as individual segments."
     ([renderer filename]
      (.beginRaw (ap/current-applet) (ap/resolve-renderer renderer) (u/absolute-path filename)))))

(defn
  ^{:requires-bindings true
    :processing-name "beginShape()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  begin-shape
  "Enables the creation of complex forms. [[begin-shape]] begins recording
  vertices for a shape and [[end-shape]] stops recording. Use the `mode`
  keyword to specify which shape to create from the provided
  vertices. With no mode specified, the shape can be any irregular
  polygon.

  The available mode keywords are `:points`, `:lines`, `:triangles`,
                                  `:triangle-fan`, `:triangle-strip`,
                                  `:quads`, `:quad-strip`.

  After calling the [[begin-shape]] function, a series of vertex commands
  must follow. To stop drawing the shape, call [[end-shape]]. The [[vertex]]
  function with two parameters specifies a position in 2D and the
  [[vertex]] function with three parameters specifies a position in
  3D. Each shape will be outlined with the current stroke color and
  filled with the fill color.

  Transformations such as [[translate]], [[rotate]], and [[scale]] do not work
  within [[begin-shape]]. It is also not possible to use other shapes,
  such as [[ellipse]] or [[rect]] within [[begin-shape]]."
  ([] (.beginShape (current-graphics)))
  ([mode]
   (let [mode (u/resolve-constant-key mode shape-modes)]
     (.beginShape (current-graphics) (int mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "bezier()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier
  "Draws a Bezier curve on the screen. These curves are defined by a
  series of anchor and control points. The first two parameters
  specify the first anchor point and the last two parameters specify
  the other anchor point. The middle parameters specify the control
  points which define the shape of the curve."
  ([x1 y1 cx1 cy1 cx2 cy2 x2 y2]
   (.bezier (current-graphics)
            (float x1) (float y1)
            (float cx1) (float cy1)
            (float cx2) (float cy2)
            (float x2) (float y2)))
  ([x1 y1 z1 cx1 cy1 cz1 cx2 cy2 cz2 x2 y2 z2]
   (.bezier (current-graphics)
            (float x1) (float y1) (float z1)
            (float cx1) (float cy1) (float cz1)
            (float cx2) (float cy2) (float cz2)
            (float x2) (float y2) (float z2))))

(defn
  ^{:requires-bindings true
    :processing-name "bezierDetail()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier-detail
  "Sets the resolution at which Beziers display. The **default** value is
  20. This function is only useful when using the `:p3d` or `:opengl`
  renderer as the default (`:java2d`) renderer does not use this
  information."
  [detail]
  (.bezierDetail (current-graphics) (int detail)))

(defn
  ^{:requires-bindings true
    :processing-name "bezierPoint()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier-point
  "Evaluates the Bezier at point `t` for points `a`, `b`, `c`, `d`. The
  parameter `t` varies between 0 and 1, `a` and `d` are points on the curve,
  and `b` and `c` are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a bezier curve at `t`."
  [a b c d t]
  (.bezierPoint (current-graphics) (float a) (float b) (float c)
                (float d) (float t)))

(defn
  ^{:requires-bindings true
    :processing-name "bezierTangent()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier-tangent
  "Calculates the tangent of a point on a Bezier curve.
  (See http://en.wikipedia.org/wiki/Tangent)"
  [a b c d t]
  (.bezierTangent (current-graphics) (float a) (float b) (float c)
                  (float d) (float t)))

(defn
  ^{:requires-bindings true
    :processing-name "bezierVertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  bezier-vertex
  "Specifies vertex coordinates for Bezier curves. Each call to
  [[bezier-vertex]] defines the position of two control points and one
  anchor point of a Bezier curve, adding a new segment to a line or
  shape. The first time [[bezier-vertex]] is used within a [[begin-shape]]
  call, it must be prefaced with a call to [[vertex]] to set the first
  anchor point. This function must be used between [[begin-shape]] and
  [[end-shape]] and only when there is no parameter specified to
  [[begin-shape]]."
  ([cx1 cy1 cx2 cy2 x y]
   (.bezierVertex (current-graphics)
                  (float cx1) (float cy1)
                  (float cx2) (float cy2)
                  (float x) (float y)))
  ([cx1 cy1 cz1 cx2 cy2 cz2 x y z]
   (.bezierVertex (current-graphics)
                  (float cx1) (float cy1) (float cz1)
                  (float cx2) (float cy2) (float cz2)
                  (float x) (float y) (float z))))

(defn
  ^{:require-binding false
    :processing-name "binary()"
    :category "Data"
    :subcategory "Conversion"
    :added "1.0"}
  binary
  "Returns a `string` representing the binary value of an `int`, `char` or
  `byte`. When converting an `int` to a `string`, it is possible to specify
  the number of digits used."
  ([val]
   #?(:clj (PApplet/binary (int val))
      :cljs (.toString val 2)))
  ([val num-digits]
   #?(:clj (PApplet/binary (int val) (int num-digits))
      :cljs (let [full (binary val)]
              (subs full (- (count full) num-digits) (count full))))))

(defn
  ^{:requires-bindings true
    :processing-name "blend()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  blend
  "Blends a region of pixels from one image into another with full alpha
  channel support. If `src` is not specified it defaults to [[current-graphics]].
  If dest is not specified it defaults to [[current-graphics]].

  Note: it is recommended to use the [[blend-mode]] function instead of this one.

  Available blend modes are:

  * `:blend`      - linear interpolation of colours: C = A*factor + B
  * `:add`        - additive blending with white clip:
                                                C = min(A*factor + B, 255)
  * `:darkest`    - only the darkest colour succeeds:
                                                C = min(A*factor, B)
  * `:lightest`   - only the lightest colour succeeds:
                                                C = max(A*factor, B)
  * `:difference` - subtract colors from underlying image.
  * `:exclusion`  - similar to `:difference`, but less extreme.
  * `:multiply`   - Multiply the colors, result will always be darker.
  * `:screen`     - Opposite multiply, uses inverse values of the colors.
  * `:overlay`    - A mix of `:multiply` and `:screen`. Multiplies dark values
                    and screens light values.
  * `:hard-light` - `:screen` when greater than 50% gray, `:multiply` when
                    lower.
  * `:soft-light` - Mix of `:darkest` and `:lightest`. Works like :overlay,
                    but not as harsh.
  * `:dodge`      - Lightens light tones and increases contrast, ignores
                    darks.
                    Called \"Color Dodge\" in Illustrator and Photoshop.
  * `:burn`       - Darker areas are applied, increasing contrast, ignores
                    lights. Called \"Color Burn\" in Illustrator and
                    Photoshop.

  In clj the following blend modes are also supported:
  `:subtract`   - subtractive blending with black clip:
                                            C = max(B - A*factor, 0)

  In cljs the following blend modes are also supported:
  `:replace`    - the pixels entirely replace the others and don't utilize
                  alpha (transparency) values."
  ([x y width height dx dy dwidth dheight mode]
   (blend (current-graphics) (current-graphics) x y width height dx dy dwidth dheight mode))
  ([^PImage src-img x y width height dx dy dwidth dheight mode]
   (blend src-img (current-graphics) x y width height dx dy dwidth dheight mode))
  ([^PImage src-img ^PImage dest-img x y width height dx dy dwidth dheight mode]
   (let [mode (u/resolve-constant-key mode blend-modes)]
     (.blend dest-img src-img
             (int x) (int y) (int width) (int height)
             (int dx) (int dy) (int dwidth) (int dheight) mode))))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name "blendColor()"
       :processing-link nil
       :category "Color"
       :subcategory "Creating & Reading"
       :added "1.0"}
     blend-color
     "Blends two color values together based on the blending mode given specified
     with the mode keyword.

     Available blend modes are:

     * `:blend`      - linear interpolation of colours: C = A*factor + B
     * `:add`        - additive blending with white clip:
                                                   C = min(A*factor + B, 255)
     * `:subtract`   - subtractive blending with black clip:
                                                   C = max(B - A*factor, 0)
     * `:darkest`    - only the darkest colour succeeds:
                                                   C = min(A*factor, B)
     * `:lightest`   - only the lightest colour succeeds:
                                                   C = max(A*factor, B)
     * `:difference` - subtract colors from underlying image.
     * `:exclusion`  - similar to :difference, but less extreme.
     * `:multiply`   - Multiply the colors, result will always be darker.
     * `:screen`     - Opposite multiply, uses inverse values of the colors.
     * `:overlay`    - A mix of :multiply and :screen. Multiplies dark values
                       and screens light values.
     * `:hard-light` - :screen when greater than 50% gray, :multiply when
                       lower.
     * `:soft-light` - Mix of :darkest and :lightest. Works like :overlay,
                       but not as harsh.
     * `:dodge`      - Lightens light tones and increases contrast, ignores
                       darks.
                       Called \"Color Dodge\" in Illustrator and Photoshop.
     * `:burn`       - Darker areas are applied, increasing contrast, ignores
                       lights. Called \"Color Burn\" in Illustrator and
                       Photoshop."
     [c1 c2 mode]
     (let [mode (u/resolve-constant-key mode blend-modes)]
       (PApplet/blendColor (u/clj-unchecked-int c1) (u/clj-unchecked-int c2) (int mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "blendMode()"
    :category "Image"
    :subcategory "Rendering"
    :added "2.0"}
  blend-mode
  "Blends the pixels in the display window according to the defined mode.
  There is a choice of the following modes to blend the source pixels (A)
  with the ones of pixels already in the display window (B):

  * `:blend`      - linear interpolation of colours: C = A*factor + B
  * `:add`        - additive blending with white clip:
                                                C = min(A*factor + B, 255)
  * `:subtract`   - subtractive blending with black clip:
                                                C = max(B - A*factor, 0)
  * `:darkest`    - only the darkest colour succeeds:
                                                C = min(A*factor, B)
  * `:lightest`   - only the lightest colour succeeds:
                                                C = max(A*factor, B)
  * `:difference` - subtract colors from underlying image.
  * `:exclusion`  - similar to `:difference`, but less extreme.
  * `:multiply`   - Multiply the colors, result will always be darker.
  * `:screen`     - Opposite multiply, uses inverse values of the colors.
  * `:replace`    - the pixels entirely replace the others and don't utilize
                    alpha (transparency) values.
  * `:overlay`    - mix of `:multiply` and `:screen`. Multiplies dark values,
                    and screens light values.
  * `:hard-light` - :screen when greater than 50% gray, `:multiply` when lower.
  * `:soft-light` - mix of `:darkest` and `:lightest`. Works like :overlay, but
                    not as harsh.
  * `:dodge`      - lightens light tones and increases contrast, ignores darks.
  * `:burn`       - darker areas are applied, increasing contrast, ignores
                    lights.

  Note: in clj `:hard-light`, `:soft-light`, `:overlay`, `:dodge`, `:burn`
  modes are not supported. In cljs `:subtract` mode is not supported.

  factor is the alpha value of the pixel being drawn"
  ([mode]
   (let [mode (u/resolve-constant-key mode blend-modes)]
     (.blendMode (current-graphics) mode))))

(defn
  ^{:requires-bindings true
    :processing-name "blue()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  blue
  "Extracts the blue value from a color, scaled to match current color-mode.
  Returns a `float`."
  [color]
  (.blue (current-graphics) (u/clj-unchecked-int color)))

(defn
  ^{:requires-bindings true
    :processing-name "box()"
    :category "Shape"
    :subcategory "3D Primitives"
    :added "1.0"}
  box
  "Creates an extruded rectangle."
  ([size] (.box (current-graphics) (float size)))
  ([width height depth] (.box (current-graphics) (float width) (float height) (float depth))))

(defn
  ^{:requires-bindings true
    :processing-name "brightness()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  brightness
  "Extracts the brightness value from a color. Returns a `float`."
  [color]
  (.brightness (current-graphics) (u/clj-unchecked-int color)))

(defn
  ^{:requires-bindings true
    :processing-name "camera()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  camera
  "Sets the position of the camera through setting the eye position,
  the center of the scene, and which axis is facing upward. Moving the
  eye position and the direction it is pointing (the center of the
  scene) allows the images to be seen from different angles. The
  version without any parameters sets the camera to the default
  position, pointing to the center of the display window with the Y
  axis as up. The default values are:

  * `eyeX`    - `(/ (width) 2.0)`
  * `eyeY`    - `(/ (height) 2.0)`
  * `eyeZ`    - `(/ (/ (height) 2.0) (tan (/ (* Math/PI 60.0) 360.0)))`
  * `centerX` - `(/ (width) 2.0)`
  * `centerY` - `(/ (height) 2.0)`
  * `centerZ` - `0`
  * `upX`     - `0`
  * `upY`     - `1`
  * `upZ`     - `0`

  Similar to `gluLookAt()` in OpenGL, but it first clears the
  current camera settings."
  ([] (.camera (current-graphics)))
  ([eyeX eyeY eyeZ centerX centerY centerZ upX upY upZ]
   (.camera (current-graphics) (float eyeX) (float eyeY) (float eyeZ)
            (float centerX) (float centerY) (float centerZ)
            (float upX) (float upY) (float upZ))))

(defn
  ^{:requires-bindings false
    :processing-name "ceil()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  ceil
  "Calculates the closest `int` value that is greater than or equal to
  the value of the parameter. For example, `(ceil 9.03)` returns the
  value 10."
  [n]
  #?(:clj (PApplet/ceil (float n))
     :cljs (.ceil (ap/current-applet) n)))

(defn
  ^{:requires-bindings true
    :processing-name "clear()"
    :category "Color"
    :subcategory "Setting"
    :added "2.4.0"}
  clear
  "Clears the pixels within a buffer. This function only works on
  graphics objects created with the [[create-graphics]] function meaning
  that you should call it only inside [[with-graphics]] macro. Unlike
  the main graphics context (the display window), pixels in additional
  graphics areas created with [[create-graphics]] can be entirely or
  partially transparent. This function clears everything in a graphics
  object to make all of the pixels 100% transparent."
  []
  (.clear (current-graphics)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "clip()"
       :category "Rendering"
       :subcategory nil
       :added "2.4.0"}
     clip
     "Limits the rendering to the boundaries of a rectangle defined by
  the parameters. The boundaries are drawn based on the state of
  the [[image-mode]] function, either `:corner`, `:corners`, or `:center`.
  To disable use [[no-clip]]."
     [x y w h]
     (.clip (current-graphics) (float x) (float y) (float w) (float h))))

(defn
  ^{:requires-bindings true
    :processing-name "color()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  color
  "Creates an integer representation of a color. The parameters are
  interpreted as RGB or HSB values depending on the current
  [[color-mode]]. The default mode is RGB values from 0 to 255 and
  therefore, the function call `(color 255 204 0)` will return a bright
  yellow. Args are cast to floats.

  * r - red or hue value
  * g - green or saturation value
  * b - blue or brightness value
  * a - alpha value"
  ([gray] (.color (current-graphics) (float gray)))
  ([gray alpha] (.color (current-graphics) (float gray) (float alpha)))
  ([r g b] (.color (current-graphics) (float r) (float g) (float b)))
  ([r g b a] (.color (current-graphics) (float r) (float g) (float b) (float a))))

(defn
  ^{:requires-bindings true
    :processing-name "colorMode()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  color-mode
  "Changes the way Processing interprets color data. Available modes
  are `:rgb` and `:hsb` (and `:hsl` in clojurescript).
  By default, the parameters for [[fill]], [[stroke]],
  [[background]], and [[color]] are defined by values between 0 and 255 using
  the `:rgb` color model. The [[color-mode]] function is used to change the
  numerical range used for specifying colors and to switch color
  systems. For example, calling
  `(color-mode :rgb 1.0)` will specify that values are specified between
  0 and 1. The limits for defining colors are altered by setting the
  parameters range1, range2, range3, and range 4."
  ([mode]
   (let [mode (u/resolve-constant-key mode color-modes)]
     (.colorMode (current-graphics) mode #?(:cljs 255))))
  ([mode max]
   (let [mode (u/resolve-constant-key mode color-modes)]
     (.colorMode (current-graphics) mode (float max))))
  ([mode max-x max-y max-z]
   (let [mode (u/resolve-constant-key mode color-modes)]
     (.colorMode (current-graphics) mode (float max-x) (float max-y) (float max-z))))
  ([mode max-x max-y max-z max-a]
   (let [mode (u/resolve-constant-key mode color-modes)]
     (.colorMode (current-graphics) mode (float max-x) (float max-y) (float max-z) (float max-a)))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "cone()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "3.0.0"}
     cone
     "Draw a cone with given `radius` and `height`.

      Optional parameters:
        * `detail-x` - number of segments, the more segments the smoother geometry default is 24
        * `detail-y` - number of segments, the more segments the smoother geometry default is 24
        * `cap`      - whether to draw the base of the cone"
     ([radius height]
      (.cone (current-graphics) (float radius) (float height)))
     ([radius height detail-x]
      (.cone (current-graphics) (float radius) (float height) (int detail-x)))
     ([radius height detail-x detail-y]
      (.cone (current-graphics) (float radius) (float height) (int detail-x) (int detail-y)))
     ([radius height detail-x detail-y cap]
      (.cone (current-graphics) (float radius) (float height) (int detail-x) (int detail-y) (boolean cap)))))

(defn
  ^{:requires-bindings false
    :processing-name "constrain()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  constrain
  "Constrains a value to not exceed a maximum and minimum value."
  [amt low high]
  #?(:clj
     (if (u/int-like? amt)
       (PApplet/constrain (int amt) (int low) (int high))
       (PApplet/constrain (float amt) (float low) (float high)))
     :cljs (.constrain (ap/current-applet) amt low high)))

(defn
  ^{:requires-bindings true
    :processing-name "copy()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  copy
  "Copies a region of pixels from one image to another. If `src-img`
  is not specified it defaults to [[current-graphics]]. If `dest-img` is not
  specified - it defaults to [[current-graphics]]. If the source
  and destination regions aren't the same size, it will automatically
  resize the source pixels to fit the specified target region. No
  alpha information is used in the process, however if the source
  image has an alpha channel set, it will be copied as well. "
  ([[sx sy swidth sheight] [dx dy dwidth dheight]]
   (.copy (current-graphics)
          (int sx) (int sy) (int swidth) (int sheight)
          (int dx) (int dy) (int dwidth) (int dheight)))

  ([^PImage src-img [sx sy swidth sheight] [dx dy dwidth dheight]]
   (copy src-img (current-graphics) [sx sy swidth sheight] [dx dy dwidth dheight]))

  ([^PImage src-img ^PImage dest-img [sx sy swidth sheight] [dx dy dwidth dheight]]
   (.copy dest-img src-img
          (int sx) (int sy) (int swidth) (int sheight)
          (int dx) (int dy) (int dwidth) (int dheight))))

(defn
  ^{:requires-bindings false
    :processing-name "cos()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  cos
  "Calculates the cosine of an angle. This function expects the values
  of the angle parameter to be provided in radians (values from 0 to
  Math/PI*2). Values are returned in the range -1 to 1."
  [angle]
  #?(:clj (PApplet/cos (float angle))
     :cljs (.cos (ap/current-applet) angle)))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name nil
       :category "Typography"
       :subcategory "Loading & Displaying"
       :added "1.0"}
     font-available?
     "Returns `true` if font (specified as a string) is available on this
  system, `false` otherwise"
     [font-str]
     (if (some #{font-str} (available-fonts))
       true
       false)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "createFont()"
       :category "Typography"
       :subcategory "Loading & Displaying"
       :added "1.0"}
     create-font
     "Dynamically converts a font to the format used by Processing (a
     `PFont`) from either a font name that's installed on the computer, or
     from a `.ttf` or `.otf` file inside the sketches 'data' folder. This
     function is an advanced feature for precise control.

     Use [[available-fonts]] to obtain the names for the fonts recognized by
     the computer and are compatible with this function.

     The `size` parameter states the font size you want to generate. The
     `smooth` parameter specifies if the font should be antialiased or not,
     and the `charset` parameter is an array of chars that specifies the
     characters to generate.

     This function creates a bitmapped version of a font. It loads a font
     by name, and converts it to a series of images based on the size of
     the font. When possible, the text function will use a native font
     rather than the bitmapped version created behind the scenes with
     create-font. For instance, when using the default renderer
     setting (JAVA2D), the actual native version of the font will be
     employed by the sketch, improving drawing quality and
     performance. With the `:p2d`, `:p3d`, and `:opengl` renderer settings, the
     bitmapped version will be used. While this can drastically improve
     speed and appearance, results are poor when exporting of the sketch
     does not include the `.otf` or `.ttf` file, and the requested font is
     not available on the machine running the sketch."
     ([name size] (.createFont (ap/current-applet) (str name) (float size)))
     ([name size smooth] (.createFont (ap/current-applet) (str name) (float size) smooth))
     ([name size smooth ^chars charset]
      (.createFont (ap/current-applet) (str name) (float size) smooth charset))))

(defn
  ^{:requires-bindings true
    :processing-name "createGraphics()"
    :category "Image"
    :subcategory "Rendering"
    :added "1.0"}
  create-graphics
  "Creates and returns a new `PGraphics` object of the types `:p2d`, `:p3d`,
  `:java2d`, `:pdf`. By default `:java2d` is used. Use this class if you
  need to draw into an off-screen graphics buffer. It's not possible
  to use [[create-graphics]] with the `:opengl` renderer, because it doesn't
  allow offscreen use. The `:pdf` renderer requires the filename parameter.

  Note: don't use [[create-graphics]] in draw in clojurescript, it leaks memory.
  You should create graphic in setup and reuse it in draw instead of creating
  a new one.

  It's important to call any drawing commands between `(.beginDraw graphics)` and
  `(.endDraw graphics)` statements or use [[with-graphics]] macro. This is also true
  for any commands that affect drawing, such as [[smooth]] or [[color-mode]].

  If you're using `:pdf` renderer - don't forget to call `(.dispose graphics)`
  as last command inside [[with-graphics]] macro, otherwise graphics won't be
  saved.

  Unlike the main drawing surface which is completely opaque, surfaces
  created with [[create-graphics]] can have transparency. This makes it
  possible to draw into a graphics and maintain the alpha channel. By
  using save to write a `PNG` or `TGA` file, the transparency of the
  graphics object will be honored."
  ([w h]
   (.createGraphics (ap/current-applet) (int w) (int h)))
  ([w h renderer]
   (.createGraphics (ap/current-applet) (int w) (int h) (ap/resolve-renderer renderer)))
  #?(:clj
     ([w h renderer path]
      (.createGraphics (ap/current-applet) (int w) (int h)
                       (ap/resolve-renderer renderer)
                       (u/absolute-path path)))))

(defn
  ^{:requires-bindings true
    :processing-name "createImage()"
    :category "Image"
    :subcategory nil
    :added "1.0"}
  create-image
  "Creates a new datatype for storing images (`PImage` for clj and
  `Image` for cljs). This provides a fresh buffer of pixels to play
  with. Set the size of the buffer with the `width` and `height`
  parameters.

  In clj the `format` parameter defines how the pixels are stored.
  See the PImage reference for more information.
  Possible formats: `:rgb`, `:argb`, `:alpha` (grayscale alpha channel)

  Prefer using [[create-image]] over initialising new `PImage` (or `Image`)
  instances directly."
  #?@(:clj
      ([w h format]
       (let [format (u/resolve-constant-key format image-formats)]
         (.createImage (ap/current-applet) (int w) (int h) (int format))))
      :cljs
      ([w h]
       (.createImage (ap/current-applet) (int w) (int h)))))

(defn
  ^{:requires-bindings true
    :processing-name "PGraphics.fillColor"
    :processing-link "http://processing.github.io/processing-javadocs/core/processing/core/PGraphics.html#fillColor"
    :category "Color"
    :subcategory "Creating & Reading"}
  current-fill
  "Return the current fill color."
  []
  (:current-fill @(internal-state)))

(defn
  ^{:requires-bindings true
    :processing-name "PGraphics.strokeColor"
    :processing-link "http://processing.github.io/processing-javadocs/core/processing/core/PGraphics.html#strokeColor"
    :category "Color"
    :subcategory "Creating & Reading"}
  current-stroke
  "Return the current stroke color."
  []
  (:current-stroke @(internal-state)))

(defn
  ^{:requires-bindings true
    :processing-name "cursor()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  cursor
  "Sets the cursor to a predefined symbol or makes it
  visible if already hidden (after [[no-cursor]] was called).

  Available modes: `:arrow`, `:cross`, `:hand`, `:move`, `:text`, `:wait`

  See [[cursor-image]] for specifying a generic image as the cursor
  symbol (clj only)."
  ([] (.cursor (ap/current-applet)))
  ([cursor-mode]
   (let [cursor-mode (u/resolve-constant-key cursor-mode cursor-modes)]
     (.cursor (ap/current-applet)
              #?(:clj (int cursor-mode)
                 :cljs (str cursor-mode))))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "cursor()"
       :category "Environment"
       :subcategory nil
       :added "1.0"}
     cursor-image
     "Set the cursor to a predefined image. The horizontal and vertical
     active spots of the cursor may be specified with `hx` and `hy`.
     It is recommended to make the size 16x16 or 32x32 pixels."
     ([^PImage img] (.cursor (ap/current-applet) img))
     ([^PImage img hx hy] (.cursor (ap/current-applet) img (int hx) (int hy)))))

(defn
  ^{:requires-bindings true
    :processing-name "curve()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve
  "Draws a curved line on the screen. The first and second parameters
  specify the beginning control point and the last two parameters
  specify the ending control point. The middle parameters specify the
  start and stop of the curve. Longer curves can be created by putting
  a series of curve functions together or using [[curve-vertex]]. An additional
  function called [[curve-tightness]] provides control for the visual quality
  of the curve. The [[curve]] function is an implementation of Catmull-Rom
  splines."
  ([x1 y1 x2 y2 x3 y3 x4 y4]
   (.curve (current-graphics)
           (float x1) (float y1)
           (float x2) (float y2)
           (float x3) (float y3)
           (float x4) (float y4)))
  ([x1 y1 z1 x2 y2 z2 x3 y3 z3 x4 y4 z4]
   (.curve (current-graphics)
           (float x1) (float y1) (float z1)
           (float x2) (float y2) (float z2)
           (float x3) (float y3) (float z3)
           (float x4) (float y4) (float z4))))

(defn
  ^{:requires-bindings true
    :processing-name "curveDetail()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-detail
  "Sets the resolution at which curves display. The default value is
  20. This function is only useful when using the `:p3d` or `:opengl`
  renderer as the default (`:java2d`) renderer does not use this
  information."
  [detail]
  (.curveDetail (current-graphics) (int detail)))

(defn
  ^{:requires-bindings true
    :processing-name "curvePoint()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-point
  "Evaluates the curve at point `t` for points `a`, `b`, `c`, `d`. The parameter
  `t` varies between 0 and 1, `a` and `d` are points on the curve, and `b` and `c`
  are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a curve at `t`."
  [a b c d t]
  (.curvePoint (current-graphics) (float a) (float b) (float c) (float d) (float t)))

(defn
  ^{:requires-bindings true
    :processing-name "curveTangent()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-tangent
  "Calculates the tangent of a point on a curve.
  See: http://en.wikipedia.org/wiki/Tangent"
  [a b c d t]
  (.curveTangent (current-graphics) (float a) (float b) (float c) (float d) (float t)))

(defn
  ^{:requires-bindings true
    :processing-name "curveTightness()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-tightness
  "Modifies the quality of forms created with curve and
  [[curve-vertex]]. The parameter `tightness` determines how the curve fits
  to the vertex points. The value 0.0 is the default value for
  `tightness` (this value defines the curves to be Catmull-Rom splines)
  and the value 1.0 connects all the points with straight
  lines. Values within the range -5.0 and 5.0 will deform the curves
  but will leave them recognizable and as values increase in
  magnitude, they will continue to deform."
  [tightness]
  (.curveTightness (current-graphics) (float tightness)))

(defn
  ^{:requires-bindings true
    :processing-name "curveVertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  curve-vertex
  "Specifies vertex coordinates for curves. This function may only be
  used between [[begin-shape]] and [[end-shape]] and only when there is no
  `mode` keyword specified to [[begin-shape]]. The first and last points in a
  series of [[curve-vertex]] lines will be used to guide the beginning and
  end of a the curve. A minimum of four points is required to draw a
  tiny curve between the second and third points. Adding a fifth point
  with [[curve-vertex]] will draw the curve between the second, third, and
  fourth points. The [[curve-vertex]] function is an implementation of
  Catmull-Rom splines."
  ([x y] (.curveVertex (current-graphics) (float x) (float y)))
  ([x y z] (.curveVertex (current-graphics) (float x) (float y) (float z))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "cylinder()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "3.0.0"}
     cylinder
     "Draw a cylinder with given `radius` and `height`."
     ([radius height]
      (.cylinder (current-graphics) (float radius) (float height)))

     ([radius height detail-x detail-y bottom-cap top-cap]
      (.cylinder (current-graphics) (float radius) (float height) (int detail-x) (int detail-y) (boolean bottom-cap) (boolean top-cap)))))

(defn
  ^{:requires-bindings false
    :processing-name "day()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  day
  "Get the current day of the month (1 through 31)."
  []
  #?(:clj (PApplet/day)
     :cljs (.day (ap/current-applet))))

(defn
  ^{:requires-bindings false
    :processing-name "degrees()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  degrees
  "Converts a radian measurement to its corresponding value in
  degrees. Radians and degrees are two ways of measuring the same
  thing. There are 360 degrees in a circle and `(* 2 Math/PI)` radians
  in a circle. For example, `(= 90° (/ Math/PI 2) 1.5707964)`. All
  trigonometric methods in Processing require their parameters to be
  specified in radians."
  [radians]
  #?(:clj (PApplet/degrees (float radians))
     :cljs (.degrees (ap/current-applet) radians)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "delay()"
       :processing-link nil
       :category "Structure"
       :subcategory nil
       :added "1.0"}
     delay-frame
     "Forces the program to stop running for a specified time. Delay
     times are specified in thousandths of a second, therefore the
     function call `(delay 3000)` will stop the program for three
     seconds. Because the screen is updated only at the end of `draw`,
     the program may appear to 'freeze', because the screen will not
     update when the [[delay-frame]] function is used. This function
     has no effect inside `setup`."
     [freeze-ms]
     (.delay (ap/current-applet) (int freeze-ms))))

(defn
  ^{:requires-bindings true
    :processing-name "directionalLight()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  directional-light
  "Adds a directional light. Directional light comes from one
  direction and is stronger when hitting a surface squarely and weaker
  if it hits at a gentle angle. After hitting a surface, a
  directional lights scatters in all directions. Lights need to be
  included in the `draw` function to remain persistent in a looping
  program. Placing them in the `setup` function of a looping program will cause
  them to only have an effect the first time through the loop. The
  affect of the `r`, `g`, and `b` parameters is determined by the current
  color mode. The `nx`, `ny`, and `nz` parameters specify the direction the
  light is facing. For example, setting `ny` to -1 will cause the
  geometry to be lit from below (the light is facing directly upward)."
  [r g b nx ny nz]
  (.directionalLight (current-graphics) (float r) (float g) (float b)
                     (float nx) (float ny) (float nz)))

(defn
  ^{:requires-bindings false
    :processing-name "dist()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  dist
  "Calculates the distance between two points."
  ([x1 y1 x2 y2]
   #?(:clj (PApplet/dist (float x1) (float y1) (float x2) (float y2))
      :cljs (.dist (ap/current-applet) x1 y1 x2 y2)))
  ([x1 y1 z1 x2 y2 z2]
   #?(:clj (PApplet/dist (float x1) (float y1) (float z1) (float x2) (float y2) (float z2))
      :cljs (.dist (ap/current-applet) x1 y1 z1 x2 y2 z2))))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Output"
    :subcategory "Files"
    :added "2.5"}
  do-record
  "Macro for drawing on graphics which saves result in the file at the end.
  Similar to [[with-graphics]] macro. [[do-record]] assumed to be used with `:pdf`
  graphics.

  Example:
  ```
  (q/do-record (q/create-graphics 200 200 :pdf \"output.pdf\")
    (q/fill 250 0 0)
    (q/ellipse 100 100 150 150))
  ```"
  [graphics & body]
  `(let [gr# ~graphics]
     (with-graphics gr#
       ~@body)
     (.dispose gr#)))

(defn
  ^{:requires-bindings true
    :processing-name "ellipse()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  ellipse
  "Draws an ellipse (oval) in the display window. An ellipse with an
  equal `width` and `height` is a circle. The origin may be changed with
  the [[ellipse-mode]] function."
  [x y width height]
  (.ellipse (current-graphics) (float x) (float y) (float width) (float height)))

(defn
  ^{:requires-bindings true
    :processing-name "ellipseMode()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  ellipse-mode
  "Modifies the origin of the ellipse according to the specified `mode`:

   * `:center`  - specifies the location of the ellipse as
                  the center of the shape **(default)**.
   * `:radius`  - similar to center, but the width and height parameters to
                  ellipse specify the radius of the ellipse, rather than the
                  diameter.
   * `:corner`  - draws the shape from the upper-left corner of its bounding
                  box.
   * `:corners` - uses the four parameters to ellipse to set two opposing
                  corners of the ellipse's bounding box."
  [mode]
  (let [mode (u/resolve-constant-key mode ellipse-modes)]
    (.ellipseMode (current-graphics) mode)))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "ellipsoid()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "3.0.0"}
     ellipsoid
     "Draws an ellipsoid with given radius

      Optional parameters:
        * `detail-x` - number of segments, the more segments the smoother geometry default is 24
        * `detail-y` - number of segments, the more segments the smoother geometry default is 16"
     ([radius-x radius-y radius-z]
      (.ellipsoid (current-graphics) radius-x radius-y radius-z))
     ([radius-x radius-y radius-z detail-x]
      (.ellipsoid (current-graphics) (float radius-x) (float radius-y) (float radius-z) (int detail-x)))
     ([radius-x radius-y radius-z detail-x detail-y]
      (.ellipsoid (current-graphics) (float radius-x) (float radius-y) (float radius-z) (int detail-x) (detail-y)))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "emissive()"
       :category "Lights, Camera"
       :subcategory "Material Properties"
       :added "1.0"}
     emissive
     "Sets the emissive color of the material used for drawing shapes
  drawn to the screen. Used in combination with [[ambient]], [[specular]], and
  [[shininess]] in setting the material properties of shapes.

  If passed one arg it is assumed to be an `int` (i.e. a color),
  multiple args are converted to `floats`."
     ([gray] (.emissive (current-graphics) gray))
     ([r g b] (.emissive (current-graphics) (float r) (float g) (float b)))))

(defn
  ^{:requires-bindings true
    :processing-name "endContour()"
    :category "Shape"
    :subcategory "Vertex"
    :added "2.0"}
  end-contour
  "Use the [[begin-contour]] and [[end-contour]] function to create negative
  shapes within shapes. These functions can only be within a
  [[begin-shape]]/[[end-shape]] pair and they only work with the `:p2d` and `:p3d`
  renderers."
  []
  (.endContour (current-graphics)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "endRaw()"
       :category "Output"
       :subcategory "Files"
       :added "1.0"}
     end-raw
     "Complement to [[begin-raw]]; they must always be used together. See
     the [[begin-raw]] docstring for details."
     []
     (.endRaw (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "endShape()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  end-shape
  "May only be called after [[begin-shape]]. When [[end-shape]] is called,
  all of image data defined since the previous call to [[begin-shape]] is
  written into the image buffer. The keyword `:close` may be passed to
  close the shape (to connect the beginning and the end)."
  ([] (.endShape (current-graphics)))
  ([mode]
   (when-not (= :close mode)
     #?(:clj (throw (Exception. (str "Unknown mode value: " mode ". Expected :close")))
        :cljs nil))
   (.endShape (current-graphics)
              #?(:clj PApplet/CLOSE
                 :cljs (aget js/p5.prototype "CLOSE")))))

(defn
  ^{:requires-bindings true
    :processing-name "exit()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  exit
  "Quits/stops/exits the program. Rather than terminating
  immediately, [[exit]] will cause the sketch to exit after `draw` has
  completed (or after `setup` completes if called during the `setup`
  method). "
  []
  #?(:clj (.exit (ap/current-applet))
     :cljs (.remove (ap/current-applet))))

(defn
  ^{:requires-bindings false
    :processing-name "exp()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  exp
  "Returns Euler's number `e` (2.71828...) raised to the power of the
  `val` parameter."
  [val]
  #?(:clj (PApplet/exp (float val))
     :cljs (.exp (ap/current-applet) val)))

(defn- save-current-fill
  "Save current fill color vector in the internal state. It can be accessed using [[current-fill]] function."
  [color]
  (swap! (internal-state) assoc :current-fill color))

(declare no-fill)

(defn
  ^{:requires-bindings true
    :processing-name "fill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  fill
  "Sets the color used to fill shapes. For example, if you run `(fill 204 102 0)`,
  all subsequent shapes will be filled with orange.  This function casts all
  input as a `float`. If nil is passed it removes any fill color; equivalent to
  calling [[no-fill]]."
  ([gray]
    ; provided color might be passed from (current-fill) in which case it's a vector.
    ; In that case just call fill again applying vector to the (fill).
   (cond (vector? gray) (apply fill gray)
         (nil? gray) (no-fill)
         true (do
                (.fill (current-graphics) gray)
                (save-current-fill [gray]))))

  ([gray alpha]
   (.fill (current-graphics) (float gray) (float alpha))
   (save-current-fill [gray alpha]))

  ([r g b]
   (.fill (current-graphics) (float r) (float g) (float b))
   (save-current-fill [r g b]))

  ([r g b alpha]
   (.fill (current-graphics) (float r) (float g) (float b) (float alpha))
   (save-current-fill [r g b alpha])))

(defn
  ^{:requires-bindings true
    :processing-name "displayDensity()"
    :category "Environment"
    :subcategory nil
    :added "2.4.0"}
  display-density
  "This function returns the number 2 if the screen is a high-density
  screen (called a Retina display on OS X or high-dpi on Windows and
  Linux) and a 1 if not. This information is useful for a program to
  adapt to run at double the pixel density on a screen that supports
  it. Can be used in conjunction with [[pixel-density]]."
  ([] (.displayDensity (ap/current-applet)))
  ([display] (.displayDensity (ap/current-applet) display)))

(defn
  ^{:requires-bindings true
    :processing-name "filter()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  display-filter
  "Originally named filter in Processing Language.
  Filters the display window with the specified mode and level.
  Level defines the quality of the filter and mode may be one of the
  following keywords:

  * `:threshold` - converts the image to black and white pixels depending
                   if they are above or below the threshold defined by
                   the level parameter. The level must be between
                   0.0 (black) and 1.0 (white). If no level is specified,
                   0.5 is used.
  * `:gray`      - converts any colors in the image to grayscale
                   equivalents. Doesn't work with level.
  * `:invert`    - sets each pixel to its inverse value. Doesn't work with
                   level.
  * `:posterize` - limits each channel of the image to the number of
                   colors specified as the level parameter. The parameter can
                   be set to values between 2 and 255, but results are most
                   noticeable in the lower ranges.
  * `:blur`      - executes a Gaussian blur with the level parameter
                   specifying the extent of the blurring. If no level
                   parameter is used, the blur is equivalent to Gaussian
                   blur of radius 1.
  * `:opaque`    - sets the alpha channel to entirely opaque. Doesn't work
                   with level.
  * `:erode`     - reduces the light areas. Doesn't work with level.
  * `:dilate`    - increases the light areas. Doesn't work with level."
  ([mode]
   (.filter (current-graphics)
            (u/resolve-constant-key mode filter-modes)))

  ([mode level]
   (let [mode (u/resolve-constant-key mode filter-modes)]
     (.filter (current-graphics) mode (float level)))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "filter()"
       :category "Image"
       :subcategory "Pixels"
       :added "2.0"}
     filter-shader
     "Originally named filter in Processing Language.
  Filters the display window with given shader (only in `:p2d` and `:p3d` modes)."
     [^PShader shader-obj] (.filter (current-graphics) shader-obj)))

(defn
  ^{:requires-bindings false
    :processing-name "floor()"
    :category "Math"
    :subcategory "Calculation"
    :added "2.0"}
  floor
  "Calculates the closest `int` value that is less than or equal to the
  value of the parameter. For example, `(floor 9.03)` returns the value 9."
  [n]
  #?(:clj (PApplet/floor (float n))
     :cljs (.floor (ap/current-applet) n)))

(defn
  ^{:requires-bindings true
    :processing-name "focused"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  focused
  "Returns `true` if the applet has focus, `false` otherwise."
  []  (.-focused (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "frameCount"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  frame-count
  "The system variable frameCount contains the number of frames
  displayed since the program started. Inside setup() the value is 0
  and after the first iteration of draw it is 1, etc."
  []
  #?(:clj (.frameCount (ap/current-applet))
     :cljs (.-frameCount (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "frameRate"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  current-frame-rate
  "Returns the current framerate"
  []
  (.frameRate (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name nil
    :category "Environment"
    :subcategory nil
    :added "2.7.2"}
  looping?
  "Returns whether the sketch is looping."
  []
  (:looping? @(internal-state)))

(defn
  ^{:requires-bindings true
    :processing-name "frameRate()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  frame-rate
  "Specifies a new target framerate (number of frames to be displayed every
  second). If the processor is not fast enough to maintain the
  specified rate, it will not be achieved. For example, the function
  call `(frame-rate 30)` will attempt to refresh 30 times a second. It
  is recommended to set the frame rate within setup. The default rate
  is 60 frames per second."
  [new-rate]
  (do
    (swap! (internal-state) assoc :frame-rate new-rate)
    (.frameRate (ap/current-applet) (float new-rate))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "frustum()"
       :category "Lights, Camera"
       :subcategory "Camera"
       :added "1.0"}
     frustum
     "Sets a perspective matrix defined through the parameters. Works
  like glFrustum, except it wipes out the current perspective matrix
  rather than multiplying itself with it.
  https://en.wikipedia.org/wiki/Frustum"
     [left right bottom top near far]
     (.frustum (current-graphics) (float left) (float right) (float bottom) (float top)
               (float near) (float far))))

(defn
  ^{:requires-bindings true
    :processing-name "get()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  get-pixel
  "Reads the color of any pixel or grabs a section of an image. If no
  parameters are specified, a copy of entire image is returned. Get the
  value of one pixel by specifying an `x`,`y` coordinate. Get a section of
  the image by specifying an additional `width` and `height` parameter.
  If the pixel requested is outside of the image window, black is returned.
  The numbers returned are scaled according to the current color ranges,
  but only RGB values are returned by this function. For example, even though
  you may have drawn a shape with `(color-mode :hsb)`, the numbers returned
  will be in RGB.

  Getting the color of a single pixel with `(get x y)` is easy, but not
  as fast as grabbing the data directly using the [[pixels]] function.

  If no `img` specified - [[current-graphics]] is used."
  ([] (get-pixel (current-graphics)))
  ([^PImage img] (.get img))
  ([x y] (get-pixel (current-graphics) x y))
  ([^PImage img x y] (.get img (int x) (int y)))
  ([x y w h] (get-pixel (current-graphics) x y w h))
  ([^PImage img x y w h] (.get img (int x) (int y) (int w) (int h))))

(defn
  ^{:requires-bindings true
    :processing-name "green()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  green
  "Extracts the green value from a color, scaled to match current
  [[color-mode]]. This value is always returned as a `float` so be careful
  not to assign it to an `int` value."
  [col]
  (.green (current-graphics) (u/clj-unchecked-int col)))

(defn
  ^{:require-binding false
    :processing-name "hex()"
    :category "Data"
    :subcategory "Conversion"}
  hex
  "Converts a byte, char, int, or color to a String containing the
  equivalent hexadecimal notation. For example color(0, 102, 153) will
  convert to the String \"FF006699\". This function can help make your
  geeky debugging sessions much happier."
  ([val]
   #?(:clj (PApplet/hex (int val))
      :cljs (.hex (ap/current-applet) val)))
  ([val num-digits]
   #?(:clj (PApplet/hex (int val) (int num-digits))
      :cljs (.hex (ap/current-applet) val num-digits))))

(defn
  ^{:requires-bindings true
    :processing-name "getHeight()"
    :processing-link nil
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  height
  "Height of the display window. The value of height is zero until
  size is called."
  []
  (.-height (ap/current-applet)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "hint()"
       :processing-link nil
       :category "Rendering"
       :subcategory nil
       :added "1.0"}
     hint
     "Set various hints and hacks for the renderer. This is used to
  handle obscure rendering features that cannot be implemented in a
  consistent manner across renderers. Many options will often graduate
  to standard features instead of hints over time.

  Options:

  * `:enable-native-fonts` - Use the native version fonts when they are
    installed, rather than the bitmapped version from a .vlw
    file. This is useful with the default (or JAVA2D) renderer
    setting, as it will improve font rendering speed. This is not
    enabled by default, because it can be misleading while testing
    because the type will look great on your machine (because you have
    the font installed) but lousy on others' machines if the identical
    font is unavailable. This option can only be set per-sketch, and
    must be called before any use of text-font.

  * `:disable-native-fonts` - Disables native font support.

  * `:disable-depth-test` - Disable the zbuffer, allowing you to draw on
    top of everything at will. When depth testing is disabled, items
    will be drawn to the screen sequentially, like a painting. This
    hint is most often used to draw in 3D, then draw in 2D on top of
    it (for instance, to draw GUI controls in 2D on top of a 3D
    interface). Starting in release 0149, this will also clear the
    depth buffer. Restore the default with :enable-depth-test
    but note that with the depth buffer cleared, any 3D drawing that
    happens later in draw will ignore existing shapes on the screen.

  * `:enable-depth-test` - Enables the zbuffer.

  * `:enable-depth-sort` - Enable primitive z-sorting of triangles and
    lines in :p3d and :opengl rendering modes. This can slow
    performance considerably, and the algorithm is not yet perfect.

  * `:disable-depth-sort` - Disables hint :enable-depth-sort

  * `:disable-opengl-errors` - Speeds up the OPENGL renderer setting
     by not checking for errors while running.

  * `:enable-opengl-errors` - Turns on OpenGL error checking

  * `:enable-depth-mask`
  * `:disable-depth-mask`

  * `:enable-optimized-stroke`
  * `:disable-optimized-stroke`
  * `:enable-retina-pixels`
  * `:disable-retina-pixels`
  * `:enable-stroke-perspective`
  * `:disable-stroke-perspective`
  * `:enable-stroke-pure`
  * `:disable-stroke-pure`
  * `:enable-texture-mipmaps`
  * `:disable-texture-mipmaps`
"
     [hint-type]
     (let [hint-type (if (keyword? hint-type)
                       (get hint-options hint-type)
                       hint-type)]
       (.hint (current-graphics) (int hint-type)))))

(defn
  ^{:requires-bindings false
    :processing-name "hour()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  hour
  "Returns the current hour as a value from 0 - 23."
  []
  #?(:clj (PApplet/hour)
     :cljs (.hour (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "hue()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  hue
  "Extracts the hue value from a color."
  [col]
  (.hue (current-graphics) (u/clj-unchecked-int col)))

(defn
  ^{:requires-bindings true
    :processing-name "image()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  image
  "Displays images to the screen. Processing currently works with GIF,
  JPEG, and Targa images. The color of an image may be modified with
  the [[tint]] function and if a GIF has transparency, it will maintain
  its transparency. The `img` parameter specifies the image to display
  and the `x` and `y` parameters define the location of the image from its
  upper-left corner. The image is displayed at its original size
  unless the width and height parameters specify a different size. The
  [[image-mode]] function changes the way the parameters work. A call to
  `(image-mode :corners)` will change the `width` and `height` parameters to
   define the x and y values of the opposite corner of the image."
  (#?(:clj [^PImage img x y]
      :cljs [img x y])
   (.image (current-graphics) img (float x) (float y)))

  (#?(:clj [^PImage img x y c d]
      :cljs [img x y c d])
   (.image (current-graphics) img (float x) (float y) (float c) (float d))))

(defn
  ^{:requires-bindings true
    :processing-name "PImage.filter()"
    :category "Image"
    :subcategory "Pixels"
    :added "2.0"}
  image-filter
  "Originally named filter in Processing Language.
  Filters given image with the specified `mode` and `level`.
  `level` defines the quality of the filter and `mode` may be one of
  the following keywords:

  * `:threshold` - converts the image to black and white pixels depending
                   if they are above or below the threshold defined by
                   the level parameter. The level must be between
                   0.0 (black) and 1.0 (white). If no level is specified,
                   0.5 is used.
  * `:gray`      - converts any colors in the image to grayscale
                   equivalents. Doesn't work with level.
  * `:invert`    - sets each pixel to its inverse value. Doesn't work with
                   level.
  * `:posterize` - limits each channel of the image to the number of
                   colors specified as the level parameter. The parameter can
                   be set to values between 2 and 255, but results are most
                   noticeable in the lower ranges.
  * `:blur`      - executes a Gaussian blur with the `level` parameter
                   specifying the extent of the blurring. If no level
                   parameter is used, the blur is equivalent to Gaussian
                   blur of radius 1.
  * `:opaque`    - sets the alpha channel to entirely opaque. Doesn't work
                   with level.
  * `:erode`     - reduces the light areas. Doesn't work with `level`.
  * `:dilate`    - increases the light areas. Doesn't work with `level`."
  ([^PImage img mode]
   (let [mode (u/resolve-constant-key mode filter-modes)]
     (.filter img mode)))
  ([^PImage img mode level]
   (let [mode (u/resolve-constant-key mode filter-modes)]
     (.filter img mode (float level)))))

(defn
  ^{:requires-bindings true
    :processing-name "imageMode()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  image-mode
  "Modifies the location from which images draw. The default `mode` is `:corner`.
   Available modes are:

  * `:corner`  - specifies the location to be the upper left corner and
                 uses the fourth and fifth parameters of image to set the
                 image's width and height.
  * `:corners` - uses the second and third parameters of image to set the
                 location of one corner of the image and uses the fourth
                  and fifth parameters to set the opposite corner.
  * `:center`  - draw images centered at the given x and y position."
  [mode]
  (let [mode (u/resolve-constant-key mode image-modes)]
    (.imageMode (current-graphics) mode)))

(defn
  ^{:requires-bindings true
    :processing-name "keyCode"
    :category "Input"
    :subcategory "Keyboard"
    :added "1.0"}
  key-code
  "The variable keyCode is used to detect special keys such as the UP,
  DOWN, LEFT, RIGHT arrow keys and ALT, CONTROL, SHIFT. When checking
  for these keys, it's first necessary to check and see if the key is
  coded. This is done with the conditional `(= (key) CODED)`.

  The keys included in the ASCII specification (BACKSPACE, TAB, ENTER,
  RETURN, ESC, and DELETE) do not require checking to see if they key
  is coded, and you should simply use the key variable instead of
  [[key-code]]. If you're making cross-platform projects, note that the
  ENTER key is commonly used on PCs and Unix and the RETURN key is
  used instead on Macintosh. Check for both ENTER and RETURN to make
  sure your program will work for all platforms.

  For users familiar with Java, the values for UP and DOWN are simply
  shorter versions of Java's `KeyEvent.VK_UP` and
  `KeyEvent.VK_DOWN`. Other keyCode values can be found in the Java
  KeyEvent reference."
  []
  (.-keyCode (ap/current-applet)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name nil
       :category "Input"
       :subcategory "Keyboard"
       :added "2.4.0"}
     key-modifiers
     "Set of key modifiers that were pressed when event happened.
  Possible modifiers `:ctrl`, `:alt`, `:shift`, `:meta`. Not available in
  ClojureScript."
     []
     (let [modifiers
           (if-let [^processing.event.Event
                    event (-> (ap/current-applet) meta :key-event deref)]
             [(if (.isAltDown event) :alt nil)
              (if (.isShiftDown event) :shift nil)
              (if (.isControlDown event) :control nil)
              (if (.isMetaDown event) :meta nil)]
             [])]
       (set (remove nil? modifiers)))))

(defn
  ^{:requires-bindings true
    :processing-name "keyPressed"
    :category "Input"
    :subcategory "Keyboard"
    :added "1.0"}
  key-pressed?
  "true if any key is currently pressed, false otherwise."
  []
  #?(:clj (.-keyPressed (ap/current-applet))
     :cljs (.-keyIsPressed (ap/current-applet))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "lightness()"
       :category "Color"
       :subcategory "Creating & Reading"
       :added "3.0.0"}
     lightness
     "Extracts the HSL lightness value from a color or pixel array."
     [c]
     (.lightness (current-graphics) c)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "lightFalloff()"
       :category "Lights, Camera"
       :subcategory "Lights"
       :added "1.0"}
     light-falloff
     "Sets the falloff rates for point lights, spot lights, and ambient
  lights. The parameters are used to determine the falloff with the
  following equation:

  d = distance from light position to vertex position
  falloff = 1 / (CONSTANT + d * LINEAR + (d*d) * QUADRATIC)

  Like fill, it affects only the elements which are created after it
  in the code. The default value is `(light-falloff 1.0 0.0 0.0)`.
  Thinking about an ambient light with a falloff can be tricky. It is
  used, for example, if you wanted a region of your scene to be lit
  ambiently by one color and another region to be lit ambiently by
  another color, you would use an ambient light with location and
  falloff. You can think of it as a point light that doesn't care
  which direction a surface it is facing."
     [constant linear quadratic]
     (.lightFalloff (current-graphics) (float constant) (float linear) (float quadratic))))

(defn
  ^{:requires-bindings true
    :processing-name "lerpColor()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  lerp-color
  "Calculates a color or colors between two color at a specific
  increment. The `amt` parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc."
  [c1 c2 amt]
  (.lerpColor (current-graphics)
              (u/clj-unchecked-int c1)
              (u/clj-unchecked-int c2)
              (float amt)))

(defn
  ^{:requires-bindings false
    :processing-name "lerp()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  lerp
  "Calculates a number between two numbers at a specific
  increment. The `amt` parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc. The lerp function
  is convenient for creating motion along a straight path and for
  drawing dotted lines."
  [start stop amt]
  #?(:clj (PApplet/lerp (float start) (float stop) (float amt))
     :cljs (.lerp (ap/current-applet) start stop amt)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "lights()"
       :category "Lights, Camera"
       :subcategory "Lights"
       :added "1.0"}
     lights
     "Sets the default ambient light, directional light, falloff, and
  specular values. The defaults are:

  `(ambient-light 128 128 128)`
  `(directional-light 128 128 128 0 0 -1)`
  `(light-falloff 1 0 0)`
  `(light-specular 0 0 0)`.

  Lights need to be included in the draw to remain persistent in a
  looping program. Placing them in the setup of a looping program
  will cause them to only have an effect the first time through the
  loop."
     []
     (.lights (current-graphics))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "lightSpecular()"
       :category "Lights, Camera"
       :subcategory "Lights"
       :added "1.0"}
     light-specular
     "Sets the specular color for lights. Like [[fill]], it affects only the
  elements which are created after it in the code. Specular refers to
  light which bounces off a surface in a preferred direction (rather
  than bouncing in all directions like a diffuse light) and is used
  for creating highlights. The specular quality of a light interacts
  with the specular material qualities set through the [[specular]] and
  [[shininess]] functions."
     [r g b]
     (.lightSpecular (current-graphics) (float r) (float g) (float b))))

(defn
  ^{:requires-bindings true
    :processing-name "line()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  line
  "Draws a line (a direct path between two points) to the screen. The
  version of line with four parameters draws the line in 2D. To color
  a line, use the [[stroke]] function. A line cannot be filled, therefore
  the fill method will not affect the color of a line. 2D lines are
  drawn with a width of one pixel by default, but this can be changed
  with the [[stroke-weight]] function. The version with six parameters
  allows the line to be placed anywhere within XYZ space."
  ([p1 p2] (apply line (concat p1 p2)))
  ([x1 y1 x2 y2] (.line (current-graphics) (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2]
   (.line (current-graphics) (float x1) (float y1) (float z1)
          (float x2) (float y2) (float z2))))

(defn
  ^{:requires-bindings true
    :processing-name "loadFont()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-font
  "Loads a font into a variable of type `PFont`. To load correctly,
  fonts must be located in the data directory of the current sketch.
  To create a font to use with Processing use the [[create-font]] function.

  Like [[load-image]] and other methods that load data, the [[load-font]]
  function should not be used inside draw, because it will slow down the sketch
  considerably, as the font will be re-loaded from the disk (or
  network) on each frame.

  For most renderers, Processing displays fonts using the `.vlw` font
  format, which uses images for each letter, rather than defining them
  through vector data. When hint `:enable-native-fonts` is used with the
  JAVA2D renderer, the native version of a font will be used if it is
  installed on the user's machine.

  Using [[create-font]] (instead of [[load-font]]) enables vector data to be
  used with the JAVA2D (default) renderer setting. This can be helpful
  when many font sizes are needed, or when using any renderer based on
  JAVA2D, such as the PDF library."
  [filename]
  (.loadFont (ap/current-applet)  (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "loadImage()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-image
  "Loads an image into a variable of type `PImage`. Four types of
  images (`.gif`, `.jpg`, `.tga`, `.png`) may be loaded. To load
  correctly, images must be located in the data directory of the
  current sketch. In most cases, load all images in `setup` to preload
  them at the start of the program. Loading images inside `draw` will
  reduce the speed of a program.

  The filename parameter can also be a URL to a file found online.

  Image is loaded asynchronously. In order to check whether image
  finished loading use [[loaded?]]."
  [filename]
  #?(:clj (.requestImage (ap/current-applet) (str filename))
     :cljs (.loadImage (ap/current-applet) (str filename))))

(defn
  ^{:requires-bindings true
    :processing-name "loadShader()"
    :category "Rendering"
    :subcategory "Shaders"
    :added "2.0"}
  load-shader
  "Loads a shader into the `PShader` object for clj and `Shader` object for
  cljs. In clj mode shaders are
  compatible with the P2D and P3D renderers, but not with the default
  renderer. In cljs mode shaders are compatible with the P3D renderer."
  ([fragment-filename]
   (.loadShader (current-graphics) fragment-filename))
  ([fragment-filename vertex-filename]
   #?(:clj (.loadShader (current-graphics) fragment-filename vertex-filename)
      :cljs (.loadShader (current-graphics) vertex-filename fragment-filename))))

(defn
  ^{:requires-bindings true
    :processing-name "loadShape()"
    :category "Shape"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-shape
  "Load a geometry from a file as a `PShape` in clj, and a `Geometry`
  in cljs."
  [filename]
  #?(:clj (.loadShape (ap/current-applet) filename)
     :cljs (.loadModel (ap/current-applet) filename)))

(defn
  ^{:requires-bindings false
    :processing-name nil
    :category "Environment"
    :subcategory nil
    :added "3.0.0"}
  loaded?
  "Returns true if object is loaded."
  [object]
  (condp = (type object)
    #?@(:clj  (PImage (pos? (.-width object))
               ; shader is loaded synchronously
                      PShader true)
        :cljs (js/p5.Shader (and (aget object "_vertSrc") (aget object "_fragSrc"))
                            js/p5.Image (pos? (.-width object))))))

(defn
  ^{:requires-bindings false
    :processing-name "log()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  log
  "Calculates the natural logarithm (the base-e logarithm) of a
  number. This function expects the values greater than 0.0."
  [val]
  #?(:clj (PApplet/log (float val))
     :cljs (.log (ap/current-applet) val)))

(defn
  ^{:requires-bindings false
    :processing-name "mag()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  mag
  "Calculates the magnitude (or length) of a vector. A vector is a
  direction in space commonly used in computer graphics and linear
  algebra. Because it has no start position, the magnitude of a vector
  can be thought of as the distance from coordinate `(0,0)` to its `(x,y)`
  value. Therefore, [[mag]] is a shortcut for writing `(dist 0 0 x y)`."
  ([a b]
   #?(:clj (PApplet/mag (float a) (float b))
      :cljs (.mag (ap/current-applet) a b)))
  #?(:clj ([a b c]
           (PApplet/mag (float a) (float b) (float c)))))

(defn
  ^{:requires-bindings false
    :processing-name "map()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  map-range
  "Re-maps a number from one range to another.

  Numbers outside the range are not clamped to 0 and 1, because
  out-of-range values are often intentional and useful."
  [val low1 high1 low2 high2]
  #?(:clj (PApplet/map (float val) (float low1) (float high1) (float low2) (float high2))
     :cljs (.map (ap/current-applet) val low1 high1 low2 high2)))

(defn
  ^{:requires-bindings false
    :processing-name "PImage.mask()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  mask-image
  "Masks part of an image from displaying by loading another image and
  using it as an alpha channel. This mask image should only contain
  grayscale data. The mask image needs to be the same size as the image
  to which it is applied.

  If single argument function is used - masked image is sketch itself
  or graphics if used inside [[with-graphics]] macro. If you're passing
  graphics to this function - it works only with `:p3d` and `:opengl` renderers.

  This method is useful for creating dynamically generated alpha
  masks."
  ([^PImage mask] (mask-image (current-graphics) mask))
  ([^PImage img ^PImage mask]
   #?(:clj
        ; in clj we can't use image as mask as it uses blue component, not alpha.
        ; instead we convert extract alphas into array and use array version of mask()
        ; see https://processing.org/reference/PImage_mask_.html
      (let [pxls (pixels mask)
            alphas (amap ^ints pxls idx ret (int (alpha (aget ^ints pxls idx))))]
        (.mask img alphas))
      :cljs (.mask img mask))))

(defn
  ^{:requires-bindings true
    :processing-name "millis()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  millis
  "Returns the number of milliseconds (thousandths of a second) since
  starting the sketch. This information is often used for timing
  animation sequences."
  []
  (.millis (ap/current-applet)))

(defn
  ^{:requires-bindings false
    :processing-name "minute()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  minute
  "Returns the current minute as a value from 0 - 59"
  []
  #?(:clj (PApplet/minute)
     :cljs (.minute (ap/current-applet))))

(defn
  ^{:requires-bindings false
    :processing-name "month()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  month
  "Returns the current month as a value from 1 - 12."
  []
  #?(:clj (PApplet/month)
     :cljs (.month (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "mouseButton"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-button
  "The value of the system variable mouseButton is either `:left`, `:right`,
  or `:center` depending on which button is pressed. `nil` if no button pressed"
  []
  (let [button-code   (.-mouseButton (ap/current-applet))]
    #?(:clj
       (condp = button-code
         PConstants/LEFT :left
         PConstants/RIGHT :right
         PConstants/CENTER :center
         nil)

       :cljs
       (condp = button-code
         (aget js/p5.prototype "LEFT") :left
         (aget js/p5.prototype "RIGHT") :right
         (aget js/p5.prototype "CENTER") :center
         nil))))

(defn
  ^{:requires-bindings true
    :processing-name "mousePressed"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-pressed?
  "true if a mouse button is pressed, false otherwise."
  []
  #?(:clj (.-mousePressed (ap/current-applet))
     :cljs (.-mouseIsPressed (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "mouseX"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-x
  "Current horizontal coordinate of the mouse."
  []
  (.-mouseX (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "mouseY"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-y
  "Current vertical coordinate of the mouse."
  []
  (.-mouseY (ap/current-applet)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "noClip()"
       :category "Rendering"
       :subcategory nil
       :added "2.4.0"}
     no-clip
     "Disables the clipping previously started by the [[clip]] function."
     []
     (.noClip (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "noCursor()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  no-cursor
  "Hides the cursor from view. Will not work when running in full
  screen (Present) mode."
  []
  (.noCursor (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "noFill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  no-fill
  "Disables filling geometry. If both [[no-stroke]] and [[no-fill]] are called,
  nothing will be drawn to the screen."
  []
  (.noFill (current-graphics))
  (swap! (internal-state) assoc :current-fill nil))

(defn
  ^{:requires-bindings true
    :processing-name "noise()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  noise
  "Returns the Perlin noise value at specified coordinates. Perlin
  noise is a random sequence generator producing a more natural
  ordered, harmonic succession of numbers compared to the standard
  random function. It was invented by Ken Perlin in the 1980s and
  been used since in graphical applications to produce procedural
  textures, natural motion, shapes, terrains etc.

  The main difference to the random function is that Perlin noise is
  defined in an infinite n-dimensional space where each pair of
  coordinates corresponds to a fixed semi-random value (fixed only for
  the lifespan of the program). The resulting value will always be
  between 0.0 and 1.0. Processing can compute 1D, 2D and 3D noise,
  depending on the number of coordinates given. The noise value can be
  animated by moving through the noise space and the 2nd and 3rd
  dimensions can also be interpreted as time.

  The actual noise is structured similar to an audio signal, in
  respect to the function's use of frequencies. Similar to the concept
  of harmonics in physics, perlin noise is computed over several
  octaves which are added together for the final result.

  Another way to adjust the character of the resulting sequence is the
  scale of the input coordinates. As the function works within an
  infinite space the value of the coordinates doesn't matter as such,
  only the distance between successive coordinates does (eg. when
  using noise within a loop). As a general rule the smaller the
  difference between coordinates, the smoother the resulting noise
  sequence will be. Steps of 0.005-0.03 work best for most
  applications, but this will differ depending on use."
  ([x] (.noise (ap/current-applet) (float x)))
  ([x y] (.noise (ap/current-applet) (float x) (float y)))
  ([x y z] (.noise (ap/current-applet) (float x) (float y) (float z))))

(defn
  ^{:requires-bindings true
    :processing-name "noiseDetail()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  noise-detail
  "Adjusts the character and level of detail produced by the Perlin
  noise function. Similar to harmonics in physics, noise is computed
  over several octaves. Lower octaves contribute more to the output
  signal and as such define the overall intensity of the noise, whereas
  higher octaves create finer grained details in the noise
  sequence. By default, noise is computed over 4 octaves with each
  octave contributing exactly half than its predecessor, starting at
  50% strength for the 1st octave. This falloff amount can be changed
  by adding an additional function parameter. Eg. a falloff factor of
  0.75 means each octave will now have 75% impact (25% less) of the
  previous lower octave. Any value between 0.0 and 1.0 is valid,
  however note that values greater than 0.5 might result in greater
  than 1.0 values returned by noise.

  By changing these parameters, the signal created by the noise
  function can be adapted to fit very specific needs and
  characteristics."
  ([octaves] (.noiseDetail (ap/current-applet) (int octaves)))
  ([octaves falloff] (.noiseDetail (ap/current-applet) (int octaves) (float falloff))))

(defn
  ^{:requires-bindings true
    :processing-name "noiseSeed()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  noise-seed
  "Sets the seed value for noise. By default, noise produces different
  results each time the program is run. Set the `val` parameter to a
  constant to return the same pseudo-random numbers each time the
  software is run."
  [val]
  (.noiseSeed (ap/current-applet) (long val)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "noLights()"
       :category "Lights, Camera"
       :subcategory "Lights"
       :added "1.0"}
     no-lights
     "Disable all lighting. Lighting is turned off by default and enabled
  with the [[lights]] function. This function can be used to disable lighting so
  that 2D geometry (which does not require lighting) can be drawn
  after a set of lighted 3D geometry."
     []
     (.noLights (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "noLoop()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  no-loop
  "Stops Processing from continuously executing the code within
  `draw`. If [[start-loop]] is called, the code in `draw` will begin to run
  continuously again. If using [[no-loop]] in setup, it should be the last
  line inside the block.

  When [[no-loop]] is used, it's not possible to manipulate or access the
  screen inside event handling functions such as [[mouse-pressed]] or
  [[key-pressed]]. Instead, use those functions to call [[redraw]] or
  loop which will run `draw`, which can update the screen
  properly. This means that when [[no-loop]] has been called, no drawing
  can happen, and functions like [[save-frame]] may not be used.

  Note that if the sketch is resized, [[redraw]] will be called to
  update the sketch, even after [[no-loop]] has been
  specified. Otherwise, the sketch would enter an odd state until
  loop was called."
  []
  (.noLoop (ap/current-applet))
  (swap! (internal-state) assoc :looping? false))

(defn
  ^{:requires-bindings true
    :processing-name "norm()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  norm
  "Normalize a value to exist between 0 and 1 (inclusive)."
  [val start stop]
  #?(:clj (PApplet/norm (float val) (float start) (float stop))
     :cljs (.norm (ap/current-applet) val start stop)))

(defn
  ^{:requires-bindings true
    :processing-name "noSmooth()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  no-smooth
  "Draws all geometry with jagged (aliased) edges. Must be called inside
  `:settings` handler."
  [] (.noSmooth (current-graphics)))

(defn
  ^{:requires-bindings true
    :processing-name "noStroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  no-stroke
  "Disables drawing the stroke (outline). If both [[no-stroke]] and
  [[no-fill]] are called, nothing will be drawn to the screen."
  []
  (.noStroke (current-graphics))
  (swap! (internal-state) assoc :current-stroke nil))

(defn
  ^{:requires-bindings true
    :processing-name "noTint()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  no-tint
  "Removes the current fill value for displaying images and reverts to
  displaying images with their original hues."
  []
  (.noTint (current-graphics)))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "orbitControl()"
       :category "Lights, Camera"
       :subcategory "Camera"
       :added "3.0.0"}
     orbit-control
     "Allows the camera to orbit around a target using mouse."
     []
     (.orbitControl (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "ortho()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  ortho
  "Sets an orthographic projection and defines a parallel clipping
  volume. All objects with the same dimension appear the same size,
  regardless of whether they are near or far from the camera. The
  parameters to this function specify the clipping volume where `left`
  and `right` are the minimum and maximum x values, `top` and `bottom` are
  the minimum and maximum y values, and `near` and `far` are the minimum
  and maximum z values. If no parameters are given, the default is
  used: `(ortho 0 width 0 height -10 10)`"
  ([] (.ortho (current-graphics)))
  ([left right bottom top]
   (.ortho (current-graphics) (float left) (float right) (float bottom) (float top)))
  ([left right bottom top near far]
   (.ortho (current-graphics) (float left) (float right) (float bottom) (float top) (float near) (float far))))

(defn
  ^{:requires-bindings true
    :processing-name "perspective()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  perspective
  "Sets a perspective projection applying foreshortening, making
  distant objects appear smaller than closer ones. The parameters
  define a viewing volume with the shape of truncated pyramid. Objects
  near to the front of the volume appear their actual size, while
  farther objects appear smaller. This projection simulates the
  perspective of the world more accurately than orthographic
  projection. The version of perspective without parameters sets the
  default perspective and the version with four parameters allows the
  programmer to set the area precisely. The default values are:
  `perspective(PI/3.0, width/height, cameraZ/10.0, cameraZ*10.0)` where
  `cameraZ` is `((height/2.0) / tan(PI*60.0/360.0))`"
  ([] (.perspective (current-graphics)))
  ([fovy aspect z-near z-far]
   (.perspective (current-graphics) (float fovy) (float aspect)
                 (float z-near) (float z-far))))

(defn
  ^{:requires-bindings true
    :processing-name "pixelDensity()"
    :category "Environment"
    :subcategory nil
    :added "2.4.0"}
  pixel-density
  "It makes it possible for Processing to render using all of the pixels
  on high resolutions screens like Apple Retina displays and Windows
  High-DPI displays. Possible values 1 or 2. Must be called only from
  :settings handler. To get density of the current screen you can use
  the [[display-density]] function."
  [density]
  (.pixelDensity (ap/current-applet) density))

(defn
  ^{:requires-bindings true
    :processing-name "pixels[]"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  pixels
  "Array containing the values for all the pixels in the display
  window or image. This array is therefore the size of the display window. If
  this array is modified, the [[update-pixels]] function must be called to
  update the changes. Calls `.loadPixels` before obtaining the pixel array."
  ([] (pixels (current-graphics)))

  ([^PImage img]
   (.loadPixels img)
   (.-pixels img)))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "plane()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "3.0.0"}
     plane
     "Draw a plane with given `width` and `height`."
     [width height]
     (.plane (current-graphics) (float width) (float height))))

(defn
  ^{:requires-bindings true
    :processing-name "pmouseX"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  pmouse-x
  "Horizontal coordinate of the mouse in the previous frame"
  []
  (.-pmouseX (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "pmouseY"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  pmouse-y
  "Vertical coordinate of the mouse in the previous frame"
  []
  (.-pmouseY (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "point()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  point
  "Draws a point, a coordinate in space at the dimension of one
  pixel.

  Parameters:
  * `x` - the horizontal value for the point
  * `y` - the vertical value for the point
  * `z` - the depth value (optional)

  Drawing this shape in 3D using the `z` parameter requires the `:p3d`
  or `:opengl` renderer to be used."
  ([x y] (.point (current-graphics) (float x) (float y)))
  ([x y z] (.point (current-graphics) (float x) (float y) (float z))))

(defn
  ^{:requires-bindings true
    :processing-name "pointLight()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  point-light
  "Adds a point light. Lights need to be included in the draw() to
  remain persistent in a looping program. Placing them in the setup()
  of a looping program will cause them to only have an effect the
  first time through the loop. The affect of the `r`, `g`, and `b`
  parameters is determined by the current [[color-mode]]. The `x`, `y`, and `z`
  parameters set the position of the light"
  [r g b x y z]
  (.pointLight (current-graphics) (float r) (float g) (float b) (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "popMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  pop-matrix
  "Pops the current transformation matrix off the matrix
  stack. Understanding pushing and popping requires understanding the
  concept of a matrix stack. The [[push-matrix]] function saves the current
  coordinate system to the stack and [[pop-matrix]] restores the prior
  coordinate system. [[push-matrix]] and [[pop-matrix]] are used in conjunction
  with the other transformation methods and may be embedded to control
  the scope of the transformations."
  []
  #?(:clj (.popMatrix (current-graphics))
     :cljs (.pop (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "popStyle()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  pop-style
  "Restores the prior settings on the 'style stack'. Used in
  conjunction with [[push-style]]. Together they allow you to change the
  style settings and later return to what you had. When a new style is
  started with [[push-style]], it builds on the current style information.
  The [[push-style]] and [[pop-style]] functions can be nested to provide more
  control"
  []
  #?(:clj (.popStyle (current-graphics))
     :cljs (.pop (current-graphics))))

(defn
  ^{:requires-bindings false
    :processing-name "pow()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  pow
  "Facilitates exponential expressions. The [[pow]] function is an
  efficient way of multiplying numbers by themselves (or their
  reciprocal) in large quantities. For example, `(pow 3 5)` is
  equivalent to the expression `(* 3 3 3 3 3)` and `(pow 3 -5)` is
  equivalent to `(/ 1 (* 3 3 3 3 3))`."
  [num exponent]
  #?(:clj (PApplet/pow (float num) (float exponent))
     :cljs (.pow (ap/current-applet) num exponent)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "printCamera()"
       :category "Lights, Camera"
       :subcategory "Camera"
       :added "1.0"}
     print-camera
     "Prints the current camera matrix to std out. Useful for debugging."
     []
     (.printCamera (current-graphics))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "printMatrix()"
       :category "Transform"
       :subcategory nil
       :added "1.0"}
     print-matrix
     "Prints the current matrix to std out. Useful for debugging."
     []
     (.printMatrix (current-graphics))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "printProjection()"
       :category "Lights, Camera"
       :subcategory "Camera"
       :added "1.0"}
     print-projection
     "Prints the current projection matrix to std out. Useful for
     debugging"
     []
     (.printProjection (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "pushMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  push-matrix
  "Pushes the current transformation matrix onto the matrix
  stack. Understanding [[[push-matrix]] and [[pop-matrix]] requires
  understanding the concept of a matrix stack. The [[push-matrix]]
  function saves the current coordinate system to the stack and
  [[pop-matrix]] restores the prior coordinate system. [[push-matrix]] and
  [[pop-matrix]] are used in conjunction with the other transformation
  methods and may be embedded to control the scope of the
  transformations."
  []
  #?(:clj (.pushMatrix (current-graphics))
     :cljs (.push (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "pushStyle()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  push-style
  "Saves the current style settings onto a 'style stack'. Use with
  [[pop-style]] which restores the prior settings. Note that these
  functions are always used together. They allow you to change the
  style settings and later return to what you had. When a new style is
  started with [[push-style]], it builds on the current style
  information. The [[push-style]] and [[pop-style]] functions can be
  embedded to provide more control.

  The style information controlled by the following functions are
  included in the style: [[fill]], [[stroke]], [[tint]], [[stroke-weight]],
  [[stroke-cap]], [[stroke-join]], [[image-mode]], [[rect-mode]], [[ellipse-mode]],
  [[shape-mode]], [[color-mode]], [[text-align]], [[text-font]], [[text-mode]], [[text-size]],
  [[text-leading]], [[emissive]], [[specular]], [[shininess]], and [[ambient]]."
  []
  #?(:clj (.pushStyle (current-graphics))
     :cljs (.push (current-graphics))))

(defn
  ^{:requires-bindings true
    :processing-name "quad()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  quad
  "A quad is a quadrilateral, a four sided polygon. It is similar to a
  rectangle, but the angles between its edges are not constrained to
  be ninety degrees. The first pair of parameters `(x1,y1)` sets the
  first vertex and the subsequent pairs should proceed clockwise or
  counter-clockwise around the defined shape."
  [x1 y1 x2 y2 x3 y3 x4 y4]
  (.quad (current-graphics)
         (float x1) (float y1)
         (float x2) (float y2)
         (float x3) (float y3)
         (float x4) (float y4)))

(defn
  ^{:requires-bindings true
    :processing-name "quadraticVertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "2.0"}
  quadratic-vertex
  "Specifies vertex coordinates for quadratic Bezier curves. Each call to
  [[quadratic-vertex]] defines the position of one control points and one
  anchor point of a Bezier curve, adding a new segment to a line or shape.
  The first time [[quadratic-vertex]] is used within a [[begin-shape]] call, it
  must be prefaced with a call to [[vertex]] to set the first anchor point.
  This function must be used between [[begin-shape]] and [[end-shape]] and only
  when there is no MODE parameter specified to begin-shape. Using the 3D
  version requires rendering with `:p3d`."
  ([cx cy x3 y3]
   (.quadraticVertex (current-graphics) (float cx) (float cy) (float x3) (float y3)))
  ([cx cy cz x3 y3 z3]
   (.quadraticVertex (current-graphics) (float cx) (float cy) (float cz) (float x3) (float y3) (float z3))))

(defn
  ^{:requires-bindings false
    :processing-name "radians()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  radians
  "Converts a degree measurement to its corresponding value in
  radians. Radians and degrees are two ways of measuring the same
  thing. There are 360 degrees in a circle and 2*PI radians in a
  circle. For example, 90° = PI/2 = 1.5707964. All trigonometric
  methods require their parameters to be specified in radians."
  [degrees]
  #?(:clj (PApplet/radians (float degrees))
     :cljs (.radians (ap/current-applet) degrees)))

(defn
  ^{:requires-bindings true
    :processing-name "random()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  random
  "Generates random numbers. Each time the random function is called,
  it returns an unexpected value within the specified range. If one
  parameter is passed to the function it will return a `float` between
  zero and the value of the high parameter. The function call `(random
  5)` returns values between 0 and 5 (starting at zero, up to but not
  including 5). If two parameters are passed, it will return a `float`
  with a value between the parameters. The function call
  `(random -5 10.2)` returns values starting at -5 up to (but not
  including) 10.2."
  ([max] (.random (ap/current-applet) (float max)))
  ([min max] (.random (ap/current-applet) (float min) (float max))))

(defn
  ^{:requires-bindings true
    :processing-name nil
    :category "Math"
    :subcategory "Random"
    :added "2.6.1"}
  random-2d
  "Returns a new 2D unit vector with a random direction"
  []
  (let [theta (random TWO-PI)]
    [(Math/cos theta) (Math/sin theta)]))

(defn
  ^{:requires-bindings true
    :processing-name nil
    :category "Math"
    :subcategory "Random"
    :added "2.6.1"}
  random-3d
  "Returns a new 3D unit vector with a random direction"
  []
  ; Algorithm: http://mathworld.wolfram.com/SpherePointPicking.html
  (let [theta (random TWO-PI)
        u (random -1 1)
        xy (Math/sqrt (- 1 (* u u)))
        vx (* xy (Math/cos theta))
        vy (* xy (Math/sin theta))
        vz u]
    [vx vy vz]))

(defn
  ^{:requires-bindings true
    :processing-name "randomGaussian()"
    :category "Math"
    :subcategory "Random"
    :added "2.0"}
  random-gaussian
  "Returns a `float` from a random series of numbers having a mean of 0 and
  standard deviation of 1. Each time the [[random-gaussian]] function is called,
  it returns a number fitting a Gaussian, or normal, distribution.
  There is theoretically no minimum or maximum value that [[random-gaussian]]
  might return. Rather, there is just a very low probability that values far
  from the mean will be returned; and a higher probability that numbers near
  the mean will be returned."
  []
  (.randomGaussian (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "randomSeed()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  random-seed
  "Sets the seed value for random. By default, random produces
  different results each time the program is run. Set the value
  parameter to a constant to return the same pseudo-random numbers
  each time the software is run."
  [w]
  (.randomSeed (ap/current-applet) (float w)))

(defn
  ^{:requires-bindings true
    :processing-name "key"
    :category "Input"
    :subcategory "Keyboard"
    :added "1.0"}
  raw-key
  "Contains the value of the most recent key on the keyboard that was
  used (either pressed or released).

  For non-ASCII keys, use the keyCode variable. The keys included in
  the ASCII specification (BACKSPACE, TAB, ENTER, RETURN, ESC, and
  DELETE) do not require checking to see if they key is coded, and you
  should simply use the key variable instead of keyCode If you're
  making cross-platform projects, note that the ENTER key is commonly
  used on PCs and Unix and the RETURN key is used instead on
  Macintosh. Check for both ENTER and RETURN to make sure your program
  will work for all platforms."
  []
  (.-key (ap/current-applet)))

(defn
  ^{:requires-bindings true
    :processing-name "rect()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  rect
  "Draws a rectangle to the screen. A rectangle is a four-sided shape
  with every angle at ninety degrees. By default, the first two
  parameters set the location of the upper-left corner, the third
  sets the width, and the fourth sets the height. These parameters
  may be changed with [[rect-mode]].

  To draw a rounded rectangle, add a fifth parameter, which is used as
  the radius value for all four corners. To use a different radius value
  for each corner, include eight parameters."
  ([x y width height]
   (.rect (current-graphics) (float x) (float y) (float width) (float height)))
  ([x y width height r]
   (.rect (current-graphics) (float x) (float y) (float width) (float height) (float r)))
  ([x y width height top-left-r top-right-r bottom-right-r bottom-left-r]
   (.rect (current-graphics) (float x) (float y) (float width) (float height)
          (float top-left-r) (float top-right-r) (float bottom-right-r) (float bottom-left-r))))

(defn
  ^{:requires-bindings true
    :processing-name "rectMode()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  rect-mode
  "Modifies the location from which rectangles draw. The default `mode`
  is `:corner`. Available modes are:

  * `:corner`  - Specifies the location to be the upper left corner of the
                 shape and uses the third and fourth parameters of [[rect]] to
                 specify the width and height.
  * `:corners` - Uses the first and second parameters of [[rect]] to set the
                 location of one corner and uses the third and fourth
                 parameters to set the opposite corner.
  * `:center`  - Draws the image from its center point and uses the third
                 and fourth parameters of [[rect]] to specify the image's width
                 and height.
  * `:radius`  - Draws the image from its center point and uses the third
                 and forth parameters of [[rect]] to specify half of the
                 image's width and height."

  [mode]
  (let [mode (u/resolve-constant-key mode rect-modes)]
    (.rectMode (current-graphics) mode)))

(defn
  ^{:requires-bindings true
    :processing-name "red()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  red
  "Extracts the red value from a color, scaled to match the current
  [[color-mode]]."
  [c]
  (.red (current-graphics) (u/clj-unchecked-int c)))

(defn
  ^{:requires-bindings true
    :processing-name "redraw()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  redraw
  "Executes the code within the `draw` function one time (or n times in cljs).
  This function allows the program to update the display window only
  when necessary, for example when an event registered by [[mouse-pressed]] or
  [[key-pressed]] occurs.

  In structuring a program, it only makes sense to call [[redraw]]
  within events such as [[mouse-pressed]]. This is because [[redraw]] does
  not run draw immediately (it only sets a flag that indicates an
  update is needed).

  Calling [[redraw]] within `draw` has no effect because `draw` is
  continuously called anyway."
  ([]
   (.redraw (ap/current-applet)))
  #?(:cljs ([n]
            (.redraw (ap/current-applet) n))))

(defn
  ^{:requires-bindings true
    :processing-name "resetMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  reset-matrix
  "Replaces the current matrix with the identity matrix. The
  equivalent function in OpenGL is `glLoadIdentity()`"
  []
  (.resetMatrix (current-graphics)))

#?(:clj
   (def ^{:private true}
     shader-modes {:points PApplet/POINTS
                   :lines PApplet/LINES
                   :triangles PApplet/TRIANGLES}))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "resetShader()"
       :category "Rendering"
       :subcategory "Shaders"
       :added "2.0"}
     reset-shader
     "Restores the default shaders. Code that runs after [[reset-shader]] will
  not be affected by previously defined shaders. Optional `kind` parameter -
  type of shader, either `:points`, `:lines`, or `:triangles`"
     ([] (.resetShader (current-graphics)))
     ([kind]
      (let [mode (u/resolve-constant-key kind shader-modes)]
        (.resetShader (current-graphics) mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "resize()"
    :category "Image"
    :processing-link "https://processing.org/reference/PImage_resize_.html"
    :added "2.1.0"}
  resize
  "Resize the image to a new width and height.
  To make the image scale proportionally, use 0 as the value for the wide or
  high parameter. For instance, to make the width of an image 150 pixels,
  and change the height using the same proportion, use `(resize 150 0)`.

  Even though a `PGraphics` is technically a `PImage`, it is not possible
  to rescale the image data found in a `PGraphics`.
  (It's simply not possible to do this consistently across renderers:
  technically infeasible with P3D, or what would it even do with PDF?)
  If you want to resize `PGraphics` content, first get a copy of its image data
  using the get() method, and call resize() on the `PImage` that is returned."
  [^PImage img w h]
  (.resize img w h))

(defn
  ^{:require-bindings true
    :category "Environment"
    :added "2.7.0"}
  resize-sketch
  "Resizes sketch.
  Note about ClojureScript version: if the `div` element is resized externally
  (for example from js on a page then you still need to call this
  method in order to tell Quil that size has changed. Currently there is no
  good way to automatically detect that size of the `<div>` element changed."
  [width height]
  #?(:cljs
     (ap/set-size (ap/current-applet) width height)
     :clj
     (.. (ap/current-applet) getSurface (setSize width height))))

(defn
  ^{:requires-bindings true
    :processing-name "rotate()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate
  "Rotates a shape the amount specified by the `angle` parameter. Angles
  should be specified in radians (values from 0 to TWO-PI) or
  converted to radians with the [[radians]] function.

  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a clockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling `(rotate HALF-PI)` and then `(rotate HALF-PI)` is the
  same as `(rotate PI)`. All transformations are reset when draw begins
  again.

  Technically, rotate multiplies the current transformation matrix by
  a rotation matrix. This function can be further controlled by the
  [[push-matrix]] and [[pop-matrix]] functions.

  When 4 arguments are provided it produces a rotation of `angle` degrees
  around the vector `vx` `vy` `vz`. Check examples to better understand.
  This rotation follows the right-hand rule, so if the vector x y z points
  toward the user, the rotation will be counterclockwise."
  ([angle] (.rotate (current-graphics) (float angle)))
  ([angle vx vy vz] (.rotate (current-graphics)
                             (float angle)
                             #?@(:clj ((float vx) (float vy) (float vz))
                                 :cljs ((array (float vx) (float vy) (float vz)))))))

(defn
  ^{:requires-bindings true
    :processing-name "rotateX()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-x
  "Rotates a shape around the x-axis the amount specified by the `angle`
  parameter. Angles should be specified in radians (values from 0 to
  (* PI 2)) or converted to radians with the [[radians]] function. Objects
  are always rotated around their relative position to the origin and
  positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling `(rotate-x HALF-PI)` and then `(rotate-x HALF-PI)` is
  the same as `(rotate-x PI)`. If [[rotate-x]] is called within the draw
  function, the transformation is reset when the loop begins again. This
  function requires either the `:p3d` or `:opengl` renderer."
  [angle]
  (.rotateX (current-graphics) (float angle)))

(defn
  ^{:requires-bindings true
    :processing-name "rotateY()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-y
  "Rotates a shape around the y-axis the amount specified by the `angle`
  parameter. Angles should be specified in radians (values from 0
  to (* PI 2)) or converted to radians with the [[radians]] function.
  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling `(rotate-y HALF-PI)` and then `(rotate-y HALF-PI)` is
  the same as `(rotate-y PI)`. If [[rotate-y]] is called within the draw
  function, the transformation is reset when the loop begins again. This
  function requires either the `:p3d` or `:opengl` renderer."
  [angle]
  (.rotateY (current-graphics) (float angle)))

(defn
  ^{:requires-bindings true
    :processing-name "rotateZ()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-z
  "Rotates a shape around the z-axis the amount specified by the `angle`
  parameter. Angles should be specified in radians (values from 0
  to (* PI 2)) or converted to radians with the [[radians]] function.
  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling `(rotate-z HALF-PI)` and then `(rotate-z HALF-PI)` is
  the same as `(rotate-z PI)`. If [[rotate-y]] is called within the draw
  function, the transformation is reset when the loop begins again. This
  function requires either the `:p3d` or `:opengl` renderer."
  [angle]
  (.rotateZ (current-graphics) (float angle)))

(defn
  ^{:requires-bindings false
    :processing-name "round()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  round
  "Calculates the integer closest to the value parameter. For example,
  `(round 9.2)` returns the value 9."
  [val]
  #?(:clj (PApplet/round (float val))
     :cljs (.round (ap/current-applet) val)))

(defn
  ^{:requires-bindings true
    :processing-name "saturation()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  saturation
  "Extracts the saturation value from a color."
  [c]
  (.saturation (current-graphics) (u/clj-unchecked-int c)))

(defn
  ^{:requires-bindings true
    :processing-name "save()"
    :category "Output"
    :subcategory "Image"
    :added "1.0"}
  save
  "Saves an image from the display window. Images are saved in TIFF,
  TARGA, JPEG, and PNG format depending on the extension within the
  filename parameter. For example, image.tif will have a TIFF image
  and image.png will save a PNG image. If no extension is included in
  the filename, the image will save in TIFF format and .tif will be
  added to the name. All images saved from the main drawing window
  will be opaque. To save images without a background, use
  [[create-graphics]]."
  [filename]
  (.save (current-graphics) (str filename)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "saveFrame()"
       :category "Output"
       :subcategory "Image"
       :added "1.0"}
     save-frame
     "Saves an image identical to the current display window as a
  file. May be called multiple times - each file saved will have a
  unique name. Name and image format may be modified by passing a
  string parameter of the form \"foo-####.ext\" where foo- can be any
  arbitrary string, #### will be replaced with the current frame id
  and .ext is one of .tiff, .targa, .png, .jpeg or .jpg

  Examples:
     ```
     (save-frame \"pretty-pic-####.jpg\")
     ```"
     ([name] (.saveFrame (ap/current-applet) (str name)))))

(defn
  ^{:requires-bindings true
    :processing-name "scale()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  scale
  "Increases or decreases the size of a shape by expanding and
  contracting vertices. Objects always scale from their relative
  origin to the coordinate system. Scale values are specified as
  decimal percentages. For example, the function call `(scale 2)`
  increases the dimension of a shape by 200%. Transformations apply to
  everything that happens after and subsequent calls to the function
  multiply the effect. For example, calling `(scale 2)` and then
  `(scale 1.5)` is the same as `(scale 3)`. If scale is called within
  draw, the transformation is reset when the loop begins again. Using
  this function with the `sz` parameter requires specifying `:p3d` or `:opengl`
  as the renderer. This function can be further controlled by
  [[push-matrix]] and [[pop-matrix]]."
  ([s] (.scale (current-graphics) (float s)))
  ([sx sy] (.scale (current-graphics) (float sx) (float sy)))
  ([sx sy sz] (.scale (current-graphics) (float sx) (float sy) (float sz))))

#?(:clj
   (defn- ^java.awt.Dimension current-screen
     []
     (let [default-toolkit (java.awt.Toolkit/getDefaultToolkit)]
       (.getScreenSize default-toolkit))))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name nil
       :category "Environment"
       :subcategory nil
       :added "1.0"}
     screen-width
     "Returns the width of the main screen in pixels."
     []
     (.width (current-screen))))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name nil
       :category "Environment"
       :subcategory nil
       :added "1.0"}
     screen-height
     "Returns the height of the main screen in pixels."
     []
     (.height (current-screen))))

(defn
  ^{:requires-bindings false
    :processing-name "second()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  seconds
  "Returns the current second as a value from 0 - 59."
  []
  #?(:clj (PApplet/second)
     :cljs (.second (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "set()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  set-pixel
  "Changes the color of any pixel in the display window. The `x` and `y`
  parameters specify the pixel to change and the `c` parameter
  specifies the color value. The color parameter is affected by the
  current [[color-mode]] (the default is RGB values from 0 to 255).

  Setting the color of a single pixel with `(set-pixel x y)` is easy, but not
  as fast as putting the data directly into [[pixels]].

  This function ignores [[image-mode]].

  Due to what appears to be a bug in Apple's Java implementation, the
  [[point]] and [[set-pixel]] methods are extremely slow in some circumstances
  when used with the default renderer. Using `:p2d` or `:p3d` will fix the
  problem. Grouping many calls to [[point]] or [[set-pixel]] together can also
  help. (Bug 1094)"
  ([x y c] (set-pixel (current-graphics) x y c))
  ([^PImage img x y c]
   (.set img (int x) (int y) c)))

(defn
  ^{:requires-bindings true
    :processing-name "set()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  set-image
  "Writes an image directly into the display window. The `x` and `y`
  parameters define the coordinates for the upper-left corner of the
  image."
  [x y ^PImage src]
  (.set (current-graphics) (int x) (int y) src))

(defn
  ^{:requires-bindings true
    :processing-name "shader()"
    :category "Rendering"
    :subcategory "Shaders"
    :added "2.0"}
  shader
  "Applies the shader specified by the parameters. It's compatible with the `:p2d`
  and `:p3d` renderers, but not with the default `:java2d` renderer.
  In clj mode you can pass an optional `kind` parameter that specifies
  the type of shader, either `:points`, `:lines`, or `:triangles`"
  ([shader] (.shader (current-graphics) shader))
  #?(:clj
     ([shader kind]
      (let [mode (u/resolve-constant-key kind shader-modes)]
        (.shader (current-graphics) shader mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "shape()"
    :category "Shape"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  shape
  "Displays shapes to the screen. The shapes must have been loaded
  with [[load-shape]]. Processing currently works with SVG shapes
  only. The `sh` parameter specifies the shape to display and the `x` and
  `y` parameters define the location of the shape from its upper-left
  corner. The shape is displayed at its original size unless the `width`
  and `height` parameters specify a different size. The [[shape-mode]]
  function changes the way the parameters work. A call to
  `(shape-mode :corners)` for example, will change the width and height
  parameters to define the x and y values of the opposite corner of
  the shape.

  Note complex shapes may draw awkwardly with the renderers `:p2d`, `:p3d`, and
  `:opengl`. Those renderers do not yet support shapes that have holes
  or complicated breaks."
  ([^PShape sh] #?(:clj (.shape (current-graphics) sh)
                   :cljs (.model (current-graphics) sh)))
  #?(:clj
     ([^PShape sh x y] (.shape (current-graphics) sh (float x) (float y))))
  #?(:clj
     ([^PShape sh x y width height] (.shape (current-graphics) sh (float x) (float y) (float width) (float height)))))

(defn
  ^{:requires-bindings true
    :processing-name "shearX()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  shear-x
  "Shears a shape around the x-axis the amount specified by the `angle`
  parameter. Angles should be specified in radians (values from 0 to
  PI*2) or converted to radians with the [[radians]] function. Objects
  are always sheared around their relative position to the origin and
  positive numbers shear objects in a clockwise direction.
  Transformations apply to everything that happens after and
  subsequent calls to the function accumulates the effect. For
  example, calling `(shear-x (/ PI 2))` and then `(shear-x (/ PI 2))` is
  the same as `(shear-x PI)`. If [[shear-x]] is called within the draw
  function, the transformation is reset when the loop begins again. This
  function works in P2D or JAVA2D mode.

  Technically, [[shear-x]] multiplies the current transformation matrix
  by a rotation matrix. This function can be further controlled by the
  [[push-matrix]] and [[pop-matrix]] functions."
  [angle]
  (.shearX (current-graphics) (float angle)))

(defn
  ^{:requires-bindings true
    :processing-name "shearY()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  shear-y
  "Shears a shape around the y-axis the amount specified by the `angle`
  parameter. Angles should be specified in radians (values from 0 to
  PI*2) or converted to radians with the [[radians]] function. Objects
  are always sheared around their relative position to the origin and
  positive numbers shear objects in a clockwise direction.
  Transformations apply to everything that happens after and
  subsequent calls to the function accumulates the effect. For
  example, calling `(shear-y (/ PI 2))` and then `(shear-y (/ PI 2))` is
  the same as `(shear-y PI)`. If [[shear-y]] is called within the draw
  function, the transformation is reset when the loop begins again. This
  function works in P2D or JAVA2D mode.

  Technically, [[shear-y]] multiplies the current transformation matrix
  by a rotation matrix. This function can be further controlled by the
  [[push-matrix]] and [[pop-matrix]] functions."
  [angle]
  (.shearY (current-graphics) (float angle)))

#?(:clj
   (defn ^{:requires-bindings true
           :processing-name "shapeMode()"
           :category "Shape"
           :subcategory "Loading & Displaying"
           :added "1.0"}
     shape-mode
     "Modifies the location from which shapes draw. Available modes are:

     * `:corner`  - specifies the location to be the upper left corner of the
                    shape and uses the third and fourth parameters of shape
                    to specify the width and height. **(default)**
     * `:corners` - uses the first and second parameters of shape to set
                    the location of one corner and uses the third and fourth
                    parameters to set the opposite corner.
     * `:center`  - draws the shape from its center point and uses the third
                    and forth parameters of shape to specify the width and
                    height."
     [mode]
     (let [mode (u/resolve-constant-key mode p-shape-modes)]
       (.shapeMode (current-graphics) (int mode)))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "shininess()"
       :category "Lights, Camera"
       :subcategory "Material Properties"
       :added "1.0"}
     shininess
     "Sets the amount of gloss in the surface of shapes. Used in
  combination with [[ambient]], [[specular]], and [[emissive]] in setting
  the material properties of shapes."
     [shine]
     (.shininess (current-graphics) (float shine))))

(defn
  ^{:requires-bindings false
    :processing-name "sin()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  sin
  "Calculates the sine of an angle. This function expects the values
  of the angle parameter to be provided in radians (values from 0 to
  6.28). A `float` within the range -1 to 1 is returned."
  [angle]
  #?(:clj (PApplet/sin (float angle))
     :cljs (.sin (ap/current-applet) angle)))

(defn
  ^{:requires-bindings true
    :processing-name "smooth()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  smooth
  "Draws all geometry with smooth (anti-aliased) edges. This will slow
  down the frame rate of the application, but will enhance the visual
  refinement.

  Must be called inside `:settings` handler.

  The `level` parameter (int) increases the level of smoothness with the
  `:p2d` and `:p3d` renderers. This is the level of over sampling applied to
  the graphics buffer. The value `2` will double the rendering size
  before scaling it down to the display size. This is called `2x
  anti-aliasing`. The value `4` is used for `4x anti-aliasing` and `8` is
  specified for `8x anti-aliasing`. If level is set to `0`, it will disable
  all smoothing; it's the equivalent of the function [[no-smooth]].
  The maximum anti-aliasing level is determined by the hardware of the
  machine that is running the software.

  Note that smooth will also improve image quality of resized images."
  ([] (.smooth #?(:clj (ap/current-applet)
                  :cljs (current-graphics))))
  ([level] (.smooth #?(:clj (ap/current-applet)
                       :cljs (current-graphics))
                    (int level))))

(defn
  ^{:requires-bindings true
    :processing-name "specular()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  specular
  "Sets the specular color of the materials used for shapes drawn to
  the screen, which sets the color of highlights. Specular refers to
  light which bounces off a surface in a preferred direction (rather
  than bouncing in all directions like a diffuse light). Used in
  combination with [[emissive]], [[ambient]], and [[shininess]] in setting
  the material properties of shapes."
  ([gray]
   #?(:clj (.specular (current-graphics) (float gray))
      :cljs (.specularMaterial (current-graphics) (float gray))))
  ([x y z]
   #?(:clj (.specular (current-graphics) (float x) (float y) (float z))
      :cljs (.specularMaterial (current-graphics) (float x) (float y) (float z)))))

(defn
  ^{:requires-bindings true
    :processing-name "sphere()"
    :category "Shape"
    :subcategory "3D Primitives"
    :added "1.0"}
  sphere
  "Generates a hollow ball made from tessellated triangles."
  [radius] (.sphere (current-graphics) (float radius)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "sphereDetail()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "1.0"}
     sphere-detail
     "Controls the detail used to render a sphere by adjusting the number
  of vertices of the sphere mesh. The default resolution is 30, which
  creates a fairly detailed sphere definition with vertices every
  360/30 = 12 degrees. If you're going to render a great number of
  spheres per frame, it is advised to reduce the level of detail using
  this function. The setting stays active until [[sphere-detail]] is
  called again with a new parameter and so should not be called prior
  to every [[sphere]] statement, unless you wish to render spheres with
  different settings, e.g. using less detail for smaller spheres or
  ones further away from the camera. To control the detail of the
  horizontal and vertical resolution independently, use the version of
  the functions with two parameters."
     ([res] (.sphereDetail (current-graphics) (int res)))
     ([ures vres] (.sphereDetail (current-graphics) (int ures) (int vres)))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "spotLight()"
       :category "Lights, Camera"
       :subcategory "Lights"
       :added "1.0"}
     spot-light
     "Adds a spot light. Lights need to be included in the draw to
  remain persistent in a looping program. Placing them in the setup
  of a looping program will cause them to only have an effect the
  first time through the loop. The affect of the `r`, `g`, and `b`
  parameters is determined by the current [[color-mode]]. The `x`, `y`, and `z`
  parameters specify the position of the light and `nx`, `ny`, `nz` specify
  the direction or light. The angle parameter affects the angle of the
  spotlight cone."
     ([r g b x y z nx ny nz angle concentration]
      (.spotLight (current-graphics) r g b x y z nx ny nz angle concentration))
     ([[r g b] [x y z] [nx ny nz] angle concentration]
      (.spotLight (current-graphics) r g b x y z nx ny nz angle concentration))))

(defn
  ^{:requires-bindings false
    :processing-name "sq()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  sq
  "Squares a number (multiplies a number by itself). The result is
  always a positive number, as multiplying two negative numbers always
  yields a positive result. For example, -1 * -1 = 1."
  [a]
  #?(:clj (PApplet/sq (float a))
     :cljs (.sq (ap/current-applet) a)))

(defn
  ^{:requires-bindings false
    :processing-name "sqrt()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  sqrt
  "Calculates the square root of a number. The square root of a number
  is always positive, even though there may be a valid negative
  root. The square root s of number a is such that (= a (* s s)). It
  is the opposite of squaring."
  [a]
  #?(:clj (PApplet/sqrt (float a))
     :cljs (.sqrt (ap/current-applet) a)))

(defn
  ^{:requires-bindings true
    :processing-name "loop()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  start-loop
  "Causes Processing to continuously execute the code within
  draw. If [[no-loop]] is called, the code in draw stops executing."
  []
  (.loop (ap/current-applet))
  (swap! (internal-state) assoc :looping? true))

(defn- save-current-stroke
  "Save current stroke color vector in the internal state. It can be accessed
  using the [[current-stroke]] function."
  [color]
  (swap! (internal-state) assoc :current-stroke color))

(defn
  ^{:requires-bindings true
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke
  "Sets the color used to draw lines and borders around shapes. This
  color is either specified in terms of the RGB or HSB color depending
  on the current [[color-mode]] (the default color space is RGB, with
  each value in the range from 0 to 255).
  If nil is passed it removes any fill color; equivalent to [[no-stroke]]."
  ([gray]
    ; provided color might be passed from (current-fill) in which case it's a vector.
    ; In that case just call fill again applying vector to the (fill).
   (cond (vector? gray) (apply stroke gray)
         (nil? gray) (no-stroke)
         true (do
                (.stroke (current-graphics)  gray)
                (save-current-stroke [gray]))))
  ([gray alpha]
   (.stroke (current-graphics) (float gray) (float alpha))
   (save-current-stroke [gray alpha]))
  ([x y z]
   (.stroke (current-graphics) (float x) (float y) (float z))
   (save-current-stroke [x y z]))
  ([x y z alpha]
   (.stroke (current-graphics) (float x) (float y) (float z) (float alpha))
   (save-current-stroke [z y z alpha])))

(defn
  ^{:requires-bindings true
    :processing-name "strokeCap()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  stroke-cap
  "Sets the style for rendering line endings. These ends are either
  squared, extended, or rounded and specified with the corresponding
  parameters `:square`, `:project`, and `:round`. The default cap is `:round`."
  [cap-mode]
  (let [cap-mode (u/resolve-constant-key cap-mode stroke-cap-modes)]
    (.strokeCap (current-graphics)
                #?(:clj (int cap-mode)
                   :cljs (str cap-mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "strokeJoin()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  stroke-join
  "Sets the style of the joints which connect line
  segments. These joints are either mitered, beveled, or rounded and
  specified with the corresponding parameters `:miter`, `:bevel`, and
  `:round`. The default joint is `:miter`.

  This function is not available with the `:p2d`, `:p3d`, or `:opengl`
  renderers."
  [join-mode]
  (let [join-mode (u/resolve-constant-key join-mode stroke-join-modes)]
    (.strokeJoin (current-graphics)
                 #?(:clj (int join-mode)
                    :cljs (str join-mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "strokeWeight()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  stroke-weight
  "Sets the width of the stroke used for lines, points, and the border
  around shapes. All widths are set in units of pixels. "
  [weight]
  (.strokeWeight (current-graphics) (float weight)))

(defn
  ^{:requires-bindings false
    :processing-name "tan()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  tan
  "Calculates the ratio of the sine and cosine of an angle. This
  function expects the values of the angle parameter to be provided in
  radians (values from 0 to PI*2). Values are returned in the range
  infinity to -infinity."
  [angle]
  #?(:clj (PApplet/tan (float angle))
     :cljs (.tan (ap/current-applet) angle)))

(defn
  ^{:requires-bindings true
    :category "Environment"
    :subcategory nil
    :added "1.5.0"}
  target-frame-rate
  "Returns the target framerate specified with the function [[frame-rate]]"
  []
  (:frame-rate @(internal-state)))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-char
  "Draws a char to the screen in the specified position. See the
  [[text]] function for more details."
  ([c x y]
   (when (current-fill)
     (.text (current-graphics) (char c) (float x) (float y))))
  ([c x y z]
   (when (current-fill)
     (.text (current-graphics) (char c) (float x) (float y) (float z)))))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-num
  "Draws a number to the screen in the specified position. See the
  [[text]] function for more details."
  ([num x y]
   (when (current-fill)
     (.text (current-graphics) (float num) (float x) (float y))))
  ([num x y z]
   (when (current-fill)
     (.text (current-graphics) (float num) (float x) (float y) (float z)))))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text
  "Draws text to the screen in the position specified by the `x` and `y`
  parameters (and the optional `z` parameter in clj). A default font will be used
  unless a font is set with the [[text-font]] function. Change the color of the
  text with the [[fill]] function. The text displays in relation to the
  [[text-align]] function, which gives the option to draw to the left, right, and
  center of the coordinates.

  The `x1`, `y1`, `x2` and `y2` parameters define a
  rectangular area to display within and may only be used with string
  data. For text drawn inside a rectangle, the coordinates are
  interpreted based on the current [[rect-mode]] setting."
  ([^String s x y]
   (when (current-fill)
     (.text (current-graphics) s (float x) (float y))))
  #?(:clj
     ([^String s x y z]
      (when (current-fill)
        (.text (current-graphics) s (float x) (float y) (float z)))))
  ([^String s x1 y1 x2 y2]
   (when (current-fill)
     (.text (current-graphics) s (float x1) (float y1) (float x2) (float y2)))))

(defn
  ^{:requires-bindings true
    :processing-name "textAlign()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-align
  "Sets the current alignment for drawing text. Available modes are:

  horizontal - `:left`, `:center`, and `:right`
  vertical   - `:top`, `:bottom`, `:center`, and `:baseline`

  An optional second parameter specifies the vertical alignment
  mode. `:baseline` is the default. The `:top` and `:center` parameters are
  straightforward. The `:bottom` parameter offsets the line based on the
  current [[text-descent]]. For multiple lines, the final line will be
  aligned to the bottom, with the previous lines appearing above it.

  When using text with width and height parameters, `:baseline` is
  ignored, and treated as `:top`. (Otherwise, text would by default draw
  outside the box, since `:baseline` is the default setting. `:baseline` is
  not a useful drawing mode for text drawn in a rectangle.)

  The vertical alignment is based on the value of [[text-ascent]], which
  many fonts do not specify correctly. It may be necessary to use a
  hack and offset by a few pixels by hand so that the offset looks
  correct. To do this as less of a hack, use some percentage of
  [[text-ascent]] or [[text-descent]] so that the hack works even if you
  change the size of the font."
  ([align]
   (let [align (u/resolve-constant-key align horizontal-alignment-modes)]
     (.textAlign (current-graphics) align)))
  ([align-x align-y]
   (let [align-x (u/resolve-constant-key align-x horizontal-alignment-modes)
         align-y (u/resolve-constant-key align-y vertical-alignment-modes)]
     (.textAlign (current-graphics) align-x align-y))))

(defn
  ^{:requires-bindings true
    :processing-name "textAscent()"
    :category "Typography"
    :subcategory "Metrics"
    :added "1.0"}
  text-ascent
  "Returns the ascent of the current font at its current size. This
  information is useful for determining the height of the font above
  the baseline. For example, adding the [[text-ascent]] and [[text-descent]]
  values will give you the total height of the line."
  []
  (.textAscent (current-graphics)))

(defn
  ^{:requires-bindings true
    :processing-name "textDescent()"
    :category "Typography"
    :subcategory "Metrics"
    :added "1.0"}
  text-descent
  "Returns descent of the current font at its current size. This
  information is useful for determining the height of the font below
  the baseline. For example, adding the [[text-ascent]] and [[text-descent]]
  values will give you the total height of the line."
  []
  (.textDescent (current-graphics)))

(defn
  ^{:requires-bindings true
    :processing-name "textFont()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-font
  "Sets the current font that will be drawn with the text
  function. Fonts must be loaded with [[load-font]] before it can be
  used. This font will be used in all subsequent calls to the [[text]]
  function. If no `size` parameter is input, the font will appear at its
  original size until it is changed with [[text-size]].

  Because fonts are usually bitmaped, you should create fonts at the
  sizes that will be used most commonly. Using [[text-font]] without the
  `size` parameter will result in the cleanest-looking text.

  With the default (JAVA2D) and PDF renderers, it's also possible to
  enable the use of native fonts via the command
  `(hint :enable-native-fonts)`. This will produce vector text in JAVA2D
  sketches and PDF output in cases where the vector data is available:
  when the font is still installed, or the font is created via the
  [[create-font]] function."
  ([^PFont font] (.textFont (current-graphics) font))
  ([^PFont font size] (.textFont (current-graphics) font (int size))))

(defn
  ^{:requires-bindings true
    :processing-name "textLeading()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-leading
  "Sets the spacing between lines of text in units of pixels. This
  setting will be used in all subsequent calls to the [[text]] function."
  [leading]
  (.textLeading (current-graphics) (float leading)))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "textMode()"
       :category "Typography"
       :subcategory "Attributes"
       :added "1.0"}
     text-mode
     "Sets the way text draws to the screen - available modes
     are `:model` and `:shape`

     In the default configuration (the `:model` mode), it's possible to
     rotate, scale, and place letters in two and three dimensional space.

     The `:shape` mode draws text using the glyph outlines of individual
     characters rather than as textures. This mode is only supported with
     the PDF and OPENGL renderer settings. With the PDF renderer, you
     must specify the `:shape` [[text-mode]] before any other drawing occurs.
     If the outlines are not available, then `:shape` will be ignored and
     `:model` will be used instead.

     The `:shape` option in OPENGL mode can be combined with [[begin-raw]] to
     write vector-accurate text to 2D and 3D output files, for instance
     DXF or PDF. `:shape` is not currently optimized for OPENGL, so if
     recording shape data, use `:model` until you're ready to capture the
     geometry with [[begin-raw]]."
     [mode]
     (let [mode (u/resolve-constant-key mode text-modes)]
       (.textMode (current-graphics) (int mode)))))

(defn
  ^{:requires-bindings true
    :processing-name "textSize()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-size
  "Sets the current font size. This size will be used in all
  subsequent calls to the [[text]] function. Font size is measured in
  units of pixels."
  [size]
  (.textSize (current-graphics) (float size)))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "textStyle()"
       :category "Typography"
       :subcategory "Attributes"
       :added "2.8.0"}
     text-style
     "Sets/gets the style of the text for system fonts to `:normal`, `:italic`,
     or `:bold`. Note: this may be is overridden by CSS styling. For
     non-system fonts (opentype, truetype, etc.) please load styled fonts
     instead."
     [style]
     (let [s (u/resolve-constant-key style text-styles)]
       (.textStyle (current-graphics) s))))

(defn
  ^{:requires-bindings true
    :processing-name "texture()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  texture
  "Sets a texture to be applied to vertex points. The [[texture]] function must
  be called between [[begin-shape]] and [[end-shape]] and before any calls to
  [[vertex]].

  When textures are in use, the fill color is ignored. Instead, use
  [[tint]] to specify the color of the texture as it is applied to the
  shape."
  #?(:clj [^PImage img]
     :cljs [img])
  (.texture (current-graphics) img))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "textureMode()"
       :category "Shape"
       :subcategory "Vertex"
       :added "1.0"}
     texture-mode
     "Sets the coordinate space for texture mapping. There are two
  options, `:image` and `:normal`.

  `:image` refers to the actual coordinates of the image and `:normal`
  refers to a normalized space of values ranging from 0 to 1. The
  default `mode` is `:image`. In `:image`, if an image is 100 x 200 pixels,
  mapping the image onto the entire size of a quad would require the
  points `(0,0) (0,100) (100,200) (0,200)`. The same mapping in
  NORMAL_SPACE is `(0,0) (0,1) (1,1) (0,1)`."
     [mode]
     (let [mode (u/resolve-constant-key mode texture-modes)]
       (.textureMode (current-graphics) (int mode)))))

#?(:clj
   (defn
     ^{:requires-bindings true
       :processing-name "textureWrap()"
       :category "Shape"
       :subcategory "Vertex"
       :added "2.0"}
     texture-wrap
     "Defines if textures repeat or draw once within a texture map. The two
  parameters are `:clamp` (the default behavior) and `:repeat`. This function
  only works with the `:p2d` and `:p3d` renderers."
     [mode]
     (let [mode (u/resolve-constant-key mode texture-wrap-modes)]
       (.textureWrap (current-graphics) mode))))

(defn
  ^{:requires-bindings true
    :processing-name "textWidth()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-width
  "Calculates and returns the width of any text string."
  [^String data]
  (.textWidth (current-graphics) data))

(defn
  ^{:requires-bindings true
    :processing-name "tint()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  tint
  "Sets the fill value for displaying images. Images can be tinted to
  specified colors or made transparent by setting the `alpha`.

  To make an image transparent, but not change it's color, use white
  as the tint color and specify an `alpha` value. For instance,
  `(tint 255 128)` will make an image 50% transparent (unless
  [[color-mode]] has been used).

  The value for the parameter gray must be less than or equal to the
  current maximum value as specified by [[color-mode]]. The default
  maximum value is 255.

  Also used to control the coloring of textures in 3D."
  ([gray] (.tint (current-graphics) (float gray)))
  ([gray alpha] (.tint (current-graphics) (float gray) (float alpha)))
  ([r g b] (.tint (current-graphics) (float r) (float g) (float b)))
  ([r g b a] (.tint (current-graphics) (float g) (float g) (float b) (float a))))

#?(:cljs
   (defn
     ^{:requires-bindings true
       :processing-name "torus()"
       :category "Shape"
       :subcategory "3D Primitives"
       :added "3.0.0"}
     torus
     "Draw a torus with given `radius` and `tube-radius`.

      Optional parameters:
        * `detail-x` - number of segments, the more segments the smoother geometry default is 24
        * `detail-y` - number of segments, the more segments the smoother geometry default is 16"
     ([radius tube-radius]
      (.torus (current-graphics) (float radius) (float tube-radius)))
     ([radius tube-radius detail-x]
      (.torus (current-graphics) (float radius) (float tube-radius) (int detail-x)))
     ([radius tube-radius detail-x detail-y]
      (.torus (current-graphics) (float radius) (float tube-radius) (int detail-x) (int detail-y)))))

(defn
  ^{:requires-bindings true
    :processing-name "translate()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  translate
  "Specifies an amount to displace objects within the display
  window. The `tx` parameter specifies left/right translation, the `ty`
  parameter specifies up/down translation, and the `tz` parameter
  specifies translations toward/away from the screen.  Transformations
  apply to everything that happens after and subsequent calls to the
  function accumulates the effect. For example, calling `(translate 50
  0)` and then `(translate 20, 0)` is the same as `(translate 70, 0)`. If
  [[translate]] is called within draw, the transformation is reset when
  the loop begins again. This function can be further controlled by
  the [[push-matrix]] and [[pop-matrix]] functions."
  ([v] (apply translate v))
  ([tx ty] (.translate (current-graphics) (float tx) (float ty)))
  ([tx ty tz] (.translate (current-graphics) (float tx) (float ty) (float tz))))

(defn
  ^{:requires-bindings true
    :processing-name "triangle()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  triangle
  "A triangle is a plane created by connecting three points. The first
  two arguments specify the first point, the middle two arguments
  specify the second point, and the last two arguments specify the
  third point."
  [x1 y1 x2 y2 x3 y3]
  (.triangle (current-graphics)
             (float x1) (float y1)
             (float x2) (float y2)
             (float x3) (float y3)))

(defn
  ^{:require-binding false
    :processing-name "unbinary()"
    :category "Data"
    :subcategory "Conversion"
    :added "1.0"}
  unbinary
  "Unpack a binary string to an integer. See [[binary]] for converting
  integers to strings."
  [str-val]
  #?(:clj (PApplet/unbinary (str str-val))
     :cljs (js/parseInt str-val 2)))

(defn
  ^{:require-binding false
    :processing-name "hex()"
    :category "Data"
    :subcategory "Conversion"}
  unhex
  "Converts a String representation of a hexadecimal number to its
  equivalent integer value."
  [hex-str]
  #?(:clj (PApplet/unhex (str hex-str))
     :cljs (.unhex (ap/current-applet) (str hex-str))))

(defn
  ^{:requires-bindings true
    :processing-name "updatePixels()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  update-pixels
  "Updates the display window or image with the data in the pixels array.
  Use in conjunction with [[pixels]]. If you're only reading pixels from
  the array, there's no need to call [[update-pixels]] unless there are
  changes.

  Certain renderers may or may not seem to require [[pixels]] or
  [[update-pixels]]. However, the rule is that any time you want to
  manipulate the `pixels` array, you must first call [[pixels]], and
  after changes have been made, call [[update-pixels]]. Even if the
  renderer may not seem to use this function in the current Processing
  release, this will always be subject to change."
  ([] (update-pixels (current-graphics)))
  ([^PImage img] (.updatePixels img)))

(defn
  ^{:requires-bindings true
    :processing-name "vertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  vertex
  "All shapes are constructed by connecting a series of
  vertices. [[vertex]] is used to specify the vertex coordinates for
  points, lines, triangles, quads, and polygons and is used
  exclusively within the [[begin-shape]] and [[end-shape]] functions.

  Drawing a vertex in 3D using the `z` parameter requires the `:p3d` or
  `:opengl` renderers to be used.

  This function is also used to map a texture onto the geometry. The
  [[texture]] function declares the texture to apply to the geometry and the `u`
  and `v` coordinates set define the mapping of this texture to the
  form. By default, the coordinates used for `u` and `v` are specified in
  relation to the image's size in pixels, but this relation can be
  changed with [[texture-mode]]."
  ([x y] (.vertex (current-graphics) (float x) (float y)))
  ([x y z] (.vertex (current-graphics) (float x) (float y) (float z)))
  ([x y u v] #?(:clj (.vertex (current-graphics) (float x) (float y) (float u) (float v))
                :cljs (.vertex (current-graphics) (float x) (float y) 0 (float u) (float v))))
  ([x y z u v]
   (.vertex (current-graphics) (float x) (float y) (float z) (float u) (float v))))

(defn
  ^{:requires-bindings false
    :processing-name "year()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  year
  "Returns the current year as an integer (2003, 2004, 2005, etc)."
  []
  #?(:clj (PApplet/year)
     :cljs (.year (ap/current-applet))))

(defn
  ^{:requires-bindings true
    :processing-name "getWidth()"
    :processing-link nil
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  width
  "Width of the display window. The value of width is zero until size is
  called."
  []
  (.-width (ap/current-applet)))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Color"
    :subcategory "Utility Macros"
    :added "1.7"}
  with-fill
  "Temporarily set the fill color for the body of this macro.
   The code outside of the [[with-fill]] form will have the previous
   fill color set.

   A fill argument of nil disables the fill.

   Examples:
   ```
   (with-fill 255 ...)
   (with-fill [10 80 98] ...)
   (with-fill nil ...)
   ```"
  [fill & body]
  `(let [fill# ~fill
         previous-fill# (quil.core/current-fill)]

     (cond (sequential? fill#) (apply quil.core/fill fill#)
           true (quil.core/fill fill#))

     ;;return the value from body, not from the if after it.
     (let [return-val# (do ~@body)]
       (if (nil? previous-fill#)
         (quil.core/no-fill)
         (quil.core/fill previous-fill#))
       return-val#)))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Color"
    :subcategory "Utility Macros"
    :added "1.7"}
  with-stroke
  "Temporarily set the stroke color for the body of this macro.
   The code outside of the [[with-stroke]] form will have the previous
   stroke color set.

   A stroke argument of nil disables the stroke.

   Examples:
   ```
   (with-stroke 255 ...)
   (with-stroke [10 80 98] ...)
   (with-stroke nil ...)
   ```"
  [stroke & body]
  `(let [stroke# ~stroke
         previous-stroke# (quil.core/current-stroke)]

     (cond (sequential? stroke#) (apply quil.core/stroke stroke#)
           true (quil.core/stroke stroke#))

     ;;return the value from body, not from the if after it.
     (let [return-val# (do ~@body)]
       (if (nil? previous-stroke#)
         (quil.core/no-stroke)
         (quil.core/stroke previous-stroke#))
       return-val#)))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Transform"
    :subcategory "Utility Macros"
    :added "1.0"}
  with-translation
  "Performs body with translation, restores current transformation on
  exit."
  [translation-vector & body]
  `(let [tr# ~translation-vector]
     (quil.core/push-matrix)
     (try
       (quil.core/translate tr#)
       ~@body
       (finally
         (quil.core/pop-matrix)))))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Transform"
    :subcategory "Utility Macros"
    :added "1.0"}
  with-rotation
  "Performs body with rotation, restores current transformation on exit.
  Accepts a vector `[angle]` or `[angle x y z]`.

  When 4 arguments provides it produces a rotation of angle degrees
  around the vector x y z. Check examples to better understand.
  This rotation follows the right-hand rule, so if the vector x y z points
  toward the user, the rotation will be counterclockwise.

  Example:
  ```
    (with-rotation [angle]
      (vertex 1 2))
  ```"
  [rotation & body]
  `(let [tr# ~rotation]
     (quil.core/push-matrix)
     (try
       (apply quil.core/rotate tr#)
       ~@body
       (finally
         (quil.core/pop-matrix)))))

(defn begin-draw [graphics]
  #?(:clj (.beginDraw graphics)))

(defn end-draw [graphics]
  #?(:clj (.endDraw graphics)))

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Rendering"
    :added "1.7"}
  with-graphics
  "All subsequent calls of any drawing function will draw on given
  graphics. [[with-graphics]] cannot be nested (you can draw simultaneously
  only on 1 graphics)"
  [graphics & body]
  `(let [^PGraphics gr# ~graphics]
     (binding [quil.core/*graphics* gr#]
       (begin-draw gr#)
       ~@body
       (end-draw gr#))))

(defn ^{:requires-bindings false
        :category "Environment"
        :subcategory nil
        :added "1.0"}
  sketch
  "Create and start a new visualisation applet. Can be used to create
  new sketches programmatically. See documentation for [[defsketch]] for
  list of available options."
  [& opts]
  #?(:clj (apply ap/applet opts)
     :cljs (apply ap/sketch opts)))

(defmacro ^{:requires-bindings false
            :category "Environment"
            :subcategory nil
            :added "1.0"}
  defsketch
  "Define and start a sketch and bind it to a var with the symbol
  `app-name`. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original functions are reflected in
  the visualisation.

  * `:size`           - A vector of width and height for the sketch or :fullscreen.
                        Defaults to `[500 300]`. If you're using :fullscreen you may
                        want to enable present mode - :features [:present].
                        :fullscreen size works only in Clojure. In ClojureScript
                        all sketches are support fullscreen when you press F11.
  * `:renderer`       - Specifies the renderer type. One of `:p2d`, `:p3d`, `:java2d`,
                        `:opengl`, `:pdf`, `:svg`). Defaults to `:java2d`. `:dxf` renderer
                        can't be used as sketch renderer. Use [[begin-raw]] method
                        instead. In clojurescript only `:p2d` and `:p3d` renderers
                        are supported.
  * `:output-file`    - Specifies an output file path. Only used in `:pdf` and `:svg`
                        modes. Not supported in clojurescript. When writing to a
                        file, call [[exit]] at the end of the draw call to end
                        the sketch and not write repeatedly to the file.
  * `:title`          - A string which will be displayed at the top of
                        the sketch window. Not supported in clojurescript.
  * `:features`       - A vector of keywords customizing sketch behaviour.
                        Supported features:
    - `:keep-on-top`   - Sketch window will always be above other windows.
                         Note: some platforms might not support always-on-top windows.
                         Not supported in clojurescript.
    - `:exit-on-close` - Shutdown JVM  when sketch is closed.
                         Not supported in clojurescript.
    - `:resizable`     - Makes sketch resizable. Not supported in clojurescript.
    - `:no-safe-fns`   - Do not catch and print exceptions thrown inside functions
                         provided to sketch (like draw, [[mouse-click]],
                         [[key-pressed]] and others). By default all exceptions
                         thrown inside these functions are caught. This prevents
                         sketch from breaking when bad function was provided and
                         allows you to fix it and reload it on fly. You can
                         disable this behaviour by enabling `:no-safe-fns`
                         feature. Not supported in clojurescript.
    - `:present`       - Switch to present mode (fullscreen without borders, OS
                         panels). You may want to use this feature together with
                         `:size :fullscreen`. Not supported in ClojureScript. In
                         ClojureScript fullscreen is enabled by pressing F11 and
                         it's enabled on all sketches automatically.
    - `:no-start`      - Disables autostart if sketch was created using defsketch
                         macro. To start sketch you have to call function created
                         defsketch. Supported only in ClojureScript.
                        Usage example: `:features [:keep-on-top :present]`
  * `:bgcolor`        - Sets background color for unused space in present mode.
                        Color is specified in hex format for example
                        `:bgcolor \"#00FFFF\"` (cyan background)
                        Not supported in ClojureScript.
  * `:display`        - Sets what display should be used by this sketch.
                        Displays are numbered starting from 0. Example: `:display 1`.
                        Not supported in ClojureScript.
  * `:setup`          - A function to be called once when setting the sketch up.
  * `:draw`           - A function to be repeatedly called at most n times per
                        second where n is the target [[frame-rate]] set for
                        the visualisation.
  * `:host`           - String id of canvas element or DOM element itself.
                        Specifies host for the sketch. Must be specified in sketch,
                        may be omitted in defsketch. If omitted in defsketch,
                        :host is set to the name of the sketch. If element with
                        specified id is not found on the page and page is empty -
                        new canvas element will be created. Used in ClojureScript.
  * `:focus-gained`   - Called when the sketch gains focus.
                        Not supported in ClojureScript.
  * `:focus-lost`     - Called when the sketch loses focus.
                        Not supported in ClojureScript.
  * `:mouse-entered`  - Called when the mouse enters the sketch window.
  * `:mouse-exited`   - Called when the mouse leaves the sketch window
  * `:mouse-pressed`  - Called every time a mouse button is pressed.
  * `:mouse-released` - Called every time a mouse button is released.
  * `:mouse-clicked`  - Called once after a mouse button has been pressed
                        and then released.
  * `:mouse-moved`    - Called every time the mouse moves and a button is
                        not pressed.
  * `:mouse-dragged`  - Called every time the mouse moves and a button is
                        pressed.
  * `:mouse-wheel`    - Called every time mouse wheel is rotated.
                        Takes 1 argument - wheel rotation, an int.
                        Negative values if the mouse wheel was rotated
                        up/away from the user, and positive values
                        if the mouse wheel was rotated down/ towards the user
  * `:key-pressed`    - Called every time any key is pressed.
  * `:key-released`   - Called every time any key is released.
  * `:key-typed`      - Called once every time non-modifier keys are
                        pressed.
  * `:on-close`       - Called once, when sketch is closed.
                        Not supported in ClojureScript.
  * `:middleware`     - Vector of middleware to be applied to the sketch.
                        Middleware will be applied in the same order as in comp
                        function: [f g] will be applied as `(f (g options))`.
  * `:settings`       - Cousin of `:setup`. A function to be called once when
                        setting sketch up. Should be used only for [[smooth]] and
                        [[no-smooth]]. Due to Processing limitations these functions
                        cannot be used neither in `:setup` nor in `:draw`."
  [app-name & options]
  #?(:clj
     (if (u/clj-compilation?)
       `(ap/defapplet ~app-name ~@options)
       `(quil.sketch/defsketch ~app-name ~@options))
     :cljs
     `(quil.sketch$macros/defsketch ~app-name ~@options)))

(defn ^{:requires-bindings false
        :processing-name nil
        :category "Input"
        :subcategory "Keyboard"
        :added "1.6"}
  key-coded?
  "Returns true if char `c` is a `coded` char i.e. it is necessary to
  fetch the [[key-code]] as an integer and use that to determine the
  specific key pressed. See [[key-as-keyword]]."
  [c]
  #?(:clj (= PConstants/CODED (int c))
     ; See https://github.com/google/closure-compiler/issues/1676
     :cljs (= 65535 (.charCodeAt (js/String c)))))

(defn ^{:requires-bindings true
        :processing-name nil
        :category "Input"
        :subcategory "Keyboard"
        :added "1.6"}
  key-as-keyword
  "Returns a keyword representing the currently pressed key. Modifier
  keys are represented as: `:up`, `:down`, `:left`, `:right`, `:alt`, `:control`,
  `:shift`, `:command`, `:f1-24`"
  []
  (let [key-char (raw-key)
        code     (key-code)]
    (if (key-coded? key-char)
      (get KEY-CODES code :unknown-key)
      ; Workaround for closure compiler incorrect string casts.
      ; See https://github.com/google/closure-compiler/issues/1676
      (let [key-str #?(:clj (str key-char)
                       :cljs (js/String key-char))]
        (or (KEY-MAP key-str)
            (keyword key-str))))))

#?(:clj
   (defn
     ^{:requires-bindings false
       :processing-name nil
       :category "Debugging"
       :added "1.6"}
     debug
     "Prints `msg` and then sleeps the current thread for `delay-ms`. Useful
  for debugging live running sketches. `delay-ms` defaults to 300. "
     ([msg] (debug msg 300))
     ([msg delay-ms]
      (println msg)
      (Thread/sleep delay-ms))))

;;; doc utils

#?(:clj
   (def ^{:private true}
     fn-metas
     "Returns a seq of metadata maps for all fns related to the original
  Processing API (but may not have a direct Processing API equivalent)."
     (->> *ns* ns-publics vals (map meta))))

#?(:clj
   (defn show-cats
     "Print out a list of all the categories and subcategories,
  associated index nums and fn count (in parens)."
     []
     (doseq [[cat-idx cat] (docs/sorted-category-map fn-metas)]
       (println cat-idx (:name cat))
       (doseq [[subcat-idx subcat] (:subcategories cat)]
         (println "  " subcat-idx (:name subcat))))))

#?(:clj
   (defn show-fns
     "If given a number, print all the functions within category or
  subcategory specified by the category index (use [[show-cats]] to see a
  list of index nums).

  If given a string or a regular expression, print all the functions
  whose name or category name contains that string.

  If a category is specified, it will not print out the fns in any of
  cat's subcategories."
     [q]
     (letfn [(list-category [cid c & {:keys [only]}]
               (let [category-fns (:fns c)
                     display-fns (if (nil? only)
                                   category-fns
                                   (clojure.set/intersection
                                    (set only) (set category-fns)))
                     names (sort (map str display-fns))]
                 (when-not (empty? names))
                 (println cid (:name c))
                 (docs/pprint-wrapped-lines names :fromcolumn 4)))
             (show-fns-by-cat-idx [cat-idx]
                                  (let [c (get (docs/all-category-map fn-metas) (str cat-idx))]
                                    (list-category cat-idx c)))
             (show-fns-by-name-regex [re]
                                     (doseq [[cid c] (sort-by key (docs/all-category-map fn-metas))]
                                       (let [in-cat-name? (re-find re (:name c))
                                             matching-fns (filter #(re-find re (str %)) (:fns c))
                                             in-fn-names? (not (empty? matching-fns))]
                                         (cond
                                           in-cat-name? (list-category cid c) ;; print an entire category
                                           in-fn-names? (list-category cid c :only matching-fns)))))]
       (cond
         (string? q) (show-fns-by-name-regex (re-pattern (str "(?i)" q)))
         (isa? (type q) java.util.regex.Pattern) (show-fns-by-name-regex q)
         :else (show-fns-by-cat-idx q)))))

#?(:clj
   (defn show-meths
     "Takes a string representing the start of a method name in the
  original Processing API and prints out all matches alongside the
  Processing-core equivalent."
     [orig-name]
     (let [res (docs/matching-processing-methods fn-metas orig-name)]
       (u/print-definition-list res))))
