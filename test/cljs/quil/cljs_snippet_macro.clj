(ns quil.cljs-snippet-macro
  (:require [quil.snippets.all-snippets :as as]))

(def default-size [500 500])
(def default-host {:p2d "quil-test-2d"
                   :p3d "quil-test-3d"})

(defmacro deftest-from-snippet
  "Generates a function from snippet to be used in cljs tests."
  [snip-name opts body]
  `(do
       (defn ~(vary-meta snip-name assoc :export true) []
         (quil.core/sketch
          :size ~(:size opts default-size)
          :renderer ~(:renderer opts :p2d)
          :host ~(or (:host opts)
                     (get default-host (:renderer opts :p2d)))

          :setup (fn [] ~(:setup opts))

          :draw (fn []
                  (try
                    ~@body
                    (catch js/Error e#
                      (swap! quil.snippet/failed inc)
                      (throw e#))
                    (finally (q/exit))))))

       (swap! quil.snippet/test-data conj
              {:name (name '~snip-name)
               :ns ~(str (ns-name *ns*))
               :fn ~snip-name})))

(defmacro generate-test-functions []
  nil
  #_(do ~@(map (fn [{:keys [name opts body]}]
                `(do
                   (deftest-from-snippet ~name ~opts ~body)))
              (as/all-snippets))))
