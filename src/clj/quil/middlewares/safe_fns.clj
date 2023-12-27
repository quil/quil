(ns quil.middlewares.safe-fns
  (:require [clojure.stacktrace :refer [print-cause-trace]]
            [quil.util :as u]))

(defn- wrap-fn [name function delay-ms]
  (fn []
    (try
      (function)
      (catch Exception e
        (println "Exception in " name " function: " e "\nstacktrace: " (with-out-str (print-cause-trace e)))
        (Thread/sleep delay-ms)))))

(defn- wrap-mouse-wheel [function delay-ms]
  (fn [rotation]
    (try
      (function rotation)
      (catch Exception e
        (println "Exception in :mouse-wheel function:" e "\nstacktrace: " (with-out-str (print-cause-trace e)))
        (Thread/sleep delay-ms)))))

(defn safe-fns
  "Wraps all functions in `options` such that they will not throw exceptions.
  If a function (for example `draw`) throws an exception, the exception is
  caught, printed to `stdout` and execution of the program is paused for
  `delay-ms`. The sketch will not be aborted, it will continue to run,
  making it possible to fix the function and reload it on the fly."
  [options delay-ms]
  (into {}
        (for [[name value] options]
          [name (if (u/callable? value)
                  ;; :mouse-wheel is a special case as it takes single argument
                  ;; while all other fns don't take any arguments
                  (if (= name :mouse-wheel)
                    (wrap-mouse-wheel value delay-ms)
                    (wrap-fn name value delay-ms))
                  value)])))
