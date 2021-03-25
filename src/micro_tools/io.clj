(ns micro-tools.io
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defmacro with-read-lines
  [reader bindings & body]
  (cond
    (= (count bindings) 0) `(do ~@body)
    (symbol? (bindings 0)) `(let* [rdr# (~reader ~(bindings 1))
                                   ~(bindings 0) (line-seq rdr#)]
                              (try
                                (with-read-lines ~reader ~(subvec bindings 2) ~@body)
                                (finally
                                  (. rdr# close))))
    :else (throw (IllegalArgumentException.
                   "with-read-lines only allows Symbols in bindings"))))

(defn resource [path]
  (.toString (.getPath (io/resource path))))

(defn input-data [resource-path]
  (string/split (slurp (resource resource-path)) #"\r\n"))

(defn output-data [resource-path data]
  (spit (resource resource-path)
        (if (coll? data)
            (string/join "\n" data)
            data)))

(defn load-data [resource-path]
  (load-file (resource resource-path)))

(defn files [dir pattern]
  (->> dir
       io/file
       file-seq
       (map #(.toString (.getFileName (.toPath %))))
       (filter #(re-find pattern %))))
