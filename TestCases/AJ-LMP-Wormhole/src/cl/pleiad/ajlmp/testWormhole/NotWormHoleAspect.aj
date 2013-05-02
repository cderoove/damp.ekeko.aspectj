package cl.pleiad.ajlmp.testWormhole;

public aspect NotWormHoleAspect {
	
	pointcut entry(int save): execution(* BaseClass.*1(int)) && args(save);
	
	pointcut exit() : execution(* BaseClass.*2());
	
	int nostore;
	
	before(int savedarg) : entry (savedarg){
		this.nostore = savedarg;
	}
	
	before() : exit() {
		System.out.println("Not reading the store!");
	}

}
