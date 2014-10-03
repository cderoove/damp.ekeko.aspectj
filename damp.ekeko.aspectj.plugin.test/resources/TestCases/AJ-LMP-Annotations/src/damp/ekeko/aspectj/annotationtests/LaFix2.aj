package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

/*
 * Fixture 2 for labels
 */
@Label({"Label2a","Label2b"})
public aspect LaFix2 {

	@Label({"MethLabel2a","MethLabel2b"})
	public void multiLabeledMethod(){}
	
	@Label("MethLabel2")
	public void labeledMethod(){}
	
	@Label("ConsLabel2")
	public LaFix2(){}
	
	//Not meant to match anything really
	pointcut fooCall() : call(* Foo*(..) );

	//Not meant to match anything really
	@Label("PCLabel2")
	pointcut barCall() : call(* Bar*(..) );

	@Label("AdvLabel2")
	before(): fooCall() {}
	
	before(): barCall() {}
	
	@Label({"AdvLabel2a","AdvLabel2b"})
	before(): fooCall() || barCall() {}
}
