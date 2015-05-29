package fi.javalogoweb;

import java.awt.*;
import java.net.URL;
import java.util.*;

import javax.swing.JApplet;

import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.*;
import logotekenap.*;

public class JavaLogoWeb extends JApplet implements WiskOpdrApplet
{
	protected SCORM12APIInterface api;
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	private Uitvoerblad uitvoerblad;
	
	//public static Image editImage;
	public static final Font defaultfont = new Font("Calibri", Font.PLAIN, 12);
	public static final Font boldfont = new Font("Verdana", Font.BOLD, 12);
	
	public static ResourceBundle rb;
	
	
	public static void main(String[] args)    
	{	int width = 1160;
        int height = 650;
		ScormMainFrame mf = new ScormMainFrame(new JavaLogoWeb(),width, height);
		mf.setTitle("JavaLogoWeb");
		mf.pack();
		mf.setVisible(true);
		mf.setSize(width, height);
	}
	
	public JavaLogoWeb()
	{	Locale language = new Locale ("nl", "");
		// applet=this;
	}
	
	public JavaLogoWeb(Locale language)
	{	
		//applet=this;
		rb = ResourceBundle.getBundle("fi.javalogoweb.text.Text",language);
	}
	
	public void init() 
	{	
		setLayout(null);
		
		String langArg = getParameter("language");
		if (langArg == null) langArg = "en";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.javalogoweb.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = Color.white;//new Color(230,240,255);
		getContentPane().setBackground(bgcolor);
		setBackground(bgcolor);
	/* no explicit edit button needed anymore...			
		AppletUtil au = new AppletUtil(this);
		editImage = au.getImage("resources/edit.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(editImage,0);
		try{tr.waitForAll();} catch(Exception e) {}
		
		uitvoerblad = new Tekenblad(this);
		uitvoerblad.setBounds(620, 10, getWidth()-631, getHeight()-71);
		add(uitvoerblad);
			
		javaLogoSchuifVeld = new JavaLogoSchuifVeld(0, 0, 618, getHeight()-2, uitvoerblad);
		javaLogoSchuifVeld.setBackground(getBackground());
		add(javaLogoSchuifVeld);
		javaLogoSchuifVeld.initialize();
			
		TraceBeheerder trb = new TraceBeheerder( (Tekenblad)uitvoerblad, javaLogoSchuifVeld);
		trb.setBounds(620, getHeight()-59, getWidth()-631, 58);
		trb.setBackground(getBackground());
		trb.addActionListener(javaLogoSchuifVeld);
		add(trb);
			
		uitvoerblad.meldTraceBeheerder(trb);
		//trb.naarBegin();
	*/
		JavaLogoInteractiePanel jlip = new JavaLogoInteractiePanel();
		jlip.setBounds(0,0,1160,600);
		add(jlip);
	}
	
	public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();
    	/*
    	h.put("varWaarde","true");
    	h.put("expWaarde","true");
    	h.put("zoomOptie","true");
    	h.put("constructieTools","true");
    	h.put("variabeleX","true");
    	h.put("variabeleY","false");
    	h.put("variabeleZ","false");
    	h.put("lineaal","true");
    	h.put("resultaatVak","true");
    	*/
    	return h;
    }
	
	public void setSingleComponent()
	{	
	}
	
	public InteractiePanel getInteractiePanel()
	{	return null;
	}
	
	public void execprogramma()
	{	javaLogoSchuifVeld.execute(uitvoerblad);
		
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
	
	/*public void paintComponent(Graphics g) 
	{	//super.paint(g);
		g.setColor(getBackground());
		g.fillRect(0,0,getSize().width-1, getSize().height-1);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1, getSize().height-1);
		//g.drawLine(419, getSize().height-60, getSize().width, getSize().height-60);
	
	}*/
	
	public void setState(String s)
	{	Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		String code = "";
		
		if(h.containsKey("code")) code = (String)h.get("code");
		
		//javaLogoSchuifVeld.setCode(code);
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
