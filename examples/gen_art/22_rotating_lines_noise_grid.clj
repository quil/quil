(ns quil.examples.gen-art.22-rotating-lines-noise-grid
  (:use quil.core
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 22 - Rotating Lines 2D Noise Grid
;; Taken from Section 5.12, p86 (Figure 5.3)

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
;;   pushMatrix();
;;   translate(x,y);
;;   rotate(noiseFactor * radians(360));
;;   stroke(0, 150);
;;   line(0,0,20,0);
;;   popMatrix();
;; }


(defn draw-point
  [x y noise-factor]
  (push-matrix)
  (translate x y)
  (rotate (* noise-factor (radians 360)))
  (stroke 0 150)
  (line 0 0 20 0)
  (pop-matrix))

(defn draw-all-points
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
  (draw-all-points (random 10) (random 10)))

(defsketch gen-art-22
  :title "Rotating Lines 2D Noise Grid"
  :setup setup
  :size [300 300])
