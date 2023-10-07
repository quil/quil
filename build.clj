(ns build
  (:require [clojure.tools.build.api :as b]))

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
(def version (format "4.3.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

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

(defn release [_]
  (aot _)
  (b/uber {:class-dir class-dir
           :uber-file jar-file
           :basis basis
           ;; don't bundle clojure into the jar
           :exclude ["^clojure[/].+"]}))
