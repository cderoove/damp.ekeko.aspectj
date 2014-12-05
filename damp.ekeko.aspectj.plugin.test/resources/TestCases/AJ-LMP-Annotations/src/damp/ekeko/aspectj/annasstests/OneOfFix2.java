package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//Should raise an error as both are present
@OneOf(type = {"ReqFix","ReqLabFix"})
public class OneOfFix2 {

}
