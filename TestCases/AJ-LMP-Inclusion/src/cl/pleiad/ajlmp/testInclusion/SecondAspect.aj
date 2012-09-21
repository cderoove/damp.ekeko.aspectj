package cl.pleiad.ajlmp.testInclusion;

public aspect SecondAspect {
	before() : call(* BaseClass.*1()){
		System.out.println("SecondAspect-BeforeCall");
	}
}
