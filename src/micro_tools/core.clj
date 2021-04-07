(ns micro-tools.core)

(defn dev [& option]
  (apply 
   require
   '[micro-tools.excel :as excel]
   '[micro-tools.explorer :as explorer]
   '[micro-tools.free :as free]
   '[micro-tools.io :as m-io]
   '[micro-tools.java-props :as java-props]
   '[micro-tools.json-props :as json-props]
   '[micro-tools.name-map :as name-map]
   '[micro-tools.set :as m-set] 
   '[micro-tools.sql-binding :as sql-binding]
   '[micro-tools.string :as m-string]
   '[micro-tools.swagger :as swagger]
   '[micro-tools.table-filter :as table-filter]
   '[micro-tools.ut-resource :as ut-resource]
   option))

(defn reload []
  (require 'micro-tools.core :reload))

(defmacro exec [script]
  `(load-file (str "scripts/" ~(name script) ".clj")))

(defmacro run [tool & args]
  `(do
     (reload)
     (dev :reload)
     (~(symbol (name tool) "run") ~@args)))
