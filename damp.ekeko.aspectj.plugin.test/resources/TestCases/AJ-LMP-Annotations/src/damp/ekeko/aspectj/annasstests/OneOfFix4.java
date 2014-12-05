package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.OneOf;

//This should not raise an error
@OneOf(type = {"ReqSat","MissingClass1"})
public class OneOfFix4 {

}
