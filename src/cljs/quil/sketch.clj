(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*applet* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  (let [raw-opts (apply hash-map options)
        opts     (if (:host raw-opts) raw-opts
                     (merge raw-opts {:host app-name}))
        features (set (:features opts))]
    `(do
       (defn ~(vary-meta app-name assoc :export true) []
         (quil.sketch/sketch
          ~@(apply concat (seq (merge (dissoc opts :features) features)))))

       ~(when (not (:no-start features))
          `(quil.sketch/add-sketch-to-init-list 
            {:fn ~app-name 
             :host-id (if (:host raw-opts) 
                          (:host raw-opts)
                          '~app-name)})))))
