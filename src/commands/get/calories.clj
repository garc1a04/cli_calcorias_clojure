(ns commands.get.calories
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]
            [clojure.string :as str]))

(def api-url "http://0.0.0.0:3000")

(defn- add-req-food []
  (let [url (str api-url "/api/calories")
        result (http-client/get url :as :json)
        body (json/parse-string (:body result) true)]
    body))

(defn- filter-results [foods]
  (map (fn [item] (str (:type item) "," (:name item) "," (:kcal item) "," (:date item))) foods))

(defn add-food [arguments options]
    (let [body (add-req-food)]
      (println (str " \nResults: \n\n" (str/join ".\n" (filter-results (:data body)))))))