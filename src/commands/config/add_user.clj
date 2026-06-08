(ns commands.config.add-user
  (:require [cheshire.core :as json]))


(defn is-valid? [name weigth]
  (if (empty name)
    (throw (Exception. "Failed: Name is empty"))
    (try
      (Double/parseDouble weigth)
      {:sucesso true}
      (catch Exception e
        (throw (Exception. "Failed: weight is not a number"))))))

(defn add-user [arguments options]
  (if (< (count arguments) 2)
    (throw (Exception. "Failed: Invalid arguments. Use: config set <nome> <peso>"))
    (let [valid (is-valid? (first arguments) (second arguments))]
      (when (:sucesso valid)
        (let [data {:name (first arguments)
                    :weight (second arguments)}
              perfil (conj data options)]
          (spit "perfil.json" (json/generate-string perfil))))
      (:detalhe valid))))