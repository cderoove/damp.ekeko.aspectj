package cl.pleiad.ajlmp.testPointcuts;

public abstract aspect FirstAspect extends AbstractAspect {

	pointcut abstractpc1() : execution(* BaseClass.*1(..));
	
	pointcut abstractpc2() : execution(* BaseClass.*2(..));

}
