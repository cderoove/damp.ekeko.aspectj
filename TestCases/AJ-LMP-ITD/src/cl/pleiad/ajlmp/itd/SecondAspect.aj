package cl.pleiad.ajlmp.itd;

public aspect SecondAspect {
	after(): execution(* BaseClass.*1(..)) {
		System.out.println("SecondAspect-AfterCall");
	}

	public void BaseClass.itdB(){
		System.out.println("SecondAspect-DeadITD");
	}
}
