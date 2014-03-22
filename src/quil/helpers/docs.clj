(ns ^{:doc "Functions for filtering/displaying docs in REPL and generating wiki pages."}
  quil.helpers.docs
  (:require [clojure.string :as cstr]
            [clojure.java.io :as io]))

(defn- fn-metas-with-orig-method-name
  "Returns a seq of metadata maps for all fns with a corresponding
  Processing API method."
  [fn-metas]
  (filter :processing-name fn-metas))

(defn matching-processing-methods
  "Takes a string representing the start of a method name in the
  original Processing API and returns a map of orig/new-name pairs"
  [fn-metas orig-name]
  (let [metas   (fn-metas-with-orig-method-name fn-metas)
        matches (filter #(.startsWith (str (:processing-name %)) orig-name) metas)]
    (into {} (map (fn [{:keys [name processing-name]}]
                    [(str processing-name) (str name)])
                  matches))))

(defn- fields-as-sorted-set
  "Make sorted set of all possible values for given field from metas.
  Example: metas [{:name 1} {:name 2} {:name nil} {:name 2} {:name 3}], field :name
  will return #{1 2 3}."
  [fn-metas field]
  (->> (map field fn-metas)
       (remove nil?)
       (into (sorted-set))))

(defn- find-subcategories
  "Sorted set of subcategories for given category based on provided metas."
  [fn-metas category]
  (let [in-cat (filter #(= category (:category %)) fn-metas)]
    (fields-as-sorted-set in-cat :subcategory)))

(defn- find-fns
  "Find the names of fns in category cat that also belong to the subcategory subcat."
  [fn-metas cat subcat]
  (let [res   (filter (fn [m]
                        (and (= cat (:category m))
                             (= subcat (:subcategory m))))
                      fn-metas)]
    (sort (map :name res))))

(defn- subcategory-map
  "Build map idx -> subcategory. Example:
  {\"1.1\" {:name \"Subcat 1\"
            :fns [\"hello\" \"world\"]}
   \"1.2\" {:name \"Subcat 2\"
            :fns [\"inc\" \"dec\"]}}"
  [fn-metas cat cat-idx]
  (let [subcats (->> (find-subcategories fn-metas cat)
                     (map (fn [subcat]
                       {:name subcat
                        :fns (find-fns fn-metas cat subcat)})))
        idx (map #(str cat-idx "." %) (rest (range)))]
    (into (sorted-map) (zipmap idx subcats))))

(defn sorted-category-map
  "Builds map idx -> category. Indices are 1, 2, 3, ...
  Category is a map with keys :name, :fns, :subcategories.
  See subcategory-map for format of subcategory."
  [fn-metas]
  (let [cats (fields-as-sorted-set fn-metas :category)
        idxs (rest (range))
        idxd-cats (zipmap idxs cats)]
    (into (sorted-map)
      (for [[idx cat] idxd-cats]
        [idx  {:name cat
               :fns (find-fns fn-metas cat nil)
               :subcategories (subcategory-map fn-metas cat idx)}]))))

(defn all-category-map
  "Build map idx -> (sub)category. Basically it is merge of category map and all subcategories maps."
  [fn-metas]
  (let [cat-map (sorted-category-map fn-metas)]
    (->> (vals cat-map)
         (map :subcategories)
         (apply merge cat-map))))

(defn- wrap-lines
  "Split a list of words in lists (lines) not longer than width chars each,
   space between words included."
  [width words]
  (letfn [(wrap-lines-rec
            [words accum-lns]
            (if (empty? words)
              accum-lns
              (let [lens (map #(inc (count %)) words)
                    cumlens (reductions + lens)
                    [line _] (split-with #(> width %) cumlens)
                    [line other] (split-at (count line) words)]
                (recur other (conj accum-lns line)))))]
    (wrap-lines-rec words [])))

(defn pprint-wrapped-lines
  "Pretty print words across several lines by wrapping lines at word boundary."
  [words & {:keys [fromcolumn width] :or {fromcolumn 0 width 80}}]
  (let [w (- width fromcolumn)
        wordss (wrap-lines w words)
        indent (apply str (repeat fromcolumn \space))
        lines (map #(apply str indent (interpose " " %)) wordss)]
    (doseq [l lines]
      (println l))))


;;; wiki generation

(defn- as-url [str]
  (cstr/replace str " " "-"))

(defn- as-filename [& path]
  (->> path (map as-url) (cstr/join "/")))

(defn- as-filename-ext [& path]
  (str (apply as-filename path) ".md"))

(defn- write-lines [file lines]
  (io/make-parents file)
  (spit file (cstr/join \newline lines)))

(defn- link
  ([to]
    (link to (as-filename to)))
  ([text to]
    (format "[[%s|%s]]" text to)))

(defn- generate-all-categories-page [fn-metas folder]
  (let [cats (sort (fields-as-sorted-set fn-metas :category))
        lines (map #(str "* " (link %)) cats)]
    (write-lines (as-filename-ext folder "all_categories") lines)))

(defn- generate-category-page [category folder]
  (let [{:keys [name fns subcategories]} category
        fn-line (fn [fn-name]
                  (str "* " (link fn-name (as-filename name fn-name))))
        subcat-lines (fn [subcat]
                       (concat [""
                                (str "## " (:name subcat))]
                               (map fn-line (:fns subcat))
                               []))
        lines (flatten (concat (map fn-line fns)
                               (map subcat-lines (vals subcategories))))]
    (write-lines (as-filename-ext folder name) lines)))

(defn- generate-function-page [fn-meta folder]
  (let [{:keys [name arglists subcategory added doc
                processing-name requires-bindings category]} fn-meta
        bold #(str "**" % "**")
        lines [(str "## " name)
               (bold "Arguments") (map #(str "`" (pr-str %) "`  ") arglists)
               (bold "Docstring") (str "```text\n" doc "\n```")
               (bold "Category") (link category)
               (if subcategory
                 [(bold "Subcategory") "" (format "[[%s|%s#%s]]"
                                                  subcategory
                                                  (as-filename category)
                                                  (as-filename subcategory))]
                 nil)
               (bold "Works only inside sketch functions?") (if requires-bindings "Yes" "No")
               (bold "Original Processing method") (if processing-name
                                                     (str "`" processing-name "`")
                                                     "None. It is present only in Quil.")
               (bold "Added in") added]]
     (write-lines (as-filename-ext folder category name)
                  (->> lines (remove nil?) (interpose "") flatten))))

(defn- generate-all [fn-metas folder]
  (generate-all-categories-page fn-metas folder)
  (let [category-map (sorted-category-map fn-metas)]
    (doseq [category (vals category-map)]
      (generate-category-page category folder)))
  (doseq [fn-meta (filter :category fn-metas)]
    (generate-function-page fn-meta folder)))

;(generate-all @(resolve 'quil.core/fn-metas) "../wiki")
