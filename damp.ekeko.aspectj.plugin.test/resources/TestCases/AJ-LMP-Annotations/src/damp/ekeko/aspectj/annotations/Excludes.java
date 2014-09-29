package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
//@Repeatable(MultiExcludes.class)
public @interface Excludes {
		
	String aspect() default "";
	String label() default "";
	
}