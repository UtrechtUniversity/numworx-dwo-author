package fi.wiskopdr.cbook;

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.WeakHashMap;

import javax.swing.JComponent;

import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.rm.ResourceManager;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.LinkIF;
import fi.wiskopdr.tekstobjects.LinkRegel;

public abstract class WidgetBridge implements /*WiskOpdrApplet,*/ Constants {

	public static final String TELT_MEE = "teltMee";

	public static final String JSOBJECT = "JSObject";
	public static final String APPLET_CONTEXT = "AppletContext";
	
	public static final String LAUNCH_DATA = "launchData";
	
	static LinkIF linkif;
	
	private CBookWidgetIF widget;
	private Locale locale;
	protected WidgetBridge(Class<? extends CBookWidgetIF> clazz, Locale locale) 
	throws Exception
	{
		this.locale = locale;
		JComponent.setDefaultLocale(locale);
		widget = clazz.newInstance(); 
	}
		
//	public InteractiePanel getInteractiePanel() {
//		return new CBookInteractiePanel(widget);
//	}

	public static void setJSObjectOwner(LinkIF applet) {
		linkif = applet;
	}
	
	static AppletContext getAppletContext() {
		if(linkif != null) {
			return linkif.getAppletContext();
		}
		return null;
	}
	
	static Object getJSObject() {
		LinkRegel.findJSObject(linkif);
		if(linkif != null)
			return linkif.getJSObject();
		return null;
		
	}

	private static WeakHashMap<String, ResourceManager> rmmap = new WeakHashMap<String, ResourceManager>();
	
	static ResourceManager getResourceManager(String widget, String instance) {
		String key = widget + "/" + instance;
		ResourceManager rm = rmmap.get(key);
		if(rm != null) return rm;
		
		String student = WiskOpdr.getLearner_id();
		String unit    = WiskOpdr.getUnit_id();
		String user    = student;
		String passwd  = WiskOpdr.getOAuthToken();

		URL root = null;
		try {
			root = new URL("https://mc2-resource.appspot.com/dav/");
			//root = new URL("http://localhost:8888/dav/"); // LOCAL
		} catch (MalformedURLException _) {}
		
		rm = new fi.wiskopdr.cbook.rm.WebManager(root, widget, unit, instance, student, user, passwd);
		rmmap.put(key, rm);
		return rm;

	}
}
