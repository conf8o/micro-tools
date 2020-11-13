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

(defn words [s]
  (re-seq #"\w+" s))

(defn json-props [lines]
  (for [line lines
        :when (.contains line ":")]
    (-> line
        words
        first)))

(defn main-json-props []
  (with-read-lines clojure.java.io/reader
    [lines "a.json"]
    (let [props (clojure.string/join "\n" (json-props lines))]
      (spit "out.txt" props))))