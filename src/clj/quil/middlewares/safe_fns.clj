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
  "Wraps all functions in `options` such that they will not throw exceptions.
  If a function (for example `draw`) throws an exception, the exception is
  caught, printed to `stdout` and execution of the program is paused for
  1 sec. The sketch will not be aborted, it will continue to run,
  making it possible to fix the function and reload it on the fly."
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
