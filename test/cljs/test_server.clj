(ns test-server
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [hiccup.page :as h]))

(defn header [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:title title]
   [:script {:type "text/javascript" :src "js/main.js"}]
   [:link {:rel "stylesheet" :type "text/css" :href "style/style.css"}]])

(def root-page
  (h/html5
   (header "Quil tests")
   [:body {:data-page "root"}
    [:div.centerLayer {:align "center"}
     [:p [:a {:href "/test.html"} "Common Quil API tests"]]
     [:p [:a {:href "/manual"} "Manual Quil API tests"]]
     [:p [:a {:href "/fullscreen"} "Fullscreen Manual test"]]]]))

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
   (header "Manual Quil tests")
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
   (header "Fullscreen Quil tests")
   [:body {:data-page "fullscreen"}
    [:div.centerLayer {:align "center"}
     (gen-test-container
      "fullscreen"
      "Trigger fullscreen by clicking F11. Exit from it by clicking Esc. Size should change")]]))

;; FIXME: duplicated from quil.snippets.test-helper, but lein tests are in
;; test/clj not test/quil, so not accessible in this context yet. Remove after
;; web server runs from deps.edn.
(defn path-to-snippet-snapshots [platform]
  (str "dev-resources/snippet-snapshots/" platform "/normal/"))

(defroutes app-routes
  (GET "/" _req root-page)
  (GET "/manual" _req manual-page)
  (GET "/fullscreen" _req fullscreen-page)
  (route/files "/snapshots" {:root (path-to-snippet-snapshots "cljs")})
  ;; Figwheel runs with deps.edn so clojure.basis will be specified
  (route/files "/js" {:root (if (System/getProperty "clojure.basis")
                              "target/public/cljs-out"
                              "target/js")})
  (route/files "/" {:root "test/html"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
