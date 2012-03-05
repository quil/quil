(ns quil.version)

(def QUIL-VERSION {:major 1
                   :minor 0
                   :patch 0
                   :snapshot true})

(def QUIL-VERSION-STR
  (let [version QUIL-VERSION]
    (str "v"
         (:major version)
         "."
         (:minor version)
         (if-not (= 0 (:patch version)) (:patch version) "")
         (if (:snapshot version) "-dev" ""))))
