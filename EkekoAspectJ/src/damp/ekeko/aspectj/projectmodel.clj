(ns damp.ekeko.aspectj.projectmodel
  (:import 
    [damp.ekeko.aspectj AspectJProjectModel])
  (:require
    [damp.ekeko [ekekomodel :as ekekomodel]]))




(defn 
  aspectj-project-models
  "Returns all AspectJProjectModel instances that are to be queried.
   Subset of (queried-project-models), which is itself 
   a subset of (all-project-models)."
  []
  (filter (fn [project-model] (instance? AspectJProjectModel project-model))
          (ekekomodel/queried-project-models)))



;(ekekomodel/queried-project-models)
