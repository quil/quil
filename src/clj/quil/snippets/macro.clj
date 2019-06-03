(ns quil.snippets.macro
  (:require [quil.util :as u]))

(defmacro defsnippet
  "Defines a snippet. A snippet is a small example showing how to use a specific
  Quil function. Snippets are used in Quil docs and for release testing.

  If a snippet is intended for a clj-only or cljs-only function, use
  [reader
  conditionals](https://clojure.org/reference/reader#_reader_conditionals)
  to define the snippet only in clj or cljs. You can also use reader
  conditionals inside the snippet itself if needed.

  If the snippet is not trivial and has multiple parts it should have
  comments. These comments will help Quil users to better understand what the
  snippet does when they read the Quil API. Since standard Clojure comments (the
  ones that start with `;`) are stripped in macros, comments should be added
  using the `(comment)` macro like this:

  ```
  (comment \"Do foo bar\")
  (foo-bar 123)
  ```

  All `(comment)` forms will be converted into `;` comments when generating the
  documentation.

  Snippets are stored in
  https://github.com/quil/quil/tree/master/src/cljc/quil/snippets

  More info about snippets: https://github.com/quil/quil/wiki/Snippets

  This macro works by storing its parts in a
  list [[quil.snippets.all-snippets/all-snippets]] that is later used to
  generate tests for release testing or generate documentation.

  Parameters:
    * `name` - Name of snippet. Usually the same as the function
               the snippet tests. Needs to be unique in the current file.
    * `fns`  - Name of the Quil function that the snippet tests. Used to
               to find all snippets for each function when generating
               documentation. Supports a collection of names if the
               snippet tests a few related Quil functions at the same time.
    * `opts` - Map of extra options. Supported options:
      - `:renderer` - Renderer to use for the snippet, if not default.
      - `:setup`    - Setup function to use. Default is empty.
      - `:mouse-clicked` - Function to use as :mouse-clicked. Default is empty.
      - `:skip-image-diff?` - If set to true then snippets won't be used
                              in automated image diff tests.
    * `body` - The body of the `draw` function of the snippet."
  [snip-name fns opts & body]
  (let [setup (:setup opts '())
        mouse-clicked (:mouse-clicked opts '())]
    `(swap! quil.snippets.all-snippets-internal/all-snippets
            conj
            {:name (name '~snip-name)
             :fns ~(if (string? fns) [fns] fns)
             :opts ~(dissoc opts :setup :mouse-clicked :skip-image-diff?)
             :setup (fn [] ~setup)
             :setup-str ~(pr-str setup)
             :body (fn [] ~@body)
             :body-str ~(pr-str body)
             :mouse-clicked (fn [] ~mouse-clicked)
             :mouse-clicked-str ~(pr-str mouse-clicked)
             :ns ~(str (ns-name *ns*))
             :skip-image-diff? ~(:skip-image-diff? opts false)})))
