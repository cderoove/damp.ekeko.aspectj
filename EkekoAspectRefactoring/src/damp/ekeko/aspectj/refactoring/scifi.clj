(ns damp.ekeko.aspectj.refactoring.scifi
  ^{:doc "Source Change Invariants through Fictional Input"
    :author "Carlos Noguera" }
 (:refer-clojure :exclude [== type declare class])
 (:require [damp.ekeko.aspectj.weaverworld :as aj])
 (:require [clojure.core.logic :as l])
 (:require [damp.ekeko.jdt 
            [reification :as reif :exclude [type-field]]
            [basic :as basic]] )
 (:require [clojure.string :as str])
 (:require [clojure.set :as set])
 (:import [damp.ekeko.aspectj.refactoring.recompiling ChangeSimulator]))


(def change_sim (ChangeSimulator.))

(defn register-icu-content [icu content]
 (.registerNewContent change_sim icu content))

(defn- simulate-changes []
  (.simulateChange change_sim))

(defn- revert-changes []
  (.restoreState change_sim))

 (def select-values (comp vals select-keys))

(defrecord Change [cu content])

(defn- compare-sets [a b]
  (let [a-map (zipmap (map #(.toString %1) a) a)
        diff (set/difference (set (map #(.toString %1) a)) (set (map #(.toString %1) b)))]
    (select-values a-map diff)
  ))

(defn validate [before after changes]
  (let [bef (set (l/run* [?a] (before ?a)))]
    (doall (map #(register-icu-content (:cu %1) (:content %1)) changes))
    (simulate-changes)
    (let [aft (set (l/run* [?a] (after ?a)))]
      (revert-changes) 
      {:add (compare-sets aft bef)
       :rem (compare-sets bef aft)})))


(comment
  
  (def test-icu (.getJavaElement (first (l/run 1 [a] (reif/ast :CompilationUnit a)))))
  
  (def content (str/replace (.getSource test-icu) #"main" "maain"))
  
  (def empty-base "package cl.pleiad.ajlmp.testAdviceOrdering; public class BaseClass{}")
  
  (def test-change (Change. test-icu content))
  
  (defn method-decl [?a] 
    (l/all (reif/ast :MethodDeclaration ?a)))
  
  (validate  method-decl   method-decl  [test-change])
  
  (defn aspect-shadows [?a] (l/all (aj/aspect-shadow (l/lvar) ?a)))
  
  (validate aspect-shadows aspect-shadows [(Change. test-icu empty-base)])
  ;{:add nil, :rem (#<ProgramElement method-call(void cl.pleiad.ajlmp.testAdviceOrdering.subClass.baseMehod1())>)}
  )