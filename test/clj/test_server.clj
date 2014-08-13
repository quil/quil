(ns test-server
  (:require ring.util.response
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.core :as h]))

(def root-page
  (h/html
   [:head
    [:title "Quil tests"]]
   [:p [:a {:href "/test.html"} "Common Quil API tests"]]
   [:p [:a {:href "/manual"} "Manual Quil API tests"]]))

(defn gen-test-canvas [id]
  (h/html
   [:div
    [:canvas {:id id :width 500 :height 500}]]))

(def manual-page
  (h/html
   [:head
    [:title "Manual Quil tests"]
    [:script {:type "text/javascript" :src "js/main.js"}]]
   [:p "Under construction"]
   (gen-test-canvas "redraw-on-key")
   (gen-test-canvas "fun-mode")))

(defroutes app-routes
  (GET "/" req root-page)
  (GET "/manual" req manual-page)
  (route/files "/js" {:root "target/js"})
  (route/files "/" {:root "test/html"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
