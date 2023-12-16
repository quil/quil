# Testing

For non-snippet tests
```
$ clojure -X:test :excludes '[:clj-snippets :cljs-snippets]'
```

For manual CLJ tests
```
$ clojure -X:test :nses '[quil.manual]'
```

Snippet test environment variables that are useful:

`LOGTEST=true` will print the command to invoke the current snippet test before running the test.

`MANUAL=true` will keep a sketch open after saving the output, only executing the test case after exiting out of the sketch with escape or closing it.

`UPDATE_SCREENSHOTS=true` will copy the current snippet output to the reference image.

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
