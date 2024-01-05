# Testing

There are a couple different testing approaches for quil. There are a few combined cljc tests and unit tests. Those are run using either kaocha or test-runner, or using figwheel test in the browser. These are mainly used for calculation tests where a value output is useful instead of comparing image snapshots. Snippet tests make up the majority of the test suite. For both CLJ and CLJS snippet tests, a snapshot of a snippet is compared against the expected images in `dev-resources/snippet-snapshots`. The folders correspond to different environments like retina or 1:1 pixel density, or CLJ/CLJS suites. The CLJ tests use `q/save` to save the current frame of a snippet for comparison, and the CLJS tests use etaoin to script a browser and take snapshots of the canvas after running a particular snippet. Finally, there are a few manual test cases for both CLJ and CLJS that check input behavior or fullscreen views.

**Caution:** In CI there are still some non-deterministic failures where Java encounters a SEGV while executing the snippet tests. The test suite is otherwise reasonably stable though some of the image tests are occasionally flaky from asynchronously loading an image from a remote source.

## Using Kaocha

```
$ clojure -M:dev:kaocha [unit|clj-snippets|cljs-snippets]
```

See `tests.edn` for the different suites. Multiple test suites can be run like so:

```
$ clojure -M:dev:kaocha unit clj-snippets
```

For CLJS Manual tests:
```
$ clj -M:dev:fig:server -b dev -s
```

For CLJS snippets:
```
$ clojure -M:dev:fig:kaocha cljs-snippets
```

## Using Test-Runner

**Warning:** Often hangs after completing all tests, so CI uses kaocha. However there is more documentation on how to focus on running an individual test with this test-runner, and for running the manual tests, so keeping it for now. 

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
# Optionally start a web server to run the snippets with:
$ lein with-profile cljs-testing do cljsbuild once tests, ring server
# OR
$ clj -M:dev:fig:server -b dev -s
# If the server is not started externally, cljs-snippets has a fixture which provides a server automatically.

# Run the test suite
$ clojure -X:test :includes '[:cljs-snippets]'
```

## Automated Image Testing aka Snippet Tests

Quil has tests that run each snippets, takes a screenshot and compares with expected output image. The expected output is stored in the repo under the `dev-resources/snippet-snapshots` directory. It supports both clj and cljs. During test run a screenshot of the sketch is taken and compared with reference image. If difference exceeds threshold then snippet considered failed and `snippet-name-difference.png` is stored in the folder where screenshots are kept.

Ensure that `imagemagick`, `geckodriver`, and `chromedriver` are installed before running tests. Imagemagick is required for both clj and cljs while `geckodriver` or `chromedriver` is only needed for cljs. Currently CLJS tests are using `chromedriver` to account for a screenshot size issue with `geckodriver`.

To see all differences from failed snippets try:

```
$IMAGEVIEWER path/to/screenshots/folder/*difference*
```

where `$IMAGEVIEWER` is your image viewer. For example on ubuntu it can be eog.

## Test Environment Variables

Snippet test environment variables that are useful:

`LOGTEST=true` will print the command to invoke the current snippet test before running the test.

`MANUAL=true` will keep a sketch open after saving the output, only executing the test case after exiting out of the sketch with escape or closing it.

`UPDATE_SCREENSHOTS=true` will copy the current snippet output to the reference image.

See also https://github.com/quil/quil/wiki/Dev-notes#automated-image-tests

These flags will work on both Kaocha and test-runner suites.

## CLJS Browser Unit Tests

The last type of test runner is for executing unit tests in CLJC files in a browser context. 

```
$ clj -Mfig:cljs-test
```

These tests are run using the Figwheel test runner. The included tests are all
referenced in the require list for the `test/quil/test_runner.cljs`.
