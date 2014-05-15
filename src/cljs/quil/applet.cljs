(ns cljs.quil.applet
  (:require [clojure.browser.dom  :as dom])
  (:use-macros [cljs.quil.helpers.tools :only [with-applet]]))

(defn no-fn [])

(def ^:dynamic
  *surface* nil)


(defn current-graphics [] *surface*)


(defn sketch [opts]
  (let [draw-fn   (or (:draw opts) no-fn)
        setup-fn  (or (:setup opts) no-fn)]
    (fn [prc]
      (set! (.-draw prc)
            (fn []
              (with-applet prc
                (draw-fn))))

      (set! (.-setup prc)
            (fn []
              (with-applet prc
                (setup-fn)))))))


(defn ^:export make-processing
  [& opts]
  (let [opts-map (apply hash-map opts)]
    (let [host-elem (dom/get-element (:host opts-map))
          processing-fn (sketch opts-map)]
      (when host-elem
        (js/Processing. host-elem processing-fn)))))
