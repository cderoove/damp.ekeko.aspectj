package cl.pleiad.ajlmp.testPrecedence;

//This is getting crazy
public aspect EightAspect extends SeventhAspect {
	declare precedence: ThirdAspect, EightAspect;
}
