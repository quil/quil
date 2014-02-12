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
