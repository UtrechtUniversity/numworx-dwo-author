package fi.javalogoweb;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;
import java.util.*;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.*;
import logotekenap.*;

public class JavaLogoWeb extends Applet implements ScormAppletIF
{
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private JavaLogoSchuifVeld javaLogoSchuifVeld;
	private Tekenblad tekenblad;
	
	public static Image editImage;
	
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
		ScormMainFrame mf = new ScormMainFrame(new JavaLogoWeb(),width, height);
		mf.setTitle("JavaLogoWeb");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
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
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
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
		
		javaLogoSchuifVeld = new JavaLogoSchuifVeld(1, 1, 418, getSize().height-2, tekenblad);
		javaLogoSchuifVeld.setBackground(getBackground());
		add(javaLogoSchuifVeld);
		javaLogoSchuifVeld.initialize();
		
		TraceBeheerder trb = new TraceBeheerder( tekenblad,null);
		trb.setBounds(418,getSize().height-59,getSize().width-419,58);
		trb.addActionListener(javaLogoSchuifVeld);
		add(trb);
		
		tekenblad.meldTraceBeheerder(trb);
		//trb.naarBegin();
		
		
	}
	
	public void tekenprogramma()
	{	javaLogoSchuifVeld.teken(tekenblad);
		
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
	
	public void paint(Graphics g) 
	{	super.paint(g);
		g.setColor(Color.black);
		g.drawRect(0,0,getSize().width-1, getSize().height-1);
		//g.drawLine(419, getSize().height-60, getSize().width, getSize().height-60);
	
	}
	
	public void setState(String s)
	{	//decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		//haal de data uit de hashtabel
		String text = (String)h.get("text");
		
	    //herstel de state van het applet
	    //textField.setText(text);
	}
	
	public String getState()
	{	String text = null;
	
		//vraag de gegevens op die de state bepalen
	    //text = textField.getText();
	    
	    Hashtable h = new Hashtable();
	    
	    //voeg de gegeven toe aan de hashtable
	    //h.put("text", text);
	      
	    //codeer de hashtable tot string
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
