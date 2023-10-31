(ns quil.fullscreen
  (:require [quil.examples :as qe]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(defn start-sketch []
  (qe/resize "fullscreen"))

(events/listenOnce js/window EventType/LOAD
                   #(when (= (-> js/document
                                 (.-body)
                                 (.-dataset)
                                 (aget "page"))
                             "fullscreen")
                      (start-sketch)))
