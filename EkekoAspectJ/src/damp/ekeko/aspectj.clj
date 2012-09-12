(ns 
  damp.ekeko.aspectj
  (:refer-clojure :exclude [== type])
;  (:use [clojure.core.logic])
;  (:use [damp.ekeko logic])
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
  
  (ekeko* [?aspect ?pc] (aj/aspect-advice ?aspect ?pc))
  

  )





