(ns clojure-csv.core
  (:require [clojure.string :as str]
            [clojure.walk]
            [compojure.core :as comp]
            [ring.adapter.jetty :as ring]
            [hiccup.core :as hic]))

;defines a binding iff the variable has nil value
(defonce server (atom nil))

(defn gen-purchase []
  (println "Enter a category:")
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

        ;turns key strings into keys
        purchases (clojure.walk/keywordize-keys purchases)]))

        ;user input
        ;category (read-line)
        ;gets purchases by category
        ;purchases (filter (fn [purchase](= (get purchase :category) category)) purchases)]))
        ;prints out result
        ;file-text (pr-str purchases)]
    ;write to file by category name
    ;(spit (str "filtered_purchases.edn") file-text)
    ;(println purchases)))

;filters purchases based on category and builds html
(defn purchase-html [category]
  (let [purchases (gen-purchase) purchases (if (= 0 (count category))
        purchases (filter (fn [purchase] (= (get purchase :category)category))purchases))]))


(comp/defroutes webroot
     (comp/GET "/:category{.*}" [category]
                          ;if the text is blue & surrounded in parens, it calls as a function

                          ;this calls html forms, :html denotes a tag
                          (hic/html [:html [:body
                                    [:a {:href "Furniture "} :Furniture]
                                    [:a {:href "Alcohol "} :Alcohol]
                                    [:a {:href "Toiletries "} :Toiletries]
                                    [:a {:href "Shoes "} :Shoes]
                                    [:a {:href "Food "} :Food]
                                    [:a {:href "Jewelry "} :Jewelry]
                                            (purchase-html category)]])))

(defn -main []
  ;@ points to defonce binding
  (when @server
    (.stop @server))
  (reset! server (ring/run-jetty webroot {:port 3000 :join? false})))

