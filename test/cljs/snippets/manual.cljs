(ns snippets.manual
  (:require [quil.core :as q :include-macros true]
            [quil.middlewares.fun-mode :as fm]))

(defn ^:export sketch-start [id]
  (q/with-sketch (q/get-sketch-by-id id)
    (q/start-loop)))

(defn ^:export sketch-stop [id]
  (q/with-sketch (q/get-sketch-by-id id)
    (q/no-loop)))

(q/defsketch redraw-on-key
  :size [500 200]
  :setup q/no-loop
  :draw (fn []
          (q/fill 0)
          (q/background 220)
          (q/text (str "This sketch should show current time but update it only on key press.") 20 20)
          (q/text (str (q/hour) ":" (q/minute) ":" (q/seconds)) 20 50))
  :key-pressed #(q/redraw))


(defn single-fn [n]
  (fn [state]
    (q/background 255)
    (q/text (str n) 50 20)
    (q/text (str state) 50 50)
    state))

(defn double-fn [n]
  (fn [state event]
    (q/background 255)
    (q/text (str n) 50 20)
    (q/text (str event) 50 35)
    (q/text (str state) 50 50)
    state))

(q/defsketch fun-mode
  :size [500 500]
  :setup (fn []
           (q/frame-rate 30)
           (q/fill 0)
           {:round 0})
  :update (fn [state] (update-in state [:round] inc))
  :focus-gained (single-fn :focus-gained)
  :focus-lost (single-fn :focus-lost)
  :mouse-entered (double-fn :mouse-entered)
  :mouse-exited (double-fn :mouse-exited)
  :mouse-pressed (double-fn :mouse-pressed)
  :mouse-released (double-fn :mouse-released)
  :mouse-clicked (double-fn :mouse-clicked)
  :mouse-moved (double-fn :mouse-moved)
  :mouse-dragged (double-fn :mouse-dragged)
  :mouse-wheel (double-fn :mouse-wheel)
  :key-pressed (double-fn :key-pressed)
  :key-released (single-fn :key-released)
  :key-typed (double-fn :key-typed)
  :middleware [fm/fun-mode])

(q/defsketch extern-control
  :size [500 500]
  :draw (fn []
          (q/background 255)

          (q/stroke 200 60 5)
          (q/stroke-weight 2)
          (q/line 0 150 (- 300 (mod (q/millis) 300))
                  (- 300 (mod (q/millis) 300)))

          (q/stroke 60 50 200)
          (q/stroke-weight 3)
          (q/line (mod (q/millis) 300) (mod (q/millis) 300)
                  300 150)))
