(ns 
  ^{:doc "Relations for implementing parts of the 'aspect assumptions' paper."
    :author "Coen De Roover, Johan Fabry" }
   damp.ekeko.aspectj.assumptions
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic])
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.aspectj weaverworld])
  (:require [damp.ekeko.aspectj
             [soot :as ajsoot]
             [ajdt :as ajdt]
             [xcut :as xcut]]
            [damp.ekeko.soot
             [soot :as ssoot]]
            ))

;; Assumption: incomplete precedence
(defn
  incomplete-precedence
  [?first ?second]
  (all
    (aspect ?first)
    (aspect ?second)
    (!= ?first ?second)
    (fails (aspect-dominates-aspect ?first ?second))
    (fails (aspect-dominates-aspect ?second ?first))))

;; Assumption: incomplete precedence for a certain shadow
;; DOES NOT WORK. not all shadows are found. finds shadows of 6th aspect, which does not have any!
; Also affects   overriden-implicit-precedence-shadow 
; Here are the shadows on the relevant method (baseMethod1)
;            FifthAspect.before(): <anonymous pointcut>
;            SecondAspect.before(): <anonymous pointcut>
;            ThirdAspect.before(): pc..
;            FourthAspect.before(): pc..
;            FirstAspect.before(): <anonymous pointcut>
(defn
  incomplete-precedence-shadow
  [?first ?second ?shadow]
  (fresh [?ad1 ?ad2]
         (incomplete-precedence ?first ?second)
         (aspect-advice ?first ?ad1)
         (advice-shadow ?ad1 ?shadow)
         (aspect-advice ?second ?ad2)
         (advice-shadow ?ad2 ?shadow)))

;;Assumption: implicit precedence is overridden
(defn
  overriden-implicit-precedence
  [?first ?second]
  (all
    (aspect-dominates-aspect ?second ?first)
    (aspect-dominates-aspect-implicitly+ ?first ?second)))

;;Assumption: implicit precedence is overridden for a certain shadow
; DOES NOT WORK. shadows are not found. Why?
; see  incomplete-precedence-shadow
(defn
  overriden-implicit-precedence-shadow
  [?first ?second ?shadow]
  (fresh [?ad1 ?ad2]
         (overriden-implicit-precedence ?first ?second)
         (aspect-advice ?first ?ad1)
         (advice-shadow ?ad1 ?shadow)
         (aspect-advice ?second ?ad2)
         (advice-shadow ?ad2 ?shadow)))

;;Assumption: aspect modifies other aspect
(defn 
  modifies-aspect
  [?modifier ?modified]
  (fresh [?advice ?shadow]
         (aspect-advice ?modifier ?advice)
         (advice-shadow ?advice ?shadow)
         (shadow-enclosingtypedeclaration ?shadow ?modified)
         (aspect ?modified)))

;;Assumption itd use: intertype method is introduced, but never called
(defn 
  intertypemethod-unused
  [?itmethod]
  (fresh [?sootmethod ?caller]
         (intertypemethod ?itmethod)
         (fails 
           (all
             (ajsoot/intertypemethod-sootmethod ?itmethod ?sootmethod)
             (ssoot/soot-method-called-by-method ?sootmethod ?caller)))))


;;Assumption no double concretization of abstract pointcuts
(defn 
  abstractpointcut-concretized-reconcretized
  [?abpointcut ?concpointcut1 ?concpointcut2]
  (all
    (pointcut-concretizedby ?abpointcut ?concpointcut1)
    (pointcut-concretizedby ?concpointcut1 ?concpointcut2)))
    
    

  