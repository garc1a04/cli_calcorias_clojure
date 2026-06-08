(ns cli-calcorias.cli
  (:require [commands.config.add-user :refer [add-user]]
            [commands.config.update-user :refer [update-user]]
            [clojure.tools.cli :refer [parse-opts]]))


(defn interpretar-opcoes [commands arg]
  (let [comando (get commands (first arg))
        subcomando (get (:subcommands comando) (second arg))
        rest-args (rest (rest arg))
        opts (parse-opts rest-args (:opts subcomando))]
    ((:fn subcomando) (:arguments opts) (:options opts))))

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
               :fn      update-user}}}})