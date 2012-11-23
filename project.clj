(defproject quil "1.6.0"
  :description "(mix Processing Clojure)"
  :url "http://github.com/quil/quil"
  :mailing-list {:name "Quil Mailing List"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-processing"
                 :post "clj-processing@googlegroups.com"}
  :license {:name "Common Public License - v 1.0"
            :url "http://www.opensource.org/licenses/cpl1.0"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojars.processing-core/org.processing.core "1.5.1"]
                 [org.clojars.processing-core/org.processing.gluegen-rt "1.5.1"]
                 [org.clojars.processing-core/org.processing.jogl "1.5.1"]
                 [org.clojars.processing-core/org.processing.opengl "1.5.1"]
                 [org.clojars.processing-core/org.processing.itext "1.5.1"]
                 [org.clojars.processing-core/org.processing.pdf "1.5.1"]]
  :aot [quil.applet])
