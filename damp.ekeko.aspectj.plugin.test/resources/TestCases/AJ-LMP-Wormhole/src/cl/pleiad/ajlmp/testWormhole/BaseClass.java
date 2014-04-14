package cl.pleiad.ajlmp.testWormhole;

public class BaseClass implements Runnable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseClass bc = new BaseClass(42);
		bc.run();
		new Thread(new BaseClass(128)).start();
		new Thread(new BaseClass(256)).start();
	}
	
	int theArgument;
	
	public BaseClass(int arg){
		this.theArgument = arg;
	}

	public void run(){
		this.baseMethod1(theArgument);
		for(int i=0; i<10; i++)
			System.out.print('+');
		System.out.println();
		this.baseMethod2();
	}
	
	public void baseMethod1(int arg){
		System.out.println("Argument is: "+arg);
	}
	
	public void baseMethod2(){
		System.out.println("I have no argument with you!");
	}
}
