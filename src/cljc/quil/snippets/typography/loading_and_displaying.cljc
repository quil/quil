(ns quil.snippets.typography.loading-and-displaying
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal
            #?(:clj [clojure.java.io :as io]))
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

#?(:clj
   (defsnippet available-fonts
     "available-fonts"
     ;; font list is dependent on installed fonts which varies wildly between
     ;; individual computers and operating systems
     {:skip-image-diff? true}

     (q/background 255)
     (q/fill 0)
     (q/text-size 10)
     (doseq [[col fonts] (->> (q/available-fonts)
                              (partition-all 50)
                              (map-indexed vector))
             [row font] (map-indexed vector fonts)]
       (q/text font (+ 20 (* col 100)) (+ 20 (* row 10))))))

;; Monospaced.plain, SansSerif.plain, and Serif.plain are font aliases provided
;; by Java, so they should be available on Ubuntu, OSX, and hopefully Windows.
;;
;; FIXME: reference snapshots were not updated for high dpi tests aka retina
#?(:clj
   (defsnippet create-font
     "create-font"
     {;; slight differences in macOS and Linux font rendering
      :accepted-diff-threshold 0.01}

     (q/background 255)
     (q/fill 0)

     (comment "create Mono font by name and size")
     (q/text-font (q/create-font "Inconsolata" 30))
     (q/text "(print :hello-Inconsolata)" 20 50)

     (comment "create Sans Serif font by name and size and using smooth")
     (q/text-font (q/create-font "DejaVu Sans" 30 true))
     (q/text "(print :hello-DejaVu-Sans)" 20 100)

     (comment "create Sans Serif font by name and size and smoothing disabled")
     (q/text-font (q/create-font "Roboto" 30 false))
     (q/text "(print :hello-Roboto)" 20 150)

     (comment "create Serif font with all parameters")
     ;; macOS doesn't match Tinos Regular so using Tinos, which Ubuntu accepts as alias
     (q/text-font (q/create-font "Tinos" 30 false (char-array "what is it for?")))
     (q/text "(print :hello-Tinos)" 20 200)))

;; FIXME: reference snapshots were not updated for high dpi tests aka retina
;; TODO: prefer unit test over snapshot here
#?(:clj
   (defsnippet font-available-p
     "font-available?"
     {}

     (q/text-font (q/create-font "Roboto" 16))
     (q/background 255)
     (q/fill 0)
     (q/text (str "'Inconsolata' available: " (q/font-available? "Inconsolata"))
             20 20)
     (q/text (str "'Roboto' available: " (q/font-available? "Roboto"))
             20 40)
     (q/text (str "'DejaVu Sans' available: " (q/font-available? "DejaVu Sans"))
             20 60)
     (q/text (str "'Tinos Regular' available: " (q/font-available? "Tinos Regular"))
             20 80)
     (q/text (str "'My Custom Font' available: " (q/font-available? "My Custom Font"))
             20 100)))

(defsnippet load-font
  "load-font"
  {:setup (let [_ (comment "create url to load font")
                url #?(:clj (.getPath (io/resource "ComicSansMS-48.vlw"))
                       :cljs "https://www.fontsquirrel.com/fonts/download/roboto")]
            (q/set-state! :font (q/load-font url)))}

  (q/background 255)
  (q/fill 0)
  (let [font (q/state :font)]
    (q/text-font font)

    (q/text "CoMiC SaNs HeRe" 20 100)))

(defsnippet text-2d
  "text"
  {}

  (q/background 255)
  (q/fill 0)

  (comment "draw text")
  (q/text "word" 10 30)

  (comment "draw text in a 'box'")
  (q/text "a long sentence wrapping inside a box" 60 20 120 60))

#?(:clj
   (defsnippet text-3d
     "text"
     {:renderer :p3d
      ;; slight differences in macOS and Linux font rendering
      :accepted-diff-threshold 0.01}

     (q/text-font (q/create-font "Roboto" 12))
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

#?(:clj
   (defsnippet text-char
     "text-char"
     {:renderer :p3d}

     (q/text-font (q/create-font "Roboto" 12))
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
  (let [font #?(:clj (q/create-font "Inconsolata" 20)
                :cljs "Courier New")]
    (q/text-font font 20)
    (q/text "(print :hello)" 20 30)
    (q/text-font font 30)
    (q/text "(print-bigger :hello)" 20 100)))

#?(:clj
   (defsnippet text-num
     "text-num"
     {:renderer :p3d}

     (q/text-font (q/create-font "DejaVu Sans" 12))
     (q/background 255)
     (q/fill 0)
     (q/camera 70 70 70 0 0 0 0 0 -1)
     (q/text-num 42 0 0)
     (q/text-num 3/6 0 0 10)))
