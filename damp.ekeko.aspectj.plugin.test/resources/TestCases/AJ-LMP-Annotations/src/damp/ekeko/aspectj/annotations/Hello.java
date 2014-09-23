package damp.ekeko.aspectj.annotations;

@Requires(aspect="HelloAspect", label="Foobar")
public class Hello {

	@RequiresPrevious(signature="onMethod")
	void b() {
		System.out.println("In b"); 
	}
}
