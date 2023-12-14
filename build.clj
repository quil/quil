(ns build
  (:require
   [build.processing :as processing]
   [clojure.data.xml :as xml]
   [clojure.java.io :as jio]
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
(def basis (b/create-basis {:project "deps.edn"}))

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
  (b/compile-clj {:basis basis
                  :src-dirs ["src/clj" "src/cljc" "src/cljs"]
                  :class-dir class-dir
                  :ns-compile ['quil.helpers.applet-listener 'quil.applet 'quil.sketch]}))

(xml/alias-uri 'pom "http://maven.apache.org/POM/4.0.0")

(defn- pom-template [{:keys [version src-dirs]}]
  [::pom/project
   {:xmlns "http://maven.apache.org/POM/4.0.0"
    (keyword "xmlns:xsi") "http://www.w3.org/2001/XMLSchema-instance"
    (keyword "xsi:schemaLocation")
    "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"}
   [::pom/modelVersion "4.0.0"]
   [::pom/packaging "jar"]
   [::pom/groupId "quil"]
   [::pom/artifactId "quil"]
   [::pom/version version]
   [::pom/name "quil"]
   [::pom/licenses
    [::pom/license
     [::pom/name "Eclipse Public License 2.0"]
     [::pom/url "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt"]]]
   [::pom/scm
    [::pom/url "https://github.com/quil/quil"]
    [::pom/connection "scm:git:https://github.com/quil/quil.git"]
    [::pom/developerConnection "scm:git:ssh:git@github.com/quil/quil.git"]
    (when-not (re-find #"-SNAPSHOT$" version)
      [::pom/tag (str "v" version)])]
   [::pom/dependencies
    [:-comment " all deps are baked into the jar "]]
   [::pom/build
    (for [src-dir src-dirs]
      [::pom/sourceDirectory src-dir])]
   [::pom/repositories
    [::pom/repository
     [::pom/id "clojars"]
     [::pom/url "https://repo.clojars.org/"]]]])

;; clj -T:build pom
(defn pom
  "Generate a pom.xml for the current version"
  [opts]
  (let [version (release-version opts)]
    (->> {:version version
          :src-dirs ["src/clj"]}
         pom-template
         xml/sexp-as-element
         xml/indent-str
         (spit (jio/file "." "pom.xml")))
    (println "Generated pom.xml for version:" version)))

(defn release [opts]
  (aot opts)
  (pom opts)
  (let [jar-file (jar-file opts)]
    (b/uber {:class-dir class-dir
             :uber-file jar-file
             :basis basis
             ;; don't bundle clojure into the jar
             :exclude ["^clojure[/].+"]})
    (println "release:" jar-file
             (format "(%.1f kb)" (/ (.length (jio/file jar-file)) 1024.0)))))

(defn deploy [opts]
  (dd/deploy {:installer (if (:clojars opts) :remote :local)
              :artifact (b/resolve-path (jar-file opts))
              :pom-file "pom.xml"}))

(defn processing-clojars [_]
  (processing/clojars-release _))
