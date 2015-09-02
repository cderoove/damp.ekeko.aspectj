package test.damp.ekeko;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import test.damp.EkekoTestHelper;
import ccw.util.osgi.ClojureOSGi;
import damp.ekeko.aspectj.EkekoAspectJPlugin;

public class EkekoAspectJTest {

	private static Bundle myBundle;

	static {
		myBundle = FrameworkUtil.getBundle(EkekoAspectJTest.class);
	}

	@BeforeClass
	public static void ensureTestCasesExist() throws Exception {

		String[] projectNames = {
				"AJ-LMP-AdviceOrdering" , 
				"AJ-LMP-Inclusion",
				"AJ-LMP-MarkerIface",
				//"AJ-LMP-Pointcuts",
				"AJ-LMP-RefineUsedPointcut", 
				"AJ-LMP-Wormhole",
				"AJ-LMP-ITD",
				"AJ-LMP-Interface", 
				"AJ-LMP-MutExHeuristics",
				"AJ-LMP-Precedence",
				"AJ-LMP-SimpleReentrancy",
				"AJ-LMP-Annotations"
		};
		for(String projectName : projectNames) {
			EkekoTestHelper.ensureProjectImported(myBundle, "/resources/TestCases/", projectName);
		}
	}

	@Test
	public void testPluginID() {
		assertEquals(EkekoAspectJPlugin.PLUGIN_ID, "damp.ekeko.aspectj.plugin");		
	}
	
	@Test 
	public void testRequireEkekoAspectJ() {
		ClojureOSGi.require(myBundle, "damp.ekeko.aspectj");
	}

	@Test 
	public void testEkekoAspectJTestSuite() {
		EkekoTestHelper.testClojureNamespace(myBundle, "test.damp.ekeko.aspectj");
	} 



}
