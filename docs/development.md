# Development

## Developing a library referencing Quil

To use a local copy of Quil as a dependency for another library, use [git dep](https://clojure.org/reference/deps_edn#deps_git) or a [local root](https://clojure.org/reference/deps_edn#deps_local) instead of a maven jar dependency.

```clojure
{:deps {quil/quil {:git/url "https://github.com/quil/quil" + ;; + :git/sha or :git/tag
                   ;; OR :local/root "../path/to/quil"
                   }}}
```

However, quil has some code that requires ahead of time (AOT) compilation to interface with Processing. After adding a git or local root dependency for quil, run:

```bash
clj -X:deps prep
```

This will force the clojure CLI to AOT compile the interfaces to Processing. See the `deps.edn` documentation on [prep libs](https://clojure.org/guides/deps_and_cli#prep_libs) for more details.

