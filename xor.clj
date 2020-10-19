(require 'clojure.set)

(defn xor
  ([coll1 coll2]
    (let [s1 (set coll1)
          s2 (set coll2)]
    (clojure.set/difference (clojure.set/union s1 s2)
                            (clojure.set/intersection s1 s2))))
  ([coll1 coll2 & more]
    (reduce xor (xor coll1 coll2) more)))