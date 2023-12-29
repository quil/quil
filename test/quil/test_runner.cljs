(ns quil.test-runner
  "Runs tests mounted as `dev-tests.js` from `resources/public/test-harness.html`"
  (:require
   ;; ensure p5js is loaded first to prevent occasional load error
   cljsjs.p5

   [clojure.test :as t :refer [deftest is] :include-macros true]
   [figwheel.main.testing :refer-macros [run-tests-async]]

   ;; require namespaces to tests
   quil.calculation-test
   ))

;; print out browser version information
(deftest verify-browser
  (let [user-agent (.-userAgent js/navigator)
        version (.-appVersion js/navigator)]
    (println "browser version: " version)
    (is (seq version))
    (println "browser user-agent: " user-agent)
    (is (seq user-agent))))

(defn -main [& _args]
  (run-tests-async 5000))
