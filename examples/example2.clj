;; processing example

(ns example2
  (:use [processing.core]
        [processing.core.applet]))

;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn draw
  "Example usage of with-translation and with-rotation."
  []
  (background-float 125)
  (stroke-float 10)
  (fill-float (rand-int 125) (rand-int 125) (rand-int 125))
  (with-translation [(/ 200 2) (/ 200 2)]
    (with-rotation [QUARTER-PI]
      (begin-shape)
      (vertex -50  50)
      (vertex  50  50)
      (vertex  50 -50)
      (vertex -50 -50)
      (end-shape :close)))
  (display-filter :invert))

(defn setup []
  "Runs once."
  (size 200 200)
  (smooth)
  (no-stroke)
  (fill 226)
  (frame-rate 10))

;; Now we just need to define an applet:

(defapplet example2
  :title "An example."
  :setup setup
  :draw draw
  :size [200 200])

(run example2)

;; (stop example2)