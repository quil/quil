(ns quil.middlewares.bind-output
  (:require [quil.util :as u]))

(defn bind-output
  "Saves current `*out*` thread and before calling each
  handler binds local `*out*` to the saved one. This way
  all logs go to original output. When used from repl
  in emacs logs go to repl buffer instead of server
  buffer."
  [options]
  (let [original-out *out*]
    (into {}
          (for [[name value] options]
            [name (if (u/callable? value)
                    ; :mouse-wheel is a special case as it takes single argument
                    ; while all other fns don't take any arguments
                    (if (= name :mouse-wheel)
                      #(binding [*out* original-out]
                         (value %))
                      #(binding [*out* original-out]
                         (value)))
                    value)]))))
