# Release

See also [release-process](https://github.com/quil/quil/wiki/Dev-notes#release-process).

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
