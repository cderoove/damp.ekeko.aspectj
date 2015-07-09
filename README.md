# About GASR

GASR ( *General-purpose Aspectual Source code Reasoner* ) is a tool for answering user-specified questions about the structure as well as the behavior of an aspect-oriented program. Examples range from " _which pointcut definitions are overridden in a subtype?_ " over " _which pointcuts have a join point shadow in an advice?_ " to " _can these advices be executed consecutively?_ ". Such questions have to be specified as a logic query of which the conditions quantify over the program’s source code. 

GASR owes its query language to the [core.logic](https://github.com/clojure/core.logic) port to [Clojure](http://clojure.org/) of [Kanren](http://kanren.sourceforge.net/), and its IDE integration to the [Ekeko](https://github.com/cderoove/damp.ekeko/tree/master/EkekoPlugin) Eclipse plugin. The latter enables launching and scheduling program queries, as well as inspecting the solutions to a query. 

## Documentation

Our [SCAM13](http://www.ieee-scam.org/2013/) paper titled [Aspectual Source Code Analysis with GASR](http://soft.vub.ac.be/Publications/2013/vub-soft-tr-13-06.pdf) introduces GASR. The paper's experimental data  can be found in a [markdown-formatted file](https://github.com/cderoove/damp.ekeko.aspectj/blob/master/experimental-results.md).

See the GASR [wiki](https://github.com/cderoove/damp.ekeko.aspectj/wiki) for some [example program queries](https://github.com/cderoove/damp.ekeko.aspectj/wiki/Example-Queries). 

See the [API documentation](http://cderoove.github.com/damp.ekeko.aspectj/) for an overview of the predicates than can be used within a program query.

Slideshare hosts an [earlier presentation on GASR](http://www.slideshare.net/oniroi/detecting-aspectspecific-code-smells-using-ekeko-for-aspectj). 


## Installation

Ensure that the dependencies [AspectJ Development Tools](http://www.eclipse.org/ajdt/) and and [Counterclockwise](https://github.com/laurentpetit/ccw/wiki/GoogleCodeHome) are installed.  (See [Installing New Software](http://help.eclipse.org/luna/topic/org.eclipse.platform.doc.user/tasks/tasks-124.htm) for help on installing Eclipse plugins from an update site.)

The plugin can be built from the [EkekoAspectJ](https://github.com/cderoove/damp.ekeko.aspectj/tree/master/EkekoAspectJ) Eclipse project in this repository.

GASR has been tested against [Eclipse Luna (4.4)](http://www.eclipse.org/luna/) and [Eclipse Mars (4.5)](http://www.eclipse.org/mars/).

> **Note:** for annotations to be visible to GASR, their type definitions need to be on the AspectJ inpath (Project context menu item "AspectJ Tools" -> "Configure AspectJ Build path ..." )

## License  

Copyright © 2012-2014 GASR contributors: [Coen De Roover](http://soft.vub.ac.be/~cderoove/), [Johan Fabry](http://www.dcc.uchile.cl/~jfabry), [Carlos Noguera](https://soft.vub.ac.be/soft/members/cnoguera)

Distributed under the Eclipse Public License (EPL version 1.0). See ``LICENSE.html``.
