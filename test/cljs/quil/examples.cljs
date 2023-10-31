(ns quil.examples
  (:require [quil.core :as q :include-macros true]))

;; used for both manual resize and fullscreen tests
(defn resize [host]
  (q/sketch
   :host host
   :size [500 500]
   :draw (fn []
           (q/background 240)
           (q/text-size 20)
           (q/fill 0)
           (q/text-align :left :top)
           (q/text (str "width: " (q/width)
                        "\nheight: " (q/height)
                        "\ndensity: " (q/display-density))
                   10 10)

           ;; FIXME: after resize cross hairs are on lower, right edges because
           ;; width/height of canvas element changes from style width/height.
           ;; initial canvas has width/height 1000x1000, but after resize it is
           ;; 700x700 and 500x500 but only showing the upper left quadrant of
           ;; the canvas.
           (q/ellipse (/ (q/width) 4) (/ (q/height) 4) 10 10)
           (q/line 0 (/ (q/height) 2) (q/width) (/ (q/height) 2))
           (q/line (/ (q/width) 2) 0 (/ (q/width) 2) (q/height)))))

