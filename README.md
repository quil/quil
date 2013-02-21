<img src="http://cloud.github.com/downloads/quil/quil/quil.png" alt="Quil Painting" title="Quil" align="right" />

# Quil

Quil looked up in shock to see Bigelow floating high in the clouds, his balloons rustling merrily in the wind. He gruffed to her from above, "This truly is a party!". Image after image, vista after vista, passed furry Bige's wide-open eyes. A deep underlying beauty unfolded before him. A flock of bezier gulls whistled past. Beneath his dangling paws a distant shepherd called his scribbly sheep in for re-sketching. Goading him from the distance, wooden letters of so many different fonts mocked **PERLIN-WOULD** from the hilltops.

This truly was an amazing place. Here, dreams and reality had been drawn together - all in one Process. "*_Why* would I ever leave?" he barked with joy! *_Why* indeed!

## (mix Processing Clojure)

In one hand Quil holds Processing, a carefully crafted API for making drawing and animation extremely easy to get your biscuit-loving chops around. In the other she clutches Clojure, an interlocking suite of exquisite language abstractions forged by an army of hammocks and delicately wrapped in flowing silky parens of un-braided joy.

In one swift, skilled motion, Quil throws them both high into the air. In a dusty cloud of pixels, they bond instantly and fly off into the distance painting their way with immutable trails of brilliant colour. Moments later, you see them swiftly return and hover nearby. Your very own ride to Perlinwould awaits. Summon the winds and ride well, my friend.

## Requirements

Quil requires Clojure 1.4.0 or higher, and other dependencies described in `project.clj` which are automatically pulled with leiningen. For the OpenGL support you need to use leiningen 2.0 or higher.

## Installation

[Leiningen](https://github.com/technomancy/leiningen) users simply need to add Quil as a dependency to their `project.clj`:

    [quil "1.6.0"]

Then to pull in all of Quil's silky goodness, just add the following to your `ns` declaration:

    (:use quil.core)

For more detailed instructions [head over to the wiki](https://github.com/quil/quil/wiki/Installing).

## Getting Started

Using Quil is as easy as eating chocolate digestives. You just need to grok three basic concepts:

* The Setup fn
* The Draw fn
* The Sketch

If `setup` and `draw` are hard working *artistic gladiators*, `sketch` is the *arena* in which they battle for the glory of art. However, they don't actually fight each other - they work as a team - relentlessly spilling colour all over the arena sands. The crowds roar for messy fight.

`setup` lays all the groundwork and is called only once at the start. `draw`, on the other hand, is called immediately after `setup` has completed, and then repeatedly until you summon it to stop. When you create a `sketch` and name your `setup` and `draw` fns, the fun automatically starts.

A simple example is called for:

    (ns for-the-glory-of-art
      (:use quil.core))

    (defn setup []
      (smooth)                          ;;Turn on anti-aliasing
      (frame-rate 1)                    ;;Set framerate to 1 FPS
      (background 200))                 ;;Set the background colour to
                                        ;;  a nice shade of grey.
    (defn draw []
      (stroke (random 255))             ;;Set the stroke colour to a random grey
      (stroke-weight (random 10))       ;;Set the stroke thickness randomly
      (fill (random 255))               ;;Set the fill colour to a random grey

      (let [diam (random 100)           ;;Set the diameter to a value between 0 and 100
            x    (random (width))       ;;Set the x coord randomly within the sketch
            y    (random (height))]     ;;Set the y coord randomly within the sketch
        (ellipse x y diam diam)))       ;;Draw a circle at x y with the correct diameter

    (defsketch example                  ;;Define a new sketch named example
      :title "Oh so many grey circles"  ;;Set the title of the sketch
      :setup setup                      ;;Specify the setup fn
      :draw draw                        ;;Specify the draw fn
      :size [323 200])                  ;;You struggle to beat the golden ratio

<img src="https://github.com/downloads/quil/quil/readme-oh-so-many-grey-circles.png" alt="Oh so many grey cicles" title="Oh so many grey cicles" align="left" />

Feast your eyes on this beauty.

You're witnessing `setup`, `draw` and `sketch` working in complete harmony. See how `setup` turns on anti-aliasing, sets the framerate to 1 FPS and sets the background colour to a nice shade of grey. `draw` then kicks into action. It chooses random stroke, fill colours as well as a random stroke weight (thickness of the pen). It then chooses some random coordinates and circle size and draws an ellipse. An ellipse with the same height and width is a circle. Finally `defsketch` a convenience macro around `sketch` ties everything together, specifies a title and size and starts things running. Don't just watch it though, start modifying it to see immediate effects. Go to town.

## Documentation


<a href="https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf"><img src="http://github.com/downloads/quil/quil/readme-cheatsheet.png" alt="Cheatsheet" title="Cheatsheet" align="right" /></a>

When getting started with Quil, it's always useful to have the [Cheatsheet](https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf) handy.

If you're new to Processing and graphics programming in general, the [Processing.org Learning Pages](http://processing.org/learning/) are an excellent primer and will get you started in no time.

The full [Quil API](https://github.com/quil/quil/wiki/API) and [Installation Intructions](https://github.com/quil/quil/wiki/Installing) are also available on the wiki.

## API Exploration

Quil supports an explorable API. For a full list of API categories and subcategories simply type `(show-cats)` at the REPL.

    user=> (show-cats)
    1 Color (0)
       1.1 Creating & Reading (11)
       1.2 Loading & Displaying (1)
       1.3 Pixels (1)
       1.4 Setting (12)
    2 Data (0)
       2.1 Conversion (4)
       .
       .
       . etc

In order to see the fns within a specific category use `(show-fns 11.1)` if `11.1` is the index of the category you wish to examine.

    user=> (show-fns 11.1)
    11.1 2D Primitives
        arc ellipse line point quad rect triangle

You can also lookup functions by name of the category or function name.

    user=> (show-fns "trans")
    14 Transform
        apply-matrix pop-matrix print-matrix push-matrix reset-matrix rotate
        rotate-x rotate-y rotate-z scale shear-x shear-y translate
    14.1 Utility Macros
        with-translation

If you know the start of Processing API method name such as `bezier`, you can use `(show-meths "bezier")` to list all Processing API methods starting with `bezier` alongside their Quil equivalents:

    user=> (show-meths "bezier")
    bezierPoint()    -  bezier-point
    bezierDetail()   -  bezier-detail
    bezier()         -  bezier
    bezierTangent()  -  bezier-tangent
    bezierVertex()   -  bezier-vertex


## Examples

<img src="http://cloud.github.com/downloads/quil/quil/readme-wave.png" alt="Wave Clock" title="Wave Clock" align="right" />

<img src="http://cloud.github.com/downloads/quil/quil/readme-spiral.png" alt="Spiral" title="Sprial" align="right" />

<img src="http://cloud.github.com/downloads/quil/quil/readme-lines.png" alt="Lines" title="Lines" align="right" />

Quil comes chock-packed full of examples covering most of the available API. Many of them have been translated from the excellent book "Generative Art" by Matt Pearson, with kind permission from the author.

Head over to the [Gen Art Examples Page](https://github.com/quil/quil/blob/master/examples/gen_art/README.md).

## Processing Compatibility

Quil provides support for the standard Processing API - currently version `1.5.1`. The majority of fns Processing methods have an equivalent Quil fn. Typically, `camelCased` methods have been converted to `hyphenated-versions`. For a full API list (with both Processing and Quil equivalents) see [API.txt](https://github.com/quil/quil/blob/master/API.txt).

## Community

You can ask questions, get support on our mailing list:

https://groups.google.com/forum/?fromgroups#!forum/clj-processing

There is also a small number of people that hang out in `#quil` on freenode. New artworks are show-cased on the @quilist Twitter account: http://twitter.com/quilist

## License

Quil is distributed under Common Public License Version 1.0. See LICENSE for details.

The official Processing.org's jars, used as dependencies, are distributed under LGPL and their code can be found on http://processing.org/

## Contributors ##

* Roland Sadowski
* Phil Hagelberg
* Vilson Vieira
* Marshall T. Vandegrift
* Ilya Epifanov
* Sam Aaron
* David Nolen
* Tyler Green
* Franco Lazzarino
* Zach Tellman
