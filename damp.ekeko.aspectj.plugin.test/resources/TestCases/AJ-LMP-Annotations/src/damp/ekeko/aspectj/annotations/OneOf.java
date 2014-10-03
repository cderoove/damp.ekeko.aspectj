package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface OneOf {
		
	String[] aspect() default {};
	String[] label() default {};
}
