package fi.wiskopdr;

import java.awt.Component;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JFrame;

import nl.numworx.swingbrowser.api.RefreshEvent;
import nl.numworx.swingbrowser.api.SwingBrowser;
import nl.numworx.swingbrowser.api.SwingBrowserProvider;

//@SuppressWarnings("restriction")
public class SimpleSwingBrowser {
  
    private static final SwingBrowserProvider BROWSER_PROVIDER = new SwingBrowserProvider();
    final private SwingBrowser browser;
	private Component observer;
	
	public void setRepaintObserver(Component c) {
	  observer = c;
	}
	private void repaint(RefreshEvent e) {
	  if (observer != null) 
	    observer.repaint();
	}
	
	public SimpleSwingBrowser() {
		browser = BROWSER_PROVIDER.getFactory().newBrowser();
		browser.addTitleListener(ev -> setTitle(ev.getTitle()));
		browser.addRefreshListener(this::repaint);
	}

	public void setAdressFieldVisible(boolean b) {
	}

	public void setStatusBarVisible(boolean b) {
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
	  browser.loadContent(content, type);
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

	private JFrame frame;

	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame();
			frame.setContentPane(getBrowserPanel());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // for fire and forget
			frame.setPreferredSize(new Dimension(1024, 600));
			frame.pack();
		}
		return frame;
	}

	public void setSize(int width, int height) {
		getFrame().setSize(width, height);
	}

	public void setVisible(boolean b) {
		getFrame().setVisible(b);
	}

}