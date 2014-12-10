package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//Should raise an error as both are absent
@OneOf(type = {"damp.ekeko.aspectj.annasstests.MissingClass1","damp.ekeko.aspectj.annasstests.MissingClass2"})
public class OneOfFix {

}
