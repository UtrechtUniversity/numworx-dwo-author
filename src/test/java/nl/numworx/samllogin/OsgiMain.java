package nl.numworx.samllogin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.swing.JFrame;

import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.util.promise.Promise;

public class OsgiMain {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		FrameworkFactory factory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
	    Map<String, String> map = new HashMap<>();
	    System.setProperty("org.osgi.service.http.port", "8686");
	    System.setProperty("felix.log.level", "4");
	    Framework framework = factory.newFramework(map);
	    framework.init();
	    framework.start();
	    BundleContext context = framework.getBundleContext();
	    JFrame frame = new JFrame("preview extern");
	    new Activator().start(context);
	    
	    SamlLoginPanel panel = new SamlLoginPanel();
	    panel.getPromise().then(OsgiMain::succes, OsgiMain::failed);
	    frame.setContentPane(panel);
	    panel.loadURL("http://localhost:8080/dwo/saml/login3.jsp");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.show();
	}

	static Promise<Properties> succes(Promise<Properties> p) throws InvocationTargetException, InterruptedException, IOException { 
		p.getValue().store(System.out, "properties");
		return p; }

	static void failed(Promise<?> p) throws InterruptedException {
		p.getFailure().printStackTrace();		
	}
	
}
