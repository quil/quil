(ns build
  (:require
   [build.processing :as processing]
   [clojure.java.io :as io]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as dd]))

;; This process seems to work to produce a rather chunky (12MB) jar.
;; On MacOS, download Processing 4.3 and copy this directory from the
;; app bundle into the top-level source directory for the processing-4
;; branch of quil:

;; Processing.app/Contents/Java/core/library/

;; (Please add the current pathing for Linux and/or Windows if you
;; know them!)

;; After which, this command:

;; clj -T:build release

;; ... should build a release-able jar in the target sub-directory.

;; I have been able to use the produced jar as a local dep in a new
;; quil project, and everything seems to work as expected so far. If
;; you have a chance to do any testing on your local platform, please
;; do!

(def lib 'quil/quil)
(def class-dir "target/classes")
;; create-basis will side-effect and verify the deps, which we may not want if
;; uploading new versions of said dependencies
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn release-version
  "Create a version id for release

  If opts includes :snapshot include git-sha and SNAPSHOT."
  [opts]
  (format "4.3.%s%s"
          (b/git-count-revs nil)
          (if (:snapshot opts)
            (format "-%s-SNAPSHOT"
                    (b/git-process {:git-args "rev-parse --short HEAD"}))
            "")))

(defn jar-file [opts]
  (format "target/%s-%s.jar" (name lib) (release-version opts)))

(defn clean [_]
  ;; release directory
  (b/delete {:path "target"})
  ;; aot classes
  (b/delete {:path "classes"}))

;; clj -T:build aot
(defn aot [_]
  (b/copy-dir {:src-dirs ["src/clj" "src/cljc" "src/cljs" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :src-dirs ["src/clj" "src/cljc" "src/cljs"]
                  :class-dir class-dir
                  :ns-compile ['quil.helpers.applet-listener 'quil.applet 'quil.sketch]}))

;; clj -T:build pom
(defn pom
  "Generate a pom.xml for the current version"
  [{:keys [snapshot] :as opts}]
  (let [version (release-version opts)]
    (b/write-pom
     {:class-dir class-dir
      :src-pom "build/template-pom.xml"
      :lib 'quil/quil
      :version version
      :basis @basis
      :scm {:tag (if-not snapshot
                   (str "v" version)
                   "")}
      :src-dirs ["src/clj"]}))
  opts)

(defn release [opts]
  (aot opts)
  (pom opts)
  (let [jar-file (jar-file opts)]
    (b/uber {:class-dir class-dir
             :uber-file jar-file
             :basis @basis
             ;; don't bundle clojure into the jar
             :exclude ["^clojure[/].+"]})
    (println "release:" jar-file
             (format "(%.1f kb)" (/ (.length (io/file jar-file)) 1024.0)))))

(defn deploy [opts]
  (dd/deploy {:installer (if (:clojars opts) :remote :local)
              :artifact (b/resolve-path (jar-file opts))
              :pom-file "target/classes/META-INF/maven/quil/quil/pom.xml"}))

(defn processing-clojars [_]
  (processing/clojars-release _))
