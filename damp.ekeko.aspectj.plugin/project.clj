(defproject GASR "1.0.0"
  :description "GASR (General-purpose Aspectual Source code Reasoner; an Ekeko plugin for querying AspectJ project)."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :plugins [[codox "0.6.4"]]
  :codox {
          :name "GASR"
          :version "1.0.0"
          :description "GASR (General-purpose Aspectual Source code Reasoner; an Ekeko plugin for querying AspectJ project)."
          :output-dir "/Users/cderoove/Desktop/doc"
          :include ['damp.ekeko.aspectj.projectmodel
                    'damp.ekeko.aspectj.weaverworld
                    'damp.ekeko.aspectj.soot]
          :sources ["src"]
          :src-dir-uri "http://github.com/cderoove/damp.ekeko.aspectj/blob/master/EkekoAspectJ"
          :src-linenum-anchor-prefix "L"}

  
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.clojure/core.logic "0.8.3"]
                 [qwal "1.0.0-SNAPSHOT"]])
