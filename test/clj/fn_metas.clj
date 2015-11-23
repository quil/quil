(ns fn-metas
  (:require quil.core
            quil.middleware
            [quil.helpers.docs :refer [link-to-processing-reference]]
            [clojure.string :refer [split]]
            [clojure.test :refer [deftest is]]
            [clj-http.client :as http]))

(defn get-public-fns-metas []
  (mapcat #(->> % the-ns ns-publics vals (map meta))
          ['quil.core 'quil.middleware]))

(defn valid-link? [link]
  (-> link (http/get {:throw-exceptions false}) :status (= 200)))

(deftest processing-links-valid
  (doseq [{:keys [link valid?]} (->> (get-public-fns-metas)
                                     (map link-to-processing-reference)
                                     (remove nil?)
                                     (pmap (fn [link]
                                             {:link link
                                              :valid? (valid-link? link)})))]
    (is valid? (str "Link " link " is not valid"))))

(defn max-docstring-length [docstring]
  (->> (split docstring #"\n")
       (map count)
       (apply max)))

(deftest max-80-characters-length-in-docstrings
  (doseq [{:keys [name doc]} (get-public-fns-metas)
          :when doc]
    (is (<= (max-docstring-length doc) 80)
        (str "Function " name " has docstring longer than 80 chars."))))
