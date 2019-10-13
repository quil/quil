(ns ^{:doc "Class that contains listener methods for PApplet. Instance of this class is passed to PApplet.registerMethod()"}
 quil.helpers.applet-listener)

(gen-class
 :name quil.helpers.AppletListener
 :main false
 :init "init"
 :state "listeners"
 :constructors {[java.util.Map] []}
 :methods [["dispose" [] java.lang.Object]])

(defn safe-call [fn]
  (when fn (fn)))

(defn -init [listeners]
  [[] listeners])

(defn -dispose [this]
  (safe-call (:on-dispose (.listeners this))))
