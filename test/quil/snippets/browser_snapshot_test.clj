(ns quil.snippets.browser-snapshot-test
  (:require
   [clj-http.client :as http]
   [clojure.edn :as edn]
   [clojure.test :as t]
   [etaoin.api :as etaoin]
   [quil.snippets.test-helper :as sth]))

;; geckodriver is producing 625x625 images for actuals vs 500x500 for reference
(defn- geckodriver-installed? []
  (sth/installed? "geckodriver" "--version"))

;; download driver from https://googlechromelabs.github.io/chrome-for-testing/
(defn- chromedriver-installed? []
  (sth/installed? "chromedriver" "--version"))

(defn- test-file-server-running? []
  (try
    (= (:status (http/get "http://localhost:3000/test.html"
                          {:throw-exceptions false}))
       200)
    (catch java.lang.Exception _e false)))

(defn preconditions [f]
  (t/is (geckodriver-installed?)
        "geckodriver is not installed. Install it and rerun the test.")
  (t/is (chromedriver-installed?)
        "chromedriver is not installed. Install it and rerun the test.")
  (f))

(defn server-setup [f]
  ;; if server exists, re-use existing
  (if (test-file-server-running?)
    (f)
    ;; late bind to figwheel so test can load without the :fig alias
    (do ((requiring-resolve 'figwheel.main.api/start) {:mode :serve} "snippets")
        (try (f)
             (finally
               ((requiring-resolve 'figwheel.main.api/stop) "snippets"))))))

(def driver (atom nil))
(defn etaoin-setup [f]
  (t/is (test-file-server-running?)
        (str "Seems like file server with test page is not running. "))
  (let [browser (etaoin/chrome {:size [1280 1024]})]
    (reset! driver browser)
    (try (f)
         (finally
           (etaoin/quit browser)
           (reset! driver nil)))))

(t/use-fixtures :once
  (t/join-fixtures [sth/imagemagick-installed
                    preconditions
                    server-setup
                    etaoin-setup]))

(defn snippet-elements [driver]
  (->> (etaoin/query-all driver {:tag :option})
       (map-indexed
        (fn [ind el]
          {:name (etaoin/get-element-text-el driver el)
           :index ind
           :skip-image-diff?
           (etaoin/get-element-attr-el driver el :data-skip-image-diff)
           :accepted-diff-threshold
           (edn/read-string (etaoin/get-element-attr-el driver el :data-accepted-diff-threshold))}))
       ;; test only snippets that don't have skip-image-diff attribute.
       (remove :skip-image-diff?)
       (doall)))

;; TODO: generate snippet test set without starting server instead of combining
;; all tests into one. Currently the tests are ordered by
;; test/cljs/quil/snippet/init-test-selection hence why `snippet-elements` above
;; crawls the site for each test. However, if tests are generated ahead of time,
;; kaocha focus-test and friends could run individual examples.
(t/deftest ^:cljs-snippets
  all-cljs-snippets-produce-expected-output
  (etaoin/go @driver "http://localhost:3000/test.html")
  (let [elements (snippet-elements @driver)]
    (t/is (seq elements) "unable to find tests on test.html harness")
    (doseq [{:keys [name index accepted-diff-threshold]} elements]
      (etaoin/go @driver (str "http://localhost:3000/test.html#" index))
      (etaoin/refresh @driver)
      (let [actual-file (sth/actual-image "cljs" name)]
        (etaoin/screenshot-element @driver {:tag :canvas} actual-file)
        (sth/verify-reference-or-update
         name "cljs" actual-file accepted-diff-threshold)
        ;; disable for now, as lots of chatty logging, but useful to inspect
        ;; (t/is (empty? (etaoin/get-logs @driver)))
        ))))

;; view image diffs with
;; $ eog dev-resources/snippet-snapshots/cljs/normal/*difference.png
