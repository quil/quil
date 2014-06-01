(ns quil.middleware.safe-fns-test
  (:require [clojure.test :refer :all]
            [quil.middleware.safe-fns :refer [safe-fns]]))

(defn throw-exc []
  (throw (ex-info "Hey ho" {})))

(deftest safe-fns-test []
  (let [options {:no-exception (fn [] 1)
                 :exception-thrown (fn [] (throw (ex-info "Test" {})))
                 :exception-in-var #'throw-exc
                 :keyword :hello
                 :string "world"
                 :vector [1 2 #(throw (ex-info "Bla-bla" {}))]}
        safe-options (safe-fns options)]
    (doseq [fn (vals (safe-fns options))
            :when (or (var? fn) (fn? fn))]
      (with-out-str (fn)))))
