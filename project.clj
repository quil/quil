(defproject quil "2.3.0"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [quil/processing-core "3.0.1"]
                 [quil/processing-pdf "3.0.1"]
                 [quil/processing-dxf "3.0.1"]
                 [quil/jogl-all-fat "2.3.2"]
                 [quil/gluegen-rt-fat "2.3.2"]
                 [quil/processing-js "1.4.8.2"]
                 [com.lowagie/itext "2.1.7"]]

  :aot [quil.helpers.applet-listener quil.applet]

  :prep-tasks [["cljx" "once"] "compile"]

  :test-selectors {:default (complement :manual)
                   :manual :manual
                   :set-0 #(= 0 (:test-set %))
                   :set-1 #(= 1 (:test-set %))
                   :set-2 #(= 2 (:test-set %))
                   :set-3 #(= 3 (:test-set %))}

  :cljx {:builds [{:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/clj/quil"
                   :rules :clj}
                  {:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/cljs/quil"
                   :rules :cljs}

                  {:source-paths ["test/cljx"]
                   :output-path "target/gentest/clj"
                   :rules :clj}
                  {:source-paths ["test/cljx"]
                   :output-path "target/gentest/cljs"
                   :rules :cljs}]}

  :source-paths ["src/clj" "target/gen/clj" "src/cljs" "target/gen/cljs"]
  :test-paths ["test/clj" "target/gentest/clj"]
  :resource-paths ["resources"]

  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [compojure "1.3.4"]
                                  [clj-http "1.1.2"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [org.clojure/tools.reader "0.9.2"]]
                   :plugins [[com.keminglabs/cljx "0.6.0"]
                             [lein-ring "0.9.4"]]}

             :cljs-testing [:dev
                            {:hooks [leiningen.cljsbuild]
                             :plugins [[lein-cljsbuild "1.0.6"]]
                             :source-paths ["test/clj"]
                             :ring {:handler test-server/app}
                             :dependencies [[prismatic/dommy "1.0.0"]
                                            [org.clojure/clojure "1.7.0"]
                                            [org.clojure/clojurescript "1.7.107"]]

                             :cljsbuild
                             {:builds [{:source-paths ["target/classes" "test/clj" "test/cljs" "target/gentest/cljs" "src/cljs"]
                                        :compiler
                                        {:output-to "target/js/main.js"
                                         :optimizations :advanced
                                         :pretty-print true}}]}}]})
