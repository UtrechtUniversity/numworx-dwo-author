package nl.numworx.samllogin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JFrame;

import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.util.promise.Promise;

public class OsgiMain {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		disableSecurity();
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
	    panel.setEndpoint("/dwo/oauth2/entree");
	    panel.getPromise().then(OsgiMain::succes, OsgiMain::failed);//.onResolve(() -> System.exit(0));
	    frame.setContentPane(panel);
	    panel.loadURL("https://test.dwo.nl/dwo/oauth2/login3.jsp?idphint=conext");
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
	

	static void disableSecurity() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
		    new X509TrustManager() {     
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
		            return new X509Certificate[0];
		        } 
		        public void checkClientTrusted( 
		            java.security.cert.X509Certificate[] certs, String authType) {
		            } 
		        public void checkServerTrusted( 
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    } 
		}; 

		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL"); 
		    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		} 
		
	}
	
	
	
}
