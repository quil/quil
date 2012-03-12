(ns quil.examples.gen-art.14-hundred-noisy-spirals
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.seqs :only [range-incl steps]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 14 - 100 Noisy Spirals
;; Taken from Listing 4.4, p71

;; void setup() {
;;   size(500,300);
;;   background(255);
;;   strokeWeight(0.5);
;;   smooth();
;;   int centX = 250;
;;   int centY = 150;

;;   float x, y;
;;   for (int i = 0; i<100; i++) {

;;     float lastx = -999;
;;     float lasty = -999;
;;     float radiusNoise = random(10);
;;     float radius = 10;
;;     stroke(random(20), random(50), random(70), 80);
;;     int startangle = int(random(360));
;;     int endangle = 1440 + int(random(1440));
;;     int anglestep = 5 + int(random(3));
;;     for (float ang = startangle; ang <= endangle; ang += anglestep) {
;;       radiusNoise += 0.05;
;;       radius += 0.5;
;;       float thisRadius = radius + (noise(radiusNoise) * 200) - 100;
;;       float rad = radians(ang);
;;       x = centX + (thisRadius * cos(rad));
;;       y = centY + (thisRadius * sin(rad));
;;       if (lastx > -999) {
;;         line(x,y,lastx,lasty);
;;       }
;;       lastx = x;
;;       lasty = y;
;;     }
;;   }
;; }

(defn setup []
  (background 255)
  (stroke-weight 0.5)
  (smooth)
  (dotimes [_ 100]
    (let [radius      100
          cent-x      250
          cent-y      150
          start-angle (rand 360)
          end-angle   (+ 1440 (rand 1440))
          angle-step  (+ 5 (rand 3))
          rad-noise   (steps (rand 10) 0.05)
          rad-noise   (map #(* 200 (noise %)) rad-noise)
          rads        (map radians (range-incl start-angle end-angle angle-step))
          radii       (steps 10 0.5)
          radii       (map (fn [rad noise] (+ rad noise -100)) radii rad-noise)
          xs          (map (fn [rad radius] (mul-add (cos rad) radius cent-x)) rads radii)
          ys          (map (fn [rad radius] (mul-add (sin rad) radius cent-y)) rads radii)
          line-args   (line-join-points xs ys)]
      (stroke (rand 20) (rand 50) (rand 70) 80)
      (dorun (map #(apply line %) line-args)))))

(defsketch gen-art-14
  :title "100 Noisy Spirals"
  :setup setup
  :size [500 300])
