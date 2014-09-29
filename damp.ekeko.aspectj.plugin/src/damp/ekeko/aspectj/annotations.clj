(ns
  ^{:doc "Annotation support for implementing parts of the 'aspect assumptions' paper."
  :author "Coen De Roover, Johan Fabry" }
  damp.ekeko.aspectj.annotations
  (:refer-clojure :exclude [== type declare class])
  (:require [clojure.core.logic :as l] )
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.aspectj weaverworld])
  (:require [damp.ekeko.aspectj
             [soot :as ajsoot]
             ]
            [damp.ekeko.soot
             [soot :as jsoot]]
            )) 

(defn
  type-annotation-annotation|type|name
  [?type ?annotation ?atn]
  (l/fresh [?at]
    (type-annotation ?type ?annotation)
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))

;;TO TEST    
(defn
  type-requires|annotation
  [?type ?ann]
  (l/all
    (type-annotation-annotation|type|name ?type ?ann "damp.ekeko.aspectj.annotations.Requires")))

;;TO TEST    
(defn
  type-oneOf|annotation
  [?type ?ann]
  (l/all 
    (type-annotation-annotation|type|name ?type ?ann "damp.ekeko.aspectj.annotations.OneOf")))

(defn
  type-label|annotation
  [?type ?ann]
  (l/all
    (type-annotation-annotation|type|name ?type ?ann "damp.ekeko.aspectj.annotations.Label")))

;This is for methods, advice, constructors 
;; FIX ME: method-annotation also returns annotations for pointcuts
(defn
  behavior-annotation-annotation|type|name
  [?meth ?annotation ?atn]
  (l/fresh [?at]
    (method-annotation ?meth ?annotation)
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))

;;TEST OK %pointcuts present
;;TO FORMALIZE IN TEST
(defn 
  behavior-label|annotation
  [?behavior ?ann]
  (l/all
    (behavior-annotation-annotation|type|name ?behavior ?ann "damp.ekeko.aspectj.annotations.Label")))