(ns build.cljs-advanced-test
  "Verify that CLJS advanced compilatio works with quil release JAR"
  (:require
   [clojure.tools.build.api :as b]
   [build :as qb]))

(defn deps [mvn-version]
  (str
   "{:deps {\n"
   "  org.clojure/clojurescript {:mvn/version \"1.11.132\"}\n"
   "  quil/quil {:mvn/version \"" mvn-version "\"}}}\n"))

(defn make-deps [{:keys [mvn-version]}]
  (b/delete {:path "/tmp/cljs-advanced"})
  (b/write-file {:path "/tmp/cljs-advanced/deps.edn"
                 :string (deps mvn-version)})
  (b/copy-file {:src "dev/sample.cljs"
                :target "/tmp/cljs-advanced/src/sample.cljs"}))

;; clojure -T:build build.cljs-advanced-test/advanced-compile
(defn advanced-compile [_]
  (qb/clean _)
  (qb/release _)
  (qb/deploy (dissoc _ :clojars))
  (make-deps (assoc _ :mvn-version (qb/release-version _)))
  (println (slurp "/tmp/cljs-advanced/deps.edn"))
  (let [args ["clojure"
              "-M" "-m" "cljs.main"
              "--optimizations" "advanced"
              "-c" "sample"]
        {:keys [exit]}
        (b/process
         {:dir "/tmp/cljs-advanced"
          :command-args args})]
    (when-not (zero? exit)
      (throw (ex-info "Compile failed" {})))))
