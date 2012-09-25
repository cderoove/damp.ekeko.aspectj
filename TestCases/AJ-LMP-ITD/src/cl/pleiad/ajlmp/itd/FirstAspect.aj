package cl.pleiad.ajlmp.itd;

public aspect FirstAspect {
	before(): execution(* BaseClass.*1(..)) {
		BaseClass base = ((BaseClass)thisJoinPoint.getTarget());
		System.out.println("FirstAspect-BeforeCall-1");
		base.itdA();
	}

	public void BaseClass.itdA(){
		System.out.println("FirstAspect-ITD");
	}
	
}
