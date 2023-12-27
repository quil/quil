(ns quil.test-runner
  "Runs tests mounted as `dev-tests.js` from `resources/public/test-harness.html`"
  (:require
   ;; ensure p5js is loaded first to prevent occasional load error
   cljsjs.p5

   #_[clojure.test :as t :refer [deftest is] :include-macros true]
   [figwheel.main.testing :refer-macros [run-tests-async]]

   ;; require namespaces to tests
   quil.calculation-test
   ))

;; uncomment to verify browser version
#_(deftest verify-browser
    (is (empty? (.-userAgent js/navigator)))
    (is (empty? (.-appName js/navigator)))
    (is (empty? (.-appVersion js/navigator))))

(defn -main [& _args]
  (run-tests-async 5000))
