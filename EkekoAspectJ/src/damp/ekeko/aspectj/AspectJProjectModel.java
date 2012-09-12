package damp.ekeko.aspectj;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.ajde.core.AjCompiler;
import org.aspectj.ajdt.internal.core.builder.AjState;
import org.aspectj.ajdt.internal.core.builder.IncrementalStateManager;
import org.aspectj.org.eclipse.jdt.core.IJavaElement;
import org.aspectj.org.eclipse.jdt.core.IJavaProject;
import org.aspectj.org.eclipse.jdt.core.dom.AST;
import org.aspectj.org.eclipse.jdt.core.dom.ASTNode;
import org.aspectj.org.eclipse.jdt.core.dom.ASTParser;
import org.aspectj.org.eclipse.jdt.core.dom.CompilationUnit;
import org.aspectj.org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.aspectj.weaver.World;
import org.eclipse.ajdt.core.AspectJPlugin;
import org.eclipse.ajdt.core.javaelements.AJCompilationUnit;
import org.eclipse.ajdt.core.javaelements.AJCompilationUnitManager;
import org.eclipse.ajdt.core.model.AJProjectModelFacade;
import org.eclipse.ajdt.core.model.AJProjectModelFactory;
import org.eclipse.ajdt.core.model.AJWorldFacade;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import damp.ekeko.JavaProjectModel;

public class AspectJProjectModel extends JavaProjectModel {

	private ConcurrentHashMap<AJCompilationUnit, CompilationUnit> ajicu2ajast;
	private ConcurrentHashMap<CompilationUnit, AJCompilationUnit> ajast2ajicu;
	
	
	private AJProjectModelFacade ajFacade;
	private AJWorldFacade ajWorldFacade;
	private World ajWorld;


	public AspectJProjectModel(IProject p) {
		super(p);
		ajicu2ajast = new ConcurrentHashMap<AJCompilationUnit, CompilationUnit>();
		ajast2ajicu = new ConcurrentHashMap<CompilationUnit, AJCompilationUnit>();

	}
	
	public AJCompilationUnit getAJCompilationUnitForCompilationUnit(CompilationUnit cu) {
		return ajast2ajicu.get(cu);
	}
	
	public CompilationUnit getCompilationUnitForAJCompilationUnit(AJCompilationUnit  icu) {
		return ajicu2ajast.get(icu);
	}

	
	
	private void updateAJBuildInformation() {
		updateAJProjectFacade();
		updateAJWorldFacade();
		updateAJWorldAsSeenByWeaver();
	}
	
	private void updateAJWorldFacade() {
		IProject p = getProject();
		ajWorldFacade =  new AJWorldFacade(p);
	}
	
	private void updateAJWorldAsSeenByWeaver() {
		IProject p = getProject();
		AjCompiler compiler = AspectJPlugin.getDefault().getCompilerFactory().getCompilerForProject(p);
		AjState state = IncrementalStateManager.retrieveStateFor(compiler.getId());
		if (state != null) 
			ajWorld = state.getAjBuildManager().getWorld();
		else
			ajWorld = null;
	}
	
	private void updateAJProjectFacade() {
		IProject p = getProject();
		System.out.println("Updating AspectJ project model facade for:" + p.getName());
		ajFacade = AJProjectModelFactory.getInstance().getModelForProject(getProject());
	}
	
	public AJProjectModelFacade getAJProjectFacade() {
		return ajFacade;
	}
	
	public AJWorldFacade getAJWorldFacade() {
		return ajWorldFacade;
	}
	
	public World getAJWorldAsSeenByWeaver() {
		return ajWorld;
	}

	
	public boolean isAJCompilationUnit(ICompilationUnit icu) {
		return icu instanceof AJCompilationUnit;
	}
	
	public boolean isJCompilationUnit(ICompilationUnit icu) {
		//PackageFragmentRoot.getCompilationUnits() returns an AJCompilationUnit instance
		//and a CompilationUnit instance for each .aj file ..	
		return 	!icu.getResource().getFileExtension().equals(AspectJPlugin.AJ_FILE_EXT);
	}
	
	public AJCompilationUnit ajCuForICU(ICompilationUnit icu) {
		return (AJCompilationUnit) AJCompilationUnitManager.mapToAJCompilationUnit(icu);
	}
	
	//model elements
	public Iterable<AJCompilationUnit> getAJCompilationUnits() {
		return ajicu2ajast.keySet();
	}
	
	//ast nodes
	public Iterable<CompilationUnit> getAspectJCompilationUnits() {
		return ajicu2ajast.values();
	}

	public CompilationUnit parseAJ(AJCompilationUnit ajcu,IProgressMonitor monitor) throws JavaModelException {
		//note that the JDT parser has  been configured for JLS4
		ASTParser ajdtparser = ASTParser.newParser(AST.JLS3); 	

		//options seem to be necessary: https://bugs.eclipse.org/bugs/show_bug.cgi?id=211201
		Map<String,String> options = new HashMap<String,String>();
		options.put(CompilerOptions.OPTION_Source, "1.5");
		ajdtparser.setCompilerOptions(options);

		ajdtparser.setResolveBindings(true);
		ajdtparser.setKind(ASTParser.K_COMPILATION_UNIT);

		//ajdtparser.setProject(ajcu.getJavaProject());
		//ajdtparser.setProject((IJavaProject)ajcu.getAncestor(IJavaElement.JAVA_PROJECT));
		ajdtparser.setStatementsRecovery(false);
		ajdtparser.setBindingsRecovery(false);
		char[] contents;
		synchronized (ajcu) {
			try {
				ajcu.requestOriginalContentMode();
				contents = ajcu.getContents();
			} finally {
				ajcu.discardOriginalContentMode();
			}
		}
		
		ajdtparser.setSource(contents);
		ASTNode result = ajdtparser.createAST(monitor);
		return (CompilationUnit) result;
	}

	@Override
	public void clean() {
		ajFacade = null;
		super.clean();
	}

	@Override
	public void populate(IProgressMonitor monitor) throws CoreException {
		updateAJBuildInformation();
		System.out.println("Populating AspectJProjectModel for: " + javaProject.getElementName());
		for(IPackageFragment frag : javaProject.getPackageFragments()) {
			ICompilationUnit[] icus = frag.getCompilationUnits();
			for(ICompilationUnit icu : icus) {
				if (isAJCompilationUnit(icu)) {
					try {
						AJCompilationUnit ajcu = ajCuForICU(icu);
						CompilationUnit ajcunode = parseAJ(ajcu, monitor);
						ajicu2ajast.put(ajcu, ajcunode);
						ajast2ajicu.put(ajcunode, ajcu);	
						continue;
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
				} 
				if (isJCompilationUnit(icu))
					icu2ast.put(icu, super.parse(icu, monitor));
				}
			}		
		gatherInformationFromCompilationUnits();
		gatherInformationFromAJCompilationUnits();
	}

	
	private void gatherInformationFromAJCompilationUnits() {
		final long startTime = System.currentTimeMillis();
		//for(CompilationUnit cu : ajicu2ajast.values()) 	
		//	addInformationFromVisitor(visitCompilationUnitForInformation(cu));
		final long duration = System.currentTimeMillis() - startTime;
		System.out.println("Gathered information from AJDT compilation units in " + duration + "ms");	
	}

	@Override
	public void processDelta(IResourceDelta delta, IProgressMonitor monitor)
			throws CoreException {
		// no incremental builds for aspectj files
		// TODO: refine this, such that only the aspectj facade is rebuilt
		clean();
		populate(monitor);
	}
	

	@Override
	protected void addControlFlowGraphInformationForMethodDeclaration(MethodDeclaration m) {
	}
	
	
	
}
