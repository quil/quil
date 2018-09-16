(ns utils.snippets-generator
  (:require [quil.snippets.all-snippets :as as]
            [cljs.pprint :as pprint]
            clojure.set)
  (:import [goog.string StringBuffer]))

(defn prepare-snippet-for-saving
  "Clean up snippet to remove all functions and leave only string data
  (e.g. source code of body and setup)."
  [snippet]
  (-> snippet
      (select-keys [:fns :ns :name :body-str :setup-str :opts])
      (clojure.set/rename-keys {:body-str :draw :setup-str :setup})
      ; Some snippets contains :settings function so remove it.
      (update-in [:opts :settings] (fn [_] nil))
      (assoc :target :cljs)))

(defn generate-snippets-to-textarea
  "Converts snippets to a EDN string and writes to the <textarea> object to
  be copied manually by developer."
  []
  (let [buffer (StringBuffer.)]
    (pprint/pprint (map prepare-snippet-for-saving as/all-snippets) (StringBufferWriter. buffer))
    (set! (.-value (.querySelector js/document "textarea"))
          (str buffer))))

(generate-snippets-to-textarea)

