package baristaui.views.queryResult;

import baristaui.views.queryResult.SOULLabelProvider;

import org.eclipse.ajdt.core.AJProperties;
import org.eclipse.ajdt.core.AopXmlPreferences;
import org.eclipse.ajdt.core.AspectJCorePreferences;
import org.eclipse.ajdt.core.BuildConfig;
import org.eclipse.ajdt.core.model.AJProjectModelFactory;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.aspectj.asm.IProgramElement;
import org.aspectj.org.eclipse.jdt.core.dom.ASTNode;
import org.aspectj.org.eclipse.jdt.core.dom.AjNaiveASTFlattener;
import org.eclipse.ajdt.internal.ui.resources.AJDTIcon;
import org.eclipse.ajdt.internal.ui.resources.AspectJImages;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.ajdt.core.javaelements.AJCodeElement;
import org.eclipse.ajdt.core.javaelements.AJCompilationUnit;
import org.eclipse.ajdt.core.javaelements.AdviceElement;
import org.eclipse.ajdt.core.javaelements.AspectElement;
import org.eclipse.ajdt.core.javaelements.AspectJMemberElement;
import org.eclipse.ajdt.core.javaelements.DeclareElement;
import org.eclipse.ajdt.core.javaelements.IAspectJElement;
import org.eclipse.ajdt.core.javaelements.IntertypeElement;

public class AJDTLabelProvider extends SOULLabelProvider {


	//TODO: find a better way to get the original string, straight from the buffer 
	//from which the AST node originated
	AjNaiveASTFlattener flattener = new AjNaiveASTFlattener();

	public String prettyPrint(Object object){
		
		if (object instanceof ASTNode) {
			ASTNode node = (ASTNode) object;
			flattener.reset();
			node.accept(flattener);
			String txt = flattener.getResult().split("\n")[0];
			if(txt.length()>150){
				txt = txt.substring(0, 150);
			}
			return txt+" ...";
		}	

		if(object instanceof AspectJMemberElement) {
			try {
				AspectJMemberElement e = (AspectJMemberElement) object;
				ISourceRange range = e.getSourceRange();
				AJCompilationUnit cu = (AJCompilationUnit) e.getAncestor(IJavaElement.COMPILATION_UNIT);
				ISourceRange nameRange = e.getNameRange();
				cu.requestOriginalContentMode();
				String source = cu.getSource().substring(nameRange.getOffset(), range.getOffset() + range.getLength());
				cu.discardOriginalContentMode();
				return source;
			} catch (JavaModelException e1) {
			}
		}
			
			
		if(object instanceof IAspectJElement) {
			IAspectJElement ae = (IAspectJElement) object;
			return ae.getElementName();
		}

		return super.prettyPrint(object);
	}


	@Override
	public Image getImage(Object element) {
		//adapted from  org.eclipse.ajdt.internal.ui.lazystart.ImageDecorator
		//TODO: remove clone using http://wiki.eclipse.org/FAQ_How_to_decorate_a_TableViewer_or_TreeViewer_with_Columns%3F
		AJDTIcon icon = null;
		if (element instanceof ICompilationUnit){
			ICompilationUnit comp = (ICompilationUnit) element;
			IFile file = null;
			try {
				file = (IFile) comp.getCorrespondingResource();
			} catch (JavaModelException e) {
			}
			if(file != null) {
				if(comp instanceof AJCompilationUnit) {
					if (BuildConfig.isIncluded(file)) {
						icon = AspectJImages.ASPECTJ_FILE;
					} else {
						icon = AspectJImages.EXCLUDED_ASPECTJ_FILE;
					}
				}
			}
		}
		if (element instanceof IFile) { 
			IFile file= (IFile) element;
			if (file.getFileExtension() != null) {
				if (file.getFileExtension().equals(AJProperties.EXTENSION)) {
					icon = AspectJImages.BC_FILE;
				} else if (file.getFileExtension().equals("jar") || file.getFileExtension().equals("zip")) { //$NON-NLS-1$ //$NON-NLS-2$
					// TODO: decorate out-jars?
				} else if (file.getFileExtension().equals("xml")) {
					// maybe this is an aop.xml that is part of the build config
					if (new AopXmlPreferences(file.getProject()).isAopXml(file)) {
						icon =  AspectJImages.AOP_XML;
					}
				}
			} else 
				if (element instanceof JarPackageFragmentRoot) {
					JarPackageFragmentRoot root = (JarPackageFragmentRoot) element;
					try {
						IClasspathEntry entry = root.getRawClasspathEntry();
						if (entry != null) {
							IResource resource = root.getResource();
							String name = resource == null ? root.getElementName() : resource.getName();
							if (AspectJCorePreferences.isOnAspectpathWithRestrictions(entry, name)) {
								icon = AspectJImages.JAR_ON_ASPECTPATH;
							} else if (AspectJCorePreferences.isOnInpathWithRestrictions(entry, name)) {
								icon = AspectJImages.JAR_ON_INPATH;
							}
						}
					} catch (JavaModelException e1) {
					}
				}
		}

		if (element instanceof AJCodeElement) 
			icon = AspectJImages.AJ_CODE;

		if (element instanceof IAspectJElement) {
			try {
				IAspectJElement ajElem = (IAspectJElement)element;
				if(ajElem.getJavaProject().getProject().exists()) {
					IProgramElement.Accessibility acceb = ajElem.getAJAccessibility();
					if (acceb == null){
						if (ajElem instanceof AdviceElement) {
							boolean hasTest = AJProjectModelFactory.getInstance().getModelForJavaElement(ajElem)
									.hasRuntimeTest(ajElem);
							icon = (AJDTIcon)AspectJImages.instance().getAdviceIcon(ajElem.getAJExtraInformation(), hasTest);
						} else if (ajElem instanceof IntertypeElement) {
							icon = (AJDTIcon)AspectJImages.instance().getStructureIcon(ajElem.getAJKind(), ajElem.getAJAccessibility());
						} else if (ajElem instanceof DeclareElement) {
							icon = (AJDTIcon)AspectJImages.instance().getStructureIcon(ajElem.getAJKind(), ajElem.getAJAccessibility());
						} else {
							icon = (AJDTIcon)AspectJImages.instance().getIcon(ajElem.getAJKind());
						}
					} else {
						icon = (AJDTIcon)AspectJImages.instance().getStructureIcon(ajElem.getAJKind(), ajElem.getAJAccessibility());
					}
				}
			} catch (JavaModelException e) {
			}
		}

		if(icon != null)
			return icon.getImageDescriptor().createImage();

		return super.getImage(element);
	}

}
