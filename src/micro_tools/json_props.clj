(ns micro-tools.json-props
  (:require [clojure.string :as string]
            [micro-tools.io :as m-io]
            [micro-tools.io :as m-io]
            [micro-tools.core :refer [with-read-lines words]]))

(defn props [lines]
  (for [line lines
        :when (.contains line ":")]
    (-> line
        words
        first)))

(defn output-props []
  (with-read-lines clojure.java.io/reader
    [lines (m-io/resource "json_props/a.json")]
    (let [ps (string/join "\n" (props lines))]
      (spit (m-io/resource "json_props/out.txt") ps))))

(def run output-props)
