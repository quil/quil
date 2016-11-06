(defproject quil "2.5.0"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil/processing-core "3.2.2"]
                 [quil/processing-pdf "3.2.2"]
                 [quil/processing-dxf "3.2.2"]
                 [quil/processing-svg "3.2.2"]
                 [quil/jogl-all-fat "2.3.2"]
                 [quil/gluegen-rt-fat "2.3.2"]
                 [quil/processing-js "1.6.3.0"]
                 [com.lowagie/itext "2.1.7"]

                 ; svg
                 [org.apache.xmlgraphics/batik-svggen "1.8"]
                 [org.apache.xmlgraphics/batik-dom "1.8"]]

  :aot [quil.helpers.applet-listener quil.applet]

  :test-selectors {:default (complement :manual)
                   :manual :manual
                   :set-0 #(= 0 (:test-set %))
                   :set-1 #(= 1 (:test-set %))
                   :set-2 #(= 2 (:test-set %))
                   :set-3 #(= 3 (:test-set %))}

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]
  :resource-paths ["resources"]

  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [compojure "1.5.1"]
                                  [clj-http "3.3.0"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [org.clojure/tools.reader "0.10.0"]]
                   :plugins [[lein-ring "0.9.7"]]}

             :cljs-testing [:dev
                            {:hooks [leiningen.cljsbuild]
                             :plugins [[lein-cljsbuild "1.1.4"]]
                             :source-paths ["test/clj"]
                             :ring {:handler test-server/app}
                             :dependencies [[prismatic/dommy "1.1.0"]
                                            [org.clojure/clojure "1.8.0"]
                                            [org.clojure/clojurescript "1.9.293"]]

                             :cljsbuild
                             {:builds [{:source-paths ["target/classes" "test/clj" "test/cljs" "test/cljc" "src/cljs"]
                                        :compiler
                                        {:output-to "target/js/main.js"
                                         :optimizations :advanced
                                         :pretty-print true}}]}}]})
