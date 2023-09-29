(ns quil.test-runner
  (:require
   [cljs.test]
   [figwheel.main.testing :refer-macros [run-tests-async]]

   ;; require namespaces to tests
   quil.calculation-test
   ))

(defn -main [& _args]
  (run-tests-async 5000))
