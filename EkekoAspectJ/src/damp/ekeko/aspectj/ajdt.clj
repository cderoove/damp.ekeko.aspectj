(ns
    ^{:doc "Low-level AJDT relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.ajdt
    (:refer-clojure :exclude [== type declare])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.ekeko.aspectj [projectmodel :as projectmodel]]
      [damp.ekeko.aspectj [ajdtastnode :as astnode]])
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

(clojure.core/declare nodes-of-type)


;; AST Nodes
;; ---------

;todo: this is actually the same as the JDT one, except that the functions originate from a different namespace
;and the instanceof check is a performed on a different class
;figure out how to eliminate this duplication
(defn 
  ast 
  "AJDT version of ast/2.
  
   See also:
   API documentation of org.aspectj.org.eclipse.jdt.core.dom.ASTNode"
  [?keyword ?node]
  (conda [(v+ ?node) (conda [(v+ ?keyword) (all (succeeds-without-exception (instance? (astnode/class-for-ekeko-keyword ?keyword) ?node)))]
                            [(v- ?keyword) (all (succeeds  (instance? ASTNode ?node))
                                                (equals ?keyword (astnode/ekeko-keyword-for-class-of ?node)))])]
         [(v- ?node) (conda [(v+ ?keyword) (fresh [?nodes]
                                                  (equals ?nodes (nodes-of-type ?keyword))
                                                  (contains ?nodes ?node))]
                            [(v- ?keyword) (fresh [?keywords]
                                                  (equals ?keywords (map (fn [^Class c]
                                                                       (astnode/ekeko-keyword-for-class c))
                                                                     (astnode/ajdt-node-classes)))
                                                  (contains ?keywords ?keyword)
                                                  (ast ?keyword ?node))])]))


(defn
  has
  "AJDT version of has/3
   
   See also: 
   Ternary predicate child/3 
   API documentation of org.aspectj.eclipse.jdt.core.dom.ASTNode 
   and org.aspectj.org.eclipse.jdt.core.dom.StructuralPropertyDescriptor"
  [?keyword ?node ?child]
   (conda [(v+ ?node) (conda [(v+ ?keyword) 
                              (fresh [?childretrievingf]
                                    (equals ?childretrievingf (?keyword (astnode/reifiers ?node)))
                                    (!= ?childretrievingf nil)
                                    (equals ?child (?childretrievingf)))]
                             [(v- ?keyword) (fresh [?keywords]
                                                  (equals ?keywords (keys (astnode/reifiers ?node)))
                                                  (contains ?keywords ?keyword)
                                                  (has ?keyword ?node ?child))])]
          [(v- ?node) (conda [(v+ ?child) (all (equals ?node (astnode/owner ?child)) 
                                               (has ?keyword ?node ?child))]
                             [(v- ?child) (fresh [?astkeyw]
                                              (ast ?astkeyw ?node)
                                              (has ?keyword ?node ?child))])]))

(defn 
  child 
   "AJDT version of child/3"
  [?keyword ?node ?child]
  (fresh [?ch] 
    (has ?keyword ?node ?ch)
    (conda
           [(succeeds (instance? ASTNode ?ch)) (== ?child ?ch)] 
           [(succeeds (instance? java.util.AbstractList ?ch)) (contains ?ch ?child)])))

(defn 
  child+
  "AJDT version of child+  

  See also:
  Ternary predicate child/3"
  [?node ?child]
  (fresh [?keyw ?ch]
    (child ?keyw ?node ?ch)
    (conde [(== ?ch ?child)]
           [(child+ ?ch ?child)])))





;todo: figure out how to avoid code duplication with regular java reificaiton
;problem is the classes have different prefix, but are otherwise the same

(defn- subclass-nodes-of-type [t]
  (let [c (astnode/class-for-ekeko-keyword t)
        s (supers c)]
   ; (cond (contains? s org.aspectj.org.eclipse.jdt.core.dom.Expression)
   ;         (filter (fn [n] (instance? c n))
   ;           (nodes-of-type :Expression))
   ;       (contains? s org.aspectj.org.eclipse.jdt.core.dom.Statement)
   ;         (filter (fn [n] (instance? c n))
   ;           (nodes-of-type :Statement))
   ;       (contains? s org.aspectj.org.eclipse.jdt.core.dom.Type)
   ;         (filter (fn [n] (instance? c n))
   ;           (nodes-of-type :Type))
    ;      :else 
            (run* [?n] 
              (fresh [?cu]
                (ast :CompilationUnit ?cu)
                (child+ ?cu ?n)
                (succeeds (= c (class ?n)))))))



(defn- nodes-of-type [t]
  (let [models (projectmodel/aspectj-project-models)]
    (case t
      :CompilationUnit (mapcat (fn [aspectj-project-model]
                                 (.getAspectJCompilationUnits ^AspectJProjectModel aspectj-project-model)) models)
      (try (subclass-nodes-of-type t) (catch ClassNotFoundException e []))
      )))
                 

;; Elements
;; --------


(defn
  compilationunit
  "Relation of AJCompilationUnit instances."
  [?ajcu]
  (let [compilationunits 
        (mapcat (fn [model] (.getAJCompilationUnits ^AspectJProjectModel model))
                (projectmodel/aspectj-project-models))]
  (all
    (contains compilationunits ?ajcu))))


(defn 
  compilationunit-aspect
  "Relation between an AJCompilationUnit and one of the aspects 
   its defines using the aspect keyword."
  [?ajcu ?aspect]
  (all
    (compilationunit ?ajcu)
    (contains (.getAllAspects ^AJCompilationUnit ?ajcu) ?aspect)
    (succeeds (instance? AspectElement ?aspect))))

(defn
  aspect
  [?aspect]
  (fresh [?ajcu]
         (compilationunit-aspect ?ajcu ?aspect)))

;todo: include them in other relations
(defn 
  ajcu-aspectbyannotation
  "Relation between an AJCompilationUnit and one of the aspects 
   it defines using the @aspect annotation on a class. "
  [?ajcu ?sourcetype]
  (all
    (compilationunit ?ajcu)
    (contains (.getAllAspects ^AJCompilationUnit ?ajcu) ?sourcetype)
    (succeeds (instance? SourceType ?sourcetype))))

(defn
  aspect-advice
  "Relation between an aspect and one if its advices."
  [?aspect ?advice]
  (all 
    (aspect ?aspect)
    (contains (.getAdvice ^AspectElement ?aspect) ?advice)))

(defn
  advice
  "Relation of advices."
  [?advice]
  (fresh [?aspect]
         (aspect-advice ?aspect ?advice)))



(defn
  aspect-declare
  "Relation between an aspect and one of its declares."
  [?aspect ?declare]
  (all
    (aspect ?aspect)
    (contains (.getDeclares ^AspectElement ?aspect) ?declare)))

(defn
  declare
  "Relation of declares."  
  [?declare]
  (fresh [?aspect]
         (aspect-declare ?aspect ?declare)))

  
(defn
  aspect-pointcut
  "Relation between an aspect and one of its pointcuts."
  [?aspect ?pointcut]
  (all
    (aspect ?aspect)
    (contains (.getPointcuts ^AspectElement ?aspect) ?pointcut)))
         
(defn
  pointcut
  "Relation of pointcuts."
  [?pointcut]
  (fresh [?aspect]
         (aspect-pointcut ?aspect ?pointcut)))


(defn
  aspect-intertype
  "Relation between an aspect and one of its intertype declarations."
  [?aspect ?itd]
  (all
    (aspect ?aspect)
    (contains (.getITDs ^AspectElement ?aspect) ?itd)))

(defn
  intertype
  "Relation of intertype declarations."
  [?intertype]
  (fresh [?aspect]
         (aspect-intertype ?aspect ?intertype)))


(defn
  aspect-intertypemethod
  "Relation between an aspect and one of its intertype method declarations."
  [?aspect ?itd]
  (all 
    (aspect-intertype ?aspect ?itd)
    (succeeds (instance? MethodIntertypeElement ?itd))))

(defn
  intertypemethod
  "Relation of intertype method declarations."
  [?intertype]
  (fresh [?aspect]
         (aspect-intertypemethod ?aspect ?intertype)))

(defn
  aspect-intertypefield
  "Relation between an aspect and one of its intertype field declarations."
  [?aspect ?itd]
  (all 
    (aspect-intertype ?aspect ?itd)
    (succeeds (instance? FieldIntertypeElement ?itd))))

(defn
  intertypefield
  "Relation of intertype field declarations."
  [?intertype]
  (fresh [?aspect]
         (aspect-intertypefield ?aspect ?intertype)))


(defn
  aspect-field
  "Relation between an aspect and one of its fields."  
  [?aspect ?field]
  (all
    (aspect ?aspect)
    (contains (.getFields ^AspectElement  ?aspect) ?field)))

(defn
  field
  "Relation of fields declared within an aspect."
  [?field]
  (fresh [?aspect]
         (aspect-field ?aspect ?field)))


(defn-
  aspectj-imethod-represents-method?
  [^IMethod m]
  (not (or (instance? AdviceElement m)
           (instance? PointcutElement m)
           (instance? IntertypeElement m)
           (instance? DeclareElement m))))
  
(defn
  aspect-method
  "Relation between an aspect and one of its methods."  
  [?aspect ?method]
  (all
    (aspect ?aspect)
    (contains (.getMethods ^AspectElement  ?aspect) ?method)
    (succeeds (aspectj-imethod-represents-method? ?method))))

(defn
  method
  "Relation of methods declared within an aspect."
  [?method]
  (fresh [?aspect]
         (aspect-method ?aspect ?method)))


(defn
  element
  "Relation of IAspectJElement instances ?element and the keyword ?key representing their kind."
  [?key ?element]
  (conde
    [(== ?key :compilationunit)
     (compilationunit ?element)]
    [(== ?key :aspect) 
     (aspect ?element)]
    [(== ?key :advice) 
     (advice ?element)]
    [(== ?key :pointcut) 
     (pointcut ?element)]
    [(== ?key :declare) 
     (declare ?element)]
    [(== ?key :intertype) 
     (intertype ?element)]
    [(== ?key :intertypemethod) 
     (intertypemethod ?element)]
    [(== ?key :intertypefield) 
     (intertypefield ?element)]
    [(== ?key :method) 
     (method ?element)]
    [(== ?key :field) 
     (field ?element)]))



(defn
  element-model
  [?element ?model]
  (let [ekeko
        (damp.ekeko.ekekomodel/ekeko-model)]
  (fresh [?key]
         (element ?key ?element)
         (contains (.getProjectModel ^EkekoModel ekeko
                           ^IProject (.getProject (.getJavaProject ^IJavaElement ?element)))
                   ?model)
         (succeeds (instance? AspectJProjectModel ?model)))))

;; Link between model elements and AST nodes
;; -----------------------------------------

;unfinished:
(defn 
  element-ast
  [?element ?ast]
  (fresh [?elementKey ?astKey ?cu]
         (element ?elementKey ?element)
         (equals ?cu (astnode/ajcompilationunit-to-ast (.getCompilationUnit ^IAspectJElement ?element)))
         ;(child+ ?cu ?ast)
         ;todo: verify whether source positions correspond
         ))

;  model.javaElementToProgramElement(ae)
;						.getSourceLocation();



         
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

(defn- 
  xcut
  "Relation of all AJProjectModel facades."
  [?xcut]
  (conda [(v+ ?xcut)
          (succeeds (instance? AJProjectModelFacade ?xcut))]
         [(v- ?xcut)
          (contains (xcuts) ?xcut)]))

(defn-
  element-xcut
  "Relation between a model element ?element and the AJProjectModel ?xcut for the project in which it resides."
  [?element ?xcut]
  (fresh [?model]
         (element-model ?element ?model)
         (equals ?xcut (.getAJProjectFacade ^AspectJProjectModel ?model))
         (succeeds (.hasModel ^AJProjectModelFacade ?xcut))))


(defn
  xcut-relationtype-relation
  [?xcut ?relation-type ?relation]
  (fresh [?types]
         (contains (seq (AJRelationshipManager/getAllRelationshipTypes)) ?relation-type)
         (equals ?types (into-array AJRelationshipType [?relation-type]))
         (xcut ?xcut)
         (equals ?relation (.getRelationshipsForProject ^AJProjectModelFacade ?xcut ?types))))

;handle to JDT JavaElement
(defn-
  xcut-aje-je
  [?xcut ?handle ?element]
  (all 
    (equals ?element (.programElementToJavaElement ^AJProjectModelFacade ?xcut ^String ?handle))))

;handle to AspectJ ProgramElement
(defn-
  xcut-aje-pe
  [?xcut ?handle ?element]
  (all 
    (equals ?element (.toProgramElement ^AJProjectModelFacade ?xcut ^String ?handle))))

;JDT Javaelement to AspectJ ProgramElement
(defn
  xcut-je-pe
  [?xcut ?jdt-element ?aj-element]
  (all
    (equals ?aj-element (.javaElementToProgramElement ^AJProjectModelFacade ?xcut ?jdt-element))))



;javaElementToProgramElement(IJavaElement) goes from JDT world to AspectJ world

;; XCut shadows and advice

(defn-
  xcut-advicehandle-shadowhandle
  [?xcut ?advicehandle ?shadowhandle]
  (fresh [?relation ?relelement]
         (xcut-relationtype-relation ?xcut (AJRelationshipManager/ADVISES) ?relation)
         (contains ?relation ?relelement)
         (equals ?advicehandle (.getSourceHandle ?relelement))
         (contains (.getTargets ?relelement) ?shadowhandle)))

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
         (advice-shadow ?advice ?shadow)))



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

(defn-
  element-weaverworld
  "Relation between a model element ?element and the AJProjectModel ?xcut for the project in which it resides."
  [?element ?world]
  (fresh [?model]
         (element-model ?element ?model)
         (equals ?world (.getAJWorldAsSeenByWeaver ^AspectJProjectModel ?model))
         (!= nil ?world)))

;; precedences




;;todo: check whether this works when the aspects stem from different projects
;go to world
;
;In World (not AJWorldFacade), relevant methods:
;compareByPrecedence(ResolvedType firstAspect, ResolvedType secondAspect)
;getPrecedenceIfAny(ResolvedType aspect1, ResolvedType aspect2)
;compareByPrecedenceAndHierarchy(ResolvedType firstAspect, ResolvedType secondAspect) 
;
;need to go from aspect (jdt element) -> programelement (aspectj) -> signature -> resolvedtype
;programelement.toSignatureString
;

;world.resolveType(signature)

;;alternatief:
;world lookupBySignature


;;program element .getModel -> an AsmManager, which contains info about aspect structure

(defn
  pe-signature
  [?program-element ?signature-string]
  (all
    (equals ?signature-string (.toSignatureString ?program-element))))

(defn
  weaverworld-signature-resolvedtype
  [?world ?signature ?resolved-type]
  (equals ?resolved-type (.lookupBySignature ?world ?signature)))

;element-weaverworld


(comment 
(defn 
  aspect-explicitly-dominatedaspect
  [?dominator ?subordinate]
  (fresh [?dominator-pe ?subordinate-pe] 
    (element-xcut ?dominator ?xcut)
    (xcut-je-pe ?xcut ?dominator ?dominator-pe)
    
    (aspect ?subordinate)
    (xcut-je-pe ?xcut ?subordinate ?subordinate-pe)
    
    
    ))
)
  

;OR:
;go from declare (jdt element) -> programelement (aspectj, could be declarepattern) 
;and check what you can do there

(comment 
(damp.ekeko* [?declare ?declare-pe ?declare-pe-class]
        (fresh [?xcut]
               (declare ?declare)
               (element-xcut ?dominator ?xcut)
               (xcut-je-pe ?xcut ?declare ?declare-pe)
               ;take details from declare element
               ;contains types of two aspects
               ;then take aspect of that type
               ;yuk
)))


;(defn-
;  aspect-precedence
;  
;  )


