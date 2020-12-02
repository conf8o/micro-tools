(ns micro-tools.string
  (:require [clojure.string :as string]))

(defn last-word [s sep]
  (last (string/split s sep)))
