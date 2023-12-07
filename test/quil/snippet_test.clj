(ns quil.snippet-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :as t]
   [quil.snippets.all-snippets :as as]
   [quil.test-util :as tu]))

(defn verify-expected-snapshot-exists [{:keys [ns name skip-image-diff?]}]
  (when-not skip-image-diff?
    (doseq [platform ["clj" #_"cljs"] ;; 28 missing cljs cases
            :let [expected (tu/expected-image platform name)]]
      (t/is (.exists (io/as-file expected))
            (str "Missing expected image " expected " for "
                 ns " " name)))))

(t/deftest expected-images-exist
  (doseq [snippet as/all-snippets]
    (verify-expected-snapshot-exists snippet)))

