;; Constants for the simple processing wrapper for Clojure
;; Roland Sadowski [szabla gmail com]

;; Copyright (c) 2008 Roland Sadowski. All rights reserved.  The use and
;; distribution terms for this software are covered by the Common
;; Public License 1.0 (http://www.opensource.org/licenses/cpl1.0.php)
;; which can be found in the file CPL.TXT at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software. 

;; renderers known to processing.core

(def P2D "processing.core.PGraphics2D")
(def P3D "processing.core.PGraphics3D")
(def JAVA2D "processing.core.PGraphicsJava2D")
(def OPENGL "processing.opengl.PGraphicsOpenGL")
(def PDF "processing.pdf.PGraphicsPDF")
(def DXF "processing.dxf.RawDXF")
;; (def SVG "processing.svg.PGraphicsSVG")

;; platform IDs for PApplet.platform
(def WINDOWS 1)
(def MACOSX 3)
(def LINUX 4)
(def OTHER 0)

(def EPSILON (float 0.0001))

(def PI  (float Math/PI))
(def HALF_PI    (/ PI (float 2.0)))
(def THIRD_PI   (/ PI (float 3.0)))
(def QUARTER_PI (/ PI (float 4.0)))
(def TWO_PI     (* PI (float 2.0)))

(def DEG_TO_RAD (/ PI (float 180.0)))
(def RAD_TO_DEG (/ (float 180.0) PI))

;; for colors and/or images

(def RGB (int 1)) ; image & color
(def ARGB (int 2)) ; image
(def HSB (int 3)) ; color
(def ALPHA (int 4)) ; image
(def CMYK (int 5)) ; image & color (someday)

;; filter/convert types

(def BLUR      11)
(def GRAY      12)
(def INVERT    13)
(def OPAQUE    14)
(def POSTERIZE 15)
(def THRESHOLD 16)
(def ERODE     17)
(def DILATE    18)

;; blend mode keyword definitions
;; see processing.core.PImage#blendColor(int,int,int)

(def REPLACE     0)
(def BLEND       (bit-shift-left 1 0))
(def ADD         (bit-shift-left 1 1))
(def SUBTRACT    (bit-shift-left 1 2))
(def LIGHTEST    (bit-shift-left 1 3))
(def DARKEST     (bit-shift-left 1 4))
(def DIFFERENCE  (bit-shift-left 1 5))
(def EXCLUSION   (bit-shift-left 1 6))
(def MULTIPLY    (bit-shift-left 1 7))
(def SCREEN      (bit-shift-left 1 8))
(def OVERLAY     (bit-shift-left 1 9))
(def HARD_LIGHT  (bit-shift-left 1 10))
(def SOFT_LIGHT  (bit-shift-left 1 11))
(def DODGE       (bit-shift-left 1 12))
(def BURN        (bit-shift-left 1 13))

;; colour component bitmasks

(def ALPHA_MASK  0xff000000)
(def RED_MASK    0x00ff0000)
(def GREEN_MASK  0x0000ff00)
(def BLUE_MASK   0x000000ff)

;; for messages

(def CHATTER    0)
(def COMPLAINT  1)
(def PROBLEM    2)

;; types of projection matrices

(def CUSTOM        0)  ;; user-specified fanciness
(def ORTHOGRAPHIC  2)  ;; 2D isometric projection
(def PERSPECTIVE   3 ) ;; perspective matrix

;; shapes

;; the low four bits set the variety,
;; higher bits set the specific shape type

(def GROUP            (bit-shift-left 1 2))

(def POINTS           (bit-shift-left 1 4))

(def LINES            (bit-shift-left 1 5))
(def LINE_STRIP       (bit-or (bit-shift-left 1 5) 1))
(def LINE_LOOP        (bit-or (bit-shift-left 1 5) 2))

(def TRIANGLES        (bit-or (bit-shift-left 1 6) 0))
(def TRIANGLE_STRIP   (bit-or (bit-shift-left 1 6) 1))
(def TRIANGLE_FAN     (bit-or (bit-shift-left 1 6) 2))

(def QUADS            (bit-or (bit-shift-left 1 7) 0))
(def QUAD_STRIP       (bit-or (bit-shift-left 1 7) 1))
(def POLYGON          (bit-or (bit-shift-left 1 8) 0))
(def CONCAVE_POLYGON  (bit-or (bit-shift-left 1 8) 1))
(def CONVEX_POLYGON   (bit-or (bit-shift-left 1 8) 2))
(def OPEN  1)
(def CLOSE  2)


