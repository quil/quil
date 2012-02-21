;; processing test

(ns p5-example
  (:use processing.core)
  (:import (javax.swing JFrame))
  (:import (processing.core PApplet)))


;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn fancy-draw
  "An example of a function which does *something*."
  [dst]
  (background-float (rand-int 256) (rand-int 256) (rand-int 256))
  (fill-float (rand-int 125) (rand-int 125) (rand-int 125))
  (ellipse 100 100 (rand-int 90) (rand-int 90))
  (stroke-float 10)
  (line 10 10 (rand-int 150) (rand-int 150))
  (no-stroke)
  (display-filter :invert)
  (frame-rate 10))

;; below, we create an PApplet proxy and override setup() and draw()
;; methods. Then we put the applet into a window and display it.

(def p5-applet
     (proxy [PApplet] []
       (setup []
              (binding [*applet* this]
                (size 200 200)
                (smooth)
                (no-stroke)
                (fill 226)
                (frame-rate 10)))
       (draw []
             (binding [*applet* this]
               (fancy-draw this)))))

(.init p5-applet)

(def swing-frame (JFrame. "Processing with Clojure"))
(doto swing-frame
	(.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
	(.setSize 200 200)
	(.add p5-applet)
	(.pack)
	(.show))

