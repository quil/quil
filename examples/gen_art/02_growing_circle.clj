(ns quil.examples.gen-art.02-growing-circle
  (:use quil.core
        [quil.helpers.seqs :only [seq->stream range-incl]]))

;; Example 2 - Growing Circle
;; Taken from Listing 2.1, p28

;; int diam = 10;
;; float centX, centY;

;; void setup() {
;;   size(500, 300);
;;   frameRate(24);
;;   smooth();
;;   background(180);
;;   centX = width/2;
;;   centY = height/2;
;;   stroke(0);
;;   strokeWeight(5);
;;   fill(255, 50);
;; }

;; void draw() {
;;   if(diam <= 400) {
;;     background(180);
;;     ellipse(centX, centY, diam, diam);
;;     diam += 10;
;;   }
;; }

(defn setup []
  (frame-rate 24)
  (smooth)
  (background 180)
  (stroke 0)
  (stroke-weight 5)
  (fill 255 25)
  (let [diams (range-incl 10 400 10)]
    (set-state! :diam (seq->stream diams)
                :cent-x (/ (width) 2)
                :cent-y (/ (height) 2))))

(defn draw []
  (let [cent-x (state :cent-x)
        cent-y (state :cent-y)
        diam   ((state :diam))]
    (when diam
      (background 180)
      (ellipse cent-x cent-y diam diam))))

(defsketch gen-art-2
  :title "Growing circle"
  :setup setup
  :draw draw
  :size [500 300]
  :keep-on-top true)
