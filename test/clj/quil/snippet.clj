(ns quil.snippet
  (:require [quil.core :as q]
            [quil.snippets.all-snippets :as as]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :as t]
            [clojure.java.shell :as sh]
            [clojure.string :as string]
            clojure.pprint))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn- compare-images [expected actual difference]
  (let [{:keys [err]} (sh/sh "compare" "-metric" "mae" expected actual difference)
        result        (second (re-find #"\((.*)\)" err))]
    (edn/read-string result)))

(defn- replace-suffix [file-name suffix]
  (string/replace file-name #"(\w+)\.(\w+)$" (str suffix ".$2")))

(defn assert-unchanged-snippet-output [name]
  (when-not (contains? #{"current-frame-rate-target-frame-rate"
                         "noise"
                         "noise-detail"
                         "random-2d"
                         "random-3d"
                         "random"
                         "random-gaussian"
                         "resize-sketch"
                         "time-and-date"} name)
    (let [n          (str "snippet-snapshots/clj/" name)
          _          (q/save (str "resources/" n "-actual.png"))
          expected   (.getAbsolutePath (io/file (io/resource (str n "-expected.png"))))
          actual     (replace-suffix expected "actual")
          difference (replace-suffix expected "difference")
          result     (compare-images expected actual difference)
          threshold  0.02]
      (when (<= result threshold)
        (io/delete-file (io/file actual))
        (io/delete-file (io/file difference)))
      (t/is (<= result threshold)))))

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
               (assert-unchanged-snippet-output name)
               (catch Exception e
                 (println "Error" e)
                 (.printStackTrace e)
                 (deliver result e))
               (finally
                 (when (not manual?)
                   (q/exit)))))
     :on-close #(deliver result nil))
    (t/is (nil? @result))))

(defn define-snippet-as-test [{:keys [ns name opts setup body] :as snippet}]
  (let [test-name (str (string/replace ns "." "_")
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

