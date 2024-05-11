(ns oslo-by-sykkel-api.core
  (:require [compojure.core :refer :all]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer :all]
            [clj-http.client :as http]
            [cheshire.core :as ch]
            [org.httpkit.server :as server])
  (:gen-class))


(defonce BIKE_STATUS_URL "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json" )
(defonce BIKE_INFO_URL "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json" )

(defn fetch-data [url]
  (-> (http/get url {:as :edn})
      :body
      (ch/parse-string true)
      :data
      :stations))

(defn filter-by-id-pred [station_id]
  (fn [station] (= station_id (:station_id station))))

(defn station-status "station-status"
  []
  (let [stations-statuses (fetch-data BIKE_STATUS_URL)]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/write-str stations-statuses)}))

(defn station-status-by-id "station-status-by-id"
  [station_id]
  (let [stations-statuses (->> (fetch-data BIKE_STATUS_URL)
                               (filter (filter-by-id-pred station_id))
                               (first))]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str stations-statuses)}))

(defn station-info "station-info"
  []
  (let [stations-info (fetch-data BIKE_INFO_URL)]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str stations-info)}))

(defn station-info-by-id "station-info-by-id"
  [station_id]
  (let [stations-info (->> (fetch-data BIKE_INFO_URL)
                           (filter (filter-by-id-pred station_id))
                           (first))]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (json/write-str stations-info)}))

(defroutes app-routes
           (GET "/status/" [] (station-status))
           (GET "/status/:station_id" [station_id] (station-status-by-id station_id))
           (GET "/info/" [] (station-info))
           (GET "/info/:station_id" [station_id] (station-info-by-id station_id)))

(defn -main
  "Main Program"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running the server at http://localhost:" port "/"))))
