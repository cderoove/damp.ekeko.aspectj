package cl.pleiad.ajlmp.testAdviceOrdering;

public class BaseClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseClass bc = new BaseClass();
		bc.baseMethod1();
		System.out.println("----------");
		bc.baseMethod2();
		System.out.println("----------");
		System.out.println("----------");
		bc.baseMethod3();
	}
	
	public void baseMethod1(){
		System.out.println("Base Method 1");
	}

	public void baseMethod2(){
		System.out.println("Base Method 2");
	}

	public void baseMethod3(){
		System.out.println("Base Method 2");
	}
}
