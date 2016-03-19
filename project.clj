(defproject quil "2.3.1-SNAPSHOT"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [quil/processing-core "3.0.2"]
                 [quil/processing-pdf "3.0.2"]
                 [quil/processing-dxf "3.0.2.1"]
                 [quil/jogl-all-fat "2.3.2"]
                 [quil/gluegen-rt-fat "2.3.2"]
                 [quil/processing-js "1.4.16.0"]
                 [com.lowagie/itext "2.1.7"]]

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
                                  [compojure "1.3.4"]
                                  [clj-http "1.1.2"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [org.clojure/tools.reader "0.9.2"]]
                   :plugins [[lein-ring "0.9.4"]]}

             :cljs-testing [:dev
                            {:hooks [leiningen.cljsbuild]
                             :plugins [[lein-cljsbuild "1.0.6"]]
                             :source-paths ["test/clj"]
                             :ring {:handler test-server/app}
                             :dependencies [[prismatic/dommy "1.0.0"]
                                            [org.clojure/clojure "1.7.0"]
                                            [org.clojure/clojurescript "1.7.107"]]

                             :cljsbuild
                             {:builds [{:source-paths ["target/classes" "test/clj" "test/cljs" "test/cljc" "src/cljs"]
                                        :compiler
                                        {:output-to "target/js/main.js"
                                         :optimizations :advanced
                                         :pretty-print true}}]}}]})
