(ns
    ^{:doc "Low-level XCUT (AJProjctModelFacade) relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.xcut
    (:refer-clojure :exclude [== type declare class])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.ekeko.aspectj [ajdt :as ajdt]]
      [damp.ekeko.aspectj [projectmodel :as projectmodel]])
     (:import 
       [damp.ekeko EkekoModel]
       [damp.ekeko.aspectj AspectJProjectModel]
      ; [org.aspectj.asm.IRelationship]
       [org.aspectj.weaver World]
       [org.aspectj.org.eclipse.jdt.core.dom ASTNode]
       [org.eclipse.core.resources IProject]
       [org.eclipse.jdt.core IField IMethod IJavaElement]
       [org.eclipse.jdt.internal.core SourceType]
       [org.eclipse.ajdt.core.model AJProjectModelFacade AJRelationshipType AJRelationshipManager]
       [org.eclipse.ajdt.core.javaelements IAspectJElement AspectElement IntertypeElement AdviceElement DeclareElement PointcutElement AJCompilationUnit FieldIntertypeElement MethodIntertypeElement]
       ))
         
;; XCut model
;; ----------

;; TODO: ensure aj project has been built from scratch once


(defn- 
  xcuts
  []
  (filter 
    (fn [^AJProjectModelFacade facade] (.hasModel facade))
    (map
      (fn [model] (.getAJProjectFacade ^AspectJProjectModel model)) 
      (projectmodel/aspectj-project-models))))

(defn 
  xcut
  "Relation of all AJProjectModel facades."
  [?xcut]
  (conda [(v+ ?xcut)
          (succeeds (instance? AJProjectModelFacade ?xcut))]
         [(v- ?xcut)
          (contains (xcuts) ?xcut)]))

(defn-
  element-xcut
  "Relation between a model element ?element and the AJProjectModelFacade ?xcut for the project in which it resides."
  [?element ?xcut]
  (fresh [?model]
         (ajdt/element-model ?element ?model)
         (equals ?xcut (.getAJProjectFacade ^AspectJProjectModel ?model))
         (succeeds (.hasModel ^AJProjectModelFacade ?xcut))))


(defn
  xcut-relationtype-relation
  "Relation between an XCut model ?xcut (an AJProjectModelFacade) and one of its relations ?relation, of type ?relation-type."
  [?xcut ?relation-type ?relation]
  (fresh [?types]
         (contains (seq (AJRelationshipManager/getAllRelationshipTypes)) ?relation-type)
         (equals ?types (into-array AJRelationshipType [?relation-type]))
         (xcut ?xcut)
         (equals ?relation (.getRelationshipsForProject ^AJProjectModelFacade ?xcut ?types))))

;handle to JDT JavaElement
(defn
  xcut-handle-je
  "Non-relational. Unifies ?j-element with the JDT IJavaElement that corresponds to the 
   given ?handle of an AspectJ ProgramElement in the given XCut model ?xcut (an AJProjectModelFacade)."
  [?xcut ?handle ?j-element]
  (all 
    (equals ?j-element (.programElementToJavaElement ^AJProjectModelFacade ?xcut ^String ?handle))))

;handle to AspectJ ProgramElement
(defn
  xcut-handle-pe
  "Non-relational. Unifies ?p-element with the AspectJ ProgramElement that corresponds to the 
   given ?handle in the given XCut model ?xcut (an AJProjectModelFacade)."
  [?xcut ?handle ?p-element]
  (all 
    (equals ?p-element (.getProgramElement ^AJProjectModelFacade ?xcut ^String ?handle))))

;JDT Javaelement to AspectJ ProgramElement
(defn
  xcut-je-pe
  "Non-relational. Unifies ?aj-element with the AspectJ ProgramElement that corresponds to the 
   given JDT IJavaElement ?jdt-element in the given XCut model ?xcut (an AJProjectModelFacade)."
  [?xcut ?jdt-element ?aj-element]
  (all
    (equals ?aj-element (.javaElementToProgramElement ^AJProjectModelFacade ?xcut ?jdt-element))))

(defn
  pe-signature
  "Non-relational. Unifies ?signature-string with the signature 
   of the given AspectJ ProgramElement ?program-element"
  [?program-element ?signature-string]
  (all
    (equals ?signature-string (.toSignatureString ?program-element))))

(defn
  xcut-advicehandle-shadowhandle
  [?xcut ?advicehandle ?shadowhandle]
  (fresh [?relation ?relelement]
         (xcut-relationtype-relation ?xcut (AJRelationshipManager/ADVISES) ?relation)
         (contains ?relation ?relelement)
         (equals ?advicehandle (.getSourceHandle ?relelement))
         (contains (.getTargets ?relelement) ?shadowhandle)))


(comment
  
  ;; XCut shadows and advice
  ;; commented out because prefer to go through the WeaverWorld

  
  (defn
    advice-shadow
    [?advice ?shadow]
    (fresh [?xcut ?adviceh ?shadowh]
           (xcut-advicehandle-shadowhandle ?xcut ?adviceh ?shadowh)
           (xcut-aje-je ?xcut ?adviceh ?advice)
           (xcut-aje-je ?xcut ?shadowh ?shadow)))
  
  (defn 
    shadow
    [?shadow]
    (fresh [?advice]
           (advice-shadow ?advice ?shadow))))

  

