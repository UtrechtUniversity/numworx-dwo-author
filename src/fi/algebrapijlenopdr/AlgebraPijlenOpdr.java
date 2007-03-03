package fi.algebrapijlenopdr;

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

public class AlgebraPijlenOpdr extends Applet implements ScormAppletIF, ActionListener
{	
	private SCORM12APIInterface api;
	
	private fi.beans.copyright.FIButton fiButton;
	protected static ResourceBundle rb;
	protected static boolean simplify = false;
	private String langArg;
	
	private OpdrNavStruct ons;
	private Hashtable defaultParamValues, launchData;
	private ScormEditComponentIF scormEditComponent;
	
	private Button kopieerKnop;
	
	private Button viewButton;
	
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
        int height = 550;
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
	{	String launchDataString = //super.getParameter("launchData");
		
		"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";
		
		if(launchDataString!=null)
		{	Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable)o;
		}
		
		String variantString = super.getParameter("variant");
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
		
		fiButton = new fi.beans.copyright.FIButton("Algebra Pijlen Opdrachten",new String[]{"","versie-info: 20070303",
																	"auteur: Peter Boon",
																	"programmeur: Peter Boon",
																	"Freudenthal Instituut",
																	"www.fi.uu.nl",""});
		
		
		
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
		
		viewButton = new Button ("View");
		viewButton.setBounds(getSize().width-60, getSize().height-60, 60,20);
		viewButton.addActionListener(this);
		
		if(getParent() instanceof ScormEditMainFrame)
		{	scormEditComponent = getEditComponent(defaultParamValues);
			((ScormEditMainFrame)getParent()).setScormEditComponent(scormEditComponent);
			add(scormEditComponent.getComponent(),0);
			scormEditComponent.getComponent().setSize(getSize().width,getSize().height);
			
			add(viewButton,0);
		} 
		else
		{	maakOpdrachten();
			Panel p = new Panel();
			p.setLayout(null);
			p.setBounds(getSize().width-20,getSize().height-30,20,30);
			fiButton.setBounds(0,0,20,30);
			p.add(fiButton);
			add(p,0);
		}
		
		
	}
	
	public void maakOpdrachten()
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
		
	}
	
	public Hashtable getDefaultParamValues(int variant)
	{	return makeDefaultParamValues(variant);
	}
	
	public String getParameter(String name)
	{	String value = null;
        value = super.getParameter(name);
		if(value==null && launchData!=null)value = (String)launchData.get(name);
		if(value==null) value = (String)defaultParamValues.get(name);
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

	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==viewButton)
		{	if(viewButton.getLabel().equals("View"))
			{	remove(scormEditComponent.getComponent());
				launchData = scormEditComponent.getLaunchData();
				maakOpdrachten();
				viewButton.setLabel("Edit");
				ons.start();
				
			}
			else
			{	remove(ons);
				add(scormEditComponent.getComponent());
				scormEditComponent.reset();
				viewButton.setLabel("View");
				
			}
		}
	}
	
}

	












