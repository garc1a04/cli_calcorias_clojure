(ns commands.save.exercise
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]
            [clojure.string :as str]))

(def api-url "http://0.0.0.0:3000")

(defn- add-req-exercise [name minutes]
  (let [url (str api-url "/api/user/burned")
        payload (json/generate-string {:name name
                                       :minutes (Integer/parseInt minutes)})
        result (http-client/post url
                                 {:body payload
                                  :content-type :json
                                  :accept :json}
                                 :as :json)
        body (json/parse-string (:body result) true)]
    (if (or  (> (count (:exercise body)) 0) (= (:message body) "sucess"))
      body
      (throw (Exception. "Exercise Not found")))))

(defn- valid? [arguments]
  (if (< (count arguments) 2)
    (throw (Exception. "Failed: Invalid arguments. Use: config set <name> <minutes>"))
    (try
      (Integer/parseInt (second arguments))
      true
      (catch Exception e
        (throw (Exception. "Failed: minutes is not a number"))))))

(defn- add-user [name weight]
  (let [url (str api-url "/api/user")
        payload (json/generate-string {:name name
                                       :weight weight})]
    (http-client/post url
                      {:body payload
                       :content-type :json
                       :accept :json}
                      :as :json)))

(defn user-create? []
  (try
    (let [perfil (json/parse-string (slurp "perfil.json") true)]
      (add-user (:name perfil) (:weight perfil))
      true)
    (catch Exception e
      (println e)
      (throw (Exception. "Failed: User not create")))))

(defn- filter-results [exercise]
  (map (fn [item] (:name item)) exercise))

(defn add-exercise [arguments options]
  (when (and (valid? arguments) (user-create?))
    (let [name  (first arguments)
          minutes (second arguments)
          body (add-req-exercise name minutes)]
      (if (= (:message body) "Many Results")
        (println (str " \nMultiple matches found. Please type the full name of one: \n\n" (str/join ".\n" (filter-results (:exercise body)))))
        (println "Exercise successfully added.")))))