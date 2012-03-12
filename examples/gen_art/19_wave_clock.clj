(ns quil.examples.gen-art.19-wave-clock
  (:use quil.core
        [quil.helpers.seqs :only [cycle-between seq->stream steps]]
        [quil.helpers.calc :only [mul-add]]))

;; Example 19 - Wave Clock
;; Taken from Listing 4.6, p79

;; float _angnoise, _radiusnoise;
;; float _xnoise, _ynoise;
;; float _angle = -PI/2;
;; float _radius;
;; float _strokeCol = 254;
;; int _strokeChange = -1;

;; void setup() {
;;   size(500, 300);
;;   smooth();
;;   frameRate(30);
;;   background(255);
;;   noFill();
;;   _angnoise = random(10);
;;   _radiusnoise = random(10);
;;   _xnoise = random(10);
;;   _ynoise = random(10);
;; }

;; void draw() {
;;   _radiusnoise += 0.005;
;;   _radius = (noise(_radiusnoise) * 550) +1;
;;   _angnoise += 0.005;
;;   _angle += (noise(_angnoise) * 6) - 3;
;;   if (_angle > 360) { _angle -= 360; }
;;   if (_angle < 0) { _angle += 360; }
;;   _xnoise += 0.01;
;;   _ynoise += 0.01;
;;   float centerX = width/2 + (noise(_xnoise) * 100) - 50;
;;   float centerY = height/2 + (noise(_ynoise) * 100) - 50;
;;   float rad = radians(_angle);
;;   float x1 = centerX + (_radius * cos(rad));
;;   float y1 = centerY + (_radius * sin(rad));
;;   float opprad = rad + PI;
;;   float x2 = centerX + (_radius * cos(opprad));
;;   float y2 = centerY + (_radius * sin(opprad));
;;   _strokeCol += _strokeChange;
;;   if (_strokeCol > 254) { _strokeChange = -1; }
;;   if (_strokeCol < 0) { _strokeChange = 1; }
;;   stroke(_strokeCol, 60);
;;   strokeWeight(1);
;;   line(x1, y1, x2, y2);
;; }

(defn mk-lines-stream
  []
  (let [half-width   (/ (width) 2)
        half-height  (/ (height) 2)
        radius-steps (steps (random 10) 0.005)
        angle-steps  (steps (random 10) 0.005)
        x-steps      (steps (random 10) 0.01)
        x-noises     (map noise x-steps)
        y-steps      (steps (random 10) 0.01)
        y-noises     (map noise y-steps)
        angle-noises (map noise angle-steps)
        angle-noises (mul-add angle-noises 6 -3)
        angles       (steps (- (/ PI 2)) angle-noises)
        angles       (map #(mod % 360) angles)
        rads         (map radians angles)
        center-xs    (mul-add x-noises 100 (- half-width 50))
        center-ys    (mul-add y-noises 100 (- half-height 50))
        radii        (map noise radius-steps)
        radii        (mul-add radii 550 1)
        cos-rads     (map cos rads)
        sin-rads     (map sin rads)
        opp-rads     (map #(+ PI %) rads)
        cos-opp-rads (map cos opp-rads)
        sin-opp-rads (map sin opp-rads)
        x1s          (mul-add cos-rads radii center-xs)
        y1s          (mul-add sin-rads radii center-ys)
        x2s          (mul-add cos-opp-rads radii center-xs)
        y2s          (mul-add sin-opp-rads radii center-ys)
        lines        (map list x1s y1s x2s y2s)]
    (seq->stream lines)))

(defn mk-cols-stream
  []
  (let [stroke-cols (cycle-between 0 255)]
    (seq->stream stroke-cols)))

(defn setup []
  (smooth)
  (frame-rate 30)
  (background 255)
  (no-fill)
  (stroke-weight 3)
  (set-state! :lines-str (mk-lines-stream)
              :cols-str (mk-cols-stream)))

(defn draw []
  (let [lines-str (state :lines-str)
        cols-str  (state :cols-str)
        line-args (lines-str)
        col       (cols-str)]
    (stroke col 60)
    (apply line line-args)))

(defsketch gen-art-19
  :title "Wave Clock"
  :setup setup
  :draw draw
  :size [500 300])
