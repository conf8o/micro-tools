(ns micro-tools.core)

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

(defn dev [& option]
  (apply 
   require
   '[micro-tools.free :as free]
   '[micro-tools.java-props :as java-props]
   '[micro-tools.json-props :as json-props]
   '[micro-tools.name-map :as name-map]
   '[micro-tools.set :as m-set] 
   '[micro-tools.sql-binding :as sql-binding]
   '[micro-tools.string :as m-string]
   '[micro-tools.table-filter :as table-filter]
   '[micro-tools.ut-resource :as ut-resource]
   option))

(defn reload []
  (require 'micro-tools.core :reload))
