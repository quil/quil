(ns key-mover
	(:use quil.core)
  (:import java.awt.event.KeyEvent))

(def params {
	:screen-dimensions [400 400]
	:background-colour 0
	:blob-colour 255
  :blob-radius 10
  :screen-bounds [0 380]})

(def blob-location (atom [190 190]))

(defn normalise
  ([bounds position] (normalise bounds bounds position))

  ([x-bounds y-bounds position]
  (let [[min-x max-x] x-bounds
        [min-y max-y] y-bounds
        [x y] position
        bound (fn [a-min a-max value]
                (max a-min (min a-max value)))
        new-x (bound min-x max-x x)
        new-y (bound min-y max-y y)]
       (vector new-x new-y))))

(defn setup []
  (smooth)
  (no-stroke)
  (no-loop))

(defn draw
  []
  (let [blob-size (* 2 (params :blob-radius))
    [bx by] @blob-location]
  (background-float (params :background-colour))
  (fill (params :blob-colour))
  (rect bx by blob-size blob-size)
  (text "Use WASD and arrow keys to move" 10 390)))

(def valid-keys {
  KeyEvent/VK_UP :up
  KeyEvent/VK_DOWN :down
  KeyEvent/VK_LEFT :left
  KeyEvent/VK_RIGHT :right
  \w :up
  \s :down
  \a :left
  \d :right})

(def moves {:up [0 -10]
  :down [0 10]
  :left [-10 0]
  :right [10 0]
  :still [0 0]})

(defn charcode-to-keyword [c] (->> c str keyword))

(defn change-location [delta current-position]
  (map #(reduce + %) (split-at 2 (interleave delta current-position))))

(defn key-press []
  (let [raw-key (raw-key)
    the-key-code (key-code)
    the-key-pressed (if (= processing.core.PConstants/CODED (int raw-key)) the-key-code raw-key)
    move (moves (get valid-keys the-key-pressed :still))]
    (swap! blob-location (partial change-location move))
    (swap! blob-location (partial normalise (params :screen-bounds)))
    (redraw)))

(defsketch key-listener
  :title "Keyboard arrow keys demo"
  :size (params :screen-dimensions)
  :setup setup
  :draw draw
  :target :perm-frame 
  :key-pressed key-press)
