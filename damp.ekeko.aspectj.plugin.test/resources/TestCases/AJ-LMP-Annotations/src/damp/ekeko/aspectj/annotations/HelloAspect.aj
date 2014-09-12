package damp.ekeko.aspectj.annotations;

@Requires(aspect=HelloAspect.class)
@OneOf(aspects={HelloAspect.class, HelloAspect.class})
public aspect HelloAspect percflow(methodCall() ) {
	private int callCount = 0; 
	
	private static int totalCount = 0;
	
	public int getCount() { return callCount;
	}

	@RequiresPrevious("onAspectMethod")
	public int getTotalCount() { return totalCount;
	}
	
	public static void main(String[] arguments ) {
		Hello example = new Hello();
		example.b();
		HelloAspect aspect = HelloAspect.aspectOf(); System.out.println( "Object Count is " + aspect.getCount()
				+ " Total count " + aspect.getTotalCount() ); 
	}
	@RequiresPrevious("onPointCutDefinition")
	pointcut methodCall() : call(* *(..) );

	@RequiresPrevious("onAdvice")
	before(): methodCall() { callCount++; totalCount++;}

}