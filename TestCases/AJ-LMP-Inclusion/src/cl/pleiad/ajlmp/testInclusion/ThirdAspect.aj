package cl.pleiad.ajlmp.testInclusion;

public aspect ThirdAspect {
	before() : call(* BaseClass.*3()){
		System.out.println("ThirdAspect-BeforeCall");
	}
}
