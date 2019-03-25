(ns quil.manual
  (:require [quil.core :as q :include-macros true]
            [quil.middlewares.fun-mode :as fm]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn update-looping
  "Updates 'Looping' status for the external-control test."
  [id]
  (q/with-sketch (q/get-sketch-by-id id)
    (set! (.-innerText (.querySelector js/document "#looping-status"))
          (str (q/looping?)))))

(defn sketch-start [id]
  (q/with-sketch (q/get-sketch-by-id id)
    (q/start-loop))
  (update-looping id))

(defn sketch-stop [id]
  (q/with-sketch (q/get-sketch-by-id id)
    (q/no-loop))
  (update-looping id))

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

(defn start-all-sketches []
  (q/sketch :host "redraw-on-key"
            :size [500 200]
            ; Just to try :settings instead of usual :setup
            :settings q/no-loop
            :draw (fn []
                    (q/fill 0)
                    (q/background 220)
                    (q/text (str (q/hour) ":" (q/minute) ":" (q/seconds)) 20 20))
            :key-pressed #(q/redraw))

  (q/sketch :host "fun-mode"
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
            :key-released (double-fn :key-released)
            :key-typed (double-fn :key-typed)
            :middleware [fm/fun-mode])

  (q/sketch
   :host "external-control"
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

  (q/sketch
   :host "get-pixel"
   :site [500 500]
   :draw (fn []
           (q/background 255)
           (let [gr (q/create-graphics 100 100)]

             ; draw ellipse in gr object
             (q/with-graphics gr
               (q/background 255)
               (q/fill 127 255 180)
               (q/ellipse 50 50 70 70))

             ; draw gr object on screen
             (q/image gr 0 0)

             ; draw gr object on screen using get-pixel
             (q/image (q/get-pixel gr) 0 120)

             ; set fill color to the same as ellipse and
             ; draw rectangle
             (q/fill (q/get-pixel gr 50 50))
             (q/rect 120 0 100 100)

             ; draw up left quarter of ellipse on screen
             (q/image (q/get-pixel gr 0 0 50 50) 120 120)

             ; set fill to white using get-pixel of [0, 0]
             ; point of the screen and draw white rectangle
             (q/fill (q/get-pixel 0 0))
             (q/rect 240 0 100 100))
           (q/no-loop)))

  (q/sketch
   :host "set-pixel"
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
               (q/set-pixel (+ 40 i) (+ 40 j) (q/color 0 (* 7 i) (* 7 j)))))
           (q/no-loop)))

  (q/sketch
   :host "pixels-update-pixels"
   :size [500 500]
   :draw (fn []
           (q/background 55)
           (let [gr (q/create-graphics 100 100)]
             (q/with-graphics gr
               (q/background 55))

             (let [px (q/pixels gr)
                   c  (q/color 255)
                   r  (q/red c)
                   g  (q/green c)
                   b  (q/blue c)
                   a  (q/alpha c)]
               (dotimes [i 100]
                 (let [d (q/display-density)
                       p (* 4 i i d d)]
                   (aset px p r)
                   (aset px (+ 1 p) g)
                   (aset px (+ 2 p) b)
                   (aset px (+ 3 p) a))))
             (q/update-pixels gr)

             (q/image gr 10 10))
           (q/no-loop)))

  (q/sketch
   :host "mouse-and-key-pressed-variable"
   :size [500 500]
   :draw (fn []
           (q/background 240)
           (q/fill 0 0 0)
           (q/text (str "mouse pressed: "  (q/mouse-pressed?)) 0 20)
           (q/text (str "key pressed: " (q/key-pressed?)) 0 40)))
  (q/sketch
   :host "resizing"
   :size [500 500]
   :draw (fn []
           (q/background 240)
           (q/text-size 30)
           (q/fill 0)
           (q/text-align :center :center)
           (q/text (str "width: " (q/width)
                        "\nheight: " (q/height))
                   (/ (q/width) 2)
                   (/ (q/height) 2)))))

(defn resize-sketch []
  (.setTimeout js/window
               #(q/with-sketch (q/get-sketch-by-id "resizing")
                  (q/resize-sketch 500 500))
               2000)
  (q/with-sketch (q/get-sketch-by-id "resizing")
    (q/resize-sketch 700 700)))

(defn init []
  (events/listen (.querySelector js/document "#external-control-start")
                 EventType/CLICK
                 #(sketch-start "external-control"))
  (events/listen (.querySelector js/document "#external-control-stop")
                 EventType/CLICK
                 #(sketch-stop "external-control"))
  (events/listen (.querySelector js/document "#resize-button")
                 EventType/CLICK
                 resize-sketch)
  (start-all-sketches))

(events/listenOnce js/window EventType/LOAD
                   #(when (= (-> js/document
                                 (.-body)
                                 (.-dataset)
                                 (aget "page"))
                             "manual")
                      (init)))

