(ns
    ^{:doc "Low-level AJDT relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.reification
    (:refer-clojure :exclude [== type])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.ekeko.aspectj [projectmodel :as projectmodel]]
      [damp.ekeko.aspectj [ajdtastnode :as astnode]])
     (:import 
       [damp.ekeko.aspectj AspectJProjectModel]
       [org.aspectj.org.eclipse.jdt.core.dom ASTNode]))

(set! *warn-on-reflection* true)


(declare nodes-of-type)


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
    (cond (contains? s org.aspectj.org.eclipse.jdt.core.dom.Expression)
            (filter (fn [n] (instance? c n))
              (nodes-of-type :Expression))
          (contains? s org.aspectj.org.eclipse.jdt.core.dom.Statement)
            (filter (fn [n] (instance? c n))
              (nodes-of-type :Statement))
          (contains? s org.aspectj.org.eclipse.jdt.core.dom.Type)
            (filter (fn [n] (instance? c n))
              (nodes-of-type :Type))
          :else 
            (run* [?n] 
              (fresh [?cu]
                (ast :CompilationUnit ?cu)
                (child+ ?cu ?n)
                (succeeds (instance? c ?n)))))))



(defn- nodes-of-type [t]
  (let [models (projectmodel/aspectj-project-models)]
    (case t
      :CompilationUnit (mapcat (fn [aspectj-project-model]
                                 (.getAspectJCompilationUnits ^AspectJProjectModel aspectj-project-model)) models)
      (try (subclass-nodes-of-type t) (catch ClassNotFoundException e []))
      
      )))
                 






