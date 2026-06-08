(ns parser.core
  (:require
   [parser.config :as config]))

(defn command [args]
  (let [command (first args)]
    (cond
      (= command "config") (config/interpretar-opcoes (rest args))
      (= command "add") (rest args)
      (= command "get") (rest args)
      :else
      nil)))

(defn interpretar-opcoes [argumentos]
  (command argumentos))