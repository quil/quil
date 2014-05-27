(ns fn-metas
  (:require quil.core
            [quil.helpers.docs :refer [link-to-processing-reference]]
            [clojure.test :refer [deftest is]]
            [clj-http.client :as http]))

(defn valid-link? [link]
  (-> link (http/get {:throw-exceptions false}) :status (= 200)))

(deftest processing-links-valid
  (let [metas @(resolve 'quil.core/fn-metas)]
    (doseq [{:keys [link valid?]} (->> metas
                                       (map link-to-processing-reference)
                                       (remove nil?)
                                       (pmap (fn [link]
                                               {:link link
                                                :valid? (valid-link? link)})))]
      (is valid? (str "Link " link " is not valid")))))
