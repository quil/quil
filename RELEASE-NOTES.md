## 3.1.0
__13th October 2019_

* New cljs functions. By [@anthonygalea](http://github.com/anthonygalea)
  * `:bolditalic` support in `text-style`.
  * `lights`, `shininess`, `light-falloff` now work on cljs as well as clj.
* Added `print-first-n` and `print-every-n-millisec` functios for debugging. By [@anthonygalea](http://github.com/anthonygalea) in [#315](https://github.com/quil/quil/issues/315).
* Fix support of `:mouse-wheel` in cljs. See [#321](https://github.com/quil/quil/issues/321).
* Updated dependencies on Processing to 3.5.3 and P5.js to 0.9.0.

## 3.0.0
_1st April 2019_

* Switched from ProcessingJS to p5js as backend for ClojureScript version. By [@anthonygalea](http://github.com/anthonygalea). A lot of changes in cljs API, see [spreadsheet](https://docs.google.com/spreadsheets/d/1LlXS5DuMgSZpV5LSwvmYzSr5Tpm8UUxwfsuSAsDSamE/edit?usp=sharing).
* ClojureScript sketches have to be created using `<div>` instead of `<canvas>` due to p5js migration.
* Removed `:global-key-events` feature as it's global by default now.
* Fix space-as-keyword functionality. Issue [#232](https://github.com/quil/quil/issues/262) by [@tkocmathla](https://github.com/tkocmathla).
* Documentation improvements from [@shrynx](https://github.com/shrynx) and [@dominicfreeston](https://github.com/dominicfreeston).
* Improved type hints to avoid reflection. Pull request [#288](https://github.com/quil/quil/pull/288) by [@heyarne](https://github.com/heyarne).

## 2.8.0
_19th November 2018_

* New `looping?` function. Contributed by [@NightMachinary](https://github.com/NightMachinary) in [#244](https://github.com/quil/quil/pull/244).
* Improved fullscreen support. [#240](https://github.com/quil/quil/issues/240).
* Improved `with-sketch` and `with-stroke` macros. Contributed by [@zck](https://github.com/zck). Not it works both in Clojure and ClojureScript versions.
* Removed the following functions: `abs-int`, `abs-float`, `ambient-float`, `ambient-int`, `background-float`, `baackground-int`, `constrain-float`, `constrain-int`, `emissive-float`, `emissive-int`, `fill-float`, `fill-int`, `stroke-float`, `stroke-int`, `tint-float`, `tint-int`. Only their counterpart without int/float left and work both in Clojure and ClojureScript.
* Documentation improvements by [@Alex-Keyes](https://github.com/Alex-Keyes).
* Refactoring of snippets to use them in API docs. See [#246](https://github.com/quil/quil/issues/246).

## 2.7.1
_1st April 2018_

* Fix bug when `sketch` in cljs returned canvas instead of sketch. [Commit](https://github.com/quil/quil/commit/22f0dc4f40756e64e1b0e93781353d62fbd62599).

## 2.7.0
_1st April 2018_

* Add `resize-sketch` function. [#212](https://github.com/quil/quil/issues/212).
* Add `:enable/disable-async-saveframe` hints. Thanks to Jacob Maine.
* Add `display-ads` function to embed advertising in sketches to make a few bucks while sharing your art. [Docs](https://www.youtube.com/watch?v=dQw4w9WgXcQ).
* Pass event object to `key-released` handler in fun-mode. [#209](https://github.com/quil/quil/issues/209).
* Disable automatic setting of pixel density to be screen density. [#202](https://github.com/quil/quil/issues/202).
* Fix not working titles. See [#236](https://github.com/quil/quil/issues/236). Thanks to [@mishadoff](https://github.com/mishadoff).
* Fix `random-2d` and `random-3d` to produce uniformly distributed vectors. [Commit](https://github.com/quil/quil/commit/dd447f44f6aba573212d13862920da47b868359b).
* Update to Processing 3.3.7 and Processing.js to 1.6.6.
* Partial Java 9 support. Doesn't work on Mac OS. See Processing [plan](https://github.com/processing/processing/wiki/Supported-Platforms#java-9) for Java 9 support.

## 2.6.0
_12th February 2017_

* Add `random-2d` and `random-2d` functions to generate random vectors. [#201](https://github.com/quil/quil/pull/201) by [@prakhar1989](https://github.com/prakhar1989)
* Fixes `current-fill` and `current-stroke` on cljs. [#196](https://github.com/quil/quil/pull/196) by [@satchit8](https://github.com/satchit8)
* Fixes `mouse-pressed?` and `key-pressed?` on cljs. [#198](https://github.com/quil/quil/pull/198) by [@Norgat](https://github.com/Norgat)
* Fixes console.log problem that logged every call to `rect`. [Bug](https://github.com/quil/processing-js/pull/2).
* Update Processing to 3.2.4 and Processing.js to 1.6.4.

## 2.5.0
_5th November 2016_

* Support svg rendering. [#188](https://github.com/quil/quil/issues/188).
* Add `do-record` macro to simplify svg/pdf generation. [#189](https://github.com/quil/quil/issues/189).
* Update Processing to 3.2.2 and Processing.js to 1.6.3.
* Fix `mouse-wheel` event propagation outside of sketch. [#175](https://github.com/quil/quil/issues/175)
* Fix reflection warnings. [#185](https://github.com/quil/quil/issues/185)

Issues: might not be compatible with Java 7 and below. Processing 3.2.2 requires Java 8+.

## 2.4.0
_23rd March 2016_

* Updated cheatsheet. Thanks to @SevereOverfl0w.
* Added support for some new processing functions: `pixel-density`, `display-density`, `clip`, `no-clip`, `key-modifiers`.
* Fixes: [#170](https://github.com/quil/quil/issues/170), [#179](https://github.com/quil/quil/issues/179).
* Upgraded to ProcessingJS 1.4.16 and Processing 3.0.2.
* Drop support of Clojure 1.6.0.
* Migrated from cljx to cljc and various refactorings to make Quil compile with self-hosted cljs.

## 2.3.0
_1st December 2015_

* Migrated to Processing 3.0. See relevant Processing [page](https://github.com/processing/processing/wiki/Changes-in-3.0).
* Switch to Clojure 1.6. Makes it incompatible with Clojure 1.5.1.
* Optimizations in helper seqs functions. See [#160](https://github.com/quil/quil/issues/160).
* Fix [#161](https://github.com/quil/quil/issues/161): don't draw text when no-fill is set.
* Fix [#115](https://github.com/quil/quil/issues/166): bind *out* to REPL to make errors more visible when using emacs.
* Removed hints (due to Processing 3.0): `:enable-retina-pixels`, `:disable-retina-pixels`, `:enable-native-fonts`, `:disable-native-fonts`.

## 2.2.6
_2nd June 2015_

* Fix bug with hex colours. Issue [#71](https://github.com/quil/quil/issues/71).
* Make `with-translation` and `with-rotation` macros handle exceptions properly. Issue [#154](https://github.com/quil/quil/issues/154).
* Fix `blend-color` function in cljs. Issue [#156](https://github.com/quil/quil/issues/156).
* Add Navigation 2D middleware.
* Pass draw/update/other functions by name, not by value in cljs to make live-reloading easier.

## 2.2.5
_31st January 2015_

* Add `global-key-events` option for cljs sketches.
* Use latest cljs capabilites to simplify setting up cljs project: no more externs, preamble and easy-to-use `:none` optimization level.

## 2.2.4
_21st November 2014_

* Fix warnings introduced by 2.2.3.
* Fix cljs build failures in rare conditions.

## 2.2.3
_21st November 2014_

* Make end-shape to work inside with-graphics macro.
* Allow to pass DOM element as :host in cljs. It used to allow only element ids.
* Add runtime warning when sketches with different context (2D and 3D) are used on same canvas element.
* Improve support for latest clojurescript 0.0-2371.
* Documentation changes.

## 2.2.2
_8th September 2014_

* Processing.js is shipped in a jar to make it possible to include it using `:preamble` option.
* Support `available-fonts`, `load-pixels` and `pixels` functions in cljs version.
* Fix bug that prevented from using Quil cljs on headless servers.
* Fix bugs that prevented Quil from automatically adding canvas to a page if the page is empty.
* Fixed documentation for `pixels` function.

## 2.2.1
_5th August 2014_

* Added `:display` option to control on which display sketch will be shown.
* Docs updates.
* Clojurescript improvements: support `:mouse-wheel` handler and `frame-rate` function.

## 2.2.0
_14th July 2014_

* ClojureScript support. [Wiki](https://github.com/quil/quil/wiki/ClojureScript).
* Navigation 3D middleware. [Wiki](https://github.com/quil/quil/wiki/Navigation-3D).
* Pause On Error middleware.

## 2.1.0
_13th June 2014_

* Updated to Processing 2.2.1.
* [Middleware](https://github.com/quil/quil/wiki/Middleware) support.
* [Functional mode](https://github.com/quil/quil/wiki/Functional-mode-%28fun-mode%29) support.
* New methods: [`resize`](http://quil.info/image.html#resize) and [`state-atom`](http://quil.info/state.html#state-atom).
* `:no-safe-draw` feature renamed to `:no-safe-fns`.

###### Fixed
* NPE when exiting :p2d sketch using "esc" key. [#110](https://github.com/quil/quil/issues/110).
* Wrap all user-provided function to safe wrappers. [#106](https://github.com/quil/quil/issues/106).


## 2.0.0
_18th May 2014_

###### General changes

* Updated to Processing 2.1.2!
* New `:features` option to specify features for sketch. It replaces some options: `:target`, `:decor`, `:safe-draw-fn`. Check documentation for [sketch](http://quil.info/environment.html#sketch) to see list of supported features.
* Fullscreen support using Processing present mode. Check documentation for `:size` in [sketch](http://quil.info/environment.html#sketch).
* New `:bgcolor` option to set color of background in present mode. Check [sketch](http://quil.info/environment.html#sketch) documentation.
* DXF renderer can be used only in `(begin-raw)`.
* Texture mode `:normalized` renamed to `:normalize`.
* Added and removed bunch of hint options. Check documentation for [hint](http://quil.info/rendering.html#hint) function.
* `mouse-state` renamed to `mouse-pressed?`.

###### Fixed

* OpenGL renderers bug: [#24](https://github.com/quil/quil/issues/24).
* `(screen-z x y z)`: [commit](https://github.com/quil/quil/commit/8848af1a49fcafbbbaadc631af4b7a00ef74b733).
* `(curve-point a b c d t)`: [commit](https://github.com/quil/quil/commit/ce59e05dd3f59d946b90e5042e058cd751a3b164).
* `(apply-matrix ...)`: [commit](https://github.com/quil/quil/commit/fa87c4b056901bb2807b46c0524029f825686d8a).
* Calling `(width)` or `(height)` in setup returns 100: [#50](https://github.com/quil/quil/issues/50).
* Tinting doesn't work in `with-graphics`: [#83](https://github.com/quil/quil/issues/83).
* Wrong size after closing the window in REPL: [#31](https://github.com/quil/quil/issues/31).

###### Functions added

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

###### Functions removed

* `(text s)` - removed from Processing.
* `(text-char ch)` - removed from Processing.
* `(size)` - was deprecated in 1.7.0, removed.
* `(text s x1 y1 x2 y2 z)` - removed from Processing.
* `(quil-version)` - didn't work correctly since 1.0, removed. We have `project.clj` for that.
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
