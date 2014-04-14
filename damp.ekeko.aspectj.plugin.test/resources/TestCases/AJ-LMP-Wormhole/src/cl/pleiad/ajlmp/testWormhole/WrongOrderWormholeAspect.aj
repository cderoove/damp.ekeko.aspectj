package cl.pleiad.ajlmp.testWormhole;

public aspect WrongOrderWormholeAspect {
	pointcut entry(int save): execution(* BaseClass.*1(int)) && args(save);
	
	pointcut exit() : execution(* BaseClass.*2());
	
	int store;
	
	before() : exit (){
		this.store = 42;
	}
	
	before(int save ) : entry(save) {
		System.out.println("Not saved value is: "+store);
	}

}
