(ns cljs.quil.helpers.tools)

(defmacro with-applet [applet & body]
  `(binding [cljs.quil.applet/*surface* ~applet]
     ~@body))


(defmacro defapplet
  "Define and start an applet and bind it to a var with the symbol
  app-name. If any of the options to the various callbacks are
  symbols, it wraps them in a call to var to ensure they aren't
  inlined and that redefinitions to the original fns are reflected in
  the visualisation. See applet for the available options."
  [app-name & options]
    `(defn ^:export ~app-name []
       (cljs.quil.applet/make-processing ~@options)))
