package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.ExcludesPrevious;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

/*
 * Fixture for @RequiresPrevious and @ExcludesPrevious
 */

public aspect REPFix2 {

	
	@RequiresPrevious(label = {"Label2Ra", "Label2Rb"})
	@ExcludesPrevious(label = {"Label2Ea", "Label2Eb"})
	public REPFix2(){}
	
	@RequiresPrevious(label = {"Label2Rc","Label2Rd"})
	public void methoda(){}
	
	@ExcludesPrevious(label = {"Label2Ec","Label2Ed"})
	public void methodb(){}
	
	//Not meant to match anything really
	pointcut fooCall() : call(* Foo*(..) );
	
	@RequiresPrevious(label = "Label2Re")
	before(): fooCall() {}
	
	@ExcludesPrevious(label = "Label2Ee")
	after(): fooCall() {}
	
	@RequiresPrevious(label = {"Label2Rf","Label2Rg"})
	@ExcludesPrevious(label = {"Label2Ef","Label2Eg"})
	void around(): fooCall() {}
}
