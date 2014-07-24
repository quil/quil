(defproject quil "2.2.1-SNAPSHOT"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Common Public License - v 1.0"
            :url "http://www.opensource.org/licenses/cpl1.0"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [quil/processing-core "2.2.1"]
                 [quil/processing-pdf "2.2.1"]
                 [quil/processing-dxf "2.2.1"]
                 [quil/jogl-all-fat "2.1.5"]
                 [quil/gluegen-rt-fat "2.1.5"]
                 [com.lowagie/itext "2.1.7"]]

  :plugins [[com.keminglabs/cljx "0.4.0"]]

  :hooks [cljx.hooks]

  :aot [quil.helpers.applet-listener quil.applet]

  :test-selectors {:default (complement :manual)
                   :manual :manual}

  :cljx {:builds [{:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/clj/quil"
                   :rules :clj}
                  {:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/cljs/quil"
                   :rules :cljs}

                  {:source-paths ["test/cljx"]
                   :output-path "target/gen/clj/test"
                   :rules :clj}
                  {:source-paths ["test/cljx"]
                   :output-path "target/gen/cljs/test"
                   :rules :cljs}]}

  :source-paths ["src/clj" "target/gen/clj" "src/cljs" "target/gen/cljs"]
  :test-paths ["test/clj" "target/gen/clj/test"]
  :resource-paths ["resources"]


  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [garden "1.1.6"]
                                  [clj-http "0.9.1"]]}

             :test-1.5.1 {:dependencies [[org.clojure/clojurescript "0.0-2234"]
                                         [prismatic/dommy "0.1.2"]
                                         [quil "2.2.1-SNAPSHOT"]]
                          :hooks [leiningen.cljsbuild]
                          :plugins [[lein-cljsbuild "1.0.3"]]
                          :aot [quil.helpers.applet-listener quil.applet]

                          :cljsbuild
                          {:builds [{:source-paths ["test/clj" "test/cljs" "target/gen/cljs/test"]
                                     :compiler
                                     {:output-to "target/js/main.js"
                                      :optimizations :whitespace
                                      :pretty-print true}}]}}

             :test-1.6.0 {:dependencies [[org.clojure/clojure "1.6.0"]
                                         [org.clojure/clojurescript "0.0-2268"]
                                         [prismatic/dommy "0.1.2"]
                                         [quil "2.2.1-SNAPSHOT"]]
                          :hooks [leiningen.cljsbuild]
                          :plugins [[lein-cljsbuild "1.0.3"]]
                          :aot [quil.helpers.applet-listener quil.applet]

                          :cljsbuild
                          {:builds [{:source-paths ["test/clj" "test/cljs" "target/gen/cljs/test"]
                                     :compiler
                                     {:output-to "target/js/main.js"
                                      :optimizations :whitespace
                                      :pretty-print true}}]}}})
