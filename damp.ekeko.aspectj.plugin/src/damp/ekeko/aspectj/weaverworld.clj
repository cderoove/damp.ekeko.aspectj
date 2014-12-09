(ns
  ^{:doc "Low-level AspectJ WeaverWorld relations."
    :author "Coen De Roover, Johan Fabry" }
damp.ekeko.aspectj.weaverworld
  (:refer-clojure :exclude [== type declare class])
  (:use [clojure.core.logic])
  (:use [damp.ekeko logic])
  (:require 
    [damp.util [interop :as interop]]
    [damp.ekeko.aspectj [projectmodel :as projectmodel]]
    [damp.ekeko.aspectj [xcut :as xcut]])
  (:import 
    [org.eclipse.ajdt.core.model AJProjectModelFacade AJRelationshipType AJRelationshipManager]
    [org.aspectj.weaver.model AsmRelationshipProvider]
    [org.aspectj.weaver.patterns Pointcut AndPointcut Declare DeclarePrecedence DeclareAnnotation DeclareErrorOrWarning DeclareParents DeclarePrecedence DeclareSoft DeclareTypeErrorOrWarning PatternParser]
    [damp.ekeko EkekoModel]
    [damp.ekeko.aspectj AspectJProjectModel]
    [org.aspectj.weaver AnnotationAJ Advice AdviceKind World ResolvedTypeMunger ConcreteTypeMunger ReferenceType UnresolvedType ResolvedType  Member ResolvedMemberImpl]
    [org.aspectj.asm AsmManager IProgramElement IProgramElement$Kind IHierarchy ]
    [org.aspectj.weaver.bcel BcelTypeMunger]
    [org.aspectj.weaver.tools StandardPointcutParser TypePatternMatcher]
    [org.aspectj.apache.bcel.classfile.annotation ElementValue AnnotationElementValue ArrayElementValue ClassElementValue EnumElementValue SimpleElementValue]
    ))


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
          (fresh [?worlds]
                 (equals ?worlds (weaverworlds))
                 (contains ?worlds ?world))]))


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
;; ------------------------

;; Prefer not to expose these relations, as the hierarchy is still managed by the facade created by AsmRelationshipProvider


(defn-
  root
  "Relation of ProgramElement hierarchy roots."
  [?element]
  (fresh [?world ?model ?hierarchy]
         (weaverworld-model-hierarchy ?world ?model ?hierarchy)
         (equals ?element (.getRoot ?hierarchy))))

(clojure.core/declare element) 

(defn- 
  element-child
  "Relation between a ProgramElement ?element and one of its children ?child."
  [?element ?child-element]
  (fresh [?children]
         (element ?element)
         (equals ?children (.getChildren ?element))
         (contains  ?children ?child-element)))

(defn-
  element-child+
  "Relation between a ProgramElement ?element and one of its descendants ?child."
  [?element ?child]
  (fresh [?ch]
         (element-child ?element ?ch)
         (conde [(== ?ch ?child)]	
                [(element-child+ ?ch ?child)])))

(defn-
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

(defn-
  element-parent
  "Relation between a ProgramElement and its parent ProgramElement."
  [?element ?parent]
  (all
    (element ?element)
    (equals ?parent (.getParent ?element))))


(defn-
  element-kind
  "Relation between a ProgramElement ?element and its kind ?kind."
  [?element ?kind]
  (all
    (element ?element)
    (equals ?kind (.getKind ?element))))

(defn-
  element-signature
  "Relation between a ProgramElement and its signature string.
   Unfortunately, these are not JVM signature strings, as produced for ResolvedType hierarchy."
  [?element ?signature]
  (all
    (element ?element)
    (equals ?signature (.toSignatureString ?element))))

(defn-
  element-handle
  "Relation between a ProgramElement and its handle."
  [?element ?handle]
  (all
    (element ?element)
    (equals ?handle (.getHandleIdentifier ?element))))

(defn-
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
  ancestor-element-satisfying
  "Returns the first ancestor of the given IProgramElement 
   that satisfies the given function."
  [element testf] 
  (loop 
    [parent (.getParent element)]
    (if-not
      (nil? parent)
      (if 
        (testf parent)
        parent
        (recur (.getParent parent))))))


(defn-
  element-ancestor|type
  "Relation between a ProgramElement and its first ancestor ProgramElement 
   that is of kind CLASS, INTERFACE, ASPECT, ANNOTATION or ENUM (if any)."
  [?element ?etype]
  (all
    (element ?element)
    (equals ?etype (ancestor-element-satisfying 
                     ?element
                     (fn [ancestor] 
                       (.isType (.getKind ancestor)))))
    (!= nil ?etype)))


(defn-
  element-ancestor|member
  "Relation between a ProgramElement and its first ancestor ProgramElement 
   that is of kind FIELD, METHOD, CONSTRUCTOR, POINTCUT, ADVICE, ENUM_VALUE (if any).
   Does not consider intertype members INTER_TYPE_CONSTRUCTOR, INTER_TYPE_FIELD, INTER_TYPE_METHOD."
  [?element ?emember]
  (all
    (element ?element)
    (equals ?emember (ancestor-element-satisfying 
                       ?element
                       (fn [ancestor] 
                         (.isMember (.getKind ancestor)))))
    (!= nil ?emember)))


(defn-
  element-ancestor|intertypemember
  "Relation between a ProgramElement and its first ancestor ProgramElement 
   that is of kind INTER_TYPE_CONSTRUCTOR, INTER_TYPE_FIELD or INTER_TYPE_METHOD."
  [?element ?emember]
  (all
    (element ?element)
    (equals ?emember (ancestor-element-satisfying 
                       ?element
                       (fn [ancestor] 
                         (.isInterTypeMember (.getKind ancestor)))))
    (!= nil ?emember)))


(defn-
  element-ancestor-kind
  "Semi-relation between a ProgramElement and its first ancestor ProgramElement of kind IProgramElement$Kind,
   including the ProgramElement itself.
   Last argument has to be ground."
  [?element ?enclosing ?kind]
  (all
    (element ?element)
    (equals ?enclosing 
            (ancestor-element-satisfying 
              ?element
              (fn [ancestor] 
                (= ?kind (.getKind ancestor)))))
    (!= nil ?enclosing)))


(defn-
  element|aspect
  [?element]
  (all 
    (element-kind ?element (IProgramElement$Kind/ASPECT))))

(defn-
  element|class
  [?element]
  (all
    (element-kind ?element (IProgramElement$Kind/CLASS))))


(defn-
  element|interface
  [?element]
  (all 
    (element-kind ?element (IProgramElement$Kind/INTERFACE))))


(defn-
  element|enum
  [?element]
  (all
    (element-kind ?element (IProgramElement$Kind/ENUM))))

(defn-
  element|type
  [?element]
  (fresh [?kind]
         (element-kind ?element ?kind)
         (succeeds (.isType ?kind))))

(defn-
  element|member
  [?element]
  (fresh [?kind]
         (element-kind  ?element ?kind)
         (succeeds (.isMember ?kind))))


(defn-
  element|method
  [?element]
  (fresh [?kind]
         (element-kind ?element ?kind)
         (== (IProgramElement$Kind/METHOD) ?kind)))

(defn-
  element|constructor
  [?element]
  (fresh [?kind]
         (element-kind ?element ?kind)
         (== (IProgramElement$Kind/CONSTRUCTOR) ?kind)))

(defn-
  element|advice
  [?element]
  (fresh [?kind]
         (element-kind ?element ?kind)
         (== (IProgramElement$Kind/ADVICE) ?kind)))



;; Actual Reification of WeaverWorld
;; ---------------------------------


(defn-
  weaverworld-typemap
  "Relation between an AspectJ weaverworld and its non-expandable TypeMap."
  [?world ?typemap]
  ;see org.aspectj.weaver.World$TypeMap, consider configuring TypeMap.DONT_USE_REFS to avoid the weaver from discarding expandable types.
  (all 
    (weaverworld ?world)
    (equals ?typemap (seq (-> ?world .getTypeMap .getMainMap)))))


(defn
  type
  "Relation of non-expandable types known to the AspectJ weaver.
   Instances of org.aspectj.weaver.ResolvedType."
  [?resolvedtype]
  (conda [(v+ ?resolvedtype)
          (succeeds (instance? org.aspectj.weaver.ResolvedType ?resolvedtype))]
         [(v- ?resolvedtype)
          (fresh [?world ?map ?entry]
                 (weaverworld-typemap ?world ?map)
                 (contains ?map ?entry)
                 (equals ?resolvedtype (.getValue ?entry)))]))


(defn
  interface
  "Relation of interface types known to the AspectJ weaver."
  [?resolvedtype]
  (all 
    (type ?resolvedtype)
    (succeeds (.isInterface ?resolvedtype))))

(defn
  class
  "Relation of class types known to the AspectJ weaver."
  [?resolvedtype]
  (all 
    (type ?resolvedtype)
    (succeeds (.isClass ?resolvedtype))))


;problem: typemap only returns user-defined annotation types
;i.e. those for which .isExposedToWeaver returns true
;see comment on annotation/1
(defn
  type|annotation
  "Relation of annotation type declarations known to the AspectJ weaver."
  [?annotypedeclaration]
  (all 
    (type ?annotypedeclaration)
    (succeeds (.isAnnotation ?annotypedeclaration))))


(defn
  anonymous
  "Relation of anonymous types known to the AspectJ weaver."
  [?resolvedtype]
  (all 
    (type ?resolvedtype)
    (succeeds (.isAnonymous ?resolvedtype))))


(defn
  enum
  "Relation of enum types known to the AspectJ weaver."
  [?resolvedtype]
  (all 
    (type ?resolvedtype)
    (succeeds (.isEnum ?resolvedtype))))

(defn
  nestedtype
  "Relation of nested types known to the AspectJ weaver. 
   This includes: member, local and anonymous types."
  [?resolvedtype]
  (all 
    (type ?resolvedtype)
    (succeeds (.isNested ?resolvedtype))))

(defn
  topleveltype
  "Relation of non-nested types known to the AspectJ weaver."
  [?type]
  (all
    (type ?type)
    (equals false (.isNested ?type))))

(defn
  nestedtype-outertype
  "Relation between a nested and its immediate outer type.

   e.g., (ekeko* [?i ?o]  (interface ?i) (nestedtype-outertype ?i ?o))

   See also nestedtype-outermostype/2"
  [?nested ?outer]
  (all 
    (nestedtype ?nested)
    (equals ?outer (.getOuterClass ?nested)))) ;also returns other outer types

(defn
  nestedtype-outertype+
  "Relation between a nested and one of its encompassing outer types."
  [?nested ?outer]
  (conde
    [(nestedtype-outertype ?nested ?outer)]
    [(fresh [?o]
            (nestedtype-outertype ?nested ?o)
            (nestedtype-outertype ?o ?outer))]))


(defn
  nestedtype-outermosttype
  "Relation between a nested and its outermost type."
  [?nested ?outermost]
  (fresh [?outer] 
         (nestedtype-outertype ?nested ?outer)
         (conda [(nestedtype ?outer)
                 (nestedtype-outermosttype ?outer ?outermost)]
                [(== ?outermost ?outer)])))


(defn
  aspect
  "Relation of aspects known to the weaver."
  [?aspect]
  (all
    (type ?aspect)
    (succeeds (.isAspect ?aspect))))

(defn
  aspect|abstract
  "Relation of abstract aspects known to the weaver."
  [?aspect]
  (all
    (aspect ?aspect)
    (succeeds (.isAbstract ?aspect))))

(defn-
  aspect-crosscuttingmembers
  "Relation between an aspect and its CrosscuttingMembersSet."
  [?aspect ?members]
  (fresh [?set]
         (aspect ?aspect)
         (equals ?set (-> ?aspect .getWorld .getCrosscuttingMembersSet))
         (equals
           ?members
           (seq (.get 
                  (interop/get-invisible-field (class ^CrosscuttingMembersSet ?set)
                                               (symbol "members")
                                               ?set)
                  ?aspect)))))


(defn
  type-fields
  "Relation between a type and its declared fields as known to the weaver.
   For fields that do not declare a field, ?fields==[]."
  [?resolvedtype ?fields]
  (all
    (type ?resolvedtype)
    (equals ?fields (vec (.getDeclaredFields ?resolvedtype))))) 

(defn
  type-fields+
  "Relation between a type and all of its declared and inherited fields (as known to the weaver)."
  ;;see org.apectj.weaver.ResolvedType.getFields()
  [?resolvedtype ?fields]
  (all
    (type ?resolvedtype)
    (equals ?fields (vec (iterator-seq (.getFields ?resolvedtype))))))

(defn
  type-field
  "Relation between a type and one of its declared fields as known to the weaver."
  [?resolvedtype ?field]
  (fresh [?fields]
         (type-fields ?resolvedtype ?fields)
         (contains ?fields ?field)))

(defn
  type-field+
  "Relation between a type and one of its declared or inherited fields (as known to the weaver).

  e.g., inherited fields:
  (damp.ekeko/ekeko* [?t ?f] 
                  (type-field+ ?t ?f)
                  (fails 
                    (type-field ?t ?f)))"
  [?resolvedtype ?field]
  (fresh [?fields]
         (type-fields+ ?resolvedtype ?fields)
         (contains ?fields ?field)))

(defn
  field
  "Relation of field members known to the weaver."
  [?field]
  (fresh [?type]
         (type-field ?type ?field)))

(clojure.core/declare annotated-annotation)


(defn
  field-annotation
  "Relation between a field member and one of its annotations."
  [?field ?annotation]
  (all
    (field ?field)
    (annotated-annotation ?field ?annotation)))
  

(defn
  type-methods
  "Relation between a type and its declared methods as known to the weaver."
  [?resolvedtype ?methods]
  (all
    (type ?resolvedtype)
    (equals ?methods (vec (.getDeclaredMethods ?resolvedtype)))))


(defn
  type-methods+
  "Relation between a type and all of its declared and inherited methods (as known to the weaver).
   Includes methods added through an ITD."
  ;;see org.apectj.weaver.ResolvedType.getMethods(boolean wantGenerics, boolean wantDeclaredParents)
  [?resolvedtype ?methods]
  (all
    (type ?resolvedtype)
    (equals ?methods (vec (iterator-seq (.getMethods ?resolvedtype true true))))))

(defn
  type-method
  "Relation between a type and one of its declared methods as known to the weaver."
  [?resolvedtype ?method]
  (fresh [?methods]
         (type-methods ?resolvedtype ?methods)
         (contains ?methods ?method)))

(defn
  type-method+
  "Relation between a type and one of its declared or inherited methods (as known to the weaver)."
  [?resolvedtype ?method]
  (fresh [?methods]
         (type-methods+ ?resolvedtype ?methods)
         (contains ?methods ?method)))


(defn
  method
  "Relation of method members known to the weaver.
   Use member|nonsynthetic/1 to filter out synthetic methods created to support the weaver."
  [?method]
  (fresh [?type]
         (type-method ?type ?method)))


(defn
  method-annotation
  [?method ?annotation]
  (all
    (method ?method)
    (annotated-annotation ?method ?annotation)))


(clojure.core/declare type-pointcutdefinition)


(defn 
  type-member
  "Relation between a type and one of its declared members (i.e., a field / method / pointcutdefinition)."
  [?type ?member]
  (all
    (type ?type)
    (conde [(type-method ?type ?member)]
           [(type-field ?type ?member)]
           [(type-pointcutdefinition ?type ?member)]
           ;[(type-intertype ?type ?member)] not a subtype of ResolvedMember          
           )))


(defn
  member
  "Relation of declared members known to the weaver."
  [?member]
  (conda
    [(v+ ?member)
     (succeeds (instance? org.aspectj.weaver.ResolvedMember ?member))]
    [(v- ?member)
     (fresh [?type]
            (type-member ?type ?member))]))

(defn- 
  type|ajsynthetic?
  [?type ?boolean]
  (all
    (equals ?boolean (.isSynthetic ?type))))


(defn
  type|nonsynthetic
  "Relation of types that are not AspectJ synthetic."
  [?type]
  (all
    (type ?type)
    (type|ajsynthetic? ?type false)))

(defn
  type|synthetic
  "Relation of types that are AspectJ synthetic."
  [?type]
  (all
    (type ?type)
    (type|ajsynthetic? ?type true)))


(defn
  type|reference
  "Relation of reference (i.e., non-primitive types) known to the AspectJ weaver."
  [?type]
  (all 
    (type ?type)
    (succeeds (instance? ReferenceType ?type))))


(defn-
  annotated-annotations
  [?target ?annos]
  (all
    (v+ ?target)
    (equals ?annos (.getAnnotations ?target))))


(clojure.core/declare annotation)

(defn-
  annotated-annotation
  [?target ?anno]
  (fresh [?annos]
         (annotated-annotations ?target ?annos)
         (contains ?annos ?anno)
         (annotation ?anno) ;to ensure only those of which the type is exposed to the weaver are returned
         ))

(defn
  type-annotation
  "Relation between a type declarations and its annotation instances."
  [?type ?annotation]
  (all
    (type|reference ?type)
    (annotated-annotation ?type ?annotation)))

(defn
  annotation
  "Relation of annotation instances exposed to the AspectJ weaver.
   Does not return instances of e.g., library-definined annotation types such as @Retention or @Target.
   See type|annotation/1 for the relation of all annotation types exposed to the weaver."
  [?annotation]
  (all 
    (conda 
      [(v+ ?annotation)
       (succeeds (instance? AnnotationAJ ?annotation))]
      [(v- ?annotation)
       (fresh [?target]
              (conde [(type-annotation ?target ?annotation)]
                     [(method-annotation ?target ?annotation)]
                     [(field-annotation ?target ?annotation)]
                     ) 
              )])
    (succeeds (.isExposedToWeaver (.getType ?annotation)))))
;.isExposedToWeaver is to align the results of type|annotation/1 and annotation-type/1
;because the TypeMap reified by type/1 does not contain entries for e.g., library-defined annotation types
;otherwise:
;-> 4 solutions: 
; (annotation-annotationtype ?annotation ?type)
; (type|annotation ?type)
;-> 2 solutions:
; (type|annotation ?type)
; (annotation-annotationtype ?annotation ?type)


(defn
  annotation-annotationtype
  "Relation between an annotation instance and its type (i.e., a ResolvedType instance).
   See type|annotation/1 for the relation of all annotation types."
  [?annotation ?annotationtype]
  (all
    (annotation ?annotation)
    (equals ?annotationtype (.getType ?annotation))))


(defn-
  annotation-values
  [?annotation ?values]
  (fresh [?bcel]
        (annotation ?annotation)
        (equals ?bcel (.getBcelAnnotation ?annotation))
        (equals ?values (.getValues ?bcel))))


;todo: also support AnnotationElementValue, EnumElementValue
(defprotocol IBCelToWeaver
  (toweavervalue [bcel world]))

(extend-type ElementValue IBCelToWeaver
  (toweavervalue [bcel world]
    (.stringifyValue bcel)))

(extend-type ClassElementValue IBCelToWeaver
  (toweavervalue [bcel world]
    (.lookupBySignature world (.getClassString bcel))))

(extend-type SimpleElementValue IBCelToWeaver
  (toweavervalue [bcel world]
    (let [t (.getElementValueType bcel)]
      (if 
        (= t ElementValue/STRING)
        (.getValueString bcel)
        (read-string (.stringifyValue bcel))))))

(extend-type ArrayElementValue IBCelToWeaver
  (toweavervalue [bcel world]
    (into [] (map (fn [el] (toweavervalue el world)) (.getElementValuesArray bcel)))))

;(extend-type EnumElementValue IBCelToWeaver
;  (toweavervalue [bcel world]
;    (.lookupBySignature world (.getEnumTypeString bcel)) ;what about the getEnumValueString?
;    ))



(defn
  annotation-key-value
  "Relation of an annotation, one of its keys (a String), and the value of this key.
   Currently only supports values of type Class (reified as elements of relation type/1) or of a primitive type (reified as Java values),
   or an array of such values.
   Other values are reified to their string representation."
  [?annotation ?name ?value]
  (fresh [?pairs ?pair ?bcelvalue ?world] ;of type org.aspectj.apache.bcel.classfile.annoation.ElementValue
         (annotation ?annotation)
         (equals ?world (.getWorld (.getType ?annotation)))
         (annotation-values ?annotation ?pairs)
         (contains ?pairs ?pair)
         (equals ?name (.getNameString ?pair))
         (equals ?bcelvalue (.getValue ?pair))
         (equals ?value (toweavervalue ?bcelvalue ?world))
         ))



(defn- 
  member|ajsynthetic?
  [?member ?boolean]
  (all
    ;;note: do not use .isSynthetic on ResolvedMember, 
    ;;those are java-based synthetics (rather than ajc-based synthetics)
    (equals ?boolean (.isAjSynthetic ?member))))



(defn
  member|synthetic
  "Relation of members that are AspectJ synthetic (e.g., hasAspect())."
  [?member]
  (all
    (member ?member)
    (member|ajsynthetic? ?member true)))

;;TODO: perhaps filter out using signature?
(defn
  member|nonsynthetic
  "Relation of members that are not AspectJ synthetic. Still includes members implementing ITD."
  [?member]
  (all
    (member ?member)
    (member|ajsynthetic? ?member false)))

(defn
  type-declaredinterface
  "Relation between a type and one of the interfaces it 
   declares to be implementing (for a class and aspect)
   or extending (for an interface)."
  [?type ?interface]
  (fresh [?interfaces]
         (type ?type)
         (equals ?interfaces (.getDeclaredInterfaces ?type))
         (contains ?interfaces ?interface)))  

(defn
  type-declaredinterface+
  "Relation between a type and one of the ancestors in its declared interface hierarchy."
  [?type ?ancestor]
  (conde
    [(type-declaredinterface ?type ?ancestor) ]
    [(fresh [?inbetween]
            (type-declaredinterface ?type ?inbetween)
            (type-declaredinterface+ ?inbetween ?ancestor))]))

(defn
  aspect-declaredinterface
  "Relation between an aspect and one of the interfaces it declares to be implementing."
  [?aspect ?interface]
  (all
    (aspect ?aspect)
    (type-declaredinterface ?aspect ?interface)))


(defn
  type-declaredsuper
  "Relation between a type and its super class (including java.lang.Object for aspects
   that do not declare a super aspect)."
  [?type ?super]
  (all
    (type ?type)
    (equals ?super (.getSuperclass ?type))))

(defn
  aspect-declaredsuper
  "Relation between an aspect and its super class (including java.lang.Object for aspects
   that do not declare a super aspect)."
  [?aspect ?super]
  (all
    (aspect ?aspect)
    (type-declaredsuper ?aspect ?super)))


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
    (class ?super)))


(defn
  type-declaredsuper+
  "Relation between a type and one of the ancestors in its declared super hierarchy."
  [?type ?ancestor]
  (conde
    [(type-declaredsuper ?type ?ancestor) ]
    [(fresh [?inbetween]
            (type-declaredsuper ?type ?inbetween)
            (type-declaredsuper+ ?inbetween ?ancestor))]))


(defn
  aspect-declaredsuper+
  "Relation between an aspect and one of the ancestors in its declared super hierarchy."
  [?aspect ?ancestor]
  (all
    (aspect ?aspect)
    (type-declaredsuper+ ?aspect ?ancestor)))

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
    (class  ?ancestor)))


;todo: wantgenerics is true, check whether this is compatible with the types returned by the other predicates
;(i.e., test with a parameterized aspec)


(defn
  type-super+
  "Relation between a type and one of its direct or indirect
   super types (classes, aspects as well as interfaces),
   including those that stem from an intertype declaration."
  [?type ?super]
  (fresh [?supers]
         (!= ?super ?type)
         (type ?type)
         (equals ?supers (iterator-seq (.getHierarchy ?type true true)))
         (contains ?supers ?super)))


(defn
  aspect-super+
  "Relation between an aspect and one of its direct or indirect
   super types (classes, aspects as well as interfaces),
   including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (aspect ?aspect)
    (type-super+ ?aspect ?type)))


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
    (interface ?type)))

(defn
  aspect-superclass+
  "Relation between an aspect and one of its direct or indirect
   super classes, including those that stem from an intertype declaration."
  [?aspect ?type]
  (all
    (aspect-super+ ?aspect ?type)
    (class ?type)))


;; Advice
;; ------

(clojure.core/declare advice)

(defn
  aspect-advice
  "Relation between an aspect and one of the advices it declares."
  [?aspect ?advice]
  (all
    ;;necessary to exclude synthetic advices:
    ;;[?advice ?aspect]
    ;;[#<BcelAdvice (perCflowEntry: call(* *(..))->org.aspectj.runtime.internal.CFlowStack HelloAspect.ajc$perCflowStack)> nil]
    (!= nil ?aspect) 
    (advice ?advice)
    (equals ?aspect (.getDeclaringType ?advice))))


;Incorrect: for some aspects, does not return all advices
;(defn
;  aspect-advice
;  "Relation between an aspect and one of the advices it declares."
;  [?aspect ?advice]
;  (fresh [?members]
;         (aspect-crosscuttingmembers ?aspect ?members)
;         (contains (.getShadowMungers ?members) ?advice)))


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
  (conda
    [(v+ ?advice) 
     (succeeds (instance? org.aspectj.weaver.ShadowMunger ?advice))]
    [(v- ?advice) 
     (fresh [?world ?advices]
            (weaverworld ?world)
            (equals ?advices (-> ?world .getCrosscuttingMembersSet .getShadowMungers))
            (contains ?advices  ?advice))]))

;TODO: any corner cases?
(defn
  advice-method|ajsynthetic
  "Relation between advice and the AJ synthetic method it is compiled to."
  [?advice ?method]
  (all 
    (advice ?advice)
    (equals ?method (.getSignature ?advice))))


;note: no need to include in annotation/1, as already included in method-annotation/2
(defn
  advice-annotation
  "Relation between advice and one of its annotations."
  [?advice ?annotation]
  (fresh [?method]
         (advice-method|ajsynthetic ?advice ?method)
         (method-annotation ?method ?annotation)))


;(defn
;  advice
;  "Relation of advices known to the weaver."
;  [?advice]
;  (fresh [?aspect]
;         (aspect-advice ?aspect ?advice)))



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
  advice|before 
  "Relation of before advices."
  [?advice]
  (fresh [?kind]
         (advice-kind ?advice ?kind)
         (equals ?kind AdviceKind/Before)))

(defn
  advice|after
  "Relation of after advices."
  [?advice]
  (fresh [?kind]
         (advice-kind ?advice ?kind)
         (equals ?kind AdviceKind/After)))

(defn
  advice|afterthrowing
  "Relation of after throwing advices."
  [?advice]
  (fresh [?kind]
         (advice-kind ?advice ?kind)
         (equals ?kind AdviceKind/AfterThrowing)))

(defn
  advice|afterreturning
  "Relation of after returning advices."
  [?advice]
  (fresh [?kind]
         (advice-kind ?advice ?kind)
         (equals ?kind AdviceKind/AfterReturning)))

(defn
  advice|around
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


(clojure.core/declare intertype)

(defn
  aspect-intertype
  "Relation between an aspect and one of its intertype declarations (a ConcreteTypeMunger)."
  [?aspect ?intertype]
  (all
    (intertype ?intertype)
    (equals ?aspect (.getAspectType ?intertype))))


(def
  ^{:doc "Alias for aspect-intertype."}
type-intertype
  aspect-intertype)


;changed in order to be analogous to aspect-advice
;(defn
;  aspect-intertype 
;  "Relation between an aspect and one of its intertype declarations (a ConcreteTypeMunger)."
;  [?aspect ?intertype]
;  (fresh [?members]
;    (aspect-crosscuttingmembers ?aspect ?members)
;    (contains (.getTypeMungers ?members) ?intertype)))


(defn
  intertype
  [?intertype]
  "Relation of intertype declarations. 
   Does not include intertypes used internally by the weaver."
  (all
    (conda
      [(v+ ?intertype) 
       (succeeds (instance? ConcreteTypeMunger ?intertype))]
      [(v- ?intertype) 
       (fresh [?world ?set]
              (weaverworld ?world)
              (equals ?set (.getCrosscuttingMembersSet ?world))
              (conde [(fresh [?col]
                             (equals ?col (.getTypeMungers ?set))
                             (contains ?col ?intertype))]
                     [(fresh [?col]
                             (equals ?col (.getLateTypeMungers ?set))
                             (contains ?col ?intertype))]))])
    (fresh [?munger]
           ;;from the doc: returns null for mungers that are used internally, but were not part of a declared thing in source code.
           (equals ?munger (.getMunger ?intertype))
           (!= nil ?munger))))


;note: ConcreteTypeMunger.getMunger returns a ResolvedTypeMunger (from a previous weaving stage)

(defn
  intertype-kind
  "Relation between an intertype declaration and its kind."
  [?intertype ?kind]
  (all
    (intertype ?intertype)
    (equals ?kind (-> ?intertype .getMunger .getKind))))

(defn
  intertype|field
  "Relation of field intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Field))))

(defn
  intertype|method
  "Relation of method intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Method))))


(defn
  intertype|constructor
  "Relation of constructor intertype declarations."
  [?intertype]
  (fresh [?kind]
         (intertype-kind ?intertype ?kind)
         (equals ?kind (ResolvedTypeMunger/Constructor))))


(defn
  intertype-member-target
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

   Prefer to use the above intertype-member-target instead, as it does not escape the weaver world."
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
         (!= nil ?equivalentmember)
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


(defn-
  advice-pointcut|raw
  "Relation between an Advice ?advice and its raw, implementation-level Pointcut ?pointcut.
   These are conjunctions of the user-defined Pointcut and one synthetic Pointcut corresponding 
   to its aspect's PerClause (cflow, super, object, singleton, typewithin)."
  [?advice ?pointcut]
  (all
    (advice ?advice)
    (equals ?pointcut (.getPointcut ?advice))))


;; some info: 
;; KindedPointcut or MatchesNothingPointcut returned by
;; getPointCut() on a PointCutDefinition retrieved through aspect-pointcutdefinition
;; differ from Pointcut returned by advice-pointcut

;; (damp.ekeko/ekeko [?pd ?p]
;;                (fresh [?a] (aspect-pointcutdefinition ?a ?pd))
;;                  (fresh [?a] (advice-pointcut ?a ?p))
;;                  (equals ?p (.getPointcut ?pd)))
;; 

;; reason being the persingleton etc that are added

;;following query correctly links a pc to its pcdefinition
;;(damp.ekeko/ekeko* [?pd ?p ?left]
;;                     ;<AndPointcut (execution(* cl.pleiad.ajlmp.testPointcuts.BaseClass.*2(..)) && persingleton(cl.pleiad.ajlmp.testPointcuts.SecondAspect))>
;;                     (fresh [?advice] (advice-pointcut ?advice ?p))
;;                     ;;rhs of and is always a  persingleton(cl.pleiad.ajlmp.testPointcuts.SecondAspect) etc
;;                     (equals ?left (.getLeft ?p)) 
;;                     ;lhs is <KindedPointcut execution(* cl.pleiad.ajlmp.testPointcuts.BaseClass.*2(..))>
;;                     (fresh [?aspect]  (aspect-pointcutdefinition ?aspect ?pd))
;;                     (equals ?left (.getPointcut ?pd)
;;                     ))



(defn
  advice-pointcut
  "Relation between an Advice ?advice and its Pointcut ?pointcut.
   Note that these are different from the ResolvedPointcutDefinition instances returned
   by aspect-pointcutdefinition."
  [?advice ?pointcut]
  (fresh [?raw]
         (advice-pointcut|raw ?advice ?raw)
         (succeeds (instance? AndPointcut ?raw)) ;;TODO: check whether others are possible?
         (equals ?pointcut (.getLeft ?raw))))


(clojure.core/declare pointcutdefinition-pointcut)

(defn-
  pointcut|maybeduplicate
  [?pointcut]
  (conde [(fresh [?advice]
                 ;pointcuts inline in an advice
                 ;or the same pointcut as the one in the pointcutdefinition the advice refers to
                 (advice-pointcut ?advice ?pointcut))]
         [(fresh [?pointcutdefinition]
                 ;pointcuts within a pointcutdefinition (including those in a definition never referred to by an advice)
                 (pointcutdefinition-pointcut ?pointcutdefinition ?pointcut))]))

(defn-
  pointcuts|noduplicates
  []
  (let [results (run* [?pc] (pointcut|maybeduplicate ?pc))]
    (vec (set results))))


(defn
  pointcut
  "Relation of all Pointcut in Advice or PointcutDefinition."
  [?pc]
  (conda
    [(v+ ?pc)
     (succeeds (instance? Pointcut ?pc))]
    [(v- ?pc)
     (fresh [?pcs]
            (equals ?pcs (pointcuts|noduplicates))
            (contains ?pcs ?pc))]))

(defn
  advice-pointcutdefinition
  "Relation between an Advice and the PointcutDefinition of its Pointcut, 
   if it exist."
  [?advice ?pointcutdefinition]
  (fresh [?pc]
         (advice-pointcut ?advice ?pc)
         (pointcutdefinition-pointcut ?pointcutdefinition ?pc)))



;;TODO: decide whether to define relations on Pointcut hierarcy or on PointcutDefinitionHierarchy? 
;; (former is probably better, as there are advices with inline pointcuts)


;; Pointcut definitions
;; --------------------

(defn
  type-pointcutdefinitions
  "Relation between a type and its declared pointcutdefinitions.
   Note that these are instances of ResolvedPointcutDefinition,
   rather than the Pointcut instances within ShadowMungers (advice)."
  [?type ?pointcutdefinitions]
  (all 
    (type ?type)
    (equals ?pointcutdefinitions (.getDeclaredPointcuts ?type))))

(defn
  aspect-pointcutdefinitions
  "Relation between an aspect and its declared pointcutdefinitions."
  [?aspect ?pointcutdefinitions]
  (all
    (aspect ?aspect)
    (type-pointcutdefinitions ?aspect ?pointcutdefinitions)))

(defn
  class-pointcutdefinitions
  "Relation between a class and its declared pointcutdefinitions."
  [?class ?pointcutdefinitions]
  (all
    (class ?class)
    (type-pointcutdefinitions ?class ?pointcutdefinitions)))

(defn
  type-pointcutdefinitions+
  "Relation between a type and all of its own and inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: type-pointcutdefinitions+|exposed where overrides are resolved."
  [?type ?pointcutdefinitions]
  (all 
    (type ?type)
    (equals ?pointcutdefinitions (vec (iterator-seq (.getPointcuts ?type))))))

(defn
  aspect-pointcutdefinitions+
  "Relation between an aspect and all of its own and inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: aspect-pointcutdefinitions+|exposed where overrides are resolved."
  [?aspect ?pointcutdefinitions]
  (all
    (aspect ?aspect)
    (type-pointcutdefinitions+ ?aspect ?pointcutdefinitions)))

(defn
  class-pointcutdefinitions+
  "Relation between a class and all of its own and inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: class-pointcutdefinitions+|exposed where overrides are resolved."
  [?class ?pointcutdefinitions]
  (all
    (class ?class)
    (type-pointcutdefinitions+ ?class ?pointcutdefinitions)))

(defn
  type-pointcutdefinition
  "Relation between a type (aspect or class) and one of the pointcuts it declares.
   Note that these are instances of ResolvedPointcutDefinition,
   rather than the Pointcut instances within ShadowMungers (advice)."
  [?type ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (type-pointcutdefinitions ?type ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  aspect-pointcutdefinition
  "Relation between an aspect and one of the pointcuts it declares."
  [?aspect ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (aspect-pointcutdefinitions ?aspect ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  class-pointcutdefinition
  "Relation between a class and one of the pointcuts it declares."
  [?class ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (class-pointcutdefinitions ?class ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  type-pointcutdefinition+
  "Relation between a type and one of its own or inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: type-pointcutdefinition+|exposed where overrides are resolved."
  [?type ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (type-pointcutdefinitions+ ?type ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  aspect-pointcutdefinition+
  "Relation between an aspect and one of its own or inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: aspect-pointcutdefinition+|exposed where overrides are resolved."
  [?aspect ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (aspect-pointcutdefinitions+ ?aspect ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  class-pointcutdefinition+
  "Relation between a class and one of its own or inherited pointcutdefinitions.
   Includes overriddens and overriders. 

   See also: aspect-pointcutdefinition+|exposed where overrides are resolved."
  [?class ?pointcutdefinition]
  (fresh [?pointcutdefinitions] 
         (class-pointcutdefinitions+ ?class ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  type-pointcutdefinitions+|exposed
  "Relation between a type and all of its exposed pointcutdefinitions.
   In contrast to type-pointcutdefinitions+, overrides are resolved."
  [?type ?pointcutdefinitions]
  (all 
    (type ?type)
    (equals ?pointcutdefinitions (vec (.getExposedPointcuts ?type)))))

(defn
  aspect-pointcutdefinitions+|exposed
  "Relation between an aspect and all of its exposed pointcutdefinitions.
   In contrast to aspect-pointcutdefinitions+, overrides are resolved."
  [?aspect ?pointcutdefinitions]
  (all 
    (aspect ?aspect)
    (type-pointcutdefinitions+|exposed ?aspect ?pointcutdefinitions)))

(defn
  class-pointcutdefinitions+|exposed
  "Relation between a class and all of its exposed pointcutdefinitions.
   In contrast to class-pointcutdefinitions+, overrides are resolved."
  [?class ?pointcutdefinitions]
  (all 
    (aspect ?class)
    (type-pointcutdefinitions+|exposed ?class ?pointcutdefinitions)))

(defn
  type-pointcutdefinition+|exposed
  "Relation between a type and one of its exposed pointcutdefinitions.
   In contrast to type-pointcutdefinition+, overrides are resolved."
  [?type ?pointcutdefinition]
  (fresh [?pointcutdefinitions]
         (type-pointcutdefinitions+|exposed ?type ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  class-pointcutdefinition+|exposed
  "Relation between a class and one of its exposed pointcutdefinitions.
   In contrast to class-pointcutdefinition+, overrides are resolved."
  [?type ?pointcutdefinition]
  (fresh [?pointcutdefinitions]
         (class-pointcutdefinitions+|exposed ?type ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))

(defn
  aspect-pointcutdefinition+|exposed
  "Relation between a aspect and one of its exposed pointcutdefinitions.
   In contrast to aspect-pointcutdefinition+, overrides are resolved."
  [?type ?pointcutdefinition]
  (fresh [?pointcutdefinitions]
         (aspect-pointcutdefinitions+|exposed ?type ?pointcutdefinitions)
         (contains ?pointcutdefinitions ?pointcutdefinition)))


(defn
  pointcutdefinition
  "Relation of pointcut definitions known to the weaver.
   Note: these are instances of ResolvedPointcutDefinition."
  [?pointcutdefinition]
  (fresh [?type]
         (type-pointcutdefinition ?type ?pointcutdefinition)))

(defn
  pointcutdefinition|abstract
  "Relation of abstract pointcut definitions."
  [?pointcut]
  (all
    (pointcutdefinition ?pointcut)
    (succeeds (.isAbstract ?pointcut))))

(defn
  pointcutdefinition|concrete
  "Relation of concrete pointcut definitions."
  [?pointcut]
  (all
    (pointcutdefinition ?pointcut)
    (equals false (.isAbstract ?pointcut))))

(clojure.core/declare pointcut)

(defn
  pointcutdefinition-pointcut
  "Relation between a concrete pointcut definition and its pointcut."
  [?pointcutdefinition ?pointcut]
  (all
    (pointcutdefinition|concrete ?pointcutdefinition)
    (equals ?pointcut (.getPointcut ?pointcutdefinition))))

(clojure.core/declare pointcutdefinition-name)

(clojure.core/declare method-name)


;only a heuristic ...
(defn-
  methodnamedaspointcut
  [methodname pointcutname]
  (let [mangled (str "ajc$pointcut$$" pointcutname)]
    (.startsWith methodname mangled)))
    

(defn
  pointcutdefinition-method|ajsynthetic
  "Relation between pointcutdefinition and the AJ synthetic method it is compiled to."
  [?pointcutdefinition ?method]
  (fresh [?type ?pdname ?mname]
         (type-pointcutdefinition ?type ?pointcutdefinition)
         (pointcutdefinition-name ?pointcutdefinition ?pdname)
         (type-method ?type ?method)
         (member|synthetic ?method)
         (method-name ?method ?mname)
         (succeeds (methodnamedaspointcut ?mname ?pdname)))) 

;note: no need to include in annotation/1, as already included in method-annotation/2
(defn
  pointcutdefinition-annotation
  "Relation between a pointcut definition and one of its annotations."
  [?pointcutdefinition ?annotation]
  (fresh [?method]
         (pointcutdefinition-method|ajsynthetic ?pointcutdefinition ?method)
         (method-annotation ?method ?annotation)))



(defn
  pointcutdefinition-concretizedby
  "Relation of (possibly abstract) pointcut definitions and their concretizing (definitely concrete) pointcut definitions."
  [?abstractpc ?concretepc]
  (fresh [?abaspect ?concaspect ?name]
         (aspect|abstract ?abaspect)
         (aspect-super+ ?concaspect ?abaspect)
         (aspect-pointcutdefinition ?abaspect ?abstractpc)
         (aspect-pointcutdefinition ?concaspect ?concretepc)
         (pointcutdefinition|concrete ?concretepc)
         (pointcutdefinition-name ?abstractpc ?name)
         (pointcutdefinition-name ?concretepc ?name)))



;; Declare declarations
;; --------------------


(defn
  aspect-declare
  "Relation between an aspect and one of its declare declarations."
  [?aspect ?declare]
  (fresh [?declares]
         (aspect ?aspect)
         (equals ?declares (.getDeclares ?aspect))
         (contains ?declares ?declare)))


(defn
  declare
  "Relation of all declare declarations known to the weaver."
  [?declare]
  (conda [(v+ ?declare)
          (succeeds (instance? Declare ?declare))]
         [(v- ?declare)
          (fresh [?aspect]
                 (aspect-declare ?aspect ?declare))]))

(defn
  declare|parents
  "Relation of all parents declare declarations."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareParents ?declare))))

(defn
  declare|annotation
  "Relation of all annotation declare declarations."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareAnnotation ?declare))))


;;TODO: should be renamed (but also its uses in the test cases) for consistency
(defn
  declareprecedence
  "Relation of all precedence declarations."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclarePrecedence ?declare))))

(def
  declare|precedence
  declareprecedence)


(defn
  declare|warning 
  "Relation of all warning declare declarations (pc expression)."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareErrorOrWarning ?declare))
    (equals false (.isError ?declare))))


(defn
  declare|twarning 
  "Relation of all warning declare declarations (type pattern)."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareTypeErrorOrWarning ?declare))
    (succeeds (.isError ?declare))))


(defn
  declare|error 
  "Relation of all error declare declarations (pc expression)"
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareErrorOrWarning ?declare))
    (succeeds (.isError ?declare))))

(defn 
  declare|terror
  "Relation of all warning declare declarations (type pattern)."
  [?declare]
  (declare ?declare)
  (succeeds (instance? DeclareTypeErrorOrWarning ?declare))
  (equals true (.isError ?declare)))



(defn
  declare|soft
  "Relation of all soft declare declarations."
  [?declare]
  (all
    (declare ?declare)
    (succeeds (instance? DeclareSoft ?declare))))


(defn
  declare|parents-parents|patterns 
  "Relation between a declare parents ?declare and its TypePatternList of ?parents."
  [?declare ?parents]
  (all
    (declare|parents ?declare)
    (equals ?parents (.getParents ?declare))))


(defn-
  typepatterns2types
  [typepatternlist world]
  (let [patterns (.getTypePatterns typepatternlist)]
    (for [pattern patterns]
      (.resolve (.getExactType pattern) world))))

(defn
  declare|parents-parents|types
  "Relation between a declare parents ?declare and its collection of parent ?types."
  [?declare ?types]
  (fresh [?world ?patterns]
         (weaverworld ?world)
         (declare|parents-parents|patterns ?declare ?patterns)
         (equals ?types (typepatterns2types ?patterns ?world))))


(defn
  declare|parents-parent|type
  "Relation between a declare parents ?declare and one of its parent types ?type."
  [?declare ?type]
  (fresh [?types]
         (declare|parents-parents|types ?declare ?types)
         (contains ?types ?type)))


(comment
  
  ; Commented out because isExtends is always true
  ; TODO:if necessary to distinguish extends/implements declare parents, find other way
  
  ; // note - will always return true after deserialization, this doesn't affect weaver
  ;	public boolean isExtends() {
  ;		return this.isExtends;
  ;	}
  
  
  (defn
    declare|parents|extends
    "Relation of declare parents extends declarations."
    [?declare]
    (all
      (declare|parents ?declare)
      (succeeds (.isExtends ?declare))))
  
  )



(defn
  declare|parents-target|pattern
  "Relation between a declare parents declaration and its target type pattern."
  [?declare ?targetpattern]
  (all
    (declare|parents ?declare)
    (equals ?targetpattern (.getChild ?declare))))

;;TODO: iterating over all types is slow, and might miss types that match dynamically:
;; 	public boolean match(ResolvedType typeX) {
;;		if (!child.matchesStatically(typeX)) {
;;			return false;
;;		}
;; ....

(defn
  declare|parents-target|type
  "Relation between a declare parents declaration and its target type."
  [?declare ?target]
  (all
    (declare|parents ?declare)
    (type ?target)
    (succeeds (.match ?declare ?target))))


(defn
  declare|parents-target-parent
  "Relation between a declare parents ?declare, one of its target type ?target, and one of its parent types ?parent."
  [?declare ?target ?parent]
  (all
    (declare|parents-target|type ?declare ?target)
    (declare|parents-parent|type ?declare ?parent)))



(defn
  declareprecedence-patterns 
  "Relation between a DeclarePrecedence ?declare and its TypePatternList ?patterns"
  [?declare ?patterns]
  (all
    (declareprecedence ?declare)
    (equals ?patterns (.getPatterns ?declare))))


;; Aspect precedence
(defn
  aspect|dominates-aspect-explicitly
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
  aspect|dominates-aspect-explicitly+
  "Transitive explicit dominates relationship"
  [?dominator ?subordinate]
  (conde [(fresh [?prec]
                 (aspect|dominates-aspect-explicitly ?dominator ?subordinate ?prec))]
         [(fresh [?intermediate ?prec]
                 (aspect|dominates-aspect-explicitly ?dominator ?intermediate ?prec)
                 (aspect|dominates-aspect-explicitly+ ?intermediate ?subordinate))]))

;	precedence rules: sub-aspects implicitly have precedence over their super-aspect;	Not reified: for advice of same aspect lexically first advice has implicit precedence over second advice

(defn
  aspect|dominates|implicitly-aspect+
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
          [(aspect|dominates-aspect-explicitly+ ?dominator ?subordinate)]
          [(aspect|dominates-aspect-implicitly+ ?dominator ?subordinate)]
          [(fresh [?intermediate]
                  (aspect-dominates-aspect ?dominator ?intermediate)
                  (aspect-dominates-aspect ?intermediate ?subordinate))])
        (fails (aspect|dominates-aspect-explicitly+ ?subordinate ?dominator)))))
  )

(defn- 
  domination-edge
  [?d ?s]
  (all 
    (conde 
      [(aspect|dominates-aspect-explicitly+ ?d ?s)]
      [(aspect|dominates|implicitly-aspect+ ?d ?s)])))

;not very declarative, see TODO about tabling above
;perhaps todo: 1-dom->5 is repeatd in results
(defn
  aspect|dominates-aspect
  "Precedence domination relation between an aspect and its subordinate,
   by combining explicit precedence declarations with implicit precedence relations."
  ([?dominator ?subordinate]
    (all
      (!= ?dominator ?subordinate)
      (aspect|dominates-aspect ?dominator ?subordinate [])))
  ([?dominator ?subordinate ?explored-subs]
    (fresh [?sub]
           (domination-edge ?dominator ?sub)
           (fails (contains ?explored-subs ?sub))
           (conde
             [(== ?subordinate ?sub)]
             [(fresh [?new-explored-subs]
                     (equals ?new-explored-subs (conj ?explored-subs ?sub))
                     (aspect|dominates-aspect ?sub ?subordinate ?new-explored-subs))])
           (fails (aspect|dominates-aspect-explicitly+ ?subordinate ?dominator)))))


;; Aspect instantiation policies
;; -----------------------------

(comment
  
  ;; This one returns PerClause instances of which the inAspect field is still null .. 
  
  ;; TODO: retrieve correctly instantiated ones through the right-hand-side of advice-raw .... and then link those
  ;; back to their defining aspect (will have to eliminate duplicates)
  ;; (damp.ekeko/ekeko [?policy]
  ;;                      (fresh [?a ?p] 
  ;;                      (advice ?a)
  ;;                      (equals ?p (.getPointcut ?a)) 
  ;;                      (succeeds (instance? AndPointcut ?p))
  ;;                      (equals ?policy (.getRight ?p))))
  
  
  (defn
    aspect-policy 
    "Relation between an aspect and its instantiation policy (instance of PerClause).
   Includes implicit policies (e.g., perSingleton and perFromSuper)."
    [?aspect ?policy]
    (all
      (aspect ?aspect)
      (equals ?policy (.getPerClause ?aspect))))
  
  )








;; Link between World (aka weaverworld) and AJProjectModelFacade (aka xcut)
;; ------------------------------------------------------------------------

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


;; Link between ProgramElement (weaverworld/element) and ResolvedType (weaverworld/type)
;; -------------------------------------------------------------------------------------


(defn-
  element-type
  "Relation between a ProgramElement of kind Aspect/Enum/Class/Interface
   and the ResolvedType instance from the weaver world it corresponds to."
  [?element ?type]
  (fresh [?element-name] ;todo: take correct world into account
         (element|type ?element)
         (equals ?element-name (str (.getPackageName ?element) "." (.getName ?element))) ;todo: check what this returns for parameterized types
         (type ?type) 
         (equals ?element-name (.getName ?type)))) ;or .getBaseName if above includes type parameters


;(damp.ekeko/ekeko [?e ?en] (element|method ?e) (equals ?en (.toSignatureString ?e true)))
;#<ProgramElement main(java.lang.String[])> "main(java.lang.String[])
(defn-
  methodparameters-elementsignature
  [^ResolvedMemberImpl method]
  (str
    "("
    (apply str (interpose "," (map (fn [pt] (.getName pt)) (.getParameterTypes method))))
    ")"))

(defn-
  method-elementsignature
  [^ResolvedMemberImpl method]
  (str
    (.getName method)
    (methodparameters-elementsignature method)))

(defn-
  constructor-elementsignature
  [^ResolvedMemberImpl method ^ResolvedType declaringt]
  (str
    (.getClassName declaringt)
    (methodparameters-elementsignature method)))



(defn-
  element-member
  "Relation between a mProgramElement and the IResolvedMember it corresponds to."
  [?element ?member]
  (fresh [?type ?etype ?kind]
         (element-ancestor|type ?element ?etype)
         (element-type ?etype ?type)
         (element-kind ?element ?kind)
         (conda
           [(== ?kind (IProgramElement$Kind/METHOD))
            (fresh [?sig]
                   (equals ?sig (.toSignatureString ?element true))
                   (type-member ?type ?member)
                   (equals ?sig (method-elementsignature ?member)))]
           [(== ?kind (IProgramElement$Kind/CONSTRUCTOR))
            (fresh [?sig]
                   (equals ?sig (.toSignatureString ?element true))
                   (type-member ?type ?member)
                   (equals "<init>" (.getName ?member)) ;TODO: what about <clinit>?
                   (equals ?sig (constructor-elementsignature ?member ?type)))])))


(defn-
  advice-elementbytecodename
  [^Advice advice]
  (when-let [method (.getSignature advice)]
    (.getName method))) 

(defn-
  element-advice
  "Relation between a ProgramElement and the Advice it corresponds to."
  [?element ?member]
  (fresh [?bname] 
         (element|advice ?element)
         (equals ?bname (.getBytecodeName ?element))
         (advice ?member)
         (equals ?bname (advice-elementbytecodename ?member))))

(defn-
  element-intertypemember
  "Relation between a ProgramElement and the intertype Member it corresponds to."
  [?element ?member]
  (fresh [?intertype ?targettype]
         (intertype-element ?intertype ?element)
         (intertype-member-target ?intertype ?member ?targettype)))

(defn-
  element-weaverthing
  "Relation between a ProgramElement and the Type/Member/IntertypeMember/Advice it corresponds to in the weaver."
  [?element ?weaverthing]
  (fresh [?kind]
         (element-kind ?element ?kind)
         (conda [(succeeds (.isType ?kind))
                 (element-type ?element ?weaverthing)]
                [(conde [(== ?kind (IProgramElement$Kind/METHOD))]
                        [(== ?kind (IProgramElement$Kind/CONSTRUCTOR))])
                 (element-member ?element ?weaverthing)]
                [(succeeds (.isInterTypeMember ?kind))
                 (element-intertypemember ?element ?weaverthing)]
                [(== ?kind (IProgramElement$Kind/ADVICE))
                 (element-advice ?element ?weaverthing)])))


;; Shadows
;; -------

;TODO: it would be nicer if ?shadow were an actual Shadow instance
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

;;for efficiency's sake (called often by shadow-enclosing):
;;when ?shadow is already ground, will only check that it is a ProgramElement 
;;rather than that it is the shadow of an advice 
;;--> hence not relational
(defn
  shadow
  [?shadow]
  (conda [(v+ ?shadow)
          (element ?shadow)]
         [(v- ?shadow)
          (fresh [?advice]
                 (advice-shadow ?advice ?shadow))]))


(defn
  aspect-shadow
  "Relation between an aspect and one of the shadows of its advices."
  [?aspect ?shadow]
  (fresh [?advice]
         (aspect-advice ?aspect ?advice)
         (advice-shadow ?advice ?shadow)))


(defn 
  shadow-enclosing
  "Relation between a shadow and its immediately enclosing entity
   or the entity itself for entity shadows.

   This entity is one of the entities in the relations
   - member/1 (and its subrelations method/1, ...)
   - intertype/1  
   - advice/1
   - type/1 (and its subrelations aspect/1, class/1, ...).

   For retrieving the first enclosing entity of a specific kind, 
   see relations shadow-ancestor|method/2, shadow-ancestor|advice/2, ..."
  [?shadow ?enclosing]
  (fresh [?kind]
         (shadow ?shadow)
         (element-kind ?shadow ?kind)
         (conda [(== ?kind (IProgramElement$Kind/CODE))
                 (fresh [?parent] 
                        (element-parent ?shadow ?parent)
                        (element-weaverthing ?parent ?enclosing))]
                [(!= ?kind (IProgramElement$Kind/CODE))
                 (element-weaverthing ?shadow ?enclosing)])))


(defn
  shadow-ancestor|method
  "Relation between a shadow and its first enclosing method (if any).
   Does not consider the entity itself as candidate. "
  [?shadow ?member]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/METHOD))
         (element-member ?element ?member)))

(defn
  shadow-ancestor|constructor
  "Relation between a shadow and its first enclosing constructor (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/CONSTRUCTOR))
         (element-member ?element ?member)))

(defn
  shadow-ancestor|advice
  "Relation between a shadow and its first enclosing advice (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element] 
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/ADVICE))
         (element-member ?element ?member)))

(defn
  shadow-ancestor|intertypemethod
  "Relation between a shadow and its first enclosing intertype method (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element] 
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/INTER_TYPE_METHOD))
         (element-intertypemember ?element ?member)))

(defn
  shadow-ancestor|intertypeconstructor
  "Relation between a shadow and its first enclosing intertype constructor (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element] 
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/INTER_TYPE_CONSTRUCTOR))
         (element-intertypemember ?element ?member)))

(defn
  shadow-ancestor|class
  "Relation between a shadow and its first enclosing class (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?type]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/CLASS))
         (element-type ?element ?type)))

(defn
  shadow-ancestor|aspect
  "Relation between a shadow and its first enclosing aspect (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?type]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor-kind ?shadow ?element (IProgramElement$Kind/ASPECT))
         (element-type ?element ?type)))

(defn
  shadow-ancestor|type
  "Relation between a shadow and its first enclosing type (aspect/class/interface/enum) (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?type]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor|type ?shadow ?element)
         (element-type ?element ?type)))

(defn
  shadow-ancestor|member-or-advice
  "Relation between a shadow and its first enclosing member (method/constructor/advice) (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element]
         (shadow ?shadow)
         (element-ancestor|member ?shadow ?element)
         (element-member ?element ?member)))

(defn
  shadow-ancestor|intertypemember
  "Relation between a shadow and its first enclosing intertype member (method/constructor) (if any).
   Does not consider the entity itself as candidate."
  [?shadow ?member]
  (fresh [?element] 
         (shadow ?shadow)
         (element-ancestor|intertypemember ?shadow ?element)
         (element-intertypemember ?element ?member)))


;; Names (In Progress)
;; -------------------

(defn
  type-name
  "Relation between a type and its name as a user-friendly string."
  [?type ?namestring]
  (all 
    (type ?type)
    (equals ?namestring (.getName ?type))))

(defn
  pointcutdefinition-name
  "Relation between a pointcut definitions and its name as a string."
  [?pointcut ?name]
  (all
    (pointcutdefinition ?pointcut)
    (equals ?name (.getName ?pointcut))))

;;TODO: align with other name relations
(defn
  intertype|method-name
  "Relation of method intertype declarations and their name."
  [?intertype ?name]
  (all (intertype|method ?intertype)
       (equals ?name (.toString (.getSignature (.getMunger ?intertype))))))

(defn
  method-name
  "Relation between method and its name."
  [?method ?name]
  (all 
    (method ?method)
    (equals ?name (.getName ?method))))

;;pointcut type matching

(defn 
  type-type|pattern
  "non relational match to a type given a type pattern"
  [?type ?pat]
  (fresh
    [?pattern ?parsedPat ?w]
    (v+ ?pat)
    (type ?type)
    (weaverworld ?w)
    (equals ?pattern (.parseTypePattern (PatternParser. ?pat)))
    (perform (.resolve ?pattern ?w)) 
    (equals true (.matchesStatically ?pattern ?type))))

    
    
