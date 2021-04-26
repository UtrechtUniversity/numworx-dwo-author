package nl.numworx.samllogin;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
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
    static BiConsumer<SamlLoginPanel, API> strategy = (p, api) -> p.setApi(api);

    /** for use elsewhere 
     * 
     * @param targetStringLength length
     * @return randomstring
     */
    String randomAlphanumericString(int targetStringLength) {
  	    int leftLimit = 48; // numeral '0'
  	    int rightLimit = 122; // letter 'z'
  	    ThreadLocalRandom random = ThreadLocalRandom.current();
  	 
  	    String generatedString = random.ints(leftLimit, rightLimit + 1)
  	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
  	      .limit(targetStringLength)
  	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
  	      .toString();
  	 
  	    return(generatedString);
  	}

    
    public final class PrintStatus extends Console implements Status {
        @Override
        public void showStatus(String message) {
            LOG.fine(message);
        }

      @Override
      public void log(String object) {
            LOG.info(object);
      }

		@Override
		public void debug(Object msg) {
			LOG.info(Objects.toString(msg, "debug message null"));
			
			if (msg != null ) { 
				String message = (String) msg;
				if (message.startsWith("NavigationRedirected:"))
					message = message.substring(21);
				if (last.startsWith(message) && last.length() > message.length()) {
					SamlLoginPanel.super.loadURL(last);
				}
			}
		}
      
    }

    /**
     * capture 
     * @author wim
     *
     */
  public static class API extends DefaultAPI implements PropertyChangeListener
  {
    private Properties map = new Properties();
    private Deferred<Properties> defer = new Deferred<>();
    @Override
    public String LMSSetValue(String key, String value) {
      map.setProperty(key, value);
      return super.LMSSetValue(key, value);
    }

    @Override
    public String LMSGetValue(String key) {
    	return map.getProperty(key, "");
    }
    
    @Override
    public String LMSFinish(String iParam) {
      defer.resolve(map);
      return super.LMSFinish(iParam);
    }

    public Promise<Properties> getPromise() {
        return defer.getPromise();
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("Terminate".equals(evt.getPropertyName()))
			LMSFinish((String) evt.getOldValue());
		else if (evt.getNewValue() instanceof String)
			LMSSetValue(evt.getPropertyName(), (String) evt.getNewValue());			
	}
  }

	private boolean inited = true;
    
    void cleanup() {
      if (SwingUtilities.isEventDispatchThread()) {
  		super.loadURL(null);
  		int cnt = 3;
  		synchronized(this) {
  			while( cnt-- > 0 && inited )
  				try {
  					wait(1000);
  				} catch (InterruptedException e) {
  				}
  		}
        setApi(null);
        setConsole(null);
        removeMembers();
      } else {
        SwingUtilities.invokeLater(this::cleanup);
      }
    }
  
    public Promise<Properties> getPromise() {
      Promise<Properties> result = api.getPromise().map(this::getCookie);
      result = result.then(p -> { cleanup(); return p; }, p -> cleanup());
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
    String extra = "";

	private String last;    

    @Override
	public void loadURL(String url) {
    	URI u = URI.create(url);
    	String login = u.resolve("/dwo/saml/login").toString();
    	api.LMSSetValue("dme.oauth.endpoint", login);
		super.loadURL(last = url + extra);
	    getJfxPanel().setName("Aanmelden");
	}

	public SamlLoginPanel(String url) {
      this();
      loadURL(url);
    }
    API api = new API();
    Color COLOR15 = new Color(49,71,112);   // color 15 (color of menu text, body text)

  public SamlLoginPanel() {
	super(provider.getFactory());
    newSession();
    PrintStatus status = new PrintStatus();
    setConsole(status);
    strategy.accept(this, api);
    setStatus(status);
    setSize(320,446); // UU 
    setPreferredSize(getSize());
    setMinimumSize(getSize());
    setBackground(COLOR15);
// voor Midden-03

    api.LMSSetValue("dme.oauth._children", "client_id,code_challenge,endpoint,state");
    api.LMSSetValue("dme.oauth.client_id", "5493fd2c-d09a-11ea-87d0-0242ac130003");
    String verifier = randomAlphanumericString(64);
	api.LMSSetValue("dme.oauth.code_verifier", verifier);
	MessageDigest digest = null;
	try {
		digest = MessageDigest.getInstance("SHA-256");
	} catch (NoSuchAlgorithmException e) {
		
	}
	byte[] encodedhash = digest.digest(
	  verifier.getBytes(StandardCharsets.UTF_8));
	String challenge = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(encodedhash);
	api.LMSSetValue("dme.oauth.code_challenge", challenge);    
  
  }
    
  public JComponent asComponent() {
    return this;
  }
}


