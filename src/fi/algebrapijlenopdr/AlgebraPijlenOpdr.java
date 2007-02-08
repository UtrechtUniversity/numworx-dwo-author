package fi.algebrapijlenopdr;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.text.*;
import fi.algebrapijlenopdr.opdrnav.*;
import java.applet.Applet;
import fi.beans.copyright.*;
import fi.beans.base64code.*;
import fi.beans.scorm.*;
import fi.beans.mainframe.*;
import fi.beans.stringutils.*;
import fi.beans.appletutil.*;
import fi.beans.tooltip.ToolTipManager;

/**
 * @author Peter Boon
 */

public class AlgebraPijlenOpdr extends Applet implements ScormAppletIF
{	
	private SCORM12APIInterface api;
	
	private fi.beans.copyright.FIButton fiButton;
	protected static ResourceBundle rb;
	protected static boolean simplify = false;
	private String langArg;
	
	private OpdrNavStruct ons;
	private Hashtable defaultParamValues;
	private ScormEditComponentIF scormEditComponent;
	
	private Button kopieerKnop;
	
	//AlgebraSchuifVeld as;
		
	/*public static void main(String[] args)    
	{	int width = 780;
        int height = 550;
        MainFrame mf = new MainFrame(new AlgebraPijlen(), width, height);
		mf.show();
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);
	}*/
	
	public static void main(String[] args)    
	{	int width = 800;
        int height = 620;
		ScormEditMainFrame mf = new ScormEditMainFrame(new AlgebraPijlenOpdr(),width, height);
		//ScormMainFrame mf = new ScormMainFrame(new AlgebraPijlenOpdr(),width, height);
		mf.setTitle("AlgebraPijlenOpdr");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public AlgebraPijlenOpdr()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.algebrapijlenopdr.text.Text",language);
	}
	
	public void init()
	{	String variantString = super.getParameter("variant");
		int variant = 0;
		if(variantString!=null) variant = Integer.parseInt(variantString);
		
		defaultParamValues = makeDefaultParamValues(variant);
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e)
		{
		}
		
		setLayout(null);
		
		//JVMChecker jvmc = new JVMChecker(this);
		//jvmc.check();
		
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.algebrapijlenopdr.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(230,240,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		fiButton = new fi.beans.copyright.FIButton("Algebra Pijlen Opdrachten",new String[]{"","versie-info: 20061113",
																	"auteur: Peter Boon",
																	"programmeur: Peter Boon",
																	"Freudenthal Instituut",
																	"www.fi.uu.nl",""});
		
		Panel p = new Panel();
		p.setLayout(null);
		p.setBounds(180,0,20,30);
		fiButton.setBounds(0,0,20,30);
		p.add(fiButton);
		add(p);
		
		AppletUtil au = new AppletUtil(this);
		Image goedkrul = au.getImage("resources/goedkrul.gif");
		Image foutkruis = au.getImage("resources/foutkruis.gif");
		Image halfkrul = au.getImage("resources/goedkrulhalf.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(goedkrul,0);
		tr.addImage(foutkruis,0);
		tr.addImage(halfkrul,0);
		try{tr.waitForAll();} catch(Exception e) {}
		AntwoordFormuleVak.zetPlaatjes(goedkrul,foutkruis,halfkrul);
		
		
		String SimplifyString = getParameter("simplify");
		if ( SimplifyString != null && SimplifyString.equals("false")) simplify = false;
		else simplify = true;
																	
		//AppletUtil au = new AppletUtil(this);
		//Image uitleg = au.getImage("resources/help.gif");
		//MediaTracker tr = new MediaTracker(this);
		//tr.addImage(uitleg,0);
		//try{tr.waitForAll();} 
		//catch(Exception e) {}
		//UitlegButton uitlegButton = new UitlegButton("Uitleg Algebrapijlen",uitleg);
		
		//as = new AlgebraSchuifVeld(0,0,getSize().width, getSize().height);
		//add(as);
		//as.tekenOpnieuw();
				
		//fiButton.setBounds(0,0,20,30);
		//as.add(fiButton);
		
		//uitlegButton.setBounds(18,445,70,20);
		//as.add(uitlegButton);
		
		if(getParent() instanceof ScormEditMainFrame)
		{	scormEditComponent = getEditComponent(defaultParamValues);
			((ScormEditMainFrame)getParent()).setScormEditComponent(scormEditComponent);
			add(scormEditComponent.getComponent(),0);
			scormEditComponent.getComponent().setSize(getSize().width,getSize().height);
			
		} 
		else
		{	String aantalActiviteitenString = this.getParameter("aantalActiviteiten");
			int aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
			int[] aantalOpdrachten = new int[aantalActiviteiten];
			String[] activiteitNamen = new String[aantalActiviteiten];
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	activiteitNamen[i] = this.getParameter("activiteit_"+(i+1));
				String aantalString = this.getParameter("aantalOpdrachten_"+(i+1));
				aantalOpdrachten[i] = Integer.parseInt(aantalString);
			}
			
			ons = new OpdrNavStruct(aantalActiviteiten,aantalOpdrachten,activiteitNamen,0,0,getSize().width, getSize().height, api, false);
			add(ons);
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	
				for(int j=0 ; j<aantalOpdrachten[i] ; j++)
				{	String opdracht = 	this.getParameter("opdracht_"+(i+1)+"_"+(j+1));
					MyOpdrContainer opdrContainer = new MyOpdrContainer(0,0,getSize().width, getSize().height);
					opdrContainer.zetOpdracht(opdracht);
					opdrContainer.start();
					ons.zetOpdrContainer(opdrContainer,i,j);
				}
			}
			//ons.zetOpdrachtNr(0,0);
		}
		
		
	}
	
	public Hashtable getDefaultParamValues(int variant)
	{	return makeDefaultParamValues(variant);
	}
	
	public String getParameter(String name)
	{	String value = super.getParameter(name);
		if(value==null)value = (String)defaultParamValues.get(name);
		return value;
	}
	
	private Hashtable makeDefaultParamValues(int variant)
	{	/*Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
							
		if(variant==0)
		{	Hashtable editModeLaunchData = new Hashtable();
			editModeLaunchData.put("titel","");
			editModeLaunchData.put("tekst","");
			editModeLaunchData.put("randVarString","");
			editModeLaunchData.put("antwoordString","$f@");
			String editModeState = StringCodeObject.encodeObjectToString(editModeLaunchData);
			
			h.put("editModeState",editModeState);
		}
		
		return h;*/
		
		Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		
		Hashtable defaultEditModeLaunchData = new Hashtable();
		defaultEditModeLaunchData.put("titel","Opdracht");
		defaultEditModeLaunchData.put("tekst","Op een jaarlijkse afrekening van het elektriciteitsbedrijf staat dat er dat jaar 1500 kWh (kilowattuur) aan elektriciteit verbruikt is. De prijs per kWh is 15 cent.  Bovendien moet iedereen 17,85 	euro vastrecht per jaar betalen, ongeacht het verbruik. Maak een pijlenketting met als invoer het verbruik, en als uitvoer het bedrag. Gebruik deze pijlenketting om het bedrag te berekenen bij een verbruik van 1750 kWh en bij een verbruik van 1975 kWh");
		defaultEditModeLaunchData.put("APState","");
		
		String defaultEditModeState = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);
		
		if(variant==0)
		{	h.put("aantalActiviteiten","1");
 
 			h.put("activiteit_1","Niveau 1");
			h.put("aantalOpdrachten_1","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_1_"+(i+1),defaultEditModeState);
		    }
			h.put("activiteit_2","Niveau 2");
			h.put("aantalOpdrachten_2","10");
			for (int i = 0; i<10; i++) 
			{	h.put("opdracht_2_"+(i+1),defaultEditModeState);
		    }
		}
		
		return h;
	}
	
	public void start()
	{	if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
		}
		//as.tekenOpnieuw();
		//if(scormEditComponent != null)scormEditComponent.reset();
		//
		//api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
		//
		
		Thread startDraad = new Thread()
			{	public void run()
				{	try
		    		{   sleep(500);
					}
		    		catch(InterruptedException e)    
					{ }
					if(scormEditComponent != null)scormEditComponent.reset();
					else ons.start();
				}
			};
		startDraad.start();
	}
	
	public void stopSco()
	{	if(api!=null)
		{	stop();
			api = null;
		}
	
	}
	
	public void stop()
	{	if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	public void destroy()
	{	if(ons!=null) ons.destroy();
		if(scormEditComponent!=null) scormEditComponent.end();
	}
		
	public void setState(String s)
	{	Object o = StringCodeObject.decodeStringToObject(s);
		if(o==null)return;
		Hashtable h = (Hashtable)o;
		{	Hashtable onsState = (Hashtable)h.get("onsState");
			ons.setState(onsState);
		}
	}
	
	public String getState()
	{	Hashtable onsState = null;
	
	    onsState = ons.getState();
	    
	    Hashtable h = new Hashtable();
	    h.put("onsState", onsState);

	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public double getScore()
	{	double score = ons.getScore();
		return score;
	}
	
    
    public boolean hasEditMode()
	{	return true;
	}
	
	public ScormEditComponentIF getEditComponent(Hashtable launchData)
	{	return new ScormEditComponent(launchData);
	}
	
	public Parameter[] getEditableParameters()
	{	return null;
	}
	
	public Parameter[] getAllParameters()
	{	Parameter[] parameters = new Parameter[3]; 
		DataType type = null;
		Parameter param = null;	

		type = new ScormString();
		param = new Parameter("language", "Taal", type);
		parameters[0] = param;
		
		type = new ScormInteger();
		type.setSize(8);
		param = new Parameter("bgcolor", "Achtergrondkleur", type);
		parameters[1] = param;
		
		type = new ScormString();
		param = new Parameter("editModeData", "Data begintoestand", type);
		parameters[2] = param;
		
		return parameters;

	}

	
	
}

	












