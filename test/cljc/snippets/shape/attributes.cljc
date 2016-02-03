(ns snippets.shape.attributes
  (:require #?(:cljs quil.snippet
               :clj [quil.snippet :refer [defsnippet]])
            [quil.core :as q :include-macros true])
  #?(:cljs
     (:use-macros [quil.snippet :only [defsnippet]])))


(defsnippet ellipse-mode {}
  (q/stroke-weight 5)
  (q/ellipse-mode :center)
  (q/with-translation [125 125]
   (q/ellipse 0 0 100 70)
   (q/point 0 0))

  (q/ellipse-mode :radius)
  (q/with-translation [375 125]
    (q/ellipse 0 0 100 70)
    (q/point 0 0))

  (q/ellipse-mode :corner)
  (q/with-translation [125 375]
    (q/ellipse 0 0 100 70)
    (q/point 0 0))

  (q/ellipse-mode :corners)
  (q/with-translation [375 375]
    (q/ellipse -50 -35 50 35)
    (q/point -50 -35)
    (q/point 50 35)))

(defsnippet smooth-no-smooth {:renderer :p2d
                              :settings (fn []
                                          (q/smooth 2)
                                          (q/no-smooth)
                                          (q/smooth 2))}
  (q/with-translation [125 125]
   (q/ellipse 0 0 200 200)))

(defsnippet rect-mode {}
  (q/stroke-weight 5)
  (q/rect-mode :center)
  (q/with-translation [125 125]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (q/rect-mode :radius)
  (q/with-translation [375 125]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (q/rect-mode :corner)
  (q/with-translation [125 375]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (q/rect-mode :corners)
  (q/with-translation [375 375]
    (q/stroke 0)
    (q/rect -50 -35 50 35)
    (q/stroke 255 0 0)
    (q/point -50 -35)
    (q/point 50 35)))

(defsnippet stroke-cap {}
  (q/stroke-weight 12)
  (q/stroke-cap :square)
  (q/line 230 200 270 200)

  (q/stroke-cap :project)
  (q/line 230 250 270 250)

  (q/stroke-cap :round)
  (q/line 230 300 270 300))

(defsnippet stroke-join {}
  (q/rect-mode :center)
  (q/stroke-weight 12)
  (q/stroke-join :miter)
  (q/rect 125 125 100 100)

  (q/stroke-join :bevel)
  (q/rect 375 125 100 100)

  (q/stroke-join :round)
  (q/rect 125 375 100 100))

(defsnippet stroke-weight {}
  (doseq [i (range 1 10)]
    (q/stroke-weight i)
    (q/line 230 (+ (* i 30) 100)
          270 (+ (* i 30) 100))))

