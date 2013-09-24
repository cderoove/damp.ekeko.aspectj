package damp.ekeko.aspectj.refactoring;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.osgi.framework.Bundle;

import ccw.util.BundleUtils;
import ccw.util.osgi.ClojureOSGi;
import ccw.util.osgi.RunnableWithException;
import clojure.lang.Var;
import damp.ekeko.aspectj.refactoring.recompiling.ChangeSimulator;

public class AJInvariantChecker {

	private String fname;
	private Var invariant;
	private Bundle theBundle;

	public AJInvariantChecker(String invariant) throws CoreException {
		fname = invariant;
		theBundle = BundleUtils.loadAndGetBundle("damp.ekeko.plugin");
		this.invariant = BundleUtils.requireAndGetVar(theBundle, fname);

	}

	public Object eval() {
		return ClojureOSGi.withBundle(theBundle, new RunnableWithException() {
			public Object run() throws Exception {
				Object ret = invariant.invoke();
				return ret;
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Collection<?> compare(Object preInv, Object postInv) {
		
		if (postInv instanceof Collection && (preInv instanceof Collection)) {
			Collection  post = (Collection) postInv;
			Collection pre = (Collection) preInv;
			
			Collection ret = new HashSet(pre);
			
			for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				for (Object another : post) {
					if(object.toString().equals(another.toString()))
						iterator.remove();
				}
					
			}
			return ret;
		}
		
		return Collections.EMPTY_SET;
	}

	public static RefactoringStatus checkInvariants(Change refactoringChange, String name){
	
		ChangeSimulator rec = new ChangeSimulator();
		try {
			AJInvariantChecker inv = new AJInvariantChecker("damp.ekeko.aspectj.refactoring.invariants/invariant");
			Object preInv = inv.eval();
			configureChangeSimulatorForChange(rec,refactoringChange);
			try {
				rec.simulateChange();
			} catch (IOException e) {
				throw new RuntimeException("Could not simulate change -- file error", e);
			}

			Object postInv = inv.eval();
			Collection<?> lostShadows = AJInvariantChecker.compare(preInv,postInv);
			Collection<?> newlyCapturedShadows = AJInvariantChecker.compare(postInv, preInv);
			RefactoringStatus rs = new RefactoringStatus();
			
			for (Object object : newlyCapturedShadows) {
				rs.addWarning("New Shadow captured "+ object);
			}	
			
			for (Object object : lostShadows) {
				rs.addWarning("Old Shadow no longer captured "+ object);
			}
			
			try {
				rec.restoreState();
			} catch (IOException e) {
				throw new RuntimeException("Could not restore change -- file error", e);
			}

			
			System.out.println("DONE");
			return rs;
		} catch (CoreException e) {
			throw new RuntimeException("Died trying to simulate change", e);
		}
		
	}

	private static void configureChangeSimulatorForChange(
			ChangeSimulator rec, Change refactoringChange) throws CoreException {
		for (CompilationUnitChange cuc : ChangeSimulator.getTChanges(refactoringChange, CompilationUnitChange.class)) {
			rec.registerNewContent(cuc.getCompilationUnit(),cuc.getPreviewContent(null));
		}
	}


	private static Set<IJavaProject> projectsAffectedByChange(Change c){
		Set<IJavaProject> projects = new HashSet<IJavaProject>();
		for (CompilationUnitChange cuc : ChangeSimulator.getTChanges(c, CompilationUnitChange.class)) {
			projects.add(cuc.getCompilationUnit().getJavaProject());
		}
		return projects;
	}
}
