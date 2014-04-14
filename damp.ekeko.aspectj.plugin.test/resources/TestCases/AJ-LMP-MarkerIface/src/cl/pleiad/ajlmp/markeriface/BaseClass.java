package cl.pleiad.ajlmp.markeriface;

public class BaseClass {

	public static void main(String[] args) {
		BaseClass bc = new BaseClass();
		BaseClass2 bc2 = new BaseClass2();
		BaseClass3 bc3 = new BaseClass3();
		bc.baseMethod2(null);
		bc2.baseMethod2dot1();
		bc3.baseMethod3dot1();
	}

	public void baseMethod1(){
		System.out.println("BaseClass-M1");
	}
	
	public void baseMethod2(Object param){
		System.out.println("BaseClass-M2-1");
		baseMethod1();
		System.out.println("BaseClass-M2-2");
	}
	
	public void baseMethod3(){
		System.out.println("BaseClass-M3");
	}
	
}
