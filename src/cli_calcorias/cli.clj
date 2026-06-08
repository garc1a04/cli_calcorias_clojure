(ns cli-calcorias.cli
  (:require [commands.config.add-user :refer [add-user]]
            [commands.config.update-user :refer [update-user]]
            [commands.save.food :refer [add-food]]
            [clojure.tools.cli :refer [parse-opts]]))

(defn interpretar-opcoes [commands arg]
  (let [cmd-name (first arg)
        sub-name (second arg)
        comando (get commands cmd-name)
        subcomando (get (:subcommands comando) sub-name)]
    (cond
      (nil? comando) (println "Comando desconhecido:" cmd-name)
      (nil? subcomando) (println "Subcomando inválido para" cmd-name ":" sub-name)
      :else
      (let [rest-args (drop 2 arg)
            opts (parse-opts rest-args (:opts subcomando))]
        ((:fn subcomando) (:arguments opts) (:options opts))))))

(def comandos
  {"config"
   {:summary    "Adiciona ou atualiza usuários"
    :subcommands
    {"set"    {:summary "Adiciona o usuário"
               :usage    "<name> <weigth>"
               :opts    [["-a" "--age AGE" "Idade do usuário (opcional)"
                          :id :age]

                         ["-s" "--sex SEX" "Sexo do usuário (M ou F) (opcional)"
                          :id :sex]]
               :fn      add-user}

     "update" {:summary "Atualiza um usuário"
               :usage    "<name> <weigth> (Opcional)"
               :opts    [["-a" "--age AGE" "Idade do usuário (opcional)"
                          :id :age]

                         ["-s" "--sex SEX" "Sexo do usuário (M ou F) (opcional)"
                          :id :sex]]
               :fn      update-user}}}
   "add"
   {:summary    "Adiciona comidas ou atividades"
    :subcommands
    {"food"    {:summary "Adiciona comida"
                :usage    "<name> <grams>"
                :fn      add-food}

     "exercise" {:summary "Adiciona atividades"
                 :usage    "<name> <minutes>"
                 :fn      update-user}}}})