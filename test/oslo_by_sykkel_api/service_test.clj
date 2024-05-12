(ns oslo-by-sykkel-api.service-test
  (:require [clojure.test :refer :all]
            [oslo-by-sykkel-api.core :as core]))


;; Should read from file
(defonce stations-status (slurp "https://gbfs.urbansharing.com/oslobysykkel.no/station_status.json"))
(defonce stations-info (slurp "https://gbfs.urbansharing.com/oslobysykkel.no/station_information.json"))

(deftest station-status-test
  (testing
   (with-redefs [core/fetch-data* (constantly stations-status)]
     (is (= 200
            (:status (core/station-status)))))
    (with-redefs [core/fetch-data* (constantly nil)]
      (is (= 404
             (:status (core/station-status)))))))

(deftest station-status-by-id-test
  (let [station-id (->> (core/parse-data stations-status)
                        (first)
                        :station_id)]
    (testing
     (with-redefs [core/fetch-data* (constantly stations-status)]
       (is (= 200
              (:status (core/station-status-by-id station-id)))))
      (with-redefs [core/fetch-data* (constantly nil)]
        (is (= 404
               (:status (core/station-status-by-id station-id)))))
      (with-redefs [core/fetch-data* (constantly nil)]
        (is (= 404
               (:status (core/station-status-by-id "TaylorSwift"))))))))


(deftest station-info-test
  (testing
   (with-redefs [core/fetch-data* (constantly stations-info)]
     (is (= 200
            (:status (core/station-info)))))
    (with-redefs [core/fetch-data* (constantly nil)]
      (is (= 404
             (:status (core/station-info)))))))

(deftest station-info-by-id-test
  (let [station-id (->> (core/parse-data stations-info)
                        (first)
                        :station_id)]
    (testing
     (with-redefs [core/fetch-data* (constantly stations-info)]
       (is (= 200
              (:status (core/station-info-by-id station-id)))))
      (with-redefs [core/fetch-data* (constantly nil)]
        (is (= 404
               (:status (core/station-info-by-id station-id)))))
      (with-redefs [core/fetch-data* (constantly nil)]
        (is (= 404
               (:status (core/station-info-by-id "DojaCat"))))))))