(ns quil.snippets.typography.loading-and-displaying
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet available-fonts
     "available-fonts"
     {}

     (q/background 255)
     (q/fill 0)
     (q/text-size 10)
     (doseq [[col fonts] (->> (q/available-fonts)
                              (partition-all 50)
                              (map-indexed vector))
             [row font] (map-indexed vector fonts)]
       (q/text font (+ 20 (* col 100)) (+ 20 (* row 10))))))

#?(:clj
   (defsnippet create-font
     "create-font"
     {}

     (q/background 255)
     (q/fill 0)

     (comment "create font by name and size")
     (q/text-font (q/create-font "Courier New" 30))
     (q/text "(print :hello)" 20 50)

     (comment "create font by name and size and using smooth")
     (q/text-font (q/create-font "Georgia" 30 true))
     (q/text "(print :hello)" 20 100)

     (comment "create font with all parameters")
     (q/text-font (q/create-font "Georgia" 30 false (char-array "what is it for?")))
     (q/text "(print :hello)" 20 150)))

#?(:clj
   (defsnippet font-available?-s
     "font-available?"
     {}

     (q/background 255)
     (q/fill 0)
     (q/text (str "'Courier New' available: " (q/font-available? "Courier New"))
             20 20)
     (q/text (str "'Ubuntu' available: " (q/font-available? "Ubuntu"))
             20 40)
     (q/text (str "'My Custom Font' available: " (q/font-available? "My Custom Font"))
             20 60)))

#?(:clj
   (defsnippet load-font
     "load-font"
     {}

     (q/background 255)
     (q/fill 0)
     (->> (clojure.java.io/resource "ComicSansMS-48.vlw")
          (.getPath)
          (q/load-font)
          (q/text-font))
     (q/text "CoMiC SaNs HeRe" 20 100)))

#?(:clj
   (defsnippet text
     "text"
     {:renderer :p3d}

     (q/background 255)
     (q/fill 0)
     (q/camera 50 50 50 0 0 0 0 0 -1)
     (comment "draw x/y/z axis")
     (q/line 0 0 0 0 0 20)
     (q/line 0 0 0 0 20 0)
     (q/line 0 0 0 20 0 0)
     (comment "draw text '2D'")
     (q/text "2D" 0 15)
     (q/rotate-x (- q/HALF-PI))
     (comment "draw text '3D'")
     (q/text "3D" 0 -5 0)
     (comment "draw text 'box'")
     (q/rotate-y q/HALF-PI)
     (q/rect-mode :corners)
     (q/text "box" -30 0 30 -15)))

#?(:cljs
   (defsnippet text
     "text"
     {}

     (q/background 255)
     (q/fill 0)

     (comment "draw text")
     (q/text "word" 10 30)

     (comment "draw text in a 'box'")
     (q/text "a long sentence wrapping inside a box" 60 20 120 60)))

#?(:clj
   (defsnippet text-char
     "text-char"
     {:renderer :p3d}

     (q/background 255)
     (q/fill 0)
     (q/camera 50 50 50 0 0 0 0 0 -1)
     (q/text-char \Q 0 0)
     (q/text-char \W 0 0 10)))

(defsnippet text-font
  "text-font"
  {}

  (q/background 255)
  (q/fill 0)
  (let [font #?(:clj (q/create-font "Courier New" 20)
                :cljs (q/create-font "serif" 20))]
    (q/text-font font)
    (q/text "(print :hello)" 20 30)
    (q/text-font font 30)
    (q/text "(print-bigger :hello)" 20 100)))

#?(:clj
   (defsnippet text-num
     "text-num"
     {:renderer :p3d}

     (q/background 255)
     (q/fill 0)
     (q/camera 70 70 70 0 0 0 0 0 -1)
     (q/text-num 42 0 0)
     (q/text-num 3/6 0 0 10)))
