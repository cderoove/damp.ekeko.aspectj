(ns 
  damp.ekeko.aspectj
  (:refer-clojure :exclude [== type declare class])
  (:require [clojure.core.logic :as l])
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:require [damp.ekeko.aspectj
             [soot :as soot]
             [ajdt :as ajdt]
             [xcut :as xcut] 
             [weaverworld :as world]
             [assumptions :as assumptions]
             ]))

(defn
  make-query-views-aspectj-aware!
  []
  (intern 'damp.ekeko.gui 'label-provider-class baristaui.views.queryResult.AJDTLabelProvider))

(make-query-views-aspectj-aware!)

(comment
  ;; Example REPL session
  ;; Before trying these out, make sure that the AspectJ project has been built at least once in this Eclipse session.
  ;; For instance, by selecting the project in the dialog openeded by the Project > Clean .. menu entry. 
  
  ;; Load all files
  (use 'damp.ekeko.aspectj)
  ;; Switch to the namespaces that reifies the domain model of the AspectJ weaver. 
  (in-ns 'damp.ekeko.aspectj.weaverworld)
  
  ;;Solutions to the following query consist of an ?aspect and one of its direct or indirect ?super types
  (damp.ekeko/ekeko* [?aspect ?super] (aspect-super+ ?aspect ?super))
  
  ;;Solutions consist of an ?aspect and one of its own, non-inherited pointcut definitions ?poincutdef
  (damp.ekeko/ekeko* [?aspect ?pointcutdef] (aspect-pointcutdefinition ?aspect ?pointcutdef))
  
  ;;Solutions correspond to all advices known to the weaver.
  (damp.ekeko/ekeko* [?advice] (advice ?advice))
  
  ;;Solutions correspond to all before advices known to the weaver.
  (damp.ekeko/ekeko* [?advice] (advice|before ?advice))
   
  ;;Solutions consist of an advice definition and its location in the source code.
  (damp.ekeko/ekeko* [?advice ?location] (advice-sourcelocation ?advice ?location))

  ;;Solutions consist of an advice and its pointcut.
  (damp.ekeko/ekeko* [?advice ?pointcut]  (advice-pointcut ?advice ?pointcut))
  
  ;;Solutions consist of an advice and one of its join point shadows. 
  (damp.ekeko/ekeko* [?advice ?shadow] (advice-shadow ?advice ?shadow))
  
  ;;Solutions consist of two different advices that share a join point shadow.
  (damp.ekeko/ekeko* [?advice1 ?advice2 ?shadow] 
                      (advice-shadow ?advice1 ?shadow) 
                      (advice-shadow ?advice2 ?shadow)
                      (!= ?advice1 ?advice2))
  
  ;;Solutions consist of an aspect and one of its intertype declarations.
  (damp.ekeko/ekeko* [?aspect ?intertype] (aspect-intertype ?aspect ?intertype))
  
  ;;Solutions consist of an intertype declaration, the field/method/constructor member it declares, and the target type to which this member is added.
  (damp.ekeko/ekeko* [?intertype ?member ?type] (intertype-member-target ?intertype ?member ?type))
  
  ;;Solutions consist of an intertype declarations that add a member to an aspect.
  (damp.ekeko/ekeko* [?declaringaspect ?intertype ?member ?targetaspect] 
                     (aspect-intertype ?declaringaspect ?intertype)
                     (intertype-member-target ?intertype ?member ?targetaspect)
                     (aspect ?targetaspect))  

  ;;To re-generate documentation
  ;;- ensure Eclipse is started using the plugin project as the working directory (e.g., /Users/cderoove/git/damp.ekeko.aspectj/EkekoAspectJ)
  ;;- evaluate:
  (use 'codox.main)
  (codox.main/generate-docs {
             :name "GASR"
             :version "1.0.0"
             :description "GASR (General-purpose Aspectual Source code Reasoner; an Ekeko plugin for querying AspectJ project)."
             :output-dir "/Users/cderoove/Desktop/doc"
             :include ['damp.ekeko.aspectj.projectmodel
                       'damp.ekeko.aspectj.weaverworld
                       'damp.ekeko.aspectj.soot]
             :sources ["src"]
             :src-dir-uri "https://github.com/cderoove/damp.ekeko.aspectj/blob/master/EkekoAspectJ"
             :src-linenum-anchor-prefix "L"}
   )
  
  )





