(ns processing.util)

(defn int-like?
  [val]
  (let [t (type val)]
    (or (= java.lang.Long t)
        (= java.lang.Integer t))))

(defn resolve-constant-key
  [key mappings]
  (when-not (keyword? key)
    (throw (Exception. (str "Expecting a keyword, got: " key))))

  (if-let [const (get mappings key)]
    const
    (throw (Exception. (str "Mode constant not found matching key: " key ". Expected one of: " (vec (sort (keys mappings))))))))


(defn length-of-longest-key
  "Returns the length of the longest key of map m. Assumes m's keys are strings
   and returns 0 if map is empty:
   (length-of-longest-key {\"foo\" 1 \"barr\" 2 \"bazzz\" 3}) ;=> 5
   (length-of-longest-key {}) ;=> 0"
  [m]
  (or (last (sort (map #(.length %) (keys m))))
      0))

(defn gen-padding
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
