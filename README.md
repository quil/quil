<img src="http://cloud.github.com/downloads/quil/quil/quil.png" alt="Quil OPainting" title="Quil" align="right" />

# Quil

Quil looked up in shock to see Bigelow floating high in the clouds, his balloons rustling merrily in the wind. He gruffed to her from above, "This truly is a party!". Image after image, vista after vista, passed furry Bige's wide-open eyes. A deep underlying beauty unfolded before him. A flock of bezier gulls whistled past. Beneath his dangling feet a distant shepherd called his scribbly sheep in for re-drawing. Goading him from the distance, wooden letters of so many different fonts mocked **PERLIN-WOULD** from the hilltops.

This truly was an amazing place. Here, dreams and reality had been drawn together - all in one Process. "*_Why* would I ever leave?" he barked with joy! *_Why* indeed!

## (mix Processing Clojure)

In one hand Quil holds Processing, a carefully crafted API for making drawing and animation extremely easy to get your biscuit-loving chops around. In the other she clutches Clojure, an interlocking suite of exquisite language abstractions forged by an army of hammocks and delicately wrapped in flowing silky parens of un-braided joy.

In one swift, skilled motion, Quil throws them both high into the air. In a dusty cloud of pixels, they bond instantly and fly off into the distance painting their way with immutable trails of brilliant colour. Moments later, you see them swiftly return and hover nearby. Your very own ride to Perlinwould awaits. Summon the winds and ride well, my friend.

## Installation

Simply add Quil as a dependency in your `project.clj`:

    [quil "1.0.0-SNAPSHOT"]

Then to pull in all of Quil's silky goodness, just add the following to your `ns` declaration:

    (:use [quil core applet])

**Please Note:** In order to use the OpenGL features, you need to be using Leiningen 2.x.

## Getting Started

Using Quil is as easy as eating chocolate digestives. You just need to grok three basic concepts:

* The Setup fn
* The Draw fn
* The Applet obj

If `setup` and `draw` are hard working *artistic gladiators*, `applet` is the *arena* in which they battle for the glory of art. However, they don't actually fight each other - they work as a team - relentlessly spilling colour all over the arena sands. The crowds roar for messy fight.

`setup` lays all the groundwork and is called only once at the start. `draw`, on the other hand, is called immediately after `setup` has completed, and then repeatedly until you summon it to stop. When you create an `applet` and name your `setup` and `draw` fns, the fun automatically starts.

A simple example is called for:

    (ns for-the-glory-of-art
      (:use [quil core applet]))

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
            x    (random (width))       ;;Set the x coord randomly within the applet
            y    (random (height))]     ;;Set the y coord randomly within the applet
        (ellipse x y diam diam)))       ;;Draw a circle at x y with the correct diameter

    (defapplet example                  ;;Define a new applet named example
      :title "Oh so many grey circles"  ;;Set the title of the applet
      :setup setup                      ;;Specify the setup fn
      :draw draw                        ;;Specify the draw fn
      :size [500 300])                  ;;Specify the size to be 500 x 300


Feast your eyes on this beauty. You're witnessing `setup`, `draw` and `applet` working in complete harmony. See how `setup` turns on anti-aliasing, sets the framerate to 1 FPS and sets the background colour to a nice shade of grey. `draw` then kicks into action. It chooses random stroke, fill colours as well as a random stroke weight (thickness of the pen). It then chooses some random coordinates and circle size and draws an ellipse. An ellipse with the same height and width is a circle. Finally `defapplet` a convenience macro around `applet` ties everything together, specifies a title and size and starts things running. Don't just watch it though, start modifying it to see immediate effects. Go to town.

## Documentation

When getting started with Quil, it's always useful to have the [Cheatsheet](http://cloud.github.com/downloads/quil/quil/quil-cheatsheet.pdf) handy. Also, the [Processing.org Learning Pages](http://processing.org/learning/) are super useful for beginners.

The [full Quil API](https://github.com/quil/quil/wiki/API) is also available on the wiki.

Further documentation will drop into the wiki soon - watch this space!

## Examples

<img src="http://cloud.github.com/downloads/quil/quil/readme-wave.png" alt="Wave Clock" title="Wave Clock" align="right" />

<img src="http://cloud.github.com/downloads/quil/quil/readme-spiral.png" alt="Spiral" title="Sprial" align="right" />

<img src="http://cloud.github.com/downloads/quil/quil/readme-lines.png" alt="Lines" title="Lines" align="right" />

Quil comes chock-packed full of examples covering most of the available API. Many of them have been translated from the excellent book "Generative Art" by Matt Pearson, with kind permission from the author. Head over to the [Gen Art Examples Page](https://github.com/quil/quil/tree/master/examples/gen_art).

## API Exploration

Quil supports an explorable API. For a full list of API categories and subcategories simply type `(doc-cats)` at the REPL. In order to see the fns within a specific category use `(doc-fns 1.1)` if `1.1` is the index of the category you wish to examine. If you know the start of Processing API method name such as `bezier`, you can use `(doc-meths "bezier")` to list all Processing API methods starting with `bezier` alongside their Quil equivalents.

## Processing Compatibility

Quil provides support for the standard Processing API. The majority of fns Processing methods have an equivalent Quil fn. Typically, `camelCased` methods have been converted to `hyphenated-versions`. For a full API list (with both Processing and Quil equivalents) see `API.txt`.



## License

Quil is distributed under Common Public License Version 1.0. See LICENSE for details.

The official Processing.org's jars, used as dependencies, are distributed under LGPG and their code can be found on http://processing.org/

## Contributors ##

* Roland Sadowski
* Phil Hagelberg
* Vilson Vieira
* Marshall T. Vandegrift
* Ilya Epifanov
* Sam Aaron
