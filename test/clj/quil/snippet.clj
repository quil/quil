(ns quil.snippet
  (:require [quil.util :refer [clj-compilation?]]
            [quil.core :as q]
            [quil.util :refer [no-fn]]
            [clojure.test :refer [is]]))

(def default-size [500 500])
(def default-host "quil-test")

(defmacro snippet-as-test [snip-name opts & draw-fn-body]
  `(let [result# (promise)]
     (Thread/sleep 100)
     (q/sketch
      :title (str '~snip-name)
      :size ~(:size opts default-size)
      :renderer ~(:renderer opts :java2d)
      :setup (fn []
               ~(:setup opts)
               (q/frame-rate 5))
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

(defmacro defsnippet [snip-name opts & body]
  (if (clj-compilation?)
    ;; Clojure version
    `(def ~(vary-meta snip-name assoc
                    :test `(fn [] (snippet-as-test ~snip-name ~opts ~@body)))
     (fn []
       (q/sketch
        :title (str '~snip-name)
        :size ~default-size
        :setup (fn []
                 (q/frame-rate 5)
                 ~(:setup opts))
        :renderer ~(:renderer opts :java2d)
        :draw (fn [] ~@body))))

    ;; ClojureScript version
    `(do
     (defn ~snip-name []
       (quil.core/sketch
        :size ~(:size opts default-size)
        :renderer ~(:renderer opts :p2d)
        :host ~(:host opts default-host)

        :setup (fn []
                 ~(:setup opts)
                 (q/frame-rate 5))

        :draw (fn []
                (try
                  ~@body
                  (catch js/Error e#
                    (swap! quil.snippet/failed inc)
                    (throw (js/Error. e#)))
                  (finally (q/exit))))))

     (swap! quil.snippet/test-data conj
            {:name (name '~snip-name)
             :ns ~(str (ns-name *ns*))
             :fn ~snip-name}))))
