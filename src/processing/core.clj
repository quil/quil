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
  (:import [processing.core PApplet PImage PGraphics PFont PConstants PShape])
  (:use [processing.constants]
        [processing.util :only [int-like? resolve-constant-key length-of-longest-key gen-padding print-definition-list]]))

;; used by functions in this lib. Use binding to set it
;; to an instance of processing.core.PApplet
(def ^{:dynamic true} ^PApplet *applet*)
(def ^{:dynamic true} *state*)

(defn
  ^{:requires-bindings true
    :added "1.0"}
  state
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

(defn
  ^{:requires-bindings true
    :added "1.0"}
  set-state!
  "Set canvas-specific state. May only be called once (ideally in the
  setup fn).  Subsequent calls have no effect.

  Example:
  (set-state! :foo 1 :bar (atom true) :baz (/ (width) 2))"
  [& state-vals]
  (when-not @*state*
    (let [state-map (apply hash-map state-vals)]
      (reset! *state* state-map))))

(defn
  ^{:requires-bindings false
    :processing-name "abs()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  abs-int
  "Calculates the absolute value (magnitude) of a number. The absolute
  value of a number is always positive. Takes and returns an int."
  [n]
  (PApplet/abs (int n)))

(defn
  ^{:requires-bindings false
    :processing-name "abs()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  abs-float
  "Calculates the absolute value (magnitude) of a number. The absolute
  value of a number is always positive. Takes and returns a float."
  [n]
  (PApplet/abs (float n)))

(defn
  ^{:requires-bindings false
    :processing-name "abs()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  abs
  "Calculates the absolute value (magnitude) of a number. The
  absolute value of a number is always positive. Dynamically casts to
  an int or float appropriately"
    [n]
    (if (int-like? n)
      (abs-int n)
      (abs-float n)))

(defn
  ^{:requires-bindings false
    :processing-name "acos()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  acos
  "The inverse of cos, returns the arc cosine of a value. This
  function expects the values in the range of -1 to 1 and values are
  returned in the range 0 to Math/PI (3.1415927)."
  [n]
  (PApplet/acos (float n)))

(defn
  ^{:requires-bindings true
    :processing-name "alpha()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  alpha
  "Extracts the alpha value from a color."
  [color]
  (.alpha *applet* (int color)))

(defn
  ^{:requires-bindings true
    :processing-name "ambient()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  ambient-float
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The
  color components set through the parameters define the
  reflectance. For example in the default color mode, setting x=255,
  y=126, z=0, would cause all the red light to reflect and half of the
  green light to reflect. Used in combination with emissive, specular,
  and shininess in setting the material properties of shapes."
  ([gray] (.ambient *applet* (float gray)))
  ([x y z] (.ambient *applet* (float x) (float y) (float z))))

(defn
  ^{:requires-bindings true
    :processing-name "ambient()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  ambient-int
  "Sets the ambient reflectance for shapes drawn to the screen. This
  is combined with the ambient light component of environment. The rgb
  color components set define the reflectance. Used in combination
  with emissive, specular, and shininess in setting the material
  properties of shapes."
  [rgb]
  (.ambient
   *applet* (int rgb)))

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
  reflectance. For example in the default color mode, setting x=255,
  y=126, z=0, would cause all the red light to reflect and half of the
  green light to reflect. Used in combination with emissive, specular,
  and shininess in setting the material properties of shapes."
  ([rgb] (if (int-like? rgb) (ambient-int rgb) (ambient-float rgb)))
  ([x y z] (ambient-float x y z)))

(defn
  ^{:requires-bindings true
    :processing-name "ambientLight()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  ambient-light
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

(defn
  ^{:requires-bindings true
    :processing-name "arc()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  arc
  "Draws an arc in the display window. Arcs are drawn along the outer
  edge of an ellipse defined by the x, y, width and height
  parameters. The origin or the arc's ellipse may be changed with the
  ellipseMode() function. The start and stop parameters specify the
  angles at which to draw the arc."
  [x y width height start stop]
  (.arc *applet* (float x)(float y) (float width) (float height)
        (float start) (float stop)))

(defn
  ^{:requires-bindings false
    :processing-name "asin()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  asin
  "The inverse of sin, returns the arc sine of a value. This function
  expects the values in the range of -1 to 1 and values are returned
  in the range -PI/2 to PI/2."
  [n]
  (PApplet/asin (float n)))

(defn
  ^{:requires-bindings false
    :processing-name "atan()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  atan
  "The inverse of tan, returns the arc tangent of a value. This
  function expects the values in the range of -Infinity to
  Infinity (exclusive) and values are returned in the range -PI/2 to
  PI/2 ."
  [n]
  (PApplet/atan (float n)))

(defn
  ^{:requires-bindings false
    :processing-name "atan2()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  atan2
  "Calculates the angle (in radians) from a specified point to the
  coordinate origin as measured from the positive x-axis. Values are
  returned as a float in the range from PI to -PI. The atan2 function
  is most often used for orienting geometry to the position of the
  cursor. Note: The y-coordinate of the point is the first parameter
  and the x-coordinate is the second due the the structure of
  calculating the tangent."
  [y x]
  (PApplet/atan2 (float y) (float x)))

(defn
  ^{:requires-bindings true
    :processing-name "background()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  background-float
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

(defn
  ^{:requires-bindings true
    :processing-name "background()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  background-int
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  create-graphics. Converts rgb to an int and alpha to a float."
  ([rgb] (.background *applet* (int rgb)))
  ([rgb alpha] (.background *applet* (int rgb) (float alpha))))

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
  create-graphics. Converts args to floats."
  ([rgb] (if (int-like? rgb) (background-int rgb) (background-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (background-int rgb alpha) (background-float rgb alpha)))
  ([r g b] (background-float r g b))
  ([r g b a] (background-float r g b a)))

(defn
  ^{:requires-bindings true
    :processing-name "background()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  background-image
  "Specify an image to be used as the background for a sketch. Its
  width and height must be the same size as the sketch window. Images
  used as background will ignore the current tint setting."
  [^PImage img]
  (.background *applet* img))

(defn
  ^{:requires-bindings true
    :processing-name "beginCamera()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  begin-camera
  "Sets the matrix mode to the camera matrix so calls such as
  translate, rotate, apply-matrix and reset-matrix affect the
  camera. begin-camera should always be used with a following
  end-camera and pairs of begin-camera and end-camera cannot be
  nested.

  For most situations the camera function will be sufficient."
  []
  (.beginCamera *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "endCamera()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  end-camera
  "Unsets the matrix mode from the camera matrix. See begin-camera."
  []
  (.endCamera *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "beginRaw()"
    :category "Output"
    :subcategory "Files"
    :added "1.0"}
  begin-raw
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


(defn
  ^{:requires-bindings true
    :processing-name "beginRecord()"
    :category "Output"
    :subcategory "Files"
    :added "1.0"}
  begin-record
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
  (let [renderer (resolve-constant-key renderer render-modes)]
    (println "renderer: " renderer)
    (.beginRecord *applet* (str renderer) (str filename))))

(defn
  ^{:requires-bindings true
    :processing-name "endRecord()"
    :category "Output"
    :subcategory "Files"
    :added "1.0"}
  end-record
  "Stops the recording process started by begin-record and closes the
  file."
  []
  (.endRecord *applet*))

(def ^{:private true}
     shape-modes {:points POINTS
                  :lines LINES
                  :triangles TRIANGLES
                  :triangle-strip TRIANGLE-STRIP
                  :triangle-fan TRIANGLE-FAN
                  :quads QUADS
                  :quad-strip QUAD-STRIP})

(defn
  ^{:requires-bindings true
    :processing-name "beginShape()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  begin-shape
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
     (let [mode (resolve-constant-key mode shape-modes)]
       (.beginShape *applet* (int mode)))))

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

(defn
  ^{:requires-bindings true
    :processing-name "bezierDetail()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier-detail
  "Sets the resolution at which Beziers display. The default value is
  20. This function is only useful when using the P3D or OPENGL
  renderer as the default (JAVA2D) renderer does not use this
  information."
  [detail]
  (.bezierDetail *applet* (int detail)))

(defn
  ^{:requires-bindings true
    :processing-name "bezierPoint()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  bezier-point
  "Evaluates the Bezier at point t for points a, b, c, d. The
  parameter t varies between 0 and 1, a and d are points on the curve,
  and b and c are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a bezier curve at t."
  [a b c d t]
  (.bezierPoint *applet* (float a) (float b) (float c)
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
  (.bezierTangent *applet* (float a) (float b) (float c)
                  (float d) (float t)))

(defn
  ^{:requires-bindings true
    :processing-name "bezierVertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  bezier-vertex
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

(defn
  ^{:require-binding false
    :processing-name "binary()"
    :category "Data"
    :subcategory "Conversion"
    :added "1.0"}
  binary
  "Returns a string representing the binary value of an int, char or
  byte. When converting an int to a string, it is possible to specify
  the number of digits used."
  ([val] (PApplet/binary (if (number? val) (int val) val)))
  ([val num-digits] (PApplet/binary (int val) (int num-digits))))

(defn
  ^{:require-binding false
    :processing-name "unbinary()"
    :category "Data"
    :subcategory "Conversion"
    :added "1.0"}
    unbinary
  "Unpack a binary string to an integer. See binary for converting
  integers to strings."
  [str-val]
  (PApplet/unbinary (str str-val)))

(def ^{:private true}
  blend-modes {:blend BLEND
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

(defn
  ^{:requires-bindings true
    :processing-name "blend()"
    :category "Color"
    :subcategory "Pixels"
    :added "1.0"}
  blend
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
     (let [mode (resolve-constant-key mode blend-modes)]
       (.blend *applet* (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode))))
  ([^PImage src x y width height dx dy dwidth dheight mode]
     (let [mode (resolve-constant-key mode blend-modes)]
       (.blend *applet* src (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode)))))

(defn
  ^{:requires-bindings false
    :processing-name "blendColor()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  blend-color
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
  (let [mode (resolve-constant-key mode blend-modes)]
    (PApplet/blendColor (int c1) (int c2) (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "blue()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  blue
  "Extracts the blue value from a color, scaled to match current color-mode.
  Returns a float."
  [color]
  (.blue *applet* (int color)))

(defn
  ^{:requires-bindings true
    :processing-name "box()"
    :category "Shape"
    :subcategory "3D Primitives"
    :added "1.0"}
  box
  "Creates an extruded rectangle."
  ([size] (.box *applet* (float size)))
  ([width height depth] (.box *applet* (float width) (float height) (float depth))))

(defn
  ^{:requires-bindings true
    :processing-name "brightness()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  brightness
  "Extracts the brightness value from a color. Returns a float."
  [color]
  (.brightness *applet* (int color)))

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

(defn
  ^{:requires-bindings false
    :processing-name "ceil()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  ceil
  "Calculates the closest int value that is greater than or equal to
  the value of the parameter. For example, (ceil 9.03) returns the
  value 10."
  [n]
  (PApplet/ceil (float n)))

(defn
  ^{:requires-bindings true
    :processing-name "color()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  color
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

(defn
  ^{:requires-bindings true
    :processing-name "colorMode()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  color-mode
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
     (let [mode (resolve-constant-key mode color-modes)]
       (.colorMode *applet* (int mode))))
  ([mode max]
     (let [mode (resolve-constant-key mode color-modes)]
       (.colorMode *applet* (int mode) (float max))))
  ([mode max-x max-y max-z]
     (let [mode (resolve-constant-key mode color-modes)]
       (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z))))
  ([mode max-x max-y max-z max-a]
     (let [mode (resolve-constant-key mode color-modes)]
       (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z) (float max-a)))))

(defn
  ^{:requires-bindings false
    :processing-name "constrain()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  constrain-float
  "Constrains a value to not exceed a maximum and minimum value. All
  args are cast to floats."
  [amt low high]
  (PApplet/constrain (float amt) (float low) (float high)))

(defn
  ^{:requires-bindings false
    :processing-name "constrain()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  constrain-int
  "Constrains a value to not exceed a maximum and minimum value. All
  args are cast to ints."
  [amt low high]
  (PApplet/constrain (int amt) (int low) (int high)))

(defn
  ^{:requires-bindings false
    :processing-name "constrain()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  constrain
  "Constrains a value to not exceed a maximum and minimum value."
  [amt low high]
  (if (int-like? amt)
    (constrain-int amt low high)
    (constrain-float amt low high)))

(defn
  ^{:requires-bindings true
    :processing-name "copy()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  copy
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
  (PApplet/cos (float angle)))

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
  include a .ttf or .otf version of your font in the data directory of
  the sketch because other people might not have the font installed on
  their computer. Only fonts that can legally be distributed should be
  included with a sketch."
  []
  (seq (PFont/list)))

(defn
  ^{:requires-bindings false
    :processing-name nil
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  font-available?
  "Returns true if font (specified as a string) is available on this
  system, false otherwise"
  [font-str]
  (if (some #{font-str} available-fonts)
    true
    false))

(defn
  ^{:requires-bindings true
    :processing-name "createFont()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  create-font
  "Dynamically converts a font to the format used by Processing (a
  PFont) from either a font name that's installed on the computer, or
  from a .ttf or .otf file inside the sketches 'data' folder. This
  function is an advanced feature for precise control.

  Use available-fonts to obtain the names for the fonts recognized by
  the computer and are compatible with this function.

  The size parameter states the font size you want to generate. The
  smooth parameter specifies if the font should be antialiased or not,
  and the charset parameter is an array of chars that specifies the
  characters to generate.

  This function creates a bitmapped version of a font It loads a font
  by name, and converts it to a series of images based on the size of
  the font. When possible, the text function will use a native font
  rather than the bitmapped version created behind the scenes with
  create-font. For instance, when using the default renderer
  setting (JAVA2D), the actual native version of the font will be
  employed by the sketch, improving drawing quality and
  performance. With the P2D, P3D, and OPENGL renderer settings, the
  bitmapped version will be used. While this can drastically improve
  speed and appearance, results are poor when exporting if the sketch
  does not include the .otf or .ttf file, and the requested font is
  not available on the machine running the sketch."
  ([name size] (.createFont *applet* (str name) (float size)))
  ([name size smooth] (.createFont *applet* (str name) (float size) smooth))
  ([name size smooth ^chars charset]
     (.createFont *applet* (str name) (float size) smooth charset)))

(defn
  ^{:requires-bindings true
    :processing-name "createGraphics()"
    :category "Rendering"
    :subcategory nil
    :added "1.0"}
  create-graphics
  "Creates and returns a new PGraphics object of the types P2D, P3D,
  and JAVA2D. Use this class if you need to draw into an off-screen
  graphics buffer. It's not possible to use create-graphics with
  OPENGL, because it doesn't allow offscreen use. The PDF renderer
  requires the filename parameter. The DXF renderer should not be used
  with create-graphics, it's only built for use with begin-raw and
  end-raw.

  It's important to call any drawing commands between begin-draw and
  end-draw statements. This is also true for any commands that affect
  drawing, such as smooth or colorMode.

  Unlike the main drawing surface which is completely opaque, surfaces
  created with create-graphics can have transparency. This makes it
  possible to draw into a graphics and maintain the alpha channel. By
  using save to write a PNG or TGA file, the transparency of the
  graphics object will be honored. Note that transparency levels are
  binary: pixels are either complete opaque or transparent. For the
  time being (as of release 1.2.1), this means that text characters
  will be opaque blocks. This will be fixed in a future release (Issue
  80)."
  ([w h renderer]
     (.createGraphics *applet* (int w) (int h) renderer))
  ([w h renderer path]
     (.createGraphics *applet* (int w) (int h) renderer (str path))))

(defn
  ^{:requires-bindings true
    :processing-name "createImage()"
    :category "Image"
    :subcategory nil
    :added "1.0"}
  create-image
  "Creates a new PImage (the datatype for storing images). This
  provides a fresh buffer of pixels to play with. Set the size of the
  buffer with the width and height parameters. The format parameter
  defines how the pixels are stored. See the PImage reference for more
  information.

  Be sure to include all three parameters, specifying only the width
  and height (but no format) will produce a strange error.

  Prefer using create-image over initialising new PImage instances
  directly."
  [w h format]
  (.createImage *applet* (int w) (int h) (int format)))

(defn
  ^{:requires-bindings true
    :processing-name "createInput()"
    :category "Input"
    :subcategory "Files"
    :added "1.0"}
  create-input
  "This is a method for advanced programmers to open a Java
  InputStream. The method is useful if you want to use the facilities
  provided by PApplet to easily open files from the data folder or
  from a URL, but want an InputStream object so that you can use other
  Java methods to take more control of how the stream is read.

  If the requested item doesn't exist, null is returned.

  If not online, this will also check to see if the user is asking for
  a file whose name isn't properly capitalized. If capitalization is
  different an error will be printed to the console. This helps
  prevent issues that appear when a sketch is exported to the web,
  where case sensitivity matters, as opposed to running from inside
  the Processing Development Environment on Windows or Mac OS, where
  case sensitivity is preserved but ignored.

  The filename passed in can be:
  - A URL, for instance (open-stream \"http://processing.org/\";
  - A file in the sketch's data folder
  - The full path to a file to be opened locally (when running as an
    application)

  If the file ends with .gz, the stream will automatically be gzip
  decompressed. If you don't want the automatic decompression, use the
  related function create-input-raw."
  [filename]
  (.createInput *applet* (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "createInputRaw()"
    :category "Input"
    :subcategory "Files"
    :added "1.0"}
  create-input-raw
  "Call create-input without automatic gzip decompression."
  [filename]
  (.createInputRaw *applet* filename))

(defn
  ^{:requires-bindings true
    :processing-name "createOutput()"
    :category "Output"
    :subcategory "Files"
    :added "1.0"}
  create-output
  "Similar to create-input, this creates a Java OutputStream for a
  given filename or path. The file will be created in the sketch
  folder, or in the same folder as an exported application.

  If the path does not exist, intermediate folders will be created. If
  an exception occurs, it will be printed to the console, and null
  will be returned.

  This method is a convenience over the Java approach that requires
  you to 1) create a FileOutputStream object, 2) determine the exact
  file location, and 3) handle exceptions. Exceptions are handled
  internally by the function, which is more appropriate for sketch
  projects.

  If the output filename ends with .gz, the output will be
  automatically GZIP compressed as it is written."
  [filename]
  (.createOutput *applet* (str filename)))

(def ^{:private true}
  cursor-modes {:arrow PConstants/ARROW
                :cross PConstants/CROSS
                :hand PConstants/HAND
                :move PConstants/MOVE
                :text PConstants/TEXT
                :wait PConstants/WAIT})

(defn
  ^{:requires-bindings true
    :processing-name "cursor()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  cursor
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
  ([cursor-mode] (.cursor *applet* (int (resolve-constant-key cursor-mode cursor-modes)))))

(defn
  ^{:requires-bindings true
    :processing-name "cursor()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
    cursor-image
  "Set the cursor to a predefined image. The horizontal and vertical
  active spots of the cursor may be specified with hx and hy"
  ([^PImage img] (.cursor *applet* img))
  ([^PImage img hx hy] (.cursor *applet* img (int hx) (int hy))))

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

(defn
  ^{:requires-bindings true
    :processing-name "curveDetail()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-detail
  "Sets the resolution at which curves display. The default value is
  20. This function is only useful when using the P3D or OPENGL
  renderer as the default (JAVA2D) renderer does not use this
  information."
  [detail]
  (.curveDetail *applet* (int detail)))

(defn
  ^{:requires-bindings true
    :processing-name "curvePoint()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-point
  "Evalutes the curve at point t for points a, b, c, d. The parameter
  t varies between 0 and 1, a and d are points on the curve, and b c
  and are the control points. This can be done once with the x
  coordinates and a second time with the y coordinates to get the
  location of a curve at t."
  [a b c d t]
  (.bezierPoint *applet* (float a) (float b) (float c) (float d) (float t)))

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
  (.curveTangent *applet* (float a) (float b) (float c) (float d) (float t)))

(defn
  ^{:requires-bindings true
   :processing-name "curveTightness()"
    :category "Shape"
    :subcategory "Curves"
    :added "1.0"}
  curve-tightness
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

(defn
  ^{:requires-bindings true
    :processing-name "curveVertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  curve-vertex
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

(defn
  ^{:requires-bindings false
    :processing-name "day()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  day
  "Get the current day of the month (1 through 31)."
  []
  (PApplet/day))

(defn
  ^{:requires-bindings false
    :processing-name "degrees()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  degrees
  "Converts a radian measurement to its corresponding value in
  degrees. Radians and degrees are two ways of measuring the same
  thing. There are 360 degrees in a circle and (* 2 Math/PI) radians
  in a circle. For example, (= 90° (/ Math/PI 2) 1.5707964). All
  trigonometric methods in Processing require their parameters to be
  specified in radians."
  [radians]
  (PApplet/degrees (float radians)))

(defn
  ^{:requires-bindings true
    :processing-name "delay()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  delay-frame
  "Forces the program to stop running for a specified time. Delay
  times are specified in thousandths of a second, therefore the
  function call (delay 3000) will stop the program for three
  seconds. Because the screen is updated only at the end of draw,
  the program may appear to 'freeze', because the screen will not
  update when the delay fn is used. This function has no affect
  inside setup."
  [freeze-ms]
  (.delay *applet* (int freeze-ms)))

(defn
  ^{:requires-bindings true
    :processing-name "directionalLight()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  directional-light
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

(defn
  ^{:requires-bindings false
    :processing-name "dist()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  dist
  "Calculates the distance between two points"
  ([x1 y1 x2 y2] (PApplet/dist (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2] (PApplet/dist (float x1) (float y1) (float z1)
                               (float x2) (float y2) (float z2))))

(defn
  ^{:requires-bindings true
    :processing-name "ellipse()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  ellipse
  "Draws an ellipse (oval) in the display window. An ellipse with an
  equal width and height is a circle.  The origin may be changed with
  the ellipse-mode function"
  [x y width height]
  (.ellipse *applet* (float x) (float y) (float width) (float height)))

(def ^{:private true}
     ellipse-modes   {:center CENTER
                      :radius RADIUS
                      :corner CORNER
                      :corners CORNERS})



(defn
  ^{:requires-bindings true
    :processing-name "ellipseMode()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  ellipse-mode
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
  (let [mode (resolve-constant-key mode ellipse-modes)]
    (.ellipseMode *applet* (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "emissive()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  emissive-float
  "Sets the emissive color of the material used for drawing shapes
 drawn to the screen. Used in combination with ambient, specular, and
 shininess in setting the material properties of shapes. Converts all
 args to floats"
  ([float-val] (.emissive *applet* (float float-val)))
  ([r g b] (.emissive *applet* (float r) (float g) (float b))))

(defn
  ^{:requires-bindings true
    :processing-name "emissive()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  emissive-int
  "Sets the emissive color of the material used for drawing shapes
  drawn to the screen. Used in combination with ambient, specular, and
  shininess in setting the material properties of shapes. Converts all
  args to ints"
  [int-val] (.emissive *applet* (int int-val)))

(defn
  ^{:requires-bindings true
    :processing-name "emissive()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  emissive
  "Sets the emissive color of the material used for drawing shapes
  drawn to the screen. Used in combination with ambient, specular, and
  shininess in setting the material properties of shapes.

  If passed one arg - it is assumed to be an int (i.e. a color),
  multiple args are converted to floats."
  ([c] (if (int-like? c) (emissive-int c) (emissive-float c)))
  ([r g b] (emissive-float r g b)))

(defn
  ^{:requires-bindings true
    :processing-name "endRaw()"
    :category "Output"
    :subcategory "Files"
    :added "1.0"}
  end-raw
  "Complement to begin-raw; they must always be used together. See
  the begin-raw docstring for details."
  []
  (.endRaw *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "endShape()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  end-shape
  "May only be called after begin-shape. When end-shape is called,
  all of image data defined since the previous call to begin-shape is
  written into the image buffer. The keyword :close may be passed to
  close the shape (to connect the beginning and the end)."
  ([] (.endShape *applet*))
  ([mode]
     (when-not (= :close mode)
       (throw (Exception. (str "Unknown mode value: " mode ". Expected :close"))))
     (.endShape *applet* CLOSE)))

(defn
  ^{:requires-bindings true
    :processing-name "exit()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
    exit
  "Quits/stops/exits the program.  Rather than terminating
  immediately, exit will cause the sketch to exit after draw has
  completed (or after setup completes if called during the setup
  method). "
  []
  (.exit *applet*))

(defn
  ^{:requires-bindings false
    :processing-name "exp()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  exp
  "Returns Euler's number e (2.71828...) raised to the power of the
  value parameter."
  [val]
  (PApplet/exp (float val)))

(defn
  ^{:requires-bindings true
    :processing-name "fill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  fill-float
  "Sets the color used to fill shapes. For example, (fill 204 102 0),
  will specify that all subsequent shapes will be filled with orange."
  ([gray] (.fill *applet* (float gray)))
  ([gray alpha] (.fill *applet* (float gray) (float alpha)))
  ([r g b] (.fill *applet* (float r) (float g) (float b)))
  ([r g b alpha] (.fill *applet* (float r) (float g) (float b) (float alpha))))

(defn
  ^{:requires-bindings true
    :processing-name "fill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  fill-int
  "Sets the color used to fill shapes."
  ([rgb] (.fill *applet* (int rgb)))
  ([rgb alpha] (.fill *applet* (int rgb) (float alpha))))

(defn
  ^{:requires-bindings true
    :processing-name "fill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  fill
  "Sets the color used to fill shapes."
  ([rgb] (if (int-like? rgb) (fill-int rgb) (fill-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (fill-int rgb alpha) (fill-float rgb alpha)))
  ([r g b] (fill-float r g b))
  ([r g b a] (fill-float r g b a)))

(def ^{:private true}
  filter-modes {:threshold THRESHOLD
                :gray GRAY
                :invert INVERT
                :posterize POSTERIZE
                :blur BLUR
                :opaque OPAQUE
                :erode ERODE
                :dilate DILATE})

(defn
  ^{:requires-bindings true
    :processing-name "filter()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  display-filter
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
     (let [mode (resolve-constant-key mode filter-modes)]
       (.filter *applet* (int mode))))
  ([mode level]
     (let [mode (resolve-constant-key mode filter-modes)]
       (.filter *applet* (int mode) (float level)))))

(defn
  ^{:requires-bindings true
    :processing-name "focused"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  focused
  "Returns a boolean value representing whether the applet has focus."
  []
  (. *applet* :focused))

(defn
  ^{:requires-bindings true
    :processing-name "frameCount"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  frame-count
  "The system variable frameCount contains the number of frames
  displayed since the program started. Inside setup() the value is 0
  and and after the first iteration of draw it is 1, etc."
  []
  (.frameCount *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "frameRate"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  current-frame-rate
  "Returns the current framerate"
  []
  (.frameRate *applet*)  )

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
  call (frame-rate 30) will attempt to refresh 30 times a second. It
  is recommended to set the frame rate within setup. The default rate
  is 60 frames per second."
  [new-rate]
  (.frameRate *applet* (float new-rate)))

(defn
  ^{:requires-bindings true
    :processing-name "frustum()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  frustum
  "Sets a perspective matrix defined through the parameters. Works
  like glFrustum, except it wipes out the current perspective matrix
  rather than muliplying itself with it."
  [left right bottom top near far]
  (.frustum *applet* (float left) (float right) (float bottom) (float top)
            (float near) (float far)))

(defn
  ^{:requires-bindings true
    :processing-name "get()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  get-pixel
  "Reads the color of any pixel or grabs a section of an image. If no
  parameters are specified, the entire image is returned. Get the
  value of one pixel by specifying an x,y coordinate. Get a section of
  the display window by specifying an additional width and height
  parameter. If the pixel requested is outside of the image window,
  black is returned. The numbers returned are scaled according to the
  current color ranges, but only RGB values are returned by this
  function. For example, even though you may have drawn a shape with
  colorMode(HSB), the numbers returned will be in RGB.

  Getting the color of a single pixel with (get x y) is easy, but not
  as fast as grabbing the data directly using the pixels fn."
  ([] (.get *applet*))
  ([x y] (.get *applet* (int x) (int y)))
  ([x y w h] (.get *applet* (int x) (int y) (int w) (int h))))

(declare load-pixels)

(defn
  ^{:requires-bindings true
    :processing-name "pixels[]"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  pixels
  "Array containing the values for all the pixels in the display
  window. This array is therefore the size of the display window. If
  this array is modified, the update-pixels fn must be called to update
  the changes. Calls load-pixels before obtaining the pixel array."
  []
  (load-pixels)
  (. *applet* :pixels))

(defn
  ^{:requires-bindings true
    :processing-name "green()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  green
  "Extracts the green value from a color, scaled to match current
  color-mode. This value is always returned as a float so be careful
  not to assign it to an int value."
  [col]
  (.green *applet* (int col)))

(defn
  ^{:require-binding false
    :processing-name "hex()"
    :category "Data"
    :subcategory "Conversion"}
  hex
  "Converts a byte, char, int, or color to a String containing the
  equivalent hexadecimal notation. For example color(0, 102, 153) will
  convert to the String \"FF006699\". This function can help make your
  geeky debugging sessions much happier. "
  ([val] (PApplet/hex val))
  ([val num-digits] (PApplet/hex (int val) (int num-digits))))

(defn
  ^{:require-binding false
    :processing-name "hex()"
    :category "Data"
    :subcategory "Conversion"}
  unhex
  "Converts a String representation of a hexadecimal number to its
  equivalent integer value."
  [hex-str]
  (PApplet/unhex (str hex-str)))

(defn
  ^{:requires-bindings true
    :processing-name "height"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  height
  "Height of the display window. The value of height is zero until
  size is called."
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

(defn
  ^{:requires-bindings true
    :processing-name "hint()"
    :category "Rendering"
    :subcategory nil
    :added "1.0"}
  hint
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

(defn
  ^{:requires-bindings false
    :processing-name "hour()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  hour
  "Returns the current hour as a value from 0 - 23."
  []
  (PApplet/hour))

(defn
  ^{:requires-bindings true
    :processing-name "hue()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  hue
  "Extracts the hue value from a color."
  [col]
  (.hue *applet* (int col)))

(defn
  ^{:requires-bindings true
    :processing-name "image()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  image
  "Displays images to the screen. Processing currently works with GIF,
  JPEG, and Targa images. The color of an image may be modified with
  the tint function and if a GIF has transparency, it will maintain
  its transparency. The img parameter specifies the image to display
  and the x and y parameters define the location of the image from its
  upper-left corner. The image is displayed at its original size
  unless the width and height parameters specify a different size. The
  image-mode fn changes the way the parameters work. A call to
  (image-mode :corners) will change the width and height parameters to
  define the x and y values of the opposite corner of the image.

  Starting with release 0124, when using the default (JAVA2D)
  renderer, smooth will also improve image quality of resized
  images."
  ([^PImage img x y] (.image *applet* img (float x) (float y)))
  ([^PImage img x y c d] (.image *applet* img (float x) (float y)
                                  (float c) (float d)))
  ([^PImage img x y c d u1 v1 u2 v2]
     (.image *applet* img (float x) (float y) (float c) (float d)
             (float u1) (float v1) (float u2) (float v2))))

(def ^{:private true}
  image-modes {:corner CORNER
               :corners CORNERS
               :center CENTER})

(defn
  ^{:requires-bindings true
    :processing-name "imageMode()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  image-mode
  "Modifies the location from which images draw. The default mode is :corner.
   Available modes are:

  :corner  - specifies the location to be the upper left corner and
             uses the fourth and fifth parameters of image to set the
             image's width and height.

  :corners - uses the second and third parameters of image to set the
             location of one corner of the image and uses the fourth
             and fifth parameters to set the opposite corner.

  :center  - draw images centered at the given x and y position."
  [mode]
  (let [mode (resolve-constant-key mode image-modes)]
    (.imageMode *applet* (int mode))))

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
  in the code. The default value is (light-falloff 1.0 0.0 0.0).
  Thinking about an ambient light with a falloff can be tricky. It is
  used, for example, if you wanted a region of your scene to be lit
  ambiently one color and another region to be lit ambiently by
  another color, you would use an ambient light with location and
  falloff. You can think of it as a point light that doesn't care
  which direction a surface is facing."
  [constant linear quadratic]
  (.lightFalloff *applet* (float constant) (float linear) (float quadratic)))

(defn
  ^{:requires-bindings true
    :processing-name "lerpColor()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  lerp-color
  "Calculates a color or colors between two color at a specific
  increment. The amt parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc."
  [c1 c2 amt]
  (.lerpColor *applet* (int c1) (int c2) (float amt)))

(defn
  ^{:requires-bindings false
    :processing-name "lerp()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  lerp
  "Calculates a number between two numbers at a specific
  increment. The amt parameter is the amount to interpolate between
  the two values where 0.0 equal to the first point, 0.1 is very near
  the first point, 0.5 is half-way in between, etc. The lerp function
  is convenient for creating motion along a straight path and for
  drawing dotted lines."
  [start stop amt]
  (PApplet/lerp (float start) (float stop) (float amt)))

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
  (. *applet* :key))

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
  coded. This is done with the conditional (= (key) CODED).

  The keys included in the ASCII specification (BACKSPACE, TAB, ENTER,
  RETURN, ESC, and DELETE) do not require checking to see if they key
  is coded, and you should simply use the key variable instead of
  key-code If you're making cross-platform projects, note that the
  ENTER key is commonly used on PCs and Unix and the RETURN key is
  used instead on Macintosh. Check for both ENTER and RETURN to make
  sure your program will work for all platforms.

  For users familiar with Java, the values for UP and DOWN are simply
  shorter versions of Java's KeyEvent.VK_UP and
  KeyEvent.VK_DOWN. Other keyCode values can be found in the Java
  KeyEvent reference."
  []
  (. *applet* :keyCode))

(defn
  ^{:requires-bindings true
    :processing-name "keyPressed"
    :category "Input"
    :subcategory "Keyboard"
    :added "1.0"}
    key-pressed?
  "true if any key is currently pressed, false otherwise."
  []
  (. *applet* :keyPressed))

(defn
  ^{:requires-bindings true
    :processing-name "lights()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  lights
  "Sets the default ambient light, directional light, falloff, and
  specular values. The defaults are:

  (ambient-light 128 128 128)
  (directional-light 128 128 128 0 0 -1)
  (light-falloff 1 0 0)
  (light-specular 0 0 0).

  Lights need to be included in the draw to remain persistent in a
  looping program. Placing them in the setup of a looping program
  will cause them to only have an effect the first time through the
  loop."
  []
  (.lights *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "lightSpecular()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  light-specular
  "Sets the specular color for lights. Like fill, it affects only the
  elements which are created after it in the code. Specular refers to
  light which bounces off a surface in a perferred direction (rather
  than bouncing in all directions like a diffuse light) and is used
  for creating highlights. The specular quality of a light interacts
  with the specular material qualities set through the specular and
  shininess functions."
  [r g b]
  (.lightSpecular *applet* (float r) (float g) (float b)))

(defn
  ^{:requires-bindings true
    :processing-name "line()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  line
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

(defn
  ^{:requires-bindings true
    :processing-name "loadBytes()"
    :category "Input"
    :subcategory "Files"
    :added "1.0"}
  load-bytes
  "Reads the contents of a file or url and places it in a byte
  array. The filename parameter can also be a URL to a file found
  online."
  [filename]
  (.loadBytes *applet* (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "loadFont()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-font
  "Loads a font into a variable of type PFont. To load correctly,
  fonts must be located in the data directory of the current sketch.
  To create a font to use with Processing use the create-font fn.

  Like load-image and other methods that load data, the load-font fn
  should not be used inside draw, because it will slow down the sketch
  considerably, as the font will be re-loaded from the disk (or
  network) on each frame.

  For most renderers, Processing displays fonts using the .vlw font
  format, which uses images for each letter, rather than defining them
  through vector data. When hint :enable-native-fonts is used with the
  JAVA2D renderer, the native version of a font will be used if it is
  installed on the user's machine.

  Using create-font (instead of load-font) enables vector data to be
  used with the JAVA2D (default) renderer setting. This can be helpful
  when many font sizes are needed, or when using any renderer based on
  JAVA2D, such as the PDF library."
  [filename]
  (.loadFont *applet*  (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "loadImage()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-image
  "Loads an image into a variable of type PImage. Four types of
  images ( .gif, .jpg, .tga, .png) images may be loaded. To load
  correctly, images must be located in the data directory of the
  current sketch. In most cases, load all images in setup to preload
  them at the start of the program. Loading images inside draw will
  reduce the speed of a program.

  The filename parameter can also be a URL to a file found online.

  The extension parameter is used to determine the image type in cases
  where the image filename does not end with a proper
  extension. Specify the extension as the second parameter to
  load-image, as shown in the third example on this page.

  If an image is not loaded successfully, the null value is returned
  and an error message will be printed to the console. The error
  message does not halt the program, however the null value may cause
  a NullPointerException if your code does not check whether the value
  returned from load-image is nil.

  Depending on the type of error, a PImage object may still be
  returned, but the width and height of the image will be set to
  -1. This happens if bad image data is returned or cannot be decoded
  properly. Sometimes this happens with image URLs that produce a 403
  error or that redirect to a password prompt, because load-image
  will attempt to interpret the HTML as image data."
  [filename]
  (.loadImage *applet* (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "loadPixels()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  load-pixels
  "Loads the pixel data for the display window into the pixels[]
  array. This function must always be called before reading from or
  writing to pixels.

  Certain renderers may or may not seem to require load-pixels or
  update-pixels. However, the rule is that any time you want to
  manipulate the pixels array, you must first call load-pixels, and
  after changes have been made, call updatePixels. Even if the
  renderer may not seem to use this function in the current Processing
  release, this will always be subject to change."
  []
  (.loadPixels  *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "loadShape()"
    :category "Shape"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  load-shape
  "Load a geometry from a file as a PShape."
  [filename]
  (.loadShape *applet* filename))

(defn
  ^{:requires-bindings true
    :processing-name "loadStrings()"
    :category "Input"
    :subcategory "Files"
    :added "1.0"}
  load-strings
  "Load data from a file and shove it into a String array."
  [filename]
  (.loadStrings *applet* filename))

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
  (PApplet/log (float val)))

(defn
  ^{:requires-bindings true
    :processing-name "loop()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
    start-loop
  "Causes Processing to continuously execute the code within
  draw. If no-loop is called, the code in draw stops executing."
  []
  (.loop *applet*))

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
  can be thought of as the distance from coordinate (0,0) to its (x,y)
  value. Therefore, mag is a shortcut for writing (dist 0 0 x y)."
  ([a b] (PApplet/mag (float a) (float b)))
  ([a b c] (PApplet/mag (float a) (float b) (float c))))

(defn
  ^{:requires-bindings false
    :processing-name "map()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  map-range
  "Re-maps a number from one range to another. In the example above,
  the number '25' is converted from a value in the range 0..100 into a
  value that ranges from the left edge (0) to the right edge (width)
  of the screen.

  Numbers outside the range are not clamped to 0 and 1, because
  out-of-range values are often intentional and useful."
  [val low1 high1 low2 high2]
  (PApplet/map (float val) (float low1) (float high1) (float low2) (float high2)))

(defn
  ^{:requires-bindings false
    :processing-name nil
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  mask-image
  "Masks part of an image from displaying by loading another image and
  using it as an alpha channel.  This mask image should only contain
  grayscale data, but only the blue color channel is used. The mask
  image needs to be the same size as the image to which it is
  applied.

   This method is useful for creating dynamically generated alpha
   masks."
  [^PImage img]
  (.mask *applet* img))

(defn
  ^{:requires-bindings true
    :processing-name "millis()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  millis
  "Returns the number of milliseconds (thousandths of a second) since
  starting an applet. This information is often used for timing
  animation sequences."
  []
  (.millis *applet*))

(defn
  ^{:requires-bindings false
    :processing-name "minute()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  minute
  "Returns the current minute as a value from 0 - 59"
  []
  (PApplet/minute))

(defn
  ^{:requires-bindings true
    :processing-name "modelX()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  model-x
  "Returns the three-dimensional x, y, z position in model space. This
  returns the x value for a given coordinate based on the current set
  of transformations (scale, rotate, translate, etc.) The x value can
  be used to place an object in space relative to the location of the
  original point once the transformations are no longer in use."
  [x y z]
  (.modelX *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "modelY()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  model-y
  "Returns the three-dimensional x, y, z position in model space. This
  returns the y value for a given coordinate based on the current set
  of transformations (scale, rotate, translate, etc.) The y value can
  be used to place an object in space relative to the location of the
  original point once the transformations are no longer in use."
  [x y z]
  (.modelY *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "modelZ()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  model-z
  "Returns the three-dimensional x, y, z position in model space. This
  returns the z value for a given coordinate based on the current set
  of transformations (scale, rotate, translate, etc.) The z value can
  be used to place an object in space relative to the location of the
  original point once the transformations are no longer in use."
  [x y z]
  (.modelZ *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings false
    :processing-name "month()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  month
  "Returns the current month as a value from 1 - 12."
  []
  (PApplet/month))

(defn
  ^{:requires-bindings true
    :processing-name "mouseButton"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-button
  "The value of the system variable mouseButton is either :left, :right,
  or :center depending on which button is pressed."
  []
  (let [button-code   (. *applet* :mouseButton)]
    (case button-code
      LEFT :left
      RIGHT :right
      CENTER :center)))

(defn
  ^{:requires-bindings true
    :processing-name "mousePressed"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-state
  "Variable storing if a mouse button is pressed. The value of the
  system variable mousePressed is true if a mouse button is pressed
  and false if a button is not pressed."
  []
  (. *applet* :mousePressed))

(defn
  ^{:requires-bindings true
    :processing-name "mouseX"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-x
  "Current horizontal coordinate of the mouse."
  []
  (. *applet* :mouseX))

(defn
  ^{:requires-bindings true
    :processing-name "mouseY"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  mouse-y
  "Current vertical coordinate of the mouse."
  []
  (. *applet* :mouseY))

(defn
  ^{:requires-bindings true
    :processing-name "pmouseX"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  pmouse-x
  "Horizontal coordinate of the mouse in the previous frame"
  []
  (. *applet* :pmouseX))

(defn
  ^{:requires-bindings true
    :processing-name "pmouseY"
    :category "Input"
    :subcategory "Mouse"
    :added "1.0"}
  pmouse-y
  "Vertical coordinate of the mouse in the previous frame"
  []
  (. *applet* :pmouseY))

(defn
  ^{:requires-bindings true
    :processing-name "noCursor()"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  no-cursor
  "Hides the cursor from view. Will not work when running the in full
  screen (Present) mode."
  []
  (.noCursor *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "noFill()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  no-fill
 "Disables filling geometry. If both no-stroke and no-fill are called,
  nothing will be drawn to the screen."  []
 (.noFill *applet*))

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
  ([x] (.noise *applet* (float x)))
  ([x y] (.noise *applet* (float x) (float y)))
  ([x y z] (.noise *applet* (float x) (float y) (float z))))

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
  signal and as such define the overal intensity of the noise, whereas
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
  ([octaves] (.noiseDetail *applet* (int octaves)))
  ([octaves falloff] (.noiseDetail *applet* (int octaves) (float falloff))))

(defn
  ^{:requires-bindings true
    :processing-name "noiseSeed()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  noise-seed
  "Sets the seed value for noise. By default, noise produces different
  results each time the program is run. Set the value parameter to a
  constant to return the same pseudo-random numbers each time the
  software is run."
  [val]
  (.noiseSeed *applet* (int val)))

(defn
  ^{:requires-bindings true
    :processing-name "noLights()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  no-lights
  "Disable all lighting. Lighting is turned off by default and enabled
  with the lights fn. This function can be used to disable lighting so
  that 2D geometry (which does not require lighting) can be drawn
  after a set of lighted 3D geometry."
  []
  (.noLights *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "noLoop()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  no-loop
  "Stops Processing from continuously executing the code within
  draw. If start-loop is called, the code in draw will begin to run
  continuously again. If using no-loop in setup, it should be the last
  line inside the block.

  When no-loop is used, it's not possible to manipulate or access the
  screen inside event handling functions such as mouse-pressed or
  key-pressed. Instead, use those functions to call redraw or
  loop which will run draw, which can update the screen
  properly. This means that when no-loop has been called, no drawing
  can happen, and functions like save-frame or load-pixels may not
  be used.

  Note that if the sketch is resized, redraw will be called to
  update the sketch, even after no-oop has been
  specified. Otherwise, the sketch would enter an odd state until
  loop was called."
  []
  (.noLoop *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "norm()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  norm
  "Normalize a value to exist between 0 and 1 (inclusive)."
  [val start stop]
  (PApplet/norm (float val) (float start) (float stop)))

(defn
  ^{:requires-bindings true
    :processing-name "normal()"
    :category "Lights, Camera"
    :subcategory "Lights"
    :added "1.0"}
  normal
  "Sets the current normal vector. This is for drawing three
  dimensional shapes and surfaces and specifies a vector perpendicular
  to the surface of the shape which determines how lighting affects
  it. Processing attempts to automatically assign normals to shapes,
  but since that's imperfect, this is a better option when you want
  more control. This function is identical to glNormal3f() in OpenGL."
  [nx ny nz]
  (.normal *applet* (float nx) (float ny) (float nz)))

(defn
  ^{:requires-bindings true
    :processing-name "noSmooth()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  no-smooth
  "Draws all geometry with jagged (aliased) edges."
  [] (.noSmooth *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "noStroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  no-stroke
  "Disables drawing the stroke (outline). If both no-stroke and
  no-fill are called, nothing will be drawn to the screen."
  []
  (.noStroke *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "noTint()"
    :category "Color"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  no-tint
  "Removes the current fill value for displaying images and reverts to
  displaying images with their original hues."
  []
  (.noTint *applet*))

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
  parameters to this function specify the clipping volume where left
  and right are the minimum and maximum x values, top and bottom are
  the minimum and maximum y values, and near and far are the minimum
  and maximum z values. If no parameters are given, the default is
  used: ortho(0, width, 0, height, -10, 10)."
  ([] (.ortho *applet*))
  ([left right bottom top near far]
     (.ortho *applet* (float left) (float right) (float bottom) (float top) (float near) (float far))))

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
  perspective(PI/3.0, width/height, cameraZ/10.0, cameraZ*10.0) where
  cameraZ is ((height/2.0) / tan(PI*60.0/360.0));"
  ([] (.perspective *applet*))
  ([fovy aspect z-near z-far]
     (.perspective *applet* (float fovy) (float aspect)
                   (float z-near) (float z-far))))

(defn
  ^{:requires-bindings true
    :processing-name "point()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  point
  "Draws a point, a coordinate in space at the dimension of one
  pixel. The first parameter is the horizontal value for the point,
  the second value is the vertical value for the point, and the
  optional third value is the depth value. Drawing this shape in 3D
  using the z parameter requires the P3D or OPENGL parameter in
  combination with size as shown in the above example."
  ([x y] (.point *applet* (float x)(float y)))
  ([x y z] (.point *applet* (float x) (float y) (float z))))

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
  first time through the loop. The affect of the r, g, and b
  parameters is determined by the current color mode. The x, y, and z
  parameters set the position of the light"
  [r g b x y z]
  (.pointLight *applet* (float r) (float g) (float b) (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "popMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  pop-matrix
  "Pops the current transformation matrix off the matrix
  stack. Understanding pushing and popping requires understanding the
  concept of a matrix stack. The push-matrix fn saves the current
  coordinate system to the stack and pop-matrix restores the prior
  coordinate system. push-matrix and pop-matrix are used in conjuction
  with the other transformation methods and may be embedded to control
  the scope of the transformations."
  []
  (.popMatrix *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "popStyle()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  pop-style
  "Restores the prior settings on the 'style stack'. Used in
  conjunction with push-style. Together they allow you to change the
  style settings and later return to what you had. When a new style is
  started with push-style, it builds on the current style information.
  The push-style and pop-style functions can be nested to provide more
  control"
  []
  (.popStyle *applet*))

(defn
  ^{:requires-bindings false
    :processing-name "pow()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  pow
  "Facilitates exponential expressions. The pow() function is an
  efficient way of multiplying numbers by themselves (or their
  reciprocal) in large quantities. For example, (pow 3 5) is
  equivalent to the expression (* 3 3 3 3 3) and (pow 3 -5) is
  equivalent to (/ 1 (* 3 3 3 3 3))."
  [num exponent]
  (PApplet/pow (float num) (float exponent)))

(defn
  ^{:requires-bindings true
    :processing-name "printCamera()"
    :category "Lights, Camera"
    :subcategory "Camera"
    :added "1.0"}
  print-camera
  "Prints the current camera matrix to std out. Useful for debugging."
  []
  (.printCamera *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "printMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  print-matrix
  "Prints the current matrix to std out. Useful for debugging."
  []
  (.printMatrix *applet*))

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
  (.printProjection *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "pushMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  push-matrix
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

(defn
  ^{:requires-bindings true
    :processing-name "pushStyle()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  push-style
  "Saves the current style settings onto a 'style stack'. Use with
  pop-style which restores the prior settings. Note that these
  functions are always used together. They allow you to change the
  style settings and later return to what you had. When a new style is
  started with push-style, it builds on the current style
  information. The push-style and pop-style fns can be embedded to
  provide more control (see the second example above for a
  demonstration.)

  The style information controlled by the following functions are
  included in the style: fill, stroke, tint, stroke-weight,
  stroke-cap, stroke-join, image-mode, rect-mode, ellipse-mode,
  shape-mode, color-mode, text-align, text-font, text-mode, text-size,
  text-leading, emissive, specular, shininess, and ambient"
  []
  (.pushStyle *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "quad()"
    :category "Shape"
    :subcategory "2D Primitives"
    :added "1.0"}
  quad
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
  methods in Processing require their parameters to be specified in
  radians."
  [degrees]
  (PApplet/radians (float degrees)))

(defn
  ^{:requires-bindings true
    :processing-name "random()"
    :category "Math"
    :subcategory "Random"
    :added "1.0"}
  random
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
  (.randomSeed *applet* (float w)))

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
   may be changed with rect-mode."
  [x y width height]
  (.rect *applet* (float x) (float y) (float width) (float height)))

(def ^{:private true}
  rect-modes {:corner CORNER
              :corners CORNERS
              :center CENTER
              :radius RADIUS})

(defn
  ^{:requires-bindings true
    :processing-name "rectMode()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  rect-mode
  "Modifies the location from which rectangles draw. The default mode
  is :corner. Available modes are:


  :corner  - Specifies the location to be the upper left corner of the
             shape and uses the third and fourth parameters of rect to
             specify the width and height.

  :corners - Uses the first and second parameters of rect to set the
             location of one corner and uses the third and fourth
             parameters to set the opposite corner.

  :center  - Draws the image from its center point and uses the third
             and forth parameters of rect to specify the image's width
             and height.

  :radius  - Draws the image from its center point and uses the third
             and forth parameters of rect() to specify half of the
             image's width and height."

  [mode]
  (let [mode (resolve-constant-key mode rect-modes)]
    (.rectMode *applet* (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "red()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  red
  "Extracts the red value from a color, scaled to match current color-mode."
  [c]
  (.red *applet* (int c)))

(defn
  ^{:requires-bindings true
    :processing-name "redraw()"
    :category "Structure"
    :subcategory nil
    :added "1.0"}
  redraw
  "Executes the code within the draw fn one time. This functions
  allows the program to update the display window only when necessary,
  for example when an event registered by mouse-pressed or
  key-pressed occurs.

  In structuring a program, it only makes sense to call redraw
  within events such as mouse-pressed. This is because redraw does
  not run draw immediately (it only sets a flag that indicates an
  update is needed).

  Calling redraw within draw has no effect because draw is
  continuously called anyway."
  []
  (.redraw *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "requestImage()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  request-image
  "This function load images on a separate thread so that your sketch
  does not freeze while images load during setup. While the image is
  loading, its width and height will be 0. If an error occurs while
  loading the image, its width and height will be set to -1. You'll
  know when the image has loaded properly because its width and height
  will be greater than 0. Asynchronous image loading (particularly
  when downloading from a server) can dramatically improve
  performance.

  The extension parameter is used to determine the image type in cases
  where the image filename does not end with a proper
  extension. Specify the extension as the second parameter to
  request-image."
  ([filename] (.requestImage *applet* (str filename)))
  ([filename extension] (.requestImage *applet* (str filename) (str extension))))

(defn
  ^{:requires-bindings true
    :processing-name "resetMatrix()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  reset-matrix
  "Replaces the current matrix with the identity matrix. The
  equivalent function in OpenGL is glLoadIdentity()"
  []
  (.resetMatrix *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "rotate()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate
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

(defn
  ^{:requires-bindings true
    :processing-name "rotateX()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-x
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

(defn
  ^{:requires-bindings true
    :processing-name "rotateY()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-y
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

(defn
  ^{:requires-bindings true
    :processing-name "rotateZ()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  rotate-z
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

(defn
  ^{:requires-bindings false
    :processing-name "round()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  round
  "Calculates the integer closest to the value parameter. For example,
  round(9.2) returns the value 9."
  [val]
  (PApplet/round (float val)))

(defn
  ^{:requires-bindings true
    :processing-name "saturation()"
    :category "Color"
    :subcategory "Creating & Reading"
    :added "1.0"}
  saturation
  "Extracts the saturation value from a color."
  [c]
  (.saturation *applet* (int c)))

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
  create-graphics."
  [filename]
  (.save *applet* (str filename)))

(defn
  ^{:requires-bindings true
    :processing-name "saveFrame()"
    :category "Output"
    :subcategory "Image"
    :added "1.0"}
  save-frame
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

(defn- current-screen
  []
  (let [default-toolkit (java.awt.Toolkit/getDefaultToolkit)]
    (.getScreenSize default-toolkit)))

(def ^{:private true} orig-screen-width
  (let [screen (current-screen)]
    (.width screen)))

(defn
  ^{:requires-bindings false
    :processing-name "screen.width"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  screen-width
  "Returns the width of the main screen in pixels."
  []
  orig-screen-width)

(def ^{:private true} orig-screen-height
  (let [screen (current-screen)]
    (.height screen)))

(defn
  ^{:requires-bindings false
    :processing-name "screen.height"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  screen-height
  "Returns the height of the main screen in pixels."
  []
  orig-screen-height)

(defn
  ^{:requires-bindings true
    :processing-name "screenX()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  screen-x
  "Takes a three-dimensional x, y, z position and returns the x value
  for where it will appear on a (two-dimensional) screen, once
  affected by translate, scale or any other transformations"
  [x y z]
  (.screenX *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "screenY()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  screen-y
  "Takes a three-dimensional x, y, z position and returns the y value
  for where it will appear on a (two-dimensional) screen, once
  affected by translate, scale or any other transformations"
  [x y z]
  (.screenY *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings true
    :processing-name "screenZ()"
    :category "Lights, Camera"
    :subcategory "Coordinates"
    :added "1.0"}
  screen-z
  "Given an x, y, z coordinate, returns its z value.
   This value can be used to determine if an x, y, z coordinate is in
   front or in back of another (x, y, z) coordinate. The units are
   based on how the zbuffer is set up, and don't relate to anything
   'real'. They're only useful for in comparison to another value
   obtained from screen-z, or directly out of the zbuffer"
  [x y z]
  (.screenX *applet* (float x) (float y) (float z)))

(defn
  ^{:requires-bindings false
    :processing-name "seconds()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  seconds
  "Returns the current second as a value from 0 - 59."
  []
  (PApplet/second))

(defn
  ^{:requires-bindings true
    :processing-name "set()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  set-pixel
  "Changes the color of any pixel in the display window. The x and y
  parameters specify the pixel to change and the color parameter
  specifies the color value. The color parameter is affected by the
  current color mode (the default is RGB values from 0 to 255).

  Setting the color of a single pixel with (set x, y) is easy, but not
  as fast as putting the data directly into pixels[].

  This function ignores imageMode().

  Due to what appears to be a bug in Apple's Java implementation, the
  point() and set() methods are extremely slow in some circumstances
  when used with the default renderer. Using P2D or P3D will fix the
  problem. Grouping many calls to point or set-pixel together can also
  help. (Bug 1094)"
  [x y c]
  (.set *applet* (int x) (int y) (int c)))

(defn
  ^{:requires-bindings true
    :processing-name "set()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  set-image
  "Writes an image directly into the display window. The x and y
  parameters define the coordinates for the upper-left corner of the
  image."
  [x y ^PImage src]
  (.set *applet* (int x) (int y) src))

(defn
  ^{:requires-bindings true
    :processing-name "shape()"
    :category "Shape"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  shape
  "Displays shapes to the screen. The shapes must have been loaded
  with load-shape. Processing currently works with SVG shapes
  only. The sh parameter specifies the shape to display and the x and
  y parameters define the location of the shape from its upper-left
  corner. The shape is displayed at its original size unless the width
  and height parameters specify a different size. The shape-mode
  fn changes the way the parameters work. A call to
  (shape-mode :corners), for example, will change the width and height
  parameters to define the x and y values of the opposite corner of
  the shape.

  Note complex shapes may draw awkwardly with P2D, P3D, and
  OPENGL. Those renderers do not yet support shapes that have holes or
  complicated breaks."
  ([^PShape sh] (.shape *applet* sh))
  ([^PShape sh x y] (.shape *applet* sh (float x) (float y)))
  ([^PShape sh x y width height] (.shape *applet* sh (float x) (float y) (float width) (float height))))

(defn
  ^{:requires-bindings true
    :processing-name "shearX()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  shear-x
  "Shears a shape around the x-axis the amount specified by the angle
  parameter. Angles should be specified in radians (values from 0 to
  PI*2) or converted to radians with the radians() function. Objects
  are always sheared around their relative position to the origin and
  positive numbers shear objects in a clockwise direction.
  Transformations apply to everything that happens after and
  subsequent calls to the function accumulates the effect. Fora
  example, calling (shear-x (/ PI 2)) and then (shear-x (/ PI 2)) is
  the same as (shear-x PI). If shear-x is called within the draw fn,
  the transformation is reset when the loop begins again. This
  function works in P2D or JAVA2D mode as shown in the example above.

  Technically, shear-x multiplies the current transformation matrix
  by a rotation matrix. This function can be further controlled by the
  push-matrix and pop-matrix fns."
  [angle]
  (.shearX *applet* (float angle)))

(defn
  ^{:requires-bindings true
    :processing-name "shearY()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  shear-y
  "Shears a shape around the y-axis the amount specified by the angle
  parameter. Angles should be specified in radians (values from 0 to
  PI*2) or converted to radians with the radians() function. Objects
  are always sheared around their relative position to the origin and
  positive numbers shear objects in a clockwise direction.
  Transformations apply to everything that happens after and
  subsequent calls to the function accumulates the effect. Fora
  example, calling (shear-y (/ PI 2)) and then (shear-y (/ PI 2)) is
  the same as (shear-y PI). If shear-y is called within the draw fn,
  the transformation is reset when the loop begins again. This
  function works in P2D or JAVA2D mode as shown in the example above.

  Technically, shear-y multiplies the current transformation matrix
  by a rotation matrix. This function can be further controlled by the
  push-matrix and pop-matrix fns."
  [angle]
  (.shearY *applet* (float angle)))

(def ^{:private true}
  p-shape-modes {:corner CORNER
                 :corners CORNERS
                 :center CENTER})

(defn ^{:requires-bindings true
        :processing-name "shapeMode()"
        :category "Shape"
        :subcategory "Loading & Displaying"
        :added "1.0"}
  shape-mode
  "Modifies the location from which shapes draw. Available modes are
  :corner, :corners and :center. Default is :corner.

  :corner  - specifies the location to be the upper left corner of the
             shape and uses the third and fourth parameters of shape
             to specify the width and height.

  :corners - uses the first and second parameters of shape to set
             the location of one corner and uses the third and fourth
             parameters to set the opposite corner.

  :center  - draws the shape from its center point and uses the third
             and forth parameters of shape to specify the width and
             height. "
  [mode]
  (let [mode (resolve-constant-key mode p-shape-modes)]
    (.shapeMode *applet* (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "shininess()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  shininess
  "Sets the amount of gloss in the surface of shapes. Used in
  combination with ambient, specular, and emissive in setting
  the material properties of shapes."
  [shine]
  (.shininess *applet* (float shine)))

(defn
  ^{:requires-bindings false
    :processing-name "sin()"
    :category "Math"
    :subcategory "Trigonometry"
    :added "1.0"}
  sin
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

  Note that smooth will also improve image quality of resized images."
  []
  (.smooth *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "specular()"
    :category "Lights, Camera"
    :subcategory "Material Properties"
    :added "1.0"}
  specular
  "Sets the specular color of the materials used for shapes drawn to
  the screen, which sets the color of hightlights. Specular refers to
  light which bounces off a surface in a perferred direction (rather
  than bouncing in all directions like a diffuse light). Used in
  combination with emissive, ambient, and shininess in setting
  the material properties of shapes."
  ([gray] (.specular *applet* (float gray)))
  ([gray alpha] (.specular *applet* (float gray) (float alpha)))
  ([x y z] (.specular *applet* (float x) (float y) (float z)))
  ([x y z a] (.specular *applet* (float x) (float y) (float z) (float a))))

(defn
  ^{:requires-bindings true
    :processing-name "sphere()"
    :category "Shape"
    :subcategory "3D Primitives"
    :added "1.0"}
  sphere
  "Genarates a hollow ball made from tessellated triangles."
  [radius] (.sphere *applet* (float radius)))

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
  this function. The setting stays active until sphere-detail is
  called again with a new parameter and so should not be called prior
  to every sphere statement, unless you wish to render spheres with
  different settings, e.g. using less detail for smaller spheres or
  ones further away from the camera. To controla the detail of the
  horizontal and vertical resolution independently, use the version of
  the functions with two parameters."
  ([res] (.sphereDetail *applet* (int res)))
  ([ures vres] (.sphereDetail *applet* (int ures) (int vres))))

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
  first time through the loop. The affect of the r, g, and b
  parameters is determined by the current color mode. The x, y, and z
  parameters specify the position of the light and nx, ny, nz specify
  the direction or light. The angle parameter affects angle of the
  spotlight cone."
  ([r g b x y z nx ny nz angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration))
  ([[r g b] [x y z] [nx ny nz] angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration)))

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
  (PApplet/sq (float a)))

(defn
  ^{:requires-bindings false
    :processing-name "sqrt()"
    :category "Math"
    :subcategory "Calculation"
    :added "1.0"}
  sqrt
  "Calculates the square root of a number. The square root of a number
  is always positive, even though there may be a valid negative
  root. The square root s of number a is such that (= a (* s s)) . It
  is the opposite of squaring."
  [a]
  (PApplet/sqrt (float a)))

(defn
  ^{:requires-bindings true
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke-float
  "Sets the color used to draw lines and borders around
  shapes. Converts all args to floats"
  ([gray] (.stroke *applet* (float gray)))
  ([gray alpha] (.stroke *applet* (float gray) (float alpha)))
  ([x y z] (.stroke *applet* (float x) (float y) (float z)))
  ([x y z a] (.stroke *applet* (float x) (float y) (float z) (float a))))

(defn
  ^{:requires-bindings true
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke-int
  "Sets the color used to draw lines and borders around
  shapes. Converts rgb to int and alpha to a float."
  ([rgb] (.stroke *applet* (int rgb)))
  ([rgb alpha] (.stroke *applet* (int rgb) (float alpha))))

(defn
  ^{:requires-bindings true
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke
  "Sets the color used to draw lines and borders around shapes. This
  color is either specified in terms of the RGB or HSB color depending
  on the current color-mode (the default color space is RGB, with
  each value in the range from 0 to 255)."
  ([rgb] (if (int-like? rgb) (stroke-int rgb) (stroke-float rgb)))
  ([rgb alpha] (if (int-like? rgb) (stroke-int rgb alpha) (stroke-float rgb alpha)))
  ([x y z] (stroke-float x y z))
  ([x y z a] (stroke-float x y z a)))

(def ^{:private true}
  stroke-cap-modes {:square SQUARE
                    :round ROUND
                    :project PROJECT
                    :model MODEL})


(defn
  ^{:requires-bindings true
    :processing-name "strokeCap()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  stroke-cap
  "Sets the style for rendering line endings. These ends are either
  squared, extended, or rounded and specified with the corresponding
  parameters :square, :project, and :round. The default cap is :round."
  [cap-mode]
  (let [cap-mode (resolve-constant-key cap-mode stroke-cap-modes)]
    (.strokeCap *applet* (int cap-mode))))

(def ^{:private true}
  stroke-join-modes {:miter PConstants/MITER
                     :bevel PConstants/BEVEL
                     :round PConstants/ROUND})

(defn
  ^{:requires-bindings true
    :processing-name "strokeJoin()"
    :category "Shape"
    :subcategory "Attributes"
    :added "1.0"}
  stroke-join
  "Sets the style of the joints which connect line
  segments. These joints are either mitered, beveled, or rounded and
  specified with the corresponding parameters :miter, :bevel, and
  :round. The default joint is :miter.

  This function is not available with the P2D, P3D, or OPENGL
  renderers (see bug report). More information about the renderers can
  be found in the size reference."
  [join-mode]
  (let [join-mode (resolve-constant-key join-mode stroke-join-modes)]
    (.strokeJoin *applet* (int join-mode))))

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
  (.strokeWeight *applet* (float weight)))

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
  (PApplet/tan (float angle)))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-char
  "Draws a char to the screen in the specified position. See text fn
  for more details."
  ([c] (.text *applet* (char c)))
  ([c x y] (.text *applet* (char c) (float x) (float y)))
  ([c x y z] (.text *applet* (char c) (float x) (float y) (float z))))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-num
  "Draws a number to the screen in the specified position. See text fn
  for more details."
  ([num x y] (.text *applet* (float num) (float x) (float y)))
  ([num x y z] (.text *applet* (float num) (float x) (float y) (float z))))

(defn
  ^{:requires-bindings true
    :processing-name "text()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text
  "Draws text to the screen in the position specified by the x and y
  parameters and the optional z parameter. A default font will be used
  unless a font is set with the text-font fn. Change the color of the
  text with the fill fn. The text displays in relation to the
  text-align fn, which gives the option to draw to the left, right, and
  center of the coordinates.

  The x1, y1, x2 and y2 (and optional z) parameters define a
  rectangular area to display within and may only be used with string
  data. For text drawn inside a rectangle, the coordinates are
  interpreted based on the current rect-mode setting.

  Use the text-mode function with the :screen parameter to display text
  in 2D at the surface of the window."
  ([^String s] (.text *applet* s))
  ([^String s x y] (.text *applet* s (float x) (float y)))
  ([^String s x y z] (.text *applet* s (float x) (float y) (float z)))
  ([^String s x1 y1 x2 y2] (.text *applet* s (float x1) (float y1) (float x2) (float y2)))
  ([^String s x1 y1 x2 y2 z] (.text *applet* s (float x1) (float y1) (float x2) (float y2) (float z))))

(def ^{:private true}
  horizontal-alignment-modes {:left LEFT
                              :center CENTER
                              :right RIGHT})

(defn ^{:private true}
  vertical-alignment-modes {:top TOP
                            :bottom BOTTOM
                            :center CENTER
                            :baseline BASELINE})

(defn
  ^{:requires-bindings true
    :processing-name "textAlign()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-align
  "Sets the current alignment for drawing text. Available modes are:

  horizontal - :left, :center, and :right
  vertical   - :top, :bottom, :center, and :baseline

  An optional second parameter specifies the vertical alignment
  mode. :baseline is the default. The :top and :center parameters are
  straightforward. The :bottom parameter offsets the line based on the
  current text-descent. For multiple lines, the final line will be
  aligned to the bottom, with the previous lines appearing above it.

  When using text with width and height parameters, :baseline is
  ignored, and treated as :top. (Otherwise, text would by default draw
  outside the box, since :baseline is the default setting. :baseline is
  not a useful drawing mode for text drawn in a rectangle.)

  The vertical alignment is based on the value of text-ascent, which
  many fonts do not specify correctly. It may be necessary to use a
  hack and offset by a few pixels by hand so that the offset looks
  correct. To do this as less of a hack, use some percentage of
  text-ascent or text-descent so that the hack works even if you
  change the size of the font."
  ([align]
     (let [align (resolve-constant-key align horizontal-alignment-modes)]
       (.textAlign *applet* (int align))))
  ([align-x align-y]
     (let [align-x (resolve-constant-key align-x horizontal-alignment-modes)
           align-y (resolve-constant-key align-y vertical-alignment-modes)]
       (.textAlign *applet* (int align-x) (int align-y)))))

(defn
  ^{:requires-bindings true
    :processing-name "textAscent()"
    :category "Typography"
    :subcategory "Metrics"
    :added "1.0"}
  text-ascent
  "Returns the ascent of the current font at its current size. This
  information is useful for determining the height of the font above
  the baseline. For example, adding the text-ascent and text-descent
  values will give you the total height of the line."
  []
  (.textAscent *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "textDescent()"
    :category "Typography"
    :subcategory "Metrics"
    :added "1.0"}
  text-descent
  "Returns descent of the current font at its current size. This
  information is useful for determining the height of the font below
  the baseline. For example, adding the text-ascent and text-descent
  values will give you the total height of the line."
  []
  (.textDescent *applet*))

(defn
  ^{:requires-bindings true
    :processing-name "textFont()"
    :category "Typography"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  text-font
  "Sets the current font that will be drawn with the text
  function. Fonts must be loaded with load-font before it can be
  used. This font will be used in all subsequent calls to the text
  function. If no size parameter is input, the font will appear at its
  original size until it is changed with text-size.

  Because fonts are usually bitmaped, you should create fonts at the
  sizes that will be used most commonly. Using textFont without the
  size parameter will result in the cleanest-looking text.

  With the default (JAVA2D) and PDF renderers, it's also possible to
  enable the use of native fonts via the command
  (hint :enable-native-fonts). This will produce vector text in JAVA2D
  sketches and PDF output in cases where the vector data is available:
  when the font is still installed, or the font is created via the
  create-font fn"
  ([^PFont font] (.textFont *applet* font))
  ([^PFont font size] (.textFont *applet* font (int size))))

(defn
  ^{:requires-bindings true
    :processing-name "textLeading()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-leading
  "Sets the spacing between lines of text in units of pixels. This
  setting will be used in all subsequent calls to the text function."
  [leading]
  (.textLeading *applet* (float leading)))

(def ^{:private true}
  text-modes {:model PConstants/MODEL
              :shape PConstants/SHAPE
              :screen PConstants/SCREEN})

(defn
  ^{:requires-bindings true
    :processing-name "textMode()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-mode
  "Sets the way text draws to the screen - available modes
  are :model, :shape and :screen

  In the default configuration (the :model mode), it's possible to
  rotate, scale, and place letters in two and three dimensional space.

  Changing to :screen mode draws letters directly to the front of the
  window and greatly increases rendering quality and speed when used
  with the P2D and P3D renderers. :screen with OPENGL and JAVA2D (the
  default) renderers will generally be slower, though pixel accurate
  with P2D and P3D. With :screen, the letters draw at the actual size
  of the font (in pixels) and therefore calls to text-size will not
  affect the size of the letters. To create a font at the size you
  desire, use create-font. When using :screen, any z-coordinate passed
  to a text command will be ignored, because your computer screen
  is...flat!

  The :shape mode draws text using the the glyph outlines of
  individual characters rather than as textures. This mode is only
  only supported with the PDF and OPENGL renderer settings. With the
  PDF renderer, you must specify the :shape text-mode before any other
  drawing occurs. If the outlines are not available, then
  :shape will be ignored and :model will be used instead.

  The :shape option in OPENGL mode can be combined with begin-raw to
  write vector-accurate text to 2D and 3D output files, for instance
  DXF or PDF. :shape is not currently optimized for OPENGL, so if
  recording shape data, use :model until you're ready to capture the
  geometry with begin-raw."
  [mode]
  (let [mode (resolve-constant-key mode text-modes)]
    (.textMode *applet* (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "textSize()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-size
  "Sets the current font size. This size will be used in all
  subsequent calls to the text fn. Font size is measured in
  units of pixels."
  [size]
  (.textSize *applet* (float size)))

(defn
  ^{:requires-bindings true
    :processing-name "texture()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  texture
  "Sets a texture to be applied to vertex points. The texture fn must
  be called between begin-shape and end-shape and before any calls to
  vertex.

  When textures are in use, the fill color is ignored. Instead, use
  tint to specify the color of the texture as it is applied to the
  shape."
  [^PImage img]
  (.texture *applet* img))

(def ^{:private true}
  texture-modes {:image IMAGE
                :normalized NORMALIZED})

(defn
    ^{:requires-bindings true
    :processing-name "textureMode()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  texture-mode
  "Sets the coordinate space for texture mapping. There are two
  options, :image and :normalized.

  :image refers to the actual coordinates of the image and :normalized
  refers to a normalized space of values ranging from 0 to 1. The
  default mode is :image. In :image, if an image is 100 x 200 pixels,
  mapping the image onto the entire size of a quad would require the
  points (0,0) (0,100) (100,200) (0,200). The same mapping in
  NORMAL_SPACE is (0,0) (0,1) (1,1) (0,1)."
  [mode]
  (let [mode (resolve-constant-key mode texture-modes)]
    (.textureMode *applet* (int mode))))

(defn
  ^{:requires-bindings true
    :processing-name "textWidth()"
    :category "Typography"
    :subcategory "Attributes"
    :added "1.0"}
  text-width
  "Calculates and returns the width of any character or text string."
  [data]
  (let [data  (if (= (class data) (class \a))
                (char data)
                (str data))]
    (.textWidth *applet* data)))

(defn
  ^{:requires-bindings true
    :processing-name "tint()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  tint-float
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

(defn
  ^{:requires-bindings true
    :processing-name "tint()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  tint-int
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

(defn
  ^{:requires-bindings true
    :processing-name "tint()"
    :category "Image"
    :subcategory "Loading & Displaying"
    :added "1.0"}
  tint
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

(defn
  ^{:requires-bindings true
    :processing-name "translate()"
    :category "Transform"
    :subcategory nil
    :added "1.0"}
  translate
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
  (.triangle *applet*
             (float x1) (float y1)
             (float x2) (float y2)
             (float x3) (float y3)))


(defn
  ^{:requires-bindings true
    :processing-name "updatePixels()"
    :category "Image"
    :subcategory "Pixels"
    :added "1.0"}
  update-pixels
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

(defn
  ^{:requires-bindings true
    :processing-name "vertex()"
    :category "Shape"
    :subcategory "Vertex"
    :added "1.0"}
  vertex
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

(defn
  ^{:requires-bindings false
    :processing-name "year()"
    :category "Input"
    :subcategory "Time & Date"
    :added "1.0"}
  year
  "Returns the current year as an integer (2003, 2004, 2005, etc)."
  []
  (PApplet/year))

(defn
  ^{:requires-bindings true
    :processing-name "width"
    :category "Environment"
    :subcategory nil
    :added "1.0"}
  width
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

;;; doc utils

(def ^{:private true}
  processing-fn-metas
  "Returns a seq of metadata maps for all fns related to the original
  Processing API (but may not have a direct Processing API equivalent)."
  (map #(-> % second meta) (ns-publics *ns*)))

(defn- processing-fn-metas-with-orig-method-name
  "Returns a seq of metadata maps for all fns with a corresponding
  Processing API method."
  []
  (filter :processing-name processing-fn-metas))

(defn- matching-processing-methods
  "Takes a string representing the start of a method name in the
  original Processing API and returns a map of orig/new-name pairs"
  [orig-name]
  (let [metas   (processing-fn-metas-with-orig-method-name)
        matches (filter #(.startsWith (:processing-name %) orig-name) metas)]
    (into {} (map (fn [{:keys [name processing-name]}]
                    [(str processing-name) (str name)])
                  matches))))

(defn- find-categories
  []
  (let [metas processing-fn-metas
        cats  (into (sorted-set) (map :category metas))]
    (clojure.set/difference cats #{nil})))

(defn- find-subcategories
  [category]
  (let [metas  processing-fn-metas
        in-cat (filter #(= category (:category %)) metas)
        res    (into (sorted-set) (map :subcategory in-cat))]
    (clojure.set/difference res #{nil})))

(defn- find-fns
  "Find the names of fns in category cat that don't also belong to a
  subcategory"
  [cat subcat]
  (let [metas processing-fn-metas
        res   (filter (fn [m]
                        (and (= cat (:category m))
                             (= subcat (:subcategory m))))
                      metas)]
    (sort (map :name res))))

(defn- subcategory-map
  [cat cat-idx]
  (let [subcats (find-subcategories cat)
        idxs    (map inc (range))
        idxd-subcats (map (fn [idx subcat]
                            [(str cat-idx "." idx) subcat])
                          idxs subcats)]
    (into {}
          (map (fn [[idx subcat]]
                 [idx {:name subcat
                       :fns (find-fns cat subcat)}])
               idxd-subcats))))

(defn- sorted-category-map
  []
  (let [cats      (find-categories)
        idxs      (map inc (range))
        idxd-cats (into {} (map (fn [idx cat] [idx cat]) idxs cats))]
    (into (sorted-map)
          (map (fn [[idx cat]]
                 [idx  {:name cat
                        :fns (find-fns cat nil)
                        :subcategories (subcategory-map cat idx)}])
               idxd-cats))))

(defn- all-category-map
  []
  (reduce (fn [sum [k v]]
            (merge sum (:subcategories v)))
          (into {} (map (fn [[k v]] [(str k) v]) (sorted-category-map)))
          (sorted-category-map)))

(defn doc-cats
  "Print out a list of all the categories and subcategories,
  associated index nums and fn count (in parens)."
  []
  (let [cats (sorted-category-map)]
    (dorun
     (map (fn [[cat-idx cat]]
            (println cat-idx
                     (:name cat)
                     (str "(" (count (find-fns (:name cat) nil)) ")"))
            (dorun (map (fn [[subcat-idx subcat]]
                          (println "  "
                                   subcat-idx
                                   (:name subcat)
                                   (str "(" (count (find-fns (:name cat) (:name subcat))) ")")))
                        (:subcategories cat))))
          cats))))

(defn doc-fns
  "Print all the functions within category or subcategory specified by
  cat-idx (use print-cats to see a list of index nums). If a category is
  specified, it will not print out the fns in any of cat's
  subcategories. "
  [cat-idx]
  (let [res (get (all-category-map) (str cat-idx))]
    (println (:name res))
    (dorun
     (map #(println "  -" %) (:fns res)))))

(defn doc-meths
  "Takes a string representing the start of a method name in the
  original Processing API and prints out all matches alongside the
  Processing-core equivalent."
  [orig-name]
  (let [res (matching-processing-methods orig-name)]
    (print-definition-list res))
  nil)

;;; Useful trig constants
(def PI  (float Math/PI))
(def HALF-PI    (/ PI (float 2.0)))
(def THIRD-PI   (/ PI (float 3.0)))
(def QUARTER-PI (/ PI (float 4.0)))
(def TWO-PI     (* PI (float 2.0)))

(def DEG-TO-RAD (/ PI (float 180.0)))
(def RAD-TO-DEG (/ (float 180.0) PI))
