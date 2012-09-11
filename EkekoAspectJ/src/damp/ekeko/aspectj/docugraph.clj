(ns 
  damp.ekeko.aspectj.docugraph
  (:require 
    [damp.ekeko.aspectj [ajdtastnode :as astnode]]
    [dorothy [core :as dot]]))


;;Unfortunately, mostly a copy/paste from the corresponding JDT one
;;only difference being the namespace the property descriptor functions stem from
;;todo: figure out a way to eliminate this code duplication

(defn 
  property-graph
  [class]
  (let 
    [name (.getSimpleName class)
     properties 
     
     (astnode/nodeclass-property-descriptors class)
     
     
     ]
    {:nodes 
     (into 
       #{}
       (map (fn [p]
              [(astnode/property-descriptor-id p)
               {:color :gray 
                :style
                (cond 
                 (astnode/property-descriptor-child? p) 
                 :solid
                 (astnode/property-descriptor-list? p)
                 :dotted
                 (astnode/property-descriptor-simple? p)
                 :dashed)
               }])
            properties))
     :edges
     (into 
       #{}
       (concat
         (map (fn [p]
                [name :> (astnode/property-descriptor-id p)
                 {:color :gray}])
              properties)
         (for [p properties]
           (let [id 
                 (astnode/property-descriptor-id p)
                 destination 
                 (.getSimpleName 
                   (cond 
                     (astnode/property-descriptor-child? p) 
                     (astnode/property-descriptor-child-node-class p)
                     (astnode/property-descriptor-list? p)
                     (astnode/property-descriptor-element-node-class p)
                     (astnode/property-descriptor-simple? p)
                     (astnode/property-descriptor-value-class p)))]
             [id :> destination {:color :gray}]))))}))

(defn 
  super-graph 
  ([class]
    (super-graph class #{} #{})) ;speciale vorm geven want leaf
  ([class nodes edges]
    (if-let
      [super (.getSuperclass class)]
      (recur super 
             (conj nodes [(.getSimpleName super)])
             (conj edges [(.getSimpleName super) :> (.getSimpleName class)])) ;pijlen omdraaien door richting te veranderen (zie er schema)
      (zipmap [:nodes :edges] [nodes edges]))))
  
(defn 
  jdt-graph
  [astnode-classes]
  (reduce
    (fn [sofar class]
      (let [descriptors (astnode/nodeclass-property-descriptors class)]
        (merge-with
          clojure.set/union
          sofar
          {:nodes #{ [(.getSimpleName class)  ] }
           :edges #{}
           }
          (super-graph class)
          (property-graph class)
          )))
    {:nodes #{}
     :edges #{}}
    astnode-classes))            

(defn 
  graph
  [astnode-classes]
  (dot/digraph :JDT (apply concat (vals (jdt-graph astnode-classes)))))

;supers (map (memfn getSimpleName) (supers class))


(defn safe-ajdt-node-classes-aspectj []
  (clojure.set/difference 
    (set (astnode/ajdt-node-classes-aspectj))
    #{;org.aspectj.org.eclipse.jdt.core.dom.DeclareDeclaration 
      org.aspectj.org.eclipse.jdt.core.dom.AndPointcut
      org.aspectj.org.eclipse.jdt.core.dom.CflowPointcut
      org.aspectj.org.eclipse.jdt.core.dom.OrPointcut
      org.aspectj.org.eclipse.jdt.core.dom.PerObject
      ;org.aspectj.org.eclipse.jdt.core.dom.PointcutDesignator
      ;org.aspectj.org.eclipse.jdt.core.dom.DeclareAnnotationDeclaration
      ;org.aspectj.org.eclipse.jdt.core.dom.PatternNode
      org.aspectj.org.eclipse.jdt.core.dom.PerCflow
      ;org.aspectj.org.eclipse.jdt.core.dom.TypePattern
      }))




(comment
  (spit "/Users/cderoove/Desktop/ajdt_docu_graph.dot" (dot/dot (graph (safe-ajdt-node-classes-aspectj))))

  
  ;(spit "/Users/cderoove/Desktop/jdt_docu_graph.dot" (dot/dot (graph (astnode/node-classes))))
  
  ;(spit "/Users/cderoove/Desktop/jdt_docu_graph.dot" (dot/dot (graph [org.eclipse.jdt.core.dom.MethodDeclaration])))
  
  )
      
  
  