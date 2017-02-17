package fi.javalogoweb3d;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.*;

import javax.swing.JApplet;

import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.*;
//import fi.logotekenap3d.*;

public class JavaLogoWeb3d extends JApplet implements WiskOpdrApplet,  ComponentListener
{
	protected SCORM12APIInterface api;
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
//	private Uitvoerblad uitvoerblad;
	
	public static final Font defaultfont = new Font("Calibri", Font.PLAIN, 12);
	public static final Font boldfont = new Font("Verdana", Font.BOLD, 12);
	
	public static ResourceBundle rb;
	
	JavaLogoInteractiePanel jlip;
	
	public static void main(String[] args)    
	{	int width = 1168;
        int height = 627;
		ScormMainFrame mf = new ScormMainFrame(new JavaLogoWeb3d(),width, height);
		mf.setTitle("JavaLogoWeb3d");
		mf.pack();
		mf.setVisible(true);
		mf.setSize(width, height);
	}
	
	public JavaLogoWeb3d()
	{	Locale language = new Locale ("nl", "");
	}
	
	public JavaLogoWeb3d(Locale language)
	{	rb = ResourceBundle.getBundle("fi.javalogoweb3d.text.Text",language);
	}
	
	public void init() 
	{	
		setLayout(null);
		
		String langArg = getParameter("language");
		if (langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.javalogoweb3d.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = Color.white;//new Color(230,240,255);
		getContentPane().setBackground(bgcolor);
		setBackground(bgcolor);
			
		addComponentListener(this);
		
		jlip = new JavaLogoInteractiePanel();
		jlip.setBounds(0,0,getWidth(),getHeight());
		add(jlip);
	}
	
	public void componentResized(ComponentEvent e) {
		jlip.setBounds(0,0,getWidth(),getHeight());

	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}
	
	public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();
    	return h;
    }
	
	public void setSingleComponent()
	{	
	}
	
	public InteractiePanel getInteractiePanel()
	{	return new JavaLogoInteractiePanel();
	}
	
	public void start()
	{	if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
		}
	
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}
	
	public void stop()
	{	if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	public void setState(String s)
	{	Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		String code = "";
		if(h.containsKey("code")) code = (String)h.get("code");
		
	}
	
	public String getState()
	{	String code = "";
	
		code = javaLogoSchuifVeld.getCode();
		 
	    Hashtable h = new Hashtable();
	    h.put("code", code);
	    
	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public double getScore()
	{	return 0.5;
	}
    
}
