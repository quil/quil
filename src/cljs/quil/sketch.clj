(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*applet* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  (let [raw-opts (apply hash-map options)
        opts     (if (:host raw-opts) raw-opts
                     (merge raw-opts {:host (str app-name)}))]
    `(do
       (defn ~(vary-meta app-name assoc :export true) []
         (quil.sketch/sketch
          ~@(apply concat (seq opts))))

       (when-not (some #(= :no-start %) ~(:features opts))
          (quil.sketch/add-sketch-to-init-list 
            {:fn ~app-name 
             :host-id (:host opts)})))))
