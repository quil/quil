(ns quil.snippets.macro
  (:require [quil.util :as u]))

(defmacro defsnippet
  "Defines a snippet by storing its parts in a list
  quil.snippets.all-snippets/all-snippets that can be later
  access from clj or cljs to run tests or prepare samples.

  Params:
    name Name of snippet.
    fns Quil function name that the snippet tests or collection of names.
    opts Map of extra options. Supported options:
      :renderer Renderer to use for snippet, if not default.
      :setup Setup function to use. Default is empty.
    body Body of draw function of the snippet"
  [snip-name fns opts & body]
  (let [setup (:setup opts '())]
    `(swap! quil.snippets.all-snippets/all-snippets
            conj
            {:name (name '~snip-name)
             :fns ~(if (string? fns) [fns] fns)
             :opts ~(dissoc opts :setup)
             :setup (fn [] ~setup)
             :setup-str ~(pr-str setup)
             :body (fn [] ~@body)
             :body-str ~(pr-str body)
             :ns ~(str (ns-name *ns*))})))
