(ns 
  damp.ekeko.aspectj
  (:refer-clojure :exclude [== type])
;  (:use [clojure.core.logic])
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:require [damp.ekeko.aspectj
             [ajdt :as ajdt]
             [xcut :as xcut] 
             [weaverworld :as world]]))

(defn
  make-query-views-aspectj-aware!
  []
  (intern 'damp.ekeko.gui 'label-provider-class baristaui.views.queryResult.AJDTLabelProvider))

(make-query-views-aspectj-aware!)



(comment
  
  (use 'damp.ekeko.aspectj)
  (in-ns 'damp.ekeko.aspectj.weaverworld)
  
  ;;before trying these out, make sure that the AspectJ project has been built at least in this Eclipse session
  
  (damp.ekeko/ekeko* [?aspect ?super] (aspect-declaredsuper+ ?aspect ?super))
  
  ;to check: these should include indirect supers (class/interface/aspect) stemming from intertype declarations
  (damp.ekeko/ekeko* [?aspect ?super] (aspect-super+ ?aspect ?super))
  
  (damp.ekeko/ekeko* [?aspect ?pointcut] (aspect-pointcutdefinition ?aspect ?pointcut))
  
  (damp.ekeko/ekeko* [?advice] (advice ?advice))
  
  (damp.ekeko/ekeko* [?advice]  (advicebefore ?advice))
  
  (damp.ekeko/ekeko* [?advice ?location] (advice-sourcelocation ?advice ?location))

  (damp.ekeko/ekeko* [?advice ?pointcut]  (advice-pointcut ?advice ?pointcut))
  
  (damp.ekeko/ekeko* [?advice ?shadow] (advice-shadow ?advice ?shadow))
  
  
  ;to check: pairs of different shadows for the same advice
  (damp.ekeko/ekeko* [?advice ?shadow1 ?shadow2] 
                     (advice-shadow ?advice ?shadow1) 
                     (advice-shadow ?advice ?shadow2) 
                     (!= ?shadow1 ?shadow2))

  ;to check: pairs of advices on same shadow
  ;(looks cool!)
  (damp.ekeko/ekeko* [?advice1 ?advice2 ?shadow] 
                      (advice-shadow ?advice1 ?shadow) 
                      (advice-shadow ?advice2 ?shadow)
                      (!= ?advice1 ?advice2))
  
  
  (damp.ekeko/ekeko* [?aspect ?intertype] (aspect-intertype ?aspect ?intertype))
  
  (damp.ekeko/ekeko* [?intertype ?member ?type] (intertype-member-type ?intertype ?member))
  
  ;;intertype declarations that add a member to an aspect
  (damp.ekeko/ekeko* [?declaringaspect ?intertype ?member ?targetaspect] 
                     (aspect-intertype ?declaringaspect ?intertype)
                     (intertype-member-type ?intertype ?member ?targetaspect)
                     (aspect ?targetaspect))
  
  ;;adviced shadows that stem from an intertype declaration
  (damp.ekeko/ekeko* [?advice ?shadow ?intertype]
                     (intertype-element ?intertype ?shadow)
                     (advice-shadow ?advice ?shadow))
  
  
  
  )





