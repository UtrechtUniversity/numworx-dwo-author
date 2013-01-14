package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;
import java.util.*;

import javax.swing.JApplet;

import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.*;
import logotekenap.*;

public class JavaLogoWeb extends JApplet implements ScormAppletIF, WiskOpdrParamEditApplet
{
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	private Tekenblad tekenblad;
	private Rekenblad rekenblad;
	private boolean rekenApplet;
	
	public static Image editImage;
	
	
	public static void main(String[] args)    
	{	int width = 900;
        int height = 600;
		ScormMainFrame mf = new ScormMainFrame(new JavaLogoWeb(),width, height);
		mf.setTitle("JavaLogoWeb");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public JavaLogoWeb()
	{	Locale language = new Locale ("nl", "");
		//applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	public JavaLogoWeb(Locale language)
	{	
		//applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	public void init() 
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		setLayout(null);
		
		URL url = this.getCodeBase();
		if(url!=null && (url.getHost().equals("www.informatica-actief.stoas.nl"))){
			System.out.println(url.toString());
			System.out.println(url.getHost());
		}
		//else return;
		
		
		//instelling taal
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.javalogoweb.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(230,240,255);
		String kleurcode = getParameter("bgcolor");
		//if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		getContentPane().setBackground(bgcolor);
		setBackground(bgcolor);
		
		//instelling rekenApplet/tekenApplet
		String rekenAppletString = getParameter("rekenApplet");
		if ( rekenAppletString != null && rekenAppletString.equals("true")) rekenApplet = true;
				
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("JavaLogoWeb",new String[]
			{	"versie-info: ...",
				"auteur: ...",
				"programmeur: ...",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(0,0,20,30);
		//add(fiButton);
		
		AppletUtil au = new AppletUtil(this);
		editImage = au.getImage("resources/edit.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(editImage,0);
		try{tr.waitForAll();} catch(Exception e) {}
				
		tekenblad = new Tekenblad(this);
		tekenblad.setBounds(420, 10, getSize().width-431, getSize().height-71);
		add(tekenblad);
		
		rekenblad = new Rekenblad(this);
		rekenblad.setBounds(620, 10, getSize().width-631, getSize().height-171);
		//add(rekenblad);
		
		/*javaLogoSchuifVeld = new JavaLogoSchuifVeld(1, 1, 618, getSize().height-2, tekenblad);
		javaLogoSchuifVeld.setBackground(getBackground());
		add(javaLogoSchuifVeld);
		javaLogoSchuifVeld.initialize();*/
		
		javaLogoSchuifVeld = new JavaLogoSchuifVeld(1, 1, 618, getSize().height-2, tekenblad);
		javaLogoSchuifVeld.setBackground(getBackground());
		add(javaLogoSchuifVeld);
		javaLogoSchuifVeld.initialize();
		
		/*TraceBeheerder trb = new TraceBeheerder( tekenblad,null);
		trb.setBounds(418,getSize().height-59,getSize().width-419,58);
		trb.setBackground(getBackground());
		trb.addActionListener(javaLogoSchuifVeld);
		add(trb);*/
		
		TraceBeheerder trb = new TraceBeheerder( tekenblad,null);
		trb.setBounds(618,getSize().height-59,getSize().width-619,58);
		trb.setBackground(getBackground());
		trb.addActionListener(javaLogoSchuifVeld);
		add(trb);
		
		Label versieLabel = new Label("v20100620");
		versieLabel.setFont(new Font("SansSerif",Font.PLAIN,10));
		versieLabel.setBounds(getSize().width-60,getSize().height-20,60,15);
		add(versieLabel,0);
		
		tekenblad.meldTraceBeheerder(trb);
		//trb.naarBegin();
		
		
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
	{	return new InteractiePanelAdapter(this);
	}
	
	public void tekenprogramma()
	{	javaLogoSchuifVeld.teken(tekenblad);
		
	}
	
	public void rekenprogramma()
	{	javaLogoSchuifVeld.reken(rekenblad);
		
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
		
		javaLogoSchuifVeld.setCode(code);
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
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
    
    public Parameter[] getEditableParameters()
	{	return null;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }
    
}
