;; processing example

(ns example2
  (:use [rosado.processing]
        [rosado.processing.applet]))

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
    (with-rotation [QUARTER_PI]
      (begin-shape)
      (vertex -50  50)
      (vertex  50  50)
      (vertex  50 -50)
      (vertex -50 -50)
      (end-shape CLOSE)))
  (filter-kind INVERT)
  (framerate 10))

(defn setup []
  "Runs once."
  (smooth)
  (no-stroke)
  (fill 226)
  (framerate 10))

;; Now we just need to define an applet:

(defapplet example2 "An example."
  setup draw 200 200)

(run-example2)

;; (stop-example2)