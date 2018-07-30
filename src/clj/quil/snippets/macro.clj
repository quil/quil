(ns quil.snippets.macro
  (:require [quil.util :as u]))

(def snippets-coll (atom '()))

(defmacro defsnippet
  "Defines a snippet by storing it in a list. Note that this macro
  doesn't generate any side effects and only stores snippet in
  snippets-coll list. Usages of snippets implemented separately.

  Params:
    name Name of snippet.
    fns Quil function name that the snippet tests or collection of names.
    opts Map of extra options. Supported options:
      :renderer Renderer to use for snippet, if not default.
      :setup Setup function to use. Default is empty.
    body Body of draw function of the snippet"
  [name fns opts & body]
    (println (boolean (:ns &env)))

  (swap! snippets-coll conj
         {:name name
          :fns (if (string? fns) [fns] fns)
          :opts opts
          :body body})
  nil)
