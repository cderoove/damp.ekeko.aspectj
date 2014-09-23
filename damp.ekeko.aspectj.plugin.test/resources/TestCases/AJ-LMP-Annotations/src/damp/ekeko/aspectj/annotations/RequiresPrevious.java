package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface RequiresPrevious {
		
	String signature() default "";
	String label() default "";
}