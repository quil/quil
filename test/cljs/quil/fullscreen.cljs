(ns quil.fullscreen
  (:require [quil.core :as q :include-macros true]
            [quil.middlewares.fun-mode :as fm]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn start-sketch []
  (q/sketch
   :host "fullscreen"
   :size [500 500]
   :draw (fn []
           (q/background 240)
           (q/text-size 30)
           (q/fill 0)
           (q/text-align :center :center)
           (q/text (str "width: " (q/width)
                        "\nheight: " (q/height))
                   (/ (q/width) 2)
                   (/ (q/height) 2)))))

(events/listenOnce js/window EventType/LOAD
                   #(when (= (-> js/document
                                 (.-body)
                                 (.-dataset)
                                 (aget "page"))
                             "fullscreen")
                      (start-sketch)))
