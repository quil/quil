(ns quil.snippet
  (:require
   [clj-http.client :as http]
   [clojure.java.io :as io]
   [clojure.java.shell :as sh]
   [clojure.pprint]
   [clojure.string :as string]
   [clojure.test :as t]
   [etaoin.api :as etaoin]
   [quil.core :as q]
   [quil.snippets.all-snippets :as as]
   [quil.test-util :as tu]))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def update-screenshots? (-> (System/getenv) (get "UPDATE_SCREENSHOTS") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn installed? [& cmds]
  (try
    (apply sh/sh cmds)
    true
    (catch java.io.IOException e false)))

(defn check-dependencies [f]
  (if (and (installed? "compare" "-version")
           (installed? "convert" "-version")
           (installed? "identify" "-version"))
    (f)
    (do
      (println "Imagemagick not detected. Please install it for automated image comparison to work.")
      false)))

(t/use-fixtures :once check-dependencies)

(defn- compare-images
  "Compares images at file paths `expected` and `actual` and produces another
  image at path `difference` which highlights any differences in the images.

  Returns a number between `0` and `1` indicating a measure of the difference,
  with `0` indicating the images are the same, and `nil` if imagemagick not
  installed."
  [expected actual difference]
  ;; use imagemagick compare executable for comparison
  ;; see https://imagemagick.org/script/compare.php
  (let [{:keys [err]} (sh/sh "compare" "-metric" "mae" expected actual difference)
        result        (second (re-find #"\((.*)\)" err))]
    (if result
      (Double/parseDouble result)
      (do
        (println "Couldn't parse output of compare. Got following string: " err)
        1.0))))

(defn save-snippet-screenshot-as-expected [name]
  (let [filename (tu/expected-image "clj" name)]
    (println "saving screenshot to " filename)
    (q/save filename)))

(defn assert-comparison! [test-name expected-file actual-file diff-file]
  (let [result (compare-images expected-file actual-file diff-file)
        ;; identify output to verify image sizes are equivalent
        identify (:out (sh/sh "identify" actual-file expected-file))
        threshold 0.02]
    (when (number? result)
      (if (<= result threshold)
        (do
          (io/delete-file (io/file actual-file))
          (io/delete-file (io/file diff-file)))
        ;; add actual and expected images to difference image for easier comparison
        (sh/sh "convert"
               actual-file
               diff-file
               expected-file
               "+append"
               diff-file))
      (t/is (<= result threshold)
            (str "Image differences in \"" test-name "\", see: " diff-file "\n"
                 identify)))))

(defn assert-unchanged-snippet-output [test-name]
  (let [actual-file (tu/actual-image "clj" test-name)
        _           (q/save actual-file)
        expected-file (tu/expected-image "clj" test-name)
        diff-file (tu/diff-image "clj" test-name)]
    (assert-comparison! test-name expected-file actual-file diff-file)))

(defn run-snippet-as-test [snippet]
  (let [result (promise)
        snip-name (str (:ns snippet)
                       "/"
                       (:name snippet))
        opts (:opts snippet)]
    (println "Test:" snip-name)
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
     :settings (fn []
                 (:settings opts))
     :draw (fn []
             (try
               (q/background 255)
               ((:body snippet))
               (when-not (:skip-image-diff? snippet)
                 (if update-screenshots?
                   (save-snippet-screenshot-as-expected (:name snippet))
                   (assert-unchanged-snippet-output (:name snippet))))
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

;; geckodriver is producing 625x625 images for actuals vs 500x500 for reference
(defn- geckodriver-installed? []
  (installed? "geckodriver" "--version"))

;; download driver from https://googlechromelabs.github.io/chrome-for-testing/
(defn- chromedriver-installed? []
  (installed? "chromedriver" "--version"))

(defn- test-file-server-running? []
  (try
    (= (:status (http/get "http://localhost:3000/test.html"
                          {:throw-exceptions false}))
       200)
    (catch java.lang.Exception e false)))

(t/deftest ^:manual
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
      (let [; test only snippets that don't have skip-image-diff attribute.
            elements (->> (etaoin/query-all driver {:tag :option})
                          (map-indexed (fn [ind el] {:name (etaoin/get-element-text-el driver el)
                                                    :index ind
                                                    :skip-image-diff? (etaoin/get-element-attr-el driver el :data-skip-image-diff)}))
                          (remove #(:skip-image-diff? %))
                          (doall))]
        (doseq [{:keys [name index]} elements]
          (etaoin/go driver (str "http://localhost:3000/test.html#" index))
          (etaoin/refresh driver)
          (let [expected-file (tu/expected-image "cljs" name)
                actual-file (tu/actual-image "cljs" name)
                diff-file (tu/diff-image "cljs" name)]
            (if update-screenshots?
              (etaoin/screenshot-element driver {:tag :canvas} expected-file)
              (do
                (etaoin/screenshot-element driver {:tag :canvas} actual-file)
                (assert-comparison! name expected-file actual-file diff-file))))))
      (etaoin/quit driver))))

;; view image diffs with
;; $ eog dev-resources/snippet-snapshots/cljs/normal/*difference.png
