package nl.numworx.samllogin;

import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;

import nl.numworx.samllogin.SamlLoginPanel.API;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;

public class Activator extends SwingBrowserProvider implements BundleActivator, BiConsumer<SamlLoginPanel, SamlLoginPanel.API> {

	FactoryTracker tracker;
	
	@Override
	public void start(BundleContext context) throws Exception {
		Filter filter = context.createFilter("(&(objectClass=nl.numworx.swingbrowser.api.SwingBrowserFactory)(nl.numworx.swingbrowser.type=ext))");
		tracker = new FactoryTracker(context, filter);
		SamlLoginPanel.provider = this;
		SamlLoginPanel.strategy = this;
		tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
		
	}

	@Override
	public SwingBrowserFactory getFactory() throws NoSuchElementException {
		return tracker.getFactory();
	}

	@Override
	public void accept(SamlLoginPanel t, API u) {
		t.extra = "?r=" + tracker.getPort();
		t.getJfxPanel().addPropertyChangeListener(u);
	}

}
