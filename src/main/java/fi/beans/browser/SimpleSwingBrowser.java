

package fi.beans.browser; 
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import fi.beans.scorm.SCORM12APIInterface;
import nl.numworx.swingbrowser.api.ConsoleEvent;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserFactory;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;
import nl.numworx.swingbrowser.scorm.ConsoleListener;

public class SimpleSwingBrowser extends JPanel implements Status, ConsoleListener {
    public static boolean debug;
    
    static final SwingBrowserProvider PROVIDER = new SwingBrowserProvider();
    private final SwingBrowserFactory FACTORY;    
    private final SwingBrowser browser;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JComponent jfxPanel;
 
	public JComponent getJfxPanel() {
		return jfxPanel;
	}

	public void newSession() {
	  FACTORY.newSession();
	}
	
	SCORM12APIInterface api;
	private Console console;
	private Status status = this;
 
    public SCORM12APIInterface getApi() {
		return api;
	}

	public void setApi(SCORM12APIInterface api) {
		this.api = api;
		browser.setAPI(new Scorm2004API(getApi()));
		browser.addConsoleListener(this);
	}

	public Console getConsole() {
		return console;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if(status == null) status = this; // Null Pattern
		this.status = status;
	}

	private final JLabel lblStatus = new JLabel();
 
	
	public SimpleSwingBrowser() {
		this(PROVIDER.getFactory());
	}
	
    public SimpleSwingBrowser(SwingBrowserFactory factory) {
        super(new BorderLayout());
        FACTORY = factory;
        browser = FACTORY.newBrowser();
        jfxPanel = browser.asComponent();
        initComponents();
        browser.addStatusListener(e -> status.showStatus(e.getStatus()));
    }

	private void initComponents() {
   
        lblStatus.setText("");
        
        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
 
        add(statusBar, BorderLayout.SOUTH);
        add(jfxPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(1024, 600));
    }
 
 
    public void loadURL(final String url) {
      browser.loadURL(url);
    }

	public void removeMembers() {
	  try {
        browser.close();
      } catch (IOException e) {
    	  Logger.getLogger(getClass().getName()).log(Level.WARNING, "removeMembers", e);
      }
	}
	
	
	public void showStatus(String message) {
		lblStatus.setText(message);
	}

  @Override
  public void onConsole(ConsoleEvent event) {
    Console c = console;
    if (c != null) {
      String m = event.getMessage();
      switch(event.getLevel()) {
        case LOG:
          c.log(m); break;
        case DEBUG: c.debug(m); break;
        case ERROR: c.error(m); break;
        case INFO: c.info(m); break;
        case WARN: c.warn(m); break;
      }
    }
    
  }

@Override
public void print(Graphics g) {
	super.print(g);
//	Image image = browser.toImage();
//	g.drawImage(image, 0, 0, null);
}

}

	