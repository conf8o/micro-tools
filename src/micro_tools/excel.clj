(ns micro-tools.excel
  (:require [dk.ative.docjure.spreadsheet :as spreadsheet]
            [micro-tools.io :as m-io])
  (:import  [org.apache.poi.xssf.usermodel XSSFWorkbook XSSFSheet]
            [java.lang String]))

(defrecord TableInfo [mapping sheet-name])

(defmulti table (fn [info obj] (class obj)))
(defmethod table XSSFSheet [info sheet]
  (->> sheet
       (spreadsheet/select-columns (:mapping info))))
(defmethod table XSSFWorkbook [info workbook]
  (->> workbook
       (spreadsheet/select-sheet (:sheet-name info))
       (table info)))
(defmethod table String [info path]
  (->> path
       (spreadsheet/load-workbook)
       (table info)))

(defn run []
  (let [mapping 
        (m-io/load-data "excel/mapping.clj")
        
        [sheet-name file-name]
        (m-io/input-data "excel/input.txt")]
  (table
    (TableInfo. mapping sheet-name)
    file-name)))
