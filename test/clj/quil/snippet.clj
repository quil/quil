(ns quil.snippet
  (:require [quil.core :as q]
            [quil.snippets.all-snippets :as as]
            [clojure.test :as t :refer [is]]
            [clojure.string :as cstr]
            clojure.pprint))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn run-snippet-as-test [{:keys [ns name opts setup body body-str mouse-clicked]}]
  (let [result (promise)
        snip-name (str ns "/" name)]
    (println mouse-clicked)
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
     :mouse-clicked mouse-clicked
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
                 (when (not manual?)
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

(doseq [snippet as/all-snippets]
  (define-snippet-as-test snippet))

(t/deftest all-snippets-map-to-function
  (doseq [{:keys [fns name]} as/all-snippets
          fn fns]
    (when (nil? (ns-resolve 'quil.core (symbol fn)))
      (throw (ex-info (str "Snippet '" name "' matches to non-existent function '" fn "'")
                      {}))))
  (t/is true))

