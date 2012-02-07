;; simple processing wrapper for Clojure
;; Roland Sadowski [szabla gmail com]

;; Copyright (c) 2008 Roland Sadowski. All rights reserved.  The use and
;; distribution terms for this software are covered by the Common
;; Public License 1.0 (http://www.opensource.org/licenses/cpl1.0.php)
;; which can be found in the file CPL.TXT at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software.

(ns rosado.processing
  (:import (processing.core PApplet PImage PGraphics PFont))
  (:load "constants"))

;; used by functions in this lib. Use binding to set it
;; to an instance of processing.core.PApplet
(def ^{:dynamic true} ^PApplet *applet*)

(def toupper (memfn toUpperCase))

(defn tosymb [kw]
  (-> kw name toupper symbol))

(defn abs-int
  "Calculates the absolute value (magnitude) of a number. The absolute value of
  a number is always positive. Returns an int."
  [n]
  (PApplet/abs (int n)))

(defn abs-float
  "Calculates the absolute value (magnitude) of a number. The absolute value of
  a number is always positive. Returns a float."
  [n]
  (PApplet/abs (float n)))

(def abs abs-float)

(defn acos
  "The inverse of cos, returns the arc cosine of a value. This function expects
  the values in the range of -1 to 1 and values are returned in the range 0 to
  Math/PI (3.1415927)."
  [n]
  (PApplet/acos n))

;; $$addListeners

(defn alpha
  "Extracts the alpha value from a color."
  [color] (.alpha *applet* (int color)))

(defn ambient
  "Sets the ambient reflectance for shapes drawn to the screen. This is combined
  with the ambient light component of environment. The color components set
  through the parameters define the reflectance. For example in the default
  color mode, setting x=255, y=126, z=0, would cause all the red light to
  reflect and half of the green light to reflect. Used in combination with
  emissive, specular, and shininess in setting the material properties of
  shapes."
  ([gray] (.ambient *applet* (float gray)))
  ([x y z] (.ambient *applet* (float x) (float y) (float z))))

(defn ambient-int
  "Sets the ambient reflectance for shapes drawn to the screen. This is combined
  with the ambient light component of environment. The rgb color components set
  define the reflectance. Used in combination with emissive, specular, and
  shininess in setting the material properties of shapes."
  [rgb]
  (.ambient
   *applet* (int rgb)))

(defn ambient-light
  "Adds an ambient light. Ambient light doesn't come from a specific direction,
   the rays have light have bounced around so much that objects are evenly lit
   from all sides. Ambient lights are almost always used in combination with
   other types of lights. Lights need to be included in the draw to remain
   persistent in a looping program. Placing them in the setup of a looping
   program will cause them to only have an effect the first time through the
   loop. The effect of the parameters is determined by the current color mode."
  ([red green blue]
     (.ambientLight *applet* (float red) (float green) (float blue)))
  ([red green blue x y z]
     (.ambientLight *applet* (float red) (float green) (float blue)
                    (float x) (float y) (float z))))

;; $$append

(defn apply-matrix
  "Multiplies the current matrix by the one specified through the
  parameters. This is very slow because it will try to calculate the inverse of
  the transform, so avoid it whenever possible. The equivalent function in
  OpenGL is glMultMatrix()."
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
  "Draws an arc in the display window. Arcs are drawn along the outer edge of an
  ellipse defined by the x, y, width and height parameters. The origin or the
  arc's ellipse may be changed with the ellipseMode() function. The start and
  stop parameters specify the angles at which to draw the arc."
  [x y width height start stop]
  (.arc *applet* (float x)(float y) (float width) (float height)
        (float start) (float stop)))

;; $$arraycopy

(defn asin
  "The inverse of sin, returns the arc sine of a value. This function expects
  the values in the range of -1 to 1 and values are returned in the range -PI/2
  to PI/2."
  [n]
  (PApplet/asin (float n)))

(defn atan
  "The inverse of tan, returns the arc tangent of a value. This function expects
  the values in the range of -Infinity to Infinity (exclusive) and values are
  returned in the range -PI/2 to PI/2 ."
  [n]
  (PApplet/atan (float n)))

(defn atan2
  "Calculates the angle (in radians) from a specified point to the coordinate
  origin as measured from the positive x-axis. Values are returned as a float in
  the range from PI to -PI. The atan2 function is most often used for
  orienting geometry to the position of the cursor. Note: The y-coordinate of
  the point is the first parameter and the x-coordinate is the second due the
  the structure of calculating the tangent."
  [y x]
  (PApplet/atan2 (float y) (float x)))

(defn background-float
  "Sets the color used for the background of the Processing window. The default
  background is light gray. In the draw function, the background color is used
  to clear the display window at the beginning of each frame.

  It is not possible to use transparency (alpha) in background colors with the
  main drawing surface, however they will work properly with create-graphics."
  ([gray] (.background *applet* (float gray)))
  ([gray alpha] (.background *applet* (float gray) (float alpha)))
  ([r g b] (.background *applet* (float r) (float g) (float b)))
  ([r g b a] (.background *applet* (float r) (float g) (float b) (float a))))

(defn background-int
  "Sets the color used for the background of the Processing window. The default
  background is light gray. In the draw function, the background color is used
  to clear the display window at the beginning of each frame.

  It is not possible to use transparency (alpha) in background colors with the
  main drawing surface, however they will work properly with create-graphics."
  ([rgb] (.background *applet* (int rgb)))
  ([rgb alpha] (.background *applet* (int rgb) (float alpha))))

(def ^{:arglists (:arglists (meta #'background-float))
       :doc (:doc (meta #'background-float))}
  background background-float)


(defn background-image
  "Specify an image to be used as the background for a sketch. Its width and
  height must be the same size as the sketch window. Images used as background
  will ignore the current tint setting."
  [^PImage img]
  (.background *applet* img))

(defn begin-camera
  "Sets the matrix mode to the camera matrix so calls such as translate, rotate,
  apply-matrix and reset-matrix affect the camera. begin-camera should always be
  used with a following end-camera and pairs of begin-camera and end-camera
  cannot be nested.

  For most situations the camera function will be sufficient."
  []
  (.beginCamera *applet*))

(defn begin-raw
  "Enables the creation of vectors from 3D data. Requires corresponding end-raw
  command. These commands will grab the shape data just before it is rendered to
  the screen. At this stage, your entire scene is nothing but a long list of
  individual lines and triangles. This means that a shape created with sphere
  method will be made up of hundreds of triangles, rather than a single
  object. Or that a multi-segment line shape (such as a curve) will be rendered
  as individual segments."
  ([^PGraphics raw-gfx] (.beginRaw *applet* raw-gfx))
  ([^String renderer ^String filename]
     (.beginRaw *applet* renderer filename)))

;; $$beginRecord

(def ^{:private true}
     shapes-map {:points POINTS
                 :lines LINES
                 :triangles TRIANGLES
                 :triangle-strip TRIANGLE_STRIP
                 :triangle-fan TRIANGLE_FAN
                 :quads QUADS
                 :quad-strip QUAD_STRIP})

(defn- resolve-shape-mode
  [mode]
  (if (keyword? mode)
    (get shapes-map mode)
    mode))

(defn begin-shape
  "Enables the creation of complex forms. begin-shape begins recording vertices
  for a shape and end-shape stops recording. Use the mode keyword to specify
  which shape create from the provided vertices. With no mode specified, the
  shape can be any irregular polygon.

  The available mode keywords are :points, :lines, :triangles, :triangle-fan,
                                  :triangle-strip, :quads, :quad-strip.


  After calling the begin-shape function, a series of vertex commands must
  follow. To stop drawing the shape, call end-shape. The vertex function with
  two parameters specifies a position in 2D and the vertex function with three
  parameters specifies a position in 3D. Each shape will be outlined with the
  current stroke color and filled with the fill color.

  Transformations such as translate, rotate, and scale do not work within
  begin-shape. It is also not possible to use other shapes, such as ellipse
  or rect within begin-shape."
  ([] (.beginShape *applet*))
  ([mode]
     (let [mode (resolve-shape-mode  mode)]
       (.beginShape *applet* (int mode)))))

(defn bezier
  "Draws a Bezier curve on the screen. These curves are defined by a series of
  anchor and control points. The first two parameters specify the first anchor
  point and the last two parameters specify the other anchor point. The middle
  parameters specify the control points which define the shape of the curve."
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
  "Sets the resolution at which Beziers display. The default value is 20. This
  function is only useful when using the P3D or OPENGL renderer as the
  default (JAVA2D) renderer does not use this information."
  [detail]
  (.bezierDetail *applet* (int detail)))

(defn bezier-point
  "Evaluates the Bezier at point t for points a, b, c, d. The parameter t varies
  between 0 and 1, a and d are points on the curve, and b and c are the control
  points. This can be done once with the x coordinates and a second time with
  the y coordinates to get the location of a bezier curve at t."
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
  "Specifies vertex coordinates for Bezier curves. Each call to bezier-vertex
  defines the position of two control points and one anchor point of a Bezier
  curve, adding a new segment to a line or shape. The first time bezier-vertex
  is used within a begin-shape call, it must be prefaced with a call to
  vertex to set the first anchor point. This function must be used between
  begin-shape and end-shape and only when there is no parameter specified
  to begin-shape."
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

;; $$binary

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
             :hard-light HARD_LIGHT
             :soft-light SOFT_LIGHT
             :dodge DODGE
             :burn BURN})

(defn- resolve-blend-mode
  [mode]
  (if (keyword? mode)
    (get blend-map mode)
    mode))

(defn blend
  "Blends a region of pixels from one image into another (or in itself again)
  with full alpha channel support.

  Available blend modes are:

  :blend      - linear interpolation of colours: C = A*factor + B
  :add        - additive blending with white clip: C = min(A*factor + B, 255)
  :subtract   - subtractive blending with black clip: C = max(B - A*factor, 0)
  :darkest    - only the darkest colour succeeds: C = min(A*factor, B)
  :lightest   - only the lightest colour succeeds: C = max(A*factor, B)
  :difference - subtract colors from underlying image.
  :exclusion  - similar to :difference, but less extreme.
  :multiply   - Multiply the colors, result will always be darker.
  :screen     - Opposite multiply, uses inverse values of the colors.
  :overlay    - A mix of :multiply and :screen. Multiplies dark values, and
                screens light values.
  :hard-light - :screen when greater than 50% gray, :multiply when lower.
  :soft-light - Mix of :darkest and :lightest. Works like :overlay, but not as
                harsh.
  :dodge      - Lightens light tones and increases contrast, ignores darks.
                Called \"Color Dodge\" in Illustrator and Photoshop.
  :burn       - Darker areas are applied, increasing contrast, ignores lights.
                Called \"Color Burn\" in Illustrator and Photoshop."
  ([x y width height dx dy dwidth dheight mode]
     (let [mode (resolve-mode mode)]
       (.blend *applet* (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode))))
  ([^PImage src x y width height dx dy dwidth dheight mode]
     (let [mode (resolve-mode mode)]
       (.blend *applet* src (int x) (int y) (int width) (int height)
               (int dx) (int dy) (int dwidth) (int dheight) (int mode)))))

(defn blend-color
  "Blends two color values together based on the blending mode given specified
  with the mode keyword.

  Available blend modes are:

  :blend      - linear interpolation of colours: C = A*factor + B
  :add        - additive blending with white clip: C = min(A*factor + B, 255)
  :subtract   - subtractive blending with black clip: C = max(B - A*factor, 0)
  :darkest    - only the darkest colour succeeds: C = min(A*factor, B)
  :lightest   - only the lightest colour succeeds: C = max(A*factor, B)
  :difference - subtract colors from underlying image.
  :exclusion  - similar to :difference, but less extreme.
  :multiply   - Multiply the colors, result will always be darker.
  :screen     - Opposite multiply, uses inverse values of the colors.
  :overlay    - A mix of :multiply and :screen. Multiplies dark values, and
                screens light values.
  :hard-light - :screen when greater than 50% gray, :multiply when lower.
  :soft-light - Mix of :darkest and :lightest. Works like :overlay, but not as
                harsh.
  :dodge      - Lightens light tones and increases contrast, ignores darks.
                Called \"Color Dodge\" in Illustrator and Photoshop.
  :burn       - Darker areas are applied, increasing contrast, ignores lights.
                Called \"Color Burn\" in Illustrator and Photoshop."
  [c1 c2 mode]
  (let [mode (resolve-mode mode)]
    (PApplet/blendColor (int c1) (int c2) (int mode))))

(defn blue
  "Extracts the blue value from a color, scaled to match current color-mode.
  Returns a float. "
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
  "Sets the position of the camera through setting the eye position, the center
  of the scene, and which axis is facing upward. Moving the eye position and
  the direction it is pointing (the center of the scene) allows the images to be
  seen from different angles. The version without any parameters sets the camera
  to the default position, pointing to the center of the display window with the
  Y axis as up. The default values are:

  eyeX:     (/ (width) 2.0)
  eyeY:     (/ (height) 2.0)
  eyeZ:     (/ (/ (height) 2.0) (tan (/ (* Math/PI 60.0) 360.0)))
  centerX:  (/ (width) 2.0)
  centerY:  (/ (height) 2.0)
  centerZ:  0
  upX:      0
  upY:      1
  upZ:      0

  Similar imilar to gluLookAt() in OpenGL, but it first clears the current
  camera settings."
  ([] (.camera *applet*))
  ([eyeX eyeY eyeZ centerX centerY centerZ upX upY upZ]
     (.camera *applet* (float eyeX) (float eyeY) (float eyeZ)
              (float centerX) (float centerY) (float centerZ)
              (float upX) (float upY) (float upZ))))

(defn can-draw? [] (.canDraw *applet*))

(defn ceil
  "Calculates the closest int value that is greater than or equal to the value
  of the parameter. For example, (ceil 9.03) returns the value 10."
  [n]
  (PApplet/ceil (float n)))

(defn color-float
  ([gray] (.color *applet* (float gray)))
  ([gray alpha] (.color *applet* (float gray) (float alpha)))
  ([r g b] (.color *applet* (float r) (float g) (float b)))
  ([r g b a] (.color *applet* (float r) (float g) (float b) (float a))))

(defn color-int
  ([gray] (.color *applet* (int gray)))
  ([gray alpha] (.color *applet* (int gray) (float alpha)))
  ([r g b] (.color *applet* (int r) (int g) (int b)))
  ([r g b a] (.color *applet* (int r) (int g) (int b) (int a))))

(def color color-float)

(defn color-mode
  ([mode] (.colorMode *applet* (int mode)))
  ([mode max] (.colorMode *applet* (int mode) (float max)))
  ([mode max-x max-y max-z]
     (.colorMode *applet* (int mode) (float max-x) (float max-y) (float max-z)))
  ([mode max-x max-y max-z max-a]
     (.colorMode *applet* (int mode)
                 (float max-x) (float max-y) (float max-z) (float max-a))))

;; $$concat

(defn constrain-float
  [amt low high]
  (PApplet/constrain (float amt) (float low) (float high)))

(defn constrain-int
  [amt low high]
  (PApplet/constrain (int amt) (int low) (int high)))

(def constrain constrain-float)

(defn copy
  ([[sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
     (.copy *applet* (int sx1) (int sy1) (int sx2) (int sy2)
            (int dx1) (int dy1) (int dx2) (int dy2)))
  ([^PImage img [sx1 sy1 sx2 sy2] [dx1 dy1 dx2 dy2]]
     (.copy *applet* img (int sx1) (int sy1) (int sx2) (int sy2)
            (int dx1) (int dy1) (int dx2) (int dy2))))

(defn cos [angle] (PApplet/cos (float angle)))

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

(defn cursor
  ([] (.cursor *applet*))
  ([cur-type] (.cursor *applet* (int cur-type))))

(defn cursor-image
  ([^PImage img] (.cursor *applet* img))
  ([^PImage img hx hy] (.cursor *applet* img (int hx) (int hy))))

(defn curve
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

(defn curve-detail [detail] (.curveDetail *applet* (int detail)))

(defn curve-point
  [a b c d t]
  (.bezierPoint *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tangent
  [a b c d t]
  (.curveTangent *applet* (float a) (float b) (float c) (float d) (float t)))

(defn curve-tightness [ti]
  (.curveTightness *applet* (float ti)))

(defn curve-vertex
  ([x y] (.curveVertex *applet* (float x) (float y)))
  ([x y z] (.curveVertex *applet* (float x) (float y) (float z))))

;; $$dataFile
;; $$dataPath

(defn day
  "Get the current day of the month (1 through 31)."
  []
  (PApplet/day))

(defn degrees [radians] (PApplet/degrees (float radians)))

(defn delay-frame [nap-time] (.delay *applet* (int nap-time)))

(defn destroy [] (.destroy *applet*))

;; $$die

(defn directional-light
  [r g b nx ny nz]
  (.directionalLight *applet* (float r) (float g) (float b)
                     (float nx) (float ny) (float nz)))

(defn displayable? [] (.displayable *applet*))

(defn dist
  ([a b x y] (PApplet/dist (float a) (float b) (float x) (float y)))
  ([a b c x y z] (PApplet/dist (float a) (float b) (float c)
                               (float x) (float y) (float z))))

;; $$draw

(defn ellipse
  [a b c d]
  (.ellipse *applet* (float a) (float b) (float c) (float d)))

(defmacro ellipse-mode [mode]
  "Takes a keyword argument; :center, :radius, :corner or :corners."
  (let [mode (tosymb mode)]
    `(.ellipseMode *applet* (int ~mode))))

(defn emissive-float
  ([gray] (.emissive *applet* (float gray)))
  ([x y z] (.emissive *applet* (float x) (float y) (float z))))

(defn emissive-int
  [gray] (.emissive *applet* (int gray)))

(def emissive emissive-float)

(defn end-camera [] (.endCamera *applet*))

(defn end-raw [] (.endRaw *applet*))

(defmacro end-shape
  "Takes an optinal :close keyword as argument."
  ([] `(.endShape *applet*))
  ([kind]
     (let [kind (tosymb kind)]
       `(.endShape *applet* (int ~kind)))))

;; $$exec

(defn exit [] (.exit *applet*))

(defn exp [a] (PApplet/exp (float a)))

;; $$expand

(defn fill-float
  ([gray] (.fill *applet* (float gray)))
  ([gray alpha] (.fill *applet* (float gray) (float alpha)))
  ([x y z] (.fill *applet* (float x) (float y) (float z)))
  ([x y z a] (.fill *applet* (float x) (float y) (float z) (float a))))

(defn fill-int
  ([rgb] (.fill *applet* (int rgb)))
  ([rgb alpha] (.fill *applet* (int rgb) (float alpha))))

(def fill fill-float)

(defn filter-kind
  ([kind] (.filter *applet* (int kind)))
  ([kind param] (.filter *applet* (int kind) (float param))))

;; $$focusGained
;; $$focusLost

(defn frame-count [] (.frameCount *applet*))

(defn framerate [new-rate] (.frameRate *applet* (float new-rate)))

(def frame-rate framerate)

(defn frustum
  [l r b t near far]
  (.frustum *applet* (float l) (float r) (float b) (float t)
            (float near) (float far)))

(defn get-pixel
  ([] (.get *applet*))
  ([x y] (.get *applet* (int x) (int y)))
  ([x y w h] (.get *applet* (int x) (int y) (int w) (int h))))

(defn green [what] (.green *applet* (int what)))

;; $$handleDraw
;; $$hex

(defn height
  "Height of the display window. The value of height is zero until size() is called."
  []
  (.getHeight *applet*))

(defn hint [which] (.hint *applet* (int which)))

(defn hour [] (PApplet/hour))

(defn hue [what] (.hue *applet* (int what)))

(defn image
  ([^PImage img x y] (.image *applet* img (float x) (float y)))
  ([^PImage img x y c d] (.image *applet* img (float x) (float y)
                                  (float c) (float d)))
  ([^PImage img x y c d u1 v1 u2 v2]
     (.image *applet* img (float x) (float y) (float c) (float d)
             (float u1) (float v1) (float u2) (float v2))))

(defn image-mode [mode] (.imageMode *applet* (int mode)))

;; $$init
;; $$insertFrame
;; $$join
;; $$keyPressed
;; $$keyReleased
;; $$keyTyped
;; $$lerp
;; $$lerpColor
;; $$lightFallof

(defn lights [] (.lights *applet*))

(defn light-specular
  [x y z]
  (.lightSpecular *applet* (float x) (float y) (float z)))

(defn line
  "Draws a line (a direct path between two points) to the screen. The version of
  line with four parameters draws the line in 2D. To color a line, use the
  stroke function. A line cannot be filled, therefore the fill method will
  not affect the color of a line. 2D lines are drawn with a width of one pixel
  by default, but this can be changed with the stroke-weight function. The
  version with six parameters allows the line to be placed anywhere within XYZ
  space. "
  ([p1 p2] (apply line (concat p1 p2)))
  ([x1 y1 x2 y2] (.line *applet* (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2]
     (.line *applet* (float x1) (float y1) (float z1)
            (float x2) (float y2) (float z2))))

;; $$link

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

;; $$log

(defn start-loop [] (.loop *applet*))

;; $$mag
;; $$main

(defn map-to [val istart istop ostart ostop]
  (PApplet/map (float val) (float istart) (float istop) (float ostart) (float ostop)))

(defn map-to-double [val istart istop ostart ostop]
  (PApplet/map (double val) (double istart) (double istop) (double ostart) (double ostop)))

(defn mask
  ([^ints alpha-array] (.mask *applet* alpha-array)))

(defn mask-image [^PImage img] (.mask *applet* img))

;; $$match
;; $$max

(defn millis [] (.millis *applet*))

;; $$min

(defn minute [] (PApplet/minute))

(defn model-x [x y z] (.modelX *applet* (float x) (float y) (float z)))
(defn model-y [x y z] (.modelY *applet* (float x) (float y) (float z)))
(defn model-z [x y z] (.modelZ *applet* (float x) (float y) (float z)))

(defn month [] (PApplet/month))

;; $$mouseClicked
;; $$mouseDragged
;; $$mouseEntered
;; $$mouseExited
;; $$mouseMoved
;; $$mousePressed
;; $$mouseReleased
;; $$nf
;; $$nfc
;; $$nfp
;; $$nfs

(defn no-cursor [] (.noCursor *applet*))

(defn no-fill [] (.noFill *applet*))

(defn noise
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

(defn no-stroke [] (.noStroke *applet*))

(defn no-tint [] (.noTint *applet*))

(defn open [^String filename] (PApplet/open filename))

;; $$open -- overload

(defn ortho
  ([] (.ortho *applet*))
  ([l r b t near far] (.ortho *applet* (float l) (float r) (float b)
                              (float t) (float near) (float far))))

;; $$paint
;; $$param
;; $$parseBoolean
;; $$parseByte
;; $$parseChar
;; $$parseFloat
;; $$parseInt

(defn perspective
  ([] (.perspective *applet*))
  ([fovy aspect z-near z-far]
     (.perspective *applet* (float fovy) (float aspect)
                   (float z-near) (float z-far))))

(defn point
  ([x y] (.point *applet* (float x)(float y)))
  ([x y z] (.point *applet* (float x) (float y) (float z))))

(defn point-light
  [r g b x y z]
  (.pointLight *applet* r g b x y z))

(defn pop-matrix [] (.popMatrix *applet*))

(defn pow [a b] (PApplet/pow (float a) (float b)))

;; $$print

(defn print-camera [] (.printCamera *applet*))

;; $$println

(defn print-matrix [] (.printMatrix *applet*))

(defn print-projection [] (.printProjection *applet*))

(defn push-matrix [] (.pushMatrix *applet*))

(defn quad
  [x1 y1 x2 y2 x3 y3 x4 y4]
  (.quad *applet* x1 y1 x2 y2 x3 y3 x4 y4))

(defn radians [deg] (PApplet/radians (float deg)))

(defn random
  ([max] (.random *applet* (float max)))
  ([min max] (.random *applet* (float min) (float max))))


(defn random-seed [w] (.randomSeed *applet* (float w)))

(defn rect [x1 y1 x2 y2]
  (.rect *applet* (float x1) (float y1) (float x2) (float y2)))

(defn rect-mode [mode] (.rectMode *applet* (int mode)))

(defn red [what] (.red *applet* (int what)))

(defn redraw [] (.redraw *applet*))

;; $$registerDispose
;; $$registerDraw
;; $$reqisterKeyEvent
;; $$registerMouseEvent
;; $$registerPost
;; $$registerPre
;; $$registerSize
;; $$registerSize

(defn request-image
  ([filename] (.requestImage *applet* filename))
  ([filename extension] (.requestImage *applet* filename extension)))

(defn reset-matrix [] (.resetMatrix *applet*))

(defn reverse-array [arr] (PApplet/reverse arr))

(defn rotate
  ([angle] (.rotate *applet* (float angle)))
  ([angle vx vy vz] (.rotate *applet* (float angle)
                             (float vx) (float vy) (float vz))))

(defn rotate-x [angle] (.rotateX *applet* (float angle)))

(defn rotate-y [angle] (.rotateY *applet* (float angle)))

(defn rotate-z [angle] (.rotateZ *applet* (float angle)))

(defn round [what] (PApplet/round (float what)))

;; $$run

(defn saturation [what] (.saturation *applet* (int what)))

(defn save [filename] (.save *applet* filename))

;; $$saveBytes
;; $$saveFile

(defn save-frame
  ([] (.saveFrame *applet*))
  ([what] (.saveFrame *applet*)))

;; $$savePath
;; $$saveStream
;; $$saveStrings

(defn scale
  ([s] (.scale *applet* (float s)))
  ([sx sy] (.scale *applet* (float sx) (float sy))))

(defn screen-x
  ([x y] (.screenX *applet* (float x) (float y)))
  ([x y y] (.screenX *applet* (float x) (float y))))

(defn screen-y
  ([x y] (.screenY *applet* (float x) (float y)))
  ([x y z] (.screenY *applet* (float x) (float y) (float z))))

(defn screen-z
  [x y z] (.screenX *applet* (float x) (float y) (float z)))

(defn seconds [] (PApplet/second))

;; $$selectFolder
;; $$selectInput
;; $$selectOutput

(defn set-pixel
  [x y c] (.set *applet* (int x) (int y) (int c)))

(defn set-image-at
  [dx dy ^PImage src] (.set *applet* (int dx) (int dy) src))

;; $$setup

;; $$setupExternalMessages
;; $$setupFrameListener

(defn shininess [shine] (.shininess *applet* (float shine)))

;; $$shorten

(defn sin [angle] (PApplet/sin (float angle)))

(defn size
  "Defines the dimension of the display window in units of pixels."
  ([width height] (.size *applet* (int width) (int height)))
  ([width height ^String renderer] (.size *applet* (int width) (int height) renderer)))

;; $$sketchFile
;; $$sketchPath

(defn smooth
  "Draws all geometry with smooth (anti-aliased) edges. This will slow down the
  frame rate of the application, but will enhance the visual refinement.

  Note that smooth will also improve image quality of resized images."
  []
  (.smooth *applet*))

;; $$sort

(defn specular
  ([gray] (.specular *applet* (float gray)))
  ([gray alpha] (.specular *applet* (float gray) (float alpha)))
  ([x y z] (.specular *applet* (float x) (float y) (float z)))
  ([x y z a] (.specular *applet* (float x) (float y) (float z) (float a))))

(defn sphere
  [r] (.sphere *applet* (float r)))

(defn sphere-detail
  ([res] (.sphereDetail *applet* (int res)))
  ([ures vres] (.sphereDetail *applet* (int ures) (int vres))))

;; $$splice
;; $$split
;; $$splitTokens

(defn spotlight
  ([r g b x y z nx ny nz angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration))
  ([[r g b] [x y z] [nx ny nz] angle concentration]
     (.spotLight *applet* r g b x y z nx ny nz angle concentration)))

(defn sq [a] (PApplet/sq (float a)))

(defn sqrt [a] (PApplet/sqrt (float a)))

;; $$start
;; $$status
;; $$stop
;; $$str

(defn stroke-float
  "Sets the color used to draw lines and borders around shapes."
  ([gray] (.stroke *applet* (float gray)))
  ([gray alpha] (.stroke *applet* (float gray) (float alpha)))
  ([x y z] (.stroke *applet* (float x) (float y) (float z)))
  ([x y z a] (.stroke *applet* (float x) (float y) (float z) (float a))))

(defn stroke-int
  "Sets the color used to draw lines and borders around shapes."
  ([rgb] (.stroke *applet* (int rgb)))
  ([rgb alpha] (.stroke *applet* (int rgb) (float alpha))))

(def ^{:arglists (:arglists (meta #'stroke-float))
       :doc (:doc (meta #'stroke-float))}
  stroke stroke-float)

(defn stroke-cap [cap] (.strokeCap *applet* (int cap)))

(defn stroke-join [jn] (.strokeJoin *applet* (int jn)))

(defn stroke-weight
  "Sets the width of the stroke used for lines, points, and the border around
  shapes. All widths are set in units of pixels. "
  [weight]
  (.strokeWeight *applet* (float weight)))

;; $$subset

(defn tan [angle] (PApplet/tan (float angle)))

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
  ([gray] (.tint *applet* (float gray)))
  ([gray alpha] (.tint *applet* (float gray) (float alpha)))
  ([x y z] (.tint *applet* (float x)(float y) (float z)))
  ([x y z a] (.tint *applet* (float x)(float y) (float z) (float a))))

(defn tint-int
  ([rgb] (.tint *applet* (int rgb)))
  ([rgb alpha] (.tint *applet* (int rgb) (float alpha))))

(def tint tint-float)

(defn translate
  ([v] (apply translate v))
  ([tx ty] (.translate *applet* (float tx) (float ty)))
  ([tx ty tz] (.translate *applet* (float tx) (float ty) (float tz))))

(defn triangle
  [x1 y1 x2 y2 x3 y3]
  (.triangle *applet* (float x1) (float y1)
             (float x2) (float y2) (float x3) (float y3)))

;; $$trim
;; $$unbinary
;; $$unhex
;; $$unint
;; $$unregisterDispose
;; $$unregisterDraw
;; $$unregiserKeyEvent
;; $$unregiserMouseEvent
;; $$unregiserKeyEvent
;; $$unregiserPost
;; $$unregisterPre
;; $$unregisterSize
;; $$update

(defn update-pixels [] (.updatePixels *applet*))

(defn vertex
  ([x y] (.vertex *applet* (float x) (float y)))
  ([x y z] (.vertex *applet* (float x) (float y) (float z)))
  ([x y u v] (.vertex *applet* (float x) (float y) (float u) (float v)))
  ([x y z u v]
     (.vertex *applet* (float x) (float y) (float z) (float u) (float v))))

(defn year [] (PApplet/year))

;; utility functions. clj-processing specific

(defn width
  "Width of the display window. The value of width is zero until size is
  called."
  []
  (.getWidth *applet*))

(defmacro with-translation
  "Berforms body with translation, restores current transformation on exit."
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
                                           "rosado/processing/version.properties")
      properties     (doto (new java.util.Properties) (.load version-stream))
      prop (fn [k] (.getProperty properties (str "rosado.processing.version." k)))
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
   clj-processing is compatible.")

(defn
  processing-version
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
