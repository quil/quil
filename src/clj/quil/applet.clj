(ns ^{:doc "Functions and macros for initialising and controlling visualisation applets."}
 quil.applet
  (:import [processing.core PApplet]
           [javax.swing JFrame]
           [java.awt Dimension GraphicsEnvironment]
           [java.awt.event WindowListener])
  (:require [quil.util :as u]
            [quil.middlewares
             [deprecated-options :refer [deprecated-options]]
             [safe-fns :refer [safe-fns]]
             [bind-output :refer [bind-output]]]
            [clojure.string :as string]))

(defonce untitled-applet-id* (atom 0))
(def ^:dynamic *applet* nil)

(defn ^PApplet current-applet []
  *applet*)

(defmacro with-applet
  "Binds dynamic var to current applet."
  [applet & body]
  `(binding [*applet* ~applet]
     ~@body))

(def ^:private  executor (java.util.concurrent.Executors/newSingleThreadScheduledExecutor))

(defn- destroy-window
  "Clean up native window object once sketch is closed. Processing doesn't
  perform cleanups because it probably assumes that when the sketch is closed
  the JVM is closed as well which is not the case for clojure. So here we're
  performing renderer-specific cleanups."
  [applet]
  (let [native (-> applet .getSurface .getNative)]
    (condp = (.getClass native)

      com.jogamp.newt.opengl.GLWindow
      ; Cannot destroy GLWindow right away because there is some callbacks going on
      ; and NPE is thrown when they execute if window is destroyed. It doesn't seem
      ; to affect anything, but annoying to have NPE in logs. Delaying destroying
      ; window for 1 sec. Ugly hack, but couldn't think of better way. Suggestions welcome.
      (.schedule executor #(.destroy native) 1 java.util.concurrent.TimeUnit/SECONDS)

      processing.awt.PSurfaceAWT$SmoothCanvas
      (-> native .getFrame .dispose)

      :nothing)))

(defn applet-disposed
  "This function is called when `PApplet` executes `dispose` method. It means we
  can dispose the frame, call the `on-close` function and perform other
  cleanups."
  [applet]
  (with-applet applet
    ((:on-close (meta applet))))
  (-> applet .getSurface (.setVisible false))
  (destroy-window applet))

(defn applet-state
  "Fetch an element of state from within the `applet`."
  [applet k]
  (get @(:state (meta applet)) k))

(defn- prepare-applet-surface
  [applet title]
  (let [m              (meta applet)
        keep-on-top?   (:keep-on-top m)
        surface        (.getSurface applet)
        frame          (.frame applet)
        resizable?     (:resizable m)]
    ; TODO: check if resizable and alwaysOnTop work correctly.
    (javax.swing.SwingUtilities/invokeLater
     (fn []
       (.setTitle surface title)
       (.setResizable surface resizable?)
       (.setAlwaysOnTop surface keep-on-top?)))
    applet))

(defn- applet-run
  "Launches the applet to the specified target."
  [applet title renderer]
  (PApplet/runSketch
   (into-array String
               (vec (filter string?
                            [(when (:display (meta applet))
                               (str "--display=" (:display (meta applet))))
                             (when (and (:bgcolor (meta applet))
                                        (:present (meta applet)))
                               (str "--window-color" "=" (str (:bgcolor (meta applet)))))
                             (when (:present (meta applet))
                               (str "--present"))
                             "--hide-stop" title])))
   applet)
  (prepare-applet-surface applet title))

(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :svg    PApplet/SVG
                  :dxf    PApplet/DXF
                  :fx2d   PApplet/FX2D})

(defn resolve-renderer
  "Converts `renderer` keyword to `Processing` renderer `String` constant.
  This `String` can be passed to native `Processing` methods.
  If `renderer` is passed as `String` do nothing and simply return it."
  [renderer]
  (cond (keyword? renderer) (u/resolve-constant-key renderer renderer-modes)
        (string? renderer) renderer
        :default (throw (RuntimeException. ":renderer should be keyword or string"))))

(defn- validate-size
  "Checks that the `size` vector is exactly two elements. If not, throws
  an exception, otherwise returns the `size` vector unmodified."
  [size]
  (if (or (= size :fullscreen)
          (and (coll? size) (= 2 (count size))))
    size
    (throw (IllegalArgumentException.
            (str "Invalid size definition: " size
                 ". Was expecting :fullscreen or 2 elements vector: "
                 "[width height].")))))

(defn- to-method-name
  "Converts keyword to java-style method symbol.
  `:on-key-pressed` => `onKeyPressed`"
  [keyword]
  (-> keyword
      name
      (string/replace
       #"-."
       #(-> % string/upper-case (subs 1)))
      symbol))

(defn- parent-method
  "Appends string 'Parent' to given symbol."
  [method]
  (symbol (str method "Parent")))

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
 :constructors {[java.util.Map] []}
 :exposes-methods {keyTyped keyTypedParent
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
                   sketchFullScreen sketchFullScreenParent})

(defn -exitActual
  "Overriding `PApplet.exitActual` because we don't want it to call
  `System.exit()`."
  [this])

(defn -sketchFullScreen [this] (:present (meta this)))

(defn -quil-applet-init [state]
  [[] state])

(defn -meta [this]
  (.state this))

(defn -settings
  "Overriding `PApplet.settings()` to set size."
  [this]
  (let [{:keys [renderer size output-file settings-fn]} (meta this)
        renderer (resolve-renderer renderer)
        output-file (u/absolute-path output-file)]
    (if (= size :fullscreen)
      (.fullScreen this renderer)
      (.size this (int (first size)) (int (second size))
             renderer output-file))
    ; calling user-provided :settings handler, if any
    (with-applet this
      (settings-fn))))

(defn -setup [this]
  ; If renderer is :pdf - we need to set it via size method,
  ; as there is no other way to set file path for renderer.
  ; Size method call must be FIRST in setup function
  ; (don't know why, but let's trust Processing guys).
  ; Technically it's not first (there are 'when' and 'let' before 'size'),
  ; but hopefully it will work fine.
  (with-applet this
    ((:setup-fn (meta this)))))

(defn -draw [this]
  (with-applet this
    ((:draw-fn (meta this)))))

(defn -frameRate [this new-rate-target]
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
  "Generates all listeners like `onKeyPress`, `onMouseClick` and others."
  []
  (letfn [(prefix [v method]
            (symbol (str v method)))
          (generate-listener [listener]
            (let [method (to-method-name listener)
                  parent-method-name (prefix "." (parent-method method))]
              `(defn ~(prefix "-" method)
                 ([~'this] (with-applet ~'this ((~listener (meta ~'this)))))
                 ([~'this ~'evt]
                   ; For all :key-xyz listeners we have to store event object
                   ; in applet state because later it might be used to
                   ; build set of key modifiers currently pressed.
                  ~(if (or (= listener :key-typed)
                           (= listener :key-pressed))
                     `(reset! (:key-event (meta ~'this)) ~'evt)
                     nil)
                  (~parent-method-name ~'this ~'evt)))))]
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
  #{:resizable :exit-on-close :keep-on-top :present :no-safe-fns
    :no-bind-output})

(defn applet
  "Create and start a new visualisation applet. All options used
  here should be documented in the [[quil.sketch/defsketch]] docstring."
  [& opts]
  (let [options (apply hash-map opts)

        options     (->> (:middleware options [])
                         (cons deprecated-options)
                         (apply comp)
                         (#(% options))
                         (merge {:size [500 300]}))

        features     (let [user-features (set (:features options))]
                       (reduce #(assoc %1 %2 (contains? user-features %2)) {}
                               supported-features))

        options     (if (:no-safe-fns features)
                      options
                      (safe-fns options))

        options     (if (:no-bind-output features)
                      options
                      (bind-output options))

        options           (merge (dissoc options :features)
                                 features)

        display           (or (:display options) :default)
        size              (validate-size (:size options))

        title             (or (:title options) (str "Quil " (swap! untitled-applet-id* inc)))
        renderer          (or (:renderer options) :java2d)
        draw-fn           (or (:draw options) u/no-fn)
        setup-fn          (or (:setup options) u/no-fn)
        settings-fn       (or (:settings options) u/no-fn)
        on-close-fn       (let [close-fn (or (:on-close options) u/no-fn)]
                            (if (:exit-on-close options)
                              (fn []
                                (close-fn)
                                (System/exit 0))
                              close-fn))

        state             (atom nil)
        listeners         (into {} (for [name listeners]
                                     [name (or (options name) u/no-fn)]))

        applet-state      (merge options
                                 {:state state
                                  :internal-state (atom u/initial-internal-state)
                                  :on-close on-close-fn
                                  :setup-fn setup-fn
                                  :settings-fn settings-fn
                                  :draw-fn draw-fn
                                  :renderer renderer
                                  :size size
                                  :display (:display options)
                                  :key-event (atom nil)}
                                 listeners)
        prx-obj           (quil.Applet. applet-state)]
    (doto prx-obj
      (applet-run title renderer)
      (attach-applet-listeners))))

(defmacro defapplet
  "Define and start an applet and bind it to
  a [var](https://clojure.org/reference/vars) with the symbol `app-name`. If any
  of the options to the various callbacks are symbols, it wraps them in a call
  to `var` to ensure they aren't inlined and that redefinitions to the original
  functions are reflected in the visualisation. See [[applet]] for the available
  options."
  [app-name & opts]
  (letfn [(wrap [v]
            (if (symbol? v)
              ; It is possible that symbol points to non-fn var.
              ; For example it points to [300 300] which defines size
              ; In this case we should not wrap it with (var ...)
              `(if (fn? ~v) (var ~v) ~v)
              v))]
    `(def ~app-name (applet ~@(map wrap opts)))))
