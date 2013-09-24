(ns damp.ekeko.aspectj.refactoring.invariants
  ^{:doc "Invariant checked when effecting a refactoring."
    :author "Carlos Noguera" }
  (:refer-clojure :exclude [== type declare])
  (:use [clojure.core.logic])
  (:use [damp.ekeko.aspectj])
  (:use [damp.ekeko.aspectj.weaverworld]))


(defn invariant []
 (into #{} (run* [?s] (aspect-shadow (lvar) ?s))))

