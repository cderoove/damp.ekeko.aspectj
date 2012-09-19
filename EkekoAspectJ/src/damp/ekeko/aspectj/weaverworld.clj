(ns
    ^{:doc "Low-level AspectJ WeaverWorld relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.weaverworld
    (:refer-clojure :exclude [== type declare])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.util [interop :as interop]]
      [damp.ekeko.aspectj [projectmodel :as projectmodel]]
      [damp.ekeko.aspectj [xcut :as xcut]])
     (:import 
       [org.eclipse.ajdt.core.model AJProjectModelFacade AJRelationshipType AJRelationshipManager]
       [org.aspectj.weaver.model AsmRelationshipProvider]
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
  "Relation between an AspectJ weaver world and its model."
  [?world ?model]
  (all
    (weaverworld ?world)
    (equals ?model (.getModel ?world))))

(defn
  weaverworld-model-hierarchy
  "Relation between an AspectJ weaver world and its ProgramElement hierarchy."
  [?world ?model ?hierarchy]
  (all
    (weaverworld-model ?world ?model)
    (equals ?hierarchy (.getHierarchy ?model))))






;; ProgramElement hierarchy
;; but not what we want
;; -----------------------
    
(defn
  root
  "Relation of ProgramElement hierarchy roots."
  [?element]
  (fresh [?world ?model ?hierarchy]
         (weaverworld-model-hierarchy ?world ?model ?hierarchy)
         (equals ?element (.getRoot ?hierarchy))))

(clojure.core/declare element) 

(defn 
  element-child
  "Relation of roots of the weaver world ProgramElement hierarchy."
  [?element ?child-element]
  (all
    (element ?element) 
    (contains (.getChildren ?element) ?child-element)))

(def
  element
  "Relation of elements of the ProgramElement hierarchy."
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
  "Relation between a ProgramElement and its parent ProgramElement."
  [?element ?parent]
  (all
    (element ?element)
    (equals ?parent (.getParent ?element))))

(defn
  element-kind
  "Relation between a ProgramElement ?element and its kind ?kind."
  [?element ?kind]
  (all
    (element ?element)
    (equals ?kind (.getKind ?element))))

(defn
  element-signature
  "Relation between a ProgramElement and its signature string."
  [?element ?signature]
  (all
    (element ?element)
    (equals ?signature (.toSignatureString ?element))))

(defn
  element-handle
  "Relation between a ProgramElement and its handle."
  [?element ?handle]
  (all
    (element ?element)
    (equals ?handle (.getHandleIdentifier ?element))))

(defn
  element-sourcelocation
  "Relation between a ProgramElement and its SourceLocation."
  [?element ?handle]
  (all
    (element ?element)
    (equals ?handle (.getSourceLocation ?element))))

 
;above is about programelement hierarchy, which is still managed by facade
;created by AsmRelationshipProvider

;; Actual Reification of WeaverWorld
;; ---------------------------------



(defn-
  weaverworld-typemap
  "Relation between an AspectJ weaverworld and its non-expandable TypeMap."
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
 
;can be used to go from xcut to weaverworld types
(defn
  weaverworld-signature-resolvedtype
  "Non-relational. Unifies ?resolved-type with the type known to the AspectJ weaver
   ?world with the given ?signature string. 

   Can be used to go from XCut types to weaver world types." 
  [?world ?signature ?resolved-type]
  (equals ?resolved-type (.lookupBySignature ?world ?signature)))


(defn
  aspect
  "Relation of aspects known to the weaver."
  [?aspect]
  (all
    (type ?aspect)
    (succeeds (.isAspect ?aspect))))

(defn-
  aspect-crosscuttingmembers
  "Relation between an aspect and its CrosscuttingMembersSet."
  [?aspect ?members]
  (fresh [?set]
    (aspect ?aspect)
    (equals ?set (-> ?aspect .getWorld .getCrosscuttingMembersSet))
    (equals
      ?members
      (.get 
        (interop/get-invisible-field (class ^CrosscuttingMembersSet ?set)
                                     (symbol "members")
                                     ?set)
        ?aspect))))

;; Advice

;(defn
;  aspect-advice
;  "Relation between an aspect and one of the advices it declares."
;  [?aspect ?advice]
;  (fresh [?members]
;         (aspect-crosscuttingmembers ?aspect ?members)
;         (contains (.getShadowMungers ?members) ?advice)))

(defn
  aspect-advice
  "Relation between an aspect and one of the advices it declares."
  [?aspect ?advice]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredAdvice ?aspect) ?advice)))

(defn
  advice
  "Relation of advices known to the weaver."
  [?advice]
  (fresh [?aspect]
         (aspect-advice ?aspect ?advice)))

;;following does not work: handles are all null
;(defn
;  advice-handle
;  "Relation between an advice and the handle for its corresponding ProgramElement.";
;  [?advice ?handle]
;  (all
;    (advice ?advice)
;    (equals ?handle (.handle ?advice))))

  
(defn
  advice-handle
  "Relation between an advice and the handle for its corresponding ProgramElement."
  [?advice ?handle]
  (fresh [?aspect]
    (aspect-advice ?aspect ?advice)
    (equals ?handle 
            (AsmRelationshipProvider/getHandle  (-> ?aspect .getWorld .getModel) ?advice))))

    
;; Pointcuts

(defn
  advice-pointcut
  "Relation between an Advice ?advice and its Pointcut ?pointcut.
   Note that these are different from the ResolvedPointcutDefinition instances returned
   by aspect-pointcutdefinition."
  [?advice ?pointcut]
  (all
         (advice ?advice)
         (equals ?pointcut (.getPointcut ?advice))))


(defn
  pointcut
  "Relation of all pointcuts known to the AspectJ weaver.
   Note that these are not pointcut definitions."
  [?advice]
  (fresh [?advice]
         (advice-pointcut)))

;;todo: 
;;check Pointcut hierarcy, (AndPointcut, HandlerPointcut, ...)


;; Pointcut definitions

(defn
  aspect-pointcutdefinition
  "Relation between an aspect and one of the pointcuts it declares.
   Note that these are instances of ResolvedPointcutDefinition,
   rather than the Pointcut instances within ShadowMungers (advice).

   Link between PointcutDefinition and Pointcut (.getPointcut) seems broken though..."
  [?aspect ?pointcutdefinition]
  (all 
    (aspect ?aspect)
    (contains (.getDeclaredPointcuts ?aspect) ?pointcutdefinition) ;instance of ResolvedPointcutDefinition
    ))

(defn
  pointcutdefinition
  "Relation of pointcuts known to the weaver.
   Note: these are instances of Pointcut rather than ResolvedPointcutDefinition."
  [?pointcut]
  (fresh [?aspect]
         (aspect-pointcutdefinition ?aspect ?pointcut)))
  



;;alternatief:
;;neem crosscuttingmembersset
;;de keys van die map daarin (ook al is die private, kan er misschien via reflectie aan), 
;;zouden de aspecten zijn
;;en daarna alles baseren op hun shadowmungers
;;en de pointcut instances die zij hebben
;;dus niet aan een resolvedtype vragen welke declarations ze hebben
;; --> heb het getest, geen verschil
;;--> probleem: vanuit een pointcut is het niet mogelijk om terug te gaan naar de oorspronkelijk pointcutdefinition
;; (ald die er was) ... misschien wel via sourcelocation
;;				IProgramElement ipe = asm.getHierarchy().findElementForSourceLine(sl);


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

;(defn
;  aspect-pointcut+
;  [?aspect ?pointcut]
;  (all
;    (aspect ?aspect)
;    (contains (iterator-seq (.getPointcuts ?aspect)) ?pointcut)))

;; Declare

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


;; Aspect precedence
(defn
  aspect-dominates-aspect-explicitly
  "Relation between an aspect ?dominator that has a higher declared
   precedence than aspect ?subordinate because of DeclarePrecedence 
   declaration ?decprec."
  [?dominator ?subordinate ?decprec]
  (all
    (declareprecedence ?decprec)
    (aspect ?dominator)
    (aspect ?subordinate)
    (equals -1 (.compare ?decprec ?dominator ?subordinate))))



;	precedence regels: sub-aspects implicitly have precedence over their super-aspect;	lexically first advice has implicit precedence over second advice;	(voor advice van hetzelfde aspect)



;; Link between World (aka weaverworld) and AJProjectModelFacade (aka xcut)
;; 

(defn
  weaverworld-xcut
  "Relation between an AspectJ weaverworld and its corresponding XCut model (AJProjectModelFacade).
   Relations for the latter are available in the damp.ekeko.aspectj.xcut namespace." 
  [?world ?xcut]
  (all
    (weaverworld ?world)
    (equals ?xcut
            (some 
              (fn [model] 
                (when
                  (= ?world (.getAJWorldAsSeenByWeaver model))
                  (.getAJProjectFacade ^AspectJProjectModel model)))
              (projectmodel/aspectj-project-models)))
    (succeeds (.hasModel ?xcut))))
    

;(defn
;  advice-shadow
;  "Relation between an advice and one of its shadows."
;  [?advice ?shadow]
;  (fresh [?aspect ?handle ?model ?relations]
;         (aspect-advice ?aspect ?advice)
;         (equals ?model (-> ?aspect .getWorld .getModel))
;         (equals ?handle (AsmRelationshipProvider/getHandle ?model ?advice))
;         
;         (contains ?shadow (.getRelationshipMap ?model ?shadow) 
;                   
;                             	public List<IRelationship> get(String sourceHandle);;
;
;         )
  
 
;todo: it would be nicer if ?shadow were an actual Shadow instance
;this way, we stay in the weaver world
;now ?shadow stems from the xcut world
;problem: upon matching of a shadow and advice, the shadow is not recorded in the relationship map
;possible solution; (in theory) overriding addAdvisedRelationship in AsmRelationshipProvider, but difficult to make the AJDT tooling aware
(defn
  advice-shadow
  "Relation between an advice ?advice and one of its shadows, as the AspectJ IProgramElement ?shadow"
  [?advice ?shadow]
  (fresh [?aspect]
         (aspect-advice ?aspect ?advice)
         (fresh [?model ?world ?xcut]
                (equals ?world (.getWorld ?aspect))
                (weaverworld-xcut ?world ?xcut)
                (weaverworld-model ?world ?model)
                (fresh [?adviceh ?shadowh]
                       (equals ?adviceh (AsmRelationshipProvider/getHandle ?model ?advice))
                       (xcut/xcut-advicehandle-shadowhandle ?xcut ?adviceh ?shadowh)
                       (xcut/xcut-aje-pe ?xcut ?shadowh ?shadow)))))


;; Shadows


;;todo:
;;super-type info
;;implicit precedence, precedence assumption
;;advice-shadow info
;;inter-aspect-shadow
;;soot?


(comment 
  
  (damp.ekeko/ekeko* [?aspect ?pointcut] (aspect-pointcutdefinition ?aspect ?pointcut))
  
  (damp.ekeko/ekeko* [?advice] (advice ?advice))

  (damp.ekeko/ekeko* [?advice ?pointcut]  (advice-pointcut ?advice ?pointcut))
  
  (damp.ekeko/ekeko* [?advice ?shadow] (advice-shadow ?advice ?shadow))
  
  ;to check: pairs of different shadows for the same advice
  (damp.ekeko/ekeko* [?advice ?shadow1 ?shadow2] 
                     (advice-shadow ?advice ?shadow1) 
                     (advice-shadow ?advice ?shadow2) 
                     (!= ?shadow1 ?shadow2))

  ;to check: pairs of advices on same shadow
  ;(looks cool!)
  (damp.ekeko/ekeko* [?advice1 ?advice2 ?shadow ] 
                      (advice-shadow ?advice1 ?shadow) 
                      (advice-shadow ?advice2 ?shadow)
                      (!= ?advice1 ?advice2))
  
  
  )
  

    