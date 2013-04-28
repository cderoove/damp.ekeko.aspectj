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
  test-aspect
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
  test-explicit-decprec+
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-explicitly+ ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest
  test-implicit-precedence+
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-implicitly+ ?dom ?sub))
 "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\")}"))

(deftest
  test-aspect-dominates-aspect
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest 
  test-overriden-implicit-precedence
  (test/tuples-correspond
    (ekeko [?first ?second]
           (assumptions/overriden-implicit-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\")}"))

(deftest 
  test-overriden-implicit-precedence-shadow
  (test/tuples-correspond
    (ekeko [?first ?second ?shadow]
           (assumptions/overriden-implicit-precedence-shadow ?first ?second ?shadow))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"baseMethod1()\")}"))

(deftest
  test-incomplete-precedence
  (test/tuples-correspond
    (ekeko [?first ?second]
           (assumptions/incomplete-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\")}"))

(deftest
  test-incomplete-precedence-shadow
  (test/tuples-correspond
    (ekeko [?first ?second ?shadow]
           (assumptions/incomplete-precedence-shadow ?first ?second ?shadow))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"baseMethod1()\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"baseMethod1()\")}"))


(deftest
  test-concretization
  (test/tuples-correspond 
    (ekeko [?abpointcut ?concpointcut1 ?concpointcut2]
           (assumptions/abstractpointcutdefinition-concretized-reconcretized ?abpointcut ?concpointcut1 ?concpointcut2))
    "#{(\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.FirstAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.SecondAspect.abstractpc1()\")}"))

(deftest
  test-intertype|method|unused
  (test/tuples-correspond
    (ekeko [?itmethod]
           (assumptions/intertype|method|unused ?itmethod))
    "#{(\"(BcelTypeMunger ResolvedTypeMunger(Method, void cl.pleiad.ajlmp.testITD.BaseClass.itdB()))\")}"))

(deftest test-same-pointcutname
  (test/tuples-correspond
    (ekeko [?name ?aspect1 ?aspect2]
           (assumptions/same-pointcutname-aspect1-aspect2 ?name ?aspect1 ?aspect2))
    "#{(\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\") (\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\")}"))

(deftest test-same-shadows
  (test/tuples-correspond
    (ekeko [?aspect1 ?aspect2]
           (assumptions/sameshadows-aspect1-aspect2 ?aspect1 ?aspect2))
    "#{(\"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\") (\"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\")}"))

(deftest test-same-superpointcuts-reuse
  (test/tuples-correspond
    (ekeko [?aspect1 ?aspect2 ?usedpc1]
           (assumptions/samepointcuts-reuse-fromsuper-sub1-sub2-usedpc ?aspect1 ?aspect2 ?usedpc1))
"#{(\"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"[#<ResolvedPointcutDefinition pointcut cl.pleiad.ajlmp.testMutExHeuristics.FirstAspect.pc1()>]\") (\"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"[#<ResolvedPointcutDefinition pointcut cl.pleiad.ajlmp.testMutExHeuristics.FirstAspect.pc1()>]\")}"))

(deftest test-inclusion
  (test/tuples-correspond
    (ekeko [?modifier ?modified]
           (assumptions/modifies-aspect1-aspect2 ?modifier ?modified))
    "#{(\"cl.pleiad.ajlmp.testInclusion.ThirdAspect\" \"cl.pleiad.ajlmp.testInclusion.FirstAspect\") (\"cl.pleiad.ajlmp.testInclusion.SecondAspect\" \"cl.pleiad.ajlmp.testInclusion.FirstAspect\")}"))

(deftest test-reentrant-aspect-advice 
  (test/tuples-correspond
    (ekeko [?aspect ?advice] (assumptions/reentrant-aspect-advice ?aspect ?advice))
"#{(\"cl.pleiad.ajlmp.testReentrancy.InfiniteLoop\" \"(before: (call(* *(..)) && persingleton(cl.pleiad.ajlmp.testReentrancy.InfiniteLoop))->void cl.pleiad.ajlmp.testReentrancy.InfiniteLoop.ajc$before$cl_pleiad_ajlmp_testReentrancy_InfiniteLoop$1$22d13d5e())\")}"))


;; Test Suite
;; ----------

; an example to generate the tuple string for the tests:
; (test/tuples-to-stringsetstring (ekeko [?a1 ?a2] (assumptions/sameshadows-aspect1-aspect2 ?a1 ?a2)))

;note: (runtests) will report that one more test has been run (successfully)
;than those that are listed here .. seems "normal"

(deftest
  test-suite 
  ;sanity check
  (test/against-project-named "AJ-LMP-Precedence" false test-aspect)

  ;precedence logic
  (test/against-project-named "AJ-LMP-Precedence" false test-explicit-decprec+)
  (test/against-project-named "AJ-LMP-Precedence" false test-implicit-precedence+)
  (test/against-project-named "AJ-LMP-Precedence" false test-aspect-dominates-aspect)
  
  ;assumptions
  (test/against-project-named "AJ-LMP-Precedence" false test-overriden-implicit-precedence-shadow)
  (test/against-project-named "AJ-LMP-Precedence" false test-incomplete-precedence)
  (test/against-project-named "AJ-LMP-Precedence" false test-incomplete-precedence-shadow)
 
  ;simple reentrancy code example
  (test/against-project-named"AJ-LMP-SimpleReentrancy" false test-reentrant-aspect-advice)
  
  ;assumptions from paper
  ; paper 3.1.1 assumption 1 case 1 and case 2. Also paper 3.2.3 assumption 2
  (test/against-project-named "AJ-LMP-Inclusion" false test-inclusion)
  ;3.1.1 assumption 2, case 1  
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-pointcutname)
  ;3.1.1 assumption 2, case 2
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-superpointcuts-reuse)
  ;3.1.1 assumption 2, case 3
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-shadows)
  ; 3.1.1 assumption 5
  (test/against-project-named "AJ-LMP-ITD" true test-intertype|method|unused)
  ; 3.1.1 assumption 7 
  (test/against-project-named "AJ-LMP-Pointcuts" false test-concretization)
  ;3.1.1: assumption 9 case 2
  (test/against-project-named "AJ-LMP-Precedence" false test-overriden-implicit-precedence)
  )

(defn 
  test-ns-hook 
  [] 
  (test/with-ekeko-disabled test-suite))

;; Example REPL Session that runs the test
;; ---------------------------------------

; note: uncommenting would run the tests upon loading

(comment  
  
  (run-tests)
  
 )
