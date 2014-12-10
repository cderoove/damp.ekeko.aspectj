package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//Should raise an error as both are present
@OneOf(type = {"damp.ekeko.aspectj.annasstests.ReqFix","damp.ekeko.aspectj.annasstests.ReqLabFix"})
public class OneOfFix2 {

}
