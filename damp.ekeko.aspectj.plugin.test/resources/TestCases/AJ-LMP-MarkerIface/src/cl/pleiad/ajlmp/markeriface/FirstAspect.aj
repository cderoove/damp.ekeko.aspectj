package cl.pleiad.ajlmp.markeriface;

public aspect FirstAspect {
	declare parents : Base*2 implements Marker;

	before() : execution (* Base*.*()){
		System.out.println("FirstAspect-BeforeExec");
	}
}
