# Processing for Clojure #

This lib is a wrapper for the [Processing][] project. Mixture of Clojure and Processing enables you to do live-coding and experiment easily.

Most of the function are covered; some have a slightly different name
than in Java (partly because Clojure is confused by methods of the
same arity but different argument types). Most of the constants are
accessible.

## How To Use It? ##

If you're using Leiningen, just add the following line to your
dependencies list:

   [org.clojars.automata/rosado.processing "1.1.0"]

Otherwise, add your `core.jar` file from the Processing installation to your
CLASSPATH.

To use the OpenGL features, you also have to add `gluegen-rt.jar`,
`jogl.jar` and `opengl.jar` from the
`$PROCESSING_DIR/libraries/opengl/library`. You also need to pass the
argument:

         -Djava.library.path=`$PROCESSING_DIR/libraries/opengl/library

to the JVM.

Then, create a JAR by running `ant` and also add the resulting file to
your CLASSPATH.

## Examples ##

The `examples` directory contains a simple script to get you going.

## COMMON ISSUES ##

clj-procesing may not work if you're using `add-classpath` to put the
Processing jars into your CLASSPATH. Please, use a "proper" way to set
up your CLASSPATH (eg. with a special script like the [clj][cljscript]
on Clojure Wiki)

If your're using Clojure's `proxy` macro, you'll have to define the
mouse handling methods to take one argument (which is just an instance
of java.awt.event.MouseEvent class) and get all needed information
(like mouse position etc) from that object. Also, accessing instance
fields of the PApplet class from within proxy doesn't work.

## TODO ##

* docstrings
* include remaining constants

[processing]:http://processing.org/
[cljscript]:http://en.wikibooks.org/wiki/Clojure_Programming/Getting_Started#Create_clj_Script
