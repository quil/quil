(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*applet* ~applet]
     ~@body))


(def ^{:private true} 
	supported-features
	#{:no-start})


(defmacro defsketch
  [app-name & options]
  (let [raw-opts (apply hash-map options)
        opts     (if (:host raw-opts) raw-opts
                     (merge raw-opts {:host app-name}))
        features (let [user-features (set (:features opts))]
                   (reduce #(assoc %1 %2 (contains? user-features %2)) {}
                           supported-features))]
    `(do
       (defn ^:export ~app-name []
         (quil.sketch/sketch 
          ~app-name ~@(apply concat (seq (merge (dissoc opts :features) features)))))
       
       ~(when (not (:no-start features))
          `(quil.sketch/add-sketch-to-init-list {:fn ~app-name :name (str '~app-name)})))))
