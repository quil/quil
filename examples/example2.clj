;; processing example

(ns p5-example2
  (:use rosado.processing)
  (:import (javax.swing JFrame JLabel JTextField JButton))
  (:import (processing.core PApplet)))


;; here's a function which will be called by Processing's (PApplet)
;; draw method every frame. Place your code here. If you eval it
;; interactively, you can redefine it while the applet is running and
;; see effects immediately

(defn fancy-draw
  "Example usage of with-translation and with-rotation."
  [dst]
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
				(framerate 10)))
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

