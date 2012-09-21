package cl.pleiad.ajlmp.testPrecedence;

public aspect SecondAspect {
	declare precedence: FirstAspect, SecondAspect;
	
	before() : execution(* BaseClass.*1()){
		System.out.println("SecondAspect-BeforeExec");
	}
}
