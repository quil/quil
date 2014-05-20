(ns cljs.quil.helpers.util)


(defn resolve-constant-key
  "Returns the val associated with key in mappings or key directly if it
  is one of the vals in mappings. Otherwise throws an exception."
  [key mappings]
  (cond
    (get mappings key)            (get mappings key)
    (some #{key} (vals mappings)) key
    :else                         nil))
