(ns quil.snippet
  (:require [quil.core :as q]))

(def snippets (atom #{}))

(defmacro defsnippet [name opts & body]
  `(let [full-name# (symbol (str *ns* "/" '~name))
         snippet# (assoc ~opts
                    :name '~name
                    :draw-fn (fn [] ~@body))]
     (def ~name snippet#)
     (swap! snippets conj full-name#)))

(defn show-snippet [snippet]
  (q/sketch
    :title (str (:name snippet))
    :size [500 500]
    :draw (:draw-fn snippet)))
