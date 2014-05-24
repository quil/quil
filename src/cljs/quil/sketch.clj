(ns quil.sketch)

(defmacro with-sketch [applet & body]
  `(binding [quil.sketch/*surface* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  `(defn ^:export ~app-name []
     (quil.sketch/make-processing ~@options)))
