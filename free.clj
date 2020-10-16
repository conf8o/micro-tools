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

(defn words [s]
  (re-seq #"\w+" s))

(defn java-props [lines]
  (for [line lines
        :when (.contains line "private")]
    (-> line
        words
        last
        (#(subs %1 0 (- (count %1) 1))))))

; JavaのSetではない模様
(defmulti xor (fn [s1 s2] [(class s1) (class s2)]))
(defmethod xor 
  [java.util.Set java.util.Set]
  [s1 s2]
  (clojure.set/difference (clojure.set/union s1 s2)
                          (clojure.set/intersection s1 s2)))


; CallableとSeqableを持つやつでやって
(defmethod xor
  [clojure.lang.Seqable clojure.lang.Seqable]
  [seq1 seq2]
  (set 
    (apply concat
      (for [x seq1
            y seq2
            :when (not (or (seq1 y) (seq2 x))())]
        (list x y)))))

; (defn a- [] (->> "a.txt"
;                  read-lines
;                  (filter #(not (empty? %)))
;                  (map #((words %) 0)
;                  set)))

; (defn b- [] (->> "b.txt"
;                  read-lines
;                  (filter #(not (empty? %)))
;                  java-props
;                  set))

; (defn main []
;   (let [a (a-)
;         b (b-)]
;     (println a)
;     (println b)
;     (println (xor a b))))

