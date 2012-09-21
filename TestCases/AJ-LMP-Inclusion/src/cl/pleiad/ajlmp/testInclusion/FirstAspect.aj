package cl.pleiad.ajlmp.testInclusion;

public aspect FirstAspect {
	before(): execution(* BaseClass.*2(..)) {
		System.out.println("FirstAspect-BeforeCall-1");
		((BaseClass)thisJoinPoint.getTarget()).baseMethod1();
		System.out.println("FirstAspect-BeforeCall-2");		
	}

}
