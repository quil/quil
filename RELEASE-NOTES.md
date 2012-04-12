## 1.2.0
11th April 2012

Additions:

* Catch exceptions in draw fn by default and print exceptions to stdout. Each exception caught also causes the draw fn to sleep for 1s to throttle exception printing. This can be turned off by specifying `:safe-draw-fn false` in the sketch options.

## 1.1.0
11th April 2012

Additions:

* Added :decor sketch option to allow user to toggle frame decorations.

Fixes:

* Fixed outdated sketch docstring.

## 1.0.0 - 7th April 2012

First release!
