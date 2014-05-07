(ns ^{:doc "Functions and macros for initialising and controlling visualisation applets."}
  quil.applet
  (:import [processing.core PApplet]
           [javax.swing JFrame]
           [java.awt Dimension]
           [java.awt.event WindowListener])
  (:require [quil.util :refer [resolve-constant-key no-fn absolute-path]]
            [clojure.stacktrace :refer [print-cause-trace]]
            [clojure.string :as string]))

(defn applet-safe-exit
  "Sets 'finished' field in applet to true. Main run loop in applet
   should stop as soon as finished == true and then call dispose."
  [applet]
  (set! (.-finished applet) true))

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
  ((:on-close (meta applet))))

(defn applet-stop
  "Stop an applet"
  [applet]
  (.stop applet))

(defn applet-state
  "Fetch an element of state from within the applet"
  [applet k]
  (get @(:state (meta applet)) k))

(defn applet-start
  "Start an applet"
  [applet]
  (.start applet))

(defn applet-exit
  "Exit the applet (may kill JVM process)"
  [applet]
  (.exit applet))

(defn applet-close
  "Stop the applet and close the window. Call on-close function afterwards."
  [applet]
  (applet-safe-exit applet)
  (javax.swing.SwingUtilities/invokeLater
    (fn []
      (when-let [frame (. applet frame)]
        (.setVisible frame false)))))


(defn- prepare-applet-frame
  [applet title renderer keep-on-top?]
  (let [truthify       #(not (or (nil? %) (false? %)))
        keep-on-top?   (truthify keep-on-top?)
        m              (.meta applet)
        close-op       JFrame/DO_NOTHING_ON_CLOSE
        f              (. applet frame)
        falsify        #(not (or (nil? %) (true? %)))]

    (doseq [listener (.getWindowListeners f)]
      (.removeWindowListener f listener))

    (doto f
      (.addWindowListener  (reify WindowListener
                             (windowActivated [this e])
                             (windowClosing [this e]
                               (applet-close applet))
                             (windowDeactivated [this e])
                             (windowDeiconified [this e])
                             (windowIconified [this e])
                             (windowOpened [this e])
                             (windowClosed [this e])))
      (.setDefaultCloseOperation close-op))

    (javax.swing.SwingUtilities/invokeLater
     (fn []
       (.setResizable f true)
       (.setAlwaysOnTop f keep-on-top?)))

    (when (= renderer :opengl)
      (.setResizable f false))
    (reset! (:target-obj (meta applet)) f)
    applet))


(defn- applet-run
  "Launches the applet to the specified target."
  [applet title renderer target]
  (PApplet/runSketch (into-array String ["--hide-stop" title]) applet)
  (case target
    :frame (prepare-applet-frame applet title renderer false)
    :perm-frame (prepare-applet-frame applet title renderer true)
    :none (prepare-applet-frame applet title renderer false)))


(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :dxf    PApplet/DXF})

(defn resolve-renderer
  "Converts keyword to Processing renderer string constant.
  This string can be passed to native Processing methods."
  [renderer]
  (resolve-constant-key renderer renderer-modes))

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

(def ^{:private true} VALID-TARGETS #{:frame :perm-frame :none})

(defn- validate-target!
  "Checks that the target option is one of the following allowed
  targets: [:frame, :perm-frame :none]."
  [target]
  (when-not (some VALID-TARGETS [target])
    (throw (IllegalArgumentException. (str "Invalid target:" target". Was expecting one of: " (vec VALID-TARGETS)))))
  target)

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
                    sketchFullScreen sketchFullScreenParent})

(defn -sketchFullScreen [this] false)

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
  (let [[width height] (:size (meta this))
          renderer (resolve-renderer (:renderer (meta this)))
          file (-> this meta :output-file absolute-path)]
    (when (= (:renderer (meta this)) :pdf)
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

(defn applet
  "Create and start a new visualisation applet.

   :size           - a vector of width and height for the sketch or :fullscreen.
                     Defaults to [500 300].

   :renderer       - Specify the renderer type. One of :p2d, :p3d, :java2d,
                     :opengl, :pdf). Defaults to :java2d. :dxf renderer
                     can't be used as sketch renderer. Use begin-raw method
                     instead.

   :output-file    - Specify an output file path. Only used in :pdf mode.

   :title          - a string which will be displayed at the top of
                     the sketch window.

   :target         - Specify the target. One of :frame, :perm-frame.

   :decor          - Specify if the window should have OS frame
                     decorations.

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

   :safe-draw-fn   - Catches and prints exceptions in the draw fn.
                     Default is true.

   :on-close       - Called once, when sketch is closed"
  [& opts]
  (let [options           (merge {:size [500 300]
                                  :target :frame
                                  :safe-draw-fn true}
                                 (apply hash-map opts))

        size              (process-size (:size options))
        target            (validate-target! (:target options))
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
        draw-fn           (if (:safe-draw-fn options) safe-draw-fn draw-fn)

        on-close-fn       (or (:on-close options) no-fn)

        state             (atom nil)
        target-obj        (atom nil)
        looping?          (atom true)
        listeners         (into {} (for [name listeners]
                                     [name (or (options name) no-fn)]))
        applet-state      (merge options
                                 {:state state
                                  :target-obj target-obj
                                  :target target
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
      (applet-run title renderer target)
      (attach-applet-listeners))))

(def ^{:private true}
  non-fn-applet-params
  #{:size :renderer :output-file :title :target :decor})

(defmacro defapplet
  "Define and start an applet and bind it to a var with the symbol
  app-name. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original fns are reflected in
  the visualisation. See applet for the available options."
  [app-name & opts]
  (let [fn-param? #(not (contains? non-fn-applet-params %))
        opts  (mapcat (fn [[k v]]
                        [k (if (and (symbol? v)
                                    (fn-param? k))
                             `(var ~v)
                             v)])
                      (partition 2 opts))]
    `(def ~app-name (applet ~@opts))))
