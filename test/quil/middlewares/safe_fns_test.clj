(ns quil.middlewares.safe-fns-test
  (:require [clojure.test :refer :all]
            [quil.middlewares.safe-fns :refer [safe-fns]]))

(defn throw-exc []
  (throw (ex-info "Hey ho" {})))

(deftest safe-fns-test []
  (let [options {:no-exception (fn [] 1)
                 :exception-thrown (fn [] (throw (ex-info "Test" {})))
                 :exception-in-var #'throw-exc
                 :keyword :hello
                 :string "world"
                 :vector [1 2 #(throw (ex-info "Bla-bla" {}))]

                 ; mouse-wheel is a special case as it takes single
                 ; argument while all other fns don'ttake any arguments
                 :mouse-wheel (fn [value] (throw (ex-info "Test" {})))}
        safe-options (safe-fns options)]
    (doseq [[name fn] (safe-fns options)
            :when (or (var? fn) (fn? fn))]
      (with-out-str
        (if (= name :mouse-wheel)
          (fn 123)
          (fn))))))
