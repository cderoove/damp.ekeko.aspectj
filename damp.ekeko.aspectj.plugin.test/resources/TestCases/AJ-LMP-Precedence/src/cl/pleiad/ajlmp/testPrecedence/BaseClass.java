package cl.pleiad.ajlmp.testPrecedence;

public class BaseClass {

	public static void main(String[] args){
		BaseClass bc = new BaseClass();
		bc.baseMethod2(null);
	}
	
	public void baseMethod1(){
		System.out.println("BaseClass-M1");
	}
	
	public void baseMethod2(Object param){
		System.out.println("BaseClass-M2-1");
		baseMethod1();
		System.out.println("BaseClass-M2-1");
	}
}
