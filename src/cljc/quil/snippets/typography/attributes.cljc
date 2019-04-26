(ns quil.snippets.typography.attributes
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet text-align
  "text-align"
  {}

  (q/fill 0)
  (let [h-align [:left :center :right]
        v-align [:top :bottom :center :baseline]]

    (comment "text-align with single argument")
    (doseq [ind (range (count h-align))
            :let [x 50
                  y (+ 20 (* ind 50))]]
      (q/text-align (h-align ind))
      (q/text (name (h-align ind)) x y)

      (q/push-style)
      (q/stroke 255 0 0)
      (q/stroke-weight 5)
      (q/point x y)
      (q/pop-style))

    (comment "text-align with multiple arguments")
    (doseq [ind-h (range (count h-align))
            ind-v (range (count v-align))
            :let [x (+ 70 (* ind-v 100))
                  y (+ 250 (* ind-h 50))
                  h-al (h-align ind-h)
                  v-al (v-align ind-v)
                  txt (str (name h-al) "+" (name v-al))]]
      (q/text-align h-al v-al)
      (q/text txt x y)

      (q/push-style)
      (q/stroke 255 0 0)
      (q/stroke-weight 5)
      (q/point x y)
      (q/pop-style))))

(defsnippet text-leading
  "text-leading"
  {}

  (q/fill 0)
  (doseq [ind (range 4)
          :let [leading (* ind 10)]]
    (q/text-leading leading)
    (q/text (str "text leading\n" leading) 20 (+ 20 (* ind 100)))))

#?(:clj
   (defsnippet text-mode
     "text-mode"
     {:renderer :p2d}

     (q/fill 0)
     (q/text-mode :model)
     (q/text "text-mode: model" 20 50)

     (q/text-mode :shape)
     (q/text "text-mode: shape" 20 100)))

(defsnippet text-size
  "text-size"
  {}

  (q/fill 0)
  (doseq [ind (range 6)
          :let [size (+ 10 (* ind 5))]]
    (q/text-size size)
    (q/text (str "Text size: " size) 20 (+ 20 (* ind 80)))))

#?(:cljs
   (defsnippet text-style
     "text-style"
     {}
     (q/fill 0)

     (q/text-style :normal)
     (q/text "text-style: normal" 20 50)

     (q/text-style :italic)
     (q/text "text-style: italic" 20 100)

     (q/text-style :bold)
     (q/text "text-style: bold" 20 150)

     (q/text-style :bolditalic)
     (q/text "text-style: bolditalic" 20 200)))

(defsnippet text-width
  "text-width"
  {}

  (q/fill 0)
  (let [txt "Hello, world!"
        width (q/text-width txt)]
    (q/text txt 20 20)
    (q/text (str "Width of text above is " width) 20 40)))

