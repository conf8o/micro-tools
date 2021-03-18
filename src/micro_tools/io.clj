(ns micro-tools.io
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn resource [path]
  (.toString (.getPath (io/resource path))))

(def reader io/reader)

(defn input-data [resource-path]
  (string/split (slurp (resource resource-path)) #"\r\n"))

(defn output-data [resource-path data]
  (spit (resource resource-path)
        (string/join "\n" data)))
