(ns utils.docsite
  (:require [quil.helpers.docs :refer :all]
            quil.core
            [hiccup.core :as h]
            [hiccup.page :as p]
            [clojure.string :as cstr]
            [clojure.java.io :as io]))

;;; wiki generation

(defn- as-url [str]
  (cstr/replace str " " "-"))

(defn- as-filename [& path]
  (->> path (map as-url) (cstr/join "/")))

(defn- as-filename-ext [& path]
  (str (apply as-filename path) ".html"))

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
        content [:div
                 [:p [:strong "Arguments"]]
                 [:p (map #(vector :code (pr-str %)) arglists)]
                 [:p [:strong "Docstring"]]
                 [:p [:pre doc]]
                 [:p "Category"]
                 []
                 
                 ]
        lines [(str "## " name)
               

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
               (bold "Added in") added]
        content (p/html5 content)]
    (spit (as-filename-ext name) content)
))

(-> @(resolve 'quil.core/fn-metas) first (generate-function-page ""))



(defn- generate-all [fn-metas folder]
  (generate-all-categories-page fn-metas folder)
  (let [category-map (sorted-category-map fn-metas)]
    (doseq [category (vals category-map)]
      (generate-category-page category folder)))
  (doseq [fn-meta (filter :category fn-metas)]
    (generate-function-page fn-meta folder)))

;(generate-all @(resolve 'quil.core/fn-metas) "../wiki")

