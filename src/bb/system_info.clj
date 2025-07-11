(ns bb.system-info
  (:require
   [babashka.process :as bp]))

(defn versions []
  (println "$ uname -a")
  (bp/shell "uname" "-a")
  (println "$ java -version")
  (bp/shell "java" "-version")
  (println "$ bb version")
  (bp/shell "bb" "version")
  (when-not (= (System/getenv "RUNNER_OS") "Windows")
    (println "$ clojure -version")
    (bp/shell "clojure" "-version"))

  ;; imagemagick utilities for image compare
  (doseq [pname ["compare" "convert" "identify"]]
    (println "$" pname "-version")
    (bp/shell (str pname " -version"))))
