package fi.algebrapijlenopdr;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.awt.event.*;

import fi.algebrapijlenopdr.text.*;
import fi.algebrapijlenopdr.opdrnav.*;
import fi.algebrapijlenopdr.tekstobjects.*;
import java.applet.Applet;
import fi.beans.copyright.*;
import fi.beans.base64code.*;
import fi.beans.scorm.*;
import fi.beans.mainframe.*;
import fi.beans.stringutils.*;
import fi.beans.appletutil.*;
import fi.beans.tooltip.ToolTipManager;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

import javax.swing.*;

/**
 * @author Peter Boon
 */

public class AlgebraPijlenOpdr extends JApplet implements ScormAppletIF, ActionListener, WiskOpdrApplet
{	
	private SCORM12APIInterface api;
	
	private fi.beans.copyright.FIButton fiButton;
	protected static ResourceBundle rb;
	protected static boolean simplify = false;
	private String langArg;
	
	private OpdrNavStruct ons;
	private Hashtable defaultParamValues, launchData;
	private ScormEditComponentIF scormEditComponent;
	
//	private Button kopieerKnop;
	
	private Button viewButton;
	
	public static String clipBoard = "";
	
	//AlgebraSchuifVeld as;
	
	static Image goedkrul, foutkruis, halfkrul;
		
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
		
//System.out.println("constr 1");

	}
	
	public AlgebraPijlenOpdr(Locale language)
	{	rb = ResourceBundle.getBundle("fi.algebrapijlenopdr.text.Text",language);

//System.out.println("constr 2");	

	}
	
	public void init()
	{	

//System.out.println("init");

		String launchDataString = super.getParameter("launchData");
		
	//"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";
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
		ToolTipManager ttm = new ToolTipManager(this);
		
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
		
		fiButton = new fi.beans.copyright.FIButton("Info",
				                                   new String[]
                                                   {"Algebra Pijlen Opdrachten",
													"versie-info: 20110923",
                  									"auteur: Peter Boon",
													"programmeur: Peter Boon",
													"Freudenthal Instituut",
													"www.fi.uu.nl",""});
		
		AppletUtil au = new AppletUtil(this);
		goedkrul = au.getImage("resources/goedkrul.gif");
		foutkruis = au.getImage("resources/foutkruis.gif");
		halfkrul = au.getImage("resources/goedkrulhalf.gif");
		MediaTracker tr = new MediaTracker(this);
		tr.addImage(goedkrul,0);
		tr.addImage(foutkruis,0);
		tr.addImage(halfkrul,0);
		try 
		{	tr.waitForAll();
		} 
		catch(Exception e) {}
		AntwoordFormuleVak.zetPlaatjes(goedkrul,foutkruis,halfkrul);
//if (goedkrul != null)
//System.out.println("not null");	
		
		
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
		
		LinkRegel.setApplet(this);
		
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
		
		//InteractiePanel inp = getInteractiePanel();
		//inp.setBounds(0, 0, 500, 300);
		//add((Component)inp,0);
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
				
//opdrContainer.as.zetPlaatjes(goedkrul, foutkruis, halfkrul);				
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
	
	public InteractiePanel getInteractiePanel()
    { 	return new AlgebraPijlenOpdrInteractiePanel();	
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

	












