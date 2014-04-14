package cl.pleiad.ajlmp.testReentrancy;

public aspect InfiniteLoop {
	before(): call(* *(..)) {
	     System.out.println("before"); }
}
