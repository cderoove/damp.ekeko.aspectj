package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.Excludes;

// We only test for type names here as patterns are tested in ReqFix
@Excludes(type={"AbsentExcClass","damp.ekeko.aspectj.annasstests.ReqFix"})
public class ExcFix {

}
