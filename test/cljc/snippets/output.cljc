(ns snippets.output
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

#?(:clj
   (defsnippet begin-raw-end-raw {:renderer :p3d}
     (q/begin-raw :dxf "generated/dxf.txt")
     (q/camera 150 150 150 0 0 0 0 0 1)
     (q/box 100)
     (q/end-raw)))

#?(:clj
   (defsnippet save {:renderer :p3d}
     (q/camera 150 150 150 0 0 0 0 0 1)
     (q/box 100)
     (q/save "generated/box.png")))

#?(:clj
   (defsnippet save-frame {:renderer :p3d}
     (q/camera 150 150 150 0 0 0 0 0 1)
     (q/background 127)
     (q/with-rotation [(/ (q/frame-count) 10)]
       (q/box 100))
     (q/save-frame)
     (q/save-frame "generated/rotating-box-####.png")))
