(ns quil.examples.graphics
  (:use quil.core
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.seqs :only [range-incl steps]]))

;; Example of using graphics via create-graphics and with-graphics.
;; On each iteration 1 spiral will be drawn on graphics and then we tile all screen using this graphics.
;; Graphics created in setup funcion and stored in state.
;; Spiral is drawn by draw-spiral function that uses standard draw functions.
;; If draw-spiral function invoked inside 'with-graphics' macro then spiral will be drawn on given graphics,
;; otherwise spiral is drawon on applet.

(def spiral-size 100)

(def cent-x (/ spiral-size 2))
(def cent-y (/ spiral-size 2))


(defn draw-spiral
  "Draws spiral on current surface: on applet or on graphics if inside with-graphics macro."
  []
  (with-translation [cent-x cent-y]
    (with-rotation [(/ (frame-count) -5 Math/PI)]
      (background 255)
      (stroke-weight 2)
      (smooth)
      (let [radius 20
            radians (map radians (steps 0 5))
            radii (range 5 (/ spiral-size 2) 0.1)
            xs (map (fn [radians radius] (* radius (cos radians))) radians radii)
            ys (map (fn [radians radius] (* radius (sin radians))) radians radii)
            line-args (line-join-points xs ys)]
        (stroke 0 30)
        (no-fill)
        (ellipse cent-x cent-y (* radius 2) (* radius 2))
        (stroke 20 50 70)
        (dorun (map #(apply line %) line-args))))))

(defn setup
  "Create graphics in setup and store it in state."
  []
  (let [gr (create-graphics spiral-size spiral-size :java2d)]
   (set-state! :spiral gr)))

(defn draw []
  (let [gr (state :spiral)]
    ; Draw spiral on graphics.
    (with-graphics gr
      (draw-spiral))
    ; Tile screen with spirals using graphics.
    (doseq [x (range 0 (width) spiral-size)
            y (range 0 (height) spiral-size)]
      (image gr x y))))

(defn run []
  (sketch :setup setup
          :draw draw
          :size [500 500]))
