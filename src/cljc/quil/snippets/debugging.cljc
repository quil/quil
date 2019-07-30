(ns quil.snippets.debugging
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet print-first-n
  "print-first-n"
  {:skip-image-diff? true}

  (q/background 255)

  (comment "will print 'foo' the first 5 iterations")
  (q/print-first-n 5 "foo"))

(defsnippet print-every-n-millisec
  "print-every-n-millisec"
  {:skip-image-diff? true}

  (q/background 255)

  (comment "will print 'foo' every 1000 milliseconds")
  (q/print-every-n-millisec 1000 "foo"))
