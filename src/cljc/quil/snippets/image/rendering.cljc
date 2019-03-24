(ns quil.snippets.image.rendering
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet blend-mode
  "blend-mode"
  {:renderer :p2d}

  (q/background 255)
  (let [modes [:replace :blend :add #?(:clj :subtract) :darkest
               :lightest :difference :exclusion :multiply :screen
               #?@(:cljs (:overlay :hard-light :soft-light
                                   :dodge :burn))]
        splitted (partition-all 4 modes)]
    (dotimes [row (count splitted)]
      (dotimes [col (count (nth splitted row))]
        (let [mode (nth (nth splitted row) col)
              gr (q/create-graphics 100 100 :p2d)]
          (q/with-graphics gr
            (q/background 127)
            (q/blend-mode mode)
            (q/no-stroke)
            (q/fill 255 0 0)
            (q/rect 10 20 80 20)
            (q/fill 50 170 255 127)
            (q/rect 60 10 20 80)
            (q/fill 200 130 150 200)
            (q/rect 10 60 80 20)
            (q/fill 20 240 50 50)
            (q/rect 20 10 20 80))
          (q/image gr (* col 120) (* row 120)))))))

(defsnippet create-graphics
  "create-graphics"
  {}

  (q/background 255)
  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (q/background 127)
      (q/ellipse 50 50 80 40))
    (q/image gr 0 0))

  #?@(:clj ((comment "use different renderer")
            (let [gr (q/create-graphics 100 100 :java2d)]
              (q/with-graphics gr
                (q/background 127)
                (q/ellipse 50 50 40 80))
              (q/image gr 100 100))
            (comment "write output to pdf file")
            (let [gr (q/create-graphics 100 100 :pdf "generated/create-graphics.pdf")]
              (q/with-graphics gr
                (q/background 127)
                (q/ellipse 50 50 80 40)
                (.dispose gr))))))
