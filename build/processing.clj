(ns build.processing
  (:require
   [clojure.tools.build.api :as b]))

(defn download [_]
  (b/process {:command-args ["bb" "processing-install"]})
  _)



