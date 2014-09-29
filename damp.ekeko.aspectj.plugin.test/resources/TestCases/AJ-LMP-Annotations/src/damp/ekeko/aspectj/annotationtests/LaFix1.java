package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.Label;

/*
 * Fixture 1 for labels
 */
@Label("Label1")
public class LaFix1 {
	
	@Label("MethLabel")
	public void labeledMethod(){}
	
	public void unlabeledMethod(){}
	
	@Label("ConsLabel")
	public LaFix1(){}
	
	public LaFix1(int nolabel){}
	
	@Label("ConsLabel1a")
//	@Label("ConsLabel1b")
	public LaFix1(String multi){}
}
