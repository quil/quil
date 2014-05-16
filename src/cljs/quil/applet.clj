(ns cljs.quil.applet)

(defmacro with-applet [applet & body]
  `(binding [cljs.quil.applet/*surface* ~applet]
     ~@body))


(defmacro defsketch
  [app-name & options]
  `(defn ^:export ~app-name []
     (cljs.quil.applet/make-processing ~@options)))
