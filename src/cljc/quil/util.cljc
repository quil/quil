(ns ^{:doc "Utility fns"}
 quil.util
  (:require [clojure.string :as cstr]))

(defn no-fn
  "Function that does nothing."
  [])

(def initial-internal-state
  "Internal state map used to initiate all sketches."
  {:frame-rate 60
   :looping? true})

#?(:clj
   (defn callable? [value]
     (or (fn? value)
         (var? value))))

#?(:clj
   (defn absolute-path [path]
     (-> (str path)
         (java.io.File.)
         (.getAbsolutePath))))

#?(:clj
   (defn int-like?
     [val]
     (let [t (type val)]
       (or (= java.lang.Long t)
           (= java.lang.Integer t)))))

(defn resolve-constant-key
  "Returns the val associated with key in mappings or key directly if it
  is one of the vals in mappings. Otherwise throws an exception."
  [key mappings]
  (cond
    (get mappings key)            (get mappings key)
    (some #{key} (vals mappings)) key

    :else                         (throw (#?(:clj Exception.
                                             :cljs js/Error.)
                                          (str "Expecting a keyword, got: " key ". Expected one of: " (vec (sort (keys mappings))))))))

(defn- length-of-longest-key
  "Returns the length of the longest key of map m. Assumes m's keys are strings
   and returns 0 if map is empty:
   (length-of-longest-key {\"foo\" 1 \"barr\" 2 \"bazzz\" 3}) ;=> 5
   (length-of-longest-key {}) ;=> 0"
  [m]
  (or (last (sort (map #(.length %) (keys m))))
      0))

(defn- gen-padding
  "Generates a padding string starting concatting s with len times pad:
   (gen-padding \"\" 5 \"b\") ;=> \"bbbbb\"
   May be called without starting string s in which case it defaults to the
   empty string and also without pad in which case it defaults to a single space"
  ([len] (gen-padding "" len " "))
  ([len pad] (gen-padding "" len pad))
  ([s len pad]
   (if (> len 0)
     (gen-padding (str s pad) (dec len) pad)
     s)))

(defn print-definition-list
  [definitions]
  (let [longest-key (length-of-longest-key definitions)]
    (dorun
     (map (fn [[k v]]
            (let [len (.length k)
                  diff (- longest-key len)
                  pad (gen-padding diff)]
              (println k pad "- " v)))
          definitions))))

(defn clj-compilation? []
  #?(:clj
     (not
      (boolean
       (when-let [n (find-ns 'cljs.analyzer)]
         (when-let [v (ns-resolve n '*cljs-file*)]
           @v))))
     :cljs false))

(defn prepare-quil-name [const-keyword]
  (cstr/replace
   (cstr/upper-case (name const-keyword))
   #"-" "_"))

(defn prepare-quil-clj-constants [constants]
  (into {}
        (map
         #(vector % (symbol (str "PConstants/" (prepare-quil-name %))))
         constants)))

(defn prepare-quil-cljs-constants [constants]
  (into {}
        (map
         #(vector % `(aget js/p5.prototype ~(prepare-quil-name %)))
         constants)))

(defn make-quil-constant-map [target const-map-name const-map]
  `(def ^{:private true}
     ~const-map-name
     ~(if (= target :clj)
        (prepare-quil-clj-constants const-map)
        (prepare-quil-cljs-constants const-map))))

(defmacro generate-quil-constants [target & opts]
  `(do
     ~@(map
        #(make-quil-constant-map target (first %) (second %))
        (partition 2 opts))))

(defn clj-unchecked-int
  "In clojure it does unchecked-int and does nothing in cljs.
  Needed to handle colors in hex form in clojure."
  [v]
  #?(:clj (unchecked-int v) :cljs v))
