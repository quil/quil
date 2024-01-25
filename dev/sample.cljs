(ns sample
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:color 0
   :angle 0})

(defn update-state [state]
  (-> state
      (update :color #(mod (+ % 0.7) 255))
      (update :angle #(mod (+ % 0.1) q/TWO-PI))))

(defn draw-state [state]
  (q/background 240)
  (q/fill (:color state) 255 255)
  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      (q/ellipse x y 100 100))))

(q/defsketch my
  :host "host"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])

