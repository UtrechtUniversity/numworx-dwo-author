package fi.wiskopdr.cbook;

import java.applet.AppletContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.WeakHashMap;
import java.util.prefs.Preferences;

import javax.swing.JComponent;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.rm.ResourceManager;

import fi.beans.wiskopdrbeans.ResourceManagerClient;
import fi.beans.wiskopdrbeans.ResourceManagerClient.ResourceManagerFactory;
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
	
	static ResourceManager getResourceManager(String widget, int page, String instance) {
		return getResourceManager(widget, page + "-" + instance);
	}
		
	static ResourceManager getResourceManager(ResourceManagerClient panel) {
		return getResourceManager(panel.getClassName(), WiskOpdr.getPageNr(), panel.getInstanceId());
	}
	
	static ResourceManager getResourceManager(String widget, String instance) {
        String unit    = WiskOpdr.getUnit_id();
		String key = widget + "/" + unit + "-" + instance;
		ResourceManager rm = rmmap.get(key);
		if(rm != null) return rm;
		
		String student = WiskOpdr.getLearner_id();
		String user    = student;
		String passwd  = WiskOpdr.getOAuthToken();
		URL root = getResourceRoot();		

		rm = null;//new fi.wiskopdr.cbook.rm.WebManager(root, widget, unit, instance, student, user, passwd);

		try {
	      ResourceManagerFactory factory = (ResourceManagerFactory) Class.forName("nl.numworx.uploadwidget.rm.Factory").newInstance();
	      Method m = factory.getClass().getMethod("setContext", CBookContext.class);		
	      m.invoke(factory,new CBookContext() {
            
            @Override
            public Object getProperty(String key) {
              if(LEARNER_ID.equals(key))
                return WiskOpdr.getLearner_id();
              if("serverUrlPath".equals(key))
                return root;
              if("oauth_token".equals(key))
                return passwd;
              if (UUID.equals(key))
                  return unit + "-" + instance;
              
              return null;
            }
          });
          rm = factory.getResourceManager();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

		rmmap.put(key, rm);
		return rm;

	}

	private static URL getResourceRoot() {
		try {
		    String serverUrlPath = WiskOpdr.applet.getParameter("serverUrlPath");
		    return new URL(serverUrlPath);
			//return new URL("http://localhost:8888/dwo/"); // LOCAL
		} catch (Exception oops) {
			return null;
		}
	}
	
//	static AbstractPageManager getPageManager() {
//		return new AbstractPageManager();
//		String student = WiskOpdr.getLearner_id();
//		String unit    = WiskOpdr.getUnit_id();
//		String user    = student;
//		String passwd  = WiskOpdr.getOAuthToken();
//		return new PageManager(getResourceRoot(), unit, user, passwd);
//	}
	
	
	public static ResourceManagerClient.ResourceManagerFactory getFactory(final ResourceManagerClient client) {
		return new ResourceManagerClient.ResourceManagerFactory() {
			public ResourceManager getResourceManager() {
				return WidgetBridge.getResourceManager(client);
			}
		};
	}

	public static ResourceManagerFactory getFactory(final CBookWidgetIF w,
			final String instance) {
		return new ResourceManagerFactory() {
			public ResourceManager getResourceManager() {
				return WidgetBridge.getResourceManager(Service.getClassName(w), WiskOpdr.getPageNr(), instance);
			}
		};
	}

	public static Preferences getPreferences(String clazzName) {
		String nodeName = clazzName.replace('.', '/');
		return Preferences.userRoot().node(nodeName);
	}
}
