(ns test-server
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.core :as h]))

(def root-page
  (h/html
   [:head
    [:title "Quil tests"]
    [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]]
   [:div.centerLayer {:align "center"}
    [:p [:a {:href "/test.html"} "Common Quil API tests"]]
    [:p [:a {:href "/manual"} "Manual Quil API tests"]]]))

(defn gen-test-canvas [id]
  (h/html
   [:div.cbox
    [:p (str id " test")]
    [:canvas {:id id}]]))

(def manual-page
  (h/html
   [:head
    [:title "Manual Quil tests"]
    [:script {:type "text/javascript" :src "js/main.js"}]
    [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]]
   [:div.centerLayer {:align "center"}
    (gen-test-canvas "redraw-on-key")
    (gen-test-canvas "fun-mode")
    (gen-test-canvas "get-pixel")
    (gen-test-canvas "set-pixel")
    (gen-test-canvas "pixels-update-pixels")
    (gen-test-canvas "global-key-events")

    [:div.cbox
     [:p "extern-control test"]
     [:table
      [:tr
       [:td
        [:canvas {:id "external-control"}]]
       [:td
        [:p [:button {:onclick "snippets.manual.sketch_start('external-control')"} "Start"]]
        [:p [:button {:onclick "snippets.manual.sketch_stop('external-control')"} "Stop"]]]]]]]))

(defroutes app-routes
  (GET "/" req root-page)
  (GET "/manual" req manual-page)
  (route/files "/js" {:root "target/js"})
  (route/files "/" {:root "test/html"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
