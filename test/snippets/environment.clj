(ns snippets.environment
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :as q]))

(defsnippet current-frame-rate-target-frame-rate-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/current-frame-rate)) 10 20)
  (q/text (str (q/target-frame-rate)) 10 40))

(defsnippet current-graphics-s {}
  (q/background 255)
  (q/fill 0 0 255)
  (.rect (q/current-graphics) 0 0 100 100)

  (let [gr (q/create-graphics 100 100)]
    (q/with-graphics gr
      (.fill (q/current-graphics) 255 255 0)
      (.ellipse (q/current-graphics) 50 50 100 100))
    (q/image gr 70 70)))

(defsnippet cursor-s {}
  (q/no-cursor)
  (q/cursor)

  (doseq [type [:arrow :cross :hand :move :text :wait]]
    (q/cursor type)))

(defsnippet cursor-image-s {}
  (let [curs (q/create-graphics 32 32)]
    (q/with-graphics curs
      (q/fill 0 0)
      (q/ellipse 16 16 32 32)
      (q/ellipse 16 16 8 8))
    (q/cursor-image curs)
    (q/cursor-image curs 16 16)))

(defsnippet focused-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/focused)) 10 20))

(defsnippet frame-count-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/frame-count)) 10 20))

(defsnippet frame-rate-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/target-frame-rate)) 10 20)

  (q/frame-rate (inc (rand-int 5))))

(defsnippet height-width-s {}
  (q/background 255)
  (q/fill 0)
  (q/text (str (q/width) "x" (q/height)) 10 20))

(defsnippet no-cursos-s {}
  (q/no-cursor))

(defsnippet screen-height-screen-width-s {}
  (q/background 255)
  (q/fill 0)
  (let [w (q/screen-width)
        h (q/screen-height)]
    (q/text (str w "x" h) 10 20)))
