(ns test.damp.ekeko.aspectj
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic :exclude [is]] :reload)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:require
    [clojure.core.logic :as l] 
    [test.damp [ekeko :as test]]
    [damp.ekeko.workspace
     [workspace :as ws]]
    [damp.ekeko.soot
     [projectmodel :as spm]]
    [damp.ekeko [aspectj]]
    [damp.ekeko.aspectj
     [soot :as soot]
     [ajdt :as ajdt]
     [xcut :as xcut] 
     [weaverworld :as world]
     [assumptions :as assumptions]
     [annotations :as annotations]
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
    (println "Aspects found: " grounding)
    (is (> (count grounding) 0))
    (is (empty? (clojure.set/difference (set grounding) (set grounding-and-checking))))))

(deftest
  test-explicit-decprec+
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect|dominates-aspect-explicitly+ ?dom ?sub))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest
  test-implicit-precedence+
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect|dominates|implicitly-aspect+ ?dom ?sub))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\")}"))

(deftest
  test-aspect-dominates-aspect
  (test/tuples-correspond 
    (ekeko [?dom ?sub]
           (world/aspect|dominates-aspect ?dom ?sub))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest 
  test-overriden-implicit-precedence
  (test/tuples-correspond
    (ekeko [?first ?second]
           (assumptions/overriden|implicit|precedence ?first ?second))
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
           (assumptions/same|pointcut|name-aspect1-aspect2 ?name ?aspect1 ?aspect2))
    "#{(\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\") (\"pc2\" \"cl.pleiad.ajlmp.testMutExHeuristics.ThirdAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.SecondAspect\")}"))

(deftest test-same-shadows
  (test/tuples-correspond
    (ekeko [?aspect1 ?aspect2]
           (assumptions/sameshadows|aspect1-aspect2 ?aspect1 ?aspect2))
    "#{(\"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\") (\"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\")}"))

(deftest test-same-superpointcuts-reuse
  (test/tuples-correspond
    (ekeko [?aspect1 ?aspect2 ?usedpc1]
           (assumptions/samepointcuts|reuse|from|super|sub1-sub2-usedpc ?aspect1 ?aspect2 ?usedpc1))
    "#{(\"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"[#<ResolvedPointcutDefinition pointcut cl.pleiad.ajlmp.testMutExHeuristics.FirstAspect.pc1()>]\") (\"cl.pleiad.ajlmp.testMutExHeuristics.FifthAspect\" \"cl.pleiad.ajlmp.testMutExHeuristics.FourthAspect\" \"[#<ResolvedPointcutDefinition pointcut cl.pleiad.ajlmp.testMutExHeuristics.FirstAspect.pc1()>]\")}"))

(deftest test-refinepc-sub-super
  (test/tuples-correspond
    (ekeko [?pointcutdef ?subaspect ?superaspect]
           (assumptions/refine|used|pointcut-sub-super ?pointcutdef ?subaspect ?superaspect))
    "#{(\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc2()\" \"cl.pleiad.ajlmp.testPointcuts.FirstAspect\" \"cl.pleiad.ajlmp.testPointcuts.AbstractAspect\") (\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc1()\" \"cl.pleiad.ajlmp.testPointcuts.SecondAspect\" \"cl.pleiad.ajlmp.testPointcuts.AbstractAspect\")}"))

(deftest test-inclusion
  (test/tuples-correspond
    (ekeko [?modifier ?modified]
           (assumptions/modifies|aspect1-aspect2 ?modifier ?modified))
    "#{(\"cl.pleiad.ajlmp.testInclusion.ThirdAspect\" \"cl.pleiad.ajlmp.testInclusion.FirstAspect\") (\"cl.pleiad.ajlmp.testInclusion.SecondAspect\" \"cl.pleiad.ajlmp.testInclusion.FirstAspect\")}"))

(deftest test-reentrant-aspect-advice 
  (test/tuples-correspond
    (ekeko [?aspect ?advice] (assumptions/reentrant-aspect-advice ?aspect ?advice))
    "#{(\"cl.pleiad.ajlmp.testReentrancy.InfiniteLoop\" \"(before: (call(* *(..)) && persingleton(cl.pleiad.ajlmp.testReentrancy.InfiniteLoop))->void cl.pleiad.ajlmp.testReentrancy.InfiniteLoop.ajc$before$cl_pleiad_ajlmp_testReentrancy_InfiniteLoop$1$22d13d5e())\")}"))

(deftest test-declareparents-markerinterface
  (test/tuples-correspond
    (ekeko [?aspect ?interface] (assumptions/aspect-declareparents|markerinterface ?aspect ?interface))
    "#{(\"cl.pleiad.ajlmp.markeriface.ThirdAspect\" \"cl.pleiad.ajlmp.markeriface.MarkerSub\") (\"cl.pleiad.ajlmp.markeriface.FirstAspect\" \"cl.pleiad.ajlmp.markeriface.Marker\")}"))

(deftest test-naive-wormhole
  (test/tuples-correspond
    (ekeko [?aspect ?advice|entry ?advice|exit ?field] (assumptions/wormhole|naive-entry-exit-field ?aspect ?advice|entry ?advice|exit ?field))
    "#{(\"cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect\" \"(before: (execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*2()) && persingleton(cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect))->void cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_WrongOrderWormholeAspect$1$b325ee9f())\" \"(before: ((execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*1(int)) && args(BindingTypePattern(int, 0))) && persingleton(cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect))->void cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_WrongOrderWormholeAspect$2$72a7b216(int))\" \"int cl.pleiad.ajlmp.testWormhole.WrongOrderWormholeAspect.store\") (\"cl.pleiad.ajlmp.testWormhole.WormholeAspect\" \"(before: ((execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*1(int)) && args(BindingTypePattern(int, 0))) && persingleton(cl.pleiad.ajlmp.testWormhole.WormholeAspect))->void cl.pleiad.ajlmp.testWormhole.WormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_WormholeAspect$1$9821f264(int))\" \"(before: (execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*2()) && persingleton(cl.pleiad.ajlmp.testWormhole.WormholeAspect))->void cl.pleiad.ajlmp.testWormhole.WormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_WormholeAspect$2$b325ee9f())\" \"int cl.pleiad.ajlmp.testWormhole.WormholeAspect.store\") (\"cl.pleiad.ajlmp.testWormhole.TSWormholeAspect\" \"(before: ((execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*1(int)) && args(BindingTypePattern(int, 0))) && percflow(cl.pleiad.ajlmp.testWormhole.TSWormholeAspect on execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.run())))->void cl.pleiad.ajlmp.testWormhole.TSWormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_TSWormholeAspect$1$9821f264(int))\" \"(before: (execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.*2()) && percflow(cl.pleiad.ajlmp.testWormhole.TSWormholeAspect on execution(* cl.pleiad.ajlmp.testWormhole.BaseClass.run())))->void cl.pleiad.ajlmp.testWormhole.TSWormholeAspect.ajc$before$cl_pleiad_ajlmp_testWormhole_TSWormholeAspect$2$b325ee9f())\" \"int cl.pleiad.ajlmp.testWormhole.TSWormholeAspect.store\")}"))

(deftest test-annotation-label-types
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?val]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/labeled|type-label|val ?type ?val)))
"#{(\"damp.ekeko.aspectj.annotationtests.LaFix2\" \"[\\\"Label2a\\\" \\\"Label2b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.LaFix1\" \"[\\\"Label1\\\"]\")}"))

(deftest test-annotation-label-behavior
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?val]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/labeled|behavior-label|val ?type ?val)))
   "#{(\"void damp.ekeko.aspectj.annotationtests.LaFix2.labeledMethod()\" \"[\\\"MethLabel2\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix2.ajc$before$damp_ekeko_aspectj_annotationtests_LaFix2$1$8598ac45()\" \"[\\\"AdvLabel2\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix2.<init>()\" \"[\\\"ConsLabel2\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix1.<init>()\" \"[\\\"ConsLabel\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix2.multiLabeledMethod()\" \"[\\\"MethLabel2a\\\" \\\"MethLabel2b\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix1.<init>(java.lang.String)\" \"[\\\"ConsLabel1a\\\" \\\"ConsLabel1b\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix1.labeledMethod()\" \"[\\\"MethLabel\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.LaFix2.ajc$before$damp_ekeko_aspectj_annotationtests_LaFix2$3$716cfb74()\" \"[\\\"AdvLabel2a\\\" \\\"AdvLabel2b\\\"]\")}"))

(deftest test-annotation-label-pointcut
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?ann]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/labeled|pointcut-label|val ?type ?ann)))
    "#{(\"pointcut damp.ekeko.aspectj.annotationtests.LaFix2.barCall()\" \"[\\\"PCLabel2\\\"]\")}"))

(deftest test-annotation-requires
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?key ?val]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/requiring|type-key-val ?type ?key ?val)))
    "#{(\"damp.ekeko.aspectj.annotationtests.REFix3\" \"label\" \"[\\\"Label3a\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix1\" \"type\" \"[\\\"Aspect1\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix3\" \"type\" \"[\\\"Aspect3a\\\" \\\"Aspect3b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix2\" \"label\" \"[\\\"Label2\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix4\" \"label\" \"[\\\"Label4a\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix4\" \"type\" \"[\\\"Aspect4a\\\" \\\"Aspect4b\\\"]\")}"))

(deftest test-annotation-excludes
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?key ?val]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/excluding|type-key-val ?type ?key ?val)))
    "#{(\"damp.ekeko.aspectj.annotationtests.REFix2\" \"type\" \"[\\\"Aspect2\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix3\" \"type\" \"[\\\"Aspect3c\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix1\" \"label\" \"[\\\"Label1\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix3\" \"label\" \"[\\\"Label3b\\\" \\\"Label3c\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix4\" \"label\" \"[\\\"Label4b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.REFix4\" \"type\" \"[\\\"Aspect4c\\\"]\")}"))

(deftest test-annotation-oneOf
  (test/tuples-correspond
    (damp.ekeko/ekeko [?type ?key ?val]
      (l/all
        (world/type-packageName ?type "damp.ekeko.aspectj.annotationtests")
        (annotations/oneOfing|type-key-val ?type ?key ?val)))
    "#{(\"damp.ekeko.aspectj.annotationtests.OOFix1\" \"type\" \"[\\\"Aspect1a\\\" \\\"Aspect1b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.OOFix2\" \"label\" \"[\\\"Label2a\\\" \\\"Label2b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.OOFix3\" \"type\" \"[\\\"Aspect3a\\\" \\\"Aspect3b\\\"]\") (\"damp.ekeko.aspectj.annotationtests.OOFix3\" \"label\" \"[\\\"Label3a\\\" \\\"Label3b\\\"]\")}"))

(deftest test-annotation-requiresPrevious
  (test/tuples-correspond
    (damp.ekeko/ekeko [?beh ?val] (annotations/reqPrev|behavior-val ?beh ?val))
"#{(\"void damp.ekeko.aspectj.annotationtests.REPFix2.ajc$around$damp_ekeko_aspectj_annotationtests_REPFix2$3$8598ac45(org.aspectj.runtime.internal.AroundClosure)\" \"[\\\"Label2Rf\\\" \\\"Label2Rg\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix2.ajc$before$damp_ekeko_aspectj_annasstests_PrevFix2$1$36c63601()\" \"[\\\"beforecompute\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix2.ajc$after$damp_ekeko_aspectj_annasstests_PrevFix2$3$36c63601()\" \"[\\\"preparing\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$before$damp_ekeko_aspectj_annasstests_PrevFix$1$36c63601()\" \"[\\\"thinking\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix2.ajc$after$damp_ekeko_aspectj_annasstests_PrevFix2$2$36c63601()\" \"[\\\"beforecompute\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$around$damp_ekeko_aspectj_annasstests_PrevFix$3$592dcb64(org.aspectj.runtime.internal.AroundClosure)\" \"[\\\"executing\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix2.ajc$after$damp_ekeko_aspectj_annasstests_PrevFix2$4$74faf45e()\" \"[\\\"beforecompute\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.methoda()\" \"[\\\"Label2Rc\\\" \\\"Label2Rd\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.ajc$before$damp_ekeko_aspectj_annotationtests_REPFix2$1$8598ac45()\" \"[\\\"Label2Re\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix1.methoda()\" \"[\\\"Label1Rb\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$after$damp_ekeko_aspectj_annasstests_PrevFix$2$74faf45e()\" \"[\\\"executing\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix1.<init>()\" \"[\\\"Label1Ra\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.<init>()\" \"[\\\"Label2Ra\\\" \\\"Label2Rb\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix2.unthink()\" \"[\\\"thinking\\\"]\") (\"void damp.ekeko.aspectj.annasstests.EntryPoint.execute()\" \"[\\\"computing\\\"]\")}"))


(deftest test-annotation-excludesPrevious
  (test/tuples-correspond
    (damp.ekeko/ekeko [?beh ?val] (annotations/exclPrev|behavior-val ?beh ?val))
"#{(\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$before$damp_ekeko_aspectj_annasstests_PrevFix$1$36c63601()\" \"[\\\"cleaning\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.ajc$around$damp_ekeko_aspectj_annotationtests_REPFix2$3$8598ac45(org.aspectj.runtime.internal.AroundClosure)\" \"[\\\"Label2Ef\\\" \\\"Label2Eg\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$after$damp_ekeko_aspectj_annasstests_PrevFix$2$74faf45e()\" \"[\\\"thinking\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.methodb()\" \"[\\\"Label2Ec\\\" \\\"Label2Ed\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.ajc$after$damp_ekeko_aspectj_annotationtests_REPFix2$2$8598ac45()\" \"[\\\"Label2Ee\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix1.methodb()\" \"[\\\"Label1Eb\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix2.<init>()\" \"[\\\"Label2Ea\\\" \\\"Label2Eb\\\"]\") (\"void damp.ekeko.aspectj.annotationtests.REPFix1.<init>(int)\" \"[\\\"Label1Ea\\\"]\") (\"void damp.ekeko.aspectj.annasstests.PrevFix.ajc$around$damp_ekeko_aspectj_annasstests_PrevFix$3$592dcb64(org.aspectj.runtime.internal.AroundClosure)\" \"[\\\"simulating\\\"]\")}"))

(deftest test-logic-requires
  (test/tuples-correspond
    (damp.ekeko/ekeko [?rqd ?rqs]
        (l/all
            (world/type-packageName ?rqs "damp.ekeko.aspectj.annasstests")
            (assumptions/missing|required-requires ?rqd ?rqs)))
    "#{(\"damp.ekeko.aspectj.annasstests.AbsentReqSat\" \"damp.ekeko.aspectj.annasstests.ReqFix\") (\"AbsentReqLabelSat\" \"damp.ekeko.aspectj.annasstests.ReqLabFix\")}"
))

(deftest test-logic-excludes
  (test/tuples-correspond
    (damp.ekeko/ekeko [?exd ?exr]
        (l/all
            (world/type-packageName ?exr "damp.ekeko.aspectj.annasstests")
            (assumptions/present|excluded-excluder ?exd ?exr)))
    "#{(\"damp.ekeko.aspectj.annasstests.ReqLabFix\" \"damp.ekeko.aspectj.annasstests.ExcFix\") (\"damp.ekeko.aspectj.annasstests.ReqFix\" \"damp.ekeko.aspectj.annasstests.ExcFix\")}"))

(deftest test-logic-oneOf
  (test/tuples-correspond
   (damp.ekeko/ekeko [?target ?offs]
        (l/all
            (world/type-packageName ?target "damp.ekeko.aspectj.annasstests")
            (assumptions/oneOf|definer-offenders ?target ?offs)))
"#{(\"damp.ekeko.aspectj.annasstests.OneOfFix3\" \"[[#<ReferenceType damp.ekeko.aspectj.annasstests.ReqSatLeaf1>] [#<ReferenceType damp.ekeko.aspectj.annasstests.ReqSatLeaf2>] [#<ReferenceType damp.ekeko.aspectj.annasstests.ReqSatTree>]]\") (\"damp.ekeko.aspectj.annasstests.OneOfFix2\" \"[[#<ReferenceType damp.ekeko.aspectj.annasstests.ReqFix>] [#<ReferenceType damp.ekeko.aspectj.annasstests.ReqLabFix>]]\") (\"damp.ekeko.aspectj.annasstests.OneOfFix\" \"[]\") (\"damp.ekeko.aspectj.annasstests.OneOfFix4\" \"[]\")}"))

;; Test Suite
;; ----------

; an example to generate the tuple string for the tests:
; (test/tuples-to-stringsetstring (ekeko [?a1 ?a2] (assumptions/sameshadows-aspect1-aspect2 ?a1 ?a2)))

;note: (runtests) will report that one more test has been run (successfully)
;than those that are listed here .. seems "normal"

(deftest
  test-suite  
 

  (comment
  
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
  ; paper 3.1.1 assumption 1 special case 2
  (test/against-project-named "AJ-LMP-MarkerIface" false test-declareparents-markerinterface)
  ;3.1.1 assumption 2, case 1  
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-pointcutname)
  ;3.1.1 assumption 2, case 2
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-superpointcuts-reuse)
  ;3.1.1 assumption 2, case 3
  (test/against-project-named "AJ-LMP-MutExHeuristics" false test-same-shadows)
  ; 3.1.1 assumption 5
  ;(test/against-project-named "AJ-LMP-ITD" true test-intertype|method|unused)
  ;temporarily disabled because running soot takes too long in an integration test 
  ; 3.1.1 assumption 6
  (test/against-project-named "AJ-LMP-Pointcuts" false test-refinepc-sub-super)
  ; 3.1.1 assumption 7 
  (test/against-project-named "AJ-LMP-Pointcuts" false test-concretization)
  ;3.1.1: assumption 9 case 2
  (test/against-project-named "AJ-LMP-Precedence" false test-overriden-implicit-precedence)
  ;3.1.2 assumption 1 case 1
  ;(test/against-project-named "AJ-LMP-Wormhole" true test-naive-wormhole)
  ;temporarily disabled because running soot takes too long in an integration test 

  ;;end commented out part
)

  ;;Annotations 
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-label-types)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-label-behavior)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-label-pointcut)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-requires)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-excludes)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-oneOf)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-requiresPrevious)
  (test/against-project-named "AJ-LMP-Annotations" false test-annotation-excludesPrevious) 
  
  ;;Annotation assumptions
  (test/against-project-named "AJ-LMP-Annotations" false test-logic-requires)
  (test/against-project-named "AJ-LMP-Annotations" false test-logic-excludes)
  (test/against-project-named "AJ-LMP-Annotations" false test-logic-oneOf)

)

(defn 
  test-ns-hook 
  [] 
  (test/with-ekeko-disabled test-suite))


; run the tests upon loading  
(run-tests)
  
