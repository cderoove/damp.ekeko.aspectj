(ns 
    ^{:doc "Low-level AspectJ-specific Soot relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.soot
    (:refer-clojure :exclude [== type declare class])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
      [damp.qwal :as qwal]
      [damp.ekeko.aspectj 
       [projectmodel :as projectmodel]
       [xcut :as xcut]
       [weaverworld :as world]]
      [damp.ekeko.soot [soot :as soot]]))


;;todo: allow WholeProgramAnalysiModel to co-exist with AspectJModel

;;todo: should check that model of advice is model of soot scene


(defprotocol
  IHasSootSignature
  (sootsignature [this] "Returns the soot signature for an element of the AspectJ weaver world."))

(defprotocol 
  IHasSootSignatureAsMethod
  (sootsignature-as-method [this] "Returns the soot method signature for an element of the Aspectj weaver world that gets compiled into a method."))

(extend-protocol
  IHasSootSignature
  org.aspectj.weaver.UnresolvedType
  (sootsignature [this]
                 (.getRawName this))
  org.aspectj.weaver.Advice
  (sootsignature [this]
                 (sootsignature-as-method (.getSignature this)))
  org.aspectj.weaver.bcel.BcelMethod
  (sootsignature [this]
    (sootsignature-as-method this))
  org.aspectj.weaver.ConcreteTypeMunger
  (sootsignature [this]
                 (sootsignature (.getMunger this)))
  org.aspectj.weaver.ResolvedTypeMunger
  (sootsignature [this]
                 (let [kind (.getKind this)]
                   (cond 
                     (= kind (org.aspectj.weaver.ResolvedTypeMunger/Method))
                     (sootsignature-as-method (.getSignature this))
                     :else ""))) ;todo: field
  org.aspectj.weaver.bcel.BcelField
  (sootsignature [this]
    (str "<" 
         (sootsignature (.getDeclaringType this)) 
         ": " 
         (sootsignature (.getType this))
         " "
         (.getName this)
         ">")))

(extend-type
  org.aspectj.weaver.Member 
  IHasSootSignatureAsMethod
  (sootsignature-as-method [this] 
                           (str "<" 
                                (sootsignature (.getDeclaringType this)) 
                                ": " 
                                (sootsignature (.getReturnType this))
                                " " 
                                (.getName this)
                                "("
                                (apply str (interpose "," (map (fn [pt] (.getName pt)) (.getParameterTypes this))))
                                ")"
                                ">")))

(extend-type
  nil 
  IHasSootSignatureAsMethod
  (sootsignature-as-method [this] 
                           ""))
  
(defn
  behavior-soot|method
  "Relation between a method or advice, and the corresponding Soot method."
  [?method ?soot]
  (fresh [?model ?scene ?signature]
         (world/method ?method)
         (equals ?signature (sootsignature ?method))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))

(defn
  advice-soot|method 
  "Relation between an advice and the corresponding Soot method."
  [?advice ?soot]
  (fresh [?model ?scene ?signature]
         (world/advice ?advice)
         (equals ?signature (sootsignature ?advice))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))

(defn
  intertype|method-soot|method
  "Relation between an intertype method and the corresponding Soot method."
  [?itmethod ?soot]
  (fresh [?model ?scene ?signature]
         (world/intertype|method ?itmethod)
         (equals ?signature (sootsignature ?itmethod))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))

(defn
  field-soot|field
  "Relation between a field and the corresponding Soot field."
  [?ajfield ?soot]
  (fresh [?model ?scene ?aspect ?signature]
         (world/aspect ?aspect)
         (world/type-field ?aspect ?ajfield)
         (equals ?signature (sootsignature ?ajfield))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-field-signature ?soot ?signature)))


(defn
  advice|reads-field
  "Relation between an advice and one of the fields it reads from."
  [?advice ?field]
  (fresh [?soot|method ?soot|field ?soot|unit]
         (advice-soot|method ?advice ?soot|method)
         (field-soot|field ?field ?soot|field)
         (soot/soot|method-soot|unit ?soot|method ?soot|unit)
         (soot/soot|unit|reads-soot|field ?soot|unit ?soot|field)))

(defn
  advice|writes-field
  "Relation between an advice and one of the fields it writes to."
  [?advice ?field]
  (fresh [?soot|method ?soot|field ?soot|unit]
         (advice-soot|method ?advice ?soot|method)
         (field-soot|field ?field ?soot|field)
         (soot/soot|method-soot|unit ?soot|method ?soot|unit)
         (soot/soot|unit|writes-soot|field ?soot|unit ?soot|field)))


(defn
  advice-soot|calling|unit-soot|calling|method
  "Relation between an advice, and the Soot unit and method that calls it."
  [?advice ?soot|calling|unit ?soot|calling|method]
  (fresh [?soot|method]
         (advice-soot|method ?advice ?soot|method)
         (soot/soot|method-soot|unit|caller ?soot|method ?soot|calling|unit)
         ;next 2 lines are expensive, but required as a unit does not know in which method it resides
         (soot/soot|method-soot|method|caller ?soot|method ?soot|calling|method)
         (soot/soot|method-soot|unit ?soot|calling|method ?soot|calling|unit)))

(defn
  advice-icfg-start
  "Relation between an advice and an interprocedural control flow graph starting at the advice's application."
  [?advice ?icfg ?icfg|start]
  (fresh [?soot|method ?soot|unit|start ?soot|method|start]
         (advice-soot|method ?advice ?soot|method)
         (advice-soot|calling|unit-soot|calling|method ?advice ?soot|unit|start ?soot|method|start)
         (soot/soot-method-icfg ?soot|method|start ?icfg)
         (equals ?icfg|start (soot/make-icfgnode  ?soot|unit|start ?soot|method|start []))))

(defn
  icfg-start
  "Relation between interprocedural control flow graph and its starting point at the application's entry method."
  [?icfg ?icfg|start]
  (fresh [?soot|method]
        (soot/soot-entry-method ?soot|method)
        (soot/soot-method-icfg-entry ?soot|method ?icfg ?icfg|start)))

(defn
  advice-reachable|advice
  "Relation between an advice and another advice that may be executed afterwards."
  [?advice1 ?advice2]
  (fresh [?soot|method1 ?soot|method2]
         (advice-soot|method ?advice1 ?soot|method1)
         (!= ?advice1 ?advice2)
         (advice-soot|method ?advice2 ?soot|method2)
         (fresh [?icfg ?icfg|start ?icfg|end]
                ;starting in main() because starting in the caller of advice1 did not work
                ;(advice-icfg-start ?advice1 ?icfg ?icfg|start)
                (icfg-start ?icfg ?icfg|start)
                (qwal/qwal ?icfg ?icfg|start ?icfg|end []
                           ;(start in application of first advice, make transition to first advice)
                           ;qwal/q=>
                           ;start in main method, make zero or more transitions to first advice
                           (qwal/q=>*)
                           (qwal/qcurrent [icfgnode]
                                          (equals ?soot|method1 (soot/icfgnode-method icfgnode)))
                           ;make one or more transitions to second advice  
                           (qwal/q=>+)
                           (qwal/qcurrent [icfgnode]
                                          (equals ?soot|method2 (soot/icfgnode-method icfgnode)))))))
         

;;slow
(defn
  behavior-reachable|behaviorSLOW
  "Relation between a behavior (advice or method) and another behavior that may be executed afterwards. Slow version using regular path expressions."
  [?behavior1 ?behavior2]
  (fresh [?soot|method1 ?soot|method2]
         (behavior-soot|method ?behavior1 ?soot|method1)
         (!= ?behavior1 ?behavior2)
         (behavior-soot|method ?behavior2 ?soot|method2)
         (fresh [?icfg ?icfg|start ?icfg|end]
                ;starting in main() because starting in the caller of advice1 did not work
                ;(advice-icfg-start ?advice1 ?icfg ?icfg|start)
                (icfg-start ?icfg ?icfg|start)
                (qwal/qwal ?icfg ?icfg|start ?icfg|end []
                           ;(start in application of first advice, make transition to first advice)
                           ;qwal/q=>
                           ;start in main method, make zero or more transitions to first advice
                           (qwal/q=>*)
                           (qwal/qcurrent [icfgnode]
                                          (equals ?soot|method1 (soot/icfgnode-method icfgnode)))
                           ;make one or more transitions to second advice  
                           (qwal/q=>+)
                           (qwal/qcurrent [icfgnode]
                                          (equals ?soot|method2 (soot/icfgnode-method icfgnode)))))))

(defn
  soot|behavior-soot|behavior|reachable
  [?m ?r]
  (soot/soot|method-soot|method|called+ ?m ?r))
  
(defn
  behavior-reachable|behavior
  "Relation between a behavior (advice or method) and another behavior that may be executed afterwards."
  [?behavior1 ?behavior2]
  (fresh [?soot|method1 ?soot|method2]
         (behavior-soot|method ?behavior1 ?soot|method1)
         (!= ?behavior1 ?behavior2)
         (behavior-soot|method ?behavior2 ?soot|method2)
         (soot|behavior-soot|behavior|reachable ?soot|method1 ?soot|method2)))
       
  




         
         
(comment
  (damp.ekeko/ekeko* [?advice ?method] (advice-soot|method ?advice ?method))
  
  (damp.ekeko/ekeko* [?itmethod ?sootmethod] (intertype|method-sootmethod ?itmethod ?sootmethod))
  
  (damp.ekeko/ekeko* [?method ?signature] (soot/soot-method-signature ?method ?signature)))