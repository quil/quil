(ns cljs.quil.helpers.tools)

(defmacro with-applet [applet & body]
  `(binding [cljs.quil.applet/*surface* ~applet]
     ~@body))


(def ^{:private true} fn-applet-params
  #{:draw})


(defmacro defapplet
  [app-name & options]
  `(defn ^:export ~app-name []
     (cljs.quil.applet/make-processing ~@options)))
