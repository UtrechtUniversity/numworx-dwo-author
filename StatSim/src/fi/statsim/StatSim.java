package fi.statsim;

import java.awt.*;
import java.applet.*;
import java.util.*;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.*;
import fi.beans.base64code.*;

public class StatSim extends Applet implements ScormAppletIF, WiskOpdrApplet { 

	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private StatSimInteractiePanel interactiePanel;
	
	public static void main(String[] args) {	
		int width = 790;
        int height = 450;
		ScormMainFrame mf = new ScormMainFrame(new StatSim(),width, height);
		mf.setTitle("StatSim");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public StatSim(Locale language) {	
		rb = ResourceBundle.getBundle("fi.statsim.text.Text",language);

		//Locale language1 = new Locale ("en", "");
		//rb = ResourceBundle.getBundle("fi.statsim.text.Text",language1);

	}
	
	public StatSim()	{	
		Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.statsim.text.Text",language);
	}
	
	public void init() {	
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		setLayout(null);
		
		//instelling taal
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.statsim.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(255,255,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("StatSim",new String[]
			{	"versie-info: 1.0",
				"auteur: Daniel Boon",
				"programmeur: Daniel Boon",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(0,0,20,30);
		add(fiButton);
		
		interactiePanel = new StatSimInteractiePanel();
		interactiePanel.setBounds(0,0,getWidth(), getHeight());
		
		add(interactiePanel);
	}

	public void start()	{	
		if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
		}
	
	}
	
	public void stopSco() {	
		stop();
		api = null;
	}
	
	public void stop() {	
		if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	public void setState(String s) {	
		//decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		//haal de data uit de hashtabel
		Hashtable state = (Hashtable)h.get("state");
		
	    //herstel de state van het interactiePanel
		interactiePanel.setState(state);
	}
	
	public String getState() {	
		Hashtable h = new Hashtable();
		
		//vraag de gegevens op van het interactiePanel
		Hashtable state = interactiePanel.getState();
	    
	    //voeg de gegevens toe aan de hashtable
	    h.put("state", state);
	      
	    //codeer de hashtable tot string
	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public double getScore() {	
		return 0;
	}
	
	public boolean hasEditMode() {	
		return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata) {	
    	return null;
    }
    
    public Parameter[] getEditableParameters()	{	
    	return null;
    }

    public Parameter[] getAllParameters() {	
    	return null;
    }

	public InteractiePanel getInteractiePanel() {
		return new StatSimInteractiePanel();
	}
}
