(ns ^{:doc "Functions for filtering/displaying docs in REPL and generating wiki pages."}
 quil.helpers.docs
  (:require [clojure.string :as cstr]
            [clojure.java.io :as io]))

(defn link-to-processing-reference
  "Returns a link to the Processing page of the function.
  If `fn-meta` has `:processing-link` key, value will be returned,
  otherwise method will try to build the link based on `:processing-name`.
  If `:processing-name` is nil, returns nil."
  [fn-meta]
  (cond (contains? fn-meta :processing-link)
        (:processing-link fn-meta)

        (:processing-name fn-meta)
        (let [name (-> (:processing-name fn-meta)
                       (cstr/replace "()" "_")
                       (cstr/replace "." "_")
                       (cstr/replace "[]" ""))]
          (str "https://processing.org/reference/" name ".html"))

        :else
        nil))

(defn link-to-p5js-reference
  "Returns a link to the p5js page of the function based on `:p5js-name`
  in the `fn-meta` metadata. If `:p5-name` is nil, returns nil."
  [fn-meta]
  (when-let [name (:p5js-name fn-meta)]
    (cond (cstr/includes? name ".")
          (str "https://p5js.org/reference/#/p5." (-> name
                                                      (cstr/replace "." "/")
                                                      (cstr/replace "()" "")))

          (cstr/includes? name "()")
          (str "https://p5js.org/reference/#/p5/" (cstr/replace name "()" ""))

          :else
          (str "https://p5js.org/reference/#/p5/" name))))

(defn- fn-metas-with-orig-method-name
  "Returns a seq of metadata maps for all fns with a corresponding
  Processing or p5js API method."
  [fn-metas]
  (filter #{:processing-name :p5js-name} fn-metas))

(defn matching-processing-methods
  "Takes a string representing the start of a method name in the
  original Processing API and returns a map of orig/new-name pairs"
  [fn-metas orig-name]
  (let [metas   (fn-metas-with-orig-method-name fn-metas)
        matches (filter #(.startsWith (str (:processing-name %)) orig-name) metas)]
    (into {} (map (fn [{:keys [name processing-name]}]
                    [(str processing-name) (str name)])
                  matches))))

(defn fields-as-sorted-set
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
        idxs (map #(format "%2d" %) (rest (range)))
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
