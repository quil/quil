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
  (let [cmd (if (= (System/getenv "RUNNER_OS") "Windows")
              "clj" "clojure")]
    (println (str "$ " cmd " -version"))
    (bp/shell cmd "-version")))
