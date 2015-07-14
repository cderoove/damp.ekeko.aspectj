package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//This should not raise an error
@OneOf(
	type = {"damp.ekeko.aspectj.annasstests.ReqSat",
			"damp.ekeko.aspectj.annasstests.MissingClass1"},
	label ={"Excluded"})
public class OneOfFix4 {

}
