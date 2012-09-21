package cl.pleiad.ajlmp.testPrecedence;

public abstract aspect ThirdAspect {
	public abstract pointcut pc();
	
	before() : pc(){
		System.out.println("ThirdAspect-BeforePC");
	}
}
