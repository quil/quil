(ns snippets.typography.attributes
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))

(defsnippet text-align {}
  (q/fill 0)
  (q/stroke 255 0 0)
  (q/stroke-weight 5)
  (let [h-align [:left :center :right]
        v-align [:top :bottom :center :baseline]]

    (doseq [ind (range (count h-align))
            :let [x 50
                  y (+ 20 (* ind 50))]]
      (q/text-align (h-align ind))
      (q/text (name (h-align ind)) x y)
      (q/point x y))

    (doseq [ind-h (range (count h-align))
            ind-v (range (count v-align))
            :let [x (+ 70 (* ind-v 100))
                  y (+ 250 (* ind-h 50))
                  h-al (h-align ind-h)
                  v-al (v-align ind-v)
                  txt (str (name h-al) "+" (name v-al))]]
      (q/text-align h-al v-al)
      (q/text txt x y)
      (q/point x y))))

(defsnippet text-leading {}
  (q/fill 0)
  (doseq [ind (range 4)
          :let [leading (* ind 10)]]
    (q/text-leading leading)
    (q/text (str "text leading\n" leading) 20 (+ 20 (* ind 100)))))

(defsnippet text-mode {:renderer :p2d}
  (q/fill 0)
  (q/text-mode :model)
  (q/text "text-mode: model" 20 50)

  (q/text-mode :shape)
  (q/text "text-mode: shape" 20 100))

(defsnippet text-size {}
  (q/fill 0)
  (doseq [ind (range 6)
          :let [size (+ 10 (* ind 5))]]
    (q/text-size size)
    (q/text (str "Text size: " size) 20 (+ 20 (* ind 80)))))

(defsnippet text-width {}
  (q/fill 0)
  (let [txt "Hello, world!"
        width (q/text-width txt)]
    (q/text txt 20 20)
    (q/text (str "Width of text above is " width) 20 40)))

