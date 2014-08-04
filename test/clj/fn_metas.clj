(ns fn-metas
  (:require quil.core
            [quil.helpers.docs :refer [link-to-processing-reference]]
            [clojure.string :refer [split]]
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

(defn max-docstring-length [docstring]
  (->> (split docstring #"\n")
       (map count)
       (apply max)))

(deftest max-80-characters-length-in-docstrings
  (doseq [{:keys [name doc]} @(resolve 'quil.core/fn-metas)
          :when doc]
    (is (<= (max-docstring-length doc) 80)
        (str "Function " name " has docstring longer than 80 chars."))))

(deftest test-sketch-docstring-up-to-date
  (let [sketch-docstring (-> #'quil.core/sketch meta :doc)
        applet-docstring (-> #'quil.applet/applet meta :doc)]
    (is (= sketch-docstring applet-docstring)
        "Docstrings for quil.core/sketch and quil.applet/applet must be equal.
         Otherwise quil.core/sketch might become stale.")))
