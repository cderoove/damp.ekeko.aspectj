package damp.ekeko.aspectj.refactoring;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IProject;

import damp.ekeko.IProjectModel;
import damp.ekeko.aspectj.AspectJProjectModelFactory;

public class RefactoringAspectJProjectModelFactory extends
		AspectJProjectModelFactory {
	
	static RefactoringAspectJProjectModelFactory instance;
	
	public RefactoringAspectJProjectModelFactory(){
		instance  = this;
	}

	public static RefactoringAspectJProjectModelFactory getCurrentInstance(){
		if(instance != null)
			return instance;	
		throw new IllegalStateException("Factory not yet created");
	}
	
	
	ConcurrentHashMap<IProject, RefactoringAspectjProjectModel> projectToModel = new ConcurrentHashMap<IProject, RefactoringAspectjProjectModel>();
	
	@Override
	public IProjectModel createModel(IProject project) {
		RefactoringAspectjProjectModel model = new RefactoringAspectjProjectModel(project);
		projectToModel.put(project, model);
		return model;
	}
	
	public RefactoringAspectjProjectModel getModel(IProject p){
		return projectToModel.get(p);
	}
	
//	@Override
//	public Collection<String> applicableNatures() {
//		return Collections.emptyList();
//	}
}
