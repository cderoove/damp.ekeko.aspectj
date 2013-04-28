package cl.pleiad.ajlmp.testRefineUsedPointcuts;

public abstract aspect FirstAspect {

	public abstract pointcut pc1();
	
	pointcut pc2() : execution(* BaseClass.*2(..));
	
	before() : pc1() {
		System.out.println("FirstAspect-Before-1");
	}
	
	before() : pc2() {
		System.out.println("FirstAspect-Before-2");
	}
}
