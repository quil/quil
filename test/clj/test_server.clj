(ns test-server
  (:require ring.util.response
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]))

(defroutes app-routes
  (compojure.core/GET "/" req (ring.util.response/redirect "/test.html"))
  (route/files "/js" {:root "target/js"})
  (route/files "/" {:root "test/html"})
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
