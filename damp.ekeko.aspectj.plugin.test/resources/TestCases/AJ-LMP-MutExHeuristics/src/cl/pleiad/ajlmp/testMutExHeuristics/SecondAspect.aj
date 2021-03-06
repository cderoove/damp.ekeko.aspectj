package cl.pleiad.ajlmp.testMutExHeuristics;

public aspect SecondAspect extends FirstAspect {
	
	pointcut pc2():call(* BaseClass.*1(..));

	pointcut pc3():call(* *Class.*2(..));
	
	before() : pc1() {
		System.out.println("SecondAspect-Before-1");
	}
	
	before() : pc2() {
		System.out.println("SecondAspect-Before-2");
	}
	
	/* not needed for now
	before() : pc3() {
		System.out.println("SecondAspect-Before-3");
	}
	*/
}
