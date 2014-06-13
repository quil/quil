(ns utils.docsite
  (:require [quil.helpers.docs :refer :all]
            quil.core
            [hiccup
             [core :as h]
             [page :as p]
             [element :as e]]
            garden.core
            [garden.units :refer [px]]
            [garden.arithmetic :as ga]
            [clojure.string :as string]
            [clojure.java.io :as io]))

(def fn-width (px 700))
(def gray-background "#FAFAFA")
(def column-width (px 200))

(def index-page-columns [["Color" "Data" "Environment"]
                         ["Image" "Transform" "Output" "Rendering" ]
                         ["Math" "Typography"]
                         ["Shape" "State" "Structure"]
                         ["Lights, Camera" "Input"]])

(def css
  (garden.core/css

   [[:body {:color "#333"}]
    [:a {:color "#333"}]
    [:p {:margin "0px"}]
    [:.category-div :#toc
     [:.category {:margin "0px 0px 7px 0px"}
      [:a {:color "#D53E07"}]]
     [:.subcategory {:margin "15px 0px 0px 0px"}
      [:a {:color "#009900"}]]
     [:.function {:margin-top "2px"}]]

    [:.index-page
     [:.wrapper {:margin "auto"
                 :width (ga/* (count index-page-columns) column-width)}]

     [:#title {:text-align "center"}]

     [:.column {:display "inline-block"
                :width column-width
                :float "left"}]
     [:.category-div {:background gray-background
                      :margin "10px"
                      :padding "10px"}]
     [:#description {:font-size "18px"
                     :text-align "center"}]]

    [:.fn-page
     [:.fns-wrapper {:width fn-width
                     :margin "auto"}

      [:h2 {:margin "10px 0px"}]

      [:div.function {:background gray-background
                      :margin "10px auto"
                      :padding "10px"}
       [:code {:margin-right "10px"}]
       [:h3 {:margin "2px 0px"}]]

      [:pre {:margin "0px"}]

      [:dt {:margin-top "10px"}]

      [:dl {:margin "0px"}]

      [:.subcategory {:margin "30px auto 0px auto"}]]

     [:#toc {:position "fixed"
             :width column-width
             :overflow-y "scroll"
             :bottom "20px"
             :top "0px"}]]]))

(defn- get-page
  "Converts hiccup-style body to full-blown page and returns it as string.
class is string that will be used as class of body element in the page."
  [class title body]
 (let [head [:head
             [:meta {:name "description"
                     :content (str "Quil is a clojure animation library for "
                                   "creating interactive sketches. Quil is based on Processing.")}]
             [:title title]
             [:style css]]]
   (p/html5 (list head [:body {:class class}
                        body]))))

(defn- as-url [str]
  (-> str
      (string/lower-case)
      (string/replace  #"[ &,]" {" " "-"
                                 "," ""
                                 "&" "and"})))

(defn- as-filename [& path]
  (->> path (map as-url) (string/join "/")))

(defn- as-filename-ext [& path]
  (str (apply as-filename path) ".html"))

(defn- link
  ([to]
    (link to to))
  ([text to]
     (link text to nil))
  ([text to anchor]
     (let [page (if to (as-filename-ext to) "")]
      (e/link-to (if anchor
                   (str page "#" (as-url anchor))
                   page)
                 text))))

(defn trim-docstring
  "Removes extra spaces in the begginning of dostringing lines."
  [dostringing]
  (->> (string/split dostringing #"\n")
       (map #(string/replace % #"^  " ""))
       (string/join "\n")))

(defn- function->html [fn-meta]
  (let [{:keys [name arglists subcategory added doc
                processing-name requires-bindings category]} fn-meta
        fields (array-map
                "Arguments" (map #(vector :code (pr-str %)) arglists)
                "Docstring" [:pre (trim-docstring doc)]
                "Works only inside sketch functions?" (if requires-bindings "Yes" "No")
                "Original Processing method" (if processing-name
                                               (if-let [link (link-to-processing-reference fn-meta)]
                                                 [:code (e/link-to link processing-name)]
                                                 [:code processing-name])
                                               "None. It is present only in Quil.")
                "Added in" added)]
    [:div.function {:id name}
     [:h3 name]
     [:dl (for [[key val] fields]
            (list [:dt key] [:dd val]))]]))

(defn- category->html [category fn-metas]
  (let [{:keys [name fns subcategories]} category
        subcat-id #(-> % :name (str "-subcategory") as-url)
        fn-metas (reduce #(assoc %1 (:name %2) %2) {} fn-metas)
        function->html (comp function->html fn-metas)
        subcat->html (fn [subcat]
                       (cons [:h3.subcategory
                              {:id (subcat-id subcat)}
                              (:name subcat)]
                             (map function->html (:fns subcat))))
        subcat->toc (fn [subcat]
                      (cons [:h4.subcategory (link (:name subcat) nil (subcat-id subcat))]
                            (map #(vector :p.function (link % nil %)) (:fns subcat))))]
    (list
     [:div#toc
      [:h3 (link "Back to index" "index")]
      [:h3.category (link name name)]
      (map #(vector :p.function (link % nil %)) fns)
      (map subcat->toc (vals subcategories))]

     [:div.fns-wrapper
      [:h2 name]
      (map function->html fns)
      (mapcat subcat->html (vals subcategories))])))

(defn generate-index-page [categories-map]
  (letfn [(fns->html [fns page]
            (map #(vector :p.function (link % page %)) fns))

          (subcat->html [subcat page]
            (cons [:h4.subcategory (link (:name subcat) page
                                         (str (:name subcat) "-subcategory"))]
                  (fns->html (:fns subcat) page)))

          (category->html [category]
            (let [{:keys [name fns subcategories]} category]
              [:div.category-div
               [:h3.category (link name)]
               (fns->html fns name)
               (map #(subcat->html % name) (vals subcategories))]))

          (find-category [name]
            (->> (vals categories-map)
                 (filter #(= (:name %) name))
                 (first)))

          (column->html [column]
            [:div.column (map (comp category->html find-category) column)])]
    [:div.wrapper
     [:h1#title "Quil 2.1.0 API"]
     [:div#description
      [:p "Quil is a clojure animation library for creating interactive sketches. "]
      [:p
       "Quil is based on " (e/link-to "http://processing.org" "Processing")
       ". Check " (e/link-to "https://github.com/quil/quil" "Github repo") "."]]
     (map column->html index-page-columns)]))

(defn- generate-all [fn-metas folder]
  (let [category-map (sorted-category-map fn-metas)]
    (doseq [category (vals category-map)]
      (let [html (get-page "fn-page" (str "Quil - " (:name category))
                           (category->html category fn-metas))
            file (as-filename-ext folder (:name category))]
        (spit file html)))
    (->> (generate-index-page category-map)
         (get-page "index-page" "Quil API")
         (spit (as-filename-ext folder "index")))))

(generate-all @(resolve 'quil.core/fn-metas) "out")


