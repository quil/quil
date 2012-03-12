(ns quil.examples.gen-art.20-noise-grid
  (:use quil.core
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 20 - 2D Noise Grid
;; Taken from Listing 5.1, p84

;; void setup() {
;;   size(300,300);
;;   smooth();
;;   background(255);
;;   float xstart = random(10);
;;   float xnoise = xstart;
;;   float ynoise = random(10);
;;   for(int y=0;y<=height;y+=1){
;;     ynoise += 0.01;
;;     xnoise = xstart;
;;     for(int x=0;x<=width;x+=1){
;;       xnoise += 0.01;
;;       int alph = int(noise(xnoise, ynoise) * 255);
;;       stroke(0, alph);
;;       line(x,y, x+1, y+1);
;;     }
;;   }
;; }

(defn setup []
  (smooth)
  (background 255)
  (dorun
   (let [x-start (random 10)
         y-start (random 10)]
     (for [y (range-incl (height))
           x (range-incl (width))]
       (let [x-noise (mul-add x 0.01 x-start)
             y-noise (mul-add y 0.01 y-start)
             alph    (* 255 (noise x-noise y-noise))]
         (stroke-int 0 alph)
         (line x y (inc x) (inc y)))))))

(defsketch gen-art-20
  :title "2D Noise Grid"
  :setup setup
  :size [300 300])
