package damp.ekeko.aspectj.refactoring;

import java.util.Map;

import org.aspectj.ajde.core.AjCompiler;
import org.aspectj.ajdt.internal.core.builder.AjState;
import org.aspectj.ajdt.internal.core.builder.IncrementalStateManager;
import org.eclipse.ajdt.core.AspectJPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import damp.ekeko.aspectj.AspectJProjectModel;

public class RefactoringAspectjProjectModel extends AspectJProjectModel {
	
	public RefactoringAspectjProjectModel(IProject p) {
		super(p);
	}

	
//	protected void updateAJWorldAsSeenByWeaver() {
//		IProject p = getProject();//should be ShadowProject
//		AjCompiler compiler = AspectJPlugin.getDefault().getCompilerFactory().getCompilerForProject(p);
//		//causes a NPE in ajde internals
//		//compiler.buildFresh();
//		AjState state = IncrementalStateManager.retrieveStateFor(compiler.getId());
//		
//		if (state != null) 
//			setAJWorldAsSeenByWeaver(state.getAjBuildManager().getWorld());
//		else
//			setAJWorldAsSeenByWeaver(null);
//	}

	public void setChanged(Map<ICompilationUnit, CompilationUnit> changedICUtoCU) throws JavaModelException {
//		refactored = changedICUtoCU;	
		for (ICompilationUnit icu : changedICUtoCU.keySet()) {
			processChangedCompilationUnit(icu, changedICUtoCU.get(icu));
			icu.save(null, true);
		}
	}
	
	public void updateWorld(){
		updateAJWorldAsSeenByWeaver();
	}
	
	private void processChangedCompilationUnit(ICompilationUnit icu, CompilationUnit cu) {
		CompilationUnit old = icu2ast.remove(icu);
		removeInformationFromVisitor(visitCompilationUnitForInformation(old));
//		CompilationUnit cu = parse(icu,null);
		icu2ast.put(icu, cu);
		addInformationFromVisitor(visitCompilationUnitForInformation(cu));
	}

	
}
