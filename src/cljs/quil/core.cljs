(ns cljs.quil.core
  (:require [cljs.quil.applet :as applet]
            [clojure.browser.dom  :as dom])
  (:use-macros [cljs.quil.helpers.tools :only [defapplet]])
  (:use [cljs.quil.applet :only [current-graphics]]))


(defn int-like? [val] (integer? val))


(defn get-sketch-by-id [id]
  (.getInstanceById js/Processing id))


(defn width []
  (.-width (current-graphics)))

(defn height []
  (.-height (current-graphics)))


(defn size [width height]
  (.size (current-graphics) (int width) (int height)))


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
  background-int
  "Sets the color used for the background of the Processing
  window. The default background is light gray. In the draw function,
  the background color is used to clear the display window at the
  beginning of each frame.

  It is not possible to use transparency (alpha) in background colors
  with the main drawing surface, however they will work properly with
  create-graphics. Converts rgb to an int and alpha to a float."
  ([rgb] (.background (current-graphics) (int rgb)))
  ([rgb alpha] (.background (current-graphics) (int rgb) (float alpha))))


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
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke-float
  "Sets the color used to draw lines and borders around
  shapes. Converts all args to floats"
  ([gray] (.stroke (current-graphics) (float gray)))
  ([gray alpha] (.stroke (current-graphics) (float gray) (float alpha)))
  ([x y z] (.stroke (current-graphics) (float x) (float y) (float z)))
  ([x y z a] (.stroke (current-graphics) (float x) (float y) (float z) (float a))))


(defn
  ^{:requires-bindings true
    :processing-name "stroke()"
    :category "Color"
    :subcategory "Setting"
    :added "1.0"}
  stroke-int
  "Sets the color used to draw lines and borders around
  shapes. Converts rgb to int and alpha to a float."
  ([rgb] (.stroke (current-graphics) (int rgb)))
  ([rgb alpha] (.stroke (current-graphics) (int rgb) (float alpha))))


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
  ([x1 y1 x2 y2] (.line (current-graphics) (float x1) (float y1) (float x2) (float y2)))
  ([x1 y1 z1 x2 y2 z2]
     (.line (current-graphics) (float x1) (float y1) (float z1)
            (float x2) (float y2) (float z2))))
