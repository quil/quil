(ns quil.test-helpers
  (:require [quil.core :as q :include-macros true]))

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
