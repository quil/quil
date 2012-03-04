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
