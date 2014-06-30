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
  "Pauses sketch if any of user-provided handlers throws error.
  It allows to fix the error on the fly and continue sketch.
  May be good alternative to default '500ms pause if exception'
  behaviour."
  [options]
  (pause-on-error/pause-on-error options))
