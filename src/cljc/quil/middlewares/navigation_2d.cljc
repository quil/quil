(ns quil.middlewares.navigation-2d
  (:require [quil.core :as q :include-macros true]))

(def ^:private ^String missing-navigation-key-error
  (str "state map is missing :navigation-2d key. "
       "Did you accidentally removed it from the state in "
       ":update or any other handler?"))

(defn- assert-state-has-navigation
  "Asserts that state map contains :navigation-2d object."
  [state]
  (when-not (:navigation-2d state)
    (throw #?(:clj (RuntimeException. missing-navigation-key-error)
              :cljs (js/Error. missing-navigation-key-error)))))

(defn- default-position
  "Default position configuration: zoom is neutral and central point is
  width/2, height/2."
  []
  {:position [(/ (q/width) 2.0)
              (/ (q/height) 2.0)]
   :zoom 1})

(defn- setup-2d-nav
  "Custom 'setup' function which creates initial position
  configuration and puts it to the state map."
  [user-setup user-settings]
  (let [initial-state (-> user-settings
                          (select-keys [:position :zoom])
                          (->> (merge (default-position))))]
    (update-in (user-setup) [:navigation-2d]
               #(merge initial-state %))))

(defn- mouse-dragged
  "Changes center of the sketch depending on the last mouse move. Takes
  zoom into account as well."
  [state event]
  (assert-state-has-navigation state)
  (let [dx (- (:p-x event) (:x event))
        dy (- (:p-y event) (:y event))
        zoom (-> state :navigation-2d :zoom)]
    (-> state
        (update-in [:navigation-2d :position 0] + (/ dx zoom))
        (update-in [:navigation-2d :position 1] + (/ dy zoom)))))

(defn- mouse-wheel
  "Changes zoom settings based on scroll."
  [state event]
  (assert-state-has-navigation state)
  (update-in state [:navigation-2d :zoom] * (+ 1 (* -0.1 event))))

(defn- draw
  "Calls user draw function with necessary all transformations (position
  and zoom) applied."
  [user-draw state]
  (assert-state-has-navigation state)
  (q/push-matrix)
  (let [nav-2d (:navigation-2d state)
        zoom (:zoom nav-2d)
        pos (:position nav-2d)]
    (q/scale zoom)
    (q/with-translation [(- (/ (q/width) 2 zoom) (first pos))
                         (- (/ (q/height) 2 zoom) (second pos))]
      (user-draw state)))
  (q/pop-matrix))

(defn navigation-2d
  "Enables navigation over 2D sketch. Dragging mouse will move center of the
  skecth and mouse wheel controls zoom."
  [options]
  (let [; 2d-navigation related user settings
        user-settings (:navigation-2d options)

        ; user-provided handlers which will be overridden
        ; by 3d-navigation
        user-draw (:draw options (fn [state]))
        user-mouse-dragged (:mouse-dragged options (fn [state _] state))
        user-mouse-wheel (:mouse-wheel options (fn [state _] state))
        setup (:setup options (fn [] {}))]
    (assoc options

           :setup (partial setup-2d-nav setup user-settings)

           :draw (partial draw user-draw)

           :mouse-dragged (fn [state event]
                            (user-mouse-dragged (mouse-dragged state event) event))
           :mouse-wheel (fn [state event]
                          (user-mouse-wheel (mouse-wheel state event) event)))))
