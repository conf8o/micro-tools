(ns micro-tools.string
  (:require [clojure.string :as string]))

(defn words [s]
  (re-seq #"\w+" s))

(defn last-word [s sep]
  (last (string/split s sep)))
