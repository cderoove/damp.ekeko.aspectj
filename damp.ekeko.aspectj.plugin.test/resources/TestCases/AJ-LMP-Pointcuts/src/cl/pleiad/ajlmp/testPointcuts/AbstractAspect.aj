package cl.pleiad.ajlmp.testPointcuts;

public abstract aspect AbstractAspect {
	abstract pointcut abstractpc1();
	
	abstract pointcut abstractpc2();
	
	pointcut concretepc1() : execution(* BaseClass.*2(..));
	
	 void around(): abstractpc1(){
		System.out.println("AbstractAspect-Around1-1");
		proceed();
		System.out.println("AbstractAspect-Around1-2");
	}
	 
	 void around(): abstractpc2(){
		System.out.println("AbstractAspect-Around2-1");
		proceed();
		System.out.println("AbstractAspect-Around2-2");
	}

	 void around(): concretepc1(){
		System.out.println("AbstractAspect-Around3-1");
		proceed();
		System.out.println("AbstractAspect-Around3-2");
	}
	 
}
