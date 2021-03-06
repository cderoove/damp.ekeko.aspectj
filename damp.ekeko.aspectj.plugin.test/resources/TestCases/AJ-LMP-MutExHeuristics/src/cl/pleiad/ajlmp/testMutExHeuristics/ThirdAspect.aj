package cl.pleiad.ajlmp.testMutExHeuristics;

public aspect ThirdAspect extends FirstAspect {
	
	pointcut pc2():call(* BaseClass.*2(..));
	
	pointcut pcthree():call(* Base*.*2(..));
	
	before() : pc1() {
		System.out.println("ThirdAspect-Before-1");
	}
	
	before() : pc2() {
		System.out.println("ThirdAspect-Before-2");
	}

	/* Not needed for now.
	before() : pcthree() {
		System.out.println("ThirdAspect-Before-three");
	}
	*/
}
