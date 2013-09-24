package damp.ekeko.aspectj.refactoring.recompiling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ProgressMonitor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
//import org.eclipse.jdt.internal.corext.refactoring.AbstractJavaElementRenameChange;
//import org.eclipse.jdt.internal.corext.refactoring.changes.RenameCompilationUnitChange;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import damp.ekeko.aspectj.refactoring.RefactoringAspectJProjectModelFactory;
import damp.ekeko.aspectj.refactoring.RefactoringAspectjProjectModel;



public class ChangeSimulator {

	ASTParser parser;

	ICompilationUnit[] preTouchedICU;
	CompilationUnit pretouchedCU;
	
	class ContentAndTimestamp {
		String content;
		long timestamp;
	}

	Map<ICompilationUnit, ContentAndTimestamp> originalContents = new HashMap<ICompilationUnit, ContentAndTimestamp>();
	Map<ICompilationUnit,String> newContents = new HashMap<ICompilationUnit, String>();
	
	private ICompilationUnit[] touchedICUAs;
	
	public ChangeSimulator() {
		parser = ASTParser.newParser(AST.JLS4);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	}
	
	
	public void registerNewContent(ICompilationUnit icu, String newContent){
		newContents.put(icu, newContent);
	}
	
	public void simulateChange() throws CoreException, IOException{
		Set<IJavaProject> projectsToRebuild = new HashSet<IJavaProject>();
	//	originalContents.clear();
		for (ICompilationUnit icu : newContents.keySet()) {
			//save originals
			ContentAndTimestamp ca = new ContentAndTimestamp();
			ca.content = icu.getSource();
			ca.timestamp = icu.getResource().getModificationStamp();
			originalContents.put(icu, ca);
			//replace contents
			icu.getBuffer().setContents(newContents.get(icu));
			
			saveIResource(icu);
			
			
			//add projects to list
			projectsToRebuild.add(icu.getJavaProject());
		}
		
		for (IJavaProject iJavaProject : projectsToRebuild) {
			//get model for project
			RefactoringAspectjProjectModel rm = RefactoringAspectJProjectModelFactory.getCurrentInstance().getModel(iJavaProject.getProject());
			//rebuild project
			iJavaProject.getProject().build(IncrementalProjectBuilder.FULL_BUILD, null);
			//update model
			rm.updateWorld();
		}
	}
	
	private void saveIResource(ICompilationUnit icu) throws IOException,
			CoreException {
		File f = icu.getResource().getLocation().toFile();
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(icu.getSource().getBytes());
		fo.close();
		icu.save(null, true);
		icu.getResource().refreshLocal(IResource.DEPTH_ONE, null);
	}

	@Deprecated
	public void simulateChange(Change c, String newName) throws CoreException{
		List<ICompilationUnit> touchedICU = new ArrayList<ICompilationUnit>();
		List<CompilationUnit> afterCU = new ArrayList<CompilationUnit>();
		
		Map<ICompilationUnit, CompilationUnit> changedICUtoCU = new HashMap<ICompilationUnit, CompilationUnit>();
		
		IJavaProject jP = null;
		for(CompilationUnitChange cuc : getCUChanges(c)){
			jP = cuc.getCompilationUnit().getJavaProject();
			ICompilationUnit changed = cuc.getCompilationUnit();
			touchedICU.add(changed);
		
			CompilationUnit compileFromString = compileFromString(cuc);
			afterCU.add(compileFromString);
//			changed.save(null,false);
			File f = changed.getResource().getLocation().toFile();
			try {
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(changed.getSource().getBytes());
				fo.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			changed.getCorrespondingResource().refreshLocal(IResource.DEPTH_ZERO, null);
			changedICUtoCU.put(changed, compileFromString);
			
		}
		
		for(ResourceChange rc : getTChanges(c,ResourceChange.class)){
			Object element = rc.getModifiedElement();
			if (element instanceof ICompilationUnit && !"".equals(newName)) {
				ICompilationUnit icu = (ICompilationUnit) element;
				jP = icu.getJavaProject();
				String oldSource = icu.getSource();
								icu.getBuffer().setContents(oldSource.replaceAll("class[\\s]+"+icu.findPrimaryType().getElementName(), "class "+newName)); //simulate rename
				CompilationUnit cu = parseICU(icu);
				afterCU.add(cu);
				touchedICU.add(icu);
				changedICUtoCU.put(icu, cu);
//				icu.getBuffer().setContents(oldSource);
				File f = icu.getResource().getLocation().toFile();
				try {
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(icu.getSource().getBytes());
					fo.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				icu.getCorrespondingResource().refreshLocal(IResource.DEPTH_ZERO, null);
			}
		}
		
		
		touchedICUAs = (ICompilationUnit[]) touchedICU
				.toArray(new ICompilationUnit[touchedICU.size()]);
		
		CompilationUnit[] afterCuA = (CompilationUnit[]) afterCU
				.toArray(new CompilationUnit[afterCU.size()]);
		if(!touchedICU.isEmpty()){
			RefactoringAspectjProjectModel rm = RefactoringAspectJProjectModelFactory.getCurrentInstance().getModel(jP.getProject());
			//rm.clean();
			rm.setChanged(changedICUtoCU);
			//rm.populate(null);
			//jP.getProject().build(IncrementalProjectBuilder.FULL_BUILD, "ajbuilder", args, monitor), null);
			//jP.getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
			jP.getProject().build(IncrementalProjectBuilder.FULL_BUILD, null);
			rm.updateWorld();
		}
		
	}
	
	public void restoreState() throws CoreException, IOException {


		for (ICompilationUnit compUnit : originalContents.keySet()) {
			compUnit.getBuffer().setContents(originalContents.get(compUnit).content);
			saveIResource(compUnit);
			compUnit.getResource().revertModificationStamp(originalContents.get(compUnit).timestamp);
			
			compUnit.getResource().refreshLocal(IResource.DEPTH_ONE, null);
			System.out.println();
		}
		originalContents.clear();
		newContents.clear();

	}
	
	IJavaProject getProjectForChange(Change c){
		IJavaProject jP = null;
		
		for(CompilationUnitChange cuc : getCUChanges(c)){
			jP = cuc.getCompilationUnit().getJavaProject();
		}
		
		if(jP == null){
			
			for(ResourceChange rc : getTChanges(c, ResourceChange.class)){
				Object o = rc.getModifiedElement();
				if (o instanceof ICompilationUnit) {
					ICompilationUnit icu = (ICompilationUnit) o;
					jP = icu.getJavaProject();
				}
			}
		}
		
		return jP;
	}
	
	
	public static <T extends Change>  List<T> getTChanges(Change cc, Class<T> clazz){
		List<T> cs = new ArrayList<T>();
		if (cc instanceof CompositeChange) {
			CompositeChange cc2 = (CompositeChange) cc;
			for(Change c : cc2.getChildren()){
				cs.addAll(getTChanges(c,clazz));
			}
		}
		
		if(clazz.isAssignableFrom(cc.getClass())){
			cs.add((T) cc); //safe cast, stupid compiler!
		}
		
		
		return cs;
	}
	

	private List<CompilationUnitChange> getCUChanges(Change cc){
		return getTChanges(cc, CompilationUnitChange.class);
//		ArrayList<CompilationUnitChange> cus = new ArrayList<CompilationUnitChange>();
//		
//		if (cc instanceof CompositeChange) {
//			CompositeChange cc2 = (CompositeChange) cc;
//			for(Change c : cc2.getChildren()){
//				cus.addAll(getCUChanges(c));
//			}
//		}
//		if(cc instanceof CompilationUnitChange){
//			cus.add((CompilationUnitChange)cc);
//		}
//		return cus;
		
	}
	
	private CompilationUnit compileFromString(CompilationUnitChange cuc) throws CoreException {
		ICompilationUnit iCompilationUnit = cuc.getCompilationUnit();
		String previous = iCompilationUnit.getSource();
		String newContent = cuc.getPreviewContent(null);
		iCompilationUnit.getBuffer().setContents(newContent); //XXX carful here!
		CompilationUnit cu = parseICU(iCompilationUnit);
		ContentAndTimestamp ca = new ContentAndTimestamp();
		ca.content = previous;
		ca.timestamp = iCompilationUnit.getResource().getLocalTimeStamp();
		originalContents.put(iCompilationUnit, ca);
		//iCompilationUnit.getBuffer().setContents(previous); //Hopefully restore contents		
		return cu;
	}

	private CompilationUnit parseICU(ICompilationUnit iCompilationUnit) {
		final CompilationUnit compilationUnits[];
		compilationUnits = new CompilationUnit[1];
		ASTRequestor requestor = new ASTRequestor() { 
			private int current=0;
			public void acceptAST(ICompilationUnit source, CompilationUnit ast){
				compilationUnits[current++] = ast;
			};
		};
		parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setProject(iCompilationUnit.getJavaProject());
		parser.createASTs(new ICompilationUnit[]{iCompilationUnit}, new String[0], requestor, null);
		CompilationUnit cu = compilationUnits[0];
		
		return cu;
	}


//	
}
