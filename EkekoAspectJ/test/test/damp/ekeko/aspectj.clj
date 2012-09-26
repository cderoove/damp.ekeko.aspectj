(ns test.damp.ekeko.aspectj
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic :exclude [is]] :reload)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko]) 
  (:require
    [damp.ekeko.workspace
     [workspace :as ws]]
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

;todo: add parameter to against-project-named that controls whether soot should be run as well


;see http://richhickey.github.com/clojure/clojure.test-api.html for api
;followed http://twoguysarguing.wordpress.com/2010/03/24/fixies/ to workaround limitations regarding setup/teardown of in


;; Supporting functions
;; --------------------

;; Test setup / teardown

(defn
  with-ekeko-disabled
  [f]
  (println "Disabling Ekeko nature on all projects.")
  (ws/workspace-disable-ekeko!)
  (ws/workspace-wait-for-builds-to-finish)
  (f))
    
(defn
  against-project
  [p f]
  (try
    (println "Enabling Ekeko nature on project: " p)
    (ws/workspace-project-enable-ekeko! p)
    (ws/workspace-wait-for-builds-to-finish)
    (f)
    (finally
      (println "Disabling Ekeko nature on project: " p)
      (ws/workspace-project-disable-ekeko! p))))

(defn
  against-project-named
  [n f]
  (against-project (ws/workspace-project-named  n) f))


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
   converted inner sequences."
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
  concretization-test
  (tuples-are 
    (ekeko [?abpointcut ?concpointcut1 ?concpointcut2]
           (assumptions/abstractpointcut-concretized-reconcretized ?abpointcut ?concpointcut1 ?concpointcut2))
    
    "#{(\"pointcut cl.pleiad.ajlmp.testPointcuts.AbstractAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.FirstAspect.abstractpc1()\" \"pointcut cl.pleiad.ajlmp.testPointcuts.SecondAspect.abstractpc1()\")}"))

;; Test Suite
;; ----------

;note: (runtests) will report that one more test has been run (successfully)
;than those that are listed here .. seems "normal"

(deftest
  test-suite 
  (against-project-named "AJ-LMP-Precedence" aspect-test )
  (against-project-named "AJ-LMP-Pointcuts" concretization-test))

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
