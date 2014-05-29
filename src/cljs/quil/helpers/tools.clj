(ns quil.helpers.tools
  (:require [clojure.string :as string]))


(defn bind-handler [prc [processing-name handler]]
  `(set!
    (~processing-name ~prc)
    (fn []
      (~'with-sketch ~prc
        (~@(if (list? handler) handler (list handler)))))))


(defmacro bind-handlers [prc & opts]
  (map #(bind-handler prc %) (partition 2 opts)))


(defmacro with-shape [mode & body]
  (if (keyword? mode)
    `(do
       (quil.core/begin-shape ~mode)
       ~@body
       (quil.core/end-shape))

    `(do
       (quil.core/begin-shape)
       ~mode
       ~@body
       (quil.core/end-shape))))

(defmacro resolve-constant-key 
  ([mode mode-list-name]
    `(~'resolve-c-key ~mode))

  ([mode]
    `(~'resolve-c-key ~mode)))