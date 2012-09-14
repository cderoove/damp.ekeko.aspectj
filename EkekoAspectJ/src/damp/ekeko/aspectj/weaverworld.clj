(ns
    ^{:doc "Low-level AspectJ WeaverWorld relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.weaverworld
    (:refer-clojure :exclude [== type declare])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.ekeko.aspectj [projectmodel :as projectmodel]]
      [damp.ekeko.aspectj [ajdtastnode :as astnode]])
     (:import 
       [org.aspectj.weaver.patterns Declare DeclarePrecedence]
       [damp.ekeko EkekoModel]
       [damp.ekeko.aspectj AspectJProjectModel]
       [org.aspectj.weaver World]
       [org.aspectj.asm IProgramElement IProgramElement$Kind]))
              

;(set! *warn-on-reflection* true)


;; AspectJ Weaver World
;; --------------------

;note: contains a lot of interesting information

(defn- 
  weaverworlds
  []
  (filter
    (complement nil?) 
    (map
      (fn [model] (.getAJWorldAsSeenByWeaver ^AspectJProjectModel model)) 
      (projectmodel/aspectj-project-models))))

(defn- 
  weaverworld
  "Relation of all AspectJ weaver worlds."
  [?world]
  (conda [(v+ ?world)
          (succeeds (instance? World ?world))]
         [(v- ?world)
          (contains (weaverworlds) ?world)]))


;world -> getModel (an AsmManager) -> 
;getHierarchy (an AspectJElementHierarchy)
; -> findElementForHandle / offset / type / getRoot / getElement / getRoot


(defn-
  weaverworld-model
  [?world ?model]
  (all
    (weaverworld ?world)
    (equals ?model (.getModel ?world))))

(defn
  weaverworld-model-hierarchy
  [?world ?model ?hierarchy]
  (all
    (weaverworld-model ?world ?model)
    (equals ?hierarchy (.getHierarchy ?model))))


;; ProgramElement hierarchy
;; but not what we want
;; -----------------------
    
(defn
  root
  [?element]
  (fresh [?world ?model ?hierarchy]
         (weaverworld-model-hierarchy ?world ?model ?hierarchy)
         (equals ?element (.getRoot ?hierarchy))))

(clojure.core/declare element) 

(defn 
  element-child
  [?element ?child-element]
  (all
    (element ?element) 
    (contains (.getChildren ?element) ?child-element)))

(def
  element
  (tabled
    [?element]
    (conda
      [(v+ ?element)
       (succeeds (instance? IProgramElement ?element))]
      [(v- ?element)
       (conde 
         [(root ?element)]
         [(fresh [?parent]
                 (element ?parent)
                 (element-child ?parent ?element))])])))


(defn
  element-parent
  [?element ?parent]
  (all
    (element ?element)
    (equals ?parent (.getParent ?element))))

(defn
  element-kind
  [?element ?kind]
  (all
    (element ?element)
    (equals ?kind (.getKind ?element))))

(defn
  element-signature
  [?element ?signature]
  (all
    (element ?element)
    (equals ?signature (.toSignatureString ?element))))

(defn
  element-handle
  [?element ?handle]
  (all
    (element ?element)
    (equals ?handle (.getHandleIdentifier ?element))))

(defn
  element-sourcelocation
  [?element ?handle]
  (all
    (element ?element)
    (equals ?handle (.getSourceLocation ?element))))

 (comment 

(defn
  aspect 
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/ASPECT))))

(defn
  advice 
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/ADVICE))))

(defn
  declare-parents 
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/DECLARE_PARENTS))))

(defn
  declare-warning
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/DECLARE_WARNING))))

(defn
  declare-error
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/DECLARE_ERROR))))

(defn
  declare-soft
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/DECLARE_SOFT))))

(defn
  declare-precedence
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/DECLARE_PRECEDENCE))))

(defn 
  intertype-field
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/INTER_TYPE_FIELD))))

(defn 
  intertype-method
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/INTER_TYPE_METHOD))))

(defn 
  intertype-constructor
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/INTER_TYPE_CONSTRUCTOR))))

(defn 
  intertype-parent
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/INTER_TYPE_PARENT))))

(defn
  intertype
  [?element]
  (conde
    [(intertype-field ?element)]
    [(intertype-method ?element)]
    [(intertype-parent ?element)]
    [(intertype-constructor ?element)]))

(defn
  pointcut 
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/POINTCUT))))

;todo: these also contain synthetic and pure java methods/fields
(defn
  field   
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/FIELD))))

(defn
  method   
  [?element]
  (all
    (element ?element) 
    (element-kind ?element (IProgramElement$Kind/METHOD))))
  

(defn
  aspect-pointcut 
  [?aspect ?pointcut]
  (all
    (pointcut ?pointcut)
    (element-parent ?pointcut ?aspect)))

(defn
  aspect-advice
  [?aspect ?advice]
  (all
    (advice ?advice)
    (element-parent ?advice ?aspect)))

(defn
  aspect-declare-precedence
  [?aspect ?declare]
  (all
    (declare-precedence ?declare)
    (element-parent ?declare ?aspect)))
)
 
;above is about programelement hierarchy, which is still managed by facade
;created by AsmRelationshipManager

;; Actual Reification of WeaverWorld
;; ---------------------------------

(defn
  weaverworld-typemap
  [?world ?typemap]
  (all 
    (weaverworld ?world)
    (equals ?typemap (-> ?world .getTypeMap .getMainMap))))

(defn
  type
  "Relation of non-expandable (i.e., project-defined) types known to the AspectJ weaver."
  [?resolvedtype]
  (fresh [?world ?map ?entry]
       (weaverworld-typemap ?world ?map)
       (contains ?map ?entry)
       (equals ?resolvedtype (.getValue ?entry))))
  
(defn
  aspect
  "Relation of aspects known to the weaver."
  [?aspect]
  (all
    (type ?aspect)
    (succeeds (.isAspect ?aspect))))


(defn
  aspect-pointcut
  "Relation between an aspect and one of the pointcuts it declares."
  [?aspect ?pointcut]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredPointcuts ?aspect) ?pointcut)))


(defn
  aspect-advice
  "Relation between an aspect and one of the advices it declares."
  [?aspect ?advice]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredAdvice ?aspect) ?advice)))

;todo:
;filter out synthetic ones
(defn
  aspect-field
  [?aspect ?field]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredJavaFields ?aspect) ?field)))

(defn
  aspect-method
  [?aspect ?method]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredJavaMethods ?aspect) ?method)))

(defn
  aspect-interface
  "Relation between an aspect and one of the interfaces it declares to be implementing."
  [?aspect ?interface]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredInterfaces ?aspect) ?interface)))

(defn
  aspect-pointcut+
  [?aspect ?pointcut]
  (all
    (aspect ?aspect)
    (contains (iterator-seq (.getPointcuts ?aspect)) ?pointcut)))

(defn
  aspect-declare
  "Relation between an aspect and one of its aspect-specific declarations."
  [?aspect ?declare]
  (all
    (aspect ?aspect)
    (contains (.getDeclares ?aspect) ?declare)))


(defn
  declare
  "Relation of all aspect-specific declarations known to the weaver."
  [?declare]
  (conda [(v+ ?declare)
          (succeeds (instance? Declare ?declare))]
         [(v- ?declare)
          (fresh [?aspect]
                 (aspect-declare ?aspect ?declare))]))
  

(defn
  declareprecedence
  "Relation of all precedence declarations."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclarePrecedence ?declare))))


(defn
  declareprecedence-patterns 
  "Relation between a DeclarePrecedence ?declare and its TypePatternList ?patterns"
  [?declare ?patterns]
  (all
    (declareprecedence ?declare)
    (equals ?patterns (.getPatterns ?declare))))

(defn
  aspect-dominates-aspect-explicitly
  "Relation between an aspect ?dominator that has a higher declared
   precedence than aspect ?subordinate because of DeclarePrecedence 
   declaraton ?decprec."
  [?dominator ?subordinate ?decprec]
  (all
    (declareprecedence ?decprec)
    (aspect ?dominator)
    (aspect ?subordinate)
    (equals -1 (.compare ?decprec ?dominator ?subordinate))))


(defn
  aspect-shadow
  [?aspect ?shadow]
  
  )


;	precedence regels: sub-aspects implicitly have precedence over their super-aspect;	lexically first advice has implicit precedence over second advice;	(voor advice van hetzelfde aspect)


;;todo:
;;super-type info
;;implicit precedence, precedence assumption
;;advice-shadow info
;;inter-aspect-shadow
;;soot?


(comment 
  
  (damp.ekeko/ekeko* [?aspect ?shadow] ())
  
  
  )
  

    