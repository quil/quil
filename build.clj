(ns build
  (:require
   [build.processing :as processing]
   [clojure.java.io :as io]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as dd]))

;; The command to build a release-able jar in the `target` sub-directory:
;;
;; clj -T:build release

(def lib 'quil/quil)
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))

(defn release-version
  "Create a version id for release

  If opts includes :snapshot include git-sha and SNAPSHOT."
  [opts]
  (let [revs (b/git-count-revs nil)
        snapshot (if (:snapshot opts)
                   (format "-%s-SNAPSHOT"
                           (b/git-process {:git-args "rev-parse --short HEAD"}))
                   "")
        ;; Major/minor prefix should match upstream processing release
        version (format "4.5.2.%s%s" revs snapshot)]
    (when (:print opts)
      (println "Version:" version))
    version))

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
                  :ns-compile ['quil.helpers.applet-listener
                               'quil.applet 'quil.core 'quil.sketch]}))

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
      :basis basis
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
             :basis basis
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
