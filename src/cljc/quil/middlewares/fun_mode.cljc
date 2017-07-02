(ns quil.middlewares.fun-mode
  (:require [quil.core :as q]))

(defn- wrap-setup [options]
  (let [setup (:setup options (fn [] nil))]
    (assoc options
           :setup #(reset! (q/state-atom) (setup)))))

(defn- wrap-draw-update [options]
  (let [draw (:draw options (fn [_]))
        update (:update options identity)
        quil-draw #(-> (q/state-atom)
                       (swap! (if (= (q/frame-count) 1)
                                identity
                                update))
                       (draw))]
    (-> options
        (dissoc :update)
        (assoc :draw quil-draw))))

(defn- mouse-event []
  {:x (q/mouse-x)
   :y (q/mouse-y)})

(defn- mouse-event-full []
  {:x (q/mouse-x)
   :y (q/mouse-y)
   :button (q/mouse-button)})

(defn- key-event []
  {:key (q/key-as-keyword)
   :key-code (q/key-code)
   :raw-key (q/raw-key)
   #?@(:clj [:modifiers (q/key-modifiers)])})

(defn- wrap-handler
  ([options handler-key]
   (wrap-handler options handler-key nil))
  ([options handler-key event-fn]
   (if-let [handler (options handler-key)]
     (assoc options handler-key
            (if event-fn
              #(swap! (q/state-atom) handler (event-fn))
              #(swap! (q/state-atom) handler)))
     options)))

(defn- wrap-handlers [options & handlers]
  (reduce (fn [options handler]
            (if (keyword? handler)
              (wrap-handler options handler)
              (apply wrap-handler options handler)))
          options handlers))

(defn- wrap-mouse-wheel [options]
  (if-let [handler (:mouse-wheel options)]
    (assoc options :mouse-wheel
           (fn [rotation]
             (swap! (q/state-atom) handler rotation)))
    options))

(defn fun-mode
  "Introduces function mode making all handlers (setup, draw, mouse-click, etc)
  state-aware. Adds support for 'update' function."
  [options]
  (-> options
      wrap-setup
      wrap-draw-update
      (wrap-handlers :focus-gained :focus-lost [:mouse-entered mouse-event]
                     [:mouse-exited mouse-event] [:mouse-pressed mouse-event-full]
                     [:mouse-released mouse-event] [:mouse-clicked mouse-event-full]
                     [:mouse-moved (fn [] {:x (q/mouse-x) :y (q/mouse-y)
                                           :p-x (q/pmouse-x) :p-y (q/pmouse-y)})]
                     [:mouse-dragged (fn [] {:x (q/mouse-x) :y (q/mouse-y)
                                             :p-x (q/pmouse-x) :p-y (q/pmouse-y)
                                             :button (q/mouse-button)})]
                     [:key-pressed key-event] [:key-released key-event] [:key-typed key-event]
                     :on-close)
      wrap-mouse-wheel))
