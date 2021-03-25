(ns micro-tools.table-filter
  (:require [micro-tools.io :as m-io]
            [clojure.string :as string]))

(def config (m-io/load-data "table_filter/config.clj"))

(defn table-seq [s]
  (re-seq (:table-re config) s))

(defn run []
  "Gets table names from log."
  (let [raw (slurp (m-io/resource "table_filter/raw.txt"))
        tables (set (table-seq raw))]
    (m-io/output-data "table_filter/out.txt"
                      (string/join "\n" (sort tables)))))
