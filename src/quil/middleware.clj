(ns ^{:doc "Quil middleware."}
  quil.middleware
  (:require [quil.middlewares.fun-mode :as fun-mode]
            [quil.middlewares.pause-on-error :as pause-on-error]))

(defn fun-mode
  "Introduces function mode. Adds 'update' function which takes current
  state and returns new state. Makes all other functions (setup, draw,
  mouse-click, etc) state-aware. See wiki for more details."
  [options]
  (fun-mode/fun-mode options))

(defn pause-on-error
  [options]
  (pause-on-error/pause-on-error options))
