(ns quil.middlewares.navigation-3d
  (:require [quil.core :as q]))

(def ^:private ^String missing-navigation-key-error
  (str "state map is missing :navigation-3d key. "
       "Did you accidentally removed it from the state in "
       ":update or any other handler?"))

(defn- assert-state-has-navigation
  "Asserts that state map contains :navigation-2d object."
  [state]
  (when-not (:navigation-3d state)
    (throw #?(:clj (RuntimeException. missing-navigation-key-error)
              :cljs (js/Error. missing-navigation-key-error)))))

(defn- default-position
  "Default position configuration. Check default configuration in
  'camera' function."
  []
  {:position [(/ (q/width) 2.0)
              (/ (q/height) 2.0)
              (/ (q/height) 2.0 (q/tan (/ (* q/PI 60.0) 360.0)))]
   :straight [0 0 -1]
   :up [0 1 0]})

(defn- rotate-by-axis-and-angle
  "Rotates vector v by angle with axis.
  Formula is taken from wiki:
  http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle"
  [v axis angle]
  (let [[a-x a-y a-z] axis
        [x y z] v
        cs (q/cos angle)
        -cs (- 1 cs)
        sn (q/sin angle)
        ; Matrix is
        ; [a b c]
        ; [d e f]
        ; [g h i]
        a (+ cs (* a-x a-x -cs))
        b (- (* a-x a-y -cs)
             (* a-z sn))
        c (+ (* a-x a-z -cs)
             (* a-y sn))
        d (+ (* a-x a-y -cs)
             (* a-z sn))
        e (+ cs (* a-y a-y -cs))
        f (- (* a-y a-z -cs)
             (* a-x sn))
        g (- (* a-x a-z -cs)
             (* a-y sn))
        h (+ (* a-y a-z -cs)
             (* a-x sn))
        i (+ cs (* a-z a-z -cs))]
    [(+ (* a x) (* b y) (* c z))
     (+ (* d x) (* e y) (* f z))
     (+ (* g x) (* h y) (* i z))]))

(defn- rotate-lr
  "Rotates nav-3d configuration left-right. angle positive - rotate right,
  negative - left."
  [nav-3d angle]
  (update-in nav-3d [:straight] rotate-by-axis-and-angle (:up nav-3d) angle))

(defn- cross-product
  "Vector cross-product: http://en.wikipedia.org/wiki/Cross_product"
  [[u1 u2 u3] [v1 v2 v3]]
  [(- (* u2 v3) (* u3 v2))
   (- (* u3 v1) (* u1 v3))
   (- (* u1 v2) (* u2 v1))])

(defn- v-mult
  "Multiply vector v by scalar mult."
  [v mult]
  (mapv #(* % mult) v))

(defn- v-plus
  "Sum of 2 vectors."
  [v1 v2]
  (mapv + v1 v2))

(defn- v-opposite
  "Returns vector opposite to vector v."
  [v]
  (v-mult v -1))

(defn- v-normalize
  "Normalize vector, returning vector
  which has same direction but with norm equals to 1."
  [v]
  (let [norm (->> (map q/sq v)
                  (apply +)
                  (q/sqrt))]
    (v-mult v (/ norm))))

(defn- rotate-ud
  "Rotates nav-3d configuration up-down."
  [nav-3d angle]
  (let [axis (cross-product (:straight nav-3d) (:up nav-3d))
        rotate #(rotate-by-axis-and-angle % axis angle)]
    (-> nav-3d
        (update-in [:straight] rotate)
        (update-in [:up] rotate))))

(defn- rotate
  "Mouse handler function which rotates nav-3d configuration.
  It uses mouse from event object and pixels-in-360 to calculate
  angles to rotate."
  [state event pixels-in-360]
  (assert-state-has-navigation state)
  (if (= 0 (:p-x event) (:p-y event))
    state
    (let [dx (- (:p-x event) (:x event))
          dy (- (:y event) (:p-y event))
          angle-lr (q/map-range dx 0 pixels-in-360 0 q/TWO-PI)
          angle-ud (q/map-range dy 0 pixels-in-360 0 q/TWO-PI)]
      (update-in state [:navigation-3d]
                 #(-> %
                      (rotate-lr angle-lr)
                      (rotate-ud angle-ud))))))

(def ^:private space (keyword " "))

(defn- move
  "Keyboard handler function which moves nav-3d configuration.
  It uses keyboard key from event object to determing in which
  direction to move."
  [state event step-size]
  (assert-state-has-navigation state)
  (let [{:keys [up straight]} (:navigation-3d state)]
    (if-let [dir (condp = (:key event)
                   :w straight
                   :s (v-opposite straight)
                   space (v-opposite up)
                   :z up
                   :d (cross-product straight up)
                   :a (cross-product up straight)
                   nil)]
      (update-in state [:navigation-3d :position]
                 #(v-plus % (v-mult dir step-size)))
      state)))

(defn- setup-3d-nav
  "Custom 'setup' function which creates initial position
  configuration and puts it to the state map."
  [user-setup user-settings]
  (let [initial-state (-> user-settings
                          (select-keys [:straight :up :position])
                          (->> (merge (default-position)))
                          (update-in [:straight] v-normalize)
                          (update-in [:up] v-normalize))]
    (update-in (user-setup) [:navigation-3d]
               #(merge initial-state %))))

(defn navigation-3d
  "Enables navigation in 3D space. Similar to how it is done in
  shooters: WASD navigation, space is go up, z is go down,
  drag mouse to look around."
  [options]
  (let [; 3d-navigation related user settings
        user-settings (:navigation-3d options)
        pixels-in-360 (:pixels-in-360 user-settings 1000)
        step-size (:step-size user-settings 20)
        rotate-on (:rotate-on user-settings :mouse-dragged)

        ; user-provided handlers which will be overridden
        ; by 3d-navigation
        draw (:draw options (fn [state]))
        key-pressed (:key-pressed options (fn [state _] state))
        rotate-on-fn (rotate-on options (fn [state _] state))
        setup (:setup options (fn [] {}))]
    (assoc options

           :setup (partial setup-3d-nav setup user-settings)

           :draw (fn [state]
                   (assert-state-has-navigation state)
                   (let [{[c-x c-y c-z] :straight
                          [u-x u-y u-z] :up
                          [p-x p-y p-z] :position} (:navigation-3d state)]
                     (q/camera p-x p-y p-z (+ p-x c-x) (+ p-y c-y) (+ p-z c-z) u-x u-y u-z))
                   (draw state))

           :key-pressed (fn [state event]
                          (key-pressed (move state event step-size) event))

           rotate-on (fn [state event]
                       (rotate-on-fn (rotate state event pixels-in-360) event)))))
