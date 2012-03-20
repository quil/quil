(ns
    ^{:doc "Functions and macros for initialising and controlling visualisation applets."
      :author "Roland Sadowsky, Sam Aaron"}
  quil.applet
  (:import (processing.core PApplet)
           (javax.swing JFrame)
           (java.awt.event WindowListener))
  (:use [quil.util :only [resolve-constant-key]]
        [quil.dynamics :only [*applet* *state*]]))

(defonce untitled-applet-id* (atom 0))
(defonce applet-tl (ThreadLocal.))
(defonce state-tl (ThreadLocal.))

(defn applet-stop
  "Stop an applet"
  [applet]
  (.stop applet))

(defn applet-state
  "Fetch an element of state from within the applet"
  [applet k]
  (get (:state (meta applet)) k))

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
  (.stop applet))

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
  "Stop the applet and close the window."
  [applet]
  (case (:target (meta applet))
    :frame      (applet-close-frame applet)
    :perm-frame (applet-close-frame applet)
    :applet     (applet-close-applet applet)))

(defn- launch-applet-frame
  [applet renderer keep-on-top?]
  (let [truthify       #(not (or (nil? %) (false? %)))
        keep-on-top?   (truthify keep-on-top?)
        m              (.meta applet)
        [width height] (or (:size m) [800 600])
        close-op       JFrame/DISPOSE_ON_CLOSE
        title          (or (:title m) (str "Quil " (swap! untitled-applet-id* inc)))
        f              (JFrame. title)]
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
  [applet renderer target]
  (.init applet)
  (case target
    :frame (launch-applet-frame applet renderer false)
    :perm-frame (launch-applet-frame applet renderer true)
    :applet applet))

(def ^{:private true}
  renderer-modes {:p2d    PApplet/P2D
                  :p3d    PApplet/P3D
                  :java2d PApplet/JAVA2D
                  :opengl PApplet/OPENGL
                  :pdf    PApplet/PDF
                  :dxf    PApplet/DXF})

(defn- applet-set-size
  ([width height] (.size *applet* (int width) (int height)))
  ([width height renderer]
     (let [renderer (resolve-constant-key renderer renderer-modes)]
       (.size (.get applet-tl) (int width) (int height) renderer))))

(defn- validate-size!
  "Checks that the size vector is exactly two elements. If not, throws
  an exception, otherwise returns the size vector unmodified."
  [size]
  (when-not (= 2 (count size))
    (throw (IllegalArgumentException. (str "Invalid size vector:" size ". Was expecting only 2 elements: [x-size y-size]. To specify renderer, use :renderer key."))))
  size)

(def ^{:private true} VALID-TARGETS #{:frame :perm-frame :applet})

(defn- validate-target!
  "Checks that the target option is one of the following allowed
  targets: [:frame, :perm-frame :applet]."
  [target]
  (when-not (some VALID-TARGETS [target])
    (throw (IllegalArgumentException. (str "Invalid target:" target". Was expecting one of: " (vec VALID-TARGETS)))))
  target)

(defn applet
  "Create and start a new visualisation applet.

  :size           - a vector of width, height and optional renderer
                    (one of :p2d, :java2d, :opengl, :pdf or :dxf). i.e.
                    [500 300] or [400 600 :opengl].
                    Defaults to [500 300].

  :title          - a string which will be displayed at the top of
                    the applet window.

  :setup          - a fn to be called once when setting the applet up.

  :draw           - a fn to be repeatedly called at most n times per
                    second where n is the target frame-rate set for
                    the visualisation.

  :target         - one of :frame, :perm-frame. Default :frame.

  :focus-gained   - Called when the applet gains focus.

  :focus-lost     - Called when the applet loses focus.

  :mouse-entered  - Called when the mouse enters the applet window.

  :mouse-exited   - Called when the mouse leaves the applet window

  :mouse-pressed  - Called every time a mouse button is pressed.

  :mouse-released - Called every time a mouse button is released.

  :mouse-clicked  - called once after a mouse button has been pressed
                    and then released.

  :mouse-moved    - Called every time the mouse moves and a button is
                    not pressed.

  :mouse-dragged  - Called every time the mouse moves and a button is
                    pressed.

  :key-pressed    - Called every time any key is pressed.

  :key-released   - Called every time any key is released.

  :key-typed      - Called once every time non-modifier keys are
                    pressed."
  [& opts]
  (let [options           (merge {:size [500 300] :target :frame}
                                 (apply hash-map opts))
        size              (validate-size! (:size options))
        target            (validate-target! (:target options))
        renderer          (or (:renderer options) :p2d)
        draw-fn           (or (:draw options) (fn [] nil))
        setup-fn          (fn []
                            (let [size-vec (concat size [renderer])]
                              (apply applet-set-size size-vec))
                            (when-let [f (:setup options)]
                              (f)))
        key-pressed-fn    (or (:key-pressed options) (fn [] nil))
        key-released-fn   (or (:key-released options) (fn [] nil))
        key-typed-fn      (or (:key-typed options) (fn [] nil))
        mouse-pressed-fn  (or (:mouse-pressed options) (fn [] nil))
        mouse-released-fn (or (:mouse-released options) (fn [] nil))
        mouse-moved-fn    (or (:mouse-moved options) (fn [] nil))
        mouse-dragged-fn  (or (:mouse-dragged options) (fn [] nil))
        mouse-entered-fn  (or (:mouse-entered options) (fn [] nil))
        mouse-exited-fn   (or (:mouse-exited options) (fn [] nil))
        mouse-clicked-fn  (or (:mouse-clicked options) (fn [] nil))
        focus-gained-fn   (or (:focus-gained options) (fn [] nil))
        focus-lost-fn     (or (:focus-lost options) (fn [] nil))
        state             (atom nil)
        target-obj        (atom nil)
        prx-obj           (proxy [processing.core.PApplet
                                  clojure.lang.IMeta] []
                            (meta [] (assoc options
                                       :state state
                                       :target-obj target-obj
                                       :target target))
                            (keyPressed
                              ([]
                                 (key-pressed-fn))
                              ([e]
                                 (proxy-super keyPressed e)))

                            (keyReleased
                              ([]
                                 (key-released-fn))
                              ([e]
                                 (proxy-super keyReleased e)))

                            (keyTyped
                              ([]
                                 (key-typed-fn))
                              ([e]
                                 (proxy-super keyTyped e)))

                            (mousePressed
                              ([]
                                 (mouse-pressed-fn))
                              ([e]
                                 (proxy-super mousePressed e)))

                            (mouseReleased
                              ([]
                                 (mouse-released-fn))
                              ([e]
                                 (proxy-super mouseReleased e)))

                            (mouseMoved
                              ([]
                                 (mouse-moved-fn))
                              ([e]
                                 (proxy-super mouseMoved e)))

                            (mouseDragged
                              ([]
                                 (mouse-dragged-fn))
                              ([e]
                                 (proxy-super mouseDragged e)))

                            (mouseClicked
                              ([]
                                 (mouse-clicked-fn))
                              ([e]
                                 (proxy-super mouseClicked e)))

                            (focusGained
                              ([] nil) ;;The no arg version of the focus
                              ;;fns don't appear too be called
                              ([e]
                                 (proxy-super focusGained e)
                                 (focus-gained-fn)))

                            (focusLost
                              ([] nil)
                              ([e]
                                 (proxy-super focusLost e)
                                 (focus-lost-fn)))

                            (mouseEntered
                              ([] nil)
                              ([e]
                                 (proxy-super mouseEntered e)
                                 (mouse-entered-fn)))

                            (mouseExited
                              ([] nil)
                              ([e]
                                 (proxy-super mouseExited e)
                                 (mouse-exited-fn)))

                            (setup
                              ([]
                                 (.set applet-tl this)
                                 (.set state-tl state)
                                 (setup-fn)))

                            (draw
                              [] (draw-fn)))]
    (applet-run prx-obj renderer target)
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
