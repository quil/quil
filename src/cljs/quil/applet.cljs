(ns cljs.quil.applet
  (:require [clojure.browser.dom  :as dom])
  (:use-macros [cljs.quil.helpers.tools :only [with-applet]]))

(defn no-fn [])

(def ^:dynamic
  *surface* nil)


(defn current-graphics [] *surface*)


(defn sketch [opts]
  (let [draw-fn       (or (:draw opts) no-fn)
        setup-fn      (or (:setup opts) no-fn)
        key-pressed   (or (:key-pressed opts) no-fn)
        key-released  (or (:key-released opts) no-fn)]
    (fn [prc]
      (set! (.-draw prc)
            (fn []
              (with-applet prc
                (draw-fn))))

      (set! (.-setup prc)
            (fn []
              (with-applet prc
                (when (:size opts)
                  (apply cljs.quil.core/size (:size opts)))
                (setup-fn))))

      (set! (.-keyPressed prc)
            (fn []
              (with-applet prc
                (key-pressed))))

      (set! (.-keyReleased prc)
            (fn []
              (with-applet prc
                (key-released))))
      )
    ))


(defn ^:export make-processing
  [& opts]
  (let [opts-map (apply hash-map opts)]
    (let [host-elem (dom/get-element (:host opts-map))
          processing-fn (sketch opts-map)]
      (when host-elem
        (js/Processing. host-elem processing-fn)))))
