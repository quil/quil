(ns
    ^{:doc "Functions and macros for initialising and controlling visualisation applets."
      :author "Roland Sadowski, Sam Aaron"}
  quil.applet
  (:import (processing.core PApplet)
           (javax.swing JFrame)
           (java.awt.event WindowListener))
  (:use [quil.util :only [resolve-constant-key]]
        [clojure.stacktrace :only [print-cause-trace]])
  (:require [clojure.string :as string]))

(defn applet-safe-exit
  "Similar to the exit method on PApplet, but doesn't kill the current
  process. Annoyingly, we don't have direct access to the looping
  field as it isn't public. However, we now proxy the loop and
  noLoop (the only places the field is modified) and keep a local copy
  of that state. Unfortunate but functional."
  [applet]
  (let [looping? @(:looping? (meta applet))]
    (if looping?
      (set! (.-finished applet) true)
      (.dispose applet))))

(defonce untitled-applet-id* (atom 0))
(def ^:dynamic *applet* nil)
(def ^:dynamic *graphics* nil)

(defn ^PApplet current-applet []
  *applet*)

(defn current-graphics []
  *graphics*)

(defn target-frame-rate []
  (:target-frame-rate (meta (current-applet))))

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

(defn- applet-close-applet
  [applet]
  (applet-safe-exit applet))

(defn- applet-close-frame
  [applet]
  (let [closing-fn (fn []
                     (let [frame @(:target-obj (meta applet))]
                       (applet-close-applet applet)
                       ;;.destroy appears to kill the process too
                       (doto frame
                         (.hide)
                         (.dispose))))]
    (javax.swing.SwingUtilities/invokeAndWait closing-fn)))

(defn applet-close
  "Stop the applet and close the window. Call on-close function afterwards."
  [applet]
  (case (:target (meta applet))
    :frame      (applet-close-frame applet)
    :perm-frame (applet-close-frame applet)
    :none       (applet-close-applet applet))
  ((get (meta applet) :on-close)))

(defn- launch-applet-frame
  [applet title renderer keep-on-top?]
  (let [truthify       #(not (or (nil? %) (false? %)))
        keep-on-top?   (truthify keep-on-top?)
        m              (.meta applet)
        [width height] (or (:size m) [800 600])
        close-op       JFrame/DISPOSE_ON_CLOSE
        f              (JFrame. title)
        falsify        #(not (or (nil? %) (true? %)))
        decor?         (falsify (:decor m))]
    (doto f
      (.addWindowListener  (reify WindowListener
                             (windowActivated [this e])
                             (windowClosing [this e]
                               (future (applet-close applet)))
                             (windowDeactivated [this e])
                             (windowDeiconified [this e])
                             (windowIconified [this e])
                             (windowOpened [this e])
                             (windowClosed [this e])))
      (.setUndecorated decor?)
      (.setDefaultCloseOperation close-op)
      (.setSize width height)
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
  (.init applet)
  (case target
    :frame (launch-applet-frame applet title renderer false)
    :perm-frame (launch-applet-frame applet title renderer true)
    :none applet))

(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :dxf    PApplet/DXF})

(defn- applet-set-size
  ([width height]
     (.size (current-applet) (int width) (int height)))
  ([width height renderer]
     (let [renderer (resolve-constant-key renderer renderer-modes)]
       (.size (current-applet) (int width) (int height) renderer)))
  ([width height renderer path]
     (let [renderer (resolve-constant-key renderer renderer-modes)]
       (.size (current-applet) (int width) (int height) renderer path))))

(defn- validate-size!
  "Checks that the size vector is exactly two elements. If not, throws
  an exception, otherwise returns the size vector unmodified."
  [size]
  (when-not (= 2 (count size))
    (throw (IllegalArgumentException. (str "Invalid size vector:" size ". Was expecting only 2 elements: [x-size y-size]. To specify renderer, use :renderer key."))))
  size)

(def ^{:private true} VALID-TARGETS #{:frame :perm-frame :none})

(defn- validate-output-file!
  "Checks to see whether the output file parameter is correct and that
  the renderer mode is compatible. Returns a vector - either empty or
  containing a file path if the renderer is compatible."
  [renderer path]
  (if (some #{renderer} #{:pdf :dxf})
    [path]
    []))

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
 :init quil-init
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

(defn -quil-init [state]
  [[] state])

(defn -meta [this]
  (.state this))

(defn -setup [this]
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

(defmacro generate-listeners []
  "Generates all listeners like onKeyPress, onMouseClick and others."
  (letfn [(prefix [v method]
            (symbol (str v method)))]
    `(do ~@(for [listener listeners]
             (let [method (to-method-name listener)]
               `(defn ~(prefix "-" method)
                  ([~'this] (with-applet ~'this ((~listener (~'.state ~'this)))))
                  ([~'this ~'evt] (~(prefix "." (parent-method method)) ~'this ~'evt))))))))

(generate-listeners)

(defn wrap-mouse-wheel
  "Wraps callback to an instance of java.awt.event.MouseEventListener
  or returns nil if given callback is nil."
  [mouse-wheel applet]
  (when mouse-wheel
    (reify java.awt.event.MouseWheelListener
      (mouseWheelMoved [this e]
        (with-applet applet
          (mouse-wheel (.getWheelRotation e)))))))

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
        draw-fn           (or (:draw options) (fn [] nil))
        output-file       (validate-output-file! renderer (:output-file options))
        target            (if (empty? output-file) target :none)
        setup-fn          (fn []
                            (let [size-vec (concat size [renderer] output-file)]
                              (apply applet-set-size size-vec))
                            (when-let [wheel-listener (wrap-mouse-wheel (:mouse-wheel options)
                                                                        (current-applet))]
                              (.addMouseWheelListener (current-applet) wheel-listener))
                            (when-let [f (:setup options)]
                              (f)))
        safe-draw-fn      (fn []
                            (try
                              (draw-fn)
                              (catch Exception e
                                (println "Exception in Quil draw-fn for sketch" title ": " e "\nstacktrace: " (with-out-str (print-cause-trace e)))
                                (Thread/sleep 1000))))
        draw-fn           (if (:safe-draw-fn options) safe-draw-fn draw-fn)
        on-close-fn       (or (:on-close options) (fn [] nil))
        state             (atom nil)
        target-obj        (atom nil)
        looping?          (atom true)
        listeners         (into {} (for [name listeners]
                                     [name (or (options name) (fn [] nil))]))
        applet-state      (merge options
                                 {:state state
                                  :target-obj target-obj
                                  :target target
                                  :looping? looping?
                                  :on-close on-close-fn
                                  :setup-fn setup-fn
                                  :draw-fn draw-fn
                                  :target-frame-rate (atom 60)}
                                 listeners)
        prx-obj           (quil.Applet. applet-state)]
    (applet-run prx-obj title renderer target)
    prx-obj))


(defmacro defapplet
  "Define and start an applet and bind it to a var with the symbol
  app-name. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original fns are reflected in
  the visualisation. See applet for the available options."
  [app-name & opts]
  (let [opts  (mapcat (fn [[k v]] [k (if (symbol? v) `(var ~v) v)]) (partition 2 opts))]
    `(def ~app-name (applet ~@opts))))
