package cl.pleiad.ajlmp.testAdviceOrdering;

public aspect FirstAspect {

	public pointcut pc1() : call(* BaseClass.*1());
	
	void around(): pc1(){
		System.out.println("Ar-1");
		proceed();
		System.out.println("Ar-2"); 
	}
	
	before(): pc1(){
		System.out.println("Be");
	}
	
	after(): pc1(){
		System.out.println("Af");
	}
	
	public pointcut pc2() : call(* BaseClass.*2());
	
	before(): pc2(){
		System.out.println("Be");
	}
	
	void around(): pc2(){
		System.out.println("Ar-1");
		proceed();
		System.out.println("Ar-2"); 
	}
	
	after(): pc2(){
		System.out.println("Af");
	}
	
	pointcut pc3() : call(* BaseClass.*3());
	
	void around(): pc3(){
		System.out.println("1Ar-1");
		proceed();
		System.out.println("1Ar-2"); 
	}
	
	before(): pc3(){
		System.out.println("1Be");
	}
	
	after(): pc3(){
		System.out.println("1Af");
	}
	
}
