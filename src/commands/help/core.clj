(ns commands.help.core)

(require '[clojure.string :as str])

(defn- formata-opcoes [opts]
  (->> opts
       (map (fn [[short long desc]]
              (format "        %s, %-15s %s" short long desc)))
       (str/join "\n")))

(defn- formata-linha-comando [{:keys [cmd subcmd summary usage opts]}]
  (let [partes     (remove str/blank? [cmd subcmd usage])
        cmd-str    (str/join " " partes)
        opcoes-str (if opts (str "\n      Options:\n" (formata-opcoes opts)) "")]
    (str "  " cmd-str "\n      " summary opcoes-str)))

(defn get-help [comandos]
  (let [lista-plana (mapcat (fn [[cmd info]]
                              (if-let [subcmds (:subcommands info)]
                                (map (fn [[subcmd subinfo]]
                                       (assoc subinfo :cmd cmd :subcmd subcmd))
                                     subcmds)
                                [(assoc info :cmd cmd)]))
                            comandos)]

    (println "Use: app <command> <subcommand> [options]\n")
    (println "Commands:")
    (println (str/join "\n\n" (map formata-linha-comando lista-plana)))))