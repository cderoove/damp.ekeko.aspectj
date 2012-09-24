package cl.pleiad.ajlmp.testWormhole;

public aspect WormholeAspect {

	pointcut entry(int save): execution(* BaseClass.*1(int)) && args(save);
	
	pointcut exit() : execution(* BaseClass.*2());
	
	int store;
	
	before(int savedarg) : entry (savedarg){
		this.store = savedarg;
	}
	
	before() : exit() {
		System.out.println("Wormholed value is: "+store);
	}
}
