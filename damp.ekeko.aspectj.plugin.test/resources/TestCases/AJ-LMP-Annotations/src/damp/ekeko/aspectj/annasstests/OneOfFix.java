package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//Should raise an error as both are absent
@OneOf(type = {"MissingClass1","MissingClass2"})
public class OneOfFix {

}
