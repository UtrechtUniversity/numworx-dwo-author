package fi.sliderwidget;

import java.awt.*;
import java.util.*;
import fi.beans.copyright.*;
import fi.beans.mainframe.JApplet;
import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.*;
import fi.beans.base64code.*;

public class SliderWidget extends JApplet implements ScormAppletIF, WiskOpdrApplet { 

	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private SliderWidgetInteractiePanel interactiePanel;
	
	public static void main(String[] args) {	
		int width = 800;
        int height = 600;
		ScormMainFrame mf = new ScormMainFrame(new SliderWidget(),width, height);
		mf.setTitle("SliderWidget");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public SliderWidget(Locale language) {	
		rb = ResourceBundle.getBundle("fi.sliderwidget.text.Text",language);
	}
	
	public SliderWidget()	{	
		Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.sliderwidget.text.Text",language);
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
		rb = ResourceBundle.getBundle("fi.sliderwidget.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(255,255,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("SliderWidget",new String[]
			{	"versie-info: ...",
				"auteur: ...",
				"programmeur: ...",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(0,0,20,30);
		add(fiButton);
		
		interactiePanel = new SliderWidgetInteractiePanel();
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
		return new SliderWidgetInteractiePanel();
	}
}
