package cl.pleiad.ajlmp.testRefineUsedPointcuts;

public aspect SecondAspect extends FirstAspect {

	pointcut pc1():execution(* BaseClass.*1(..));
	
	public SecondAspect(){}
	
	before() : pc1() {
		System.out.println("SecondAspect-Before-1");
	}

	before() : pc2() {
		System.out.println("SecondAspect-Before-2");
	}
}
