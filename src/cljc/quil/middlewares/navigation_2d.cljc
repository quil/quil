(ns quil.middlewares.navigation-2d
  (:require [quil.core :as q :include-macros true]))

(def ^:private ^String missing-navigation-key-error
  (str "state map is missing :navigation-2d key. "
       "Did you accidentally removed it from the state in "
       ":update or any other handler?"))

(defn- assert-state-has-navigation
  "Asserts that `state` map contains `:navigation-2d` object."
  [state]
  (when-not (:navigation-2d state)
    (throw #?(:clj (RuntimeException. missing-navigation-key-error)
              :cljs (js/Error. missing-navigation-key-error)))))

(defn- default-settings
  "Default configuration: zoom is neutral, central point is
  `width/2, height/2`, mouse-buttons is #{:left :right :center},
  and wrap-draw is true."
  []
  {:position [(/ (q/width) 2.0)
              (/ (q/height) 2.0)]
   :zoom 1
   :mouse-buttons #{:left :right :center}
   :wrap-draw true})

(defn- setup-2d-nav
  "Custom 'setup' function which creates initial navigation-2d
  configuration and puts it into the state map."
  [user-setup user-settings]
  (update (user-setup) :navigation-2d
          #(merge (default-settings) user-settings %)))

(defn- mouse-dragged
  "Changes center of the sketch depending on the last mouse move. Takes
  zoom into account as well."
  [state event]
  (assert-state-has-navigation state)
  (let [mouse-buttons (-> state :navigation-2d :mouse-buttons)]
    (if (contains? mouse-buttons (:button event))
      (let [dx (- (:p-x event) (:x event))
            dy (- (:p-y event) (:y event))
            zoom (-> state :navigation-2d :zoom)]
        (-> state
            (update-in [:navigation-2d :position 0] + (/ dx zoom))
            (update-in [:navigation-2d :position 1] + (/ dy zoom))))
      state)))

(defn- mouse-wheel
  "Changes zoom settings based on scroll."
  [state event]
  (assert-state-has-navigation state)
  (update-in state [:navigation-2d :zoom] * (+ 1 (* -0.1 event))))

(defn world->screen-coords
  "Returns the pixel coordinates that [x y] gets mapped to by the
  navigation transformation. i.e. if the user's draw function
  tries to draw something at [x y], it will actually get drawn at
  (world->screen-coords state [x y]) on the screen."
  [state [x y]]
  (assert-state-has-navigation state)
  (let [{zoom :zoom pos :position} (:navigation-2d state)]
      [(+ (* zoom x) (- (/ (q/width) 2.) (* zoom (first pos))))
       (+ (* zoom y) (- (/ (q/height) 2.) (* zoom (second pos))))]))

(defn screen->world-coords
  "Returns coordinates that get mapped to [x y] by the navigation
  transformation. i.e. if something is being drawn at pixel coordinates
  [x y] on the screen, then the user's draw function must have tried
  to draw it at (screen->world-coords state [x y])."
  [state [x y]]
  (assert-state-has-navigation state)
  (let [{zoom :zoom pos :position} (:navigation-2d state)]
    [(/ (- x (- (/ (q/width) 2.) (* zoom (first pos)))) zoom)
     (/ (- y (- (/ (q/height) 2.) (* zoom (second pos)))) zoom)]))

(defn with-navigation-2d*
  "Calls body-fn after translating and scaling as determined by
  navigation-2d. Restores current transformation on exit."
  [state body-fn]
  (quil.core/push-matrix)
  (try
    (assert-state-has-navigation state)
    (quil.core/translate (world->screen-coords state [0 0]))
    (quil.core/scale (-> state :navigation-2d :zoom))
    (body-fn)
    (finally
      (quil.core/pop-matrix))))

(defmacro with-navigation-2d [state & body]
  `(quil.middlewares.navigation-2d/with-navigation-2d* ~state (fn [] ~@body)))

(defn- draw
  "Calls user draw function with all necessary transformations (position
  and zoom) applied."
  [user-draw state]
  (if (-> state :navigation-2d :wrap-draw)
    (with-navigation-2d state
      (user-draw state))
    (user-draw state)))

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
        setup (:setup options (fn [] {}))]
    (assoc options

           :setup (partial setup-2d-nav setup user-settings)

           :draw (partial draw user-draw)

           :mouse-dragged (fn [state event]
                            (user-mouse-dragged (mouse-dragged state event) event))
           :mouse-wheel (fn [state event]
                          (user-mouse-wheel (mouse-wheel state event) event)))))
