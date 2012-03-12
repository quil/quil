(ns quil.examples.gen-art.26-sphere
  (:use quil.core))

;; Example 26 - 3D Sphere
;; Taken from Section 5.3.1, p94

;; import processing.opengl.*;

;; void setup() {
;;   size(500, 300, OPENGL);
;;   sphereDetail(40);

;;   translate(width/2, height/2.0);
;;   sphere(100);
;; }

(defn setup []
  (smooth)
  (sphere-detail 100)
  (translate (/ (width) 2) (/ (height) 2) 0)
  (sphere 100))

(defsketch gen-art-26
  :title "3D Sphere"
  :setup setup
  :size [500 300]
  :renderer :opengl)
