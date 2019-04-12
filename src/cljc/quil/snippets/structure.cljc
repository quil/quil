(ns quil.snippets.structure
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet push-style-pop-style
  ["push-style" "pop-style"]
  {}

  (q/background 255)
  (q/fill 255 0 0)
  (q/ellipse 125 125 100 100)
  (q/push-style)
  (q/fill 0 0 255)
  (q/ellipse 250 250 100 100)
  (q/pop-style)
  (q/ellipse 375 375 100 100))

#?(:clj
   (defsnippet delay
     "delay-frame"
     {}

     (q/background 127)
     (q/ellipse (* 5 (q/frame-count)) (* 5 (q/frame-count)) 50 50)
     (comment "delay frame for random duration between 0 and 2s")
     (q/delay-frame (rand-int (* 500 (rand-int 4))))))

(defsnippet exit
  "exit"
  {}

  (q/background 255)
  (q/fill 0)
  (comment "exit sketch on 50th frame")
  (q/text (str "countdown " (- 50 (q/frame-count))) 20 20)
  (when (= 50 (q/frame-count))
    (q/exit)))

(defsnippet no-loop-start-loop
  ["no-loop" "start-loop" "looping?"]
  {:mouse-clicked (q/start-loop)}

  (q/background 255)
  (q/fill 0)
  (comment "pause sketch on every 10th frame")
  (when (zero? (rem (q/frame-count) 10))
    (q/no-loop)
    (q/text "paused, click to unpause" 10 20))
  (q/text (str "(q/looping?) = " (q/looping?)) 10 40)
  (q/text (str "frame: " (q/frame-count)) 10 60))

(defsnippet redraw
  "redraw"
  {:setup (q/no-loop)
   :mouse-clicked (q/redraw)}

  (q/background 255)
  (q/fill 0)
  (q/text "click to move" 10 20)
  (q/rect (* (q/frame-count) 10)
          (+ (* (q/frame-count) 10)
             40)
          50 50))

