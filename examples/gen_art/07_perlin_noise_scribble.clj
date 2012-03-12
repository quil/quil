(ns quil.examples.gen-art.07-perlin-noise-scribble
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.seqs :only [range-incl perlin-noise-seq]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 7 - Perlin Noise Scribblea
;; Taken from Listing 3.1, p59

;; void setup() {
;;  size(500, 100);
;;  background(255);
;;  strokeWeight(5);
;;  smooth();

;;  stroke(0, 30);
;;  line(20, 50, 480, 50);

;;  stroke(20, 50, 70);
;;  int step = 10;
;;  float lastx = -999;
;;  float lasty = -999;
;;  float ynoise = random(10);
;;  float y;
;;  for (int x = 20 ; x <= 480 ; x += step){
;;    y = 10 + noise(ynoise) * 80;
;;    if(lastx > -999) {
;;      line(x, y, lastx, lasty);
;;    }
;;    lastx = x;
;;    lasty =y;
;;    ynoise += 0.1;
;;  }
;; }

(defn setup []
  (background 255)
  (stroke-weight 5)
  (smooth)

  (stroke 0 30)
  (line 20 50 480 50)

  (stroke 20 50 70)
  (let [step      10
        seed      (rand 10)
        seed-incr 0.1
        y-mul     80
        y-add     10
        border-x  20
        xs        (range-incl border-x (- (width) border-x) step)
        ys        (perlin-noise-seq seed seed-incr)
        scaled-ys (mul-add ys y-mul y-add)
        line-args (line-join-points xs scaled-ys)]
    (dorun (map #(apply line %) line-args))))

(defsketch gen-art-7
  :title "Perlin Noise Scribble"
  :setup setup
  :size [500 100])
