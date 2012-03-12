(ns quil.examples.gen-art.23-fluffy-clouds-noise-grid
  (:use quil.core
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 23 - Fluffy Clouds 2D Noise Grid
;; Taken from Section 5.1.2, p88 (figure 5.4)

;; float xstart, xnoise, ynoise;

;; void setup() {
;;   size(300, 300);
;;   smooth();
;;   background(0);
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
;;   rotate(noiseFactor * radians(540));
;;   float edgeSize = noiseFactor * 35;
;;   float grey = 150 + (noiseFactor * 120);
;;   float alph = 150 + (noiseFactor * 120);
;;   noStroke();
;;   fill(grey, alph);
;;   ellipse(0,0, edgeSize, edgeSize/2);
;;   popMatrix();
;; }

(defn draw-point
  [x y noise-factor]
  (push-matrix)
  (translate x y)
  (rotate (* noise-factor (radians 540)))
  (let [edge-size (* noise-factor 35)
        grey (mul-add noise-factor 120 150)
        alph (mul-add noise-factor 120 150)]
    (no-stroke)
    (fill grey alph)
    (ellipse 0 0 edge-size (/ edge-size 2))
    (pop-matrix)))

(defn draw-all-points
  [x-start y-start]
  (dorun
   (for [y (range-incl 0 (height) 5)
         x (range-incl 0 (width) 5)]
     (let [x-noise (mul-add x 0.01 x-start)
           y-noise (mul-add y 0.01 y-start)]
       (draw-point x y (noise x-noise y-noise))))))

(defn setup []
  (smooth)
  (background 0)
  (draw-all-points (random 10) (random 10)))

(defsketch gen-art-23
  :title "Fluffy Clouds 2D Noise Grid"
  :setup setup
  :size [300 300])
