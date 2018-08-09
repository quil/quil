(ns quil.snippet
  (:require [quil.util :refer [clj-compilation?]]
            [quil.core :as q]
            [quil.util :refer [no-fn]]
            [quil.snippets.all-snippets :as as]
            [clojure.test :as t :refer [is]]
            [clojure.string :as cstr]
            clojure.pprint

            ;;; Require all snippets.
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

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn run-snippet-as-test [{:keys [ns name opts setup body body-str]}]
  (let [result (promise)
        time (System/currentTimeMillis)
        snip-name (str ns "/" name)]
    (println "Test:" snip-name)
     (when manual?
       (clojure.pprint/pprint body-str))
     (q/sketch
      :title snip-name
      :size (:size opts default-size)
      :renderer (:renderer opts :java2d)
      :setup (fn []
               (setup)
               (q/frame-rate 5))
      :settings (fn []
                  (:settings opts))
      :draw (fn []
              (try
                (q/background 255)
                (body)
                (catch Exception e
                  (println "Error" e)
                  (.printStackTrace e)
                  (deliver result e))
                (finally
                  (when (and (> (- (System/currentTimeMillis) time)
                                1000)
                             (not manual?))
                    (q/exit)))))
      :on-close #(deliver result nil))
     (t/is (nil? @result))))

(defn define-snippet-as-test [{:keys [ns name opts setup body body-str] :as snippet}]
  (let [test-name (str (cstr/replace ns "." "_")
                       "_"
                       name)]
   (intern 'quil.snippet
           (vary-meta (symbol test-name) assoc
                      :test #(run-snippet-as-test snippet)
                      :test-set (let [cur (swap! current-test inc)
                                      set (quot cur tests-in-set)]
                                  (when (zero? (mod cur tests-in-set))
                                    (println "Test set" set (str " Run with: lein test :set-" set)))
                                  set))
           (fn []
             (q/sketch
              :title name
              :size default-size
              :setup (fn []
                       (q/frame-rate 5)
                       (setup))
              :renderer (:renderer opts :java2d)
              :draw body)))))

(doseq [snippet @as/all-snippets]
  (define-snippet-as-test snippet))

(t/deftest all-snippets-map-to-function
  (doseq [{:keys [fns name]} @as/all-snippets
          fn fns]
    (when (nil? (ns-resolve 'quil.core (symbol fn)))
      (throw (ex-info (str "Snippet '" name "' matches to non-existent function '" fn "'")
                      {}))))
  (t/is true))


