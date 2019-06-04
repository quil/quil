(ns test-server
  (:require [quil.test-util :as tu]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.page :as h]))

(def root-page
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Quil tests"]
    [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]]
   [:div.centerLayer {:align "center"}
    [:p [:a {:href "/test.html"} "Common Quil API tests"]]
    [:p [:a {:href "/manual"} "Manual Quil API tests"]]
    [:p [:a {:href "/fullscreen"} "Fullscreen Manual test"]]]))

(defn gen-test-container
  ([id doc]
   (gen-test-container id doc nil))
  ([id doc control]
   [:div.cbox
    [:p.test-name [:h3  (str id " test")]]
    [:p.test-doc doc]
    control
    [:div {:id id}]]))

(def manual-page
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Manual Quil tests"]
    [:script {:type "text/javascript" :src "js/main.js"}]
    [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]]
   [:body {:data-page "manual"}
    [:div.centerLayer {:align "center"}
     (gen-test-container
      "redraw-on-key"
      "Sketch should show current time but update it only on key press.")

     (gen-test-container
      "fun-mode"
      "Sketch should show recent events like mouse move, click, key click, etc.")

     (gen-test-container
      "get-pixel"
      "Sketch should show 2 colored ellipses. 1 colored and 1 white rectangular. 1 quarter ellipse.")

     (gen-test-container
      "set-pixel"
      "Sketch should show 2 rectangulars. The first one is green-red. The second one is blue-green.")

     (gen-test-container
      "pixels-update-pixels"
      "Sketch should show a bunch of white dots in weird pattern.")

     (gen-test-container
      "mouse-and-key-pressed-variable"
      "Sketch should show whether mouse or key are pressed.")

     (gen-test-container
      "external-control"
      "Sketch should start/stop when on buttons click."
      [:p.controls
       [:button {:id "external-control-start"} "Start"]
       [:button {:id "external-control-stop"} "Stop"]
       [:p "Looping: " [:span {:id "looping-status"} "true"]]])

     (gen-test-container
      "resizing"
      (str
       "Sketch should show width number. Upon clicking on button sketch should increase width "
       "and then after 1 sec back to original 500px width.")
      [:p.controls
       [:button {:id "resize-button"} "Resize"]])]]))

(def fullscreen-page
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Fullscreen Quil tests"]
    [:script {:type "text/javascript" :src "js/main.js"}]
    [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]]
   [:body {:data-page "fullscreen"}
    [:div.centerLayer {:align "center"}
     (gen-test-container
      "fullscreen"
      "Trigger fullscreen by clicking F11. Exit from it by clicking Esc. Size should change")]]))

(defroutes app-routes
  (GET "/" req root-page)
  (GET "/manual" req manual-page)
  (GET "/fullscreen" req fullscreen-page)
  (route/files "/snapshots" {:root (str "dev-resources/" (tu/path-to-snippet-snapshots "cljs"))})
  (route/files "/js" {:root "target/js"})
  (route/files "/" {:root "test/html"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
