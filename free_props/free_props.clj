(defn main-props []
  (let [props (-> "props.txt"
                  slurp
                  (clojure.string/split #"\n"))]
    (println (count props))))