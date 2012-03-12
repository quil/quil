(ns quil.examples.gen-art.15-noise-circle
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 15 - Custom Noise Circle
;; Taken from Listing 4.5, p73

;; void setup(){
;;   size(500, 300);
;;   background(255);
;;   strokeWeight(5);
;;   smooth();

;;   float radius = 100;
;;   int centX = 250;
;;   int centY = 150;

;;   stroke(0, 30);
;;   noFill();
;;   ellipse(centX, centY, radius*2, radius*2);

;;   stroke(20, 50, 70);
;;   strokeWeight(1);
;;   float x, y;
;;   float noiseval = random(10);
;;   float radVariance, thisRadius, rad;
;;   beginShape();
;;   fill(20, 50, 70, 50);
;;   for(float ang = 0; ang <= 360; ang += 1){

;;     noiseval += 0.1;
;;     radVariance = 30 * customNoise(noiseval);

;;     thisRadius = radius + radVariance;
;;     rad = radians(ang);
;;     x = centX + (thisRadius * cos(rad));
;;     y = centY + (thisRadius * sin(rad));

;;     curveVertex(x, y);
;;   }
;;   endShape();
;; }

;; float customNoise(float value){
;;   float retValue = pow(sin(value), 3);
;;   return retValue;
;; }

(defn custom-noise [val]
  (pow (sin val) 3))

(comment ;;alternative noise fn to generate Figure 4.8
  (defn custom-noise [val]
    (let [count (int (mod val 12))]
       (pow (sin val) count))))

(defn setup []
  (background 255)
  (stroke-weight 5)
  (smooth)
  (stroke 0 30)
  (no-fill)

  (let [radius     100
        cent-x     250
        cent-y     150
        noise-val  (rand 10)
        angles     (range-incl 0 360)
        rads       (map radians angles)
        noise-vals (range noise-val Float/POSITIVE_INFINITY 0.1)
        rad-vars   (map #(* 30 (custom-noise %)) noise-vals)
        radii      (map + rad-vars (repeat radius))
        xs         (map (fn [radius rad] (mul-add (cos rad) radius cent-x)) radii rads)
        ys         (map (fn [radius rad] (mul-add (sin rad) radius cent-y)) radii rads)]

    (ellipse cent-x cent-y (* 2 radius) (* 2 radius))
    (stroke 20 50 70)
    (stroke-weight 1)
    (begin-shape)
    (fill 20 50 70 50)
    (dorun (map curve-vertex xs ys))
    (end-shape)))

(defsketch gen-art-15
  :title "Custom Noise Circle"
  :setup setup
  :size [500 300])
