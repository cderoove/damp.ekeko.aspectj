package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.ExcludesPrevious;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

/*
 * Fixture for @RequiresPrevious and @ExcludesPrevious
 */

public aspect REPFix2 {

	
	@RequiresPrevious({"Label2Ra", "Label2Rb"})
	@ExcludesPrevious({"Label2Ea", "Label2Eb"})
	public REPFix2(){}
	
	@RequiresPrevious({"Label2Rc","Label2Rd"})
	public void methoda(){}
	
	@ExcludesPrevious({"Label2Ec","Label2Ed"})
	public void methodb(){}
	
	//Not meant to match anything really
	pointcut fooCall() : call(* Foo*(..) );
	
	@RequiresPrevious("Label2Re")
	before(): fooCall() {}
	
	@ExcludesPrevious("Label2Ee")
	after(): fooCall() {}
	
	@RequiresPrevious({"Label2Rf","Label2Rg"})
	@ExcludesPrevious({"Label2Ef","Label2Eg"})
	void around(): fooCall() {}
}
