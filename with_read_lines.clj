(defn read-lines [file-name]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (vec (line-seq rdr))))

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

(with-read-lines clojure.java.io/reader
  [a "a.txt" b "b.txt"]
  (println (vec a))
  (println (vec b)))