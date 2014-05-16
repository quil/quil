(ns cljs.quil.applet
  (:require [clojure.browser.dom  :as dom])
  (:use-macros [cljs.quil.applet :only [with-applet]]
               [cljs.quil.helpers.tools :only [bind-handlers]]))


(defn no-fn [])


(def ^:dynamic
  *surface* nil)


(defn current-graphics [] *surface*)


(defn sketch [opts]
  (let [draw-fn         (or (:draw opts) no-fn)
        setup-fn        (or (:setup opts) no-fn)

        key-pressed     (or (:key-pressed opts) no-fn)
        key-released    (or (:key-released opts) no-fn)
        key-typed       (or (:key-typed opts) no-fn)

        mouse-clicked   (or (:mouse-clicked opts) no-fn)
        mouse-dragged   (or (:mouse-dragged opts) no-fn)
        mouse-moved     (or (:mouse-moved opts) no-fn)
        mouse-pressed   (or (:mouse-pressed opts) no-fn)
        mouse-released  (or (:mouse-released opts) no-fn)
        ]
    (fn [prc]
      (bind-handlers prc
                     .-setup  (do
                                (when (:size opts)
                                  (apply cljs.quil.core/size (:size opts)))
                                (setup-fn)
                                (when (= true (:no-loop opts))
                                  (cljs.quil.core/no-loop)))
                     .-draw draw-fn

                     .-keyPressed key-pressed
                     .-keyReleased key-released
                     .-keyTyped key-typed

                     .-mouseClicked mouse-clicked
                     .-mouseDragged mouse-dragged
                     .-mouseMoved mouse-moved
                     .-mousePressed mouse-pressed
                     .-mouseReleased mouse-released
                     ))))


(defn ^:export make-processing
  [& opts]
  (let [opts-map (apply hash-map opts)]
    (let [host-elem (dom/get-element (:host opts-map))
          processing-fn (sketch opts-map)]
      (when host-elem
        (js/Processing. host-elem processing-fn)))))
