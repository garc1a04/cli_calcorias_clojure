(ns cli-calcorias.cli
  (:require [commands.config.add-user :refer [add-user]]
            [commands.config.update-user :refer [update-user]]
            [commands.save.food :refer [add-food]]
            [commands.save.exercise :refer [add-exercise]]
            [commands.get.calories :refer [get-calories]]
            [commands.get.balance :refer [get-balance]]
            [commands.help.core :refer [get-help]]
            [clojure.tools.cli :refer [parse-opts]]))

(def comandos
  {"config"
   {:summary    "Adds or updates user information"
    :subcommands
    {"set"    {:summary "Registers a new user"
               :usage    "<name> <weight>"
               :opts    [["-a" "--age AGE" "User's age"
                          :id :age]

                         ["-s" "--sex SEX" "User's sex (M or F)"
                          :id :sex]]
               :fn      add-user}

     "update" {:summary "Updates an existing user's data"
               :usage    "<name> <weight>"
               :opts    [["-a" "--age AGE" "User's age"
                          :id :age]

                         ["-s" "--sex SEX" "User's sex (M or F)"
                          :id :sex]]
               :fn      update-user}}}
   "add"
   {:summary    "Logs food consumption or physical exercise"
    :subcommands
    {"food"    {:summary "Logs a consumed food item"
                :usage    "<name> <grams>"
                :fn      add-food}

     "exercise" {:summary "Logs a performed physical exercise"
                 :usage    "<name> <minutes>"
                 :fn      add-exercise}}}
   "get"
   {:summary    "Retrieves recorded data and history"
    :subcommands
    {"calories"    {:summary "Displays the user's calorie history by period"
                    :usage    "<name> <grams>"
                    :opts [["-p" "--period day" "Filters by period ('day' or 'month')"
                            :id :period]]
                    :fn      get-calories}

     "balance" {:summary "Displays the user's caloric balance (intake vs. expenditure)"
                :usage    "<name> <minutes>"
                :opts [["-p" "--period day" "Filters by period ('day' or 'month')"
                        :id :period]]
                :fn      get-balance}}}
   "help" {:summary "Displays this help menu"
           :fn      get-help}})

(defn interpretar-opcoes [commands arg]
  (let [cmd-name (first arg)
        sub-name (second arg)
        comando (get commands cmd-name)
        subcomando (get (:subcommands comando) sub-name)]
    (cond
      (nil? comando) ((:fn (get commands "help")) commands)
      (= cmd-name "help") ((:fn comando) commands)
      (nil? subcomando) (println "Subcomando inválido para" cmd-name ":" sub-name)
      :else
      (let [rest-args (drop 2 arg)
            opts (parse-opts rest-args (:opts subcomando))]
        ((:fn subcomando) (:arguments opts) (:options opts))))))
