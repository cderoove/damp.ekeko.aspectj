package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.Label;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

//A fixture for control flow tests
public class EntryPoint {

	public static void main(String[] args) {
		new EntryPoint().go(args[1]);
	}

	private void go(String arg) {
		this.compute(this.think());
		if(arg == "exec")
			this.execute();
		else
			this.simulate();
		this.cleanup();
	}

	@Label("thinking")
	private String think(){
		return "zzzzz";
	}
	
	@Label("computing")
	private int compute(String value){
		return 0;
	}
	
	@Label("executing")
	@RequiresPrevious("computing")
	private void execute(){}
	
	@Label("simulating")
	private void simulate(){} 
	
	@Label("cleaning")
	private void cleanup(){}
}
