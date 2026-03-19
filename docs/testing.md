# Testing

There are a couple different testing approaches for quil. There are a few combined cljc tests and unit tests. Those are run using either kaocha or test-runner, or using figwheel test in the browser. These are mainly used for calculation tests where a value output is useful instead of comparing image snapshots. Snippet tests make up the majority of the test suite. For both CLJ and CLJS snippet tests, a snapshot of a snippet is compared against the expected images in `dev-resources/snippet-snapshots`. The folders correspond to different environments like retina or 1:1 pixel density, or CLJ/CLJS suites. The CLJ tests use `q/save` to save the current frame of a snippet for comparison, and the CLJS tests use etaoin to script a browser and take snapshots of the canvas after running a particular snippet. Finally, there are a few manual test cases for both CLJ and CLJS that check input behavior or fullscreen views.

**Caution:** In CI there are still some non-deterministic failures where Java encounters a SEGV while executing the snippet tests. The test suite is otherwise reasonably stable though some of the image tests are occasionally flaky from asynchronously loading an image from a remote source.

## Prerequisites

Before running the full suite locally:

* Run `clojure -T:build aot`
* Install ImageMagick for image comparison
* Install `chromedriver` for CLJS browser snapshot tests
* On Linux CI-style runs, use `xvfb-run` for browser and graphics tests

Kaocha is the primary test runner for local work and CI. Prefer it over
`clj -X:test` unless you specifically want the test-runner entrypoint.

## Using Kaocha

```bash
$ clojure -M:dev:kaocha [unit|manual|clj-snippets|cljs-snippets]
```

See `tests.edn` for the different suites. Multiple test suites can be run like so:

```bash
$ clojure -M:dev:kaocha unit clj-snippets
```

For CLJ Manual tests:
```bash
$ clojure -M:dev:kaocha --no-capture-output manual
```

For CLJS Manual tests:
```bash
$ clj -M:dev:fig:server -b dev -s
```

For CLJS snippets:
```bash
$ clojure -M:dev:fig:kaocha cljs-snippets
```

## CLJS Browser Unit Tests

The last type of test runner is for executing unit tests in CLJC files in a browser context.

```bash
$ clj -M:fig:cljs-test
```

These tests are run using the Figwheel test runner. The included tests are all referenced in the require list for the `test/quil/test_runner.cljs`. These tests make specific assertions about the output value, as they do in the CLJ unit tests, and do not rely on image snapshot comparisons.

## Automated Image Testing aka Snippet Tests

Quil has tests that run each snippets, takes a screenshot and compares with expected output image. The expected output is stored in the repo under the `dev-resources/snippet-snapshots` directory. It supports both clj and cljs. During test run a screenshot of the sketch is taken and compared with reference image. If difference exceeds threshold then snippet considered failed and `snippet-name-difference.png` is stored in the folder where screenshots are kept.

Ensure that `imagemagick` and `chromedriver` are installed before running
tests. ImageMagick is required for both CLJ and CLJS snapshot comparison. The
CLJS browser snapshot tests currently use `chromedriver`. `geckodriver` is
still checked by the test fixture today, but is not the browser used in CI.

If the diff exceeds threshold the image comparison will print to console names of failed tests. An example snapshot difference failure would look like:

```bash
$ clj -M:dev:kaocha clj-snippets --focus :quil.snippets.snapshot-test/quil_snippets_rendering_with-graphics
[(.F)]
Randomized with --seed 950716166

FAIL in quil.snippets.snapshot-test/quil_snippets_rendering_with-graphics (test_helper.clj:73)
Image differences in "with-graphics", see: dev-resources/snippet-snapshots/clj/normal/with-graphics-difference.png
dev-resources/snippet-snapshots/clj/normal/with-graphics-actual.png PNG 500x500 500x500+0+0 8-bit sRGB 6004B 0.000u 0:00.000
dev-resources/snippet-snapshots/clj/normal/with-graphics-expected.png PNG 500x500 500x500+0+0 8-bit sRGB 4865B 0.000u 0:00.000

expected: (<= result threshold)
  actual: (not (<= 0.0452919 0.03))
1 tests, 2 assertions, 1 failures.

bin/kaocha --focus 'quil.snippets.snapshot-test/quil_snippets_rendering_with-graphics'
```

The last line shows how to re-run just that test (possibly with the `MANUAL=true` environment variable to interact with the sketch while it's running). The "Image differences" line reports the diff image to inspect to see why it failed. The next two lines are output from identify which helps verify the sizes are equivalent. Finally we see can the see the threshold for comparison that was exceeded.

This can be adjusted on a per snippet basis using the `:accepted-diff-threshold` key. Snippets can also skip the snapshot test entirely by setting `:skip-image-diff?` to true. Image diffs can be unreliable so feel free to either increase the threshold or skip the test entirely if it's not deterministic, or hard to keep it deterministic across different machines or architectures. Finally, the `UPDATE_SCREENSHOTS=true` variable can be used to update the expected image for an existing or new snippet.

To see all differences from failed snippets try:

```bash
$IMAGEVIEWER path/to/screenshots/folder/*difference*
```

where `$IMAGEVIEWER` is your image viewer. For example on ubuntu it can be eog.

## Test Environment Variables

Snippet test environment variables that are useful:

`LOGTEST=true` will print the test-runner command to invoke the current snippet test before running the snippet.

`MANUAL=true` will keep a sketch open after saving the output, only executing the test case after exiting out of the sketch with escape or closing it.

`UPDATE_SCREENSHOTS=true` will update the reference image to the current snippet output for every snippet executed in the suite.

As example:
```bash
UPDATE_SCREENSHOTS=true clj -M:dev:kaocha clj-snippets --focus :quil.snippets.snapshot-test/quil_snippets_rendering_with-graphics
```
Would update `with-graphics-expected.png` with the current output from that snippet. For the CLJS tests it's a little trickier as they are all running in one single deftest for now. Recommend filtering the list of [`(snippet-elements)`](https://github.com/quil/quil/blob/master/test/quil/snippets/browser_snapshot_test.clj) to the subset to update.

These flags will work on both Kaocha and test-runner suites.

## Addendum: Using Test-Runner

**Warning:** This path is secondary. It is more fragile than the Kaocha
workflow and is not the main path used in CI.

For non-snippet tests
```bash
$ clojure -X:test :excludes '[:clj-snippets :cljs-snippets]'
```

For manual CLJ tests
```bash
$ clojure -X:test :nses '[quil.manual]'
```

For CLJ snippets:

```bash
$ clojure -X:test :includes '[:clj-snippets]'
```

`clj -X:test` is currently not the recommended path for CLJS snippet tests.
Those tests rely on additional browser and Figwheel setup, and the supported
workflow is:

```bash
$ clojure -M:dev:fig:kaocha cljs-snippets
```

See also https://github.com/quil/quil/wiki/Dev-notes#automated-image-tests
