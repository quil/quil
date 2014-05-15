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


(defn bind-handler [prc processing-name handler]
  `(set!
    (~processing-name ~prc)
    (fn []
      (~'with-applet ~prc
        (~@(if (list? handler) handler (list handler)))))))


(defmacro bind-handlers [prc & opts]
  (map
   #(apply bind-handler (concat (list prc) %))
   (partition 2 opts)))
