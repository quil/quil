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

(t/use-fixtures :once sth/imagemagick-installed)

(defn run-snippet-as-test [snippet test-name]
  (let [result (promise)
        snip-name (str (:ns snippet)
                       "/"
                       (:name snippet))
        opts (:opts snippet)
        actual-file (sth/actual-image "clj" (:name snippet))]
    (when sth/log-test?
      (println (str "clojure -X:test :vars '[quil.snippets.snapshot-test/"
                    test-name "]'")))
    (when sth/manual?
      (clojure.pprint/pprint (:body-str snippet)))
    (q/sketch
     :title snip-name
     :size (:size opts sth/default-size)
     :renderer (:renderer opts :java2d)
     :setup (fn []
              ((:setup snippet))
              ;; only slow down frame rate during manual runs or CI
              ;; in CI the hope is this will reduce SEGV problems
              (when (or sth/manual? sth/github-actions?)
                (q/frame-rate 4)))
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
             (when (not sth/manual?)
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
               :size sth/default-size
               :setup (fn []
                        (q/frame-rate 5)
                        (setup))
               :renderer (:renderer opts :java2d)
               :draw body)))))

(doseq [snippet as/all-snippets]
  (define-snippet-as-test snippet))

;; view image diffs with
;; $ eog dev-resources/snippet-snapshots/clj/normal/*difference.png
