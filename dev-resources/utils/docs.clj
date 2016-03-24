(ns utils.docs
  (:require [clojure.tools.reader :as rdr]
            [clojure.tools.reader.reader-types :refer [input-stream-push-back-reader]]
            [clojure.java.io :as io]
            [clojure.set :refer [union]]
            [quil.helpers.docs :refer [link-to-processing-reference]]))

(defn read-all [file feature]
  (let [reader (->> file
                    io/resource
                    io/input-stream
                    input-stream-push-back-reader)
        endof (gensym)]
    (->> #(rdr/read {:eof endof :read-cond :allow :features #{feature}} reader)
         (repeatedly)
         (take-while #(not= % endof))
         (doall))))

(defn get-public-fns [all-forms]
  (filter #(-> % second meta (contains? :category))
          all-forms))

(defn get-full-meta [[type name docstring args :as form]]
  (let [mt (meta name)
        args (if (vector? args)
               #{args}
               (set (mapv first (drop 3 form))))]
    (assoc mt
      :args args
      :name name
      :docstring docstring
      :what (if (= 'defmacro type) :macro :fn)
      :link (link-to-processing-reference mt))))

(defn get-metas [forms type]
  (->> forms
       get-public-fns
       (map get-full-meta)
       (map #(assoc % :type type))
       (map #(vector (:name %) %))
       (into {})))

(defn merge-metas [meta1 meta2]
  (let [args1 (:args meta1)
        args2 (:args meta2)
        merged-args (for [arg (union args1 args2)]
                      {:value arg
                       :type (cond (and (args1 arg) (args2 arg)) :both
                                   (args1 arg) (:type meta1)
                                   (args2 arg) (:type meta2))})]
    (-> (merge meta1 meta2)
        (assoc :args merged-args
               :type :both))))

(defn merge-all-metas [metas1 metas2]
  (merge-with merge-metas metas1 metas2))

(def files ["quil/core.cljc" "quil/middleware.cljc"])

(defn generate-metas-to-file [file]
  (let [clj (-> (mapcat #(read-all % :clj) files)
                (get-metas :clj))
        cljs (-> (mapcat #(read-all % :cljs) files)
                 (get-metas :cljs))
        merged (merge-all-metas clj cljs)]
    (with-open [wtr (io/writer file)]
      (clojure.pprint/pprint merged wtr))))

(generate-metas-to-file "out.clj")

