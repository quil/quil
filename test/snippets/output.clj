(ns snippets.output
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet begin-raw-end-raw-s {:renderer :p3d}
  (begin-raw :dxf "generated/dxf.txt")
  (camera 150 150 150 0 0 0 0 0 1)
  (box 100)
  (end-raw))

(defsnippet save-s {:renderer :p3d}
  (camera 150 150 150 0 0 0 0 0 1)
  (box 100)
  (save "generated/box.png"))

(defsnippet save-frame-s {:renderer :p3d}
  (camera 150 150 150 0 0 0 0 0 1)
  (background 127)
  (with-rotation [(/ (frame-count) 10)]
   (box 100))
  (save-frame)
  (save-frame "generated/rotating-box-####.png"))
