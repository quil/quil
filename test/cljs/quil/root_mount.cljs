(ns quil.root-mount
  "entrypoint for figwheel server, each of the requires here will bind to the
  appropriately page at mount using the body.data-page element."
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [quil.fullscreen]
            [quil.manual]
            [quil.snippet]))

(defn data-page []
  (-> js/document
      (.-body)
      (.-dataset)
      (aget "page")))

(defn init []
  (js/console.log "quil.root-mount loaded for: " (data-page)))

(events/listenOnce js/window EventType/LOAD init)

