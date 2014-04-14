package cl.pleiad.ajlmp.testInclusion;

public aspect FirstAspect {
	before(): execution(* BaseClass.*2(..)) {
		BaseClass base = ((BaseClass)thisJoinPoint.getTarget());
		System.out.println("FirstAspect-BeforeCall-1");
		base.baseMethod1();
		System.out.println("FirstAspect-BeforeCall-2");
		base.itd();
	}

	public void BaseClass.itd(){
		baseMethod3();
	}
}
