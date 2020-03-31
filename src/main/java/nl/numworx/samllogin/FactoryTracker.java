package nl.numworx.samllogin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.util.tracker.ServiceTracker;

import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;

public class FactoryTracker extends ServiceTracker<SwingBrowserFactory, SwingBrowserFactory> implements SwingBrowserFactory {

	SwingBrowserProvider fallback = new SwingBrowserProvider();
	
	public FactoryTracker(BundleContext context, Filter filter) {
		super(context, filter, null);
	}

	@Override
	public SwingBrowser newBrowser() {
		return getFactory().newBrowser();
	}

	public SwingBrowserFactory getFactory() {
		SwingBrowserFactory service = getService();
		if (service == null) service = fallback.getFactory();
		return service;
	}

	@Override
	public void newSession() {
		getFactory().newSession();
		
	}


}
