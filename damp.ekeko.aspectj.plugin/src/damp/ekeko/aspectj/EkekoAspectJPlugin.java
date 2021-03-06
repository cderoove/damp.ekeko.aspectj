package damp.ekeko.aspectj;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EkekoAspectJPlugin implements BundleActivator {

	public static final String PLUGIN_ID = "damp.ekeko.aspectj.plugin";
	
	private static EkekoAspectJPlugin plugin;
	
	public void start(BundleContext context) throws Exception {
		plugin = this;
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
	}
	
	public static EkekoAspectJPlugin getDefault() {
		return plugin;
	}

}
