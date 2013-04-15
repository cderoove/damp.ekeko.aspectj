(ns test.damp.ekeko.aspectj
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic :exclude [is]] :reload)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:require
    [test.damp [ekeko :as test]]
    [damp.ekeko.workspace
     [workspace :as ws]]
    [damp.ekeko.soot
     [projectmodel :as spm]]
    [damp.ekeko.aspectj
     [soot :as soot]
     [ajdt :as ajdt]
     [xcut :as xcut] 
     [weaverworld :as world]
     [assumptions :as assumptions]
     ])
  (:use clojure.test)) 

;NOTE: do not forget to clean all tested projects before running the tests
;TODO: fix that :)


;; Actual Tests
;; ------------

(deftest
  aspect-test
  (let [grounding
        (ekeko [?aspect] 
                    (world/aspect ?aspect))
        grounding-and-checking
        (ekeko [?aspect]
                    (world/aspect ?aspect)
                    (world/aspect ?aspect))]
        (is (> (count grounding) 0))
        (is (empty? (clojure.set/difference (set grounding) (set grounding-and-checking))))))

(deftest
  explicit-decprec+-test 
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-explicitly+ ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest
  implicit-precedence+-test 
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-implicitly+ ?dom ?sub))
 "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\")}"))

;OK 16 oct
;Below bug is not present
;BUG: in final precedence calculation if there is an implicit precedence A->B
;and explicit precedence B->A then A->B should not be present
; This returns:
;([#<ReferenceType cl.pleiad.ajlmp.testPrecedence.ThirdAspect> #<ReferenceType cl.pleiad.ajlmp.testPrecedence.FirstAspect> #<ProgramElement baseMethod1()>] [#<ReferenceType cl.pleiad.ajlmp.testPrecedence.FirstAspect> #<ReferenceType cl.pleiad.ajlmp.testPrecedence.ThirdAspect> #<ProgramElement baseMethod1()>] [#<ReferenceType cl.pleiad.ajlmp.testPrecedence.FirstAspect> #<ReferenceType cl.pleiad.ajlmp.testPrecedence.FourthAspect> #<ProgramElement baseMethod1()>] [#<ReferenceType cl.pleiad.ajlmp.testPrecedence.FourthAspect> #<ReferenceType cl.pleiad.ajlmp.testPrecedence.FirstAspect> #<ProgramElement baseMethod1()>])
; but should also be between 4 and 5 
(deftest
  aspect-dominates-aspect-test 
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest 
  overriden-implicit-precedence-test
  (test/tuples-correspond
    (ekeko [?first ?second]
           (assumptions/overriden-implicit-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\")}"))

(deftest 
  overriden-implicit-precedence-shadow-test
  (test/tuples-correspond
    (ekeko [?first ?second ?shadow]
           (assumptions/overriden-implicit-precedence-shadow ?first ?second ?shadow))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"baseMethod1()\")}"))

; OK 16 oct
(deftest
  incomplete-precedence-test
  (test/tuples-correspond
    (ekeko [?first ?second]
           (assumptions/incomplete-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\")}"))

(deftest
  incomplete-precedence-shadow-test
  (test/tuples-correspond
    (ekeko [?first ?second ?shadow]
           (assumptions/incomplete-precedence-shadow ?first ?second ?shadow))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"baseMethod1()\")}"))


(deftest
  concretization-test
  (test/tuples-correspond 
    (ekeko [?abpointcut ?concpointcut1 ?concpointcut2]
           (assumptions/abstractpointcut-concretized-reconcretized ?abpointcut ?concpointcut1 ?concpointcut2))
    "#{(\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.FirstAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.SecondAspect.abstractpc1()\")}"))

(deftest
  intertypemethod-unused-test
  (test/tuples-correspond
    (ekeko [?itmethod]
           (assumptions/intertypemethod-unused ?itmethod))
    "#{(\"(BcelTypeMunger ResolvedTypeMunger(Method, void cl.pleiad.ajlmp.testITD.BaseClass.itdB()))\")}"))

(deftest same-pointcutname-test
  (test/tuples-correspond
    (ekeko [?name ?aspect1 ?aspect2]
           (assumptions/same-pointcutname-aspect1-aspect2 ?name ?aspect1 ?aspect2))
    "#{(\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\") (\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\")}"))

;; Test Suite
;; ----------

;note: (runtests) will report that one more test has been run (successfully)
;than those that are listed here .. seems "normal"

(deftest
  test-suite 
  ;sanity check
  (test/against-project-named "AJ-LMP-Precedence" false aspect-test )

  ;precedence logic
  (test/against-project-named "AJ-LMP-Precedence" false explicit-decprec+-test)
  (test/against-project-named "AJ-LMP-Precedence" false implicit-precedence+-test)
  (test/against-project-named "AJ-LMP-Precedence" false aspect-dominates-aspect-test)
  
  ;assumptions
  (test/against-project-named "AJ-LMP-Precedence" false overriden-implicit-precedence-shadow-test)
  (test/against-project-named "AJ-LMP-Precedence" false incomplete-precedence-test)
  (test/against-project-named "AJ-LMP-Precedence" false incomplete-precedence-shadow-test)
 
  ;assumptions from paper
   ;3.1.1 assumption 2, case 1  
  (test/against-project-named "AJ-LMP-MutExHeuristics" false same-pointcutname-test)
  ; 3.1.1 assumption 5
  (test/against-project-named "AJ-LMP-ITD" true intertypemethod-unused-test)
  ; 3.1.1 assumption 7 
  (test/against-project-named "AJ-LMP-Pointcuts" false concretization-test)
  ;3.1.1: assumption 9 case 2
  (test/against-project-named "AJ-LMP-Precedence" false overriden-implicit-precedence-test)
  )

(defn 
  test-ns-hook 
  [] 
  (test/with-ekeko-disabled test-suite))

;; Example REPL Session that runs the test
;; ---------------------------------------

; note: uncommenting would run the tests upon loading

;(comment  
  
  (run-tests)
  
 ; )
