(ns snippets.structure
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet push-style-pop-style {}
  (q/background 255)
  (q/fill 255 0 0)
  (q/ellipse 125 125 100 100)
  (q/push-style)
  (q/fill 0 0 255)
  (q/ellipse 250 250 100 100)
  (q/pop-style)
  (q/ellipse 375 375 100 100))

#?(:clj
   (defsnippet delay {}
     (q/background 127)
     (q/ellipse (* 5 (q/frame-count)) (* 5 (q/frame-count)) 50 50)
     (q/delay-frame (rand-int (* 500 (rand-int 4))))))
