(ns
    ^{:doc "Low-level AspectJ WeaverWorld relations."
    :author "Coen De Roover, Johan Fabry" }
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
       [org.aspectj.weaver AdviceKind World ResolvedTypeMunger ConcreteTypeMunger ReferenceType UnresolvedType ResolvedType  Member]
       [org.aspectj.asm AsmManager IProgramElement IProgramElement$Kind IHierarchy ]
       [org.aspectj.weaver.bcel BcelTypeMunger]))
              

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
;; (but prefer not to use these) 
;; -----------------------------
    
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

(defn
  element-child+
  "Relation between a ProgramElement ?element and one of its descendants ?child."
  [?element ?child]
  (fresh [?ch]
         (element-child ?element ?ch)
         (conde [(== ?ch ?child)]	
                [(element-child+ ?ch ?child)])))

(defn
  element
  "Relation of elements of the ProgramElement hierarchy."
  [?element]
  (conda
    [(v+ ?element)
     (succeeds (instance? IProgramElement ?element))]
    [(v- ?element)
     (fresh [?root]
            (root ?root)
            (conde [(== ?element ?root)]
                   [(element-child+ ?root ?element)]))]))       

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

;note that these are not JVM signature strings, as produced for ResolvedType hierarchy
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



(defn-
  ancestors-of-element
  "Returns the ancestor chain of the given ProgramElement.
   First element is the immediate parent."
  ([element] 
    (ancestors-of-element (.getParent element) []))
  ([parent sofar]
    (if-not
      (nil? parent)
      (recur (.getParent parent) (conj sofar parent))
      sofar)))

(defn-
  enclosingtypedeclaration-of-element
  "Returns the most enclosing class, aspect or enum declaration
   in which the given ProgramElement resides."
  [element] 
  (letfn [(typedeclaration? 
            [element]
            (let [kind (.getKind element)]
              (or (= kind (IProgramElement$Kind/CLASS))
                  (= kind (IProgramElement$Kind/ENUM))
                  (= kind (IProgramElement$Kind/ASPECT)))))
          (ancestortypedeclaration 
            [parent]
            (if-not
              (nil? parent)
              (if 
                (typedeclaration? parent)
                parent
                (recur (.getParent parent)))))]
         (ancestortypedeclaration (.getParent element))))
    
(defn
  element-enclosingtypedeclaration-element
  "Relation between a ProgramElement and its enclosing aspect (if any)."
  [?element ?typedeclaration]
  (all
    (element ?element)
    (equals ?typedeclaration (enclosingtypedeclaration-of-element ?element))
    (!= nil ?typedeclaration)))

(defn
  elementaspect
  [?element]
  (all
    (element ?element)
    (equals (IProgramElement$Kind/ASPECT) (.getKind ?element))))

(defn
  elementclass
  [?element]
  (all
    (element ?element)
    (equals (IProgramElement$Kind/CLASS) (.getKind ?element))))

(defn
  elementenum
  [?element]
  (all
    (element ?element)
    (equals (IProgramElement$Kind/ENUM) (.getKind ?element))))

(defn
  elementtypedeclaration
  [?element]
  (conde
    [(elementenum ?element)]
    [(elementclass ?element)]
    [(elementaspect ?element)]))

    



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
;NO: because need JVM signature rather than what is returned by the xcut elements
;(defn
;  weaverworld-signature-resolvedtype
;  "Non-relational. Unifies ?resolved-type with the type known to the AspectJ weaver
;   ?world with the given ?signature string. 
;
;   Can be used to go from XCut types to weaver world types." 
;  [?world ?signature ?resolved-type]
;  (equals ?resolved-type (.resolve ?world ?signature)))


(defn
  element-type
  "Relation between a ProgramElement of kind Aspect/Enum/Class
   and the ResolvedType instance from the weaver world it corresponds to."
  [?element ?type]
  (fresh [?element-name] ;todo: take correct world into account
         (elementtypedeclaration ?element)
         (equals ?element-name (str (.getPackageName ?element) "." (.getName ?element))) ;todo: check what this returns for parameterized types
         (type ?type) 
         (equals ?element-name (.getName ?type)))) ;or .getBaseName if above includes type parameters

(defn
  aspect
  "Relation of aspects known to the weaver."
  [?aspect]
  (all
    ;;would normally use a conda here, but gives an exception in type
    ;;even though ?aspect is untouched in the first condition of each clause
    (conde 
      [(v+ ?aspect) 
       (succeeds (instance? ReferenceType ?aspect))]
      [(v- ?aspect)
       (type ?aspect)])
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
  aspect-declaredinterface
  "Relation between an aspect and one of the interfaces it declares to be implementing."
  [?aspect ?interface]
  (all
    (aspect ?aspect)
    (contains (.getDeclaredInterfaces ?aspect) ?interface)))

(defn
  aspect-declaredsuper
  "Relation between an aspect and its super class (including java.lang.Object for aspects
   that do not declare a super aspect)."
  [?aspect ?super]
  (all
    (aspect ?aspect)
    (equals ?super (.getSuperclass ?aspect))))

(defn
  aspect-declaredsuperaspect
  "Relation between an aspect and its declared super aspect. 
   Excludes non-aspect super types (e.g., the implicit java.lang.Object). "
  [?aspect ?super]
  (all
    (aspect-declaredsuper ?aspect ?super)
    (aspect ?super)))

(defn
  aspect-declaredsuperclass
  "Relation between an aspect and its declared super class.
   Excludes aspect super types."
  [?aspect ?super]
  (all
    (aspect-declaredsuper ?aspect ?super)
    (succeeds (.isClass  ?super))))


(def
  aspect-declaredsuper+
  "Relation between an aspect and one of the ancestors in its declared super hierarchy."
  (tabled
    [?aspect ?ancestor]
    (conde
      [(aspect-declaredsuper ?aspect ?ancestor) ]
      [(fresh [?inbetween]
              (aspect-declaredsuper+ ?aspect ?inbetween)
              (aspect-declaredsuper ?inbetween ?ancestor))])))

(defn
  aspect-declaredsuperaspect+
  "Relation between an aspect and one of the ancestor aspects in its declared super hierarchy."
  [?aspect ?ancestor]
  (all
    (aspect-declaredsuper+ ?aspect ?ancestor)
    (aspect ?ancestor)))

(defn
  aspect-declaredsuperclass+
  "Relation between an aspect and one of the ancestor classes in its declared super hierarchy."
  [?aspect ?ancestor]
  (all
    (aspect-declaredsuper+ ?aspect ?ancestor)
    (succeeds (.isClass  ?ancestor))))


;todo: wantgenerics is false, check whether this is compatible with the types returned by the other predicates
;(i.e., test with a parameterized aspec)
(defn
  aspect-super+
  "Relation between an aspect and one of its direct or indirect
   super types (classes, aspects as well as interfaces),
   including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (!= ?type ?aspect)
    (aspect ?aspect)
    (contains (iterator-seq (.getHierarchy ?aspect false true)) ?type)))

(defn
  aspect-superaspect+
  "Relation between an aspect and one of its direct or indirect
   super aspects, including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (aspect-super+ ?aspect ?type)
    (aspect ?type)))


(defn
  aspect-superinterface+
  "Relation between an aspect and one of its direct or indirect
   super interfaces, including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (aspect-super+ ?aspect ?type)
    (succeeds (.isInterface ?type))))

(defn
  aspect-superclass+
  "Relation between an aspect and one of its direct or indirect
   super classes, including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (aspect-super+ ?aspect ?type)
    (succeeds (.isClass ?type))))
  


;; Advice
;; ------

(defn
  aspect-advice
  "Relation between an aspect and one of the advices it declares."
  [?aspect ?advice]
  (fresh [?members]
         (aspect-crosscuttingmembers ?aspect ?members)
         (contains (.getShadowMungers ?members) ?advice)))

; Prefer the above because going through the CrosscuttingMembersSet has proven more reliable for intertype declarations
;(defn
;  aspect-advice
;  "Relation between an aspect and one of the advices it declares."
;  [?aspect ?advice]
;  (all
;    (aspect ?aspect)
;    (contains (.getDeclaredAdvice ?aspect) ?advice)))

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

(defn
  advice-sourcelocation
  "Relation between an advice and its location in the source code (an ISourceLocation)."
  [?advice ?location]
  (all
    (advice ?advice)
    (equals ?location (.getSourceLocation ?advice))))

(defn
  advice-kind
  "Relation between an advice and its kind (an AdviceKind)."
  [?advice ?kind]
  (all
    (advice ?advice)
    (equals ?kind (.getKind ?advice))))

(defn
  advice-kindprecedence 
  "Relation between an advice and a number representing
   the precedence associated with its kind (see AdviceKind).
   E.g., before=1, after=2, afterThrowing=3, afterReturning=4,around=5"
  [?advice ?precedence]
  (fresh [?kind]
         (advice-kind ?advice ?kind)
         (equals ?precedence (.getPrecedence ?kind))))

;todo: there are also advice kinds that cannot be declared, but are used by the weaver (see AdviceKind)

(defn
  advicebefore 
  "Relation of before advices."
  [?advice]
  (fresh [?kind]
    (advice-kind ?advice ?kind)
    (equals ?kind AdviceKind/Before)))

(defn
  adviceafter
  "Relation of after advices."
  [?advice]
  (fresh [?kind]
    (advice-kind ?advice ?kind)
    (equals ?kind AdviceKind/After)))

(defn
  adviceafterthrowing
  "Relation of after throwing advices."
  [?advice]
  (fresh [?kind]
    (advice-kind ?advice ?kind)
    (equals ?kind AdviceKind/AfterThrowing)))

(defn
  adviceafterreturning
  "Relation of after returning advices."
  [?advice]
  (fresh [?kind]
    (advice-kind ?advice ?kind)
    (equals ?kind AdviceKind/AfterReturning)))

(defn
  advicearound
  "Relation of around advices."
  [?advice]
  (fresh [?kind]
    (advice-kind ?advice ?kind)
    (equals ?kind AdviceKind/Around)))


;; Intertype declarations
;; ----------------------


;Only returned one ITD of XCutTest
;(defn
;  aspect-intertype 
; "Relation between an aspect and one of its intertype declarations."
; [?aspect ?intertype]
;  (all
;    (aspect ?aspect)
;    (contains (.getInterTypeMungers ?aspect) ?intertype)))



(defn
  aspect-intertype 
  "Relation between an aspect and one of its intertype declarations (a ConcreteTypeMunger)."
  [?aspect ?intertype]
  (fresh [?members]
    (aspect-crosscuttingmembers ?aspect ?members)
    (contains (.getTypeMungers ?members) ?intertype)))


(defn
  intertype
  [?intertype]
  "Relation of intertype declarations."
  (conda
    [(v+ ?intertype) 
     (succeeds (instance? ConcreteTypeMunger ?intertype))]
    [(v- ?intertype) 
     (fresh [?aspect]
         (aspect-intertype ?aspect ?intertype))]))         
  
;note: ConcreteTypeMunger.getMunger returns a ResolvedTypeMunger (from a previous weaving stage)

(defn
  intertype-kind
  "Relation between an intertype declaration and its kind."
  [?intertype ?kind]
  (all
    (intertype ?intertype)
    (equals ?kind (-> ?intertype .getMunger .getKind))))

(defn
  intertype-field
  "Relation of field intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Field))))

(defn
  intertype-method
  "Relation of method intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Method))))

(defn
  intertype-constructor
  "Relation of constructor intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Constructor))))


(defn
  intertype-member-type
  "Relation between an intertype declaration, the field/method/constructor member it declares
  (a ResolvedMemberImpl), and the type to which this member is added (a ResolvedType). "
  [?intertype ?member ?type]
  (fresh [?unresolved]
    (intertype ?intertype)
    (equals ?member (.getSignature ?intertype))
    (equals ?unresolved (.getDeclaringType ?member))
    (equals ?type (.resolve ?unresolved (.getWorld ?intertype)))))
  
   
;; implemented in this manner because I don't know how to create a handle for a ConcreteTypeMunger / ResolvedMemberImpl 
;; AsmRelationShipProviver.createIntertypeDeclaredChild(AsmManager model, ResolvedType aspect, BcelTypeMunger itd) {
(defn
  intertype-element
  "Relation between an intertype declaration and its resulting member element (a ProgramElement) 
   in the program element hierarchy.

   These are of the same type as the shadows we return for an advice, thus allowing checking
   whether a shadow stems from an intertype declaration.

   Prefer to use the above intertype-member-type instead, as it does not escape the weaver world."
  [?intertype ?member]
  (fresh [?equivalentmember ?signaturestring]
         (intertype ?intertype)
         (equals 
           ?equivalentmember
           (interop/call-invisible-method 
             AsmRelationshipProvider
             'createIntertypeDeclaredChild
             [AsmManager ResolvedType BcelTypeMunger]
             nil
             (-> ?intertype .getWorld .getModel)
             (-> ?intertype .getAspectType)
             ?intertype))
         (equals ?signaturestring (.toSignatureString ?equivalentmember))
         (element-signature ?member ?signaturestring))) 
  


;; SourceLocation
;; --------------


(defn
  sourcelocation-filename-startline-endline-column-offset 
  "Non-relational. Unifies ?filename with the name of the file for the given ?sourcelocation, 
   ?startline with the line at which it starts, ?endline with the line at which it ends, 
   ?column with the column at which it starts, and ?offset with its offset."
  [?sourcelocation ?filename ?startline ?endline ?column ?offset]
  (all
    (equals ?filename (.getSourceFileName ?sourcelocation))
    (equals ?startline (.getLine ?sourcelocation))
    (equals ?endline (.getEndLine ?sourcelocation))
    (equals ?column (.getColumn ?sourcelocation))
    (equals ?offset (.getOffset ?sourcelocation))))

;; Pointcuts
;; ---------


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
;; --------------------

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
  


;(defn
;  aspect-pointcut+
;  [?aspect ?pointcut]
;  (all
;    (aspect ?aspect)
;    (contains (iterator-seq (.getPointcuts ?aspect)) ?pointcut)))

;; Declare declarations
;; --------------------


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
    (equals 1 (.compare ?decprec ?dominator ?subordinate))))

(defn
  aspect-dominates-aspect-explicitly+
  "Transitive explicit dominates relationship"
    [?dominator ?subordinate]
    (conde [(fresh [?prec]
                   (aspect-dominates-aspect-explicitly ?dominator ?subordinate ?prec))]
           [(fresh [?intermediate ?prec]
                   (aspect-dominates-aspect-explicitly ?dominator ?intermediate ?prec)
                   (aspect-dominates-aspect-explicitly+ ?intermediate ?subordinate))]))

;	precedence regels: sub-aspects implicitly have precedence over their super-aspect;	lexically first advice has implicit precedence over second advice;	(voor advice van hetzelfde aspect)

(defn
  aspect-dominates-aspect-implicitly+
  "Implicit aspect domination relationship: a subaspect dominates its superaspect."
  [?dominator ?subordinate]
  (all
    (aspect-superaspect+ ?dominator ?subordinate)))


(comment
  ;; TODO: check how core.logic implements tabling
  ;; this one worked fine when used as (damp.ekeko/ekeko* [?dom ?sub] (aspect-dominates-aspect ?dom ?sub)) but:
  ;; 1) an exception is raised when used as follows:
  ;;      (damp.ekeko/ekeko* [?first ?second] (aspect ?first) (aspect ?second) (!= ?first ?second)  (fails (aspect-dominates-aspect ?first ?second)))
  ;;      IllegalArgumentException No implementation of method: :ifu of protocol: #'clojure.core.logic/IIfU found for class: clojure.lang.PersistentVector  clojure.core/-cache-protocol-fn (core_deftype.clj:495)
  ;;   where fails implements negation as failure through condu
  ;; 2) the atom used by the tabled macro to store results is not aware of workspace changes (e.g., new aspects)
  (def
    aspect-dominates-aspect
    "Precedence domination relation between an aspect and its subordinate,
   by combining explicit precedence declarations with implicit precedence relations."
  (tabled 
    [?dominator ?subordinate]
    (all
      (conde
        [(aspect-dominates-aspect-explicitly+ ?dominator ?subordinate)]
        [(aspect-dominates-aspect-implicitly+ ?dominator ?subordinate)]
        [(fresh [?intermediate]
                (aspect-dominates-aspect ?dominator ?intermediate)
                (aspect-dominates-aspect ?intermediate ?subordinate))])
      (fails (aspect-dominates-aspect-explicitly+ ?subordinate ?dominator))))))

(defn- 
  domination-edge
  [?d ?s]
  (all 
    (conde 
      [(aspect-dominates-aspect-explicitly+ ?d ?s)]
      [(aspect-dominates-aspect-implicitly+ ?d ?s)])))

;not very declarative, see TODO about tabling above
;perhaps todo: 1-dom->5 is repeatd in results
(defn
  aspect-dominates-aspect
  "Precedence domination relation between an aspect and its subordinate,
   by combining explicit precedence declarations with implicit precedence relations."
  ([?dominator ?subordinate]
    (all
      (!= ?dominator ?subordinate)
      (aspect-dominates-aspect ?dominator ?subordinate [])))
  ([?dominator ?subordinate ?explored-subs]
    (fresh [?sub]
           (domination-edge ?dominator ?sub)
           (fails (contains ?explored-subs ?sub))
           (conde
             [(== ?subordinate ?sub)]
             [(fresh [?new-explored-subs]
                     (equals ?new-explored-subs (conj ?explored-subs ?sub))
                     (aspect-dominates-aspect ?sub ?subordinate ?new-explored-subs))])
           (fails (aspect-dominates-aspect-explicitly+ ?subordinate ?dominator)))))
           

;; Link between World (aka weaverworld) and AJProjectModelFacade (aka xcut)

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
    
;; Shadows


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
                       (xcut/xcut-handle-pe ?xcut ?shadowh ?shadow)))))


(defn
  shadow
  [?shadow]
  (fresh [?advice]
         (advice-shadow ?advice ?shadow)))

(defn
  shadow-enclosingtypedeclaration
  [?shadow ?type]
  (fresh [?element]
    (shadow ?shadow)
    (element-enclosingtypedeclaration-element  ?shadow ?element)
    (element-type ?element ?type)))

  

    