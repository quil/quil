(ns ^:manual
 manual
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as fm]
            [clojure.test :refer [deftest]]))

(defn draw-text [& txt]
  (q/fill 0)
  (q/background 255)
  (q/text-size 20)
  (q/fill 0)
  (q/text-align :center :center)
  (q/text (apply str txt)
          (/ (q/width) 2)
          (/ (q/height) 2)))

(defn resizable-and-keep-on-top []
  (let [lock (promise)]
    (q/sketch
     :title "Resizable and keep-on-top"
     :draw #(draw-text "Sketch should be resizable and keep-on-top.")
     :size [500 500]
     :features [:resizable :keep-on-top]
     :on-close #(deliver lock true))
    @lock))

(defn no-loop-with-start-loop []
  (let [lock (promise)]
    (q/sketch
     :title "no-loop with start-loop"
     :draw (fn []
             (q/fill 0)
             (q/background 255)
             (q/text (str "This sketch should update seconds fields until it reaches number divisible by 10.\n"
                          "After this it should stop updating. To continue updating press any key")
                     20 20)
             (let [sec (q/seconds)]
               (q/text (str sec) 20 50)
               (when (zero? (rem sec 10))
                 (q/no-loop)))
             (q/text (str "Looping: " (q/looping?)) 20 65))
     :on-close #(deliver lock true)
     :key-pressed #(q/start-loop))
    @lock))

(defn redraw-on-key []
  (let [lock (promise)]
    (q/sketch
     :title "no-loop with start-loop"
     :setup #(q/no-loop)
     :draw (fn []
             (q/fill 0)
             (q/background 255)
             (q/text (str "This sketch should show current time but update it only on key press.")
                     20 20)
             (q/text (format "%02d:%02d:%02d" (q/hour) (q/minute) (q/seconds))
                     20 50))
     :on-close #(deliver lock true)
     :key-pressed #(q/redraw))
    @lock))

(defn fullscreen []
  (let [lock (promise)]
    (q/sketch
     :title "Fullscreen"
     :draw #(draw-text "Sketch should be fullscreen\nand not in present mode.")
     :size :fullscreen
     :on-close #(deliver lock true))
    @lock))

(defn present-and-bgcolor []
  (let [lock (promise)]
    (q/sketch
     :title "Present and bgcolor"
     :draw #(draw-text "Sketch should in present mode with\ncyan background around sketch.")
     :size [500 500]
     :features [:present]
     :bgcolor "#00FFFF"
     :on-close #(deliver lock true))
    @lock))

(defn on-close-and-exit-on-close []
  (let [lock (promise)]
    (q/sketch
     :title "on-close and exit-on-close"
     :draw #(draw-text "This is last test.\nWhen you close sketch JVM should shut down \nand you won't see tests summary.\n"
                       "Also you should see 'on-close called'\nmessage in console.")
     :on-close (fn [] (println "on-close called"))
     :features [:exit-on-close])
    @lock))

(defn fun-mode []
  (letfn [(setup []
            (q/frame-rate 30)
            (q/fill 0)
            {:round 0})
          (update [state]
            (update-in state [:round] inc))
          (single-fn [name]
            (fn [state]
              (q/background 255)
              (q/text (str name) 50 20)
              (q/text (pr-str state) 50 100)
              state))
          (double-fn [name]
            (fn [state event]
              (q/background 255)
              (q/text (str name) 50 20)
              (q/text (pr-str event) 50 55)
              (q/text (pr-str state) 50 100)
              state))]
    (let [lock (promise)]
      (q/sketch
       :title "Functional mode"
       :size [500 500]
       :setup setup
       :update update
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
       :on-close #(do (println ":on-close in fun-mode. State is:" %) (deliver lock true))
       :middleware [fm/fun-mode])
      @lock)))

(defn resize-sketch []
  (let [lock (promise)]
    (q/sketch
     :title "Resize"
     :draw #(draw-text
             "On click sketch should increase size\nby 50px width and 20px height."
             "\nwidth: " (q/width)
             "\nheight: " (q/height))
     :mouse-clicked #(q/resize-sketch
                      (+ (q/width) 50)
                      (+ (q/height) 20))
     :size [500 500]
     :on-close #(deliver lock true))
    @lock))

(defn with-stroke []
  (let [lock (promise)]
    (q/sketch
     :size [800 500]
     :on-close #(deliver lock true)
     :setup q/no-loop
     :draw (fn []

             (q/stroke-weight 5)
             (q/fill 127)

             ;; no-stroke -> stroke -> no-stroke
             (let [base-x 100]
               (q/no-stroke)
               (q/rect base-x 100 90 90)
               (q/with-stroke [0 100 0]
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; stroke -> no stroke -> original stroke
             (let [base-x 200]
               (q/stroke [0 100 0])
               (q/rect base-x 100 90 90)
               (q/with-stroke nil
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; stroke -> different stroke -> original stroke
             (let [base-x 300]
               (q/stroke [0 100 0])
               (q/rect base-x 100 90 90)
               (q/with-stroke [180 100 0]
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; no-stroke -> no-stroke -> no-stroke
             (let [base-x 400]
               (q/no-stroke)
               (q/rect base-x 100 90 90)
               (q/with-stroke nil
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))))
    @lock))

(defn with-fill []
  (let [lock (promise)]
    (q/sketch
     :size [800 500]
     :on-close #(deliver lock true)
     :setup q/no-loop
     :draw (fn []
             (q/stroke-weight 5)
             (q/fill [0 10 10 90])

             ;; no-fill -> fill -> no-fill
             (let [base-x 100]
               (q/no-fill)
               (q/rect base-x 100 90 90)
               (q/with-fill [0 100 0]
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; fill -> no-fill -> fill
             (let [base-x 200]
               (q/fill [0 100 0])
               (q/rect base-x 100 90 90)
               (q/with-fill nil
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; fill -> different fill -> original fill
             (let [base-x 300]
               (q/fill [0 100 0])
               (q/rect base-x 100 90 90)
               (q/with-fill [180 100 0]
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))

             ;; no-fill -> no-fill -> no-fill
             (let [base-x 400]
               (q/no-fill)
               (q/rect base-x 100 90 90)
               (q/with-fill nil
                 (q/rect base-x 200 90 90))
               (q/rect base-x 300 90 90))))
    @lock))

(deftest run-all
  (doseq [fn [resizable-and-keep-on-top
              fullscreen present-and-bgcolor
              no-loop-with-start-loop
              redraw-on-key
              fun-mode
              resize-sketch
              with-stroke
              with-fill
              on-close-and-exit-on-close]]
    (fn)))

