package fi.previewhtml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;

import fi.beans.browser.FilterAPI;
import fi.beans.browser.PrintStreamConsole;
import fi.beans.browser.SimpleSwingBrowser;
import fi.beans.browser.Status;
import fi.beans.copyright.FIButton;
import fi.beans.scorm.DataType;
import fi.beans.scorm.Parameter;
import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.scorm.Scorm;
import fi.beans.scorm.ScormAppletIF;
import fi.beans.scorm.ScormBoolean;
import fi.beans.scorm.ScormEditComponentIF;
import fi.beans.scorm.ScormString;

public class PreviewHTML extends JApplet implements ScormAppletIF, ActionListener, Status, Printable {

	String locationOverride;
	boolean overridden;
	
    final class NotifyConsole extends PrintStreamConsole {
		@Override
		public void debug(Object msg) { // Covert channel
			super.debug(msg);
			synchronized(browser) {
				overridden = true;
				browser.notifyAll();
			}
		}
	}
	public final class MyFilterAPI extends FilterAPI  implements SCORM12APIInterface {
		public MyFilterAPI(SCORM12APIInterface api) {
			super(api);
		}

		@Override
		public String LMSInitialize(String iParam) {
//			System.out.println("Initialize");
			final String lmsInitialize = "true" ; // NO super.LMSInitialize(iParam);
			synchronized( browser ) {
				inited = true;
				browser.notifyAll();
			}
			return lmsInitialize;
		}

		@Override
		public String LMSFinish(String iParam) {
//			System.out.println("Finish");
			super.LMSCommit(iParam); // No finish
			synchronized(browser ) {
				inited = false;
				browser.notifyAll();
			}
			return "true";
		}

		
		
		@Override
		public String LMSGetValue(String iDataModelElement) {
			if ("cmi.location".equals(iDataModelElement) && locationOverride != null) {
				synchronized(browser) {
					//overridden = true;
					browser.notifyAll();
					return locationOverride;
				}
			}
			final String result = super.LMSGetValue(iDataModelElement);
//			System.out.println("Getvalue " + iDataModelElement + " > " + result);
			return result;
		}

		@Override
		public String LMSSetValue(String iDataModelElement, String iValue) {
//			System.out.println("Setvalue " + iDataModelElement + " = " + iValue);
			return super.LMSSetValue(iDataModelElement, iValue);
		}

		@Override
		public String LMSGetLastError() {
			return "0";	 // No error
		}
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SCORM12APIInterface api;
	private String url;
	private JTextField textField;
	private SimpleSwingBrowser browser;
	
	transient private boolean inited;		
	

	private boolean debug = true;

	private JButton startKnop;

	private JButton stopKnop;

	@Override
	public void init() {
		debug = "true".equals(getParameter("debug"));
		SimpleSwingBrowser.debug = debug;
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		if(api == null)
			api = new DefaultAPI();
		Container content = getContentPane();
//instelling taal
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = Locale.forLanguageTag(langArg);
		//rb = ResourceBundle.getBundle("fi.popupurlapplet.text.Text",language);
		
//instelling achtergrondkleur
		Color bgcolor = new Color(255,255,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)
			bgcolor = Color.decode(kleurcode);
		content.setBackground(bgcolor);
		content.setLayout(new BorderLayout(2,2));
		
		url = getParameter("url"); 
		//url = "file:///users/wim/klad.html";
		//url="http://app.dwo.nl/dwo/apps/player.html#211367";
		content = Box.createHorizontalBox();
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("PreviewHTML",new String[]
			{	"versie-info: 20190902",
				"auteur: Wim van Velthoven",
				"programmeur: Wim van Velthoven",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(0,0,20,30);
		fiButton.setPreferredSize(fiButton.getSize());
		content.add(fiButton);
		
		//Test-textfield
		textField = new JTextField();
		textField.addActionListener(this);
		textField.setBounds(50,100,200,25);
        textField.setText(url);
		if (debug)
			content.add(textField);
		
		startKnop = new JButton("Start");
		startKnop.setBounds(30,50,60,25);
		startKnop.addActionListener(this);
		if (debug)
			content.add(startKnop);
		
		stopKnop = new JButton("Stop");
		stopKnop.setBounds(130,50,60,25);
		stopKnop.addActionListener(this);
		if (debug)
			content.add(stopKnop);
		if (debug)
		getContentPane().add(content, BorderLayout.NORTH);
		
		browser = new SimpleSwingBrowser();
		browser.setConsole(new NotifyConsole());
		
		browser.setApi(new MyFilterAPI(api));
		
		getContentPane().add(browser, BorderLayout.CENTER);
		
	}

	@Override
	public void start() {
		if(!debug)
		{
			start0();
		}
	}

	private void start0() {
		if(browser.getApi() == null) {
			browser.setApi(new MyFilterAPI(api));
		}
		if (browser.getConsole() == null) {
			browser.setConsole(new NotifyConsole());
		}
		browser.loadURL(url);
	}

	@Override
	public void stop() {
		SimpleSwingBrowser local = browser;
		if (local == null) return;
		local.loadURL(null);
		int cnt = 10;
		synchronized(local) {
			while( cnt-- > 0 && inited )
				try {
					local.wait(10000);
				} catch (InterruptedException e) {
				}
		}
        local.setApi(null);
        local.setConsole(null);
		System.out.println("Preview stopped " + cnt);
	}

	@Override
	public void destroy() {
		SimpleSwingBrowser local = browser;
		if (local != null) local.removeMembers();
		removeAll();
		browser = null;
		System.gc();
	}

	public String getState() {
		return "";
	}

	public void setState(String state) {
	}

	public void stopSco() {
	}

	public boolean hasEditMode() {
		return false;
	}

	public ScormEditComponentIF getEditComponent(Hashtable launchdata) {
		return null;
	}

	public Parameter[] getEditableParameters()
	{	Parameter[] parameters = new Parameter[2]; 
		DataType type;
		Parameter param;	
		type = new ScormString();
		param = new Parameter("url", "Url", type);
		parameters[0] = param;
		type = new ScormBoolean();
		param = new Parameter("debug", "Debug", type);
		parameters[1] = param;
		return parameters;
	}
	
    public Parameter[] getAllParameters()
    {	Parameter[] parameters = new Parameter[4]; 
		DataType type;
		Parameter param;	
		type = new ScormString();
		param = new Parameter("language", "Taal", type);
		parameters[0] = param;
		type = new ScormString();
		type.setSize(50);
		param = new Parameter("bgcolor", "Achtergrondkleur", type);
		parameters[1] = param;
		type = new ScormString();
		type.setSize(100);
		param = new Parameter("url", "Url", type);
		parameters[2] = param;
		type = new ScormBoolean();
		param = new Parameter("debug", "Debug", type);
		parameters[3] = param;
    	return parameters;
	}

	public void actionPerformed(ActionEvent e) {
		url = textField.getText();
		if(e.getSource() == stopKnop) stop();
		else
			browser.loadURL(url);
	}

	Html5Print html5;
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D g2d = (Graphics2D)graphics;
	    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	    
	    if(html5 == null) {
	    	html5 = new Html5Print(api);
	    	html5.init();
	    }
	    
	    if(pageIndex < html5.aantalOpdrachten) {
	    	String location = String.valueOf(pageIndex);
	    	if (!location.equals(locationOverride)) {
	    		locationOverride = location;
	    		stop(); // switch to correct page.
	    		overridden = false;
	    		start0(); // wait????
	    		synchronized(browser) { while(!inited || !overridden)
					try {
						browser.wait(1000L);
					} catch (InterruptedException e) {
					} }
	    		try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
				}
	    	}
	    	
	    	
		    double width = browser.getWidth();
		    double pageWidth = pageFormat.getImageableWidth();
		    double sx = pageWidth/width; sx = Math.min(1, sx);
//	    	browser.setSize(1024, (int) (pageFormat.getImageableHeight()/sx)); // ????
//	    	browser.invalidate();
			g2d.scale(sx, sx);
	    	browser.print(g2d);
	    	return PAGE_EXISTS;
	    }
	    browser.invalidate();
		return Printable.NO_SUCH_PAGE;
	}

	
}
