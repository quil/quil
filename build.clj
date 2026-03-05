(ns build
  (:require
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
        version (format "4.5.3.%s%s" revs snapshot)]
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
      :lib 'quil/quil
      :version version
      :basis basis
      :src-dirs ["src/clj"]
      :pom-data
      [[:licenses
        [:license
         [:name "Eclipse Public License 2.0"]
         [:url "https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt"]]]
       [:scm
        [:url "https://github.com/quil/quil"]
        [:connection "scm:git:https://github.com/quil/quil.git"]
        [:developerConnection "scm:git:ssh:git@github.com/quil/quil.git"]
        [:tag (if-not snapshot
                (str "v" version)
                "")]]]}))
  opts)

(defn release [opts]
  (aot opts)
  (pom opts)
  (let [jar-file (jar-file opts)]
    (b/uber {:class-dir class-dir
             :uber-file jar-file
             :basis basis
             :exclude
             [;; don't bundle clojure into the jar
              "^clojure[/].+"
              ;; exclude p5js to avoid JSC_DUPLICATE_EXTERN_INPUT during CLJS
              ;; advanced optimized compilation. Without this, the far-jar copy
              ;; of cljsjs/p5 and the mvn coordinate conflict and both provide
              ;; the an extern file.
              ;;
              ;; TODO: add an advanced compilation cljs example depending on a
              ;; jar to verify
              "cljsjs/p5.*"]})
    (println "release:" jar-file
             (format "(%.1f kb)" (/ (.length (io/file jar-file)) 1024.0)))))

(defn deploy [opts]
  (dd/deploy {:installer (if (:clojars opts) :remote :local)
              :artifact (b/resolve-path (jar-file opts))
              :pom-file "target/classes/META-INF/maven/quil/quil/pom.xml"}))
