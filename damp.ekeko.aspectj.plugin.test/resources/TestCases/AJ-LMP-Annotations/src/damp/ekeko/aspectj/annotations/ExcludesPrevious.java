package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface ExcludesPrevious {
		
	String[] label() default "";
	String[] signature() default "";
}
