(ns commands.get.calories
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]
            [clojure.string :as str]))

(def api-url "http://0.0.0.0:3000")

(defn- get-req-calories [period]
  (let [url (str api-url "/api/calories")
        result (http-client/get url {:query-params {:period period}
                                     :content-type :json
                                     :accept :json}
                                :as :json)
        body (json/parse-string (:body result) true)]
    body))

(defn- filter-results [foods]
  (map (fn [item] (str (:type item) ", \"" (:name item) "\", " (:kcal item) ", " (:date item))) foods))

(defn get-calories [arguments options]
  (let [body (get-req-calories (:period options))]
    (println (str " \nResults: \n\ntype, name, kcal, date\n\n" (str/join ".\n" (filter-results (:data body)))))))