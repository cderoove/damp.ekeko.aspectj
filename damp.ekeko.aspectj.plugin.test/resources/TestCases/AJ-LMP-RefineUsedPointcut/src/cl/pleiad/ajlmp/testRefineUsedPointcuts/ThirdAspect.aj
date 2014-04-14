package cl.pleiad.ajlmp.testRefineUsedPointcuts;

public aspect ThirdAspect {
	//Impossible to do FirstAspect.pc1() here, AJDT error.
	before() : SecondAspect.pc1() {
		System.out.println("SecondAspect-Before-1");
	}
}
