(ns quil.snippet
  (:require [quil.core :as q]
            [clojure.test :refer [is]]))

(def default-size [500 500])

(def snippets (atom #{}))

(defmacro snippet-as-test [& draw-fn-body]
  `(let [result# (promise)]
     (q/sketch
      :size default-size
      :draw (fn []
              (try
                ~@draw-fn-body
                (catch Exception e#
                  (println "Error" e#)
                  (.printStackTrace e#)
                  (deliver result# e#))
                (finally (q/exit))))
      :on-close #(deliver result# nil))
     (is (nil? @result#))))

(defmacro defsnippet [name opts & body]
  `(let [full-name# (symbol (str *ns* "/" '~name))
         snippet# (assoc ~opts
                    :name '~name
                    :draw-fn (fn [] ~@body))]
     (def ~(vary-meta name assoc
                      :test `(fn [] (snippet-as-test ~@body)))
       snippet#)
     (swap! snippets conj full-name#)))

(defn show-snippet [snippet]
  (q/sketch
    :title (str (:name snippet))
    :size default-size
    :draw (:draw-fn snippet)))
