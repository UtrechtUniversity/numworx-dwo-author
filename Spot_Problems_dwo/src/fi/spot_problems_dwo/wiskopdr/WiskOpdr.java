package fi.spot_problems_dwo.wiskopdr;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import fi.beans.copyright.*;
import fi.beans.mainframe.*;
import fi.beans.scorm.*;
import fi.beans.appletutil.*;
import fi.beans.base64code.*;
import fi.spot_problems_dwo.wiskopdr.opdrnav.*;

public class WiskOpdr extends JApplet implements ScormAppletIF, ActionListener
{
	protected SCORM12APIInterface api;
	protected long sessionStartTime;
	
	protected ScormEditComponentIF scormEditComponent;
	protected static ResourceBundle rb;
	
	protected OpdrNavStruct ons;
	protected Hashtable defaultParamValues;
	
	/*public static void main(String[] args)    
	{	int width = 800;
        int height = 560;
		//ScormEditMainFrame mf = new ScormEditMainFrame(new WiskOpdr(),width, height);
		ScormMainFrame mf = new ScormMainFrame(new WiskOpdr(),width, height);
		mf.setTitle("WiskOpdr");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}*/
	
	public WiskOpdr()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.spot_problems_dwo.wiskopdr.text.Text",language);
	}
	
	public void init() 
	{	String variantString = super.getParameter("variant");
		int variant = 0;
		if(variantString!=null) variant = Integer.parseInt(variantString);
		
		defaultParamValues = makeDefaultParamValues(variant);
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		setLayout(null);
		//instelling taal
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.spot_problems_dwo.wiskopdr.text.Text",language);
		
		//instelling achtergrondkleur
		Color bgcolor = new Color(230,240,255);
		String kleurcode = getParameter("bgcolor");
		if(kleurcode!=null)bgcolor = new Color(Integer.parseInt(kleurcode.substring(1),16));
		setBackground(bgcolor);
		
		//Fi-logo, copyright
		FIButton fiButton = new FIButton("WiskOpdr",new String[]
			{	"versie-info:20060722",
				"auteur:Peter Boon",
				"programmeur:Peter Boon",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		Panel p = new Panel();
		p.setLayout(null);
		p.setBounds(0,0,20,30);
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
			ons.addActionListener(this);
			add(ons);
			for(int i=0 ; i<aantalActiviteiten ; i++)
			{	
				for(int j=0 ; j<aantalOpdrachten[i] ; j++)
				{	String opdracht = 	this.getParameter("opdracht_"+(i+1)+"_"+(j+1));
					MyOpdrContainer opdrContainer = new MyOpdrContainer(0,0,getSize().width, getSize().height);
					opdrContainer.zetOpdracht(opdracht);
					ons.zetOpdrContainer(opdrContainer,i,j);
				}
			}
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
	
	public Hashtable makeDefaultParamValues(int variant)
	{	
		Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		
		Hashtable defaultEditModeLaunchData = new Hashtable();
		defaultEditModeLaunchData.put("titel","Titel");
		defaultEditModeLaunchData.put("tekst","Opdrachttekst");
		defaultEditModeLaunchData.put("randVarString","");
		defaultEditModeLaunchData.put("antwoordString","$f3x@");
		defaultEditModeLaunchData.put("herleiding",new Boolean(false));
		defaultEditModeLaunchData.put("exact",new Boolean(false));
		defaultEditModeLaunchData.put("soortHerleiding",new Integer(0));
		defaultEditModeLaunchData.put("puntenGelijkwaardig",new Integer(10));
		defaultEditModeLaunchData.put("puntenHerleiding",new Integer(0));
		defaultEditModeLaunchData.put("puntenExact",new Integer(0));
		String defaultEditModeState = StringCodeObject.encodeObjectToString(defaultEditModeLaunchData);
		
		if(variant==0)
		{	h.put("aantalActiviteiten","2");
 
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
	{	sessionStartTime = System.currentTimeMillis();
		if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
			//
			api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//
		}
		Thread startDraad = new Thread()
			{	public void run()
				{	try
		    		{   sleep(200);
					}
		    		catch(InterruptedException e)    
					{ }
					if(scormEditComponent != null)scormEditComponent.reset();
					else ons.start();
					//repaint();
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
			String t = getSessionTime();
			api.LMSSetValue("cmi.core.session_time",t);
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
	
	
	public String getSessionTime()
	{	long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int)sessionTime/3600000;
		int minutes = (int)sessionTime/60000 - hours*60;
		int seconds = (int)sessionTime/1000 - hours*3600 - minutes*60;
		if(hours<10)s += "0";
		s += hours;
		s += ":";
		if(minutes<10)s += "0";
		s += minutes;
		s += ":";
		if(seconds<10)s += "0";
		s += seconds;
		return s;
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

	public void paint(Graphics g) 
	{	super.paint(g);
	}

	public void actionPerformed(ActionEvent e)
	{
	}
	
	public int geefOpdrachtNr()
	{	return ons.geefOpdrachtNr();
	}
	
	public int geefActiviteitNr()
	{	return ons.geefActiviteitNr();
	}
	
	public MyOpdrContainer geefMyOpdrContainer(int actNr, int OpdrNr)
	{	return (MyOpdrContainer)ons.geefOpdrContainer(actNr, OpdrNr);
	}
}
