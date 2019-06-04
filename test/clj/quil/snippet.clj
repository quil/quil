(ns quil.snippet
  (:require [quil.core :as q]
            [quil.snippets.all-snippets :as as]
            [quil.test-util :as tu]
            [clojure.java.io :as io]
            [clojure.test :as t]
            [clojure.java.shell :as sh]
            [clojure.string :as string]
            [etaoin.api :as etaoin]
            [clj-http.client :as http]
            clojure.pprint))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn- imagemagick-installed? []
  (try
    (sh/sh "compare" "-version")
    (sh/sh "convert" "-version")
    true
    (catch java.io.IOException e false)))

(defn- compare-images
  "Compares images at file paths `expected` and `actual` and produces another
  image at path `difference` which highlights any differences in the images.

  Returns a number between `0` and `1` indicating a measure of the difference,
  with `0` indicating the images are the same, and `nil` if imagemagick not
  installed."
  [expected actual difference]
  ;; use imagemagick compare executable for comparison
  ;; see https://imagemagick.org/script/compare.php
  (if (imagemagick-installed?)
    (let [{:keys [err]} (sh/sh "compare" "-metric" "mae" expected actual difference)
          result        (second (re-find #"\((.*)\)" err))]
      (if result
        (Double/parseDouble result)
        (println "Couldn't parse output of compare. Got following string: " err)))
    (println "Imagemagick not detected. Please install it for automated image comparison to work.")))

(defn- replace-suffix [file-name suffix]
  (string/replace file-name #"(\w+)\.(\w+)$" (str suffix ".$2")))

(defn assert-unchanged-snippet-output [name]
  (let [n          (str (tu/path-to-snippet-snapshots "clj") name)
        _          (q/save (str "dev-resources/" n "-actual.png"))
        expected   (.getAbsolutePath (io/file (io/resource (str n "-expected.png"))))
        actual     (replace-suffix expected "actual")
        difference (replace-suffix expected "difference")
        result     (compare-images expected actual difference)
        threshold  0.02]
    (when (number? result)
      (if (<= result threshold)
        (do
          (io/delete-file (io/file actual))
          (io/delete-file (io/file difference)))
                                        ; add actual and expected images to difference image for easier comparison
        (sh/sh "convert" actual difference expected "+append" difference))
      (t/is (<= result threshold)))))

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
                 (assert-unchanged-snippet-output (:name snippet)))
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

(defn- geckodriver-installed? []
  (try
    (sh/sh "geckodriver" "--version")
    true
    (catch java.io.IOException e false)))

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
  (t/is (test-file-server-running?)
        (str "Seems like file server with test page is not running. "
             "Run 'lein with-profile cljs-testing do cljsbuild once tests, ring server' and rerun this test."))
  (when (and (geckodriver-installed?)
             (test-file-server-running?))
    (let [driver (etaoin/firefox)
          threshold 0.003]
      (etaoin/go driver "http://localhost:3000/test.html")
      (let [; test only snippets that don't have skip-image-diff attribute.
            elements (->> (etaoin/query-all driver {:tag :option})
                          (map-indexed (fn [ind el] {:name (etaoin/get-element-text-el driver el)
                                                     :index ind
                                                     :skip-image-diff? (etaoin/get-element-attr-el driver el :data-skip-image-diff)}))
                          (remove #(:skip-image-diff? %))
                          (doall))]
        (doseq [{:keys [name index]} elements]
          (let [n          (str (tu/path-to-snippet-snapshots "cljs") name "-expected.png")
                expected   (.getAbsolutePath (io/file (io/resource n)))
                actual     (replace-suffix expected "actual")
                difference (replace-suffix expected "difference")]
            (etaoin/go driver (str "http://localhost:3000/test.html#" index))
            (etaoin/refresh driver)
            (etaoin/screenshot-element driver {:tag :canvas} actual)
            (let [result (compare-images expected actual difference)]
              (when (number? result)
                (when (<= result threshold)
                  (io/delete-file (io/file actual))
                  (io/delete-file (io/file difference)))
                (t/is (<= result threshold)
                      (str "Image comparison for " name " not within acceptable threshold: " threshold)))))))
      (etaoin/quit driver))))
