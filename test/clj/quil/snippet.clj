(ns quil.snippet
  (:require [quil.util :refer [clj-compilation?]]
            [quil.core :as q]
            [quil.util :refer [no-fn]]
            [quil.snippets.macro :as snippets]
            [clojure.test :as t]
            clojure.pprint

            ;;; Require tests.
            [quil.snippets image environment input output rendering state structure transform]
            [quil.snippets.image rendering pixels loading-and-displaying]
            [quil.snippets.color creating-and-reading setting utility-macros]
            [quil.snippets.data conversion]
            [quil.snippets.lights-camera camera coordinates lights material-properties]
            [quil.snippets.math calculation random trigonometry]
            [quil.snippets.shape attributes curves loading-and-displaying primitives-2d primitives-3d vertex]
            [quil.snippets.transform utility-macros]
            [quil.snippets.typography attributes loading-and-displaying metrics]))

(def default-size [500 500])
(def default-host {:p2d "quil-test-2d"
                   :p3d "quil-test-3d"})

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defmacro snippet-as-test [snip-name opts & draw-fn-body]
  `(let [result# (promise)
         time# (System/currentTimeMillis)]
     (when manual?
       (println "Test:" '~snip-name)
       (clojure.pprint/pprint '~draw-fn-body))
     (q/sketch
      :title (str '~snip-name)
      :size ~(:size opts default-size)
      :renderer ~(:renderer opts :java2d)
      :setup (fn []
               ~(:setup opts)
               (q/frame-rate 5))
      :settings (fn []
                  ~(:settings opts))
      :draw (fn []
              (try
                (q/background 255)
                ~@draw-fn-body
                (catch Exception e#
                  (println "Error" e#)
                  (.printStackTrace e#)
                  (deliver result# e#))
                (finally
                  (when (and (> (- (System/currentTimeMillis) time#)
                                1000)
                             (not manual?))
                    (q/exit)))))
      :on-close #(deliver result# nil))
     (t/is (nil? @result#))))

(defmacro defsnippet [snip-name opts & body])

(defmacro deftest-from-snippet [snip-name opts body]
  (if (clj-compilation?)
    ;; Clojure version
    `(def ~(vary-meta snip-name assoc
                      :test `(fn [] (snippet-as-test ~snip-name ~opts ~@body))
                      :test-set (let [cur (swap! current-test inc)
                                      set (quot cur tests-in-set)]
                                  (when (zero? (mod cur tests-in-set))
                                    (println "Test set" set (str " Run with: lein test :set-" set)))
                                  set))
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
               :fn ~snip-name}))))

(defmacro realize-snippets
  "Generates a test for each snippet stored in snippet collection."
  []
  `(do ~@(map (fn [{:keys [name opts body]}]
                `(deftest-from-snippet ~name ~opts ~body))
              (snippets/get-snippets))))

;(realize-snippets)

(t/deftest all-snippets-map-to-function
  (doseq [{:keys [fns name]} (snippets/get-snippets)
          fn fns]
    (when (nil? (ns-resolve 'quil.core (symbol fn)))
      (throw (ex-info (str "Snippet '" name "' matches to non-existent function '" fn "'")
                      {}))))
  (t/is true))


