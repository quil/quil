(ns snippets.typography.loading-and-displaying
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]
            clojure.java.io))

(defsnippet available-fonts-s {}
  (background 255)
  (fill 0)
  (text-size 7)
  (doseq [[col fonts] (->> (available-fonts)
                           (partition-all 50)
                           (map-indexed vector))
          [row font] (map-indexed vector fonts)]
    (text font (+ 20 (* col 100)) (+ 20 (* row 10)))))

(defsnippet create-font-s {}
  (background 255)
  (fill 0)

  (text-font (create-font "Courier New" 30))
  (text "(print :hello)" 20 50)

  (text-font (create-font "Georgia" 30 true))
  (text "(print :hello)" 20 100)

  (text-font (create-font "Georgia" 30 false (char-array "what is it for?")))
  (text "(print :hello)" 20 150))

(defsnippet font-available?-s {}
  (background 255)
  (fill 0)
  (text (str "'Courier New' available: " (font-available? "Courier New"))
        20 20)
  (text (str "'My Custom Font' available: " (font-available? "My Custom Font"))
        20 40))

(defsnippet load-font-s {}
  (background 255)
  (fill 0)
  (let [font (load-font (clojure.java.io/resource "ComicSansMS-48.vlw"))]
    (->> (clojure.java.io/resource "ComicSansMS-48.vlw")
         (load-font)
         (text-font))
    (text "CoMiC SaNs HeRe" 20 100)))

(defsnippet text-s {:renderer :p3d}
  (background 255)
  (fill 0)
  (camera 50 50 50 0 0 0 0 0 -1)
  (line 0 0 0 0 0 20)
  (line 0 0 0 0 20 0)
  (line 0 0 0 20 0 0)
  (text "2D" 0 15)
  (rotate-x (- HALF-PI))
  (text "3D" 0 -5 0)
  (rotate-y HALF-PI)
  (rect-mode :corners)
  (text "box" -30 0 30 -15))

(defsnippet text-char-s {:renderer :p3d}
  (background 255)
  (fill 0)
  (camera 50 50 50 0 0 0 0 0 -1)
  (text-char \Q 0 0)
  (text-char \W 0 0 10))

(defsnippet text-font-s {}
  (background 255)
  (fill 0)
  (let [font (create-font "Courier New" 20)]
    (text-font font)
    (text "(print :hello)" 20 30)
    (text-font font 30)
    (text "(print-bigger :hello)" 20 100)))

(defsnippet text-num-s {:renderer :p3d}
  (background 255)
  (fill 0)
  (camera 70 70 70 0 0 0 0 0 -1)
  (text-num 42 0 0)
  (text-num 3/6 0 0 10))
