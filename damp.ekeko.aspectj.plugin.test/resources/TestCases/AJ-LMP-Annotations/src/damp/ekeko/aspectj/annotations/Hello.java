package damp.ekeko.aspectj.annotations;

@Requires(aspect=HelloAspect.class, label="Foobar")
public class Hello {

	@RequiresPrevious("onMethod")
	void b() {
		System.out.println("In b"); 
	}
}
