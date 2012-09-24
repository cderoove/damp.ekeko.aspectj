package cl.pleiad.ajlmp.testAdviceOrdering;

public aspect SecondAspect {
	
	declare precedence: FirstAspect,SecondAspect;

	pointcut pc3() : call(* BaseClass.*3());
	
	void around(): pc3(){
		System.out.println("2Ar-1");
		proceed();
		System.out.println("2Ar-2"); 
	}
	
	before(): pc3(){
		System.out.println("2Be");
	}
	
	after(): pc3(){
		System.out.println("2Af");
	}
	
}
