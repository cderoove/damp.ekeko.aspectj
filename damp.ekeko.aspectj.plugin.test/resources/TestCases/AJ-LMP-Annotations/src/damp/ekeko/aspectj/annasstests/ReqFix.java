package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.Requires;

@Requires(type = {
		"damp.ekeko.aspectj.annasstests.ReqSat",
		"damp.ekeko.aspectj.annasstests.AbsentReqSat",
		"damp.ekeko.aspectj.annasstests.ReqSatTree+",
		"damp.ekeko.aspectj.annasstests.AbsentReqSatTree+"})
public class ReqFix {

}
