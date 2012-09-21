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

(defn 
  ada 
  [?dom ?sub]
  (aspect-dominates-aspect ?dom ?sub))

(defn
  incomplete-left
  [?first ?second]
  (all
    (aspect ?first)
    (aspect ?second)
    (!= ?first ?second)
    (fails (aspect-dominates-aspect ?first ?second))))

(defn
  incomplete-precedence
  [?first ?second]
  (all
    (aspect ?first)
    (aspect ?second)
    (!= ?first ?second)
    (incomplete-left ?first ?second)
    (incomplete-left ?second ?first)))



  
;  (fresh [?dom1 ?sub1 ?dom2 ?sub2]
;    (aspect ?first)
;    (aspect ?second)
;    (!= ?first ?second)
;    (aspect-dominates-aspect ?dom1 ?sub1)
;    (aspect-dominates-aspect ?dom2 ?sub2)
;    (!= ?first ?dom)
;    (!= ?second ?sub)
;    (!= ?first ?sub)
 ;   (!= ?second ?dom)))
    ;    (fails (aspect-dominates-aspect ?first ?second))

;    (conde
;       [(fails (aspect-dominates-aspect ?first ?second))]
;       [(fails (aspect-dominates-aspect ?second ?first))])))


  