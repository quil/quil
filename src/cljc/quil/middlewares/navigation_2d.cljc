(ns quil.middlewares.navigation-2d
  (:require [quil.core :as q :include-macros true]))

(defn- default-position
  "Default position configuration: zoom is neutral and central point is
  `width/2, height/2`."
  []
  {:position [(/ (q/width) 2.0)
              (/ (q/height) 2.0)]
   :zoom 1
   :mouse-buttons #{:left :right :center}})

(defn- setup-2d-nav
  "Custom 'setup' function which creates initial position
  configuration and puts it to the state map."
  [user-setup user-settings]
  (let [initial-state (-> user-settings
                          (select-keys [:position :zoom :mouse-buttons])
                          (->> (merge (default-position))))]
    (assoc initial-state :user-state (user-setup))))

(defn- mouse-dragged
  "Changes center of the sketch depending on the last mouse move. Takes
  zoom into account as well."
  [state event]
  (let [mouse-buttons (state :mouse-buttons)]
    (if (contains? mouse-buttons (:button event))
      (let [dx (- (:p-x event) (:x event))
            dy (- (:p-y event) (:y event))
            zoom (state :zoom)]
        (-> state
            (update-in [:position 0] + (/ dx zoom))
            (update-in [:position 1] + (/ dy zoom))))
      state)))

(defn- mouse-wheel
  "Changes zoom settings based on scroll."
  [state event]
  (update state :zoom * (+ 1 (* -0.1 event))))

(defn- draw
  "Calls user draw function with all necessary transformations (position
  and zoom) applied."
  [user-draw state]
  (q/push-matrix)
  (let [zoom (:zoom state)
        pos (:position state)]
    (q/scale zoom)
    (q/with-translation [(- (/ (q/width) 2 zoom) (first pos))
                         (- (/ (q/height) 2 zoom) (second pos))]
      (user-draw (:user-state state))))
  (q/pop-matrix))

(defn- update-2d-nav [user-update state]
  (update state :user-state user-update))

(defn navigation-2d
  "Enables navigation over 2D sketch. Dragging mouse will move center of the
  sketch and mouse wheel controls zoom."
  [options]
  (let [; 2d-navigation related user settings
        user-settings (:navigation-2d options)

        ; user-provided handlers which will be overridden
        ; by 3d-navigation
        user-draw (:draw options (fn [state]))
        user-mouse-dragged (:mouse-dragged options (fn [state _] state))
        user-mouse-wheel (:mouse-wheel options (fn [state _] state))
        setup (:setup options (fn [] {}))
        user-update (:update options (fn [state] state))]
    (assoc options

           :setup (partial setup-2d-nav setup user-settings)

           :update (partial update-2d-nav user-update)

           :draw (partial draw user-draw)

           :mouse-dragged (fn [state event]
                            (user-mouse-dragged (mouse-dragged state event) event))
           :mouse-wheel (fn [state event]
                          (user-mouse-wheel (mouse-wheel state event) event)))))
