(ns quil.examples.gen-art.28-cloud-cube
  (:use quil.core
        [quil.helpers.seqs :only [indexed-range-incl]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 28 - A Cube of 3D Noise
;; Taken from Listing 5.6, p97

;; float xstart, ystart, zstart;
;; float xnoise, ynoise, znoise;

;; int sideLength = 200;
;; int spacing = 5;

;; void setup() {
;;   size(500, 300, P3D);
;;   background(0);
;;   noStroke();

;;   xstart = random(10);
;;   ystart = random(10);
;;   zstart = random(10);
;; }

;; void draw() {
;;   background(0);

;;   xstart += 0.01;
;;   ystart += 0.01;
;;   zstart += 0.01;

;;   xnoise = xstart;
;;   ynoise = ystart;
;;   znoise = zstart;

;;   translate(150, 20, -150);
;;   rotateZ(frameCount * 0.1);
;;   rotateY(frameCount * 0.1);

;;   for(int z = 0; z <= sideLength; z+= spacing){
;;     znoise += 0.1;
;;     ynoise = ystart;
;;     for(int y = 0; y <= sideLength; y+= spacing){
;;       ynoise += 0.1;
;;       xnoise = xstart;
;;       for(int x = 0; x <= sideLength; x+= spacing){
;;         xnoise += 0.1;
;;         drawPoint(x, y, z, noise(xnoise, ynoise, znoise));
;;       }
;;     }
;;   }
;; }

;; void drawPoint(float x, float y, float z, float noiseFactor){
;;   pushMatrix();
;;   translate(x, y, z);
;;   float grey = noiseFactor * 255;
;;   fill(grey, 10);
;;   box(spacing, spacing, spacing);
;;   popMatrix();
;; }

(def side-length 200)
(def spacing 5)

(defn draw-point
  [x y z noise-factor]
  (push-matrix)
  (translate x y z)
  (let [grey (* noise-factor 255)]
    (fill grey 10)
    (box spacing spacing spacing)
    (pop-matrix)))

(defn draw []
  (background 0)

  (let [fc          (frame-count)
        x-start     (state :x-start)
        y-start     (state :y-start)
        z-start     (state :z-start)
        rotate-val  (* fc 0.1)
        noise-shift (* fc 0.01)]

    (translate 150 20 -150)
    (rotate-z rotate-val)
    (rotate-y rotate-val)
    (doseq [[x-idx z] (indexed-range-incl 0 side-length spacing)
            [y-idx y] (indexed-range-incl 0 side-length spacing)
            [z-idx x] (indexed-range-incl 0 side-length spacing)]
      (let [x-noise (mul-add x-idx 0.1 (+ noise-shift x-start))
            y-noise (mul-add y-idx 0.1 (+ noise-shift y-start))
            z-noise (mul-add z-idx 0.1 (+ noise-shift z-start))]
        (draw-point x y z (noise x-noise y-noise z-noise))))))

(defn setup []
  (background 0)
  (no-stroke)
  (set-state! :x-start (random 10)
              :y-start (random 10)
              :z-start (random 10)))

(defsketch gen-art-28
  :title "A Cube of 3D Noise"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p3d)
