package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//Should raise an error as there are 2 subtypes present
@OneOf(type = {"damp.ekeko.aspectj.annasstests.ReqSatTree+"})
public class OneOfFix3 {

}
