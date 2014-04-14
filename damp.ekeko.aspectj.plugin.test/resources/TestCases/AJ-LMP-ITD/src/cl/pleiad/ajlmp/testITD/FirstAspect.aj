package cl.pleiad.ajlmp.testITD;

public aspect FirstAspect {
	before(): execution(* BaseClass.*1(..)) {
		BaseClass base = ((BaseClass)thisJoinPoint.getTarget());
		System.out.println("FirstAspect-BeforeCall-1");
		base.itdA();
	}

	public void BaseClass.itdA(){
		System.out.println("FirstAspect-ITD");
	}
	
	void around(BaseClass base): execution(* Base*.*1(..)) && target(base){
		System.out.println("FirstAspect-BeforeCall-1");
		base.itdC();
	}
	public void BaseClass.itdC(){
		System.out.println("FirstAspect-ITD2");
	}
}
