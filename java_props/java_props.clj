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

(defn xor
  ([coll1 coll2]
    (let [s1 (set coll1)
          s2 (set coll2)]
      (clojure.set/difference (clojure.set/union s1 s2)
                              (clojure.set/intersection s1 s2))))
  ([coll1 coll2 & more]
    (reduce xor (xor coll1 coll2) more)))


; 以下スクリプト

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
    [(j 4) (last j) ((clojure.string/split logical #" ") 1)])))

(defn a-props [a] (->> a
                       (filter #(not (empty? %)))
                       logical-props))

(defn b-props [b] (->> b
                       (filter #(not (empty? %)))
                       java-props))

; (defn main-xor-ab []
;   (with-read-lines clojure.java.io/reader
;     [a "a.txt"
;      b "b.txt"]
;     (xor (a-props a) (b-props b))))

(defn main-static-map []
  "javaクラスから詳細設計書用の表記にリバース"
  (with-read-lines clojure.java.io/reader
    [a_ "new.txt"
     b_ "old.txt"
     c "c.txt"]
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
      (spit "out.txt"
        (clojure.string/join "\n"
          (map 
            (fn [line]
              (let [[x y] (clojure.string/split line #",")]
                (str (logi-map x) "(= " (a-map x) ")\t" (b-map y))))
            c))))))
