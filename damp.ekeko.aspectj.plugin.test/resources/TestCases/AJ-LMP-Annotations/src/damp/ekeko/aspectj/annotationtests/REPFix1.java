package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.ExcludesPrevious;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

/*
 * Fixture for @RequiresPrevious and @ExcludesPrevious
 */


public class REPFix1 {

	@RequiresPrevious("Label1Ra")
	public REPFix1(){}
	
	@ExcludesPrevious("Label1Ea")
	public REPFix1(int second){}
	
	public REPFix1(float third){}
	
	@RequiresPrevious("Label1Rb")
	public void methoda(){}
	
	@ExcludesPrevious("Label1Eb")
	public void methodb(){}
	
	public void methodc(){}
}
