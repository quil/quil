(ns quil.sketch
  (:require [clojure.browser.dom  :as dom]
            [quil.util :refer [no-fn resolve-constant-key]]
            [quil.middlewares.deprecated-options :refer [deprecated-options]])
  (:use-macros [quil.sketch :only [with-sketch]]
               [quil.helpers.tools :only [bind-handlers]]
               [quil.util :only [generate-quil-constants]]))

(def ^:dynamic
  *applet* nil)

(defn current-applet [] *applet*)

(generate-quil-constants
  rendering-modes (:java2d :p2d :p3d :opengl))

(defn resolve-renderer [mode]
  (resolve-constant-key mode rendering-modes))

(defn size
  ([width height]
    (.size (current-applet) (int width) (int height)))

  ([width height mode]
    (.size (current-applet) (int width) (int height) (resolve-constant-key mode rendering-modes))))

(def ^{:private true}
  supported-features
  #{:no-start})

(defn make-sketch [options]
  (let [opts            (->> (:middleware options [])
                          (cons deprecated-options)
                          (apply comp)
                          (#(% options))
                          (merge {:size [500 300]}))

        draw-fn         (or (:draw opts) no-fn)
        setup-fn        (or (:setup opts) no-fn)

        sketch-size     (or (:size opts) [200 200])
        renderer        (:renderer opts)

        key-pressed     (or (:key-pressed opts) no-fn)
        key-released    (or (:key-released opts) no-fn)
        key-typed       (or (:key-typed opts) no-fn)

        mouse-clicked   (or (:mouse-clicked opts) no-fn)
        mouse-dragged   (or (:mouse-dragged opts) no-fn)
        mouse-moved     (or (:mouse-moved opts) no-fn)
        mouse-pressed   (or (:mouse-pressed opts) no-fn)
        mouse-released  (or (:mouse-released opts) no-fn)
        mouse-out       (or (:mouse-exited opts) no-fn)
        mouse-over      (or (:mouse-entered opts) no-fn)
        mouse-scrolled  (or (:mouse-wheel opts) (fn [x] ))]
    (fn [prc]
      (bind-handlers prc
                     :setup  (do
                                (apply size (concat sketch-size (if renderer [renderer] [])))
                                (setup-fn))
                     :draw draw-fn

                     :keyPressed key-pressed
                     :keyReleased key-released
                     :keyTyped key-typed

                     :mouseClicked mouse-clicked
                     :mouseDragged mouse-dragged
                     :mouseMoved mouse-moved
                     :mousePressed mouse-pressed
                     :mouseReleased mouse-released
                     :mouseOut mouse-out
                     :mouseOver mouse-over
                     :mouseScrolled (mouse-scrolled (* -1 (.-mouseScroll prc)))) ;; -1 need for compability to Clojure version
      (set! (.-quil prc) (atom nil))
      (set! (.-target-frame-rate prc) (atom 60)))))

(defn sketch
  [& opts]
  (let [opts-map (apply hash-map opts)
        host-elem (dom/get-element (:host opts-map))
        processing-fn (make-sketch opts-map)]
    (when host-elem
      (js/Processing. host-elem processing-fn))))


(def sketch-init-list (atom (list )))

(defn add-js-event [event fun]
  (if (.-addEventListener js/window)
      (.addEventListener js/window event fun false)
      (if (.-attachEvent js/window)
          (.attachEvent js/window (str "on" event) fun))))

(defn empty-body? []
  (let [child (.-childNodes (.-body js/document))]
    (= 1 (.-length child))))

(defn add-canvas [canvas-id]
  (let [canvas (.createElement js/document "canvas")]
    (.setAttribute canvas "id" canvas-id)
    (.appendChild (.-body js/document) canvas)))

(defn init-sketches []
  (let [add-elem? (empty-body?)]
    (doseq [sk @sketch-init-list]
      (when add-elem?
        (add-canvas (:host-id sk)))
      ((:fn sk)))))

(defn add-sketch-to-init-list [sk]
  (swap! sketch-init-list conj sk))

(add-js-event "load" init-sketches)
