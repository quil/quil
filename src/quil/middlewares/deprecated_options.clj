(ns quil.middlewares.deprecated-options)

(def ^:private deprecated
  {:decor ["2.0" "Try :features [:present] for similar effect"]
   :target ["2.0" "Use :features [:keep-on-top] instead."]
   :safe-draw-fn ["2.0" "Use :features [:no-safe-fns] instead."]})

(defn- check-features-vector [features]
  (let [features (set features)]
    (when (features :no-safe-draw)
      (println "Feature :no-safe-draw was renamed to :no-safe-fns in Quil 2.1."
               "Use :feature [:no-safe-fns] now."))
    (disj features :no-safe-draw)))

(defn deprecated-options
  "Checks if options map contains deprected options and removes them.
  Prints messages how to fix deprecated functions."
  [options]
  (let [options (update-in options [:features] check-features-vector)]
    (->> (for [[name value] options]
           (if-let [[version message] (deprecated name)]
             (do (println name "option was removed in Quil" version "." message)
                 nil)
             [name value]))
         (remove nil?)
         (into {}))))
