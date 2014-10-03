package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.Requires;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

//OBSOLETE

//@Requires(aspect="HelloAspect", label="Foobar")
//@Requires(label = "arg")
public class Hello {

	//@RequiresPrevious(signature="onMethod")
	void b() {
		System.out.println("In b"); 
	}
}
