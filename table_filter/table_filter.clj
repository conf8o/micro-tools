(def config (load-file "config.clj"))

(defn table-seq [s]
  (re-seq (config :table-re) s))

(defn main []
  "ログから機能がアクセスするDBテーブル一覧を取得"
  (let [raw (slurp "raw.txt")
        tables (set (table-seq raw))]
    (spit "out.txt"
      (clojure.string/join "\n" (sort tables)))))