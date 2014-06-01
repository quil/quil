(ns quil.middleware.safe-fns
  (:require [clojure.stacktrace :refer [print-cause-trace]]))


(defn- wrap-fn [name function]
  (fn []
    (try
      (function)
      (catch Exception e
        (println "Exception in " name " function: " e "\nstacktrace: " (with-out-str (print-cause-trace e)))
        (Thread/sleep 1000)))))

(defn safe-fns
  "Wraps all functions in options such that they will not throw exceptions
  If function (for example 'draw') throws an exception - exception is
  caught and printed to stdout and execution of the program is paused for
  1 sec. This way sketch will not be aborted but will continue to run,
  allowing to fix function and reload it on fly."
  [options]
  (into {}
        (for [[name value] options]
          [name (if (or (var? value) (fn? value))
                  (wrap-fn name value)
                  value)])))
