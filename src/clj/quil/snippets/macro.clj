(ns quil.snippets.macro
  (:require [quil.util :as u]))

(defmacro defsnippet
  "Defines a snippet. Snippet is a smalll example of how to use specific
  Quil function. Snippets used in Quil docs as well as they used for release
  testing.

  If snippet is intended for clj-only or cljs-only function - use reader conditionals
  (https://clojure.org/reference/reader#_reader_conditionals) to define snippet only in
  clj or cljs. Also you can use reader conditionals inside snippet itself if needed.

  If the snippet is not trivial and has multiple parts - it should have comments. These
  comments will help Quil users to understand better what snippet does when they read
  Quil API. Because standard clojure comments (the one that start with ;) are stripped in
  macros instead comments should be added using (comment) macro. Like the following:

  (comment \"Do foo bar\")
  (foo-bar 123)

  All (comment) forms will be converted into ';' comments when generating documentation.

  Snippets are stored in https://github.com/quil/quil/tree/master/src/cljc/quil/snippets

  More info about snippets: https://github.com/quil/quil/wiki/Snippets

  This macro works by storing its parts in a list quil.snippets.all-snippets/all-snippets
  that later used to generate tests for release testing or generate documentation.

  Params:
    name Name of snippet. Doesn't really matter, needs to be unique
        in the current file. Good default name is the same as function
        this snippets tests.
    fns Name of the Quil function name that the snippet tests. Used to
        to find all snippets for each function when generating documentation.
        Supports a collection of names if snippet tests few related Quil
        functions at the same time.
    opts Map of extra options. Supported options:
      :renderer Renderer to use for snippet, if not default.
      :setup Setup function to use. Default is empty.
    body Body of draw function of the snippet."
  [snip-name fns opts & body]
  (let [setup (:setup opts '())]
    `(swap! quil.snippets.all-snippets-internal/all-snippets
            conj
            {:name (name '~snip-name)
             :fns ~(if (string? fns) [fns] fns)
             :opts ~(dissoc opts :setup)
             :setup (fn [] ~setup)
             :setup-str ~(pr-str setup)
             :body (fn [] ~@body)
             :body-str ~(pr-str body)
             :ns ~(str (ns-name *ns*))})))
