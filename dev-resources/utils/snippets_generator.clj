(ns utils.snippets-generator
  "Script to generate snippets.clj with snippet usages of all API functions."
  (:require [quil.snippets.all-snippets :as as]
            [clojure.tools.reader.edn :as edn]
            [clojure.java.io :as io]))

(defn prepare-snippet-for-saving
  "Clean up snippet to remove all functions and leave only string data
  (e.g. source code of body and setup)."
  [snippet]
  (-> snippet
      (select-keys [:fns :ns :name :body-str :setup-str :opts])
      (clojure.set/rename-keys {:body-str :draw :setup-str :setup})
      ; Some snippets contains :settings function so remove it.
      (update-in [:opts :settings] (fn [_] nil))
      (assoc :target :clj)))


(defn generate-snippets-to-file
  "Adds clj snippets to the provided file. The function assumes that file
  already exists and contains some snippets. It is intended to run after
  cljs snippets generated."
  [file]
  (let [existing-cljs-snippets (edn/read-string (slurp file))
        new-clj-snippets (map prepare-snippet-for-saving as/all-snippets)]
    (with-open [wtr (clojure.java.io/writer file)]
      (clojure.pprint/pprint (distinct (concat existing-cljs-snippets new-clj-snippets))
                             wtr))))

(generate-snippets-to-file "snippets.clj")




