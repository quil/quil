# Release

See also [release-process](https://github.com/quil/quil/wiki/Dev-notes#release-process).

## Check for `cljfmt` and `clj-kondo` warnings

Run `lein do check, cljfmt check` and verify that there are no reflection warnings and that everything is formatted.

TODO: automate and include clj-kondo

## Run automated tests locally

These are run in CI with github actions, but have frequent SEGV flakes, so it's useful to verify locally. See [testing](docs/testing.md) documentation. The minimum to run here is:

```
$ clojure -M:dev:kaocha unit clj-snippets # clj unit and snapshot tests
$ clojure -Mfig:cljs-test                 # cljs unit tests
$ clojure -M:dev:fig:kaocha cljs-snippets # cljs snapshot tests
```

## Run manual tests

These tests ensure that user input through mouse or keyboard and resizing the canvas all work correctly.

```
$ clojure -M:dev:kaocha --no-capture-output manual
```

It may make sense to re-run the "automated" tests here with multiple browsers, at the very least Chrome, Firefox, as well as the manual tests covering user input, and canvas resize.

```
$ clj -M:dev:fig:server -b dev -s
```

## Verify a snapshot JAR works with quil-examples

The release process creates an uberjar with JOGL and other dependencies bundled inside of it. However, in case the pom references JOGL deps from the upstream repository, or somehow the uberjar fails to bundle them into the jar, it's helpful to checkout `quil-examples` and change it to reference a snapshot uberjar of the current revision.

The following command will be build a local snapshot of the current revision and install that snapshot to the matching location in the maven `.m2` repository. This can also be accomplished with a snapshot deploy to clojars using the `.github/workflows/clojars_snapshot_release.yaml` process.

```
$ clj -T:build release :snapshot true
release: target/quil-4.3.1508-28b0120-SNAPSHOT.jar (16776.1 kb)
$ clj -T:build deploy :clojars false :snapshot true
Installing quil/quil-4.3.1508-28b0120-SNAPSHOT to your local `.m2`
done.
```

Then checkout a copy of [quil-examples](https://github.com/quil/quil-examples), and change the `deps.edn` coordinates for quil to reference the snapshot version created above. Ie:

```
quil/quil {:mvn/version "4.3.1508-28b0120-SNAPSHOT"}
```

Then run a few example sketches, particularly ones like:

```
# uses opengl renderer
clj -M -m quil-sketches.gen-art.26-sphere
# uses p3d renderer
clj -M -m quil-sketches.gen-art.28-cloud-cube
```

As they use bindings that verify that JOGL is bundled correctly.
