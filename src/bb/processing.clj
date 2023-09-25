(ns bb.processing
  (:require
   [clojure.java.io :as io]
   [babashka.fs :as fs]
   [babashka.process :as bp]
   [babashka.http-client :as http]))

;; linux only for now
(defn install []
  (let [url "https://github.com/processing/processing4/releases/download/processing-1293-4.3/processing-4.3-linux-x64.tgz"]
    (println "Downloading processing4 ...")
    (io/copy (:body (http/get url {:as :stream}))
             (io/file "processing.jar"))

    (println "unpacking ...")
    (bp/shell "tar" "-zxf" "processing.jar")

    (fs/create-dirs "libraries")
    ;; core.jar, jogl-all.jar, gluegen-rt.jar
    (fs/copy-tree "processing-4.3/core/library" "libraries" {:replace-existing true})
    (fs/copy "processing-4.3/modes/java/libraries/dxf/library/dxf.jar" "libraries" {:replace-existing true})
    ;; pdf.jar, itext.jar
    (fs/copy-tree "processing-4.3/modes/java/libraries/pdf/library/" "libraries" {:replace-existing true})
    ;; svg.jar, batik.jar
    (fs/copy-tree "processing-4.3/modes/java/libraries/svg/library/" "libraries" {:replace-existing true})

    (fs/delete-tree "processing-4.3")
    (fs/delete "processing.jar")))

;; clj -T:build release caused:
;; Execution error (UnsupportedClassVersionError) at java.lang.ClassLoader/defineClass1 (ClassLoader.java:-2).
;; processing/core/PApplet has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 55.0
;; jdk11 is too old to process class file version, so using 17 instead:
;; $ sudo apt install openjdk-17-jdk
;; $ sudo update-java-alternatives -s java-1.17.0-openjdk-amd64
