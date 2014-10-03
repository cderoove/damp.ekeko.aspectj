package damp.ekeko.aspectj.annotationtests;

import damp.ekeko.aspectj.annotations.*;

// OBSOLETE

//@Requires(aspect="HelloAspect")
//@Requires(label ="asdf")
//@OneOf(aspects={"HelloAspect", "HelloAspect"},labels={"foobar"})
public aspect HelloAspect percflow(methodCall() ) {
	private int callCount = 0; 
	
	private static int totalCount = 0;
	
	public int getCount() { return callCount;
	}

	//@RequiresPrevious(signature="onAspectMethod") 
	public int getTotalCount() { return totalCount;
	}
	
	public static void main(String[] arguments ) {
		Hello example = new Hello();
		example.b();
		HelloAspect aspect = HelloAspect.aspectOf(); System.out.println( "Object Count is " + aspect.getCount()
				+ " Total count " + aspect.getTotalCount() ); 
	}
	//@RequiresPrevious(signature="onPointCutDefinition")
	pointcut methodCall() : call(* *(..) );

	//@RequiresPrevious(signature="onAdvice")
	before(): methodCall() { callCount++; totalCount++;}

}