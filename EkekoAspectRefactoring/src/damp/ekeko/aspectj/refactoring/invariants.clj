(ns damp.ekeko.aspectj.refactoring.invariants
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic])
  (:use [damp.ekeko.aspectj])
  (:use [damp.ekeko.aspectj.weaverworld]))


(defn invariant []
 (into #{} (run* [?s] (aspect-shadow (lvar) ?s))))

