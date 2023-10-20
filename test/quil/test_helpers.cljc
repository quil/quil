(ns quil.test-helpers
  #?(:clj
     (:require
      [quil.core :as q]
      [quil.applet])
     :cljs
     (:require
      [quil.core :as q :include-macros true]
      [quil.sketch])))

#?(:cljs
   (defn make-canvas [id]
     (let [canvas (.createElement js/document "canvas")]
       (.setAttribute canvas "id" id)
       (.appendChild (.-body js/document) canvas)
       canvas)))

#?(:cljs
   (defn test-sketch
     ([] (test-sketch (gensym "quil-sketch")))
     ([id] (q/sketch :host (make-canvas id))))

   :clj
   (defn test-sketch []
     (q/sketch)))

;; FIXME possibly unify this with quil.core/with-sketch
;; and possibly deprecate quil.applet/with-applet?
(defmacro with-sketch [applet & body]
  ;; BEWARE DRAGONS!
  ;; https://groups.google.com/g/clojurescript/c/iBY5HaQda4A/m/w1lAQi9_AwsJ
  ;; if &env has a ns, evaluate macros in cljs context
  (if (boolean (:ns &env))
    `(binding [quil.sketch/*applet* ~applet]
       ~@body)
    `(binding [quil.applet/*applet* ~applet]
       ~@body)))

(defn delta=
  ([a b] (delta= a b 1e-6))
  ([a b epsilon]
   (< (abs (- a b)) epsilon)))
