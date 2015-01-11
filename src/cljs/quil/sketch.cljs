(ns quil.sketch
  (:require [quil.util :as u :include-macros true]
            [quil.middlewares.deprecated-options :refer [deprecated-options]]
            [goog.dom :as dom]
            [goog.events :as events]
            [goog.events.EventType :as EventType])
  (:use-macros [quil.sketch :only [with-sketch]]))

(def ^:dynamic
  *applet* nil)

(defn current-applet [] *applet*)

(u/generate-quil-constants :cljs
  rendering-modes (:java2d :p2d :p3d :opengl))

(defn resolve-renderer [mode]
  (u/resolve-constant-key mode rendering-modes))

(defn size
  ([width height]
    (.size (current-applet) (int width) (int height)))

  ([width height mode]
    (.size (current-applet) (int width) (int height) (u/resolve-constant-key mode rendering-modes))))

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
                  (with-sketch prc
                    (handler)))))))

(defn make-sketch [options]
  (let [opts            (->> (:middleware options [])
                          (cons deprecated-options)
                          (apply comp)
                          (#(% options))
                          (merge {:size [500 300]}))

        sketch-size     (or (:size opts) [200 200])
        renderer        (:renderer opts)
        features        (set (:features opts))

        opts (-> opts
                 (update-in [:setup]
                            #(fn []
                               (->> (if renderer [renderer] [])
                                    (concat sketch-size)
                                    (apply size))
                               (when % (%))))
                 (update-in [:mouse-wheel]
                            #(when %
                               (fn []
                                 ;; -1 need for compability to Clojure version
                                 (% (* -1 (.-mouseScroll *applet*)))))))
        attach-function (fn [prc]
                          (bind-handlers prc opts)
                          (set! (.-quil prc) (atom nil))
                          (set! (.-target-frame-rate prc) (atom 60)))
        sketch (js/Processing.Sketch. attach-function)]
    (when (contains? features :global-key-events)
      (aset (aget sketch "options") "globalKeyEvents" true))
    sketch))

(defn sketch [& opts]
  (let [opts-map (apply hash-map opts)
        host-elem (dom/getElement (:host opts-map))
        renderer (or (:renderer opts-map) :p2d)]
    (if host-elem
      (do
        (if (.-processing-context host-elem)
          (when-not (= renderer (.-processing-context host-elem))
            (.warn js/console "WARNING: Using different context on one canvas!"))
          (set! (.-processing-context host-elem) renderer))
        (js/Processing. host-elem (make-sketch opts-map)))
      (.warn js/console "WARNING: Cannot create sketch. :host is not specified."))))

(def sketch-init-list (atom (list )))

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
      ((:fn sk)))))

(defn add-sketch-to-init-list [sk]
  (swap! sketch-init-list conj sk))

(events/listenOnce js/window EventType/LOAD init-sketches)
