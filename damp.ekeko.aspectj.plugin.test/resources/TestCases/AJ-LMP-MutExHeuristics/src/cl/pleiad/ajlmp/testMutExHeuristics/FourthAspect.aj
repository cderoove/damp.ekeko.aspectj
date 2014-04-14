package cl.pleiad.ajlmp.testMutExHeuristics;

public aspect FourthAspect extends FirstAspect {

	before() : pc1() {
		System.out.println("FourthAspect-Before-1");
	}
}
