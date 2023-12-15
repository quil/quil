(ns build.processing
  "Publishes copies of the Processing jars to Clojars.

  Processing jars are not published to maven, so publishes copies as
  quil/processing-{core,pdf,dxf,svg} jars on Clojars so that Quil can depend on
  them as mvn coordinates instead of locals."
  (:require
   [clojure.data.xml :as xml]
   [clojure.java.io :as io]
   [clojure.tools.build.api :as b]
   [deps-deploy.deps-deploy :as dd]))

(xml/alias-uri 'pom "http://maven.apache.org/POM/4.0.0")

(defn- processing-template [{:keys [artifact-id version]}]
  [::pom/project
   {:xmlns "http://maven.apache.org/POM/4.0.0"
    (keyword "xmlns:xsi") "http://www.w3.org/2001/XMLSchema-instance"
    (keyword "xsi:schemaLocation")
    "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"}
   [::pom/modelVersion "4.0.0"]
   [::pom/packaging "jar"]
   [::pom/groupId "quil"]
   [::pom/artifactId artifact-id]
   [::pom/version version]
   [::pom/licenses
    [::pom/license
     [::pom/name "GNU Lesser General Public License"]
     [::pom/url "https://www.gnu.org/licenses/lgpl.html"]]]])

(defn processing-pom
  [{:keys [version artifact-id] :as params}]
  (let [filename (str artifact-id "-" version "-pom.xml")]
    (->> params
         processing-template
         xml/sexp-as-element
         xml/indent-str
         (spit (io/file "." filename)))
    (println "Generated " filename)
    filename))

(defn deploy [{:keys [jar-file pom-file clojars] :as opts}]
  (dd/deploy {:installer (if clojars :remote :local)
              :artifact jar-file
              :pom-file pom-file})
  opts)

;; Specify processing version to release both here and in the
;; bb.processing/install URL
(def processing-version "4.2.3")

(defn clojars-release [_]
  ;; TODO: inline processing-install here
  (b/process {:command-args ["bb" "processing-install"]})

  (let [version processing-version
        artifacts ["core" "pdf" "dxf" "svg"]]
    (doseq [artifact artifacts
            :let [artifact-id (str "processing-" artifact)
                  jar-file (str artifact-id "-" version ".jar")]]
      (b/copy-file {:src (str "libraries/" artifact ".jar")
                    :target jar-file})
      (let [pom-file (processing-pom {:artifact-id artifact-id
                                      :version version})]
        (deploy {:jar-file jar-file
                 :pom-file pom-file
                 :clojars false})
        (b/delete {:path pom-file})
        (b/delete {:path jar-file}))))
  _)
