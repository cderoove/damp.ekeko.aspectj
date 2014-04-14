package cl.pleiad.ajlmp.testPointcuts;

public class BaseClass {

	public static void main(String[] args) {
		BaseClass bc = new BaseClass();
		bc.baseMethod1();
		bc.baseMethod2();
	}

	public void baseMethod1(){
		System.out.println("BaseClass-M1");
	}
	
	public void baseMethod2(){
		System.out.println("BaseClass-M2");
	}

}
