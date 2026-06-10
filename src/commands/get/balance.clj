(ns commands.get.balance
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]))

(def api-url "http://0.0.0.0:3000")

(defn- get-req-balance []
  (let [url (str api-url "/api/calories/saldo")
        result (http-client/get url {:content-type :json
                                     :accept :json}
                                :as :json)
        body (json/parse-string (:body result) true)]
    (:data body)))


(defn get-balance [arguments options]
  (let [body (get-req-balance)]
    (println "Energy Balance:" (:energy_balance body))
    body))