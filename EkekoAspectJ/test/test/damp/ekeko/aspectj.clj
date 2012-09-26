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

;see http://richhickey.github.com/clojure/clojure.test-api.html for api
;followed http://twoguysarguing.wordpress.com/2010/03/24/fixies/ to workaround limitations regarding setup/teardown of in

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
  test-suite 
  (against-project-named "AJ-LMP-Precedence" aspect-test)
  (against-project-named "AJ-LMP-Wormhole" aspect-test))

(defn 
  test-ns-hook 
  [] 
  (with-ekeko-disabled test-suite))
   

(comment  
  (run-tests)
  )
