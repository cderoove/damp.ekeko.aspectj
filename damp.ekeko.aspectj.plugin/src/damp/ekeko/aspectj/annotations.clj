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
  type-excludes|annotation
  [?type ?ann]
  (l/all
    (type-annotation-annotation|type|name ?type ?ann "damp.ekeko.aspectj.annotations.Excludes")))

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
    ;adding this does not seem to solve the problem ?
   ; (l/fails (pointcutdefinition-annotation ?meth ?annotation))
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))

(defn pointcut-annotation-annotation|type|name
  [?pc ?annotation ?atn]
  (l/fresh [?at]
    (pointcutdefinition-annotation ?pc ?annotation)
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))   

;;pointcuts present
(defn 
  behavior-label|annotation
  [?behavior ?ann]
  (l/all
    (behavior-annotation-annotation|type|name ?behavior ?ann "damp.ekeko.aspectj.annotations.Label")))

(defn
  pointcut-label|annotation
  [?pc ?ann]
  (l/all
    (pointcut-annotation-annotation|type|name ?pc ?ann "damp.ekeko.aspectj.annotations.Label")))
  

;;Type pattern matching

(defn- type-simple-name 
  [?type ?sn]
  (l/all 
    (class ?type)
    (equals ?sn (.getSimpleName ?type))))

(defn- type-name|sub
  [?sn ?subtype]
  (l/fresh [?type]
           (type-simple-name ?type ?sn)
           (type-super+ ?subtype ?type)))

(defn- type-name|sub+
  [?sn ?subtype]
  (l/conde
    [(type-simple-name ?subtype ?sn)]
    [(type-name|sub ?sn ?subtype)]))


(defn- type-pattern|type
  [?typePat ?match]
  (l/fresh [?name ?nameplus]
           (v+ ?typePat)
           (equals ?name (clojure.string/replace ?typePat #"[+]" ""))
           (equals ?nameplus (str ?name "+"))
           (l/conda
             [(l/==  ?nameplus ?typePat)
              (type-name|sub+ ?name ?match)]
             [(l/!= ?nameplus ?typePat)
              (type-simple-name ?match ?typePat)])))
    
