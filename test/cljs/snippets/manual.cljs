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
  ; Just to try :settings instead of usual :setup
  :settings q/no-loop
  :draw (fn []
          (q/fill 0)
          (q/background 220)
          (q/text (str "This sketch should show current time but update it only on key press.") 20 20)
          (q/text (str (q/hour) ":" (q/minute) ":" (q/seconds)) 20 50))
  :key-pressed #(q/redraw))

(defn add-event [state data]
  (update-in state [:last-events]
             #(take 10 (cons data %))))

(defn print-state [state]
  (q/text (str ":round " (:round state)) 50 50)
  (doseq [[ind event] (map vector
                           (range)
                           (:last-events state))]
    (q/text (pr-str event) 50 (+ 70 (* ind 15)))))

(defn single-fn [n]
  (fn [state]
    (q/background 255)
    (q/text (pr-str n) 50 20)
    (print-state state)
    (add-event state n)))

(defn double-fn [n]
  (fn [state event]
    (q/background 255)
    (q/text (pr-str n) 50 20)
    (q/text (pr-str event) 50 35)
    (print-state state)
    (add-event state [n event])))

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

(q/defsketch external-control
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

(q/defsketch get-pixel
  :site [500 500]
  :draw (fn []
          (q/background 255)
          (let [gr (q/create-graphics 100 100)]
            (q/with-graphics gr
              (q/background 255)
              (q/fill 127 255 180)
              (q/ellipse 50 50 70 70))

            (q/image gr 0 0)

            (q/image (q/get-pixel gr) 0 120)
            (q/fill (q/get-pixel gr 50 50))
            (q/rect 120 120 100 100)
            (q/image (q/get-pixel gr 0 0 50 50) 240 120)

            (q/image (q/get-pixel) 400 400)
            (q/fill (q/get-pixel 50 50))
            (q/rect 120 240 100 100)
            (q/image (q/get-pixel 0 0 50 50) 240 240))))

(q/defsketch set-pixel
  :size [500 500]
  :draw (fn []
          (q/background 255)
          (let [gr (q/create-graphics 100 100)]
            (q/with-graphics gr
              (q/background 255))

            (doseq [i (range 30)
                    j (range 30)]
              (q/set-pixel gr i j (q/color (* 7 i) (* 7 j) 0)))
            (q/update-pixels gr)
            (q/image gr 0 0)

            (doseq [i (range 30)
                    j (range 30)]
              (q/set-pixel (+ 40 i) (+ 40 j) (q/color 0 (* 7 i) (* 7 j)))))))


(q/defsketch pixels-update-pixels
  :size [500 500]
  :draw (fn []
          (q/background 55)
          (let [gr (q/create-graphics 100 100)]
            (q/with-graphics gr
              (q/background 55))

            (let [px (q/pixels gr)]
              (dotimes [i 100]
                (aset px (* i i) (q/color 255))))
            (q/update-pixels gr)

            (q/image gr 10 10))))

(let [counter (atom 0)]
  (q/defsketch global-key-events
    :size [500 500]
    :key-pressed (fn []
                   (swap! counter inc))
    :features [:global-key-events]
    :draw (fn []
            (q/background 255)
            (q/text-size 30)
            (q/fill 0)
            (q/text-align :center :center)
            (q/text (str "Key events: " @counter) 250 250))))
