(ns quil.middlewares.safe-fns
  (:require [clojure.stacktrace :refer [print-cause-trace]]
            [quil.util :as u]))


(defn- wrap-fn [name function]
  (fn []
    (try
      (function)
      (catch Exception e
        (println "Exception in " name " function: " e "\nstacktrace: " (with-out-str (print-cause-trace e)))
        (Thread/sleep 1000)))))

(defn- wrap-mouse-wheel [function]
  (fn [rotation]
    (try
      (function rotation)
      (catch Exception e
        (println "Exception in :mouse-wheel function:" e "\nstacktrace: " (with-out-str (print-cause-trace e)))
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
          [name (if (u/callable? value)
                  ; :mouse-wheel is a special case as it takes single argument
                  ; while all other fns don't take any arguments
                  (if (= name :mouse-wheel)
                    (wrap-mouse-wheel value)
                    (wrap-fn name value))
                  value)])))
