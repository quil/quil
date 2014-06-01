(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*surface* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  `(do
     (defn ^:export ~app-name []
       (quil.sketch/make-processing ~@options))

     ~(when (not (false? (:start options)))
     	`(quil.sketch/add-sketch-to-init-list ~app-name))))
