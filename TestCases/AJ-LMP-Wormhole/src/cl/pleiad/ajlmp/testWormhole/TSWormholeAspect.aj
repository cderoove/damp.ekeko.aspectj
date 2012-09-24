package cl.pleiad.ajlmp.testWormhole;

public aspect TSWormholeAspect percflow(execution (* BaseClass.run())) {

		pointcut entry(int save): execution(* BaseClass.*1(int)) && args(save);
		
		pointcut exit() : execution(* BaseClass.*2());
		
		int store;
		
		before(int savedarg) : entry (savedarg){
			System.out.println("Saving " + savedarg + " in " + this.hashCode());
			this.store = savedarg;
		}
		
		before() : exit() {
			System.out.println("TS wormholed value is: "+store+" in " + this.hashCode());
		}
}
