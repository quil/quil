(ns quil.snippets.output
  (:require #?@(:clj [[quil.snippets.macro :refer [defsnippet]]
                      [clojure.test :refer [is]]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet begin-raw-end-raw
     ["begin-raw" "end-raw"]
     {:renderer :p3d}

     (q/begin-raw :dxf "generated/dxf.txt")
     (q/camera 150 150 150 0 0 0 0 0 1)
     (q/box 100)
     (q/end-raw)))

(defsnippet save
  "save"
  {:renderer :p3d
   :skip-image-diff? true}

  (q/camera 150 150 150 0 0 0 0 0 1)
  (q/box 100)
  (q/save "generated/box.png")
  (comment "stop sketch after saving image")
  (comment "otherwise it will show save dialog")
  (comment "on every iteration")
  (q/exit))

#?(:clj
   (defsnippet save-frame
     "save-frame"
     {:renderer :p3d}

     (q/camera 150 150 150 0 0 0 0 0 1)
     (q/background 127)
     (q/with-rotation [(/ (q/frame-count) 10)]
       (q/box 100))
     (q/save-frame "generated/rotating-box-####.png")))

#?(:clj
   (defsnippet do-record
     "do-record"
     {}

     (doseq [type [:svg :pdf]
             i (range 3)]
       ; render 3 pdf files and check that each is non-empty
       ; at the end
       (let [file (str "generated/record_" i "." (name type))]
         (q/do-record (q/create-graphics 200 200 type file)
                      (q/fill 255 0 0)
                      (q/ellipse 100 100
                                 (+ 50 (* 50 i))
                                 (+ 50 (* 50 i))))
         (is (pos? (.length (clojure.java.io/file file))))))))
