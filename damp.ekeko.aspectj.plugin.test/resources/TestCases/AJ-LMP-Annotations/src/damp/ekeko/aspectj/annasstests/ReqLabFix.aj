package damp.ekeko.aspectj.annasstests;

import damp.ekeko.aspectj.annotations.Requires;
import damp.ekeko.aspectj.annotations.Label;

@Requires(label = {"ReqLabelSat","AbsentReqLabelSat"})
@Label("Excluded")
public aspect ReqLabFix {

}
