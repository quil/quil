(ns ^{:doc "Functions and macros for initialising and controlling visualisation applets."}
  quil.applet
  (:import [processing.core PApplet]
           [javax.swing JFrame]
           [java.awt Dimension]
           [java.awt.event WindowListener])
  (:require [quil.util :refer [resolve-constant-key no-fn absolute-path]]
            [clojure.stacktrace :refer [print-cause-trace]]
            [clojure.string :as string]))

(defonce untitled-applet-id* (atom 0))
(def ^:dynamic *applet* nil)

(defn ^PApplet current-applet []
  *applet*)

(defn target-frame-rate []
  (:target-frame-rate (meta (current-applet))))

(defn applet-disposed
  "This function is called when PApplet executes 'dispose' method.
  It means we can dispose frame, call on-close function and perform other
  clean ups."
  [applet]
  (.dispose (. applet frame))
  ((:on-close (meta applet))))

(defn applet-state
  "Fetch an element of state from within the applet"
  [applet k]
  (get @(:state (meta applet)) k))

(defn applet-close
  "Sets 'finished' field in applet to true. Main run loop in applet
   should stop as soon as finished == true and then call dispose."
  [applet]
  (set! (.finished applet) true))

(defn- prepare-applet-frame
  [applet title]
  (let [m              (meta applet)
        keep-on-top?   (:keep-on-top m)
        frame          (.frame applet)
        resizable?     (:resizable m)]
    (doseq [listener (.getWindowListeners frame)]
      (.removeWindowListener frame listener))
    (doto frame
      (.addWindowListener  (reify WindowListener
                             (windowActivated [this e])
                             (windowClosing [this e]
                               (applet-close applet))
                             (windowDeactivated [this e])
                             (windowDeiconified [this e])
                             (windowIconified [this e])
                             (windowOpened [this e])
                             (windowClosed [this e])))
      (.setDefaultCloseOperation JFrame/DO_NOTHING_ON_CLOSE))
    (javax.swing.SwingUtilities/invokeLater
     (fn []
       (when resizable?
         (.setResizable frame resizable?))
       (.setAlwaysOnTop frame keep-on-top?)))
    applet))


(defn- applet-run
  "Launches the applet to the specified target."
  [applet title renderer]
  (PApplet/runSketch
   (into-array String
               (vec (filter string?
                   [(when (and (:bgcolor (meta applet))
                               (:present (meta applet)))
                     (str "--bgcolor" "=" (str (:bgcolor (meta applet)))))
                    "--hide-stop" title])))
   applet)
  (prepare-applet-frame applet title))


(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :dxf    PApplet/DXF})

(defn resolve-renderer
  "Converts keyword to Processing renderer string constant.
  This string can be passed to native Processing methods.
  If renderer passed as String - do nothing and simply return it"
  [renderer]
  (cond (keyword? renderer) (resolve-constant-key renderer renderer-modes)
        (string? renderer) renderer
        :default (throw (RuntimeException. ":renderer should be keyword or string"))))

(defn- display-size
  "Returns size of screen. If there are 2 or more screens it probably return size of
  default one whatever it means."
  []
  (let [bounds (.. (java.awt.GraphicsEnvironment/getLocalGraphicsEnvironment)
                   getDefaultScreenDevice
                   getDefaultConfiguration
                   getBounds)]
    [(.-width bounds) (.-height bounds)]))

(defn- process-size
  "Checks that the size vector is exactly two elements. If not, throws
  an exception, otherwise returns the size vector unmodified."
  [size]
  (cond (= size :fullscreen) (display-size)
        (and (coll? size) (= 2 (count size))) size
        :else (throw (IllegalArgumentException.
                      (str "Invalid size definition:" size ". Was expecting :fullscreen or 2 elements vector: [x-size y-size].")))))

(defn- to-method-name [keyword]
  "Converts keyword to java-style method symbol. :on-key-pressed => onKeyPressed"
  (-> keyword
      name
      (string/replace
       #"-."
       #(-> % string/upper-case (subs 1)))
      symbol))

(defn- parent-method [method]
  "Appends string 'Parent' to given symbol"
  (symbol (str method "Parent")))

(defmacro with-applet [applet & body]
  "Binds dynamic var to current applet."
  `(binding [*applet* ~applet]
     ~@body))

(def listeners [:key-pressed
                :key-released
                :key-typed
                :mouse-pressed
                :mouse-released
                :mouse-moved
                :mouse-dragged
                :mouse-entered
                :mouse-exited
                :mouse-clicked
                :focus-gained
                :focus-lost])

(gen-class
  :name "quil.Applet"
  :implements [clojure.lang.IMeta]
  :extends processing.core.PApplet
  :state state
  :init quil-applet-init
  :post-init quil-applet-post-init
  :constructors {[java.util.Map] []}
  :exposes-methods {keyTyped keyTypedParent
                    loop loopParent
                    mouseDragged mouseDraggedParent
                    keyPressed keyPressedParent
                    mouseExited mouseExitedParent
                    mouseClicked mouseClickedParent
                    mouseEntered mouseEnteredParent
                    mouseMoved mouseMovedParent
                    keyReleased keyReleasedParent
                    mousePressed mousePressedParent
                    focusGained focusGainedParent
                    frameRate frameRateParent
                    mouseReleased mouseReleasedParent
                    focusLost focusLostParent
                    noLoop noLoopParent
                    sketchFullScreen sketchFullScreenParent
                    exit exitParent})

(defn -exit [this]
  (applet-close this))

(defn -sketchFullScreen [this] (:present (meta this)))

(defn -quil-applet-init [state]
  [[] state])

(defn -quil-applet-post-init [this _]
  (let [[width height] (:size (meta this))]
    (.resize this width height)))

(defn -meta [this]
  (.state this))

(defn -setup [this]
  ; If renderer is :pdf - we need to set it via size method,
  ; as there is no other way to set file path for renderer.
  ; Size method call must be FIRST in setup function
  ; (don't know why, but let's trust Processing guys).
  ; Technically it's not first (there are 'when' and 'let' before 'size'),
  ; but hopefully it will work fine.
  (when (= (:renderer (meta this)) :pdf)
    (let [[width height] (:size (meta this))
          renderer (resolve-renderer (:renderer (meta this)))
          file (-> this meta :output-file absolute-path)]
      (.size this (int width) (int height) renderer file)))
  (with-applet this
    ((:setup-fn (meta this)))))

(defn -draw [this]
  (with-applet this
    ((:draw-fn (meta this)))))

(defn -noLoop [this]
  (reset! (:looping? (meta this)) false)
  (.noLoopParent this))

(defn -loop [this]
  (reset! (:looping? (meta this)) true)
  (.loopParent this))

(defn -frameRate [this new-rate-target]
  (reset! (target-frame-rate) new-rate-target)
  (.frameRateParent this new-rate-target))

(defn -sketchRenderer [this]
  (let [renderer (:renderer (meta this))
        ; If renderer :pdf we can't use it as initial renderer
        ; as path to output file is not set and path can be set only
        ; via .size(width, height, renderer, path) method in setup function.
        ; Set :java2d renderer instead and call size method in setup later.
        initial-renderer (if (= renderer :pdf) :java2d renderer)]
      (resolve-renderer initial-renderer)))

(defmacro generate-listeners
  "Generates all listeners like onKeyPress, onMouseClick and others."
  []
  (letfn [(prefix [v method]
            (symbol (str v method)))
          (generate-listener [listener]
            (let [method (to-method-name listener)
                  parent-method-name (prefix "." (parent-method method))]
               `(defn ~(prefix "-" method)
                  ([this#] (with-applet this# ((~listener (meta this#)))))
                  ([this# evt#] (~parent-method-name this# evt#)))))]
    `(do ~@(map generate-listener listeners))))

(generate-listeners)

(defn -mouseWheel [this evt]
  (with-applet this
    (when-let [mouse-wheel (:mouse-wheel (.state this))]
      (mouse-wheel (.getCount evt)))))

(defn attach-applet-listeners [applet]
  (let [listeners {:on-dispose #(applet-disposed applet)}
        listener-obj (quil.helpers.AppletListener. listeners)]
    (.registerMethod applet "dispose" listener-obj)
    applet))

(def ^{:private true}
  supported-features
  #{:resizable :exit-on-close :keep-on-top :present :no-safe-draw})

(defn- check-for-removed-opts
  "Checks if opts map contains options which were removed in Quil 2.0.
  Prints messages how it could be potentially fixed."
  [opts]
  (let [messages {:decor "Try :features [:present] for similar effect"
                  :target "Use :features [:keep-on-top] instead."
                  :safe-draw-fn "Use :features [:no-safe-draw] instead."}]
    (doseq [key (keys opts)]
      (when-let [text (messages key)]
        (println key "option was removed in Quil 2.0. " text)))))

(defn applet
  "Create and start a new visualisation applet.

   :size           - A vector of width and height for the sketch or :fullscreen.
                     Defaults to [500 300]. If you're using :fullscreen you may
                     want to enable present mode - :features [:present]

   :renderer       - Specify the renderer type. One of :p2d, :p3d, :java2d,
                     :opengl, :pdf). Defaults to :java2d. :dxf renderer
                     can't be used as sketch renderer. Use begin-raw method
                     instead.

   :output-file    - Specify an output file path. Only used in :pdf mode.

   :title          - A string which will be displayed at the top of
                     the sketch window.

   :features       - A vector of keywords customizing sketch behaviour.
                     Supported features:

                     :keep-on-top - Sketch window will always be above other windows.
                                    Note: some platforms might not support
                                    always-on-top windows.

                     :exit-on-close - Shutdown JVM  when sketch is closed.

                     :resizable - Makes sketch resizable.

                     :no-safe-draw - Do not catch and print exception in the draw fn.
                                     By default all exceptions thrown inside draw function are catched
                                     so sketch doesn't break if something goes wrong.

                     :present - Switch to present mode (fullscreen without borders, OS panels). You may
                                want to use this feature together with :size :fullscreen.

                     Usage example: :features [:keep-on-top :present]

   :bgcolor        - Sets background color for unused space in present mode.
                     Color is specified in hex format: #XXXXXX.
                     Example: :bgcolor \"#00FFFF\" (cyan background)

   :setup          - a fn to be called once when setting the sketch up.

   :draw           - a fn to be repeatedly called at most n times per
                     second where n is the target frame-rate set for
                     the visualisation.

   :focus-gained   - Called when the sketch gains focus.

   :focus-lost     - Called when the sketch loses focus.

   :mouse-entered  - Called when the mouse enters the sketch window.

   :mouse-exited   - Called when the mouse leaves the sketch window

   :mouse-pressed  - Called every time a mouse button is pressed.

   :mouse-released - Called every time a mouse button is released.

   :mouse-clicked  - called once after a mouse button has been pressed
                     and then released.

   :mouse-moved    - Called every time the mouse moves and a button is
                     not pressed.

   :mouse-dragged  - Called every time the mouse moves and a button is
                     pressed.

   :mouse-wheel    - Called every time mouse wheel is rotated.
                     Takes 1 argument - wheel rotation, an int.
                     Negative values if the mouse wheel was rotated
                     up/away from the user, and positive values
                     if the mouse wheel was rotated down/ towards the user

   :key-pressed    - Called every time any key is pressed.

   :key-released   - Called every time any key is released.

   :key-typed      - Called once every time non-modifier keys are
                     pressed.

   :on-close       - Called once, when sketch is closed"
  [& opts]
  (check-for-removed-opts (apply hash-map opts))
  (let [options      (merge {:size [500 300]
                             :target :frame}
                            (apply hash-map opts))

        features     (let [user-features (set (:features options))]
                       (reduce #(assoc %1 %2 (contains? user-features %2)) {}
                               supported-features))

        options           (merge (dissoc options :features)
                                 features)

        size              (process-size (:size options))
        title             (or (:title options) (str "Quil " (swap! untitled-applet-id* inc)))
        renderer          (or (:renderer options) :java2d)
        draw-fn           (or (:draw options) no-fn)
        setup-fn          (or (:setup options) no-fn)
        safe-draw-fn      (fn []
                            (try
                              (draw-fn)
                              (catch Exception e
                                (println "Exception in Quil draw-fn for sketch" title ": " e "\nstacktrace: " (with-out-str (print-cause-trace e)))
                                (Thread/sleep 1000))))
        draw-fn           (if (:no-safe-draw options) draw-fn safe-draw-fn)

        on-close-fn       (let [close-fn (or (:on-close options) no-fn)]
                            (if (:exit-on-close options)
                              (fn []
                                (close-fn)
                                (System/exit 0))
                              close-fn))

        state             (atom nil)
        looping?          (atom true)
        listeners         (into {} (for [name listeners]
                                     [name (or (options name) no-fn)]))

        applet-state      (merge options
                                 {:state state
                                  :looping? looping?
                                  :on-close on-close-fn
                                  :setup-fn setup-fn
                                  :draw-fn draw-fn
                                  :renderer renderer
                                  :size size
                                  :target-frame-rate (atom 60)}
                                 listeners)
        prx-obj           (quil.Applet. applet-state)]
    (doto prx-obj
      (applet-run title renderer)
      (attach-applet-listeners))))

(def ^{:private true}
  fn-applet-params
  #{:setup :draw :focus-gained :focus-lost :mouse-entered
    :mouse-exited :mouse-pressed :mouse-released :mouse-clicked
    :mouse-moved :mouse-dragged :mouse-wheel :key-pressed
    :key-typed :on-close})

(defmacro defapplet
  "Define and start an applet and bind it to a var with the symbol
  app-name. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original fns are reflected in
  the visualisation. See applet for the available options."
  [app-name & opts]
  (let [fn-param? #(contains? fn-applet-params %)
        opts  (mapcat (fn [[k v]]
                        [k (if (and (symbol? v)
                                    (fn-param? k))
                             `(var ~v)
                             v)])
                      (partition 2 opts))]
    `(def ~app-name (applet ~@opts))))
