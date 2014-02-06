(defproject quil "2.0.0-SNAPSHOT"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"
  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}
  :license {:name "Common Public License - v 1.0"
            :url "http://www.opensource.org/licenses/cpl1.0"}
  :repositories [["jogamp" "http://www.jogamp.org/deployment/maven/"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.originalsurfmex.processing/core "2.1"]
                 [org.jogamp.jogl/jogl-all-main "2.1.2-rc-20131025"]
                 [org.jogamp.gluegen/gluegen-rt-main "2.1.2-rc-20131025"]]
  :aot [quil.helpers.applet-listener quil.applet])
