(ns cljs.quil.applet
  (:require [clojure.browser.dom  :as dom])
  (:use-macros [cljs.quil.helpers.tools :only [with-applet]]))


(def ^:dynamic
  *surface* nil)


(defn current-surface [] *surface*)


(defn sketch [draw-fn]
  (fn [prc]
    (set! (.-draw prc)
          (fn []
            (with-applet prc
              (draw-fn))))))


(defn ^:export make-processing
  [& {:keys [draw host]
      :or [nil nil]}]
  (let [host-elem (dom/get-element host)
        processing-fn (sketch draw)]
    (when host-elem
      (js/Processing. host-elem processing-fn))))
