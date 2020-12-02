(ns micro-tools.sql-binding
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [micro-tools.io :as m-io]
            [micro-tools.string :as m-string]
            [micro-tools.core :refer [with-read-lines]]))
                  
(def config (load-file (m-io/resource "sql_binding/config.clj")))

(defn format-write [file-name & args]
  (let [sql (-> file-name
                slurp
                (string/replace (:param config) "%s"))
        name (.getName (io/file file-name))]
    (spit (m-io/resource (str "sql_binding/formatted_" name))
          (apply format sql args))))

(defn split-params [file-name]
  (for [s (-> file-name slurp (string/split #", "))]
    (let [param (-> s
                    (string/split #"\(")
                    (#(subvec % 0 (- (count %) 1)))
                    (#(string/join "(" %)))]
      (str "'" param "'"))))

(def origin-file-name (m-io/resource "sql_binding/origin.sql"))
(def params-file-name (m-io/resource "sql_binding/params.txt"))
(def raw-file-name (m-io/resource "sql_binding/raw.txt"))

(defn from-raw [file-name]
  (with-read-lines clojure.java.io/reader
    [lines file-name]
    (let [[sql-raw param-raw] (vec lines)
          sql (m-string/last-word sql-raw (config :sql-sep))
          param (m-string/last-word param-raw (config :param-sep))]
      (spit origin-file-name sql)
      (spit params-file-name param)
      (apply format-write
        origin-file-name
        (split-params params-file-name)))))

(defn run [] (from-raw raw-file-name))
