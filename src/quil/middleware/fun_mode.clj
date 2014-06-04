(ns quil.middleware.fun-mode
  (:require [quil.core :as q]
            [quil.applet :as ap]))


(defn- state-atom []
  (-> (ap/current-applet) meta :state))

(defn- wrap-setup [options]
  (let [setup (:setup options (fn [] nil))]
    (assoc options
      :setup #(reset! (state-atom) (setup)))))

(defn- wrap-draw-update [options]
  (let [draw (:draw options (fn [_]))
        update (:update options identity)]
    (-> options
        (dissoc :update)
        (assoc :draw #(-> (state-atom)
                          (swap! update)
                          (draw))))))

(defn- mouse-event []
  {:x (q/mouse-x)
   :y (q/mouse-y)})

(defn- mouse-event-full []
  (assoc (mouse-event)
    :button (q/mouse-button)))

(defn- key-event []
  {:key (q/key-as-keyword)
   :key-code (q/key-code)
   :raw-key (q/raw-key)})

(defn- wrap-handler
  ([options handler-key]
     (wrap-handler options handler-key nil))
  ([options handler-key event-fn]
     (if-let [handler (options handler-key)]
       (assoc options handler-key
              (fn []
                (if event-fn
                  (swap! (state-atom) handler (event-fn))
                  (swap! (state-atom) handler))))
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
             (swap! (state-atom) handler rotation)))
    options))

(defn fun-mode [options]
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
                     [:key-pressed key-event] :key-released [:key-typed key-event]
                     :on-close)
      wrap-mouse-wheel))
