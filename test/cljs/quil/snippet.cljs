(ns quil.snippet
  (:require [quil.core :as q :include-macros true]
            [dommy.utils :as utils]
            [dommy.core :as d :include-macros true]
            [goog.events :as events]
            [goog.events.EventType :as EventType]))

(def test-data (atom (list)))

(def test-indx (atom 0))

(def failed (atom 0))
(def total (atom 0))

(defn log [& body]
  (.log js/console (apply str body)))

(defn ^:export print-data []
  (.log js/console (str @test-data)))

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

(defn run-single-test []
  (if (< @test-indx (count @test-data))
    (let [sketch (nth @test-data @test-indx)]
      (log-sketch-test sketch)
      (swap! total inc)

      (try
        ((:fn sketch))

        (catch js/Error e
          (log-sketch-passed sketch false)
          (swap! quil.snippet/failed inc)
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
        option (aget select (.-selectedIndex select))]
    ((:fn (.-testData option)))))

(defn init-test-selection [input]
  (doseq [[ns tests] (sort-by first (group-by :ns @test-data))]
    (let [optgroup (.createElement js/document "optgroup")]
      (set! (.-label optgroup) (subs ns (inc (count "snippet."))))
      (doseq [test tests]
        (let [option (.createElement js/document "option")]
          (set! (.-innerHTML option) (:name test))
          (set! (.-testData option) test)
          (.appendChild optgroup option)))
      (.appendChild input optgroup)))
  (events/listen input EventType/CHANGE
                 run-selected-test))

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
