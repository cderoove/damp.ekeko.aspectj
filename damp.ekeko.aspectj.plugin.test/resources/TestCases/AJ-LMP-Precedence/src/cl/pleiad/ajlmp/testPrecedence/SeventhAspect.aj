package cl.pleiad.ajlmp.testPrecedence;

//This is getting crazy
public abstract aspect SeventhAspect {
	declare precedence: SeventhAspect, FifthAspect;
}
