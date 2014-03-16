(ns snippets.environment
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet current-frame-rate-target-frame-rate-s {}
  (background 255)
  (fill 0)
  (text (str (current-frame-rate)) 10 20)
  (text (str (target-frame-rate)) 10 40))

(defsnippet current-graphics-s {}
  (background 255)
  (fill 0 0 255)
  (.rect (current-graphics) 0 0 100 100)

  (let [gr (create-graphics 100 100)]
    (with-graphics gr
      (.fill (current-graphics) 255 255 0)
      (.ellipse (current-graphics) 50 50 100 100))
    (image gr 70 70)))

(defsnippet cursor-s {}
  (no-cursor)
  (cursor)

  (doseq [type [:arrow :cross :hand :move :text :wait]]
    (cursor type)))

(defsnippet cursor-image-s {}
  (let [curs (create-graphics 32 32)]
    (with-graphics curs
      (fill 0 0)
      (ellipse 16 16 32 32)
      (ellipse 16 16 8 8))
    (cursor-image curs)
    (cursor-image curs 16 16)))

(defsnippet focused-s {}
  (background 255)
  (fill 0)
  (text (str (focused)) 10 20))

(defsnippet frame-count-s {}
  (background 255)
  (fill 0)
  (text (str (frame-count)) 10 20))

(defsnippet frame-rate-s {}
  (background 255)
  (fill 0)
  (text (str (target-frame-rate)) 10 20)

  (frame-rate (inc (rand-int 5))))

(defsnippet height-width-s {}
  (background 255)
  (fill 0)
  (text (str (width) "x" (height)) 10 20))

(defsnippet no-cursos-s {}
  (no-cursor))

(defsnippet screen-height-screen-width-s {}
  (background 255)
  (fill 0)
  (let [w (screen-width)
        h (screen-height)]
    (text (str w "x" h) 10 20)))
