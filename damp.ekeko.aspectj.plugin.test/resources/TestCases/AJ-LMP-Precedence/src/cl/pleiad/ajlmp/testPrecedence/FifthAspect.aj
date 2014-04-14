package cl.pleiad.ajlmp.testPrecedence;

public aspect FifthAspect {
	declare precedence: SecondAspect, FifthAspect;

	before() : execution(* BaseClass.*1()){
		System.out.println("FifthAspect-BeforeExec");
	}
	
	before(): call(* BaseClass.*1()){
		System.out.println("FifthAspect-BeforeCall");
	}
}
