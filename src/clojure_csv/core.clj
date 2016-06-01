(ns clojure-csv.core
  (:require [clojure.string :as str])
  (:require [clojure.walk :as keywordify]))


(defn -main []
  (println "Enter a country name:")
  ;slurp reads files
  (let [purchases (slurp "purchases.csv")
        ;splits by line
        purchases (str/split-lines purchases)
        ;splits lines by comma delimiter
        purchases (map (fn [line](str/split line #","))purchases)

        ;gets header line
        headers (first purchases)
        ;gets rest of lines after header
        purchases (rest purchases)
        ;creates hashmap from purchases on headers
        purchases (map (fn [purchase](zipmap headers purchase))purchases)

        purchases (clojure.walk/keywordize-keys purchases)
        ;user input
        category (read-line)
        ;gets purchases by category
        purchases (filter (fn [purchase](= (get purchase "category") category))purchases)
        ;prints out result
        file-text (pr-str purchases)]
    ;write to file by category name
    (spit (str category ".edn") file-text)))




