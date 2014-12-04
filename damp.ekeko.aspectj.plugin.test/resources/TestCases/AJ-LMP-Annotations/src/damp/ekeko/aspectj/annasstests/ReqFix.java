package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.Requires;

@Requires(type = {"ReqSat","AbsentReqSat","ReqSatTree+","AbsentReqSatTree+"})
public class ReqFix {

}
