(ns quil.sketch
  (:require [quil.util :as u :include-macros true]
            [quil.middlewares.deprecated-options :as do]
            [goog.dom :as dom]
            [goog.events :as events]
            [goog.style :as style]
            [goog.events.EventType :as EventType])
  (:require-macros [quil.sketch]))

(def ^:dynamic
  *applet* nil)

(defn current-applet [] *applet*)

(u/generate-quil-constants :cljs
                           rendering-modes (:java2d :p2d :p3d :opengl))

(defn resolve-renderer [mode]
  (if (= :p3d mode)
    (aget js/p5.prototype "WEBGL")
    (u/resolve-constant-key mode rendering-modes)))

(defn set-size [applet width height]
  (when-let [el (.-quil-canvas applet)]
    ; p5js creates a <canvas> element inside provided <div> element
    ; we need to resize only the canvas as outer div will adapt automatically
    (let [inner-canvas (.querySelector el "canvas")]
      (.setAttribute inner-canvas "width" width)
      (.setAttribute inner-canvas "height" height)
      (aset (.-style inner-canvas) "width" (str width "px"))
      (aset (.-style inner-canvas) "height" (str height "px"))
      (set! (.-width applet)
            (.parseInt js/window (style/getComputedStyle inner-canvas "width")))
      (set! (.-height applet)
            (.parseInt js/window (style/getComputedStyle inner-canvas "height"))))))

(defn size
  ([width height]
   (.createCanvas (current-applet) (int width) (int height)))

  ([width height mode]
   (.createCanvas (current-applet) (int width) (int height) (resolve-renderer mode))))

(defn- bind-handlers [prc opts]
  (doseq [[processing-name quil-name] {:setup :setup
                                       :draw :draw

                                       :keyPressed :key-pressed
                                       :keyReleased :key-released
                                       :keyTyped :key-typed

                                       :mouseClicked :mouse-clicked
                                       :mouseDragged :mouse-dragged
                                       :mouseMoved :mouse-moved
                                       :mousePressed :mouse-pressed
                                       :mouseReleased :mouse-released
                                       :mouseOut :mouse-exited
                                       :mouseOver :mouse-entered
                                       :mouseScrolled :mouse-wheel}]
    (when-let [handler (opts quil-name)]
      (aset prc (name processing-name)
            (fn []
              (quil.sketch/with-sketch prc
                (handler)))))))

(defn in-fullscreen? []
  (or (.-fullscreenElement js/document)
      (.-mozFullScreenElement js/document)))

(defn add-fullscreen-support
  "Adds fullscreen support for provided Processing object.
  Fullscreen is enabled when user presses F11. We turn
  sketch <canvas> element to fullscreen storing old size
  in an atom. When user cancels fullscreen (F11 or Esc)
  we resize sketch to the old size."
  [applet]
  (let [old-size (atom nil)
        adjust-canvas-size
        (fn []
          (if (in-fullscreen?)
            (do
              (reset! old-size
                      [(.-width applet) (.-height applet)])
              (set-size applet
                        (-> js/window .-screen .-width)
                        (-> js/window .-screen .-height)))
            (apply set-size applet @old-size)))]
    (events/listen js/window EventType/KEYDOWN
                   (fn [event]
                     (when (and (= (.-key event) "F11")
                                (not (in-fullscreen?)))
                       (.preventDefault event)
                       (let [canvas (.-quil-canvas applet)]
                         (cond (.-requestFullscreen canvas) (.requestFullscreen canvas)
                               (.-mozRequestFullScreen canvas) (.mozRequestFullScreen canvas)
                               :else (.warn js/console "Fullscreen mode is not supported in current browser."))))))
    (events/listen js/document "fullscreenchange" adjust-canvas-size)
    (events/listen js/document "mozfullscreenchange" adjust-canvas-size)
    (events/listen js/document "fullscreenerror"
                   #(.error js/console "Error while switching to/from fullscreen: " %))))

(defn make-sketch [options]
  (let [opts            (->> (:middleware options [])
                             (cons do/deprecated-options)
                             (apply comp)
                             (#(% options))
                             (merge {:size [500 300]}))

        sketch-size     (:size opts)
        renderer        (:renderer opts)
        features        (set (:features opts))

        setup (fn []
                (->> (if renderer [renderer] [])
                     (concat sketch-size)
                     (apply size))
                (when (:settings opts) ((:settings opts)))
                (when (:setup opts) ((:setup opts))))
        mouse-wheel (when (:mouse-wheel opts)
                      ;; -1 need for compability with Clojure version
                      #((:mouse-wheel opts) (* -1 (.-mouseScroll *applet*))))

        opts (assoc opts
                    :setup setup
                    :mouse-wheel mouse-wheel)

        sketch (fn [prc]
                 (bind-handlers prc opts)
                 (set! (.-quil prc) (atom nil))
                 (set! (.-quil-internal-state prc) (atom u/initial-internal-state)))]
    sketch))

(defn destroy-previous-sketch [host-elem]
  (when-let [proc-obj (.-processing-obj host-elem)]
    (.remove proc-obj)))

(defn sketch [& opts]
  (let [opts-map (apply hash-map opts)
        host-elem (:host opts-map)
        renderer (or (:renderer opts-map) :p2d)
        host-elem (if (string? host-elem)
                    (.getElementById js/document host-elem)
                    host-elem)]
    (if host-elem
      (do
        (if (.-processing-context host-elem)
          (when-not (= renderer (.-processing-context host-elem))
            (.warn js/console "WARNING: Using different context on one canvas!"))
          (set! (.-processing-context host-elem) renderer))
        (destroy-previous-sketch host-elem)
        (let [proc-obj (js/p5. (make-sketch opts-map) host-elem)]
          (set! (.-processing-obj host-elem) proc-obj)
          (set! (.-quil-canvas proc-obj) host-elem)
          (add-fullscreen-support proc-obj)
          proc-obj))
      (.error js/console
              (if (:host opts-map)
                (str "ERROR: Cannot find host element: " (:host opts-map))
                "ERROR: Cannot create sketch. :host is not specified or element not found.")))))

(def sketch-init-list (atom (list)))

(defn empty-body? []
  (let [child (.-childNodes (.-body js/document))]
    ; seems hacky, we should come up with better way of
    ; checking whether body is empty or not
    (<= (.-length child) 1)))

(defn add-canvas [canvas-id]
  (let [canvas (.createElement js/document "canvas")]
    (.setAttribute canvas "id" canvas-id)
    (.appendChild (.-body js/document) canvas)))

(defn init-sketches []
  (let [add-elem? (empty-body?)]
    (doseq [sk @sketch-init-list]
      (when add-elem?
        (add-canvas (:host-id sk)))
      ((:fn sk))))
  (reset! sketch-init-list []))

(defn add-sketch-to-init-list [sk]
  (swap! sketch-init-list conj sk)
  ; if page already loaded immediately init sketch we just added
  (when (= (.-readyState js/document) "complete")
    (init-sketches)))

(events/listenOnce js/window EventType/LOAD init-sketches)
