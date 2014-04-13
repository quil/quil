(ns snippets.typography.attributes
  (:require [quil.snippet :refer [defsnippet]]
            [quil.core :refer :all]))

(defsnippet text-align-s {}
  (fill 0)
  (stroke 255 0 0)
  (stroke-weight 5)
  (let [h-align [:left :center :right]
        v-align [:top :bottom :center :baseline]]

    (doseq [ind (range (count h-align))
            :let [x 50
                  y (+ 20 (* ind 50))]]
      (text-align (h-align ind))
      (text (name (h-align ind)) x y)
      (point x y))

    (doseq [ind-h (range (count h-align))
            ind-v (range (count v-align))
            :let [x (+ 70 (* ind-v 100))
                  y (+ 250 (* ind-h 50))
                  h-al (h-align ind-h)
                  v-al (v-align ind-v)
                  txt (str (name h-al) "+" (name v-al))]]
      (text-align h-al v-al)
      (text txt x y)
      (point x y))))

(defsnippet text-leading-s {}
  (fill 0)
  (doseq [ind (range 4)
          :let [leading (* ind 10)]]
    (text-leading leading)
    (text (str "text leading\n" leading) 20 (+ 20 (* ind 100)))))

(defsnippet text-mode-s {:renderer :p2d}
  (fill 0)
  (text-mode :model)
  (text "text-mode: model" 20 50)

  (text-mode :shape)
  (text "text-mode: shape" 20 100))

(defsnippet text-size-s {}
  (fill 0)
  (doseq [ind (range 6)
          :let [size (+ 10 (* ind 5))]]
    (text-size size)
    (text (str "Text size: " size) 20 (+ 20 (* ind 80)))))

(defsnippet text-width-s {}
  (fill 0)
  (let [txt "Hello, world!"
        width (text-width txt)]
    (text txt 20 20)
    (text (str "Width of text above is " width) 20 40)))

