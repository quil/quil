(ns quil.snippet
  (:require [quil.core :as q]
            [quil.snippets.all-snippets :as as]
            [clojure.java.io :as io]
            [clojure.test :as t]
            [clojure.java.shell :as sh]
            [clojure.string :as string]
            [etaoin.api :as etaoin]
            clojure.pprint))

(def default-size [500 500])

(def manual? (-> (System/getenv) (get "MANUAL") boolean))

(def tests-in-set 50)
(def current-test (atom -1))

(defn skip-automated-compare? [snippet-name]
  (contains? #{"current-frame-rate-target-frame-rate"
               "frame-rate"
               "load-shader"
               "noise"
               "noise-detail"
               "ortho-perspective"
               "random-2d"
               "random-3d"
               "random"
               "random-gaussian"
               "resize-sketch"
               "save"
               "time-and-date"} snippet-name))

(defn- imagemagick-installed? []
  (try
    (sh/sh "compare" "-version")
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
      (Double/parseDouble result))
    (println "Imagemagick not detected. Please install it for automated image comparison to work.")))

(defn- replace-suffix [file-name suffix]
  (string/replace file-name #"(\w+)\.(\w+)$" (str suffix ".$2")))

(defn assert-unchanged-snippet-output [name]
  (when-not (skip-automated-compare? name)
    (let [n          (str "snippet-snapshots/clj/" name)
          _          (q/save (str "dev-resources/" n "-actual.png"))
          expected   (.getAbsolutePath (io/file (io/resource (str n "-expected.png"))))
          actual     (replace-suffix expected "actual")
          difference (replace-suffix expected "difference")
          result     (compare-images expected actual difference)
          threshold  0.02]
      (when (number? result)
        (when (<= result threshold)
          (io/delete-file (io/file actual))
          (io/delete-file (io/file difference)))
        (t/is (<= result threshold))))))

(defn run-snippet-as-test [{:keys [ns name opts setup body body-str mouse-clicked]}]
  (let [result (promise)
        snip-name (str ns "/" name)]
    (println mouse-clicked)
    (println "Test:" snip-name)
    (when manual?
      (clojure.pprint/pprint body-str))
    (q/sketch
     :title snip-name
     :size (:size opts default-size)
     :renderer (:renderer opts :java2d)
     :setup (fn []
              (setup)
              (q/frame-rate 5))
     :mouse-clicked mouse-clicked
     :settings (fn []
                 (:settings opts))
     :draw (fn []
             (try
               (q/background 255)
               (body)
                 (assert-unchanged-snippet-output name)
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

(t/deftest ^:manual
  all-cljs-snippets-produce-expected-output
  (let [driver (etaoin/firefox)
        threshold 0.0001]
    (etaoin/go driver "http://localhost:3000/test.html")
    (let [elements (etaoin/query-all driver {:tag :option})
          names    (doall (map #(etaoin/get-element-text-el driver %) elements))]
      (dotimes [i (count elements)]
        (let [name (nth names i)]
          (when-not (skip-automated-compare? name)
            (let [n          (str "snippet-snapshots/cljs/" name "-expected.png")
                  expected   (.getAbsolutePath (io/file (io/resource n)))
                  actual     (replace-suffix expected "actual")
                  difference (replace-suffix expected "difference")]
              (etaoin/go driver (str "http://localhost:3000/test.html#" i))
              (etaoin/refresh driver)
              (etaoin/screenshot-element driver {:tag :canvas} actual)
              (let [result (compare-images expected actual difference)]
                (when (number? result)
                  (when (<= result threshold)
                    (io/delete-file (io/file actual))
                    (io/delete-file (io/file difference)))
                  (t/is (<= result threshold)
                        (str "Image comparison for " name " not within acceptable threshold: " threshold)))))))))
    (etaoin/quit driver)))
