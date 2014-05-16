(ns cljs.quil.helpers.tools
  (:require [clojure.string :as string]))


(defn bind-handler [prc processing-name handler]
  `(set!
    (~processing-name ~prc)
    (fn []
      (~'with-applet ~prc
        (~@(if (list? handler) handler (list handler)))))))


(defmacro bind-handlers [prc & opts]
  (map
   #(apply bind-handler (concat (list prc) %))
   (partition 2 opts)))
