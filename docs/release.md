# Release

Process notes and explanation on how to release a new version of Quil using the automated tooling.

1. Verify the build is ready
2. Release!

Adapted from the [release-process](https://github.com/quil/quil/wiki/Dev-notes#release-process) notes.

## Verification

### Check for `cljfmt` and `clj-kondo` warnings

Run `bin/lint` and verify that there are no reflection warnings and that everything is formatted.

This is automated with github actions, however, for now the `clj-kondo` linting
is only enabled on tests as it has too many failures in `src`.

### Run automated tests locally

These are run in CI with github actions, but have frequent SEGV flakes, so it's useful to verify locally. See [testing](testing.md) documentation. The minimum to run here is:

```
# clj unit and snapshot tests
$ clojure -M:dev:kaocha unit clj-snippets
# cljs unit tests
$ clojure -Mfig:cljs-test
# cljs snapshot tests
$ clojure -M:dev:fig:kaocha cljs-snippets
```

## Run manual tests

These tests ensure that user input through mouse or keyboard and resizing the canvas all work correctly.

```
$ clojure -M:dev:kaocha --no-capture-output manual
```

It may make sense to re-run the "automated" tests here with multiple browsers, at the very least Chrome, Firefox, as well as the manual tests covering user input, and canvas resize.

```
$ clojure -M:dev:fig:server -b dev -s
```

### Verify a snapshot JAR works with quil-examples

The release process creates an uberjar with JOGL and other dependencies bundled inside of it. However, in case the pom references JOGL deps from the upstream repository, or somehow the uberjar fails to bundle them into the jar, it's helpful to checkout `quil-examples` and change it to reference a snapshot uberjar of the current revision.

The following command will be build a local snapshot of the current revision and install that snapshot to the matching location in the maven `.m2` repository. This can also be accomplished with a snapshot deploy to clojars using the `.github/workflows/clojars_snapshot_release.yaml` process.

```
$ clojure -T:build release :snapshot true
release: target/quil-4.3.1508-28b0120-SNAPSHOT.jar (16776.1 kb)
$ clojure -T:build deploy :clojars false :snapshot true
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
clojure -M -m quil-sketches.gen-art.26-sphere
# uses p3d renderer
clojure -M -m quil-sketches.gen-art.28-cloud-cube
```

As they use bindings that verify that JOGL is bundled correctly.

## Release Steps

1. Create a new branch for release
2. Run all the manual verification steps above
3. Update `RELEASE-NOTES.md`, to reflect all the changes which went into the current release (including this PR!)
4. (optional) Consider doing a [snapshot release](https://github.com/quil/quil/actions/workflows/clojars_snapshot_release.yaml), selecting "Run workflow" from branch "master". This will push a snapshot jar to Clojars that can be tested with other projects.
5. Calculate the build version, which incorporates the number of commits to the `master` branch. Either read it from the github UI showing the latest commit, or use `git rev-list master --count` locally. Remember that since there are locally committed changes this number will likely be higher once the commits are pushed to github or the PR is merged, so adjust accordingly. The release should be in form of `v4.3.1234` where `4.3` tracks the upstream processing release version, and `1234` is the build number just calculated. This can also be calculated using: 

```
$ clojure -T:build release-version :print true
Version: 4.3.1234
```

If the release includes a changed upstream version for processing, adjust the major/minor component of the `release-version` command in `build.clj` to reflect the upstream version.

6. Update Quil version in the `README.md`, for `deps.edn` and Leiningen coordinates, and any other references. Update version in `project.clj`.
7. Push, review, and merge the release PR, making sure the version matches the build count of the merged PR.
8. [tag a release](https://github.com/quil/quil/releases/new) by creating a new tag matching the version above, ie `v4.3.1234`. Select the previous release and use generate release notes and adjust that text for the release. Leave `set as the latest release` checked.
9. Click `Publish release`. The [release action](https://github.com/quil/quil/actions/workflows/clojars_release.yaml) is configured to upload a JAR to Clojars whenever a tag is created starting with `v`. This will upload a jar to Clojars versioned as the release version ie `v4.3.1234`.
10. Monitor the [release action](https://github.com/quil/quil/actions/workflows/clojars_release.yaml) to ensure it completes correctly.
11. Update the lein and deps-new templates to reference the new Clojars release
    * https://github.com/quil/quil-templates (requires separate release for clj template)
    * https://github.com/quil/sketchbook-template
    * https://github.com/quil/clj-sketch-template

12. Update external references to the release version
    * https://github.com/quil/quil-examples
    * https://github.com/quil/quil/wiki/Runnable-jar

13. (optional) Update quil.info website. Use [generate docs](https://github.com/quil/quil/wiki/Snippets#generate-documention) steps, but requires permission to update the quil-site page.
14. Announce the Quil release on [Clojureverse](https://clojureverse.org/), [r/clojure](https://www.reddit.com/r/Clojure/), and the Clojurians slack (both in #announcements and in #quil). Previously this also included clj-processing and clojure google groups.

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
