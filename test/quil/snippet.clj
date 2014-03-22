(ns quil.snippet
  (:require [quil.core :as q]
            [quil.util :refer [no-fn]]
            [clojure.test :refer [is]]))

(def default-size [500 500])

(defmacro snippet-as-test [name opts & draw-fn-body]
  `(let [result# (promise)]
     (q/sketch
      :title (str '~name)
      :size default-size
      :renderer ~(:renderer opts :java2d)
      :setup (fn [] ~(:setup opts))
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
  `(def ~(vary-meta name assoc
                    :test `(fn [] (snippet-as-test ~name ~opts ~@body)))
     (fn []
       (q/sketch
        :title (str '~name)
        :size ~default-size
        :setup (fn []
                 (q/frame-rate 5)
                 ~(:setup opts))
        :renderer ~(:renderer opts :java2d)
        :draw (fn [] ~@body)))))
