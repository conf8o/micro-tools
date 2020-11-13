(require '[clojure.string :as string])

(defn f [file]
  "「,」区切りの一行、各文字列の最後のかっこだけ取り除く"
  (for [x (-> file slurp (string/split #", "))]
    (let [param (-> x
                    (string/split #"\(")
                    (#(subvec % 0 (- (count %) 1)))
                    (#(string/join "(" %)))]
      param)))