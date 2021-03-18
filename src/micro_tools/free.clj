(ns micro-tools.free
  (:require [micro-tools.io :as m-io]))

(defn after [] (load-file (m-io/resource "free/free.clj")))

(defn run []
  (let [x (after)]
    (->> (m-io/input-data "free/input.txt")
         (map (fn [y] (if (= y "1") x y)))
         (m-io/output-data "free/output.txt"))))
