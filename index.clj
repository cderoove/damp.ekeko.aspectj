{:namespaces
 ({:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/d1a5934c2848d05b635e1f51cb78b8e1721f6c26/EkekoAspectJ/src/damp/ekeko/aspectj.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj-api.html",
   :name "damp.ekeko.aspectj",
   :doc nil}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.ajdt-api.html",
   :name "damp.ekeko.aspectj.ajdt",
   :author "Coen De Roover",
   :doc "Low-level AJDT relations."}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdtastnode.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.ajdtastnode-api.html",
   :name "damp.ekeko.aspectj.ajdtastnode",
   :doc nil}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/7d6e6f195bc3b7098d271683d365084b33353c23/EkekoAspectJ/src/damp/ekeko/aspectj/assumptions.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.assumptions-api.html",
   :name "damp.ekeko.aspectj.assumptions",
   :author "Coen De Roover, Johan Fabry",
   :doc
   "Relations for implementing parts of the 'aspect assumptions' paper."}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/692dd7080858aaec2b3121f6cd50c6d963359e92/EkekoAspectJ/src/damp/ekeko/aspectj/projectmodel.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.projectmodel-api.html",
   :name "damp.ekeko.aspectj.projectmodel",
   :doc nil}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/9220beb310eaeca040480b1a3b5acc11dd7bbb69/EkekoAspectJ/src/damp/ekeko/aspectj/soot.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.soot-api.html",
   :name "damp.ekeko.aspectj.soot",
   :author "Coen De Roover",
   :doc "Low-level AspectJ-specific Soot relations."}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.weaverworld-api.html",
   :name "damp.ekeko.aspectj.weaverworld",
   :author "Coen De Roover, Johan Fabry",
   :doc "Low-level AspectJ WeaverWorld relations."}
  {:source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj/damp.ekeko.aspectj.xcut-api.html",
   :name "damp.ekeko.aspectj.xcut",
   :author "Coen De Roover",
   :doc "Low-level XCUT (AJProjctModelFacade) relations."}),
 :vars
 ({:arglists ([?advice]),
   :name "advice",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L187",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/advice",
   :doc "Relation of advices.",
   :var-type "function",
   :line 187,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?ajcu ?sourcetype]),
   :name "ajcu-aspectbyannotation",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L169",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/ajcu-aspectbyannotation",
   :doc
   "Relation between an AJCompilationUnit and one of the aspects \nit defines using the @aspect annotation on a class. ",
   :var-type "function",
   :line 169,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?advice]),
   :name "aspect-advice",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L179",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-advice",
   :doc "Relation between an aspect and one if its advices.",
   :var-type "function",
   :line 179,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?declare]),
   :name "aspect-declare",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L196",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-declare",
   :doc "Relation between an aspect and one of its declares.",
   :var-type "function",
   :line 196,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?field]),
   :name "aspect-field",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L275",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-field",
   :doc "Relation between an aspect and one of its fields.",
   :var-type "function",
   :line 275,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?itd]),
   :name "aspect-intertype",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L228",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-intertype",
   :doc
   "Relation between an aspect and one of its intertype declarations.",
   :var-type "function",
   :line 228,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?itd]),
   :name "aspect-intertypefield",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L259",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-intertypefield",
   :doc
   "Relation between an aspect and one of its intertype field declarations.",
   :var-type "function",
   :line 259,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?itd]),
   :name "aspect-intertypemethod",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L244",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-intertypemethod",
   :doc
   "Relation between an aspect and one of its intertype method declarations.",
   :var-type "function",
   :line 244,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?method]),
   :name "aspect-method",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L299",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-method",
   :doc "Relation between an aspect and one of its methods.",
   :var-type "function",
   :line 299,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?aspect ?pointcut]),
   :name "aspect-pointcut",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L212",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/aspect-pointcut",
   :doc "Relation between an aspect and one of its pointcuts.",
   :var-type "function",
   :line 212,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?keyword ?node]),
   :name "ast",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L33",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/ast",
   :doc
   "AJDT version of ast/2.\n\n See also:\n API documentation of org.aspectj.org.eclipse.jdt.core.dom.ASTNode",
   :var-type "function",
   :line 33,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?keyword ?node ?child]),
   :name "child",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L78",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/child",
   :doc "AJDT version of child/3",
   :var-type "function",
   :line 78,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?node ?child]),
   :name "child+",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L88",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/child+",
   :doc
   "AJDT version of child+  \n\nSee also:\nTernary predicate child/3",
   :var-type "function",
   :line 88,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?ajcu]),
   :name "compilationunit",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L141",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/compilationunit",
   :doc "Relation of AJCompilationUnit instances.",
   :var-type "function",
   :line 141,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?ajcu ?aspect]),
   :name "compilationunit-aspect",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L152",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/compilationunit-aspect",
   :doc
   "Relation between an AJCompilationUnit and one of the aspects \nits defines using the aspect keyword.",
   :var-type "function",
   :line 152,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?declare]),
   :name "declare",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L204",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/declare",
   :doc "Relation of declares.",
   :var-type "function",
   :line 204,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?key ?element]),
   :name "element",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L316",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/element",
   :doc
   "Relation of IAspectJElement instances ?element and the keyword ?key representing their kind.",
   :var-type "function",
   :line 316,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?field]),
   :name "field",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L283",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/field",
   :doc "Relation of fields declared within an aspect.",
   :var-type "function",
   :line 283,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?keyword ?node ?child]),
   :name "has",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L54",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/has",
   :doc
   "AJDT version of has/3\n\nSee also: \nTernary predicate child/3 \nAPI documentation of org.aspectj.eclipse.jdt.core.dom.ASTNode \nand org.aspectj.org.eclipse.jdt.core.dom.StructuralPropertyDescriptor",
   :var-type "function",
   :line 54,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?intertype]),
   :name "intertype",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L236",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/intertype",
   :doc "Relation of intertype declarations.",
   :var-type "function",
   :line 236,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?intertype]),
   :name "intertypefield",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L267",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/intertypefield",
   :doc "Relation of intertype field declarations.",
   :var-type "function",
   :line 267,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?intertype]),
   :name "intertypemethod",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L252",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/intertypemethod",
   :doc "Relation of intertype method declarations.",
   :var-type "function",
   :line 252,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?method]),
   :name "method",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L308",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/method",
   :doc "Relation of methods declared within an aspect.",
   :var-type "function",
   :line 308,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:arglists ([?pointcut]),
   :name "pointcut",
   :namespace "damp.ekeko.aspectj.ajdt",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj#L220",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/8168fb9c1ad1934f1306ef406dce75201787e0f4/EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdt/pointcut",
   :doc "Relation of pointcuts.",
   :var-type "function",
   :line 220,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/ajdt.clj"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdtastnode/reifiers",
   :namespace "damp.ekeko.aspectj.ajdtastnode",
   :var-type "function",
   :arglists ([this]),
   :doc
   "Returns a map of keywords to reifier functions. The latter will return an Ekeko-specific child of the AST node.",
   :name "reifiers"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.ajdtastnode/source-string",
   :namespace "damp.ekeko.aspectj.ajdtastnode",
   :var-type "function",
   :arglists ([this]),
   :doc
   "Returns a string corresponding to the actual AspectJ source code.",
   :name "source-string"}
  {:arglists ([]),
   :name "aspectj-project-models",
   :namespace "damp.ekeko.aspectj.projectmodel",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/692dd7080858aaec2b3121f6cd50c6d963359e92/EkekoAspectJ/src/damp/ekeko/aspectj/projectmodel.clj#L10",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/692dd7080858aaec2b3121f6cd50c6d963359e92/EkekoAspectJ/src/damp/ekeko/aspectj/projectmodel.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.projectmodel/aspectj-project-models",
   :doc
   "Returns all AspectJProjectModel instances that are to be queried.\nSubset of (queried-project-models), which is itself \na subset of (all-project-models).",
   :var-type "function",
   :line 10,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/projectmodel.clj"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.soot/sootsignature",
   :namespace "damp.ekeko.aspectj.soot",
   :var-type "function",
   :arglists ([this]),
   :doc
   "Returns the soot signature for an element of the AspectJ weaver world.",
   :name "sootsignature"}
  {:file nil,
   :raw-source-url nil,
   :source-url nil,
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.soot/sootsignature-as-method",
   :namespace "damp.ekeko.aspectj.soot",
   :var-type "function",
   :arglists ([this]),
   :doc
   "Returns the soot method signature for an element of the Aspectj weaver world that gets compiled into a method.",
   :name "sootsignature-as-method"}
  {:arglists ([?advice]),
   :name "advice",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L457",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice",
   :doc "Relation of advices known to the weaver.",
   :var-type "function",
   :line 457,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?handle]),
   :name "advice-handle",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L474",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-handle",
   :doc
   "Relation between an advice and the handle for its corresponding ProgramElement.",
   :var-type "function",
   :line 474,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?kind]),
   :name "advice-kind",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L491",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-kind",
   :doc "Relation between an advice and its kind (an AdviceKind).",
   :var-type "function",
   :line 491,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?precedence]),
   :name "advice-kindprecedence",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L499",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-kindprecedence",
   :doc
   "Relation between an advice and a number representing\nthe precedence associated with its kind (see AdviceKind).\nE.g., before=1, after=2, afterThrowing=3, afterReturning=4,around=5",
   :var-type "function",
   :line 499,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?pointcut]),
   :name "advice-pointcut",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L684",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-pointcut",
   :doc
   "Relation between an Advice ?advice and its Pointcut ?pointcut.\nNote that these are different from the ResolvedPointcutDefinition instances returned\nby aspect-pointcutdefinition.",
   :var-type "function",
   :line 684,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?shadow]),
   :name "advice-shadow",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L912",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-shadow",
   :doc
   "Relation between an advice ?advice and one of its shadows, as the AspectJ IProgramElement ?shadow",
   :var-type "function",
   :line 912,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice ?location]),
   :name "advice-sourcelocation",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L483",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advice-sourcelocation",
   :doc
   "Relation between an advice and its location in the source code (an ISourceLocation).",
   :var-type "function",
   :line 483,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "adviceafter",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L519",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/adviceafter",
   :doc "Relation of after advices.",
   :var-type "function",
   :line 519,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "adviceafterreturning",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L535",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/adviceafterreturning",
   :doc "Relation of after returning advices.",
   :var-type "function",
   :line 535,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "adviceafterthrowing",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L527",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/adviceafterthrowing",
   :doc "Relation of after throwing advices.",
   :var-type "function",
   :line 527,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "advicearound",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L543",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advicearound",
   :doc "Relation of around advices.",
   :var-type "function",
   :line 543,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "advicebefore",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L511",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/advicebefore",
   :doc "Relation of before advices.",
   :var-type "function",
   :line 511,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect]),
   :name "aspect",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L283",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect",
   :doc "Relation of aspects known to the weaver.",
   :var-type "function",
   :line 283,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?advice]),
   :name "aspect-advice",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L440",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-advice",
   :doc
   "Relation between an aspect and one of the advices it declares.",
   :var-type "function",
   :line 440,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?declare]),
   :name "aspect-declare",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L744",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declare",
   :doc
   "Relation between an aspect and one of its aspect-specific declarations.",
   :var-type "function",
   :line 744,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?interface]),
   :name "aspect-declaredinterface",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L330",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredinterface",
   :doc
   "Relation between an aspect and one of the interfaces it declares to be implementing.",
   :var-type "function",
   :line 330,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?super]),
   :name "aspect-declaredsuper",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L338",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuper",
   :doc
   "Relation between an aspect and its super class (including java.lang.Object for aspects\nthat do not declare a super aspect).",
   :var-type "function",
   :line 338,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L366",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuper+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :line 366,
   :var-type "var",
   :doc
   "Relation between an aspect and one of the ancestors in its declared super hierarchy.",
   :name "aspect-declaredsuper+"}
  {:arglists ([?aspect ?super]),
   :name "aspect-declaredsuperaspect",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L347",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuperaspect",
   :doc
   "Relation between an aspect and its declared super aspect. \nExcludes non-aspect super types (e.g., the implicit java.lang.Object). ",
   :var-type "function",
   :line 347,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?ancestor]),
   :name "aspect-declaredsuperaspect+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L377",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuperaspect+",
   :doc
   "Relation between an aspect and one of the ancestor aspects in its declared super hierarchy.",
   :var-type "function",
   :line 377,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?super]),
   :name "aspect-declaredsuperclass",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L356",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuperclass",
   :doc
   "Relation between an aspect and its declared super class.\nExcludes aspect super types.",
   :var-type "function",
   :line 356,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?ancestor]),
   :name "aspect-declaredsuperclass+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L385",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-declaredsuperclass+",
   :doc
   "Relation between an aspect and one of the ancestor classes in its declared super hierarchy.",
   :var-type "function",
   :line 385,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists
   ([?dominator ?subordinate]
    [?dominator ?subordinate ?explored-subs]),
   :name "aspect-dominates-aspect",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L850",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-dominates-aspect",
   :doc
   "Precedence domination relation between an aspect and its subordinate,\nby combining explicit precedence declarations with implicit precedence relations.",
   :var-type "function",
   :line 850,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?dominator ?subordinate ?decprec]),
   :name "aspect-dominates-aspect-explicitly",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L783",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-dominates-aspect-explicitly",
   :doc
   "Relation between an aspect ?dominator that has a higher declared\nprecedence than aspect ?subordinate because of DeclarePrecedence \ndeclaration ?decprec.",
   :var-type "function",
   :line 783,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?dominator ?subordinate]),
   :name "aspect-dominates-aspect-explicitly+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L795",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-dominates-aspect-explicitly+",
   :doc "Transitive explicit dominates relationship",
   :var-type "function",
   :line 795,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?dominator ?subordinate]),
   :name "aspect-dominates-aspect-implicitly+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L809",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-dominates-aspect-implicitly+",
   :doc
   "Implicit aspect domination relationship: a subaspect dominates its superaspect.",
   :var-type "function",
   :line 809,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?intertype]),
   :name "aspect-intertype",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L567",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-intertype",
   :doc
   "Relation between an aspect and one of its intertype declarations (a ConcreteTypeMunger).",
   :var-type "function",
   :line 567,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?pointcutdefinition]),
   :name "aspect-pointcutdefinition",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L710",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-pointcutdefinition",
   :doc
   "Relation between an aspect and one of the pointcuts it declares.\nNote that these are instances of ResolvedPointcutDefinition,\nrather than the Pointcut instances within ShadowMungers (advice).\n\nLink between PointcutDefinition and Pointcut (.getPointcut) seems broken though...",
   :var-type "function",
   :line 710,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?type]),
   :name "aspect-super+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L396",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-super+",
   :doc
   "Relation between an aspect and one of its direct or indirect\nsuper types (classes, aspects as well as interfaces),\nincluding those that stem from an intertype declaration.",
   :var-type "function",
   :line 396,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?type]),
   :name "aspect-superaspect+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L407",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-superaspect+",
   :doc
   "Relation between an aspect and one of its direct or indirect\nsuper aspects, including those that stem from an intertype declaration.",
   :var-type "function",
   :line 407,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?type]),
   :name "aspect-superclass+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L426",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-superclass+",
   :doc
   "Relation between an aspect and one of its direct or indirect\nsuper classes, including those that stem from an intertype declaration.",
   :var-type "function",
   :line 426,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?aspect ?type]),
   :name "aspect-superinterface+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L417",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/aspect-superinterface+",
   :doc
   "Relation between an aspect and one of its direct or indirect\nsuper interfaces, including those that stem from an intertype declaration.",
   :var-type "function",
   :line 417,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?declare]),
   :name "declare",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L753",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/declare",
   :doc
   "Relation of all aspect-specific declarations known to the weaver.",
   :var-type "function",
   :line 753,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?declare]),
   :name "declareprecedence",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L764",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/declareprecedence",
   :doc "Relation of all precedence declarations.",
   :var-type "function",
   :line 764,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?declare ?patterns]),
   :name "declareprecedence-patterns",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L773",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/declareprecedence-patterns",
   :doc
   "Relation between a DeclarePrecedence ?declare and its TypePatternList ?patterns",
   :var-type "function",
   :line 773,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element]),
   :name "element",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L103",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element",
   :doc "Relation of elements of the ProgramElement hierarchy.",
   :var-type "function",
   :line 103,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?child-element]),
   :name "element-child",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L86",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-child",
   :doc
   "Relation of roots of the weaver world ProgramElement hierarchy.",
   :var-type "function",
   :line 86,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?child]),
   :name "element-child+",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L94",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-child+",
   :doc
   "Relation between a ProgramElement ?element and one of its descendants ?child.",
   :var-type "function",
   :line 94,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?typedeclaration]),
   :name "element-enclosingtypedeclaration-element",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L193",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-enclosingtypedeclaration-element",
   :doc
   "Relation between a ProgramElement and its enclosing aspect (if any).",
   :var-type "function",
   :line 193,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?handle]),
   :name "element-handle",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L142",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-handle",
   :doc "Relation between a ProgramElement and its handle.",
   :var-type "function",
   :line 142,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?kind]),
   :name "element-kind",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L125",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-kind",
   :doc
   "Relation between a ProgramElement ?element and its kind ?kind.",
   :var-type "function",
   :line 125,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?parent]),
   :name "element-parent",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L116",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-parent",
   :doc
   "Relation between a ProgramElement and its parent ProgramElement.",
   :var-type "function",
   :line 116,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?signature]),
   :name "element-signature",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L134",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-signature",
   :doc "Relation between a ProgramElement and its signature string.",
   :var-type "function",
   :line 134,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?handle]),
   :name "element-sourcelocation",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L150",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-sourcelocation",
   :doc "Relation between a ProgramElement and its SourceLocation.",
   :var-type "function",
   :line 150,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element ?type]),
   :name "element-type",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L272",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/element-type",
   :doc
   "Relation between a ProgramElement of kind Aspect/Enum/Class\nand the ResolvedType instance from the weaver world it corresponds to.",
   :var-type "function",
   :line 272,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype ?member]),
   :name "intertype-element",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L636",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertype-element",
   :doc
   "Relation between an intertype declaration and its resulting member element (a ProgramElement) \nin the program element hierarchy.\n\nThese are of the same type as the shadows we return for an advice, thus allowing checking\nwhether a shadow stems from an intertype declaration.\n\nPrefer to use the above intertype-member-type instead, as it does not escape the weaver world.",
   :var-type "function",
   :line 636,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype ?kind]),
   :name "intertype-kind",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L589",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertype-kind",
   :doc "Relation between an intertype declaration and its kind.",
   :var-type "function",
   :line 589,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype ?member ?type]),
   :name "intertype-member-type",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L622",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertype-member-type",
   :doc
   "Relation between an intertype declaration, the field/method/constructor member it declares\n(a ResolvedMemberImpl), and the type to which this member is added (a ResolvedType). ",
   :var-type "function",
   :line 622,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype]),
   :name "intertypeconstructor",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L613",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertypeconstructor",
   :doc "Relation of constructor intertype declarations.",
   :var-type "function",
   :line 613,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype]),
   :name "intertypefield",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L597",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertypefield",
   :doc "Relation of field intertype declarations.",
   :var-type "function",
   :line 597,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?intertype]),
   :name "intertypemethod",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L605",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/intertypemethod",
   :doc "Relation of method intertype declarations.",
   :var-type "function",
   :line 605,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?advice]),
   :name "pointcut",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L695",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/pointcut",
   :doc
   "Relation of all pointcuts known to the AspectJ weaver.\nNote that these are not pointcut definitions.",
   :var-type "function",
   :line 695,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?pointcut]),
   :name "pointcutdefinition",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L723",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/pointcutdefinition",
   :doc
   "Relation of pointcuts known to the weaver.\nNote: these are instances of Pointcut rather than ResolvedPointcutDefinition.",
   :var-type "function",
   :line 723,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?element]),
   :name "root",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L76",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/root",
   :doc "Relation of ProgramElement hierarchy roots.",
   :var-type "function",
   :line 76,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists
   ([?sourcelocation ?filename ?startline ?endline ?column ?offset]),
   :name "sourcelocation-filename-startline-endline-column-offset",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L667",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/sourcelocation-filename-startline-endline-column-offset",
   :doc
   "Non-relational. Unifies ?filename with the name of the file for the given ?sourcelocation, \n?startline with the line at which it starts, ?endline with the line at which it ends, \n?column with the column at which it starts, and ?offset with its offset.",
   :var-type "function",
   :line 667,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?resolvedtype]),
   :name "type",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L251",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/type",
   :doc
   "Relation of non-expandable (i.e., project-defined) types known to the AspectJ weaver.",
   :var-type "function",
   :line 251,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?world ?model ?hierarchy]),
   :name "weaverworld-model-hierarchy",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L63",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/weaverworld-model-hierarchy",
   :doc
   "Relation between an AspectJ weaver world and its ProgramElement hierarchy.",
   :var-type "function",
   :line 63,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?world ?xcut]),
   :name "weaverworld-xcut",
   :namespace "damp.ekeko.aspectj.weaverworld",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj#L872",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/966ee14a2d70a222da46441f40aeefd3db12d7b8/EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.weaverworld/weaverworld-xcut",
   :doc
   "Relation between an AspectJ weaverworld and its corresponding XCut model (AJProjectModelFacade).\nRelations for the latter are available in the damp.ekeko.aspectj.xcut namespace.",
   :var-type "function",
   :line 872,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/weaverworld.clj"}
  {:arglists ([?program-element ?signature-string]),
   :name "pe-signature",
   :namespace "damp.ekeko.aspectj.xcut",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj#L95",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.xcut/pe-signature",
   :doc
   "Non-relational. Unifies ?signature-string with the signature \nof the given AspectJ ProgramElement ?program-element",
   :var-type "function",
   :line 95,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj"}
  {:arglists ([?xcut ?handle ?j-element]),
   :name "xcut-handle-je",
   :namespace "damp.ekeko.aspectj.xcut",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj#L69",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.xcut/xcut-handle-je",
   :doc
   "Non-relational. Unifies ?j-element with the JDT IJavaElement that corresponds to the \ngiven ?handle of an AspectJ ProgramElement in the given XCut model ?xcut (an AJProjectModelFacade).",
   :var-type "function",
   :line 69,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj"}
  {:arglists ([?xcut ?handle ?p-element]),
   :name "xcut-handle-pe",
   :namespace "damp.ekeko.aspectj.xcut",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj#L78",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.xcut/xcut-handle-pe",
   :doc
   "Non-relational. Unifies ?p-element with the AspectJ ProgramElement that corresponds to the \ngiven ?handle in the given XCut model ?xcut (an AJProjectModelFacade).",
   :var-type "function",
   :line 78,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj"}
  {:arglists ([?xcut ?jdt-element ?aj-element]),
   :name "xcut-je-pe",
   :namespace "damp.ekeko.aspectj.xcut",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj#L87",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.xcut/xcut-je-pe",
   :doc
   "Non-relational. Unifies ?aj-element with the AspectJ ProgramElement that corresponds to the \ngiven JDT IJavaElement ?jdt-element in the given XCut model ?xcut (an AJProjectModelFacade).",
   :var-type "function",
   :line 87,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj"}
  {:arglists ([?xcut ?relation-type ?relation]),
   :name "xcut-relationtype-relation",
   :namespace "damp.ekeko.aspectj.xcut",
   :source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/blob/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj#L58",
   :raw-source-url
   "http://github.com/cderoove/damp.ekeko.aspectj/raw/f18b8b9683c5555aee855bb8fa32ab7fc7b98da4/EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj",
   :wiki-url
   "http://cderoove.github.com/damp.ekeko.aspectj//damp.ekeko.aspectj-api.html#damp.ekeko.aspectj.xcut/xcut-relationtype-relation",
   :doc
   "Relation between an XCut model ?xcut (an AJProjectModelFacade) and one of its relations ?relation, of type ?relation-type.",
   :var-type "function",
   :line 58,
   :file "EkekoAspectJ/src/damp/ekeko/aspectj/xcut.clj"})}
