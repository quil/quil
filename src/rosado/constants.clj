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

(def P2D PApplet/P2D)
(def P3D PApplet/P3D)
(def JAVA2D PApplet/JAVA2D)
(def OPENGL PApplet/OPENGL)
(def PDF PApplet/PDF)
(def DXF PApplet/DXF)

;; platform IDs for PApplet.platform
(def WINDOWS PApplet/WINDOWS)
(def MACOSX PApplet/MACOSX)
(def LINUX PApplet/LINUX)
(def OTHER PApplet/OTHER)

;(def EPSILON (PApllet 0.0001))
(def EPSILON PApplet/EPSILON)

(def MAX_FLOAT PApplet/MAX_FLOAT)
(def MIN_FLOAT PApplet/MIN_FLOAT)

(def MAX_INT PApplet/MAX_FLOAT)
(def MIN_INT PApplet/MIN_FLOAT)

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

(def REPLACE     PApplet/REPLACE)
(def BLEND       PApplet/BLEND)
(def ADD         PApplet/ADD)
(def SUBTRACT    PApplet/SUBTRACT)
(def LIGHTEST    PApplet/LIGHTEST)
(def DARKEST     PApplet/DARKEST)
(def DIFFERENCE  PApplet/DIFFERENCE)
(def EXCLUSION   PApplet/EXCLUSION)
(def MULTIPLY    PApplet/MULTIPLY)
(def SCREEN      PApplet/SCREEN)
(def OVERLAY     PApplet/OVERLAY)
(def HARD_LIGHT  PApplet/HARD_LIGHT)
(def SOFT_LIGHT  PApplet/SOFT_LIGHT)
(def DODGE       PApplet/DODGE)
(def BURN        PApplet/BURN)

;; colour component bitmasks

(def ALPHA_MASK  PApplet/ALPHA_MASK)
(def RED_MASK    PApplet/RED_MASK)
(def GREEN_MASK  PApplet/GREEN_MASK)
(def BLUE_MASK   PApplet/BLUE_MASK)

;; for messages

(def CHATTER    PApplet/CHATTER)
(def COMPLAINT  PApplet/COMPLAINT)
(def PROBLEM    PApplet/PROBLEM)

;; types of projection matrices

(def CUSTOM        PApplet/CUSTOM)        ;; user-specified fanciness
(def ORTHOGRAPHIC  PApplet/ORTHOGRAPHIC)  ;; 2D isometric projection
(def PERSPECTIVE   PApplet/PERSPECTIVE)   ;; perspective matrix

;; shapes

;; the low four bits set the variety,
;; higher bits set the specific shape type

; (def GROUP            PApplet/GROUP)

(def POINT            PApplet/POINT) ;shared with light
(def POINTS           PApplet/POINTS)

(def LINE             PApplet/LINE)
(def LINES            PApplet/LINES)

(def TRIANGLE         PApplet/TRIANGLE)
(def TRIANGLES        PApplet/TRIANGLES)
(def TRIANGLE_STRIP   PApplet/TRIANGLE_STRIP)
(def TRIANGLE_FAN     PApplet/TRIANGLE_FAN)

(def QUAD             PApplet/QUAD)
(def QUADS            PApplet/QUADS)
(def QUAD_STRIP       PApplet/QUAD_STRIP)

(def POLYGON          PApplet/POLYGON)
(def PATH             PApplet/PATH)

(def RECT             PApplet/RECT)
(def ELLIPSE          PApplet/ELLIPSE)
(def ARC              PApplet/ARC)

(def SPHERE           PApplet/SPHERE)
(def BOX              PApplet/BOX)

(def OPEN             PApplet/OPEN)
(def CLOSE            PApplet/CLOSE)

(def CONCAVE_POLYGON  (bit-or (bit-shift-left 1 8) 1))
(def CONVEX_POLYGON   (bit-or (bit-shift-left 1 8) 2))

(def CORNER PApplet/CORNER)
(def CORNERS PApplet/CORNERS)
(def RADIUS PApplet/RADIUS)
(def CENTER PApplet/CENTER)
(def DIAMETER PApplet/DIAMETER)

;; vertical alignment for text placement

(def BASELINE PApplet/BASELINE)
(def TOP PApplet/TOP)
(def BOTTOM PApplet/BOTTOM)

;; uv texture orientation modes

(def NORMAL PApplet/NORMAL)
(def NORMALIZED PApplet/NORMALIZED)
(def IMAGE PApplet/IMAGE)

;; stroke modes

(def SQUARE PApplet/SQUARE)
(def ROUND PApplet/ROUND)
(def PROJECT PApplet/PROJECT)
(def MODEL PApplet/MODEL)

;; LIGHTING

(def AMBIENT PApplet/AMBIENT)
(def DIRECTIONAL PApplet/DIRECTIONAL)
;; (def POINT PApplet/POINT) ; shared with shape feature
(def SPOT PApplet/SPOT)

;; keys 

(def BACKSPACE PApplet/BACKSPACE)
(def TAB PApplet/TAB)
(def ENTER PApplet/ENTER)
(def RETURN PApplet/RETURN)
(def ESC PApplet/ESC)
(def DELETE PApplet/DELETE)

(def UP PApplet/UP)
(def DOWN PApplet/DOWN)
(def LEFT PApplet/LEFT)
(def RIGHT PApplet/RIGHT)

(def ALT PApplet/ALT)
(def CONTROL PApplet/CONTROL)
(def SHIFT PApplet/SHIFT)




