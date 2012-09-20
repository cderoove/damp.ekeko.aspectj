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
      [damp.ekeko.jdt [soot :as soot]]))


;;todo: allow WholeProgramAnalysiModel to co-exist with AspectJModel

;;todo: should check that model of advice is model of soot scene

(defn
  advice-sootmethod 
  [?advice ?soot]
  (fresh [?model ?scene ?signature]
         (soot/soot-model-scene ?model ?scene)
         (soot/soot-method-signature ?soot ?signature)))


(comment
  
  (damp.ekeko/ekeko* [?method ?signature] (soot/soot-method-signature ?method ?signature)))