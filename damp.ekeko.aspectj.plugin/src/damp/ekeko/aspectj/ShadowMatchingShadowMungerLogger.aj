package damp.ekeko.aspectj;

import org.aspectj.asm.AsmManager;
import org.aspectj.weaver.bcel.BcelWorld; 
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;


public aspect ShadowMatchingShadowMungerLogger {

	
	//Problem:
	//the AJDT compiler does not expose information about Shadows that have matched a ShadowMunger 
	//static methods on the global AsmRelationShipProvider are used to report the exposed information 
	//e.g.: public static void addAdvisedRelationship(AsmManager model, Shadow matchedShadow, ShadowMunger munger)
	//is called by BcelWorld.reportMatch(ShadowMunger, Shadow)
	//these cannot be overriden/replaced using non-invasive techniques
	
	//Possible solution:
	//use this aspect to intercept the calls, but weaving into an Eclipse plugin (i.e., org.aspectj.weaver)
	//seems rather complicated: http://eclipse.org/equinox/weaving/
	
	//More realistic solution: (to be implemented by some poor sob eventually): 
	//replace the class AsmRelationShipProvider (used as a global) with a similar one in which the static method 
	//public static void addAdvisedRelationship(AsmManager model, Shadow matchedShadow, ShadowMunger munger)
	//has been replaced by one that logs the actual ShadowMunger - Shadow pairs
	//approach:
	// - get the aspectj code from its git repo (takes ages): http://git.eclipse.org/gitroot/aspectj/org.aspectj.git
	// - get it to compile: http://www.eclipse.org/aspectj/doc/released/faq.php#q:buildingsource
	// - replace AsmRelationShipProvider
	// - export the resulting aspectjweaver.jar to Eclipse plugin org.aspectj.weaver, obtained by
	//   importing the Eclipse plugin org.aspectj.weaver from the plugin development view into the workspace as a source project
	// --> during development, the patched plugin in the workspace should now always be used instead of the installed one
	// --> to distribute the patched plugin, create a feature patch: http://www.vogella.com/articles/EclipseCodeAccess/article.html#featurepatchprojects
	
	before(): execution(void BcelWorld.reportMatch(ShadowMunger, Shadow)) {
		System.out.println("Found a match!");
	}

}
