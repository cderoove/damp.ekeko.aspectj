package cl.pleiad.ajlmp.markeriface;

public aspect SecondAspect {
	
	before() : call (* Base*.*()){
		System.out.println("SecondAspect-BeforeCall");
	}
}
