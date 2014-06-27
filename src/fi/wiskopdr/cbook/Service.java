package fi.wiskopdr.cbook;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

//import fi.nabouwenaanzichten_cbook.NabouwenAanzichten;



import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.swing.Icon;

import org.cbook.cbookif.*;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

/**
 * Utility class for CBook Widgets.
 * @author wim
 *
 */
public class Service {

	public static class ParentLoader extends ClassLoader {

		private ClassLoader parent;
		private boolean trust;

		public ParentLoader(ClassLoader parent, boolean trust) {
			super();
			this.parent = parent;
			this.trust = trust;
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve)
				throws ClassNotFoundException {
			try {
				Class<?> load = super.loadClass(name, resolve);
				//System.out.println("System loads: " + name);
				return load;
			} catch (ClassNotFoundException e) {
				if(trust || name.startsWith("org.cbook."))
				{
					Class<?> load = parent.loadClass(name);
					//System.out.println("Parent loads: " + name);
					return load;
				}
				throw e;
			}
		}

	}

	public static class WidgetComparator implements Comparator<CBookWidgetIF> {

		@Override
		public int compare(CBookWidgetIF o1, CBookWidgetIF o2) {
			return o1.toString().compareToIgnoreCase(o2.toString());
		}

	}
	
	static class Proxy implements CBookWrap, CBookWidgetFactoryIF, CBookLaunchData  {
		final private String className;
		final private ClassLoader loader;
		
		Proxy(String className, ClassLoader loader) {
			super();
			this.className = className;
			this.loader = loader;
		}

		private CBookWidgetIF delegate;		
		CBookWidgetIF getDelegate() {
			if(delegate == null)
			{
				try {
					delegate= (CBookWidgetIF)Class.forName(className, false, loader).newInstance();
					classMap.put(className, delegate);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return delegate;		
		}

		public CBookWidgetInstanceIF getInstance(CBookContext context) {
			return getDelegate().getInstance(context);
		}

		public CBookWidgetEditIF getEditor(CBookContext context) {
			return getDelegate().getEditor(context);
		}

		public String toString() {
			return getDelegate().toString();
		}

		public Icon getIcon() {
			return getDelegate().getIcon();
		}

		@Override
		public Map<String, ?> getLaunchData() {
			return Collections.emptyMap();
		}

		@Override
		public Collection<CBookLaunchData> getInitialLaunchData(
				CBookContext context) {
			if( getDelegate() instanceof CBookWidgetFactoryIF)
				return ((CBookWidgetFactoryIF) getDelegate()).getInitialLaunchData(context);
			
			return Collections.singleton((CBookLaunchData)this);
		}

		@Override
		public String getClassName() {
			return className;
		}
		
		
	}

	static class Wrap implements CBookWrap {

		private int cnt;
		private CBookWidgetIF widget;
		private CBookLaunchData launchData;

		public Wrap(int cnt, CBookWidgetIF widget, CBookLaunchData launchData) {
			this.cnt = cnt;
			if(widget instanceof Proxy) {
				widget = ((Proxy) widget).getDelegate();
			}
			this.widget = widget;
			this.launchData = launchData;
		}

		public CBookWidgetInstanceIF getInstance(CBookContext context) {
			return widget.getInstance(context);
		}

		public CBookWidgetEditIF getEditor(CBookContext context) {
			CBookWidgetEditIF editor = widget.getEditor(context);
			editor.setLaunchData(launchData.getLaunchData());
			return editor;
		}

		public Icon getIcon() {
			return widget.getIcon();
		}

		public String getClassName() {
			return getWidgetName() + "[" + cnt + "]";
		}

		private String getWidgetName() {
			if( widget instanceof CBookWrap)
				return ((CBookWrap) widget).getClassName();
			return widget.getClass().getName();
		}

		public String toString() {
			return launchData.toString();
		}
	}

	private static void initialize() {
		classMap = new HashMap<String, CBookWidgetIF>();
	try {	
		URL codebase = WiskOpdr.applet.getCodeBase();
		//codebase = new URL("http://localhost:8888/dwo/widgets/");
		String index = "index.properties";
		String param = WiskOpdr.applet.getParameter("widget_index");
		if(param != null) index = param;
		URL url = new URL(codebase, index);
		System.out.println(url);
		Properties properties = new Properties();
		properties.load(url.openStream());
		String trusted = (String) properties.remove("Trusted");
		List<String> trust = Collections.EMPTY_LIST;
		if(trusted != null) 
			trust = Arrays.asList(trusted.split(" "));
		Set<Entry<Object, Object>> entries = properties.entrySet();
		ClassLoader sandbox = Thread.currentThread().getContextClassLoader();
		ClassLoader orig = Service.class.getClassLoader();
		for (Map.Entry<Object, Object> entry : entries) {
			try {
				ClassLoader parent;
				String clazz = entry.getKey().toString();
				boolean b = trust.contains(clazz);
				if( b)
					parent = orig;
				else
					parent = sandbox;
				parent = sandbox; // ALWAYS als experiment
				System.out.println("reading " + clazz + ": " + entry.getValue() + (b? " Trusted": " Sandbox"));
				StringTokenizer st = new StringTokenizer(entry.getValue().toString());
				int count = st.countTokens();
				URL[] urls = new URL[count];
				for(int i = 0; i < count; i++) {
					urls[i] = new URL(url, st.nextToken());
				}
				
				parent = new ParentLoader(parent, b);
				
				URLClassLoader loader = new URLClassLoader(urls, parent)
				{
					final AllPermission all = new AllPermission();
					@Override
					protected PermissionCollection getPermissions(
							CodeSource codesource) {
						PermissionCollection r = super.getPermissions(codesource);
						r.add(all);
						return r;
					}				
				}
				;
				//CBookWidgetIF widget = 
			    CBookWidgetIF widget = new Proxy(clazz, loader);
				//System.out.println("Loaded  " + widget );
				classMap.put(clazz, widget);
				
			} 
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
	} catch(Exception e) {
		e.printStackTrace();
	}
		
		
	}

	private static void put(CBookWidgetIF widget ) {
//System.err.println("put " + widget.getClass());
		classMap.put(widget.getClass().getName(), widget);
	}
	
	
	static private Map<String,CBookWidgetIF> classMap;
	private static final CBookContext context = new CBookContext() {

		public Object getProperty(String key) {
			if("locale".equals(key))
				return WiskOpdr.language;
			
			return null;
		}};

		
	public static Collection<CBookWidgetIF> getWidgets(int setNr) {
		if (classMap == null) initialize();
		switch(setNr) {
		case TekstInteractiePanelVak.CindySetNr:
			return singleton(widgetForName("de.cinderella.CindyWidget"));
		case TekstInteractiePanelVak.ESlateSetNr:
			return singleton(widgetForName("widgetESlate.ESlateWidget"));
		}
		Collection<CBookWidgetIF> values = classMap.values();
		Iterator<CBookWidgetIF> iterator = values.iterator();
		values = new TreeSet(new WidgetComparator());
		while (iterator.hasNext()) {
			CBookWidgetIF cBookWidgetIF = (CBookWidgetIF) iterator.next();
			values.addAll( singleton(cBookWidgetIF));
		}
		return values;
	}

	private static Collection<CBookWidgetIF> singleton(
			CBookWidgetIF widget) {
		if(widget == null) return Collections.emptySet();
		if(widget instanceof CBookWidgetFactoryIF)
		{
			Collection<CBookWidgetIF> result = new ArrayList<CBookWidgetIF>();
			Collection<CBookLaunchData> collection = ((CBookWidgetFactoryIF) widget).getInitialLaunchData(context );
			int cnt = 1;
			if (collection == null || collection.isEmpty())
			{	result.add(widget);
			}
			else
			for (Iterator<CBookLaunchData> iterator = collection.iterator(); iterator.hasNext();) {
				CBookLaunchData launchData =  iterator.next();
				result.add(new Wrap(cnt++,widget, launchData));
			}
			return result;
		}
		
		
		return Collections.singleton(widget);
	}

	public static CBookWidgetIF widgetForName(String widgetname) {
//System.err.println("widgetForName " + widgetname);
		if(classMap == null) initialize();
		int haak = widgetname.indexOf('[');
		if(haak > 0) widgetname = widgetname.substring(0, haak);
		CBookWidgetIF widget = classMap.get(widgetname);
		if(widget == null) {
			try {
				widget = (CBookWidgetIF) Class.forName(widgetname).newInstance();
				put(widget);
			} catch (ClassNotFoundException cnfe) {
				System.err.println(cnfe);
			} catch (Exception e) {
				e.printStackTrace();
				// widget = new ErrorWidget(widgetname); // XXX wat nu...
				//throw new IllegalArgumentException(e);   // voorlopig geen runtime exception
			} 
			
		}
		return widget;
	}

	public static String getClassName(CBookWidgetIF widget) {
		String name = widget.getClass().getName();
		if(widget instanceof CBookWrap)
			name =  ((CBookWrap) widget).getClassName();
		int haak = name.indexOf('[');
		if(haak > 0) name = name.substring(0, haak);
		return name;
	}

}
