package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
public @interface Requires {
		
	String[] type() default {};
	String[] label() default {};
	
}
