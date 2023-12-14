(ns build.processing
  (:require
   [clojure.data.xml :as xml]
   [clojure.tools.build.api :as b]
   [clojure.java.io :as io]))

(defn version []
  "4.2.3")

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

(defn download [_]
  (b/process {:command-args ["bb" "processing-install"]})

  (let [artifacts ["core" "pdf" "dxf" "svg"]]
    (doseq [artifact artifacts
            :let [artifact-id (str "processing-" artifact)
                  jar-file (str artifact-id "-" (version) ".jar")]]
      (b/copy-file {:src (str "libraries/" artifact ".jar")
                    :target jar-file})
      (processing-pom {:artifact-id artifact-id
                       :version (version)})))
  _)
