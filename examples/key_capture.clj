(ns key-capture
	(:use quil.core))

(def current-text-size (atom 20))

(def params {:big-text-size 30
	:background-color 25
	:foreground-color 200})

(defn setup []
  (smooth)
  (no-stroke)
  (set-state! :message (atom "Click on screen and type a key")))

(defn draw
  []
  (background-float (params :background-color))
  (stroke-weight 20)
  (stroke-float 10)
  (fill (params :foreground-color))
  (text-size @current-text-size)
  (text @(state :message) 20 60))

(defn key-press []
	(let [big-text-size (params :big-text-size)]
	(if (< @current-text-size big-text-size) (reset! current-text-size big-text-size))
	(reset! (state :message) (str "Key pressed: " (raw-key)))))

(defsketch key-listener
  :title "Keyboard listener example"
  :size [400 100]
  :setup setup
  :draw draw
  :key-typed key-press)