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
  [?type ?ann ?atn]
  (l/fresh [?at]
    (type-annotation ?type ?ann)
    (annotation-annotationtype ?ann ?at)
    (type-name ?at ?atn)))

(defn
  type-annotation|key-annotation|value-annotation|type|name
  [?type ?key ?value ?atn]
  (l/fresh [?ann]
    (type-annotation-annotation|type|name ?type ?ann ?atn)
    (annotation-key-value ?ann ?key ?value)))

(defn
  requiring|type-key-val
  [?type ?key ?val]
  (l/all
    (type-annotation|key-annotation|value-annotation|type|name ?type ?key ?val "damp.ekeko.aspectj.annotations.Requires")))

(defn
  excluding|type-key-val
  [?type ?key ?val]
  (l/all
    (type-annotation|key-annotation|value-annotation|type|name ?type ?key ?val "damp.ekeko.aspectj.annotations.Excludes")))

(defn
  oneOfing|type-key-val
  [?type ?key ?val]
  (l/all
    (type-annotation|key-annotation|value-annotation|type|name ?type ?key ?val  "damp.ekeko.aspectj.annotations.OneOf")))

(defn
  labeled|type-label|val
  [?type ?val]
  (l/fresh [?key]
    (type-annotation|key-annotation|value-annotation|type|name ?type ?key ?val "damp.ekeko.aspectj.annotations.Label")))

;This is for methods, advice, constructors 
(defn
  behavior-annotation-annotation|type|name
  [?meth ?annotation ?atn]
  (l/fresh [?at]
    (method-annotation ?meth ?annotation)
    (fails (l/fresh [?pc] (pointcutdefinition-method|ajsynthetic ?pc ?meth)))
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))

(defn
  behavior-annotation|key-annotation|value-annotation|type|name
  [?meth ?key ?value ?atn]
  (l/fresh [?ann]
    (behavior-annotation-annotation|type|name ?meth ?ann ?atn)
    (annotation-key-value ?ann ?key ?value)))


(defn pointcut-annotation-annotation|type|name
  [?pc ?annotation ?atn]
  (l/fresh [?at]
    (pointcutdefinition-annotation ?pc ?annotation)
    (annotation-annotationtype ?annotation ?at)
    (type-name ?at ?atn)))   

(defn
  pointcut-annotation|key-annotation|value-annotation|type|name
  [?pc ?key ?value ?atn]
  (l/fresh [?ann]
    (pointcut-annotation-annotation|type|name ?pc ?ann ?atn)
    (annotation-key-value ?ann ?key ?value)))

(defn
  labeled|behavior-label|val
  [?behavior ?val]
  (l/fresh [?key]
    (behavior-annotation|key-annotation|value-annotation|type|name ?behavior ?key ?val "damp.ekeko.aspectj.annotations.Label")))

(defn
  labeled|pointcut-label|val
  [?pc ?val]
  (l/fresh [?key]
    (pointcut-annotation|key-annotation|value-annotation|type|name ?pc ?key ?val "damp.ekeko.aspectj.annotations.Label")))
 
(defn 
  reqPrev|behavior-val
  [?behavior ?val]
  (l/fresh [?key]
    (behavior-annotation|key-annotation|value-annotation|type|name ?behavior ?key ?val "damp.ekeko.aspectj.annotations.RequiresPrevious")))

(defn 
  exclPrev|behavior-val
  [?behavior ?val]
  (l/fresh [?key]
    (behavior-annotation|key-annotation|value-annotation|type|name ?behavior ?key ?val "damp.ekeko.aspectj.annotations.ExcludesPrevious")))

;;Type pattern matching
(comment 
  ;;homebrew version. AspectJ pattern matcher is type-type|pattern in weaverworld
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


(defn type-pattern|type
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
 )

(defn type-type|pattern2
 "Uses AJ pattern matcher BUT removes 'root' types when using the + operator e.g., XXX+ will NOT match XXX.
For normal AJ type-pattern matching semantics use d.e.a.weaverworld/type-type|pattern"
 [?type ?pattern]
 (l/all (v+ ?pattern)
        (type-type|pattern ?type ?pattern)
        (fails
          (l/project [?pattern]
            (l/!= ?pattern (clojure.string/replace ?pattern #"\+" ""))
            (type-type|pattern ?type (clojure.string/replace ?pattern #"\+" ""))))))