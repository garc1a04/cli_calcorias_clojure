(ns commands.save.food
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]
            [clojure.string :as str]))

(def api-url "http://0.0.0.0:3000")

(defn- add-req-food [name grams]
  (let [url (str api-url "/api/food")
        payload (json/generate-string {:name name
                                       :grams (Integer/parseInt grams)})
        result (http-client/post url
                                 {:body payload
                                  :content-type :json
                                  :accept :json}
                                 :as :json)
        body (json/parse-string (:body result) true)]
    (if (> (count (:foods body)) 0)
      body
      (throw (Exception. "Food Not found")))))

(defn- valid? [arguments]
  (if (< (count arguments) 2)
    (throw (Exception. "Failed: Invalid arguments. Use: config set <name> <grams>"))
    (try
      (Integer/parseInt (second arguments))
      true
      (catch Exception e
        (throw (Exception. "Failed: grams is not a number"))))))

(defn- filter-results [foods]
  (map (fn [item] (:name item)) foods))

(defn add-food [arguments options]
  (when (valid? arguments)
    (let [name  (first arguments)
          grams (second arguments)
          body (add-req-food name grams)]

      (if (= (:message body) "Many results")
        (println (str " \nMultiple matches found. Please type the full name of one: \n\n" (str/join ".\n" (filter-results (:foods body)))))
        (println "Food successfully added.")))))