(ns micro-tools.name-map
  (:require [micro-tools.io :as m-io]))

(defn get-mapping []
  (m-io/load-data "name_map/mapping.clj"))

(defn converter [mapping]
  (fn [x] 
    (if-let [v (mapping x)]
      v
      x)))

(defn run []
  (let [f (converter (get-mapping))]
    (->> (m-io/input-data "name_map/input.txt")
         (map f)
         (m-io/output-data "name_map/output.txt"))))
