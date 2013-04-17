(ns 
  ^{:doc "Relations for implementing parts of the 'aspect assumptions' paper."
    :author "Coen De Roover, Johan Fabry" }
   damp.ekeko.aspectj.assumptions
  (:refer-clojure :exclude [== type declare])
  (:require [clojure.core.logic :as l] )
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
  (l/all
    (aspect ?first)
    (aspect ?second)
    (l/!= ?first ?second)
    (fails (aspect-dominates-aspect ?first ?second))
    (fails (aspect-dominates-aspect ?second ?first))))

;; Assumption: incomplete precedence for a certain shadow
(defn
  incomplete-precedence-shadow
  [?first ?second ?shadow]
  (l/fresh [?ad1 ?ad2]
         (incomplete-precedence ?first ?second)
         (aspect-advice ?first ?ad1)
         (advice-shadow ?ad1 ?shadow)
         (aspect-advice ?second ?ad2)
         (advice-shadow ?ad2 ?shadow)))

;;Assumption: implicit precedence is overridden
;;paper 3.1.1: assumption 9 case 2
(defn
  overriden-implicit-precedence
  [?first ?second]
  (l/all
    (aspect-dominates-aspect ?second ?first)
    (aspect-dominates-aspect-implicitly+ ?first ?second)))

;;Assumption: implicit precedence is overridden for a certain shadow
(defn
  overriden-implicit-precedence-shadow
  [?first ?second ?shadow]
  (l/fresh [?ad1 ?ad2]
         (overriden-implicit-precedence ?first ?second)
         (aspect-advice ?first ?ad1)
         (advice-shadow ?ad1 ?shadow)
         (aspect-advice ?second ?ad2)
         (advice-shadow ?ad2 ?shadow)))

;;Assumption: aspect modifies other aspect
;; paper 3.1.1 assumption 1 case 1 and case 2
;; also paper 3.2.3 assumption 2
(defn 
  modifies-aspect
  [?modifier ?modified] ;;A2 and A1
  (l/fresh [?advice ?shadow]
         (aspect-advice ?modifier ?advice)
         (advice-shadow ?advice ?shadow)
         (shadow-enclosingtypedeclaration ?shadow ?modified)
         (aspect ?modified)))

;;Assumption itd use: intertype method is introduced, but never called
;;paper 3.1.1 assumption 5
(defn 
  intertypemethod-unused
  [?itmethod]
  (l/fresh [?sootmethod ?caller]
         (intertypemethod ?itmethod)
         (fails 
           (l/all
             (ajsoot/intertypemethod-sootmethod ?itmethod ?sootmethod)
             (ssoot/soot-method-called-by-method ?sootmethod ?caller)))))


;;Assumption no double concretization of abstract pointcuts
;;paper 3.1.1 assumption 7 
(defn 
  abstractpointcut-concretized-reconcretized
  [?abpointcut ?concpointcut1 ?concpointcut2]
  (l/all
    (pointcut-concretizedby ?abpointcut ?concpointcut1)
    (pointcut-concretizedby ?concpointcut1 ?concpointcut2)))
    

(clojure.core/declare advice-writesto)
(clojure.core/declare advice-readsfrom)
(clojure.core/declare percflowaspect)
(clojure.core/declare consecutiveexec-aspect-advice1-advice2)
    
;;Assumption this aspect implements a wormhole
;; -- the naive version
;; MOCK IMPLEMENTATION - DOES NOT WORK
(defn
  naivewormhole-aspect-entry-exit
  [?aspect ?entryadvice ?exitadvice]
  (l/fresh [?instvar] 
    (aspect-advice ?aspect ?entryadvice)
    (aspect-field ?aspect ?instvar)
    (advice-writesto ?entryadvice ?instvar) ;NOT IMPLEMENTED YET
    (aspect-advice ?aspect ?exitadvice)
    (advice-readsfrom ?exitadvice ?instvar)));NOT IMPLEMENTED YET

;;Assumption this aspect implements a wormhole
;; -- percflow of naive
;; MOCK IMPLEMENTATION - DOES NOT WORK
(defn
  wormhole-aspect-entry-exit
  [?aspect ?entryadvice ?exitadvice]
  (l/all
    (naivewormhole-aspect-entry-exit ?aspect ?entryadvice ?exitadvice)
    (percflowaspect ?aspect)));NOT IMPLEMENTED YET

;;Assumption this aspect implements a wormhole
;; -- naive + execution path from entry to exit without interruptions of other advice of the same aspect
;; MOCK IMPLEMENTATION - DOES NOT WORK
(defn
  confidentwormhole-aspect-entry-exit
  [?aspect ?entryadvice ?exitadvice]
  (l/all
    (naivewormhole-aspect-entry-exit ?aspect ?entryadvice ?exitadvice)
    (consecutiveexec-aspect-advice1-advice2 ?aspect ?entryadvice ?exitadvice)));NOT IMPLEMENTED YET


(defn
  assigns-field
  [?unit ?field]
  (l/fresh [?lhs]
    (ssoot/soot-unit-assign-leftop ?unit ?lhs)
    (ssoot/soot-value :JInstanceFieldRef ?lhs)
    (equals ?field (.getField  ?lhs))))
 
(defn
  brokenwormhole-entry-exit-field	
  [?aspect ?entryadvice ?exitadvice ?field]
  (aspect-field ?aspect ?field)
  (aspect-advice ?aspect ?entryadvice)
  (aspect-advice ?aspect ?exitadvice)
  (l/fresh [?icfg]
              (ssoot/soot-method-icfg ?entryadvice ?icfg)
              (damp.qwal/qwal ?icfg ?entryadvice ?exitadvice 
                    []
                    (damp.qwal/q=>*)
                    (damp.qwal/qcurrent [[?entryadvice ?unit]]
                              (assigns-field ?unit ?field))
                    (damp.qwal/q=>+)
                    (damp.qwal/qcurrent [[?method ?unit]]
                              (l/!= ?entryadvice ?method)
                              (assigns-field ?unit ?field))
                    (damp.qwal/q=>+)   
                    (damp.qwal/qcurrent [[?exitadvice ?unit]]
                              ; (reads-field ?unit ?field)
                               ))))

(comment
;; to test -- waiting for Coen to bugfix
;; code is in AJ-LMP-RefineUsedPointcut
;;paper 3.1.1 assumption 6
(defn
  refine-used-pointcut-sub-super-pointcut
  [?subaspect ?superaspect ?pointcut]
  (l/fresh [?newpointcut ?advice]
    (pointcut-concretizedby ?pointcut ?newpointcut)
    (advice-pointcut ?advice ?pointcut)
    (aspect-advice ?advice ?superaspect)
    (aspect-declaredsuperaspect+ ?subaspect ?superaspect) ;;better than !=
    (aspect-pointcutdefinition ?subaspect ?newpointcut)))
)

(comment

;;===========================================================================================
  
;;MOCK -- waiting for Coen 
(defn
  markerinterface
  [?interface]
  (l/all
    (interface ?interface)
    (eq interface-declaredmethods nil)))

;;paper 3.1.1 assumption 1, special case 2, first case
(defn
  aspect-declareparents-markerinterface
  [?aspect ?target ?interface]
  (l/all
    (markerinterface ?interface)
    (aspect-declareparents-interface ?aspect ?target ?interface)))

;;paper 3.1.1 assumption 1, special case 2, second case
(defn
  aspect-declareparents-markerinterface-subinterface
  [?aspect ?target ?interface ?subinterface]
  (l/all
    (markerinterface ?interface)
    (nestedinterface ?aspect ?subinterface)
    (superinterface+ ?interface ?subinterface)
    (aspect-declareparents-interface ?aspect ?target ?subinterface)))
)

;;===========================================================================================

;;paper 3.1.1 assumption 2, case 1
(defn
  same-pointcutname-aspect1-aspect2
  [?name ?aspect1 ?aspect2]
  (l/fresh [?pc1 ?pc2]
     (aspect-pointcutdefinition ?aspect1 ?pc1)
     (aspect-pointcutdefinition ?aspect2 ?pc2)
     (l/!= ?aspect1 ?aspect2)
     (pointcut-name ?pc1 ?name)
     (pointcut-name ?pc2 ?name)))

;;===========================================================================================
(comment
;; no longer needed. example of findall
(defn
  aspect-alldefdpointcuts
  [?aspect ?pointcuts]
  (l/all
    (aspect ?aspect)
    (findall ?pointcut (aspect-pointcutdefinition ?aspect ?pointcut) ?pointcuts))) 

(defn 
  aspect-usedpointcut
  [?aspect ?pointcut]
  (l/fresh [?advice]
           (aspect-advice ?aspect ?advice)
           (advice-pointcut ?advice ?pointcut)))

(defn aspect-allusedpointcuts
  [?aspect ?usedpointcuts]
  (l/all
    (aspect ?aspect)
    (findall ?usedpointcut (aspect-usedpointcut ?aspect ?usedpointcut) ?usedpointcuts)))

;;Does not work due to issue #7
;;paper 3.1.1 assumption 2, case 2
(defn
  samepointcuts-reuse-super-sub1-sub2
  [?aspect1 ?aspect2 ?usedpc1 ?usedpc2]
  (l/fresh [?superaspect]
           (aspect-declaredsuperaspect+ ?aspect1 ?superaspect)
           (aspect-declaredsuperaspect+ ?aspect2 ?superaspect)
           (l/!= ?aspect1 ?aspect2)
           (aspect-allusedpointcuts ?aspect1 ?usedpc1)
           (aspect-allusedpointcuts ?aspect2 ?usedpc2)
           (succeeds (empty? (clojure.set/difference (set ?usedpc1) (set ?usedpc2)))) )) 
)
;;===========================================================================================

(comment 
(defn
  aspect-shadow
  [?aspect ?shadow]
  (l/fresh [?advice]
           (aspect-advice ?aspect ?advice)
           (advice-shadow ?advice ?shadow)))

;;to test -- BUG: last line always succeeds. See mail to Coen on Apr 16
;;paper 3.1.1 assumption 2, case 3
(defn
  sameshadows-aspect1-aspect2
  [?aspect1 ?aspect2]
  (l/fresh [?shadows1 ?shadows2]
           (aspect ?aspect1)
           (aspect ?aspect2)
           (l/!= ?aspect1 ?aspect2) ;also exclude that one is the super of the other
           (findall ?shadow1 (aspect-shadow ?aspect1 ?shadow1) ?shadows1)
           (findall ?shadow2 (aspect-shadow ?aspect2 ?shadow2) ?shadows2)
           (succeeds (empty? (clojure.set/difference (set ?shadows1) (set ?shadows2))))))
)