(ns test.damp.ekeko.aspectj
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic :exclude [is]] :reload)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko]) 
  (:require
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


;see http://richhickey.github.com/clojure/clojure.test-api.html for api
;followed http://twoguysarguing.wordpress.com/2010/03/24/fixies/ to workaround limitations regarding setup/teardown of in


;; Supporting functions
;; --------------------

;; Test setup / teardown

(defn
  with-ekeko-disabled
  [f]
  (println "Disabling Ekeko nature on all projects.")
  (spm/workspace-disable-soot!)
  (ws/workspace-disable-ekeko!)
  (ws/workspace-wait-for-builds-to-finish)
  (f))
    
(defn
  against-project
  [p enable-soot? f]
  (try
    (println "Enabling Ekeko nature on project: " p)
    (ws/workspace-project-enable-ekeko! p)
    (ws/workspace-wait-for-builds-to-finish)
    (when 
      enable-soot?
      (do
        (println "Enabling Soot nature on project: " p)
        (spm/enable-soot-nature! p)))
    (f)
    (finally
      (println "Disabling Ekeko nature for project: " p)
      (when 
        enable-soot?
        (do
          (println "Disabling Soot nature for project: " p)
          (spm/disable-soot-nature! p)))
      (ws/workspace-project-disable-ekeko! p))))

(defn
  against-project-named
  [n enable-soot? f]
  (against-project (ws/workspace-project-named n) enable-soot? f))


;; Query results

(defn
  tuples-to-stringset
  "For a sequence of sequences (e.g., query results), converts each inner sequence 
   element to a string and returns the resulting set of converted inner sequences."
  [seqofseqs]
  (set (map (partial map str) seqofseqs)))

(defn
  tuples-to-stringsetstring
  "For a sequence of sequences (e.g., query results), converts each inner sequence 
   element to a string and returns the string representation of the resulting set of
   converted inner sequences.

   Run this on the results of a working query to transform them to a string that is
   used for the tests. For example:
   (tuples-to-stringsetstring (damp.ekeko/ekeko [?itmethod]
           (assumptions/intertypemethod-unused ?itmethod)))"
  [seqofseqs]
  (str (tuples-to-stringset seqofseqs)))

(defn
  tuples-are
  "Verifies whether the stringset representation of the tuples corresponds to the given string
   (obtained through tuples-to-stringsetstring)."
  [tuples stringsetstring]
  (is 
    (empty?
      (clojure.set/difference
        (tuples-to-stringset tuples)
        (read-string stringsetstring)))))

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
  (tuples-are 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-explicitly+ ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest
  implicit-precedence+-test 
  (tuples-are 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect-implicitly+ ?dom ?sub))
 "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\")}"))

;This test is so-so:
;JF not check if there are missing results or duplicates, just that the results make sense
(deftest
  aspect-dominates-aspect-test 
  (tuples-are 
    (ekeko [?dom ?sub]
           (world/aspect-dominates-aspect ?dom ?sub))
"#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FifthAspect\")}"))

(deftest 
  overriden-implicit-precedence-test
  (tuples-are
    (ekeko [?first ?second]
           (assumptions/overriden-implicit-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\")}"))

(deftest
  incomplete-precedence-test
  (tuples-are
    (ekeko [?first ?second]
           (assumptions/incomplete-precedence ?first ?second))
    "#{(\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.EightAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.FourthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.EightAspect\") (\"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FirstAspect\" \"cl.pleiad.ajlmp.testPrecedence.ThirdAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SixthAspect\" \"cl.pleiad.ajlmp.testPrecedence.FirstAspect\") (\"cl.pleiad.ajlmp.testPrecedence.FourthAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SecondAspect\" \"cl.pleiad.ajlmp.testPrecedence.SixthAspect\") (\"cl.pleiad.ajlmp.testPrecedence.SeventhAspect\" \"cl.pleiad.ajlmp.testPrecedence.SecondAspect\")}"))

(deftest
  concretization-test
  (tuples-are 
    (ekeko [?abpointcut ?concpointcut1 ?concpointcut2]
           (assumptions/abstractpointcut-concretized-reconcretized ?abpointcut ?concpointcut1 ?concpointcut2))
    "#{(\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.FirstAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.SecondAspect.abstractpc1()\")}"))

(deftest
  intertypemethod-unused-test
  (tuples-are
    (ekeko [?itmethod]
           (assumptions/intertypemethod-unused ?itmethod))
    "#{(\"(BcelTypeMunger ResolvedTypeMunger(Method, void cl.pleiad.ajlmp.testITD.BaseClass.itdB()))\")}"))



;; Test Suite
;; ----------

;note: (runtests) will report that one more test has been run (successfully)
;than those that are listed here .. seems "normal"

(deftest
  test-suite 
  ; testing precedence logic -- commented out for speed
  ;(against-project-named "AJ-LMP-Precedence" false aspect-test )
  ;(against-project-named "AJ-LMP-Precedence" false explicit-decprec+-test)
  ;(against-project-named "AJ-LMP-Precedence" false implicit-precedence+-test)
  ;(against-project-named "AJ-LMP-Precedence" false aspect-dominates-aspect-test)
  
  ;testing assumptions
  (against-project-named "AJ-LMP-Precedence" false overriden-implicit-precedence-test)
  (against-project-named "AJ-LMP-Precedence" false incomplete-precedence-test)
  (against-project-named "AJ-LMP-Pointcuts" false concretization-test)
  (against-project-named "AJ-LMP-ITD" true intertypemethod-unused-test)
  )

(defn 
  test-ns-hook 
  [] 
  (with-ekeko-disabled test-suite))

;; Example REPL Session that runs the test
;; ---------------------------------------

; note: uncommenting would run the tests upon loading

(comment  
  
  (run-tests)
  
  )
