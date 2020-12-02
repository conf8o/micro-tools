(ns micro-tools.java-props
  (:require [clojure.string :as string]
            [micro-tools.io :as m-io]
            [micro-tools.core :refer [with-read-lines words]]
            [micro-tools.set :refer [xor]]))

(defn java-prop [line]
  (.contains line "static"))

(defn java-props [lines]
  (for [line lines
        :when (java-prop line)]
    (-> line
        words
        vec)))

(defn logical-props [parts]
  (for [[logical prop] (partition 2 parts)]
    (let [j (-> prop words vec)]
    [(j 4) (last j) ((string/split logical #" ") 1)])))

(defn a-props [a] (->> a
                       (filter #(not (empty? %)))
                       logical-props))

(defn b-props [b] (->> b
                       (filter #(not (empty? %)))
                       java-props))

(defn source-to-dd
  "A script to reverse engineering for source code to DD"
  []
  (with-read-lines clojure.java.io/reader
    [a_ (m-io/resource "java_props/new.txt")
     b_ (m-io/resource "java_props/old.txt")
     c (m-io/resource "java_props/c.txt")]
    (let [a (vec (a-props a_))
          b (vec (b-props b_))
          props-a (map first a)
          values-a (map #(% 1) a)
          values-logi (map last a)
          props-b (map #(% 4) b)
          values-b (map last b)
          a-map (zipmap props-a values-a)
          logi-map (zipmap props-a values-logi)
          b-map (zipmap props-b values-b)]
      (spit (m-io/resource "java_props/out.txt")
        (string/join "\n"
          (map 
            (fn [line]
              (let [[x y] (string/split line #",")]
                (str (logi-map x) "(= " (a-map x) ")\t" (b-map y))))
            c))))))

(def run source-to-dd)
