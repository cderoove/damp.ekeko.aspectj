package cl.pleiad.ajlmp.testPrecedence;

public aspect SecondAspect {
	declare precedence: FirstAspect, SecondAspect;
	
	before() : call(* BaseClass.*1()){
		System.out.println("SecondAspect-BeforeCall");
	}
}
