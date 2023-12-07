(ns quil.snippet-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :as t]
   [quil.snippets.all-snippets :as as]
   [quil.test-util :as tu]))

;; run only this namespace:
;; clj -X:test :nses '[quil.snippet-test]'

(defn verify-expected-snapshot-exists [{:keys [ns name skip-image-diff?]}]
  (when-not skip-image-diff?
    (doseq [platform ["clj" #_"cljs"] ;; 28 missing cljs cases
            :let [expected (tu/expected-image platform name)]]
      (t/is (.exists (io/as-file expected))
            (str "Missing expected image " expected " for "
                 ns " " name)))))

;; Snippet tests compare against a reference image, this ensures all the
;; expected reference images exists
(t/deftest expected-images-exist
  (doseq [snippet as/all-snippets]
    (verify-expected-snapshot-exists snippet)))

;; Snippets :fns key describes the list of quil.core functions it depends on, so
;; this test verifies the functions exist.
(t/deftest all-snippets-map-to-function
  (doseq [{:keys [fns name]} as/all-snippets
          fn fns]
    (t/is (some? (ns-resolve 'quil.core (symbol fn)))
          (str "Snippet '" name "' matches to non-existent function '" fn "'"))))
