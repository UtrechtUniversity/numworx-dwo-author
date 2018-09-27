package nl.numworx.samllogin;

import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import fi.beans.browser.PrintStreamConsole;
import fi.beans.browser.SimpleSwingBrowser;
import fi.beans.browser.Status;
import fi.previewhtml.DefaultAPI;

public class SamlLoginPanel {
	
	private static final class PrintStatus implements Status {
		@Override
		public void showStatus(String message) {
			System.out.println(message);
		}
	}

	/**
	 * capture 
	 * @author wim
	 *
	 */
  public static class API extends DefaultAPI
  {
    private Properties map = new Properties();
    private Deferred<Properties> defer = new Deferred<>();
    @Override
    public String LMSSetValue(String key, String value) {
      map.setProperty(key, value);
      return super.LMSSetValue(key, value);
    }

    @Override
    public String LMSFinish(String iParam) {
      defer.resolve(map);
      return super.LMSFinish(iParam);
    }

    public Promise<Properties> getPromise() {
        return defer.getPromise();
    }
  }
	
	
	public static void main(String[] args) {
		
		final JFrame f = new JFrame("Login uu-dev");
		SimpleSwingBrowser.debug = true;
		SimpleSwingBrowser browser = new SimpleSwingBrowser();
		browser.setConsole(new PrintStreamConsole());
		API api = new API();
		api.getPromise().then( p -> {
		    p.getValue().store(System.out, "Login succeeded");
		    SwingUtilities.invokeLater(f::dispose);
		    return null;
		});
		
        browser.setApi(api);
		browser.setStatus(new PrintStatus());
		f.setContentPane(browser);
		
		f.pack();
		f.setVisible(true);
		browser.loadURL("https://uu-dev.dwo.nl/dwo/snoop");
        //browser.loadURL("http://localhost:8080/dwo/saml/login.jsp");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
