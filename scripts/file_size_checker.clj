(require '[clojure.java.io :as io])
(import '[java.io IOException])

(def base-folder (slurp "scripts/file_size_checker.input.txt"))
(def output-file "scripts/file_size_checker.output.txt")

(defn large-files [folder]
  (sort-by #(.length %) >
    (->> folder
         file-seq
         (filter #(.isFile %)))))

(defn user-input [& questions]
  (do 
    (apply println questions)
    (read)))

(defn additional-input [file]
  (user-input (format (str "%,d") (.length file))
              "bytes:"
              (.toString file)
              "\nAdd this? (N if don't want to add it.)"))

(defn deleting-input []
  (user-input "Output file is not empty. Make it empty? (Y if want to do it.)"))

(defn write [w str]
  (try
    (do (.write w str) :success)
    (catch IOException e
      (println e)
      :error)))

(defn is-quit? [c result]
  (or (= 'N c) (= :error result)))

(defn initialize [output]
  (when (and (< 0 (.length (io/file output)))
             (= 'Y (deleting-input))
        (do (spit output "") (println "Deleted.")))))

(defn app [input output]
  (let [files (large-files input)]
    (with-open [f-out (io/writer output :append true)]
      (loop [fs files
             result :success]
        (when-let [f (first fs)]
          (if (is-quit? (additional-input f) result)
            nil
            (recur (next fs) 
                   (write f-out (str (.length f) "," (.toString f) "\n")))))))))

(initialize output-file)
(app (io/file base-folder) output-file)
