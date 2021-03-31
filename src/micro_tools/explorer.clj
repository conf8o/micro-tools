(ns micro-tools.explorer
  (:require [micro-tools.io :as m-io]
            [clojure.java.io :as io])
  (:import [java.awt Desktop]))

(defn config [] (m-io/load-data "explorer/config.clj"))

(def desktop (Desktop/getDesktop))

(defn open-folder [base k]
  (let [c (config)
        path (str (base c)
                  (k (:search c)))]
    (.open desktop (io/file path))))

(defn search-files [base pattern]
  (let [c (config)]
    (->> (vals (:search c))
         (map #(str (base c) %))
         (map #(m-io/files % pattern))
         (apply concat)
         (m-io/output-data "explorer/output.txt"))))
