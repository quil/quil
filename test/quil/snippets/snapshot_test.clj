(ns quil.snippets.snapshot-test
  (:require
   [clj-http.client :as http]
   [clojure.edn :as edn]
   [clojure.pprint]
   [clojure.string :as string]
   [clojure.test :as t]
   [etaoin.api :as etaoin]
   [quil.core :as q]
   [quil.snippets.all-snippets :as as]
   [quil.snippets.test-helper :as sth]))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(t/use-fixtures :once sth/check-dependencies)

(defn run-snippet-as-test [snippet test-name]
  (let [result (promise)
        snip-name (str (:ns snippet)
                       "/"
                       (:name snippet))
        opts (:opts snippet)
        actual-file (sth/actual-image "clj" (:name snippet))]
    (println (str "clojure -X:test :vars '[quil.snippets.snapshot-test/"
                  test-name "]'"))
    (when manual?
      (clojure.pprint/pprint (:body-str snippet)))
    (q/sketch
     :title snip-name
     :size (:size opts default-size)
     :renderer (:renderer opts :java2d)
     :setup (fn []
              ((:setup snippet))
              (q/frame-rate 5))
     :mouse-clicked (:mouse-clicked snippet)
     :settings (fn [] (when-let [settings (:settings opts)]
                       (settings)))
     :draw
     (fn []
       (q/background 255)
       ;; some snippets have async calls, draw n frames before saving final copy
       (if (< (q/frame-count) (:delay-frames snippet))
         (try
           ((:body snippet))
           (catch Exception e
             (println "Error" e)
             (.printStackTrace e)))
         (try
           ((:body snippet))
           (when-not (:skip-image-diff? snippet)
             (q/save actual-file))
           (catch Exception e
             (println "Error" e)
             (.printStackTrace e)
             (deliver result e))
           (finally
             (when (not manual?)
               (q/exit))))))
     :on-close #(deliver result nil))
    ;; block on @result promise and assert empty
    (t/is (nil? @result))
    ;; verify image matches reference on testing thread
    (when-not (:skip-image-diff? snippet)
      (sth/verify-reference-or-update (:name snippet) "clj" actual-file
                                      (:accepted-diff-threshold snippet)))))

(defn define-snippet-as-test [{:keys [ns name opts setup body] :as snippet}]
  (let [test-name (str (string/replace ns "." "_")
                       "_"
                       name)]
    (intern 'quil.snippets.snapshot-test
            (vary-meta (symbol test-name) assoc
                       :clj-snippets true
                       :test #(run-snippet-as-test snippet test-name))
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

(t/deftest ^:cljs-snippets
  all-cljs-snippets-produce-expected-output
  (t/is (geckodriver-installed?)
        "geckodriver is not installed. Install it and rerun the test.")
  (t/is (chromedriver-installed?)
        "chromedriver is not installed. Install it and rerun the test.")
  (t/is (test-file-server-running?)
        (str "Seems like file server with test page is not running. "
             "Run 'lein with-profile cljs-testing do cljsbuild once tests, ring server' and rerun this test."))
  (when (and (geckodriver-installed?)
             (chromedriver-installed?)
             (test-file-server-running?))
    (let [driver (etaoin/chrome)]
      (etaoin/go driver "http://localhost:3000/test.html")
      (doseq [{:keys [name index accepted-diff-threshold]} (snippet-elements driver)]
        (etaoin/go driver (str "http://localhost:3000/test.html#" index))
        (etaoin/refresh driver)
        (let [actual-file (sth/actual-image "cljs" name)]
          (etaoin/screenshot-element driver {:tag :canvas} actual-file)
          (sth/verify-reference-or-update name "cljs" actual-file accepted-diff-threshold)))
      (etaoin/quit driver))))

;; view image diffs with
;; $ eog dev-resources/snippet-snapshots/cljs/normal/*difference.png
