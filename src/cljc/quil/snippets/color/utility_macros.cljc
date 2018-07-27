(ns snippets.color.utility-macros
  (:require #?(:cljs quil.snippet
               :clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet with-fill
  "with-fill"
  {}

     (q/fill 255) ; global white fill

     (q/rect 30 30 60 60) ; white rect

     (q/with-fill [255 0 0]
       (q/rect 60 60 90 90)
       (q/rect 90 90 120 120)) ; two red rects

     (q/rect 120 120 150 150))) ; white rect again

#?(:clj
   (defsnippet with-stroke
  "with-stroke"
  {}

     (q/fill 255) ; global white fill
     (q/stroke-weight 10) ; make borders thicker

     (q/rect 30 30 60 60) ; white rect

     (q/with-stroke [255 0 0]
       (q/rect 60 60 90 90)
       (q/rect 90 90 120 120)) ; two red rects

     (q/rect 120 120 150 150)))
