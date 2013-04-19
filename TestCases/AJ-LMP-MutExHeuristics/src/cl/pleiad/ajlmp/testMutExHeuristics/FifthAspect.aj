package cl.pleiad.ajlmp.testMutExHeuristics;

public aspect FifthAspect extends FirstAspect {

	before() : pc1() {
		System.out.println("FifthAspect-Before-1");
	}
}
