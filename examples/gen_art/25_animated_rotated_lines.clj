(ns quil.examples.gen-art.25-animated-rotated-lines
  (:use quil.core
        [quil.helpers.seqs :only [steps seq->stream range-incl tap tally indexed-range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 25 - Animated Rotated Lines
;; Taken from Listing 5.4, p91

;; float xstart, xnoise, ystart, ynoise;
;; float xstartNoise, ystartNoise;

;; void setup() {
;;   size(300, 300);
;;   smooth();
;;   background(255);
;;   frameRate(24);

;;   xstartNoise = random(20);
;;   ystartNoise = random(20);

;;   xstart = random(10);
;;   ystart = random(10);
;; }

;; void draw() {
;;   background(255);

;;   xstart += 0.01;
;;   ystart += 0.01;

;;   xstartNoise += 0.01;
;;   ystartNoise += 0.01;
;;   xstart += (noise(xstartNoise) * 0.5) - 0.25;
;;   ystart += (noise(ystartNoise) * 0.5) - 0.25;

;;   xnoise = xstart;
;;   ynoise = ystart;

;;   for(int y = 0; y <= height; y+=5){
;;     ynoise += 0.1;

;;     xnoise = xstart;
;;     for(int x = 0; x <= width; x+= 5){
;;       xnoise += 0.1;
;;       drawPoint(x, y, noise(xnoise, ynoise));
;;     }
;;   }
;; }

;; void drawPoint(float x, float y, float noiseFactor) {
;;   pushMatrix();
;;   translate(x, y);
;;   rotate(noiseFactor * radians(360));
;;   stroke(0, 150);
;;   line(0, 0, 20, 0);
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
  [x-start y-start step-size]
  (dorun
   (for [[x-idx x] (indexed-range-incl 0 (width) step-size)
         [y-idx y] (indexed-range-incl 0 (height) step-size)]
     (let [x-noise-shift (* x-idx 0.1)
           y-noise-shift (* y-idx 0.1)
           x-noise (+ x-start x-noise-shift)
           y-noise (+ y-start y-noise-shift)]
       (draw-point x y (noise x-noise y-noise))))))

(defn starts-seq []
  (let [noise-steps (steps (random 20) 0.01)
        noises      (map noise noise-steps)
        noises      (mul-add noises 0.5 -0.25)
        noise-tally (tally noises)]
    (map +
         (steps (random 10) 0.01)
         noise-tally)))

(defn setup []
  (smooth)
  (background 255)
  (frame-rate 24)

  (let [x-starts      (starts-seq)
        y-starts      (starts-seq)
        starts-str    (seq->stream (map list x-starts y-starts))]
    (set-state! :starts-str starts-str)))

(defn draw []
  (background 255)
  (let [[x-start y-start] ((state :starts-str))]
    (draw-all-points x-start y-start 5)))

(defsketch gen-art-25
  :title "Animated Rotated Lines"
  :setup setup
  :draw draw
  :size [300 300])
