package cl.pleiad.ajlmp.testPointcuts;

public aspect SecondAspect extends FirstAspect {

	pointcut abstractpc1() : execution(* BaseClass.*2(..));
	
}
