# Quil

<img src="http://cloud.github.com/downloads/quil/quil/quil.png" alt="Quil Painting" title="Quil" align="right" />

Quil looked up in shock to see Bigelow floating high in the clouds, his balloons rustling merrily in the wind. He gruffed to her from above, "This truly is a party!". Image after image, vista after vista, passed furry Bige's wide-open eyes. A deep underlying beauty unfolded before him. A flock of bezier gulls whistled past. Beneath his dangling paws a distant shepherd called his scribbly sheep in for re-sketching. Goading him from the distance, wooden letters of so many different fonts mocked **PERLIN-WOULD** from the hilltops.

This truly was an amazing place. Here, dreams and reality had been drawn together - all in one Process. "*_Why* would I ever leave?" he barked with joy! *_Why* indeed!

## (mix Processing Clojure)

In one hand Quil holds Processing, a carefully crafted API for making drawing and animation extremely easy to get your biscuit-loving chops around. In the other she clutches Clojure, an interlocking suite of exquisite language abstractions forged by an army of hammocks and delicately wrapped in flowing silky parens of un-braided joy.

In one swift, skilled motion, Quil throws them both high into the air. In a dusty cloud of pixels, they bond instantly and fly off into the distance painting their way with immutable trails of brilliant colour. Moments later, you see them swiftly return and hover nearby. Your very own ride to Perlinwould awaits. Summon the winds and ride well, my friend.

## Requirements

Quil works with Clojure 1.5.1 and 1.6.0.

## Installation

[Leiningen](https://github.com/technomancy/leiningen) users simply need to add Quil as a dependency to their `project.clj`:

```clojure
[quil "2.2.1"]
```

Then to pull in all of Quil's silky goodness, just add the following to your `ns` declaration:

```clojure
(:require [quil.core :as q])
```

For more detailed instructions [head over to the wiki](https://github.com/quil/quil/wiki/Installing).

## Getting Started

Using Quil is as easy as eating chocolate digestives. You just need to grok three basic concepts:

* The Setup fn
* The Draw fn
* The Sketch

If `setup` and `draw` are hard working *artistic gladiators*, `sketch` is the *arena* in which they battle for the glory of art. However, they don't actually fight each other - they work as a team - relentlessly spilling colour all over the arena sands. The crowds roar for messy fight.

`setup` lays all the groundwork and is called only once at the start. `draw`, on the other hand, is called immediately after `setup` has completed, and then repeatedly until you summon it to stop. When you create a `sketch` and name your `setup` and `draw` fns, the fun automatically starts.

A simple example is called for:
```clojure
(ns for-the-glory-of-art
  (:require [quil.core :as q]))

(defn setup []
  (q/smooth)                          ;; Turn on anti-aliasing
  (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (q/background 200))                 ;; Set the background colour to
                                      ;; a nice shade of grey.
(defn draw []
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [diam (q/random 100)             ;; Set the diameter to a value between 0 and 100
        x    (q/random (q/width))       ;; Set the x coord randomly within the sketch
        y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
    (q/ellipse x y diam diam)))         ;; Draw a circle at x y with the correct diameter

(q/defsketch example                  ;; Define a new sketch named example
  :title "Oh so many grey circles"    ;; Set the title of the sketch
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  :size [323 200])                    ;; You struggle to beat the golden ratio
```
<img src="https://cloud.githubusercontent.com/assets/38924/3032404/48f404d8-e057-11e3-88bd-aeefe0859887.png" alt="Oh so many grey cicles" title="Oh so many grey cicles" align="left" />

Feast your eyes on this beauty.

You're witnessing `setup`, `draw` and `sketch` working in complete harmony. See how `setup` turns on anti-aliasing, sets the framerate to 1 FPS and sets the background colour to a nice shade of grey. `draw` then kicks into action. It chooses random stroke, fill colours as well as a random stroke weight (thickness of the pen). It then chooses some random coordinates and circle size and draws an ellipse. An ellipse with the same height and width is a circle. Finally `defsketch` a convenience macro around `sketch` ties everything together, specifies a title and size and starts things running. Don't just watch it though, start modifying it to see immediate effects. Go to town.

## ClojureScript

Quil supports ClojureScript! Check [wiki article](https://github.com/quil/quil/wiki/ClojureScript) for more info.

## Documentation


<a href="https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf"><img src="http://github.com/downloads/quil/quil/readme-cheatsheet.png" alt="Cheatsheet" title="Cheatsheet" align="right" /></a>

When getting started with Quil, it's always useful to have the [Cheatsheet](https://github.com/quil/quil/raw/master/docs/cheatsheet/cheat-sheet.pdf) handy. It may be a little bit out-dated but still contains most functions.

For up-to-date documentation please check [quil.info](http://quil.info).

If you're new to Processing and graphics programming in general, the [Processing.org Learning Pages](http://processing.org/learning/) are an excellent primer and will get you started in no time.

Check Quil [wiki](https://github.com/quil/quil/wiki) for more documentation.

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

    user=> (q/show-fns 11.1)
    11.1 2D Primitives
        arc ellipse line point quad rect triangle

You can also lookup functions by name of the category or function name.

    user=> (q/show-fns "trans")
    14 Transform
        apply-matrix pop-matrix print-matrix push-matrix reset-matrix rotate
        rotate-x rotate-y rotate-z scale shear-x shear-y translate
    14.1 Utility Macros
        with-translation

If you know the start of Processing API method name such as `bezier`, you can use `(q/show-meths "bezier")` to list all Processing API methods starting with `bezier` alongside their Quil equivalents:

    user=> (q/show-meths "bezier")
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

Head over to the [Gen Art Examples Page](https://github.com/quil/quil-examples/tree/master/src/quil_sketches/gen_art/README.md). Instructions of how to run examples you can find in README in [Quil examples](https://github.com/quil/quil-examples) repo.

## Processing Compatibility

Quil provides support for the standard Processing API - currently version `2.2.1` of Processing and `1.4.8` of Processing.js. The majority of fns Processing methods have an equivalent Quil fn. Typically, `camelCased` methods have been converted to `hyphenated-versions`. For a full API list (with both Processing and Quil equivalents) see [API.txt](https://github.com/quil/quil/blob/master/API.txt).

## Community

You can ask questions, get support on our mailing list:

https://groups.google.com/forum/?fromgroups#!forum/clj-processing

There is also a small number of people that hang out in `#quil` on freenode. New artworks are show-cased on the @quilist Twitter account: http://twitter.com/quilist

## License

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

The official Processing.org's jars, used as dependencies, are distributed under LGPL and their code can be found on http://processing.org/

## Contributors ##

See [list](https://github.com/quil/quil/graphs/contributors) of contributors.
