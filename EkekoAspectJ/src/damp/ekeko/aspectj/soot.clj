(ns 
    ^{:doc "Low-level AspectJ-specific Soot relations."
    :author "Coen De Roover"}
     damp.ekeko.aspectj.soot
    (:refer-clojure :exclude [== type declare])
    (:use [clojure.core.logic])
    (:use [damp.ekeko logic])
    (:require 
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
  

(defn
  advice-soot|method 
  [?advice ?soot]
  (fresh [?model ?scene ?signature]
         (world/advice ?advice)
         (equals ?signature (sootsignature ?advice))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))

(defn
  intertype|method-soot|method
  [?itmethod ?soot]
  (fresh [?model ?scene ?signature]
         (world/intertype|method ?itmethod)
         (equals ?signature (sootsignature ?itmethod))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))



(defn
  field-soot|field
  [?ajfield ?soot]
  (fresh [?model ?scene ?aspect ?signature]
         (world/aspect ?aspect)
         (world/type-field ?aspect ?ajfield)
         (equals ?signature (sootsignature ?ajfield))
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-field-signature ?soot ?signature)))


         
                  



  


(comment
  (damp.ekeko/ekeko* [?advice ?method] (advice-sootmethod ?advice ?method))
  
  (damp.ekeko/ekeko* [?itmethod ?sootmethod] (intertype|method-sootmethod ?itmethod ?sootmethod))
  
  (damp.ekeko/ekeko* [?method ?signature] (soot/soot-method-signature ?method ?signature)))