(ns quil.examples.gen-art.22-squared-noise-grid
  (:use quil.core
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 21 - Squared 2D Noise Grid
;; Taken from Listing 5.2, p86

;; float xstart, xnoise, ynoise;
;;
;; void setup() {
;;   size(300, 300);
;;   smooth();
;;   background(255);
;;   xstart = random(10);
;;   xnoise = xstart;
;;   ynoise = random(10);
;;   for(int y = 0; y <= height; y+=5) {
;;     ynoise += 0.1;
;;     xnoise = xstart;
;;     for(int x = 0; x <= width; x+=5) {
;;       xnoise += 0.1;
;;       drawPoint(x, y, noise(xnoise, ynoise));
;;     }
;;   }
;; }

;; void drawPoint(float x, float y, float noiseFactor) {
;;   float len = 10 * noiseFactor;
;;   rect(x, y, len, len);
;; }

(defn draw-point
  [x y noise-factor]
  (let [len (* 10 noise-factor)]
    (rect x y len len)))

(defn draw-squares
  [x-start y-start]
  (dorun
   (for [y (range-incl 0 (height) 5)
         x (range-incl 0 (width) 5)]
     (let [x-noise (mul-add x 0.01 x-start)
           y-noise (mul-add y 0.01 y-start)
           alph    (* 255 (noise x-noise y-noise))]
       (draw-point x y (noise x-noise y-noise))))))

(defn setup []
  (smooth)
  (background 255)
  (draw-squares (random 10) (random 10)))

(defsketch gen-art-21
  :title "Squared 2D Noise Grid"
  :setup setup
  :size [300 300])
