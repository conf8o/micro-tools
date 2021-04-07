(require '[clojure.java.io :as io]
         '[clojure.string :as string])
(import '[java.io IOException])

(def base-folder (slurp "scripts/file_size_checker.input.txt"))
(def output-file "scripts/file_size_checker.output.txt")

(defn large-files [folder]
  (sort-by #(.length %) >
    (->> folder
         file-seq
         (filter #(.isFile %)))))

(defn user-input [max-size f]
  (do
    (println (format (str "%," max-size "d") (.length f))
             "bytes :"
             (.toString f)
             "\nAdd? (type Q if quit)")
    (read)))

(defn write [w str]
  (try
    (do (.write w str) :success)
    (catch IOException e
      (println e)
      :error)))

(defn is-quit? [c result]
  (or (= 'Q c) (= :error result)))

(defn app [input output]
  (let [files (large-files input)
        digits (apply max (map #(-> % .length str count) files))]
  (with-open [f-out (io/writer output :append true)]
    (loop [fs files
           result :success]
      (let [f (first fs)]
        (if (is-quit? (user-input digits f) result)
            nil
            (recur (next fs) (write f-out (str (.toString f) "\n")))))))))

(app (io/file base-folder) output-file)
