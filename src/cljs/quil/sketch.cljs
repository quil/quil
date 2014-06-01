(ns quil.sketch
  (:require [clojure.browser.dom  :as dom])
  (:use-macros [quil.sketch :only [with-sketch]]
               [quil.helpers.tools :only [bind-handlers]]))


(defn no-fn [])


(def ^:dynamic
  *surface* nil)


(defn current-graphics [] *surface*)


(defn sketch [opts]
  (let [draw-fn         (or (:draw opts) no-fn)
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
        mouse-out       (or (:mouse-out opts) no-fn)
        mouse-over      (or (:mouse-over opts) no-fn)]
    (fn [prc]
      (bind-handlers prc
                     .-setup  (do
                                (apply quil.core/size (concat sketch-size (if renderer [renderer] [])))
                                (setup-fn)
                                (when (= true (:no-loop opts))
                                  (quil.core/no-loop)))
                     .-draw draw-fn

                     .-keyPressed key-pressed
                     .-keyReleased key-released
                     .-keyTyped key-typed

                     .-mouseClicked mouse-clicked
                     .-mouseDragged mouse-dragged
                     .-mouseMoved mouse-moved
                     .-mousePressed mouse-pressed
                     .-mouseReleased mouse-released
                     .-mouseOut mouse-out
                     .-mouseOver mouse-over
                     ))))


(defn ^:export make-processing
  [& opts]
  (let [opts-map (apply hash-map opts)]
    (let [host-elem (dom/get-element (:host opts-map))
          processing-fn (sketch opts-map)]
      (when host-elem
        (js/Processing. host-elem processing-fn)))))


(def sketch-init-list (atom (list )))


(defn ^:export add-js-event [event fun]
  (if (.-addEventListener js/window)
      (.addEventListener js/window event fun false)
      (if (.-attachEvent js/window)
          (.attachEvent js/window (str "on" event) fun))))


(defn init-sketches []
  (doseq [sk @sketch-init-list]
    (sk)))


(defn add-sketch-to-init-list [sk]
  (swap! sketch-init-list conj sk))

(add-js-event "load" init-sketches)