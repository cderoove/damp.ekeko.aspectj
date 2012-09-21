package cl.pleiad.ajlmp.testPrecedence;

public aspect FirstAspect {
	
	before() : execution(* BaseClass.*1()){
		System.out.println("FirstAspect-BeforeExec");
	}
	
	before(): call(* BaseClass.*1()){
		System.out.println("FirstAspect-BeforeCall");
	}
	
}
