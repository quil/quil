(ns
    ^{:doc "Functions and macros for initialising and controlling visualisation applets."
      :author "Roland Sadowski, Sam Aaron"}
  quil.applet
  (:import (processing.core PApplet)
           (javax.swing JFrame)
           (java.awt Dimension)
           (java.awt.event WindowListener))
  (:use [quil.util :only [resolve-constant-key]]
        [clojure.stacktrace :only [print-cause-trace]])
  (:require [clojure.string :as string]))

(defn applet-safe-exit
  "Sets 'finished' field in applet to true. Main run loop in applet
   should stop as soon as finished == true and then call dispose."
  [applet]
  (set! (.-finished applet) true))

(defonce untitled-applet-id* (atom 0))
(def ^:dynamic *applet* nil)
(def ^:dynamic *graphics* nil)

(defn- no-fn
  "Function that does nothing."
  [])

(defn ^PApplet current-applet []
  *applet*)

(defn current-graphics []
  *graphics*)

(defn target-frame-rate []
  (:target-frame-rate (meta (current-applet))))

(defn applet-disposed
  "This function is called when PApplet executes 'dispose' method.
  It means we can dispose frame, call on-close function and perform other
  clean ups."
  [applet]
  (when-let [frame @(:target-obj (meta applet))]
    (.dispose frame))
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
  (javax.swing.SwingUtilities/invokeLater
    (fn []
      (when-let [frame @(:target-obj (meta applet))]
        (.hide frame))
      (applet-safe-exit applet))))

(defn- launch-applet-frame
  [applet title renderer keep-on-top?]
  (let [truthify       #(not (or (nil? %) (false? %)))
        keep-on-top?   (truthify keep-on-top?)
        m              (.meta applet)
        close-op       JFrame/DO_NOTHING_ON_CLOSE
        f              (JFrame. title)
        falsify        #(not (or (nil? %) (true? %)))
        decor?         (falsify (:decor m))]
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
      (.setUndecorated decor?)
      (.setDefaultCloseOperation close-op)
      (.add applet)
      (.pack))
    (when (= renderer :opengl)
      (.setResizable f false))
    (.show f)
    (.setAlwaysOnTop f keep-on-top?)
    (reset! (:target-obj (meta applet)) f)
    applet))

(defn- applet-run
  "Launches the applet to the specified target."
  [applet title renderer target]
  (case target
    :frame (launch-applet-frame applet title renderer false)
    :perm-frame (launch-applet-frame applet title renderer true)
    :none applet)
  (.init applet))

(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :dxf    PApplet/DXF})

(defn- validate-size!
  "Checks that the size vector is exactly two elements. If not, throws
  an exception, otherwise returns the size vector unmodified."
  [size]
  (when-not (= 2 (count size))
    (throw (IllegalArgumentException. (str "Invalid size vector:" size ". Was expecting only 2 elements: [x-size y-size]. To specify renderer, use :renderer key."))))
  size)

(def ^{:private true} VALID-TARGETS #{:frame :perm-frame :none})

(defn- renderer-has-output-file?
  "Checks whether selected renderer has output file (PDF or DXF) or not."
  [renderer]
  (contains? #{:pdf :dxf} renderer))

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
                    noLoop noLoopParent})

(defn -quil-applet-init [state]
  [[] state])

(defn -quil-applet-post-init [this _]
  (let [[width height] (:size (.state this))]
    (.setPreferredSize this (Dimension. width height))))

(defn -meta [this]
  (.state this))

(defn -setup [this]
  ; If renderer has output file - we need to set it via size method.
  ; And size method call must be FIRST in setup function
  ; (don't know why, but let's trust Processing guys).
  ; Technically it's not first (there are 'when' and 'let' before 'size'),
  ; but hopefully it will work fine.
  (when (renderer-has-output-file? (:renderer (meta this)))
    (let [[width height] (:size (meta this))
          renderer (resolve-constant-key (:renderer (meta this)) renderer-modes)]
    (.size this (int width) (int height) renderer (:output-file (meta this)))))
  (with-applet this
    ((:setup-fn (.state this)))))

(defn -draw [this]
  (with-applet this
    ((:draw-fn (.state this)))))

(defn -noLoop [this]
  (reset! (:looping? (.state this)) false)
  (.noLoopParent this))

(defn -loop [this]
  (reset! (:looping? (.state this)) true)
  (.loopParent this))

(defn -frameRate [this new-rate-target]
  (reset! (target-frame-rate) new-rate-target)
  (.frameRateParent this new-rate-target))

(defn -sketchRenderer [this]
  (let [renderer (:renderer (meta this))
        ; If renderer :pdf or :dxf we can't use it as initial renderer
        ; as path to output file is not set and path can be set only set
        ; via .size(width, height, renderer, path) method in setup function.
        ; Set :java2d renderer instead and call size method in setup later.
        initial-renderer (if (renderer-has-output-file? renderer) :java2d renderer)]
      (resolve-constant-key initial-renderer renderer-modes)))

(defmacro generate-listeners
  "Generates all listeners like onKeyPress, onMouseClick and others."
  []
  (letfn [(prefix [v method]
            (symbol (str v method)))]
    `(do ~@(for [listener listeners]
             (let [method (to-method-name listener)]
               `(defn ~(prefix "-" method)
                  ([~'this] (with-applet ~'this ((~listener (~'.state ~'this)))))
                  ([~'this ~'evt] (~(prefix "." (parent-method method)) ~'this ~'evt))))))))

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

   :size           - a vector of width and height for the sketch.
                     Defaults to [500 300].

   :renderer       - Specify the renderer type. One of :p2d, :java2d,
                     :opengl, :pdf or :dxf). Defaults to :java2d. If
                     :pdf or :dxf is selected, :target is forced to
                     :none.

   :output-file    - Specify an output file path. Only used in :pdf
                     and :dxf render modes.

   :title          - a string which will be displayed at the top of
                     the sketch window.

   :target         - Specify the target. One of :frame, :perm-frame
                     or :none. Ignored if :pdf or :dxf renderer is
                     used.

   :decor          - Specify if the window should have OS frame
                     decorations. Only honoured with :frame or
                     :perm-frame targets.

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
        size              (validate-size! (:size options))
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
                                  :target-frame-rate (atom 60)}
                                 listeners)
        prx-obj           (quil.Applet. applet-state)
        ]
    (doto prx-obj
      (applet-run title renderer target)
      (attach-applet-listeners))))

(defmacro defapplet
  "Define and start an applet and bind it to a var with the symbol
  app-name. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original fns are reflected in
  the visualisation. See applet for the available options."
  [app-name & opts]
  (let [opts  (mapcat (fn [[k v]] [k (if (symbol? v) `(var ~v) v)]) (partition 2 opts))]
    `(def ~app-name (applet ~@opts))))
