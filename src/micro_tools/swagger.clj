(ns micro-tools.swagger
  (:require [micro-tools.io :as m-io]
            [clojure.string :as string]))

(defprotocol Yaml
  (->yaml [this]))

(defrecord Property [name type description]
  Yaml
  (->yaml [this]
    (string/join "\n"
      (list
        (format "%s:" (:name this))
        (if-let [t (:type this)]        (format "  type: %s" t) "")
        (if-let [d (:description this)] (format "  description: %s" d) "")))))

(defn property [name type description]
  (Property. name type description))

(defn run []
  (let [data (m-io/input-data "swagger/input.txt")]
    (m-io/output-data "swagger/output.txt"
      (for [line data]
        (->> (string/split line #"\t")
             (filter #(not (empty? %)))
             (#(apply property %))
             ->yaml)))))
