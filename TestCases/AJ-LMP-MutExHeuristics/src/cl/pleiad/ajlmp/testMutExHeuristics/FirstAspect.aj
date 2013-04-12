package cl.pleiad.ajlmp.testMutExHeuristics;

public abstract aspect FirstAspect {
	pointcut pc1():execution(* BaseClass.*1(..));
}
