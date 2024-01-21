# Release

See also [release-process](https://github.com/quil/quil/wiki/Dev-notes#release-process).

## Verification

### Check for `cljfmt` and `clj-kondo` warnings

Run `lein do check, cljfmt check` and verify that there are no reflection warnings and that everything is formatted.

TODO: automate and include clj-kondo, and switch to `deps.edn` aliases

### Run automated tests locally

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

### Verify a snapshot JAR works with quil-examples

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

## Release Steps

1. Create a new branch for release
1. Update `RELEASE-NOTES.md`, to reflect all the changes which went into the current release (including this PR!)
2. (optional) Consider doing a [snapshot release](https://github.com/quil/quil/actions/workflows/clojars_snapshot_release.yaml), selecting "Run workflow" from branch "master". This will push a snapshot jar to Clojars that can be tested with other projects.
3. Calculate the build version, which incorporates the number of commits to the `master` branch. Either read it from the github UI showing the latest commit, or use `git rev-list master --count` locally. Remember that since there are locally committed changes this number will likely be higher once the commits are pushed to github or the PR is merged, so adjust accordingly. The release should be in form of `v4.3.1234` where `4.3` tracks the upstream processing release version, and `1234` is the build number just calculated.
4. Update Quil version in the `README.md`, for `deps.edn` and Leiningen coordinates, and any other references. Update version in `project.clj`.
5. Push, review, and merge the release PR, making sure the version matches the build count of the merged PR.
6. [tag a release](https://github.com/quil/quil/releases/new) by creating a new tag matching the version above, ie `v4.3.1234`. Select the previous release and use generate release notes and adjust that text for the release. Leave `set as the latest release` checked.
7. Click `Publish release`. The [release action](https://github.com/quil/quil/actions/workflows/clojars_release.yaml) is configured to upload a JAR to Clojars whenever a tag is created starting with `v`. This will upload a jar to Clojars versioned as the release version ie `v4.3.1234`.
8. Monitor the [release action](https://github.com/quil/quil/actions/workflows/clojars_release.yaml) to ensure it completes correctly.
9. Update the lein and deps-new templates to reference the new Clojars release

   * https://github.com/quil/quil-templates (requires separate release for clj template)
   * https://github.com/quil/sketchbook-template
   * https://github.com/quil/clj-sketch-template

10. Update external references to the release version

   * https://github.com/quil/quil-examples
   * https://github.com/quil/quil/wiki/Runnable-jar

11. Update quil.info website (optional)
12. Announce the Quil release on [Clojureverse](https://clojureverse.org/), [r/clojure](https://www.reddit.com/r/Clojure/), and the Clojurians slack (both in #announcements and in #quil). Previously this also included clj-processing and clojure google groups.

### Announcement Template

```
Subject:
[ANN] Quil $VERSION Release
Body:
Happy to announce Quil v4.3.123 release.
Quil is a Clojure/ClojureScript library for creating interactive drawings and animations.

The release available on clojars: https://clojars.org/quil. List of changes:

Change 1
Change 2
Documentation on http://quil.info has been updated as well.

Happy hacking!
$YOUR_NAME
```
