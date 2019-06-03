(ns quil.snippet
  (:require [quil.core :as q :include-macros true]
            [dommy.utils :as utils]
            [dommy.core :as d :include-macros true]
            [goog.events :as events]
            [goog.events.EventType :as EventType]
            [quil.snippets.all-snippets :as as]))

(def default-size [500 500])
(def default-host {:p2d "quil-test-2d"
                   :p3d "quil-test-3d"})

(def test-indx (atom 0))

(def failed (atom 0))
(def total (atom 0))

(defn snippet-to-test-function [snippet]
  {:name (:name snippet)
   :ns (:ns snippet)
   :skip-image-diff? (:skip-image-diff? snippet)
   :fn (fn []
         (let [expected (d/sel1 :#expected)]
           (d/set-attr! expected :src (str "snapshots/" (:name snippet) "-expected.png")))

         (let [opts (:opts snippet)]
           (q/sketch
            :size (:size opts default-size)
            :renderer (:renderer opts :p2d)
            :host (or (:host opts)
                      (get default-host (:renderer opts :p2d)))
            :setup (:setup snippet)
            :mouse-clicked (:mouse-clicked snippet)

            :draw (fn []
                    (try
                      ((:body snippet))
                      (catch js/Error e
                        (swap! failed inc)
                        (throw e)))))))})

(def test-functions (mapv snippet-to-test-function as/all-snippets))

(defn log [& body]
  (.log js/console (apply str body)))

(defn ^:export print-data []
  (.log js/console (str test-functions)))

(defn log-sketch-test [sketch]
  (log (str "Testing " (:ns sketch) "/" (:name sketch))))

(defn log-sketch-passed [sketch status]
  (log (str (:ns sketch) "/" (:name sketch) " is " (if status "passed." "failed."))))

(defn end-of-tests []
  (log "end of tests")
  (log "Total tested: " @total "   Accepted: " (- @total @failed) "   Failed: " @failed)

  (d/set-text! (d/sel1 :#results)
               (str "Total tested: " @total ";   Accepted: " (- @total @failed) ";   Failed: " @failed))
  (reset! test-indx 0)
  (reset! failed 0)
  (reset! total 0))

(defn remove-canvases []
   (doseq [c (d/sel [:canvas])]
     (d/remove! c)))

(defn run-single-test []
  (if (< @test-indx (count test-functions))
    (let [sketch (nth test-functions @test-indx)]
      (log-sketch-test sketch)
      (swap! total inc)

      (try
        (remove-canvases)
        ((:fn sketch))

        (catch js/Error e
          (log-sketch-passed sketch false)
          (swap! failed inc)
          (log e))

        (finally
          (swap! test-indx inc)
          (js/setTimeout run-single-test 500))))

    (js/setTimeout end-of-tests 500)))

(defn ^:export run-tests []
  (log "run tests...")

  (d/set-text! (d/sel1 :#results) "Test started")
  (reset! test-indx 0)
  (js/setTimeout run-single-test 100))

(defn run-selected-test [event]
  (let [select (.-target event)
        index (.-selectedIndex select)
        option (aget select index)]
    (remove-canvases)
    (aset js/location "hash" index)
    ((:fn (.-testData option)))))

(defn init-test-selection [input]
  (doseq [[ns tests] (sort-by first (group-by :ns test-functions))]
    (let [optgroup (.createElement js/document "optgroup")]
      (set! (.-label optgroup) (subs ns (count "quil.snippets.")))
      (doseq [test tests]
        (let [option (.createElement js/document "option")]
          (set! (.-innerHTML option) (:name test))
          (set! (.-testData option) test)
          (d/set-attr! option :data-skip-image-diff (:skip-image-diff? test))
          (.appendChild optgroup option)))
      (.appendChild input optgroup)))
  (events/listen input EventType/CHANGE
                 run-selected-test)
  (when-let [hash (aget js/location "hash")]
    (aset input "selectedIndex" (js/parseInt (subs hash 1)))
    (.dispatchEvent input (js/Event. EventType/CHANGE))))

(defn init []
  (when-let [input (.querySelector js/document "#test-select")]
    (init-test-selection input)))

(events/listenOnce js/window EventType/LOAD
                   #(when (= (-> js/document
                                 (.-body)
                                 (.-dataset)
                                 (aget "page"))
                             "automated")
                      (init)))
