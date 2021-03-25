(ns micro-tools.core)

(defn words [s]
  (re-seq #"\w+" s))

(defn dev [& option]
  (apply 
   require
   '[micro-tools.excel :as excel]
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
