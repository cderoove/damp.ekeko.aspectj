package damp.ekeko.aspectj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface Label {
	String[] value();
}
