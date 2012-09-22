(ns 
  ^{:doc "Relations for implementing parts of the 'aspect assumptions' paper."
    :author "Coen De Roover, Johan Fabry" }
   damp.ekeko.aspectj.assumptions
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic])
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.aspectj weaverworld])
  (:require [damp.ekeko.aspectj
             [soot :as soot]
             [ajdt :as ajdt]
             [xcut :as xcut]]))

;; Assumption: incomplete precedence
(defn
  incomplete-precedence
  [?first ?second]
  (all
    (aspect ?first)
    (aspect ?second)
    (!= ?first ?second)
    (fails (aspect-dominates-aspect ?first ?second))
    (fails (aspect-dominates-aspect ?second ?first))))


;Seems to work -- modified test package after checking expl, impl, trans expl - recheck
(defn
  overriden-implicit-precedence
  [?first ?second]
  (all
    (aspect-dominates-aspect ?second ?first)
    (aspect-dominates-aspect-implicitly+ ?first ?second)))

;Does not work yet - bug in element-enclosing-aspect
; should return the tuple: SecondAspect FirstAspect
(defn 
  modifies-aspect
  [?modifier ?modified]
  (fresh [?advice ?shadow]
         (aspect-advice ?modifier ?advice)
         (advice-shadow ?advice ?shadow)
         (equals ?modified (.getDeclaringType ?shadow))))
         ;(element-enclosing-aspect ?shadow ?modified))
    
  