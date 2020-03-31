package nl.numworx.samllogin;

import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import fi.beans.browser.Console;
import fi.beans.browser.SimpleSwingBrowser;
import fi.beans.browser.Status;
import fi.beans.scorm.SAMLLoginIF;
import fi.previewhtml.DefaultAPI;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;

@SuppressWarnings("serial")
public class SamlLoginPanel extends SimpleSwingBrowser implements SAMLLoginIF {
    
    static final Logger LOG = Logger.getLogger(SamlLoginPanel.class.getName());
  
    static SwingBrowserProvider provider = new SwingBrowserProvider();
    
    public static final class PrintStatus extends Console implements Status {
        @Override
        public void showStatus(String message) {
            LOG.fine(message);
        }

      @Override
      public void log(String object) {
            LOG.info(object);
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
    
    void cleanup() {
      if (SwingUtilities.isEventDispatchThread()) {
        removeMembers();
      } else {
        SwingUtilities.invokeLater(this::cleanup);
      }
    }
  
    public Promise<Properties> getPromise() {
      Promise<Properties> result = api.getPromise().map(this::getCookie);
      result.onResolve(this::cleanup);
      return result;
    }

    public Properties getCookie(Properties p) {
      String cookie = p.getProperty("dme.cookies");
      if(cookie != null) {
        for ( String item : cookie.split(";"))  {
          String[] pair = item.trim().split("=", 2);
          p.setProperty(pair[0], pair[1]);
        }
      }
      return p;
    }
    
    @Override
	public void loadURL(String url) {
		super.loadURL(url);
	}

	public SamlLoginPanel(String url) {
      this();
      loadURL(url);
    }
    API api = new API();

  public SamlLoginPanel() {
	super(provider.getFactory());
    newSession();
    PrintStatus status = new PrintStatus();
    setConsole(status);
    setApi(api);
    setStatus(status);
    setSize(320,446); // UU 
    setPreferredSize(getSize());
    setMinimumSize(getSize());
  }
    
  public JComponent asComponent() {
    return this;
  }
}


