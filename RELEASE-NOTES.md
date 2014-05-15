## 2.0.0
_SOME DATE HERE_

#### General changes

* Updated to Processing 2.1.2! Fixes OpenGL renderers bug: [#24](https://github.com/quil/quil/issues/24)
* New `:features` option to specify features for sketch. It replaces some options: `:target`, `:decor`, `:safe-draw-fn`. Check documentation for [sketch](http://quil.info/environment.html#sketch) to see list of supported features.
* Fullscreen support using Processing present mode.
* DXF renderer can be used only in `(begin-raw)`.
* Texture mode `:normalized` renamed to `:normalize`.
* Added and removed bunch of hint options. Check documentation for [hint](http://quil.info/rendering.html#hint) function.
* `mouse-state` renamed to `mouse-pressed?`.

#### Fixed

* `(screen-z x y z)`: [commit](https://github.com/quil/quil/commit/8848af1a49fcafbbbaadc631af4b7a00ef74b733).
* `(curve-point a b c d t)`: [commit](https://github.com/quil/quil/commit/ce59e05dd3f59d946b90e5042e058cd751a3b164).
* `(apply-matrix ...)`: [commit](https://github.com/quil/quil/commit/fa87c4b056901bb2807b46c0524029f825686d8a).
* Calling `(width)` or `(height)` in setup returns 100: [#50](https://github.com/quil/quil/issues/50).
* Tinting doesn't work in `with-graphics`: [#83](https://github.com/quil/quil/issues/83).
* Wrong size after closing the window in REPL: [#31](https://github.com/quil/quil/issues/31).

#### Functions added

* [(arc x y width height start stop mode)](http://quil.info/shape.html#arc)
* [(random-gaussian)](http://quil.info/math.html#random-gaussian)
* [(rect width height r), (rect width height top-left-r top-right-r bottom-right-r bottom-left-r)](http://quil.info/shape.html#rect)
* [(scale x y z)](http://quil.info/transform.html#scale)
* [(smooth level)](http://quil.info/shape.html#smooth)
* [(filter-shader shader)](http://quil.info/image.html#filter-shader)
* [(load-shader)](http://quil.info/rendering.html#load-shader)
* [(reset-shader)](http://quil.info/rendering.html#reset-shader)
* [(shader)](http://quil.info/rendering.html#shader)
* [(blend-mode)](http://quil.info/image.html#blend-mode)
* [(begin-contour)](http://quil.info/shape.html#begin-contour)/[(end-contour)](http://quil.info/shape.html#end-contour)
* [(quadratic-vertex)](http://quil.info/shape.html#quadratic-vertex)
* [(texture-wrap)](http://quil.info/shape.html#texture-wrap)
* [(current-graphics)](http://quil.info/environment.html#current-graphics)
* [(blend src dest x y width height dx dy dwidth dheight mode)](http://quil.info/image.html#blend)
* [(copy src-img dest-img [sx sy swidth sheight] [dx dy dwidth dheight])](http://quil.info/image.html#copy)
* [(image-filter mode), (image-filter mode level)](http://quil.info/image.html#image-filter)
* [(get-pixel img), (get-pixel img x y), (get-pixel img x y w h)](http://quil.info/image.html#get-pixel)
* [(pixels img)](http://quil.info/image.html#pixels)
* [(update-pixels img)](http://quil.info/image.html#update-pixels)
* [(set-pixel img x y c)](http://quil.info/image.html#set-pixel)
* [(screen-x x y)](http://quil.info/lights-camera.html#screen-x), [(screen-y x y)](http://quil.info/lights-camera.html#screen-y)
* [(state)](http://quil.info/state.html#state)
* [(floor n)](http://quil.info/math.html#floor)

#### Functions removed

* `(text s)` - removed from Processing.
* `(text-char ch)` - removed from Processing.
* `(size)` - was deprecated in 1.7.0, removed.
* `(text s x1 y1 x2 y2 z)` - removed from Processing.
* `(quil-version)` - didn't work correctly since 1.0, remove, we have `project.clj` for that.
* `(image img x y c d u1 v1 u2 v2)` - not an official processing API.
* `(request-image path ext)` - version without ext should be sufficient.
* `(load-pixels)` - reduntant because of `(pixels)`.
* `(create-input)`, `(create-input-raw)` - use `clojure.java.io/input-stream` instead.
* `(create-output)` - use `clojure.java.io/output-stream` instead.
* `(begin-raw graphics)` - not an official processing API.
* `(begin-record)`, `(end-record)` - not compatible with current architecture: everything is drawn on graphics instead of applet.


## 1.7.0
_12th February 2014_

* New [with-graphics](https://github.com/quil/quil/issues/25), [with-fill](https://github.com/quil/quil/pull/80), [with-stroke](https://github.com/quil/quil/pull/80) macros.
* New [on-close](https://github.com/quil/quil/issues/30) and [mouse-wheel](https://github.com/quil/quil/pull/62) handlers.
* Fixed bugs: [bezier-vertex](https://github.com/quil/quil/pull/40), [focused](https://github.com/quil/quil/commit/c469c2ba14b40d3ce0f243e1f5f3428de76e0b3e), [no-loop](https://github.com/quil/quil/issues/45).
* Removed `(specular gray alpha)` and `(specular x y z a)` functions: doesn't exist in Processing 1.5.1. Removed `(text-width ch)` function: doesn't work in Processing 1.5.1 correctly.


## 1.6.0
_7th July 2012_

* Add `debug` fn for printing and forcing the current thread to sleep.
* Add `key-coded?` for determining whether the currently pressed key is
  _coded_ i.e. a special character.
* Add `key-as-keyword` for returning the currently pressed key as a keyword.

## 1.5.0
_16th June 2012_

* Add elementary cellular automata example by Andrew Cholakian
* Add new fn: `target-frame-rate` which returns the target framerate specified with the fn `frame-rate`.
* Add keyword render modes to `create-graphics`
* Allow modes to be passed as constants in addition to their keyword shortcuts.
* Bugfix: fix exception in `mouse-button` - thanks to Dan Lidral-Porter.

## 1.4.1
_30th April 2012_

* Bugfix - fixed bug with text-align throwing an exception

## 1.4.0
_29th April 2012_

* Allow pdf/dxf output file to be specified in sketch options :output-file
* Override exit fn to not kill the current process.
* Use exit to ensure pdf/dxf file is written.

## 1.3.0
_19th April 2012_

Update for Clojure 1.4.0 compatibility

## 1.2.0
_11th April 2012_

Additions:

* Catch exceptions in draw fn by default and print exceptions to stdout. Each exception caught also causes the draw fn to sleep for 1s to throttle exception printing. This can be turned off by specifying `:safe-draw-fn false` in the sketch options.

## 1.1.0
_11th April 2012_

Additions:

* Added `:decor` sketch option to allow user to toggle frame decorations.

Fixes:

* Fixed outdated sketch docstring.

## 1.0.0
_7th April 2012_

First release!
