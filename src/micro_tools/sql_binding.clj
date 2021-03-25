(ns micro-tools.sql-binding
  (:require [clojure.string :as string]
            [micro-tools.io :as m-io]
            [micro-tools.string :as m-string]))
                  
(def config (m-io/load-data "sql_binding/config.clj"))

(defn format-sql [sql & args]
  (let [sql-clj (string/replace sql (:param config) "%s")]
    (apply format sql-clj args)))

(defn split-params [raw-params]
  (for [s (string/split raw-params #", ")]
    (let [param (-> s
                    (string/split #"\(")
                    (#(subvec % 0 (- (count %) 1)))
                    (#(string/join "(" %)))]
      (str "'" param "'"))))

(defn from-raw [raw-log]
  (let [[sql-raw param-raw] (string/split raw-log #"\n")
        sql (m-string/last-word sql-raw (config :sql-sep))
        param (m-string/last-word param-raw (config :param-sep))]
    (apply format-sql
      sql
      (split-params param))))

(defn run []
  (let [raw-file-name (m-io/resource "sql_binding/raw.txt")
        formatted-file-name (m-io/resource "sql_binding/formatted.sql")]
  (spit formatted-file-name
        (from-raw (slurp raw-file-name)))))
