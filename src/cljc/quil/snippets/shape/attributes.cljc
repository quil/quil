(ns quil.snippets.shape.attributes
  (:require #?(:clj [quil.snippets.macro :refer [defsnippet]])
            [quil.core :as q :include-macros true]
            quil.snippets.all-snippets-internal)
  #?(:cljs
     (:use-macros [quil.snippets.macro :only [defsnippet]])))

(defsnippet ellipse-mode
  "ellipse-mode"
  {}

  (q/stroke-weight 5)

  (comment "use :center mode")
  (q/ellipse-mode :center)
  (q/with-translation [125 125]
    (q/ellipse 0 0 100 70)
    (q/point 0 0))

  (comment "use :radius mode")
  (q/ellipse-mode :radius)
  (q/with-translation [375 125]
    (q/ellipse 0 0 100 70)
    (q/point 0 0))

  (comment "use :corner mode")
  (q/ellipse-mode :corner)
  (q/with-translation [125 375]
    (q/ellipse 0 0 100 70)
    (q/point 0 0))

  (comment "use :corners mode")
  (q/ellipse-mode :corners)
  (q/with-translation [375 375]
    (q/ellipse -50 -35 50 35)
    (q/point -50 -35)
    (q/point 50 35)))

;; FIXME: Disabling no-smooth to only test (q/smooth 2) for now
;; as no-smooth is throwing a NPE on
;; Exception in  :settings  function:  #error {
;;  :cause Cannot invoke "processing.core.PGraphics.noSmooth()" because the return value of "clojure.lang.IFn.invoke()" is null
;;  :via
;;  [{:type java.lang.NullPointerException
;;    :message Cannot invoke "processing.core.PGraphics.noSmooth()" because the return value of "clojure.lang.IFn.invoke()" is null
;;    :at [quil.core$no_smooth invokeStatic core.cljc 2844]}]
;;  :trace
;;  [[quil.core$no_smooth invokeStatic core.cljc 2844]
;;   [quil.core$no_smooth invoke core.cljc 2834]
;;   [quil.snippets.shape.attributes$eval2772$fn__2777 invoke attributes.cljc 42]
;;   [quil.snippet$run_snippet_as_test$fn__11434 invoke snippet.clj 66]
;;   [quil.middlewares.safe_fns$wrap_fn$fn__229 invoke safe_fns.clj 8]
;;   [quil.middlewares.bind_output$bind_output$iter__267__271$fn__272$fn__287 invoke bind_output.clj 21]
;;   [quil.applet$_settings invokeStatic applet.clj 202]
;;   [quil.applet$_settings invoke applet.clj 190]
;;   [quil.Applet settings nil -1]
;;   [processing.core.PApplet handleSettings PApplet.java 975]
;;   [processing.core.PApplet runSketch PApplet.java 10890]
;;   [quil.applet$applet_run invokeStatic applet.clj 81]
;;   [quil.applet$applet_run invoke applet.clj 78]
(defsnippet smooth-no-smooth
  ["smooth" "no-smooth"]
  {:renderer :p2d
   :settings (fn [] (q/smooth 2))}

  (comment "that's a bad example. smooth is odd")
  (q/with-translation [125 125]
    (q/ellipse 0 0 200 200)))

(defsnippet rect-mode
  "rect-mode"
  {}

  (q/stroke-weight 5)

  (comment "use :center mode")
  (q/rect-mode :center)
  (q/with-translation [125 125]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (comment "use :radius mode")
  (q/rect-mode :radius)
  (q/with-translation [375 125]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (comment "use :corner mode")
  (q/rect-mode :corner)
  (q/with-translation [125 375]
    (q/stroke 0)
    (q/rect 0 0 100 70)
    (q/stroke 255 0 0)
    (q/point 0 0))

  (comment "use :corners mode")
  (q/rect-mode :corners)
  (q/with-translation [375 375]
    (q/stroke 0)
    (q/rect -50 -35 50 35)
    (q/stroke 255 0 0)
    (q/point -50 -35)
    (q/point 50 35)))

(defsnippet stroke-cap
  "stroke-cap"
  {}

  (q/stroke-weight 12)
  (comment "use :square cap")
  (q/stroke-cap :square)
  (q/line 230 200 270 200)

  (comment "use :project cap")
  (q/stroke-cap :project)
  (q/line 230 250 270 250)

  (comment "use :round cap")
  (q/stroke-cap :round)
  (q/line 230 300 270 300))

(defsnippet stroke-join
  "stroke-join"
  {}

  (q/rect-mode :center)
  (q/stroke-weight 12)

  (comment "use :miter join")
  (q/stroke-join :miter)
  (q/rect 125 125 100 100)

  (comment "use :bevel join")
  (q/stroke-join :bevel)
  (q/rect 375 125 100 100)

  (comment "use :round join")
  (q/stroke-join :round)
  (q/rect 125 375 100 100))

(defsnippet stroke-weight
  "stroke-weight"
  {}

  (doseq [i (range 1 10)]
    (q/stroke-weight i)
    (q/line 230 (+ (* i 30) 100)
            270 (+ (* i 30) 100))))

