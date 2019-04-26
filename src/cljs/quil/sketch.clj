(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*applet* ~applet]
     ~@body))

(defn wrap-fns
  "[[wrap-fns]] allows dynamic redefinition of a function such as `draw` and
  `update` in cljs. This is achieved by wrapping all provided functions to
  anonymous functions such that `my-draw` function turns into
  `(fn [& args] (apply my-draw args))`. This adds a level of indirection
  so that when quil calls `draw`, it invokes anonymous function which in
  turn always calls `my-draw` by name and if you redefine, the new version
  will be used. Hence we need this cryptic macro."
  [opts]
  (into {}
        (for [[k v] opts]
          (if (symbol? v)
            [k `(if (fn? ~v) (fn [& ~'args] (apply ~v ~'args)) ~v)]
            [k v]))))

(defmacro defsketch
  [app-name & options]
  (let [raw-opts (apply hash-map options)
        opts     (->> raw-opts
                      (merge {:host (str app-name)})
                      wrap-fns)]
    `(do
       (defn ~(vary-meta app-name assoc :export true) []
         (quil.sketch/sketch
          ~@(apply concat (seq opts))))

       (when-not (some #(= :no-start %) ~(:features opts))
         (quil.sketch/add-sketch-to-init-list
          {:fn ~app-name
           :host-id ~(:host opts)})))))
