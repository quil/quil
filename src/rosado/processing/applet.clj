(ns rosado.processing.applet
  (:use rosado.processing)
  (:import (java.awt Frame)))

(defmacro defapplet
  [name title setup draw width height]
  `(do
     (def ~name
          (proxy [processing.core.PApplet] []
            (setup []
                   (binding [*applet* ~'this]
                     (size ~width ~height)
                     (~setup)))
            (draw []
                  (binding [*applet* ~'this]
                    (~draw)))))
     (defn ~(symbol (str "run-" name)) []
       (.init ~name)
       (def ~(symbol (str name "-frame")) (doto (Frame. ~title)
                    (.setSize ~width ~height)
                    (.add ~name)
                    (.pack)
                    (.show))))

     (defn ~(symbol (str "stop-" name)) []
       (.destroy ~name)
       (.hide ~(symbol (str name "-frame"))))))
