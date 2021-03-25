(ns micro-tools.json-props
  (:require [clojure.string :as string]
            [micro-tools.io :as m-io]
            [micro-tools.core :refer [words]]))

(defn props [lines]
  (for [line lines
        :when (.contains line ":")]
    (-> line
        words
        first)))

(defn output-props []
  (m-io/with-read-lines clojure.java.io/reader
    [lines (m-io/resource "json_props/a.json")]
    (m-io/output-data "json_props/out.txt" (props lines))))

(def run output-props)
