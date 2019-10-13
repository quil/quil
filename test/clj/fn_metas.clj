(ns fn-metas
  (:require quil.core
            quil.middleware
            [quil.helpers.docs :refer [link-to-processing-reference
                                       link-to-p5js-reference]]
            [quil.snippets.all-snippets :as as]
            [clojure.set :as set]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]
            [clj-http.client :as http]
            [cheshire.core :as json]))

(defn get-public-fns []
  (mapcat #(->> % the-ns ns-publics)
          ['quil.core 'quil.middleware]))

(defn get-public-fns-metas []
  (map #(-> % val meta) (get-public-fns)))

(defn get-api-fn-names []
  (->> (get-public-fns-metas)
       (filter #(some #{:processing-name :p5js-name} (keys %)))
       (map #(-> % :name name))))

(defn get-fn-names-covered-by-snippets []
  (into #{} (mapcat :fns as/all-snippets)))

(defn valid-link? [link]
  (try
    (-> link (http/get {:throw-exceptions false}) :status (= 200))
    (catch Exception e
      (println e)
      false)))

(deftest processing-links-valid
  (doseq [{:keys [link valid?]} (->> (get-public-fns-metas)
                                     (map link-to-processing-reference)
                                     (remove nil?)
                                     (pmap (fn [link]
                                             {:link link
                                              :valid? (valid-link? link)})))]
    (is valid? (str "Link " link " is not valid"))))

(defn p5js-method-or-property? [item]
  (->> (get item "itemtype")
       (contains? #{"method" "property"})))

(defn p5js-method-names []
  (let [items (-> (http/get "https://p5js.org/reference/data.min.json")
                  :body
                  (json/parse-string)
                  (get "classitems"))
        method-names (->> items
                          (filter p5js-method-or-property?)
                          (map #(str (get % "class") "." (get % "name")))
                          (map #(string/replace % "p5." "")))]
    (into #{} method-names)))

(deftest p5js-names-valid
  (let [p5js-names (p5js-method-names)]
    (doseq [p5js-name (->> (get-public-fns-metas)
                           (map :p5js-name)
                           (remove nil?)
                           (map #(string/replace % "()" "")))]
      (is (contains? p5js-names p5js-name)))))

(defn max-docstring-length [docstring]
  (->> (string/split docstring #"\n")
       (map count)
       (apply max)))

(deftest max-80-characters-length-in-docstrings
  (doseq [{:keys [name doc]} (get-public-fns-metas)
          :when doc]
    (is (<= (max-docstring-length doc) 80)
        (str "Function " name " has docstring longer than 80 chars."))))

(deftest all-public-functions-should-have-a-snippet
  (let [covered (get-fn-names-covered-by-snippets)]
    (doseq [fn-name (get-api-fn-names)]
      (is (contains? covered fn-name)))))
