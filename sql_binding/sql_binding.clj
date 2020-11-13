(require '[clojure.string :as string])

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
                  
(def config (load-file "config.clj"))

(defn format-write [file-name & args]
  (let [sql (-> file-name
                slurp
                (string/replace (config :param) "%s"))]
    (spit (str "formatted_" file-name)
          (apply format sql args))))

(defn split-params [file-name]
  (for [s (-> file-name slurp (string/split #", "))]
    (let [param (-> s
                    (string/split #"\(")
                    (#(subvec % 0 (- (count %) 1)))
                    (#(string/join "(" %)))]
      (str "'" param "'"))))

(def origin-file-name "origin.sql")
(def params-file-name "params.txt")

(defn last-word [s sep]
  (last (string/split s sep)))

(defn from-raw [file-name]
  (with-read-lines clojure.java.io/reader
    [lines file-name]
    (let [[sql-raw param-raw] (vec lines)
          sql (last-word sql-raw (config :sql-sep))
          param (last-word param-raw (config :param-sep))]
      (spit origin-file-name sql)
      (spit params-file-name param)
      (apply format-write
        origin-file-name
        (split-params params-file-name)))))