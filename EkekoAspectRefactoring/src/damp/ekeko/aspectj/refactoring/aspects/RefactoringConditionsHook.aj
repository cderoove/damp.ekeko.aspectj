package damp.ekeko.aspectj.refactoring.aspects;

import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import damp.ekeko.aspectj.refactoring.AJInvariantChecker;


public aspect RefactoringConditionsHook{

	
	pointcut runCheckConditions() : execution(public void CheckConditionsOperation.run(..));
	
	pointcut checkFinalConditions(Refactoring r) : execution(* checkFinalConditions(..)) && this(r) && !cflow(adviceexecution());
	
	
	RefactoringStatus around(Refactoring r): checkFinalConditions(r){ //&& withincode(public void CheckConditionsOperation.run(..)){
	
		RefactoringStatus rs = proceed(r);
		
		try {
			Change c = r.createChange(null);
			rs.merge(AJInvariantChecker.checkInvariants(c, ""));
			//maybe need to check conditions again
			r.checkFinalConditions(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
