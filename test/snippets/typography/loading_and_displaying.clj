(ns snippets.typography.loading-and-displaying
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]
            clojure.java.io))

(defsnippet available-fonts {}
  (q/background 255)
  (q/fill 0)
  (q/text-size 7)
  (doseq [[col fonts] (->> (q/available-fonts)
                           (partition-all 50)
                           (map-indexed vector))
          [row font] (map-indexed vector fonts)]
    (q/text font (+ 20 (* col 100)) (+ 20 (* row 10)))))

(defsnippet create-font {}
  (q/background 255)
  (q/fill 0)

  (q/text-font (q/create-font "Courier New" 30))
  (q/text "(print :hello)" 20 50)

  (q/text-font (q/create-font "Georgia" 30 true))
  (q/text "(print :hello)" 20 100)

  (q/text-font (q/create-font "Georgia" 30 false (char-array "what is it for?")))
  (q/text "(print :hello)" 20 150))

(defsnippet font-available?-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str "'Courier New' available: " (q/font-available? "Courier New"))
        20 20)
  (q/text (str "'My Custom Font' available: " (q/font-available? "My Custom Font"))
        20 40))

(defsnippet load-font {}
  (q/background 255)
  (q/fill 0)
  (let [font (q/load-font (clojure.java.io/resource "ComicSansMS-48.vlw"))]
    (->> (clojure.java.io/resource "ComicSansMS-48.vlw")
         (q/load-font)
         (q/text-font))
    (q/text "CoMiC SaNs HeRe" 20 100)))

(defsnippet text {:renderer :p3d}
  (q/background 255)
  (q/fill 0)
  (q/camera 50 50 50 0 0 0 0 0 -1)
  (q/line 0 0 0 0 0 20)
  (q/line 0 0 0 0 20 0)
  (q/line 0 0 0 20 0 0)
  (q/text "2D" 0 15)
  (q/rotate-x (- q/HALF-PI))
  (q/text "3D" 0 -5 0)
  (q/rotate-y q/HALF-PI)
  (q/rect-mode :corners)
  (q/text "box" -30 0 30 -15))

(defsnippet text-char {:renderer :p3d}
  (q/background 255)
  (q/fill 0)
  (q/camera 50 50 50 0 0 0 0 0 -1)
  (q/text-char \Q 0 0)
  (q/text-char \W 0 0 10))

(defsnippet text-font {}
  (q/background 255)
  (q/fill 0)
  (let [font (q/create-font "Courier New" 20)]
    (q/text-font font)
    (q/text "(print :hello)" 20 30)
    (q/text-font font 30)
    (q/text "(print-bigger :hello)" 20 100)))

(defsnippet text-num {:renderer :p3d}
  (q/background 255)
  (q/fill 0)
  (q/camera 70 70 70 0 0 0 0 0 -1)
  (q/text-num 42 0 0)
  (q/text-num 3/6 0 0 10))
