package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.RequiresPrevious;


//testing advice execution order and execution paths through advice and methods
public aspect PrevFix2 {
	declare precedence: PrevFix, PrevFix2;
	
	//succeeds
	@RequiresPrevious("beforecompute")
	before() : call(int *.compute(String))
	{	}
	
	//succeeds
	@RequiresPrevious("beforecompute")
	after() : call(int *.compute(String))
	{	}
	
	//succeeds
	@RequiresPrevious("preparing")
	after() : call(int *.compute(String))
	{	}
	
	//succeeds
	@RequiresPrevious("beforecompute")
	after() : call(void *.simulate())
	{
		this.unthink();
	}
	
	@RequiresPrevious("thinking")
	void unthink()
	{ }
}
