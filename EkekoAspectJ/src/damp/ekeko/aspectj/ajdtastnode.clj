(ns damp.ekeko.aspectj.ajdtastnode
  (:require [damp.ekeko.jdt [astnode :as jdt]]) 
  (:import 
    [java.lang Class]
    [java.lang.reflect Field]
    [org.aspectj.org.eclipse.jdt.core.dom
    ASTNode
    StructuralPropertyDescriptor ChildPropertyDescriptor ChildListPropertyDescriptor SimplePropertyDescriptor
      ;specific to aspectj
      AjTypeDeclaration AspectDeclaration AdviceDeclaration AfterAdviceDeclaration AfterReturningAdviceDeclaration
   AfterThrowingAdviceDeclaration AroundAdviceDeclaration BeforeAdviceDeclaration DeclareDeclaration 
   DeclareAnnotationDeclaration DeclareAtConstructorDeclaration DeclareAtFieldDeclaration DeclareAtMethodDeclaration
   DeclareAtTypeDeclaration DeclareErrorDeclaration DeclareParentsDeclaration DeclarePrecedenceDeclaration DeclareSoftDeclaration
   DeclareWarningDeclaration InterTypeFieldDeclaration InterTypeMethodDeclaration PointcutDeclaration PatternNode SignaturePattern
   TypePattern DefaultTypePattern PointcutDesignator AndPointcut CflowPointcut DefaultPointcut NotPointcut
   OrPointcut PerCflow PerObject PerTypeWithin ReferencePointcut]
  ))

(set! *warn-on-reflection* true)


(defn
  ajdt-node-classes-java
  []
  (let [lasttypeint 84] 
    (for [i (range 1 (inc lasttypeint))]
      (ASTNode/nodeClassForType i))))

(defn
  ajdt-node-classes-aspectj
  []
  [AjTypeDeclaration AspectDeclaration AdviceDeclaration AfterAdviceDeclaration AfterReturningAdviceDeclaration
   AfterThrowingAdviceDeclaration AroundAdviceDeclaration BeforeAdviceDeclaration DeclareDeclaration 
   DeclareAnnotationDeclaration DeclareAtConstructorDeclaration DeclareAtFieldDeclaration DeclareAtMethodDeclaration
   DeclareAtTypeDeclaration DeclareErrorDeclaration DeclareParentsDeclaration DeclarePrecedenceDeclaration DeclareSoftDeclaration
   DeclareWarningDeclaration InterTypeFieldDeclaration InterTypeMethodDeclaration PointcutDeclaration PatternNode SignaturePattern
   TypePattern DefaultTypePattern PointcutDesignator AndPointcut CflowPointcut DefaultPointcut NotPointcut
   OrPointcut PerCflow PerObject PerTypeWithin ReferencePointcut]
  )

(defn 
  ajdt-node-classes
  []
  (concat (ajdt-node-classes-java) 
          (ajdt-node-classes-aspectj)))

(def 
  node-classes-as-symbols 
  (memoize (fn []
             (map (fn [^Class c] (symbol (.getSimpleName c))) (ajdt-node-classes)))))

(def class-for-ekeko-keyword 
  (memoize (fn 
             [classkeyword]
             (Class/forName (str "org.aspectj.org.eclipse.jdt.core.dom." (name classkeyword))))))

(def ekeko-keyword-for-class jdt/ekeko-keyword-for-class)

(def ekeko-keyword-for-class-of jdt/ekeko-keyword-for-class-of)

;;cannot reuse because of type annotations
(defn property-descriptor-id [^StructuralPropertyDescriptor p]
  (.getId p))

;;cannot reuse because of type annotations
(defn property-descriptor-simple? [^StructuralPropertyDescriptor p]
  (.isSimpleProperty p))

;;cannot reuse because of type annotations
(defn property-descriptor-list? [^StructuralPropertyDescriptor p]
  (.isChildListProperty p))

;;cannot reuse because of type annotations
(defn property-descriptor-child? [^StructuralPropertyDescriptor p]
  (.isChildProperty p))

;;cannot reuse because of type annotations
(defn ^Class property-descriptor-owner-node-class [^StructuralPropertyDescriptor p]
  (.getNodeClass p))

;;cannot reuse because of type annotations
(defn property-descriptor-child-node-class [^ChildPropertyDescriptor p]
  (.getChildType p))

;;cannot reuse because of type annotations
(defn property-descriptor-element-node-class [^ChildListPropertyDescriptor p]
  (.getElementType p))

;;cannot reuse because of type annotations
(defn property-descriptor-value-class [^SimplePropertyDescriptor p]
  (.getValueType p))

(def ekeko-keyword-for-property-descriptor jdt/ekeko-keyword-for-property-descriptor)

;;cannot reuse because of type annotations
(defn node-property-descriptors [^ASTNode n] 
  (.structuralPropertiesForType n))

;;cannot reuse because of type annotations
(defn node-property-descriptor-for-ekeko-keyword [^ASTNode n k]
  (first (filter (fn [d] (= k (ekeko-keyword-for-property-descriptor d))) 
                   (node-property-descriptors n))))

;;cannot reuse because of type annotations
(defn node-property-value [^ASTNode n ^StructuralPropertyDescriptor p]
  (.getStructuralProperty n p))

(defn nodeclass-property-descriptors [^Class cls]
  (clojure.lang.Reflector/invokeStaticMethod cls "propertyDescriptors" (to-array [org.aspectj.org.eclipse.jdt.core.dom.AST/JLS3])))

(def property-descriptors-per-node-class
  (memoize 
    (fn []
      (let [classes (ajdt-node-classes)]
        (zipmap classes
                (map (fn [c] (nodeclass-property-descriptors c))
                     classes))))))

(def owner-properties-per-node-class
  (memoize 
    (fn 
      []
      (let [allproperties (apply concat (vals (property-descriptors-per-node-class)))
            ownerperclass (atom {})]
        (doseq [p allproperties] ;not simpelvalue
          ; (when (not (property-descriptor-simple? p))
          (swap! ownerperclass (fn [oldmap]
                                 (merge-with concat
                                             ;concat is to be used when a value already exists for a key
                                             oldmap
                                             {((cond
                                                 (property-descriptor-simple? p) property-descriptor-value-class
                                                 (property-descriptor-child? p) property-descriptor-child-node-class
                                                 (property-descriptor-list? p) property-descriptor-element-node-class)
                                                p) ;key
                                              [
                                               ;[(ekeko-keyword-for-property-descriptor p)
                                               ; (ekeko-keyword-for-class (property-descriptor-owner-node-class p))]
                                               p
                                               ] ;value to be concatenated
                                              }))))
        @ownerperclass))))
  

;cannot reuse because of type annotations
(defn node-ekeko-properties [n]
  (let [descriptors (node-property-descriptors n)]
    (zipmap (map (fn [^StructuralPropertyDescriptor p] (keyword (property-descriptor-id p))) descriptors)
            (map (fn [^StructuralPropertyDescriptor p] (fn [] (node-property-value n p))) descriptors))))

;cannot reuse because of type annotations
(defn owner [n-or-nlist]
  (if 
    (instance? ASTNode n-or-nlist)
    (.getParent ^ASTNode n-or-nlist)
    ;outer instance of nodelist is owner
    (jdt/get-invisible-field (class ^ASTNode$NodeList n-or-nlist) (symbol "this$0") n-or-nlist)))

;cannot reuse because of type annotations
(defn owner-property [n-or-nlist]
   (if 
     (instance? ASTNode n-or-nlist)
     (.getLocationInParent ^ASTNode n-or-nlist)
     (jdt/get-invisible-field (class ^ASTNode$NodeList n-or-nlist) 'propertyDescriptor n-or-nlist)))


(defprotocol IAJAST
  (reifiers [this] 
    "Returns a map of keywords to reifier functions. The latter will return an Ekeko-specific child of the AST node."))

(extend
  org.aspectj.org.eclipse.jdt.core.dom.ASTNode
  IAJAST
  {:reifiers (fn [this] (node-ekeko-properties this))})


(extend
  org.aspectj.org.eclipse.jdt.core.dom.AndPointcut
  IAJAST
  {:reifiers (fn [^AndPointcut this] 
               {:left (fn [] (.getLeft this))
                :right (fn [] (.getRight this)) }
               )})



