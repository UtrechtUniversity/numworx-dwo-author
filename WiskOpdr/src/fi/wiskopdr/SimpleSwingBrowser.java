package fi.wiskopdr;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;

import nl.numworx.swingbrowser.api.ConsoleEvent;
import nl.numworx.swingbrowser.api.RefreshEvent;
import nl.numworx.swingbrowser.api.StatusEvent;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;
import nl.numworx.swingbrowser.scorm.SCORM2004APIInterface;

//@SuppressWarnings("restriction")
public class SimpleSwingBrowser implements WindowListener, SCORM2004APIInterface {
  
    public static final SwingBrowserProvider BROWSER_PROVIDER = new SwingBrowserProvider();
    final protected SwingBrowser browser;
	private Component observer;
	
	public void setRepaintObserver(Component c) {
	  observer = c;
	}
	protected void repaint(RefreshEvent e) {
	  if (observer != null) 
	    observer.repaint();
	}
	
	public void dispose() {
	  try {
        browser.close();
      } catch (IOException e) {
      }
	}
	
	public SimpleSwingBrowser() {
		browser = BROWSER_PROVIDER.getFactory().newBrowser();
		browser.addTitleListener(ev -> setTitle(ev.getTitle()));
		browser.addRefreshListener(this::repaint);
		browser.addConsoleListener(this::console);
		browser.addStatusListener(this::status);
		browser.setAPI(this);
	}

	public JComponent getBrowserPanel() {
		return browser.asComponent();
	}
	
	void setTitle(String newValue) {
		if (frame != null) {
			frame.setTitle(newValue);
		}
	}

	public void loadURL(final String url) {
	  browser.loadURL(url);
	}

	public void loadContent(final String content, final String type) {
	  browser.loadContentAndWait(content, type);
	}

	public void loadContent(String content) {
		loadContent(content, "text/html");
	}

	private static String toURL(String str) {
		try {
			return new URL(str).toExternalForm();
		} catch (MalformedURLException exception) {
			return null;
		}
	}

	JFrame frame;

	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			frame.setContentPane(getBrowserPanel());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // for fire and forget
			frame.setPreferredSize(new Dimension(1024, 600));
			frame.addWindowListener(this); // displ
		}
		return frame;
	}

	public void setSize(int width, int height) {
		getFrame().setSize(width, height);
	}

	public void setVisible(boolean b) {
		getFrame().setVisible(b);
	}

  @Override
  public void windowActivated(WindowEvent arg0) {
  }
  @Override
  public void windowClosed(WindowEvent arg0) {
  }

  @Override
  public void windowClosing(WindowEvent ev) {
    dispose();
  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
  }
  @Override
  public void windowDeiconified(WindowEvent arg0) {
  }
  @Override
  public void windowIconified(WindowEvent arg0) {
  }
  @Override
  public void windowOpened(WindowEvent arg0) {
  }

  public void actionPerformed(ActionEvent actionEvent) {
    if(browser instanceof ActionListener) {
      ((ActionListener) browser).actionPerformed(actionEvent);
    }
    
  }
private void console(ConsoleEvent e) {
	System.err.println(e.getMessage());
}
private void status(StatusEvent statusevent1) {
	System.err.println("STATUS: " + statusevent1.getStatus());
}
@Override
public String Initialize(String dummy) {
	return "true";
}
@Override
public String Commit(String dummy) {
	return "true";
}
@Override
public String Terminate(String dummy) {
	return "true";
}
@Override
public String GetValue(String key) {
	System.out.println("GetValue "+ key);
	return "";
}
@Override
public String SetValue(String key, String value) {
	System.out.println("SetValue "+ key + "=" + value);
	return "true";
}
@Override
public String GetLastError() {
	return "0";
}
@Override
public String GetDiagnostic(String iErrorCode) {
	return "";
}
@Override
public String GetErrorString(String iErrorCode) {
	return "";
}

  
  
}