package damp.ekeko.aspectj.refactoring.aspects;

import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import damp.ekeko.aspectj.refactoring.AJInvariantChecker;


public aspect RefactoringConditionsHook{

	
	pointcut runCheckConditions() : execution(public void CheckConditionsOperation.run(..));
	
	pointcut checkFinalConditions(Refactoring r) : execution(* checkFinalConditions(..)) && this(r) && !cflow(adviceexecution());
	
	//Warning because AJ does not see the classes from the ltk plugin at compiletime (uses LTW)
	RefactoringStatus around(Refactoring r): checkFinalConditions(r){ //&& withincode(public void CheckConditionsOperation.run(..)){
	
		RefactoringStatus rs = proceed(r);
		
		try {
			Change c = r.createChange(null);
			//TODO: Changes for renames of top-level ICU's do not provide a correct preview. To get around this, we need to extract the new name from the refactoring and feed it to the invariant checker. This is not done for the moment.
			rs.merge(AJInvariantChecker.checkInvariants(c, ""));
			//need to check conditions again to reset refactoring change
			r.checkFinalConditions(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
