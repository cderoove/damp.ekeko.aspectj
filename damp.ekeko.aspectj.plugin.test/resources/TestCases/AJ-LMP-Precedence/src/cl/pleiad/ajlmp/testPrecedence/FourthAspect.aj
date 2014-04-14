package cl.pleiad.ajlmp.testPrecedence;

public abstract aspect FourthAspect extends ThirdAspect {

	declare precedence: ThirdAspect, FourthAspect;
	
	public pointcut pc() : execution(* BaseClass.*1());
	
	before() : pc(){
		System.out.println("FourthAspect-BeforePC");
	}

}
