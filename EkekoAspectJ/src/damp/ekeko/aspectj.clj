(ns 
  damp.ekeko.aspectj
  (:refer-clojure :exclude [== type])
;  (:use [clojure.core.logic])
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:require [damp.ekeko.aspectj [reification :as aj] [ajdtastnode :as ajnode]]))

(defn
  make-query-views-aspectj-aware!
  []
  (intern 'damp.ekeko.gui 'label-provider-class baristaui.views.queryResult.AJDTLabelProvider))

(make-query-views-aspectj-aware!)



(comment
  
  (use 'damp.ekeko.aspectj)
  (in-ns 'damp.ekeko.aspectj)
  
  ;results from model elements
  (ekeko* [?aspect ?advice] (aj/aspect-advice ?aspect ?advice))
  
  (ekeko* [?aspect ?pc] (aj/aspect-pointcut ?aspect ?pc))
  
  (ekeko* [?key ?element] (aj/element ?key ?element))
  
  ;results from xcut model
  (ekeko* [?advice ?shadow] (aj/advice-shadow ?advice ?shadow))

  ;;all xcut relations and their type ... only 4 contain info for the test project
  ;;TODO: check on HealthWatcher, AJHotdraw
  (ekeko* [?xcut ?reltype ?rel] (aj/xcut-relationtype-relation ?xcut ?reltype ?rel))
  
  
  
  )





