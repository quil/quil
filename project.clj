(defproject org.clojars.norgat/quil "2.0.0-alpha"
  :description "(mix Processing Clojure)"
  :url "http://github.com/Norgat/quil"

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

  :profiles {:dev {:dependencies [[hiccup "1.0.5"]
                                  [garden "1.1.6"]
                                  [clj-http "0.9.1"]]}}
  :test-selectors {:default (complement :manual)
                   :manual :manual}

  :cljx {:builds [{:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/quil"
                   :rules :clj}
                  {:source-paths ["src/cljx/quil"]
                   :output-path "target/gen/cljs/quil"
                   :rules :cljs}]}

  :source-paths ["src" "target/gen" "src/cljs" "target/gen/cljs"]
  :jar-exclusions [#"^cljx.*" #"^cljs.*" #"^resources.*" #"^js.*"]
  :uberjar-exclusions [#"^cljx.*" #"^cljs.*" #"^resources.*" #"^js.*"])
