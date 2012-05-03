(ns clj-automata.core
  "This module visualizes elementary cellular automata. It's primarily intended
   to show off the fun aspects of functional programming and lazy-sequences in clojure to those who are coming from an OO background.

   If you're interested in how elementary cellular automata work, see:
   http://mathworld.wolfram.com/ElementaryCellularAutomaton.html and
   http://en.wikipedia.org/wiki/Elementary_cellular_automaton

   Also, I highly recommend reading this source file bottom to top in order to understand it best.

   In particular, the code here makes heavy use of lazy-sequences, both directly through
   `lazy-seq` and also through functions like map, for, partition, and other functions that
   in clojure return lazy sequences. In some cases, we force lazy-evaluation for side-effects with
   (dorun).

   Since the visualization is of a stream of states for the automata, lazy-sequences are a very apt model. 
   We can define a sequence of all future results, and simply iterate our way toward where we'd like to be.
   At a high level this library revolves around a single lazy seq that represents all future states.

   The rendering model here is quite simple, we use pure clojure to fill a 2D array of 1s and 0s that contains
   the current on-screen state. This 2D buffer is, itself, a partition (e.g. slice) of the lazy sequence of all
   future states. See the run-rule function for more detail.
   
   The core functions behind the laziness are (run-rule) which sets up the initial state and UI, 
   (simulation), which returns a lazy sequence of results,
   and (simulate) which does the legwork of applying the automaton's rules.

   There are other functional aspects here at play. The (rule) function for instance, is a higher-order function,
   meaning that it returns a brand new function. The function it returns implements a given rule, matching the patterns
   of the rule to outputs"
  (:gen-class)
  (:require [quil.core :as qc])
  (:use clojure.pprint)
  (:import java.lang.Math))

;; Colors for each cell
(def live-color [242 233 99])
(def dead-color [64 37 27])

;; Elementary automata use a clever trick to allow us to describe an entire rule with only a few numbers.
;; Since the automata are essentially pattern matchers, describing what a trio of living / dead cells evaluate to
;; we can effeciently encode their behaviour as a sequence of 1s and 0s. A good visualization of this can be found
;; here, at wolfram math world: http://mathworld.wolfram.com/ElementaryCellularAutomaton.html
(defn int->bdigits
  "Gets the binary digits that comprise an integer as a seq of ints."
  [number]
  (for [c (Integer/toBinaryString number)] (Integer/valueOf (str c))))

(defn zero-pad
  "Forward pads a seq with 0s to match a given length. Used for making sure int->bdigits hits byte boundaries"
  [x len]
  (let [shortage (- len (count x))]
    (if (< shortage 1)
      x
      (concat (repeat shortage 0) x))))

(def input-patterns
  "The list of possible input sequences for elementary cellular automata, which are easily generated
   by counting down from 8 in binary, and making sure we have at least three digits.
   This should produce a list like: ((111 110 ...))"
  (map #(zero-pad (int->bdigits %1) 3) (range 8)))

(defn rule-mappings
  "Returns a mapping of patterns to new states. Returns a structure like:
   {(0 1 1) 1
    ...}"
  [number]
  ;; Zipmap combines two sequences into a map, much like a real-life zipper!
  ;; The key here is that the magic rule numbers are not numbers at all
  ;; but a sequence rather (their individual binary digits) that get mapped
  ;; onto the list of possible inputs described in input-patterns.
  ;; The heart of the generic solution here is really just checking
  ;; equality of a sequence, which we can do via a lookup in the hashmap
  ;; this generates
  (zipmap input-patterns
          (reverse (zero-pad (int->bdigits number) 8))))

(defn rule
  "Returns a function that will process a triad of input values according to a given rule #.
   Since rules are simple lookup tables, this maps to nothing more than a get really.
   We use a function here only to be able to close over the rule-mappings and only evaluate those once."
  [number]
  ;; Applying a rule is really simple, since we've reduced the problem to pattern matching, and
  ;; clojrue can match lists well (e.g. (= [1 2 3] [1 2 3]) => true even though they're separate
  ;; objects), can simply see which pattern the 3 given values match with a map lookup via get.
  (let [mappings (rule-mappings number)]
    (fn [triad] (get mappings triad))))

;; Since we assume cells that are off the grid are zeroes, and the far left and far
;; right calculatoins both require these cells, we make our calculation a bit easier by
;; simply pretending the previous row has two extra 0s on either side
(defn bookend
  "Pads a seq with a given value on both sides."
  [x v]
  (concat [v] x [v]))

(defn simulate
  "Runs a single iteration of a given rule-fn on a given-state"
  [rule-fn state]
  (let [rule (rule-mappings 110)]
    ;; We bookend the value below to add a 0 on both sides of the previous state
    ;; as it makes calculations simpler
    (for [triad (partition 3 1 (bookend state 0))]
      (rule-fn triad))))

(defn simulation
  "Returns a lazy-seq of future states for a given rule-fn and state"
  [rule-fn state]
  (let [new-state (simulate rule-fn state)]
    ;; This is an infinitely recursive lazy sequence! Notice how we start
    ;; by considing (prepending) to a new-state onto the head of a not-yet extant
    ;; lazy sequence. 
    ;; You'll notice that the lazy sequence is declared with a
    ;; body that will recurse from the present state, passing the current state into
    ;; itself. Lazy sequences such as this are inherently tail-recursive, so they won't
    ;; blow the stack.
    (cons new-state (lazy-seq (simulation rule-fn new-state)))))

(defn draw-buffer
  "Redraw what's on screen given a buffer of cell data at a given scale"
  [buffer scale]
  ;; We use letfn here because we want both of these functions to
  ;; have access to the variables `buffer` and `scale`. Closing over them
  ;; here rather than defining them separately is simply a stylistic choice.
  ;;
  ;; We use two nested `map-index` calls to iterated over the canvas row by row,
  ;; cell by cell, rendering each to the UI
  (letfn [(draw-row [y row]
            (dorun (map-indexed (fn [x col] (draw-cell x y col)) row)))
          (draw-cell [x y col]
            (apply qc/fill (if (= 1 col) live-color dead-color))
            (qc/rect (* scale x) (* scale y) scale scale))]
    (dorun (map-indexed draw-row buffer))))

(defn setup
  "Setup the UI"
  []
  (qc/smooth) ;; Enable AA
  (qc/frame-rate 24))

(defn run-rule [rule-num {:keys [width height scale]}]
  (let [width (or width 100)
        height (or height 100)
        scale (or scale 5) ; Scale factor for rendering
        ;; Our initial state is a single row of random 0s and 1s
        initial (repeatedly height #(rand-int 2))
        sim (simulation (rule rule-num) initial)
        ;; We use partition as a sliding wintdow here. Since sim is
        ;; an infinite lazy sequence of future rows we use the 3-arity
        ;; version of partition here to create a 2D view of the visible range
        ;; of results. Since this is the 3-arity version of partition, with 1
        ;; specified as the second parameter, each time we grab the next item from
        ;; the sim sequence we wind up with the same 2D view as before, but shifted ahead
        ;; one row. This is how the simulation scrolls down!
        time-slices (atom (partition height 1 sim))]
    (println "Rule " rule-num " mappings:")
    (pprint (rule-mappings rule-num))
    ;; Initialize the graphics
    (qc/defsketch automata
      :title (str "Rule " rule-num)
      :setup setup
      :draw (fn drawfn [] ;; Named anonymous functions are easier to stacktrace
              ;; Lazy sequences can also re-integrate into a more imperitive style
              ;; What follows below is convenient, but not quite functional.
              ;; Since the :draw callback is inherently not recursive I went with a
              ;; more imperitive approach here. We use an atom to maintain the current position
              ;; within our infinite sequence. This could also have been archieved with a
              ;; tail recursive loop and not-utilizing the draw callback, but that's left
              ;; as an excercise for the reader.
              (draw-buffer (first @time-slices) scale)
              (swap! time-slices (fn [_] (rest @time-slices))))
      :size [(* scale width) (* scale height)])))

(defn -main [rule-num & args]
  (run-rule (Integer/valueOf rule-num)
            {:width 100 :height 100 :scale 4}))