package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.ExcludesPrevious;
import damp.ekeko.aspectj.annotations.RequiresPrevious;

//fixture for requires / excludes previous tests
public aspect PrevFix {

	//succeeds
	@RequiresPrevious("thinking")
	@ExcludesPrevious("cleaning")
	before() : call(int *.compute(String))
	{	}
	
	//fails, never happens
	@RequiresPrevious("executing")
	//fails, always happens
	@ExcludesPrevious("thinking")
	after() : call(void *.simulate())
	{	}
	
	//fails, only sometimes happens
	@RequiresPrevious("executing")
	@ExcludesPrevious("simulating")
	void around() : execution(void *.cleanup())
	{
		proceed();
	}
}
