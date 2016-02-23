(ns ^:manual
  manual
  (:require [quil.core :refer :all]
            [quil.middlewares.fun-mode :as fm]
            [clojure.test :refer [deftest]]))

(defn draw-text-fn [& txt]
  (fn []
    (fill 0)
    (background 255)
    (text (apply str txt) 20 20)))

(defn resizable-and-keep-on-top []
  (let [lock (promise)]
    (sketch
     :title "Resizable and keep-on-top"
     :draw (draw-text-fn "Sketch should be resizable and keep-on-top.")
     :size [500 500]
     :features [:resizable :keep-on-top]
     :on-close #(deliver lock true))
    @lock))

(defn no-loop-with-start-loop []
  (let [lock (promise)]
    (sketch
     :title "no-loop with start-loop"
     :draw (fn []
             (fill 0)
             (background 255)
             (text (str "This sketch should update seconds fields until it reaches number divisible by 10.\n"
                        "After this it should stop updating. To continue updating press any key")
                   20 20)
             (let [sec (seconds)]
               (text (str sec) 20 50)
               (when (zero? (rem sec 10))
                 (no-loop))))
     :on-close #(deliver lock true)
     :key-pressed #(start-loop))
    @lock))

(defn redraw-on-key []
  (let [lock (promise)]
    (sketch
     :title "no-loop with start-loop"
     :setup #(no-loop)
     :draw (fn []
             (fill 0)
             (background 255)
             (text (str "This sketch should show current time but update it only on key press.")
                   20 20)
             (text (format "%02d:%02d:%02d" (hour) (minute) (seconds))
                   20 50))
     :on-close #(deliver lock true)
     :key-pressed #(redraw))
    @lock))

(defn fullscreen []
  (let [lock (promise)]
    (sketch
     :title "Fullscreen"
     :draw (draw-text-fn "Sketch should be fullscreen and not in present mode.")
     :size :fullscreen
     :on-close #(deliver lock true))
    @lock))

(defn present-and-bgcolor []
  (let [lock (promise)]
    (sketch
     :title "Present and bgcolor"
     :draw (draw-text-fn "Sketch should in present mode with cyan background around sketch.")
     :size [500 500]
     :features [:present]
     :bgcolor "#00FFFF"
     :on-close #(deliver lock true))
    @lock))

(defn on-close-and-exit-on-close []
  (let [lock (promise)]
    (sketch
     :title "on-close and exit-on-close"
     :draw (draw-text-fn "This is last test. When you close sketch JVM should shut down \nand you won't see tests summary.\n"
                         "Also you should see 'on-close called' message in console.")
     :on-close (fn [] (println "on-close called"))
     :features [:exit-on-close])
    @lock))

(defn fun-mode []
  (letfn [(setup []
            (frame-rate 30)
            (fill 0)
            {:round 0})
          (update [state]
            (update-in state [:round] inc))
          (single-fn [name]
            (fn [state]
              (background 255)
              (text (str name) 50 20)
              (text (pr-str state) 50 100)
              state))
          (double-fn [name]
            (fn [state event]
              (background 255)
              (text (str name) 50 20)
              (text (pr-str event) 50 55)
              (text (pr-str state) 50 100)
              state))]
    (let [lock (promise)]
     (sketch
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
      :key-released (single-fn :key-released)
      :key-typed (double-fn :key-typed)
      :on-close #(do (println ":on-close in fun-mode. State is:" %) (deliver lock true))
      :middleware [fm/fun-mode])
     @lock)))

(deftest run-all
  (doseq [fn [resizable-and-keep-on-top
              fullscreen present-and-bgcolor
              no-loop-with-start-loop
              redraw-on-key
              fun-mode
              on-close-and-exit-on-close]]
    (fn)))
