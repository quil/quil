(defproject quil "4.3.1560"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [quil/processing-core "4.2.3"]
                 [quil/processing-pdf "4.2.3"]
                 [quil/processing-dxf "4.2.3"]
                 [quil/processing-svg "4.2.3"]

                 ;; FIXME: on clojure-cli these are updated to 2.4.0-rc-20230201
                 [quil/jogl-all-fat "2.3.2"]
                 [quil/gluegen-rt-fat "2.3.2"]

                 [cljsjs/p5 "1.7.0-0"]
                 [com.lowagie/itext "2.1.7"
                  :exclusions [bouncycastle/bctsp-jdk14]]
                 [org.bouncycastle/bctsp-jdk14 "1.38"]

                 ;; svg
                 [org.apache.xmlgraphics/batik-svggen "1.8"]
                 [org.apache.xmlgraphics/batik-dom "1.8"]]

  :aot [quil.helpers.applet-listener quil.applet]

  :test-selectors {:default (complement :manual)
                   :manual :manual}

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj" "test/cljc"]
  :resource-paths ["resources"]

  :cljfmt {:file-pattern #"\.clj[sc]?$"}

  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [compojure "1.6.1"]
                                  [clj-http "3.10.0"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [org.clojure/tools.reader "1.3.2"]
                                  [cheshire "5.9.0"]
                                  [com.vladsch.flexmark/flexmark "0.34.18"]
                                  [com.vladsch.flexmark/flexmark-ext-autolink "0.34.18"]
                                  [com.vladsch.flexmark/flexmark-ext-gfm-tables "0.34.18"]
                                  [com.vladsch.flexmark/flexmark-ext-anchorlink "0.34.18"]
                                  [com.vladsch.flexmark/flexmark-ext-wikilink "0.34.18"]
                                  [etaoin "1.0.40"]]
                   :plugins [[lein-ring "0.12.5"]
                             [lein-cljfmt "0.6.4"]
                             [lein-ancient "0.6.15"]]
                   :source-paths ["dev"]}

             ;; For a test server with compiled js/main.js
             ;; lein with-profile cljs-testing do cljsbuild once tests, ring server
             :cljs-testing [:dev
                            {:plugins [[lein-cljsbuild "1.1.7"]]
                             :source-paths ["test/cljs"]
                             :ring {:handler test-server/app}
                             :dependencies [[prismatic/dommy "1.1.0"]
                                            [org.clojure/clojure "1.10.1"]
                                            [org.clojure/clojurescript "1.10.520"]]

                             :cljsbuild
                             {:builds [; Compiles all tests in advanced mode. Used for release testing.
                                       {:id "tests"
                                        :source-paths ["test/cljs" "src/cljc" "src/cljs"]
                                        :compiler
                                        {:output-to "target/js/main.js"
                                         :optimizations :advanced
                                         :pretty-print true}}

                                        ; Compiles sample sketch. Used for development to
                                        ; see changes immediately when changing quil cljs code.
                                       {:id "development"
                                        :source-paths ["dev" "src/cljs" "src/cljc"]
                                        :compiler {:output-to "target/jsdev/main.js"
                                                   :output-dir "target/jsdev"
                                                   :main "sample"}}

                                        ; Compiles script that outputs all code snippets to
                                        ; console. Used to get cljs version of code snippets.
                                       {:id "snippets-generation"
                                        :source-paths ["dev-resources" "src/cljs"]
                                        :compiler {:output-to "target/snippets_generation/main.js"
                                                   :output-dir "target/snippets_generation"
                                                   :main "utils.snippets_generator"}}]}}]})
