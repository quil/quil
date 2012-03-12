(ns quil.examples.gen-art.29-spiral-sphere
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 29 - Spiral Sphere
;; Taken from Listing 5.7, p100

;; import processing.opengl.*;

;; int radius = 100;

;; void setup() {
;;   size(500, 300, OPENGL);
;;   background(255);
;;   stroke(0);
;; }

;; void draw() {
;;   background(255);

;;   translate(width/2, height/2, 0);
;;   rotateY(frameCount * 0.03);
;;   rotateX(frameCount * 0.04);

;;   float s = 0;
;;   float t = 0;
;;   float lastx = 0;
;;   float lasty = 0;
;;   float lastz = 0;

;;   while(t < 180) {
;;     s+= 18;
;;     t+= 1;
;;     float radianS = radians(s);
;;     float radianT = radians(t);

;;     float thisx = 0 + (radius * cos(radianS) * sin(radianT));
;;     float thisy = 0 + (radius * sin(radianS) * sin(radianT));
;;     float thisz = 0 + (radius * cos(radianT));

;;     if (lastx != 0){
;;       line(thisx, thisy, thisz, lastx, lasty, lastz);
;;     }

;;     lastx = thisx;
;;     lasty = thisy;
;;     lastz = thisz;
;;   }
;; }

(def radius 100)

(defn setup []
  (background 255)
  (stroke 00))

(defn draw []
  (background 255)
  (translate (/ (width) 2) (/ (height) 2) 0)
  (rotate-y (* (frame-count) 0.03))
  (rotate-x (* (frame-count) 0.04))
  (let [line-args (for [t (range 0 180)]
                    (let [s        (* t 18)
                          radian-s (radians s)
                          radian-t (radians t)
                          x (* radius  (cos radian-s) (sin radian-t))
                          y (* radius  (sin radian-s) (sin radian-t))
                          z (* radius (cos radian-t))]
                      [x y z]))]
    (dorun
     (map #(apply line %) (line-join-points line-args)))))


(defsketch gen-art-29
  :title "Spiral Sphere"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :opengl)
