(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*applet* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  (let [raw-opts (apply hash-map options)
        opts     (merge {:host (str app-name)}
                        raw-opts)]
    `(do
       (defn ~(vary-meta app-name assoc :export true) []
         (quil.sketch/sketch
          ~@(apply concat (seq opts))))

       (when-not (some #(= :no-start %) ~(:features opts))
          (quil.sketch/add-sketch-to-init-list
            {:fn ~app-name
             :host-id ~(:host opts)})))))
