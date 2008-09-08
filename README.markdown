# Processing for Clojure #

This lib is a wrapper for the [Processing][] project. Mixture Clojure
and Processing enables you to do live-coding and experiment easily.

Most of the function are covered; some have a slightly different name
than in Java (partly because Clojure is confused by methods of the
same arity but different argument types). Most of the constants are
accessible.

## How To Use It? ##

Add your `core.jar` file from the Processing installation to your
CLASSPATH (you can do it in Clojure with `add-classpath`).

Then, create a JAR by running `ant` and also add the resulting file to
your CLASSPATH.

## TODO ##

* docstrings
* include remaining constants

[processing]:http://processing.org/
