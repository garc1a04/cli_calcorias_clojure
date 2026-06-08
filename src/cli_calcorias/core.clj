(ns cli-calcorias.core
  (:require [cli-calcorias.cli :as cli]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (cli/interpretar-opcoes cli/comandos args))


(comment
  ;; Adicionar o usuário
  ;; PADRÃO: calcorias config set <name> <peso> [opções](O ano e o sexo são opcionais.)
  ;; PADRÃO: calcorias config update <name> <peso> [opções] todas as situações são opcionais.

  ;; Adicionar alimento
  ;; PADRÃO: calcorias add food <name> <grams>

  ;; Adicionar exercício
  ;; PADRÃO: calcorias add exercise <name> <minutes> (A atividade é salva apenas se tiver o usuário cadastrado com peso)

  ;; consultar calorias
  ;; PADRÃO: calcorias get calories <day or month> 

  ;; consultar saldo
  ;; PADRÃO: calcorias get balance <day or month> 
)