(defproject quil "2.2.1-SNAPSHOT"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"

  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [quil/processing-core "2.2.1"]
                 [quil/processing-pdf "2.2.1"]
                 [quil/processing-dxf "2.2.1"]
                 [quil/jogl-all-fat "2.1.5"]
                 [quil/gluegen-rt-fat "2.1.5"]
                 [com.lowagie/itext "2.1.7"]]

  :hooks [cljx.hooks]

  :aot [quil.helpers.applet-listener quil.applet]

  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [garden "1.1.6"]
                                  [clj-http "0.9.1"]]
                   :plugins [[com.keminglabs/cljx "0.4.0"]]}}
  :test-selectors {:default (complement :manual)
                   :manual :manual}

  :cljx {:builds [{:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/clj/quil"
                   :rules :clj}
                  {:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/cljs/quil"
                   :rules :cljs}]}

  :source-paths ["src/clj" "target/gen/clj" "src/cljs" "target/gen/cljs"]
  :resource-paths ["resources"])
