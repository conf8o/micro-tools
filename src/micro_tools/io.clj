(ns micro-tools.io
  (:require [clojure.java.io :as io]))

(defn resource [path]
  (.toString (.getPath (io/resource path))))