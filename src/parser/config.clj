(ns parser.config
  (:require
   [clojure.tools.cli :refer [parse-opts]]
   [cheshire.core :as json]))


;; ATUALIZAR PARA UTILIZAR O DISPATCH DE DADOS
(def config-cli-options
  [["-a" "--age AGE" "Idade do usuário (opcional)"
    :id :age]

   ["-s" "--sex SEX" "Sexo do usuário (M ou F) (opcional)"
    :id :sex]])

(defn is-valid? [name weigth]
  (if (empty name)
    {:sucesso false, :detalhe "Failed: Name is empty"}
    (try
      (Double/parseDouble weigth)
      {:sucesso true}
      (catch Exception e
        {:sucesso false, :detalhe "Failed: weight is not a number"}))))

(defn sucess []
  (println "User sucessful create"))

(defn add-user [arguments options]
  (if (< (count arguments) 2)
    {:sucesso false, :detalhe "Failed: Invalid arguments. Use: config set <nome> <peso>"}
    (let [valid (is-valid? (first arguments) (second arguments))]
      (when (:sucesso valid)
        (let [data {:name (first arguments)
                    :weight (second arguments)}
              perfil (conj data options)]
          (spit "perfil.json" (json/generate-string perfil))
          (sucess)))
      (:detalhe valid))))

(defn add-user [arguments options]
  (if (< (count arguments) 2)
    {:sucesso false, :detalhe "Failed: Invalid arguments. Use: config set <nome> <peso>"}
    (let [valid (is-valid? (first arguments) (second arguments))]
      (when (:sucesso valid)
        (let [data {:name (first arguments)
                    :weight (second arguments)}
              perfil (conj data options)]
          (spit "perfil.json" (json/generate-string perfil))
          (sucess)))
      (:detalhe valid))))

(defn update-user [arguments options]
  (try
    (let [perfil (json/parse-string (slurp "perfil.json") true)
          data {:name (if (nil? (first arguments)) (:name perfil) (first arguments))
                :weight (if (nil? (second arguments)) (:weight perfil) (second arguments))}
          new_perfil (conj (conj perfil data) options)]
      (spit "perfil.json" (json/generate-string new_perfil))
      )
      
    (catch java.io.FileNotFoundException e
      {:sucesso false, :detalhe "Failed: User not create"})
    (catch Exception e
      {:sucesso false, :detalhe (.getMessage e)})))

(defn command [args]
  (let [command (first args)]
    (cond
      (= command "set") (let [commands (parse-opts (rest args) config-cli-options)]
                          (add-user (:arguments commands) (:options commands)))
      (= command "update") (let [commands (parse-opts (rest args) config-cli-options)]
                             (update-user (:arguments commands) (:options commands)))
      :else nil)))

(defn interpretar-opcoes [argumentos]
  (println (:detalhe (command argumentos))))