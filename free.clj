(require '[clojure.string :as string])
(require 'clojure.set)

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

(defn java-props [lines]
  (for [line lines
        :when (.contains line "private")]
    (-> line
        words
        last)))

(defn xor
  ([coll1 coll2]
    (let [s1 (set coll1)
          s2 (set coll2)]
      (clojure.set/difference (clojure.set/union s1 s2)
                              (clojure.set/intersection s1 s2))))
  ([coll1 coll2 & more]
    (reduce xor (xor coll1 coll2) more)))


(defn a-props [a] (->> a
                  (filter #(not (empty? %)))
                  (map #(first (words %)))))

(defn b-props [b] (->> b
                  (filter #(not (empty? %)))
                  java-props))

(defn main []
  (with-read-lines clojure.java.io/reader
    [a "a.txt"
     b "b.txt"]
    (xor (a-props a) (b-props b))))

