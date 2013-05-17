# About GASR

GASR ( *General-purpose Aspectual Source code Reasoner* ) is a tool for answering user-specified questions about the structure as well as the behavior of an aspect-oriented program. Examples range from " _which pointcut definitions are overridden in a subtype?_ " over " _which pointcuts have a join point shadow in an advice?_ " to " _can these advices be executed consecutively?_ ". Such questions have to be specified as a logic query of which the conditions quantify over the program’s source code. 

## Documentation

See our [wiki](https://github.com/cderoove/damp.ekeko.aspectj/wiki) for some [example program queries](https://github.com/cderoove/damp.ekeko.aspectj/wiki/Example-Queries). 

See the [API documentation](http://cderoove.github.com/damp.ekeko.aspectj/) for an overview of the predicates than can be used within a program query.

Slideshare hosts an [earlier presentation on GASR](http://www.slideshare.net/oniroi/detecting-aspectspecific-code-smells-using-ekeko-for-aspectj). 

## Experiments

The experimental results for coding aspect assumptions are in a [markdown-formatted file](https://github.com/cderoove/damp.ekeko.aspectj/blob/master/experimental-results.md).

## Dependencies

GASR owes its query language to the [core.logic](https://github.com/clojure/core.logic) port to [Clojure](http://clojure.org/) of [Kanren](http://kanren.sourceforge.net/), and its IDE integration to the [Ekeko](https://github.com/cderoove/damp.ekeko/tree/master/EkekoPlugin) Eclipse plugin. The latter enables launching and scheduling program queries, as well as inspecting the solutions to a query. 

## License  

Copyright © 2012-2013 GASR contributors: [Coen De Roover](http://soft.vub.ac.be/~cderoove/), [Johan Fabry](http://www.dcc.uchile.cl/~jfabry)

Distributed under the Eclipse Public License (EPL version 1.0). See ``LICENSE.html``.