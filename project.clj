(defproject micro-tools "0.1.0-SNAPSHOT"
  :description "Private micro tools for Java app development as my work."
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [dk.ative/docjure "1.14.0"]]
  :resource-paths ["resources"]
  :repl-options {:init-ns micro-tools.core})
