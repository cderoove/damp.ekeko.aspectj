(ns 
  ^{:doc "Relations for implementing parts of the 'aspect assumptions' paper."
    :author "Coen De Roover, Johan Fabry" }
   damp.ekeko.aspectj.assumptions
  (:refer-clojure :exclude [== type declare class])
  (:require [clojure.core.logic :as l] )
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.aspectj weaverworld])
  (:require [damp.ekeko.aspectj
             [soot :as ajsoot]
             ;only use these when absolutely necessary, not as developed as relations from weaverworld
            ;[ajdt :as ajdt] 
             ;[xcut :as xcut]
             ]
            [damp.ekeko.soot
             [soot :as jsoot]]
            ))
  
;; Assumption: incomplete precedence
(defn
  incomplete-precedence
  [?first ?second]
  (l/all
    (aspect ?first)
    (aspect ?second)
    (l/!= ?first ?second)
    (fails (aspect|dominates-aspect ?first ?second))
    (fails (aspect|dominates-aspect ?second ?first))))

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

;;===========================================================================================

;;Assumption: implicit precedence is overridden
;;paper 3.1.1: assumption 9 case 2
(defn
  overriden|implicit|precedence
  [?first ?second]
  (l/all
    (aspect|dominates-aspect ?second ?first)
    (aspect|dominates|implicitly-aspect+ ?first ?second)))

;;===========================================================================================

;;Assumption: implicit precedence is overridden for a certain shadow
(defn
  overriden-implicit-precedence-shadow
  [?first ?second ?shadow]
  (l/fresh [?ad1 ?ad2]
         (overriden|implicit|precedence ?first ?second)
         (aspect-advice ?first ?ad1)
         (advice-shadow ?ad1 ?shadow)
         (aspect-advice ?second ?ad2)
         (advice-shadow ?ad2 ?shadow)))

;;===========================================================================================

;;Assumption: aspect modifies other aspect
;; paper 3.1.1 assumption 1 case 1 and case 2
;; also paper 3.2.3 assumption 2
(defn 
  modifies|aspect1-aspect2
  [?modifier ?modified] ;;A2 and A1
  (l/fresh [?advice ?shadow]
         (aspect-advice ?modifier ?advice)
         (advice-shadow ?advice ?shadow)
         (l/conde [;;shadow on 
                   (shadow-enclosing ?shadow ?modified)] 
                  [;;shadow within 
                   (shadow-ancestor|type ?shadow ?modified)]) 
         (aspect ?modified)
         (l/!= ?modifier ?modified)))

;;===========================================================================================

;;Assumption itd use: intertype method is introduced, but never called
;;paper 3.1.1 assumption 5
(defn 
  intertype|method|unused
  [?itmethod]
  (l/fresh [?sootmethod ?caller]
         (intertype|method ?itmethod)
         (fails 
           (l/all
             (ajsoot/intertype|method-soot|method ?itmethod ?sootmethod)
             (jsoot/soot-method-called-by-method ?sootmethod ?caller)))))

;;===========================================================================================

;;Assumption no double concretization of abstract pointcuts
;;paper 3.1.1 assumption 7 
(defn 
  abstractpointcutdefinition-concretized-reconcretized
  [?abpointcut ?concpointcut1 ?concpointcut2]
  (l/all
    (pointcutdefinition-concretizedby ?abpointcut ?concpointcut1)
    (pointcutdefinition-concretizedby ?concpointcut1 ?concpointcut2)))

;;===========================================================================================

(clojure.core/declare percflowaspect)
    
;;Assumption this aspect implements a wormhole
;; -- the naive version
;;paper 3.1.2 assumption 1 case 1
(defn
  wormhole|naive-entry-exit-field
  [?aspect ?advice|entry ?advice|exit ?field]
  (l/all
    (aspect-advice ?aspect ?advice|entry)
    (type-field ?aspect ?field)
    (ajsoot/advice|writes-field ?advice|entry ?field)
    (l/!= ?advice|exit ?advice|entry)
    (aspect-advice ?aspect ?advice|exit)
    (ajsoot/advice|reads-field ?advice|exit ?field)))

;;Assumption this aspect implements a wormhole
;; -- naive + execution path from entry to exit 
;;TODO: without interruptions of other advice of the same aspect
;;paper 3.1.2 assumption 1 case 2
(defn
  wormhole|path-entry-exit-field
  [?aspect ?advice|entry ?advice|exit ?field]
  (l/all
    (wormhole|naive-entry-exit-field ?aspect ?advice|entry ?advice|exit ?field)
    (one 
      (ajsoot/advice-reachable|advice ?advice|entry ?advice|exit))))

;;Assumption this aspect implements a wormhole
;; -- percflow of naive
;; MOCK IMPLEMENTATION - DOES NOT WORK
;;paper 3.1.2 assumption 1 case 3
(defn
  wormhole-aspect-entry-exit-field
  [?aspect ?entryadvice ?exitadvice ?field]
  (l/all
    (wormhole|naive-entry-exit-field ?aspect ?entryadvice ?exitadvice ?field)
    (percflowaspect ?aspect)));NOT IMPLEMENTED YET


(comment
;TODO: start icfg traversal from callers of advice
;eventueel (qwhile [?soot|method ?soot|unit] q=> 
;finds possibly broken implementations of wormhole idiom, where the field is written to in between entry and exit
(defn
  wormhole|broken-entry-exit-field	
  [?aspect ?advice|entry ?advice|exit ?field]
  (aspect ?aspect)
  (type-field ?aspect ?field)
  (aspect-advice ?aspect ?advice|entry)
  (aspect-advice ?aspect ?advice|exit)
  (l/fresh [?soot|field ?soot|method|entry ?soot|method|exit]
           (ajsoot/field-soot|field ?field ?soot|field)
           (ajsoot/advice-soot|method ?advice|entry ?soot|method|entry)
           (ajsoot/advice-soot|method ?advice|exit ?soot|method|exit)
           (l/fresh [?soot|icfg ?soot|unit|start ?soot|unit|end]
                    (jsoot/soot-method-icfg ?soot|method|entry ?soot|icfg)
                    (jsoot/soot-method-icfg-entry ?soot|method|entry ?soot|icfg ?soot|unit|start)
                    (jsoot/soot-method-icfg-exit ?soot|method|exit ?soot|icfg ?soot|unit|end)
                    (damp.qwal/qwal
                      ?soot|icfg ?soot|unit|start ?soot|unit|end []
                      (damp.qwal/q=>*)
                      (damp.qwal/qcurrent [[?soot|method ?soot|unit]]
                                          (l/== ?soot|method|entry ?soot|method)
                                          (jsoot/soot|unit|writes-soot|field ?soot|unit ?soot|field))
                      (damp.qwal/q=>+)   
                      (damp.qwal/qcurrent [[?soot|method ?soot|unit]]
                                          (l/!= ?soot|method|entry ?soot|method)
                                          (jsoot/soot|unit|writes-soot|field ?soot|unit ?soot|field))
                      (damp.qwal/q=>+)   
                      (damp.qwal/qcurrent [[?soot|method ?soot|unit]]
                                          (l/== ?soot|method|exit ?soot|method)
                                          (jsoot/soot|unit|reads-soot|field ?soot|unit ?soot|field)
                                          )))))
)

;;===========================================================================================

;;paper 3.1.1 assumption 6
(defn
  refine|used|pointcut-sub-super
  [?pointcutdef ?subaspect ?aspect]
  (l/fresh [?newpointcutdef ?advice]
    (aspect-pointcutdefinition ?aspect ?pointcutdef)
    (pointcutdefinition|abstract ?pointcutdef)
    (pointcutdefinition-concretizedby ?pointcutdef ?newpointcutdef)
    (aspect-pointcutdefinition ?subaspect ?newpointcutdef)
    (aspect-advice ?aspect ?advice)
    (advice-pointcutdefinition ?advice ?newpointcutdef)))

;;===========================================================================================
  
(defn
  markerinterface
  [?interface]
  (l/fresh [?member]
    (interface ?interface)
    (fails (type-member ?interface ?member))))

(defn
  iface-self|or|sub
  [?interface ?subinterface]
  (l/all
    (interface ?interface)
    (interface ?subinterface)
   (l/conde [(type-declaredinterface+ ?subinterface ?interface)]
            [(l/== ?subinterface ?interface)])))

;;paper 3.1.1 assumption 1, special case 2
(defn
  aspect-declareparents|markerinterface
  [?aspect ?interface]
  (l/fresh [?superinterface ?declare]
    (markerinterface ?superinterface)
    (iface-self|or|sub ?superinterface ?interface)
    (declare|parents-parent|type ?declare ?interface)
    (aspect-declare ?aspect ?declare)))

;;===========================================================================================

;;paper 3.1.1 assumption 2, case 1
(defn
  same|pointcut|name-aspect1-aspect2
  [?name ?aspect1 ?aspect2]
  (l/fresh [?pc1 ?pc2]
     (aspect-pointcutdefinition ?aspect1 ?pc1)
     (aspect-pointcutdefinition ?aspect2 ?pc2)
     (l/!= ?aspect1 ?aspect2)
     (pointcutdefinition-name ?pc1 ?name)
     (pointcutdefinition-name ?pc2 ?name)))

;;===========================================================================================

(defn 
  aspect-usedpointcutdef
  [?aspect ?pointcutdef]
  (l/fresh [?advice]
           (aspect-advice ?aspect ?advice)
           (advice-pointcutdefinition ?advice ?pointcutdef)))

(defn aspect-allusedpointcutdefs
  [?aspect ?usedpointcutdefs]
  (l/all
    (aspect ?aspect)
    (findall ?usedpcdef (aspect-usedpointcutdef ?aspect ?usedpcdef) ?usedpointcutdefs)))

;;paper 3.1.1 assumption 2, case 2
(defn
  samepointcuts|reuse|from|super|sub1-sub2-usedpc
  [?aspect1 ?aspect2 ?usedpc1 ]
  (l/fresh [?superaspect ?usedpc2]
           (aspect-declaredsuperaspect+ ?aspect1 ?superaspect)
           (aspect-declaredsuperaspect+ ?aspect2 ?superaspect)
           (l/!= ?aspect1 ?aspect2)
           (aspect-allusedpointcutdefs ?aspect1 ?usedpc1)
           (aspect-allusedpointcutdefs ?aspect2 ?usedpc2)
           (l/!= ?usedpc1 [])
           (l/!= ?usedpc2 [])
           (same-elements ?usedpc1 ?usedpc2)
           )) 

;;===========================================================================================


;;paper 3.1.1 assumption 2, case 3
(defn
  sameshadows|aspect1-aspect2
  [?aspect1 ?aspect2]
  (l/fresh [?shadows1 ?shadows2]
           (aspect ?aspect1)
           (aspect ?aspect2)
           (l/!= ?aspect1 ?aspect2)
           (findall ?shadow1 (aspect-shadow ?aspect1 ?shadow1) ?shadows1)
           (findall ?shadow2 (aspect-shadow ?aspect2 ?shadow2) ?shadows2)
           (l/!= ?shadows1 [])
           (l/!= ?shadows2 [])
           (same-elements ?shadows1 ?shadows2)))

;;===========================================================================================

; for the simple AJ reentrancy idiom
(defn
  reentrant-aspect-advice
  [?aspect ?advice]
  (l/fresh [?shadow]
           (aspect-advice ?aspect ?advice)
           (advice-shadow ?advice ?shadow)
           (shadow-enclosing ?shadow ?advice)))

;;===========================================================================================

; Annotations
