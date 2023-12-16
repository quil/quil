# Testing

For non-snippet tests
```
$ clojure -X:test :excludes '[:clj-snippets :cljs-snippets]'
```

For manual CLJ tests
```
$ clojure -X:test :nses '[quil.manual]'
```

For CLJ snippets:
```
$ clojure -X:test :includes '[:clj-snippets]'
```

For CLJS snippets:
```
$ lein with-profile cljs-testing do cljsbuild once tests, ring server
$ clojure -X:test :includes '[:cljs-snippets]'
```

See also https://github.com/quil/quil/wiki/Dev-notes#automated-image-tests
