;; simple processing wrapper for Clojure
;; Roland Sadowski [szabla gmail com]

;; Copyright (c) 2008 Roland Sadowski. All rights reserved.  The use and
;; distribution terms for this software are covered by the Common
;; Public License 1.0 (http://www.opensource.org/licenses/cpl1.0.php)
;; which can be found in the file CPL.TXT at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software.

(ns processing.core
  (:import (processing.core PApplet PImage PGraphics PFont PConstants))
  (:load "constants"))

;; used by functions in this lib. Use binding to set it
;; to an instance of processing.core.PApplet
(def ^{:dynamic true} ^PApplet *applet*)
(def ^{:dynamic true} *state*)

(defn- int-like?
  [val]
  (let [t (type val)]
    (or (= java.lang.Long t)
        (= java.lang.Integer t))))

(defn state
  "Retrieve canvas-specific state by key. Must initially call
  set-state! to store state.

  (set-state! :foo 1)
  (state :foo) ;=> 1 "
  [key]
  (when-not @*state*
    (throw (Exception. "State not set - use set-state! before fetching state")))

  (let [state @*state*]
    (when-not (contains? state key)
      (throw (Exception. (str "Unable to find state with key: " key))))
    (get state key)))

(defn set-state!
  "Set canvas-specific state. May only be called once (ideally in the
  setup fn).  Subsequent calls have no effect.

  Example:
  (set-state! :foo 1 :bar (atom true) :baz (/ (width) 2))"
  [& state-vals]
  (when-not @*state*
    (let [state-map (apply hash-map state-vals)]
      (reset! *state* state-map))))

(defn abs-int
  "Calculates the absolute value (magnitude) of a number. The absolute
  value of a number is always positive. Takes and returns an int."
  [n]
  (PApplet/abs (int n)))

(defn abs-float
  "Calculates the absolute value (magnitude) of a number. The absolute
  value of a number is always positive. Takes and returns a float."
  [n]
  (PApplet/abs (float n)))

(defn abs
  "Calculates the absolute value (magnitude) of a number. The
  absolute value of a number is always positive. Dynamically casts to
  an int or float appropriately"
    [n]
    (if (int-like? n)
      (abs-int n)
      (abs-float n)))

(defn acos
  "The inverse of cos, returns the arc cosine of a value. This
  function expects the values in the range of -1 to 1 and values are
  returned in the range 0 to Math/PI (3.1415927)."
  [n]
  (PApplet/acos (float n)))

(defn alpha
  "Extracts the alpha value from a color."
  [color] (.alpha *applet* (int color)))

(defn ambient-float
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The
  color components set through the parameters define the
  reflectance. For example in the default color mode, setting x=255,
  y=126, z=0, would cause all the red light to reflect and half of the
  green light to reflect. Used in combination with emissive, specular,
  and shininess in setting the material properties of shapes."
  ([gray] (.ambient *applet* (float gray)))
  ([x y z] (.ambient *applet* (float x) (float y) (float z))))

(defn ambient-int
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The rgb
  color components set define the reflectance. Used in combination
  with emissive, specular, and shininess in setting the material
  properties of shapes."
  [rgb]
  (.ambient
   *applet* (int rgb)))

(defn ambient
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The
  color components set through the parameters define the
  reflectance. For example in the default color mode, setting x=255,
  y=126, z=0, would cause all the red light to reflect and half of the
  green light to reflect. Used in combination with emissive, specular,
  and shininess in setting the material properties of shapes."
  ([rgb] (if (int-like? rgb) (ambient-int rgb) (ambient-float rgb)))
  ([x y z] (ambient-float x y z)))

(defn ambient-light
  "Adds an ambient light. Ambient light doesn't come from a specific direction,
   the rays have light have bounced around so much that objects are
   evenly lit from all sides. Ambient lights are almost always used in
   combination with other types of lights. Lights need to be included
   in the draw to remain persistent in a looping program. Placing them
   in the setup of a looping program will cause them to only have an
   effect the first time through the loop. The effect of the
   parameters is determined by the current color mode."
  ([red green blue]
     (.ambientLight *applet* (float red) (float green) (float blue)))
  ([red green blue x y z]
     (.ambientLight *applet* (float red) (float green) (float blue)
                    (float x) (float y) (float z))))

(defn apply-matrix
  "Multiplies the current matrix by the one specified through the
  parameters. This is very slow because it will try to calculate the
  inverse of the transform, so avoid it whenever possible. The
  equivalent function in OpenGL is glMultMatrix()."
  ([n00 n01 n02 n10 n11 n12]
     (.applyMatrix *applet* (float n00) (float n01) (float n02)
                   (float n10) (float n11) (float n12)))
  ([n00 n01 n02 n03
    n10 n11 n12 n13
    n20 n21 n22 n23
    n30 n31 n32 n33]
     (.applyMatrix *applet* (float n00) (float n01) (float n02) (float 03)
                   (float n10) (float n11) (float n12) (float 13)
                   (float n20) (float n21) (float n22) (float 23)
                   (float n30) (float n31) (float n32) (float 33))))

(defn arc
  "Draws an arc in the display window. Arcs are drawn along the outer
  edge of an ellipse defined by the x, y, width and height
  parameters. The origin or the arc's ellipse may be changed with the
  ellipseMode() function. The start and stop parameters specify the
  angles at which to draw the arc."
  [x y width height start stop]
  (.arc *applet* (float x)(float y) (float width) (float height)
        (float start) (float stop)))

(defn asin
  "The inverse of sin, returns the arc sine of a value. This function
  expects the values in the range of -1 to 1 and values are returned
  in the range -PI/2 to PI/2."
  [n]
  (PApplet/asin (float n)))

(defn atan
  "The inverse of tan, returns the arc tangent of a value. This
  function expects the values in the range of -Infinity to
  Infinity (exclusive) and values are returned in the range -PI/2 to
  PI/2 ."
  [n]
  (PApplet/atan (float n)))

(defn atan2
  "Calculates the angle (in radians) from a specified point to the
  coordinate origin as measured from the positive x-axis. Values are
  returned as a float in the range from PI to -PI. The atan2 function
  is most often used for orienting geometry to the position of the
  cursor. Note: The y-coordinate of the point is the first parameter
  and the x-coordinate is the second due the the structure of
  calculating the tangent."
  [y x]
  (PApplet/atan2 (float y) (float x)))

(defn background-float
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  create-graphics. Converts args to floats."
  ([gray] (.background *applet* (float gray)))
  ([gray alpha] (.background *applet* (float gray) (float alpha)))
  ([r g b] (.background *applet* (float r) (float g) (float b)))
  ([r g b a] (.background *applet* (float r) (float g) (float b) (float a))))

(defn background-int
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  create-graphics. Converts rgb to an int and alpha to a float."
  ([rgb] (.background *applet* (int rgb)))
  ([rgb alpha] (.background *applet* (int rgb) (float alpha))))

(defn background
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  create-graphics. Converts args to floats."
  ([rgb] (if (int-like? rgb) (background-int rgb) (background-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (background-int rgb alpha) (background-float rgb alpha)))
  ([r g b] (background-float r g b))
  ([r g b a] (background-float r g b a)))

(defn background-image
  "Specify an image to be used as the background for a sketch. Its
  width and height must be the same size as the sketch window. Images
  used as background will ignore the current tint setting."
  [^PImage img]
  (.background *applet* img))

(defn begin-camera
  "Sets the matrix mode to the camera matrix so calls such as
  translate, rotate, apply-matrix and reset-matrix affect the
  camera. begin-camera should always be used with a following
  end-camera and pairs of begin-camera and end-camera cannot be
  nested.

  For most situations the camera function will be sufficient."
  []
  (.beginCamera *applet*))

(defn end-camera
  "Unsets the matrix mode from the camera matrix. See begin-camera."
  []
  (.endCamera *applet*))

(defn begin-raw
  "Enables the creation of vectors from 3D data. Requires
  corresponding end-raw command. These commands will grab the shape
  data just before it is rendered to the screen. At this stage, your
  entire scene is nothing but a long list of individual lines and
  triangles. This means that a shape created with sphere method will
  be made up of hundreds of triangles, rather than a single object. Or
  that a multi-segment line shape (such as a curve) will be rendered
  as individual segments."
  ([^PGraphics raw-gfx] (.beginRaw *applet* raw-gfx))
  ([^String renderer ^String filename]
     (.beginRaw *applet* renderer filename)))

(def ^{:private true}
  render-modes {:p2d P2D
                :p3d P3D
                :java2d JAVA2D
                :opengl OPENGL
                :pdf PDF
                :dxf DXF})

(defn- resolve-render-mode
  [mode]
  (if (keyword? mode)
    (get render-modes mode)
    mode))

(defn begin-record
  "Opens a new file and all subsequent drawing functions are echoed to
  this file as well as the display window. The begin-record fn
  requires two parameters, the first is the renderer and the second is
  the file name. This function is always used with end-record to stop
  the recording process and close the file.

  Note that begin-record will only pick up any settings that happen
  after it has been called. For instance, if you call text-font
  before begin-record, then that font will not be set for the file
  that you're recording to."
  [renderer filename]
  (let [renderer (resolve-render-mode renderer)]
    (println "renderer: " renderer)
    (.beginRecord *applet* (str renderer) (str filename))))

(defn end-record
  "Stops the recording process started by begin-record and closes the
  file."
  []
  (.endRecord *applet*))

(def ^{:private true}
     shapes-map {:points POINTS
                 :lines LINES
                 :triangles TRIANGLES
                 :triangle-strip TRIANGLE-STRIP
                 :triangle-fan TRIANGLE-FAN
                 :quads QUADS
                 :quad-strip QUAD-STRIP})

(defn- resolve-shape-mode
  [mode]
  (if (keyword? mode)
    (get shapes-map mode)
    mode))

(defn begin-shape
  "Enables the creation of complex forms. begin-shape begins recording
  vertices for a shape and end-shape stops recording. Use the mode
  keyword to specify which shape create from the provided
  vertices. With no mode specified, the shape can be any irregular
  polygon.

  The available mode keywords are :points, :lines, :triangles,
                                  :triangle-fan, :triangle-strip,
                                  :quads, :quad-strip.

  After calling the begin-shape function, a series of vertex commands
  must follow. To stop drawing the shape, call end-shape. The vertex
  function with two parameters specifies a position in 2D and the
  vertex function with three parameters specifies a position in
  3D. Each shape will be outlined with the current stroke color and
  filled with the fill color.

  Transformations such as translate, rotate, and scale do not work
  within begin-shape. It is also not possible to use other shapes,
  such as ellipse or rect within begin-shape."
  ([] (.beginShape *applet*))
  ([mode]
     (let [mode (resolve-shape-mode  mode)]
       (.beginShape *applet* (int mode)))))

(defn bezier
  "Draws a Bezier curve on the screen. These curves are defined by a
  series of anchor and control points. The first two parameters
  specify the first anchor point and the last two parameters specify
  the other anchor point. The middle parameters specify the control
  points which define the shape of the curve."
  ([x1 y1 cx1 cy1 cx2 cy2 x2 y2]
     (.bezier *applet*
              (float x1) (float y1)
              (float cx1) (float cy1)
              (float cx2) (float cy2)
              (float x2) (float y2)))
  ([x1 y1 z1 cx1 cy1 cz1 cx2 cy2 cz2 x2 y2 z2]
     (.bezier *applet*
              (float x1) (float y1) (float z1)
              (float cx1) (float cy1) (float cz1)
              (float cx2) (float cy2) (float cz2)
              (float x2) (float y2) (float z2))))

(defn bezier-detail
  "Sets the resolution at which Beziers display. The default value is
  20. This function is only useful when using the P3D or OPENGL
  renderer as the default (JAVA2D) renderer does not use this
  information."
  [detail]
  (.bezierDetail *applet* (int detail)))

(defn bezier-point
  "Evaluates the Bezier at point t for points a, b, c, d. The
  parameter t varies between 0 and 1, a and d are points on the curve,
  and b and c are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a bezier curve at t."
  [a b c d t]
  (.bezierPoint *applet* (float a) (float b) (float c)
                (float d) (float t)))

(defn bezier-tangent
  "Calculates the tangent of a point on a Bezier curve.
  (See http://en.wikipedia.org/wiki/Tangent)"
  [a b c d t]
  (.bezierTangent *applet* (float a) (float b) (float c)
                  (float d) (float t)))

(defn bezier-vertex
  "Specifies vertex coordinates for Bezier curves. Each call to
  bezier-vertex defines the position of two control points and one
  anchor point of a Bezier curve, adding a new segment to a line or
  shape. The first time bezier-vertex is used within a begin-shape
  call, it must be prefaced with a call to vertex to set the first
  anchor point. This function must be used between begin-shape and
  end-shape and only when there is no parameter specified to
  begin-shape."
  ([cx1 cy1 cx2 cy2 x y]
     (.bezierVertex *applet*
                    (float cx1) (float cx1)
                    (float cx2) (float cy2)
                    (float x) (float y)))
  ([cx1 cy1 cz1 cx2 cy2 cz2 x y z]
     (.bezierVertex *applet*
                    (float cx1) (float cy1) (float cz1)
                    (float cx2) (float cy2) (float cz2)
                    (float x) (float y) (float z))))

(defn binary
  "Returns a string representing the binary value of an int, char or
  byte. When converting an int to a string, it is possible to specify
  the number of digits used."
  ([val] (PApplet/binary (if (number? val) (int val) val)))
  ([val num-digits] (PApplet/binary (int val) (int num-digits))))

(defn unbinary
  "Unpack a binary string to an integer. See binary for converting
  integers to strings."
  [str-val]
  (PApplet/unbinary (str str-val)))

(def ^{:private true}
  blend-map {:blend BLEND
             :add ADD
             :subtract SUBTRACT
             :darkest DARKEST
             :lightest LIGHTEST
             :difference DIFFERENCE
             :exclusion EXCLUSION
             :multiply MULTIPLY
             :screen SCREEN
             :overlay OVERLAY
             :hard-light HARD-LIGHT
             :soft-light SOFT-LIGHT
             :dodge DODGE
             :burn BURN})

(defn- resolve-blend-mode
  [mode]
  (if (keyword? mode)
    (get blend-map mode)
    mode))

(defn blend
  "Blends a region of pixels from one image into another (or in itself
  again) with full alpha channel support.

  Available blend modes are:

  :blend      - linear interpolation of colours: C = A*factor + B
  :add        - additive blending with white clip:
                                            C = min(A*factor + B, 255)
  :subtract   - subtractive blending with black clip:
                                            C = max(B - A*factor, 0)
  :darkest    - only the darkest colour succeeds:
                                            C = min(A*factor, B)
  :lightest   - only the lightest colour succeeds:
                                            C = max(A*factor, B)
  :difference - subtract colors from underlying image.
  :exclusion  - similar to :difference, but less extreme.
  :multiply   - Multiply the colors, result will always be darker.
  :screen     - Opposite multiply, uses inverse values of the colors.
  :overlay    - A mix of :multiply and :screen. Multiplies dark values
                and screens light values.
  :hard-light - :screen when greater than 50% gray, :multiply when
                lower.
  :soft-light - Mix of :darkest and :lightest. Works like :overlay,
                but not as harsh.
  :dodge      - Lightens light tones and increases contrast, ignores
                darks.
                Called \"Color Dodge\" in Illustrator and Photoshop.
  :burn       - Darker areas are applied, increasing contrast, ignores
                lights. Called \"Color Burn\" in Illustrator and
                Photoshop."
  ([x y width height dx dy dwidth dheight mode]
     (let [mode (resolve-blend-mode mode)]
       (.blend *applet* (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode))))
  ([^PImage src x y width height dx dy dwidth dheight mode]
     (let [mode (resolve-blend-mode mode)]
       (.blend *applet* src (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode)))))

(defn blend-color
  "Blends two color values together based on the blending mode given specified
  with the mode keyword.

  Available blend modes are:


  :blend      - linear interpolation of colours: C = A*factor + B
  :add        - additive blending with white clip:
                                            C = min(A*factor + B, 255)
  :subtract   - subtractive blending with black clip:
                                            C = max(B - A*factor, 0)
  :darkest    - only the darkest colour succeeds:
                                            C = min(A*factor, B)
  :lightest   - only the lightest colour succeeds:
                                            C = max(A*factor, B)
  :difference - subtract colors from underlying image.
  :exclusion  - similar to :difference, but less extreme.
  :multiply   - Multiply the colors, result will always be darker.
  :screen     - Opposite multiply, uses inverse values of the colors.
  :overlay    - A mix of :multiply and :screen. Multiplies dark values
                and screens light values.
  :hard-light - :screen when greater than 50% gray, :multiply when
                lower.
  :soft-light - Mix of :darkest and :lightest. Works like :overlay,
                but not as harsh.
  :dodge      - Lightens light tones and increases contrast, ignores
                darks.
                Called \"Color Dodge\" in Illustrator and Photoshop.
  :burn       - Darker areas are applied, increasing contrast, ignores
                lights. Called \"Color Burn\" in Illustrator and
                Photoshop."
  [c1 c2 mode]
  (let [mode (resolve-blend-mode mode)]
    (PApplet/blendColor (int c1) (int c2) (int mode))))

(defn blue
  "Extracts the blue value from a color, scaled to match current color-mode.
  Returns a float."
  [color]
  (.blue *applet* (int color)))

(defn box
  "Creates an extruded rectangle."
  ([size] (.box *applet* (float size)))
  ([width height depth] (.box *applet* (float width) (float height) (float depth))))

(defn brightness
  "Extracts the brightness value from a color. Returns a float."
  [color]
  (.brightness *applet* (int color)))

(defn camera
  "Sets the position of the camera through setting the eye position,
  the center of the scene, and which axis is facing upward. Moving the
  eye position and the direction it is pointing (the center of the
  scene) allows the images to be seen from different angles. The
  version without any parameters sets the camera to the default
  position, pointing to the center of the display window with the Y
  axis as up. The default values are:

  eyeX:     (/ (width) 2.0)
  eyeY:     (/ (height) 2.0)
  eyeZ:     (/ (/ (height) 2.0) (tan (/ (* Math/PI 60.0) 360.0)))
  centerX:  (/ (width) 2.0)
  centerY:  (/ (height) 2.0)
  centerZ:  0
  upX:      0
  upY:      1
  upZ:      0

  Similar imilar to gluLookAt() in OpenGL, but it first clears the
  current camera settings."
  ([] (.camera *applet*))
  ([eyeX eyeY eyeZ centerX centerY centerZ upX upY upZ]
     (.camera *applet* (float eyeX) (float eyeY) (float eyeZ)
              (float centerX) (float centerY) (float centerZ)
              (float upX) (float upY) (float upZ))))

(defn ceil
  "Calculates the closest int value that is greater than or equal to
  the value of the parameter. For example, (ceil 9.03) returns the
  value 10."
  [n]
  (PApplet/ceil (float n)))

(defn color
  "Creates an integer representation of a color The parameters are
  interpreted as RGB or HSB values depending on the current
  color-mode. The default mode is RGB values from 0 to 255 and
  therefore, the function call (color 255 204 0) will return a bright
  yellow. Args are cast to floats.

  r - red or hue value
  g - green or saturation value
  b - blue or brightness value
  a - alpha value"
  ([gray] (.color *applet* (float gray)))
  ([gray alpha] (.color *applet* (float gray) (float alpha)))
  ([r g b] (.color *applet* (float r) (float g) (float b)))
  ([r g b a] (.color *applet* (float r) (float g) (float b) (float a))))

(defn ^{:private true}
  color-modes {:rgb RGB
               :hsb HSB})

(defn- resolve-color-mode
  [mode]
  (if (keyword? mode)
    (get color-modes mode)
    mode))

(defn color-mode
  "Changes the way Processing interprets color data. Available modes
  are :rgb and :hsb.By default, the parameters for fill, stroke,
  background, and color are defined by values between 0 and 255 using
  the :rgb color model. The color-mode fn is used to change the
  numerical range used for specifying colors and to switch color
  systems. For example, calling
  (color-mode :rgb 1.0) will specify that values are specified between
  0 and 1. The limits for defining colors are altered by setting the
  parameters range1, range2, range3, and range 4."
  ([mode]
     (let [mode (resolve-color-mode mode)]
       (.colorMode *applet* (int mode))))
  ([mode max]
     (let [mode (resolve-color-mode mode)]
       (.colorMode *applet* (int mode) (float max))))
  ([mode max-x max-y max-z]
     (let [mode (resolve-color-mode mode)]
       (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z))))
  ([mode max-x max-y max-z max-a]
     (let [mode (resolve-color-mode mode)]
       (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z) (float max-a)))))

(defn constrain-float
  "Constrains a value to not exceed a maximum and minimum value. All
  args are cast to floats."
  [amt low high]
  (PApplet/constrain (float amt) (float low) (float high)))

(defn constrain-int
  "Constrains a value to not exceed a maximum and minimum value. All
  args are cast to ints."
  [amt low high]
  (PApplet/constrain (int amt) (int low) (int high)))

(defn constrain
  "Constrains a value to not exceed a maximum and minimum value."
  [amt low high]
  (if (int-like? amt)
    (constrain-int amt low high)
    (constrain-float amt low high)))

(defn copy
  "Copies a region of pixels from the display window to another area
  of the display window and copies a region of pixels from an image
  used as the src-img parameter into the display window. If the source
  and destination regions aren't the same size, it will automatically
  resize the source pixels to fit the specified target region. No
  alpha information is used in the process, however if the source
  image has an alpha channel set, it will be copied as well. "
  ([[sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
     (.copy *applet* (int sx1) (int sy1) (int sx2) (int sy2)
            (int dx1) (int dy1) (int dx2) (int dy2)))
  ([^PImage img [sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
     (.copy *applet* img (int sx1) (int sy1) (int sx2) (int sy2)
            (int dx1) (int dy1) (int dx2) (int dy2))))

(defn cos
  "Calculates the cosine of an angle. This function expects the values
  of the angle parameter to be provided in radians (values from 0 to
  Math/PI*2). Values are returned in the range -1 to 1."
  [angle] (PApplet/cos (float angle)))

(defn create-font
  ([name size] (.createFont *applet* name (float size)))
  ([name size smooth] (.createFont *applet* name (float size) smooth))
  ([name size smooth ^chars charset]
     (.createFont *applet* name (float size) smooth charset)))

(defn create-graphics
  ([w h renderer]
     (.createGraphics *applet* (int w) (int h) renderer))
  ([w h renderer path]
     (.createGraphics *applet* (int w) (int h) renderer path)))

(defn create-image [w h format]
  (.createImage *applet* (int w) (int h) (int format)))

(defn create-input [filename]
  (PApplet/createInput (java.io.File. filename)))

(defn create-input-raw
  "Call openStream() without automatic gzip decompression."
  [filename]
  (.createInputRaw *applet* filename))

(defn create-output [filename]
  (PApplet/createOutput (java.io.File. filename)))

(defn create-path [filename] (PApplet/createPath filename))

(defn create-reader [filename] (.createReader *applet* filename))

(defn create-writer [filename] (.createWriter *applet* filename))

(def ^{:private true}
  cursor-modes {:arrow PConstants/ARROW
                :cross PConstants/CROSS
                :hand PConstants/HAND
                :move PConstants/MOVE
                :text PConstants/TEXT
                :wait PConstants/WAIT})

(defn- resolve-cursor-mode
  [mode]
  (if (keyword? mode)
    (get cursor-modes mode)
    mode))

(defn cursor
  "Sets the cursor to a predefined symbol, an image, or makes it
  visible if already hidden. If you are trying to set an image as the
  cursor, it is recommended to make the size 16x16 or 32x32 pixels.
  The values for parameters x and y must be less than the dimensions
  of the image.

  Available modes: :arrow, :cross, :hand, :move, :text, :wait

  Setting or hiding the cursor generally does not work with 'Present'
  mode (when running full-screen).

  See cursor-image for specifying a generic image as the cursor
  symbol."
  ([] (.cursor *applet*))
  ([cursor-mode] (.cursor *applet* (int (resolve-cursor-mode cursor-mode)))))

(defn cursor-image
  "Set the cursor to a predefined image. The horizontal and vertical
  active spots of the cursor may be specified with hx and hy"
  ([^PImage img] (.cursor *applet* img))
  ([^PImage img hx hy] (.cursor *applet* img (int hx) (int hy))))

(defn curve
  "Draws a curved line on the screen. The first and second parameters
  specify the beginning control point and the last two parameters
  specify the ending control point. The middle parameters specify the
  start and stop of the curve. Longer curves can be created by putting
  a series of curve fns together or using curve-vertex. An additional
  fn called curve-tightness provides control for the visual quality of
  the curve. The curve fn is an implementation of Catmull-Rom
  splines."
  ([x1 y1 x2 y2 x3 y3 x4 y4]
     (.curve *applet*
             (float x1) (float y1)
             (float x2) (float y2)
             (float x3) (float y3)
             (float x4) (float y4)))
  ([x1 y1 z1 x2 y2 z2 x3 y3 z3 x4 y4 z4]
     (.curve *applet*
             (float x1) (float y1) (float z1)
             (float x2) (float y2) (float z2)
             (float x3) (float y3) (float z3)
             (float x4) (float y4) (float z4))))

(defn curve-detail
  "Sets the resolution at which curves display. The default value is
  20. This function is only useful when using the P3D or OPENGL
  renderer as the default (JAVA2D) renderer does not use this
  information."
  [detail]
  (.curveDetail *applet* (int detail)))

(defn curve-point
  "Evalutes the curve at point t for points a, b, c, d. The parameter
  t varies between 0 and 1, a and d are points on the curve, and b and
  c are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a curve at t."
  [a b c d t]
  (.bezierPoint *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tangent
  "Calculates the tangent of a point on a curve.
  See: http://en.wikipedia.org/wiki/Tangent"
  [a b c d t]
  (.curveTangent *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tightness
  "Modifies the quality of forms created with curve and
  curve-vertex. The parameter squishy determines how the curve fits
  to the vertex points. The value 0.0 is the default value for
  squishy (this value defines the curves to be Catmull-Rom splines)
  and the value 1.0 connects all the points with straight
  lines. Values within the range -5.0 and 5.0 will deform the curves
  but will leave them recognizable and as values increase in
  magnitude, they will continue to deform."
  [ti]
  (.curveTightness *applet* (float ti)))

(defn curve-vertex
  "Specifies vertex coordinates for curves. This function may only be
  used between begin-shape and end-shape and only when there is no
  mode keyword specified to begin-hape. The first and last points in a
  series of curve-vertex lines will be used to guide the beginning and
  end of a the curve. A minimum of four points is required to draw a
  tiny curve between the second and third points. Adding a fifth point
  with curveVertex will draw the curve between the second, third, and
  fourth points. The curveVertex function is an implementation of
  Catmull-Rom splines."
  ([x y] (.curveVertex *applet* (float x) (float y)))
  ([x y z] (.curveVertex *applet* (float x) (float y) (float z))))

;; $$dataFile
;; $$dataPath

(defn day
  "Get the current day of the month (1 through 31)."
  []
  (PApplet/day))

(defn degrees
  "Converts a radian measurement to its corresponding value in
  degrees. Radians and degrees are two ways of measuring the same
  thing. There are 360 degrees in a circle and (* 2 Math/PI) radians
  in a circle. For example, (= 90° (/ Math/PI 2) 1.5707964). All
  trigonometric methods in Processing require their parameters to be
  specified in radians."
  [radians]
  (PApplet/degrees (float radians)))

(defn delay-frame
  "Forces the program to stop running for a specified time. Delay
  times are specified in thousandths of a second, therefore the
  function call (delay 3000) will stop the program for three
  seconds. Because the screen is updated only at the end of draw,
  the program may appear to 'freeze', because the screen will not
  update when the delay fn is used. This function has no affect
  inside setup."
  [freeze-ms]
  (.delay *applet* (int freeze-ms)))

(defn directional-light
  "Adds a directional light. Directional light comes from one
  direction and is stronger when hitting a surface squarely and weaker
  if it hits at a a gentle angle. After hitting a surface, a
  directional lights scatters in all directions. Lights need to be
  included in the draw fn to remain persistent in a looping
  program. Placing them in the setup fn of a looping program will cause
  them to only have an effect the first time through the loop. The
  affect of the r, g, and b parameters is determined by the current
  color mode. The nx, ny, and nz parameters specify the direction the
  light is facing. For example, setting ny to -1 will cause the
  geometry to be lit from below (the light is facing directly upward)"

  [r g b nx ny nz]
  (.directionalLight *applet* (float r) (float g) (float b)
                     (float nx) (float ny) (float nz)))

(defn dist
  "Calculates the distance between two points"
  ([x1 y1 x2 y2] (PApplet/dist (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2] (PApplet/dist (float x1) (float y1) (float z1)
                               (float x2) (float y2) (float z2))))

(defn ellipse
  "Draws an ellipse (oval) in the display window. An ellipse with an
  equal width and height is a circle.  The origin may be changed with
  the ellipse-mode function"
  [x y width height]
  (.ellipse *applet* (float x) (float y) (float width) (float height)))

(def ^{:private true}
     ellipse-map   {:center CENTER
                    :radius RADIUS
                    :corner CORNER
                    :corners CORNERS})

(defn- resolve-ellipse-mode
  [mode]
  (if (keyword? mode)
    (get ellipse-map mode)
    mode))

(defn ellipse-mode
  "Modifies the origin of the ellispse according to the specified mode:

  :center  - specifies the location of the ellipse as
             the center of the shape. (Default).
  :radius  - similar to center, but the width and height parameters to
             ellipse specify the radius of the ellipse, rather than the
             diameter.
  :corner  - draws the shape from the upper-left corner of its bounding
             box.
  :corners - uses the four parameters to ellipse to set two opposing
             corners of the ellipse's bounding box."
  [mode]
  (let [mode (resolve-ellipse-mode mode)]
    (.ellipseMode *applet* (int mode))))

(defn emissive-float
  "Sets the emissive color of the material used for drawing shapes
 drawn to the screen. Used in combination with ambient, specular, and
 shininess in setting the material properties of shapes. Converts all
 args to floats"
  ([float-val] (.emissive *applet* (float float-val)))
  ([r g b] (.emissive *applet* (float r) (float g) (float b))))

(defn emissive-int
  "Sets the emissive color of the material used for drawing shapes
  drawn to the screen. Used in combination with ambient, specular, and
  shininess in setting the material properties of shapes. Converts all
  args to ints"
  [int-val] (.emissive *applet* (int int-val)))

(defn emissive
  "Sets the emissive color of the material used for drawing shapes
   drawn to the screen. Used in combination with ambient, specular,
   and shininess in setting the material properties of shapes.

  If passed one arg - it is assumed to be an int (i.e. a color),
   multiple args are converted to floats."
  ([c] (if (int-like? c) (emissive-int c) (emissive-float c)))
  ([r g b] (emissive-float r g b)))

(defn end-raw
  "Complement to begin-raw; they must always be used together. See
  the begin-raw docstring for details."
  []
  (.endRaw *applet*))

(defn end-shape
  "May only be called after begin-shape. When end-shape is called,
  all of image data defined since the previous call to begin-shape is
  written into the image buffer. The keyword :close may be passed to
  close the shape (to connect the beginning and the end)."
  ([] (.endShape *applet*))
  ([mode]
     (when-not (= :close mode)
       (throw (Exception. (str "Unknown mode value: " mode ". Expected :close"))))
     (.endShape *applet* CLOSE)))

(defn exit
  "Quits/stops/exits the program.  Rather than terminating
  immediately, exit will cause the sketch to exit after draw has
  completed (or after setup completes if called during the setup
  method). "
  []
  (.exit *applet*))

(defn exp
  "Returns Euler's number e (2.71828...) raised to the power of the
  value parameter."
  [val]
  (PApplet/exp (float val)))

(defn fill-float
  "Sets the color used to fill shapes. For example, (fill 204 102 0),
  will specify that all subsequent shapes will be filled with orange."
  ([gray] (.fill *applet* (float gray)))
  ([gray alpha] (.fill *applet* (float gray) (float alpha)))
  ([r g b] (.fill *applet* (float r) (float g) (float b)))
  ([r g b alpha] (.fill *applet* (float r) (float g) (float b) (float alpha))))

(defn fill-int
  "Sets the color used to fill shapes."
  ([rgb] (.fill *applet* (int rgb)))
  ([rgb alpha] (.fill *applet* (int rgb) (float alpha))))

(defn fill
  "Sets the color used to fill shapes."
  ([rgb] (if (int-like? rgb) (fill-int rgb) (fill-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (fill-int rgb alpha) (fill-float rgb alpha)))
  ([r g b] (fill-float r g b))
  ([r g b a] (fill-float r g b a)))

(def ^{:private true}
  filter-map {:threshold THRESHOLD
              :gray GRAY
              :invert INVERT
              :posterize POSTERIZE
              :blur BLUR
              :opaque OPAQUE
              :erode ERODE
              :dilate DILATE})

(defn- resolve-filter-mode
  [mode]
  (if (keyword? mode)
    (get filter-map mode)
    mode))

(defn display-filter
  "Originally named filter in Processing Language.
  Filters the display window with the specified mode and level. Level
  defines the quality of the filter and mode may be one of the
  following keywords:

  :threshold - converts the image to black and white pixels depending
               if they are above or below the threshold defined by
               the level parameter. The level must be between
               0.0 (black) and 1.0 (white). If no level is specified,
               0.5 is used.
  :gray      - converts any colors in the image to grayscale
               equivalents
  :invert    - sets each pixel to its inverse value
  :posterize - limits each channel of the image to the number of
               colors specified as the level parameter. The level
               parameter
  :blur      - executes a Guassian blur with the level parameter
               specifying the extent of the blurring. If no level
               parameter is used, the blur is equivalent to Guassian
               blur of radius 1.
  :opaque    - sets the alpha channel to entirely opaque.
  :erode     - reduces the light areas with the amount defined by the
               level parameter.
  :dilate    - increases the light areas with the amount defined by
               the level parameter."
  ([mode]
     (let [mode (resolve-filter-mode mode)]
       (.filter *applet* (int mode))))
  ([mode level]
     (let [mode (resolve-filter-mode mode)]
       (.filter *applet* (int mode) (float level)))))

(defn focused
  "Returns a boolean value representing whether the applet has focus."
  []
  (. *applet* :focused))

(defn frame-count
  "The system variable frameCount contains the number of frames
  displayed since the program started. Inside setup() the value is 0
  and and after the first iteration of draw it is 1, etc."
  []
  (.frameCount *applet*))

(defn frame-rate
  "With no args, returns the current framerate. With one arg specifies
  a new target framerate (number of frames to be displayed every
  second). If the processor is not fast enough to maintain the
  specified rate, it will not be achieved. For example, the function
  call (frame-rate 30) will attempt to refresh 30 times a second. It
  is recommended to set the frame rate within setup. The default rate
  is 60 frames per second."
  ([] (.frameRate *applet*))
  ([new-rate]
     (.frameRate *applet* (float new-rate))))

(defn frustum
  [l r b t near far]
  (.frustum *applet* (float l) (float r) (float b) (float t)
            (float near) (float far)))

(defn get-pixel
  ([] (.get *applet*))
  ([x y] (.get *applet* (int x) (int y)))
  ([x y w h] (.get *applet* (int x) (int y) (int w) (int h))))

(defn green
  "Extracts the green value from a color, scaled to match current
  color-mode. This value is always returned as a float so be careful
  not to assign it to an int value."
  [col]
  (.green *applet* (int col)))

(defn hex
  "Converts a byte, char, int, or color to a String containing the
  equivalent hexadecimal notation. For example color(0, 102, 153) will
  convert to the String \"FF006699\". This function can help make your
  geeky debugging sessions much happier. "
  ([val] (PApplet/hex val))
  ([val num-digits] (PApplet/hex (int val) (int num-digits))))

(defn unhex
  "Converts a String representation of a hexadecimal number to its
  equivalent integer value."
  [hex-str] (PApplet/unhex (str hex-str)))

(defn height
  "Height of the display window. The value of height is zero until
  size() is called."
  []
  (.getHeight *applet*))

(def ^{:private true}
  hint-options {:enable-opengl-4x-smooth PConstants/ENABLE_OPENGL_4X_SMOOTH
                :enable-opengl-2x-smooth PConstants/ENABLE_OPENGL_2X_SMOOTH
                :enable-native-fonts PConstants/ENABLE_NATIVE_FONTS
                :disable-native-fonts PConstants/DISABLE_NATIVE_FONTS
                :enable-depth-test PConstants/ENABLE_DEPTH_TEST
                :disable-depth-test PConstants/DISABLE_DEPTH_TEST
                :enable-depth-sort PConstants/ENABLE_DEPTH_SORT
                :disable-depth-sort PConstants/DISABLE_DEPTH_SORT
                :disable-opengl-error-report PConstants/DISABLE_OPENGL_ERROR_REPORT
                :enable-opengl-error-report PConstants/ENABLE_OPENGL_ERROR_REPORT
                :enable-accurate-textures PConstants/ENABLE_ACCURATE_TEXTURES
                :disable-accurate-textures PConstants/DISABLE_ACCURATE_TEXTURES
                :disable-depth-mask PConstants/DISABLE_DEPTH_MASK
                :enable-depth-mask PConstants/ENABLE_DEPTH_MASK})

(defn hint
  "Set various hints and hacks for the renderer. This is used to
  handle obscure rendering features that cannot be implemented in a
  consistent manner across renderers. Many options will often graduate
  to standard features instead of hints over time.

  Options:

  :enable-opengl-4x-smooth - Enable 4x anti-aliasing for OpenGL. This
     can help force anti-aliasing if it has not been enabled by the
     user. On some graphics cards, this can also be set by the
     graphics driver's control panel, however not all cards make this
     available. This hint must be called immediately after the size
     command because it resets the renderer, obliterating any settings
     and anything drawn (and like size(), re-running the code that
     came before it again).

  :disable-opengl-2x-smooth - In Processing 1.0, Processing always
    enables 2x smoothing when the OpenGL renderer is used. This hint
    disables the default 2x smoothing and returns the smoothing
    behavior found in earlier releases, where smooth and no-smooth
    could be used to enable and disable smoothing, though the quality
    was inferior.

  :enable-opengl-2x-smooth - Enables default OpenGL smoothing.

  :enable-native-fonts - Use the native version fonts when they are
    installed, rather than the bitmapped version from a .vlw
    file. This is useful with the default (or JAVA2D) renderer
    setting, as it will improve font rendering speed. This is not
    enabled by default, because it can be misleading while testing
    because the type will look great on your machine (because you have
    the font installed) but lousy on others' machines if the identical
    font is unavailable. This option can only be set per-sketch, and
    must be called before any use of text-font.

  :disable-native-fonts - Disables native font support.

  :disable-depth-test - Disable the zbuffer, allowing you to draw on
    top of everything at will. When depth testing is disabled, items
    will be drawn to the screen sequentially, like a painting. This
    hint is most often used to draw in 3D, then draw in 2D on top of
    it (for instance, to draw GUI controls in 2D on top of a 3D
    interface). Starting in release 0149, this will also clear the
    depth buffer. Restore the default with :enable-depth-test
    but note that with the depth buffer cleared, any 3D drawing that
    happens later in draw will ignore existing shapes on the screen.

  :enable-depth-test - Enables the zbuffer.

  :enable-depth-sort - Enable primitive z-sorting of triangles and
    lines in P3D and OPENGL. This can slow performance considerably,
    and the algorithm is not yet perfect.

  :disable-depth-sort - Disables hint :enable-depth-sort

  :disable-opengl-error-report - Speeds up the OPENGL renderer setting
     by not checking for errors while running.

  :enable-opengl-error-report - Turns on OpenGL error checking

  :enable-accurate-textures
  :disable-accurate-textures
  :enable-depth-mask
  :disable-depth-mask"
  [hint-type]
  (let [hint-type (if (keyword? hint-type)
                    (get hint-options hint-type)
                    hint-type)]
    (.hint *applet* (int hint-type))))

(defn hour
  "Returns the current hour as a value from 0 - 23."
  []
  (PApplet/hour))

(defn hue
  "Extracts the hue value from a color."
  [col]
  (.hue *applet* (int col)))

(defn image
  ([^PImage img x y] (.image *applet* img (float x) (float y)))
  ([^PImage img x y c d] (.image *applet* img (float x) (float y)
                                  (float c) (float d)))
  ([^PImage img x y c d u1 v1 u2 v2]
     (.image *applet* img (float x) (float y) (float c) (float d)
             (float u1) (float v1) (float u2) (float v2))))

(defn image-mode [mode] (.imageMode *applet* (int mode)))

(defn light-falloff
  "Sets the falloff rates for point lights, spot lights, and ambient
  lights. The parameters are used to determine the falloff with the
  following equation:

  d = distance from light position to vertex position
  falloff = 1 / (CONSTANT + d * LINEAR + (d*d) * QUADRATIC)

  Like fill, it affects only the elements which are created after it
  in the code. The default value is (light-falloff 1.0 0.0 0.0).
  Thinking about an ambient light with a falloff can be tricky. It is
  used, for example, if you wanted a region of your scene to be lit
  ambiently one color and another region to be lit ambiently by
  another color, you would use an ambient light with location and
  falloff. You can think of it as a point light that doesn't care
  which direction a surface is facing."
  [constant linear quadratic]
  (.lightFalloff *applet* (float constant) (float linear) (float quadratic)))

(defn lerp-color
  "Calculates a color or colors between two color at a specific
  increment. The amt parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc."
  [c1 c2 amt]
  (.lerpColor *applet* (int c1) (int c2) (float amt)))

(defn lerp
  "Calculates a number between two numbers at a specific
  increment. The amt parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc. The lerp function
  is convenient for creating motion along a straight path and for
  drawing dotted lines."
  [start stop amt]
  (PApplet/lerp (float start) (float stop) (float amt)))

(defn key-code
  "Returns current key's unique code."
  []
  (. *applet* :keyCode))

(defn lights [] (.lights *applet*))

(defn light-specular
  [x y z]
  (.lightSpecular *applet* (float x) (float y) (float z)))

(defn line
  "Draws a line (a direct path between two points) to the screen. The
  version of line with four parameters draws the line in 2D. To color
  a line, use the stroke function. A line cannot be filled, therefore
  the fill method will not affect the color of a line. 2D lines are
  drawn with a width of one pixel by default, but this can be changed
  with the stroke-weight function. The version with six parameters
  allows the line to be placed anywhere within XYZ space. "
  ([p1 p2] (apply line (concat p1 p2)))
  ([x1 y1 x2 y2] (.line *applet* (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2]
     (.line *applet* (float x1) (float y1) (float z1)
            (float x2) (float y2) (float z2))))

(defn load-bytes [filename] (PApplet/loadBytes filename))

(defn load-font [filename] (.loadFont *applet* filename))

(defn load-image [filename] (.loadImage *applet* filename))

(defn load-matrix [] (.loadMatrix *applet*))

(defn load-pixels [] (.loadPixels *applet*))

(defn load-shape
  "Load a geometry from a file as a PShape."
  [filename]
  (.loadShape *applet* filename))

(defn load-strings
  "Load data from a file and shove it into a String array."
  [filename]
  (.loadStrings *applet* filename))

(defn log
  "Calculates the natural logarithm (the base-e logarithm) of a
  number. This function expects the values greater than 0.0."
  [val]
  (PApplet/log (float val)))

(defn start-loop [] (.loop *applet*))

(defn mag
  "Calculates the magnitude (or length) of a vector. A vector is a
  direction in space commonly used in computer graphics and linear
  algebra. Because it has no start position, the magnitude of a vector
  can be thought of as the distance from coordinate (0,0) to its (x,y)
  value. Therefore, mag is a shortcut for writing (dist 0 0 x y)."
  ([a b] (PApplet/mag (float a) (float b)))
  ([a b c] (PApplet/mag (float a) (float b) (float c))))

(defn map-to [val istart istop ostart ostop]
  (PApplet/map (float val) (float istart) (float istop) (float ostart) (float ostop)))

(defn map-to-double [val istart istop ostart ostop]
  (PApplet/map (double val) (double istart) (double istop) (double ostart) (double ostop)))

(defn mask
  ([^ints alpha-array] (.mask *applet* alpha-array)))

(defn mask-image [^PImage img] (.mask *applet* img))

(defn millis
  "Returns the number of milliseconds (thousandths of a second) since
  starting an applet. This information is often used for timing
  animation sequences."
  []
  (.millis *applet*))

(defn minute
  "Returns the current minute as a value from 0 - 59"
  []
  (PApplet/minute))

(defn model-x [x y z] (.modelX *applet* (float x) (float y) (float z)))
(defn model-y [x y z] (.modelY *applet* (float x) (float y) (float z)))
(defn model-z [x y z] (.modelZ *applet* (float x) (float y) (float z)))

(defn month
  "Returns the current month as a value from 1 - 12."
  []
  (PApplet/month))

(defn mouse-x
  "Current horizontal coordinate of the mouse."
  []
  (. *applet* :mouseX))

(defn mouse-y
  "Current vertical coordinate of the mouse."
  []
  (. *applet* :mouseY))

(defn no-cursor [] (.noCursor *applet*))

(defn no-fill
 "Disables filling geometry. If both no-stroke and no-fill are called,
  nothing will be drawn to the screen."  []
 (.noFill *applet*))

(defn noise
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
  ([x] (.noise *applet* (float x)))
  ([x y] (.noise *applet* (float x) (float y)))
  ([x y z] (.noise *applet* (float x) (float y) (float z))))

(defn noise-detail
  ([int detail] (.noiseDetail *applet* (int detail)))
  ([int detail falloff] (.noiseDetail *applet* (int detail) (float falloff))))

(defn noise-seed [what] (.noiseSeed *applet* (int what)))

(defn no-lights [] (.noLights *applet*))

(defn no-loop [] (.noLoop *applet*))

(defn norm
  "Normalize a value to exist between 0 and 1 (inclusive)."
  [val start stop]
  (PApplet/norm (float val) (float start) (float stop)))

(defn normal [nx ny nz] (.normal *applet* (float nx) (float ny) (float nz)))

(defn no-smooth
  "Draws all geometry with jagged (aliased) edges."
  [] (.noSmooth *applet*))

(defn no-stroke
  "Disables drawing the stroke (outline). If both no-stroke and
  no-fill are called, nothing will be drawn to the screen."
  []
  (.noStroke *applet*))

(defn no-tint [] (.noTint *applet*))

(defn open [^String filename] (PApplet/open filename))

;; $$open -- overload

(defn ortho
  ([] (.ortho *applet*))
  ([l r b t near far] (.ortho *applet* (float l) (float r) (float b)
                              (float t) (float near) (float far))))

(defn perspective
  ([] (.perspective *applet*))
  ([fovy aspect z-near z-far]
     (.perspective *applet* (float fovy) (float aspect)
                   (float z-near) (float z-far))))

(defn point
  "Draws a point, a coordinate in space at the dimension of one
  pixel. The first parameter is the horizontal value for the point,
  the second value is the vertical value for the point, and the
  optional third value is the depth value. Drawing this shape in 3D
  using the z parameter requires the P3D or OPENGL parameter in
  combination with size as shown in the above example."
  ([x y] (.point *applet* (float x)(float y)))
  ([x y z] (.point *applet* (float x) (float y) (float z))))

(defn point-light
  "Adds a point light. Lights need to be included in the draw() to
  remain persistent in a looping program. Placing them in the setup()
  of a looping program will cause them to only have an effect the
  first time through the loop. The affect of the r, g, and b
  parameters is determined by the current color mode. The x, y, and z
  parameters set the position of the light"
  [r g b x y z]
  (.pointLight *applet* (float r) (float g) (float b) (float x) (float y) (float z)))

(defn pop-matrix
  "Pops the current transformation matrix off the matrix
  stack. Understanding pushing and popping requires understanding the
  concept of a matrix stack. The push-matrix fn saves the current
  coordinate system to the stack and pop-matrix restores the prior
  coordinate system. push-matrix and pop-matrix are used in conjuction
  with the other transformation methods and may be embedded to control
  the scope of the transformations."
  []
  (.popMatrix *applet*))

(defn pow
  "Facilitates exponential expressions. The pow() function is an
  efficient way of multiplying numbers by themselves (or their
  reciprocal) in large quantities. For example, (pow 3 5) is
  equivalent to the expression (* 3 3 3 3 3) and (pow 3 -5) is
  equivalent to (/ 1 (* 3 3 3 3 3))."
  [num exponent]
  (PApplet/pow (float num) (float exponent)))

(defn print-camera
  "Prints the current camera matrix to std out. Useful for debugging."
  []
  (.printCamera *applet*))

(defn print-matrix
  "Prints the current matrix to std out. Useful for debugging."
  []
  (.printMatrix *applet*))

(defn print-projection
  "Prints the current projection matrix to std out. Useful for
  debugging"
  []
  (.printProjection *applet*))

(defn push-matrix
  "Pushes the current transformation matrix onto the matrix
  stack. Understanding push-matrix and pop-matrix requires
  understanding the concept of a matrix stack. The push-matrix
  function saves the current coordinate system to the stack and
  pop-matrix restores the prior coordinate system. push-matrix and
  pop-matrix are used in conjuction with the other transformation
  methods and may be embedded to control the scope of the
  transformations."
  []
  (.pushMatrix *applet*))

(defn quad
  "A quad is a quadrilateral, a four sided polygon. It is similar to a
  rectangle, but the angles between its edges are not constrained to
  be ninety degrees. The first pair of parameters (x1,y1) sets the
  first vertex and the subsequent pairs should proceed clockwise or
  counter-clockwise around the defined shape."
  [x1 y1 x2 y2 x3 y3 x4 y4]
  (.quad *applet*
         (float x1) (float y1)
         (float x2) (float y2)
         (float x3) (float y3)
         (float x4) (float y4)))

(defn radians
  "Converts a degree measurement to its corresponding value in
  radians. Radians and degrees are two ways of measuring the same
  thing. There are 360 degrees in a circle and 2*PI radians in a
  circle. For example, 90° = PI/2 = 1.5707964. All trigonometric
  methods in Processing require their parameters to be specified in
  radians."
  [degrees]
  (PApplet/radians (float degrees)))

(defn random
  "Generates random numbers. Each time the random function is called,
  it returns an unexpected value within the specified range. If one
  parameter is passed to the function it will return a float between
  zero and the value of the high parameter. The function call (random
  5) returns values between 0 and 5 (starting at zero, up to but not
  including 5). If two parameters are passed, it will return a float
  with a value between the the parameters. The function call
  (random -5 10.2) returns values starting at -5 up to (but not
  including) 10.2."
  ([max] (.random *applet* (float max)))
  ([min max] (.random *applet* (float min) (float max))))


(defn random-seed [w] (.randomSeed *applet* (float w)))

(defn rect
  "Draws a rectangle to the screen. A rectangle is a four-sided shape
   with every angle at ninety degrees. By default, the first two
   parameters set the location of the upper-left corner, the third
   sets the width, and the fourth sets the height. These parameters
   may be changed with rect-mode."
  [x y width height]
  (.rect *applet* (float x) (float y) (float width) (float height)))

(defn rect-mode [mode] (.rectMode *applet* (int mode)))

(defn red [what] (.red *applet* (int what)))

(defn redraw [] (.redraw *applet*))

(defn request-image
  ([filename] (.requestImage *applet* filename))
  ([filename extension] (.requestImage *applet* filename extension)))

(defn reset-matrix [] (.resetMatrix *applet*))

(defn reverse-array [arr] (PApplet/reverse arr))

(defn rotate
  "Rotates a shape the amount specified by the angle parameter. Angles
  should be specified in radians (values from 0 to TWO-PI) or
  converted to radians with the radians function.

  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a clockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling (rotate HALF-PI) and then (rotate HALF-PI) is the
  same as (rotate PI). All tranformations are reset when draw begins
  again.

  Technically, rotate multiplies the current transformation matrix by
  a rotation matrix. This function can be further controlled by the
  push-matrix and pop-matrix."
  ([angle] (.rotate *applet* (float angle)))
  ([angle vx vy vz] (.rotate *applet* (float angle)
                             (float vx) (float vy) (float vz))))

(defn rotate-x
  "Rotates a shape around the x-axis the amount specified by the angle
  parameter. Angles should be specified in radians (values from 0 to
  (* PI 2)) or converted to radians with the radians function. Objects
  are always rotated around their relative position to the origin and
  positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling (rotate-x HALF-PI) and then (rotate-x HALF-PI) is
  the same as (rotate-x PI). If rotate-x is called within the draw fn,
  the transformation is reset when the loop begins again. This
  function requires passing P3D or OPENGL into the size parameter."
  [angle]
  (.rotateX *applet* (float angle)))

(defn rotate-y
  "Rotates a shape around the y-axis the amount specified by the angle
  parameter. Angles should be specified in radians (values from 0
  to (* PI 2)) or converted to radians with the radians function.
  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling (rotate-y HALF-PI) and then (rotate-y HALF-PI) is
  the same as (rotate-y PI). If rotate-y is called within the draw fn,
  the transformation is reset when the loop begins again. This
  function requires passing P3D or OPENGL into the size parameter."
  [angle]
  (.rotateY *applet* (float angle)))

(defn rotate-z
  "Rotates a shape around the z-axis the amount specified by the angle
  parameter. Angles should be specified in radians (values from 0
  to (* PI 2)) or converted to radians with the radians function.
  Objects are always rotated around their relative position to the
  origin and positive numbers rotate objects in a counterclockwise
  direction. Transformations apply to everything that happens after
  and subsequent calls to the function accumulates the effect. For
  example, calling (rotate-z HALF-PI) and then (rotate-z HALF-PI) is
  the same as (rotate-z PI). If rotate-y is called within the draw fn,
  the transformation is reset when the loop begins again. This
  function requires passing P3D or OPENGL into the size parameter."
  [angle]
  (.rotateZ *applet* (float angle)))

(defn round [what] (PApplet/round (float what)))

(defn saturation [what] (.saturation *applet* (int what)))

(defn save [filename] (.save *applet* filename))

;; $$saveBytes
;; $$saveFile

(defn save-frame
  "Saves an image identical to the current display window as a
  file. May be called multple times - each file saved will have a
  unique name. Name and image formate may be modified by passing a
  string parameter of the form \"foo-####.ext\" where foo- can be any
  arbitrary string, #### will be replaced with the current frame id
  and .ext is one of .tiff, .targa, .png, .jpeg or .jpg

  Examples:
  (save-frame)
  (save-frame \"pretty-pic-####.jpg\")"
  ([] (.saveFrame *applet*))
  ([name] (.saveFrame *applet* (str name))))

;; $$savePath
;; $$saveStream
;; $$saveStrings

(defn scale
  "Increases or decreases the size of a shape by expanding and
  contracting vertices. Objects always scale from their relative
  origin to the coordinate system. Scale values are specified as
  decimal percentages. For example, the function call (scale 2)
  increases the dimension of a shape by 200%. Transformations apply to
  everything that happens after and subsequent calls to the function
  multiply the effect. For example, calling (scale 2) and then
  (scale 1.5) is the same as (scale 3). If scale is called within
  draw, the transformation is reset when the loop begins again. Using
  this fuction with the z parameter requires passing P3D or OPENGL
  into the size parameter as shown in the example above. This function
  can be further controlled by push-matrix and pop-matrix."
  ([s] (.scale *applet* (float s)))
  ([sx sy] (.scale *applet* (float sx) (float sy))))

(defn screen-x
  "Takes a three-dimensional x, y, z position and returns the x value
  for where it will appear on a (two-dimensional) screen, once
  affected by translate, scale or any other transformations"
  [x y z]
  (.screenX *applet* (float x) (float y) (float z)))

(defn screen-y
  "Takes a three-dimensional x, y, z position and returns the y value
  for where it will appear on a (two-dimensional) screen, once
  affected by translate, scale or any other transformations"
  [x y z]
  (.screenY *applet* (float x) (float y) (float z)))

(defn screen-z
  "Given an x, y, z coordinate, returns its z value.
   This value can be used to determine if an x, y, z coordinate is in
   front or in back of another (x, y, z) coordinate. The units are
   based on how the zbuffer is set up, and don't relate to anything
   'real'. They're only useful for in comparison to another value
   obtained from screen-z, or directly out of the zbuffer"
  [x y z]
  (.screenX *applet* (float x) (float y) (float z)))

(defn seconds
  "Returns the current second as a value from 0 - 59."
  []
  (PApplet/second))

;; $$selectFolder
;; $$selectInput
;; $$selectOutput

(defn set-pixel
  [x y c] (.set *applet* (int x) (int y) (int c)))

(defn set-image-at
  [dx dy ^PImage src] (.set *applet* (int dx) (int dy) src))

(defn shininess [shine] (.shininess *applet* (float shine)))

(defn sin
  "Calculates the sine of an angle. This function expects the values
  of the angle parameter to be provided in radians (values from 0 to
  6.28). A float within the range -1 to 1 is returned."
  [angle]
  (PApplet/sin (float angle)))

(defn size
  "Not supported. Use :size key in applet or defapplet"
  [& args]
  (println "Deprecated - size should be specified as a :size key to applet or defapplet")
  nil)

;; $$sketchFile
;; $$sketchPath

(defn smooth
  "Draws all geometry with smooth (anti-aliased) edges. This will slow
  down the frame rate of the application, but will enhance the visual
  refinement.

  Note that smooth will also improve image quality of resized images."
  []
  (.smooth *applet*))

(defn specular
  ([gray] (.specular *applet* (float gray)))
  ([gray alpha] (.specular *applet* (float gray) (float alpha)))
  ([x y z] (.specular *applet* (float x) (float y) (float z)))
  ([x y z a] (.specular *applet* (float x) (float y) (float z) (float a))))

(defn sphere
  "Genarates a hollow ball made from tessellated triangles."
  [radius] (.sphere *applet* (float radius)))

(defn sphere-detail
  "Controls the detail used to render a sphere by adjusting the number
  of vertices of the sphere mesh. The default resolution is 30, which
  creates a fairly detailed sphere definition with vertices every
  360/30 = 12 degrees. If you're going to render a great number of
  spheres per frame, it is advised to reduce the level of detail using
  this function. The setting stays active until sphere-detail is
  called again with a new parameter and so should not be called prior
  to every sphere statement, unless you wish to render spheres with
  different settings, e.g. using less detail for smaller spheres or
  ones further away from the camera. To controla the detail of the
  horizontal and vertical resolution independently, use the version of
  the functions with two parameters."
  ([res] (.sphereDetail *applet* (int res)))
  ([ures vres] (.sphereDetail *applet* (int ures) (int vres))))

(defn spotlight
  ([r g b x y z nx ny nz angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration))
  ([[r g b] [x y z] [nx ny nz] angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration)))

(defn sq
  "Squares a number (multiplies a number by itself). The result is
  always a positive number, as multiplying two negative numbers always
  yields a positive result. For example, -1 * -1 = 1."
  [a]
  (PApplet/sq (float a)))

(defn sqrt
  "Calculates the square root of a number. The square root of a number
  is always positive, even though there may be a valid negative
  root. The square root s of number a is such that (= a (* s s)) . It
  is the opposite of squaring."
  [a]
  (PApplet/sqrt (float a)))

(def ^{:private true}
  stroke-cap-map {:square SQUARE
                  :round ROUND
                  :project PROJECT
                  :model MODEL})

(defn- resolve-stroke-cap-mode
  [mode]
  (if (keyword? mode)
    (get stroke-cap-map mode)
    mode))

(defn stroke-float
  "Sets the color used to draw lines and borders around
  shapes. Converts all args to floats"
  ([gray] (.stroke *applet* (float gray)))
  ([gray alpha] (.stroke *applet* (float gray) (float alpha)))
  ([x y z] (.stroke *applet* (float x) (float y) (float z)))
  ([x y z a] (.stroke *applet* (float x) (float y) (float z) (float a))))

(defn stroke-int
  "Sets the color used to draw lines and borders around
  shapes. Converts rgb to int and alpha to a float."
  ([rgb] (.stroke *applet* (int rgb)))
  ([rgb alpha] (.stroke *applet* (int rgb) (float alpha))))

(defn stroke
  ([rgb] (if (int-like? rgb) (stroke-int rgb) (stroke-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (stroke-int rgb alpha) (stroke-float rgb alpha)))
  ([x y z] (stroke-float x y z))
  ([x y z a] (stroke-float x y z a)))

(defn stroke-cap
  "Sets the style for rendering line endings. These ends are either
  squared, extended, or rounded and specified with the corresponding
  parameters :square, :project, and :round. The default cap is :round."
  [cap-mode]
  (let [cap-mode (resolve-stroke-cap-mode cap-mode)]
    (.strokeCap *applet* (int cap-mode))))

(def ^{:private true}
  stroke-join-modes {:miter PConstants/MITER
                     :bevel PConstants/BEVEL
                     :round PConstants/ROUND})

(defn- resolve-stroke-join-mode
  [mode]
  (if (keyword? mode)
    (get stroke-join-modes mode)
    mode))

(defn stroke-join
  "Sets the style of the joints which connect line
  segments. These joints are either mitered, beveled, or rounded and
  specified with the corresponding parameters :miter, :bevel, and
  :round. The default joint is :miter.

  This function is not available with the P2D, P3D, or OPENGL
  renderers (see bug report). More information about the renderers can
  be found in the size reference."
  [join-mode]
  (let [join-mode (resolve-stroke-join-mode join-mode)]
    (.strokeJoin *applet* (int join-mode))))

(defn stroke-weight
  "Sets the width of the stroke used for lines, points, and the border
  around shapes. All widths are set in units of pixels. "
  [weight]
  (.strokeWeight *applet* (float weight)))

(defn tan
  "Calculates the ratio of the sine and cosine of an angle. This
  function expects the values of the angle parameter to be provided in
  radians (values from 0 to PI*2). Values are returned in the range
  infinity to -infinity."
  [angle]
  (PApplet/tan (float angle)))

(defn char->text
  ([c] (.text *applet* (char c)))
  ([c x y] (.text *applet* (char c) (float x) (float y)))
  ([c x y z] (.text *applet* (char c) (float x) (float y) (float z))))

(defn num->text
  ([num x y] (.text *applet* (float num) (float x) (float y)))
  ([num x y z] (.text *applet* (float num) (float x) (float y) (float z))))

(defn string->text
  ([^String s] (.text *applet* s))
  ([^String s x y] (.text *applet* s (float x) (float y)))
  ([^String s x y z] (.text *applet* s (float x) (float y) (float z))))

(defn string->text-in
  ([^String s x1 y1 x2 y2]
     (.text *applet* s (float x1) (float y1) (float x2) (float y2)))
  ([^String s x1 y1 x2 y2 z]
     (.text *applet* s (float x1) (float y1) (float x2) (float y2) (float z))))

(defn text-align
  ([align] (.textAlign *applet* (int align)))
  ([align-x align-y] (.textAlign *applet* (int align-x) (int align-y))))

(defn text-ascent [] (.textAscent *applet*))

(defn text-descend [] (.textDescent *applet*))

(defn text-font
  ([^PFont which] (.textFont *applet* which))
  ([^PFont which size] (.textFont *applet* which (int size))))

(defn text-leading [leading] (.textLeading *applet* (float leading)))

(defn text-mode [mode] (.textMode *applet* (int mode)))

(defn text-size [size] (.textSize *applet* (float size)))

(defn texture [^PImage img] (.texture *applet* img))

(defn texture-mode [mode] (.textureMode *applet* (int mode)))

(defmulti text-width #(= (class %) (class \a)))

(defmethod text-width true
  [c] (.textWidth *applet* (char c)))

(defmethod text-width false
  [^String s] (.textWidth *applet* s))

(defn tint-float
  "Sets the fill value for displaying images. Images can be tinted to
  specified colors or made transparent by setting the alpha.

  To make an image transparent, but not change it's color, use white
  as the tint color and specify an alpha value. For instance,
  tint(255, 128) will make an image 50% transparent (unless
  colorMode() has been used).

  The value for the parameter gray must be less than or equal to the
  current maximum value as specified by colorMode(). The default
  maximum value is 255.

  Also used to control the coloring of textures in 3D."
  ([gray] (.tint *applet* (float gray)))
  ([gray alpha] (.tint *applet* (float gray) (float alpha)))
  ([r g b] (.tint *applet* (float r)(float g) (float b)))
  ([r g b a] (.tint *applet* (float g) (float g) (float b) (float a))))

(defn tint-int
  "Sets the fill value for displaying images. Images can be tinted to
  specified colors or made transparent by setting the alpha.

  To make an image transparent, but not change it's color, use white
  as the tint color and specify an alpha value. For instance,
  tint(255, 128) will make an image 50% transparent (unless
  colorMode() has been used).

  The value for the parameter gray must be less than or equal to the
  current maximum value as specified by colorMode(). The default
  maximum value is 255.

  Also used to control the coloring of textures in 3D."
  ([rgb] (.tint *applet* (int rgb)))
  ([rgb alpha] (.tint *applet* (int rgb) (float alpha))))

(defn tint
  "Sets the fill value for displaying images. Images can be tinted to
  specified colors or made transparent by setting the alpha.

  To make an image transparent, but not change it's color, use white
  as the tint color and specify an alpha value. For instance,
  tint(255, 128) will make an image 50% transparent (unless
  colorMode() has been used).

  The value for the parameter gray must be less than or equal to the
  current maximum value as specified by colorMode(). The default
  maximum value is 255.

  Also used to control the coloring of textures in 3D."
  ([rgb] (if (int-like? rgb) (tint-int rgb) (tint-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (tint-int rgb alpha) (tint-float rgb alpha)))
  ([r g b] (tint-float r g b))
  ([r g b a] (tint-float r g b a)))

(defn translate
  "Specifies an amount to displace objects within the display
  window. The x parameter specifies left/right translation, the y
  parameter specifies up/down translation, and the z parameter
  specifies translations toward/away from the screen.  Transformations
  apply to everything that happens after and subsequent calls to the
  function accumulates the effect. For example, calling (translate 50
  0) and then (translate 20,h 0) is the same as (translate 70, 0). If
  translate is called within draw, the transformation is reset when
  the loop begins again. This function can be further controlled by
  the push-matrix and pop-matrix."
  ([v] (apply translate v))
  ([tx ty] (.translate *applet* (float tx) (float ty)))
  ([tx ty tz] (.translate *applet* (float tx) (float ty) (float tz))))

(defn triangle
  "A triangle is a plane created by connecting three points. The first
  two arguments specify the first point, the middle two arguments
  specify the second point, and the last two arguments specify the
  third point."
  [x1 y1 x2 y2 x3 y3]
  (.triangle *applet*
             (float x1) (float y1)
             (float x2) (float y2)
             (float x3) (float y3)))


(defn update-pixels
  "Updates the display window with the data in the pixels array. Use
  in conjunction with load-pixels. If you're only reading pixels from
  the array, there's no need to call update-pixels unless there are
  changes.

  Certain renderers may or may not seem to require load-pixels or
  update-pixels. However, the rule is that any time you want to
  manipulate the pixels array, you must first call load-pixels, and
  after changes have been made, call update-pixels. Even if the
  renderer may not seem to use this function in the current Processing
  release, this will always be subject to change."
  []
  (.updatePixels *applet*))

(defn vertex
  "All shapes are constructed by connecting a series of
  vertices. vertex is used to specify the vertex coordinates for
  points, lines, triangles, quads, and polygons and is used
  exclusively within the begin-shape and end-shape fns.

  Drawing a vertex in 3D using the z parameter requires the P3D or
  OPENGL parameter in combination with size as shown in the above
  example.

  This function is also used to map a texture onto the geometry. The
  texture fn declares the texture to apply to the geometry and the u
  and v coordinates set define the mapping of this texture to the
  form. By default, the coordinates used for u and v are specified in
  relation to the image's size in pixels, but this relation can be
  changed with texture-mode."
  ([x y] (.vertex *applet* (float x) (float y)))
  ([x y z] (.vertex *applet* (float x) (float y) (float z)))
  ([x y u v] (.vertex *applet* (float x) (float y) (float u) (float v)))
  ([x y z u v]
     (.vertex *applet* (float x) (float y) (float z) (float u) (float v))))

(defn year
  "Returns the current year as an integer (2003, 2004, 2005, etc)."
  []
  (PApplet/year))

;; utility functions. clj-processing specific

(defn width
  "Width of the display window. The value of width is zero until size is
  called."
  []
  (.getWidth *applet*))

(defmacro with-translation
  "Berforms body with translation, restores current transformation on
  exit."
  [translation-vector & body]
  `(let [tr# ~translation-vector]
     (push-matrix)
     (translate tr#)
     ~@body
     (pop-matrix)))

(defmacro with-rotation
  "Performs body with rotation, restores current transformation on exit.
  Accepts a vector [angle] or [angle x-axis y-axis z-axis].

  Example:
    (with-rotation [angle]
      (vertex 1 2))"
  [rotation & body]
  `(let [tr# ~rotation]
     (push-matrix)
     (apply rotate tr#)
     ~@body
     (pop-matrix)))

;;; version number

(let [version-stream (.getResourceAsStream (clojure.lang.RT/baseLoader)
                                           "processing/core/version.properties")
      properties     (doto (new java.util.Properties) (.load version-stream))
      prop (fn [k] (.getProperty properties (str "processing.core.version." k)))
      processing-version {:major       (Integer/valueOf ^String (prop "major"))
                          :minor       (Integer/valueOf ^String (prop "minor"))
                          :incremental (Integer/valueOf ^String (prop "incremental"))
                          :qualifier   (prop "qualifier")}]
  (def ^{:dynamic true}
    *processing-version*
    (if (not (= (prop "interim") "false"))
      (clojure.lang.RT/assoc processing-version :interim true)
      processing-version)))

(alter-meta! (var *processing-version*) assoc :doc
  "The version info for clj-processing, as a map containing :major :minor
  :incremental and optional :qualifier keys. This version number
   corresponds to the official Processing.org version with which
   processing.core is compatible.")

(defn processing-version
  "Returns clj-processing version as a printable string."
  []
  (str (:major *processing-version*)
       "."
       (:minor *processing-version*)
       (when-let [i (:incremental *processing-version*)]
         (str "." i))
       (when-let [q (:qualifier *processing-version*)]
         (when (pos? (count q)) (str "-" q)))
       (when (:interim *processing-version*)
         "-SNAPSHOT")))
