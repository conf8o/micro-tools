(ns micro-tools.ut-resource
  (:require [clojure.java.io :as io]
            [micro-tools.io :as m-io])
  (:import [java.awt Desktop]))

(defn- match-path [re path]
  (->> path
       .toString
       (re-find re)))

(def desktop (Desktop/getDesktop))

(defn reveal-ut-resource
  "Reveals resources of UT in file explore."
  [folder-re]
  (let [{base-path :base-path} (load-file (m-io/resource "ut_resource/config.clj"))
        folder (->> (io/file base-path)
                    file-seq
                    (filter #(match-path folder-re %))
                    (filter #(.isDirectory %))
                    first)]
    (.open desktop folder)))

(defn last-folder []
  (load-file (m-io/resource "ut_resource/last_folder.clj")))

(defn save-last-folder [path]
  (spit (m-io/resource "ut_resource/last_folder.clj") (str "#\"" path "\"")))

(defn run-inner [folder]
  (do
    (reveal-ut-resource folder)
    (save-last-folder folder)))

(defn run 
  ([] 
    (run-inner (last-folder)))
  ([path]
    (run-inner path)))
