(ns bb.processing
  (:require
   [clojure.java.io :as io]
   [babashka.fs :as fs]
   [babashka.process :as bp]
   [babashka.http-client :as http]))

;; linux only for now
(defn install []
  (let [url "https://github.com/processing/processing4/releases/download/processing-1304-4.4.4/processing-4.4.4-linux-x64-portable.zip"]
    (println "Downloading processing4 ...")
    (io/copy (:body (http/get url {:as :stream}))
             (io/file "processing.zip"))

    (println "unpacking ...")
    (bp/shell "unzip" "processing.zip")

    (fs/create-dirs "libraries")
    ;; dxf.jar
    (fs/copy "Processing/lib/app/resources/modes/java/libraries/dxf/library/dxf.jar"
             "libraries" {:replace-existing true})
    ;; pdf.jar, itext.jar
    (fs/copy-tree "Processing/lib/app/resources/modes/java/libraries/pdf/library"
                  "libraries" {:replace-existing true})
    ;; svg.jar, batik.jar
    (fs/copy-tree "Processing/lib/app/resources/modes/java/libraries/svg/library/"
                  "libraries" {:replace-existing true})

    (fs/delete-tree "Processing")
    (fs/delete "processing.zip")))

;; clj -T:build release caused:
;; Execution error (UnsupportedClassVersionError) at java.lang.ClassLoader/defineClass1 (ClassLoader.java:-2).
;; processing/core/PApplet has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 55.0
;; jdk11 is too old to process class file version, so using 17 instead:
;; $ sudo apt install openjdk-17-jdk
;; $ sudo update-java-alternatives -s java-1.17.0-openjdk-amd64
