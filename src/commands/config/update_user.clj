(ns commands.config.update-user
  (:require [cheshire.core :as json]))

(defn update-user [arguments options]
  (try
    (let [perfil (json/parse-string (slurp "perfil.json") true)
          data {:name (if (nil? (first arguments)) (:name perfil) (first arguments))
                :weight (if (nil? (second arguments)) (:weight perfil) (second arguments))}
          new_perfil (conj (conj perfil data) options)]
      (spit "perfil.json" (json/generate-string new_perfil)))

    (catch java.io.FileNotFoundException e
      {:sucesso false, :detalhe "Failed: User not create"})
    (catch Exception e
      {:sucesso false, :detalhe (.getMessage e)})))