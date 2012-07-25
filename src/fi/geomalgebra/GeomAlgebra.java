package fi.geomalgebra;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;


import java.applet.Applet;
import javax.swing.*;

import fi.beans.mainframe.*;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.InteractiePanel;

/**
 * @author Peter Boon
 */

public class GeomAlgebra extends JApplet implements  ActionListener, ScormAppletIF, WiskOpdrParamEditApplet
{	
	protected SCORM12APIInterface api;
	private long sessionStartTime;
	
	private FIButton fiButton;
	private int breedte, hoogte;
	private Image im ;
  	private Graphics gIm ;
	
	protected static ResourceBundle rb;
	protected static String langArg;
	
	ControlPanel cp;
	private LineaalHor lh;
	private LineaalVer lv;
	private AlgebraVeld av;
	//private String langArg;
	
	private boolean varWaardeZichtbaar;
	private boolean oppWaardeZichtbaar;
	private boolean formuleZichtbaar=true;
	private boolean constructieTools = true;
	private boolean alleenOppervlaktes;
	 
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
		MainFrame mf = new ScormMainFrame(new GeomAlgebra(),width, height);
		mf.setTitle("Geometrische Algebra 2d");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public GeomAlgebra()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.geomalgebra.text.Text",language);
	}
	
	public GeomAlgebra(Locale language)
	{	
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.geomalgebra.text.Text", language);	
	}
	
	public void init()
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		hoogte = getSize().height;
		breedte = getSize().width;
		
		setLayout(null);
		
		langArg = getParameter("language");
		if (langArg == null) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.geomalgebra.text.Text",language);
		
		String varWaardeString = getParameter("varWaarde");
		if (varWaardeString != null && varWaardeString.equals("true")) 
			varWaardeZichtbaar = true;
		
		String oppWaardeString = getParameter("oppWaarde");
		if (oppWaardeString != null && oppWaardeString.equals("true")) 
			oppWaardeZichtbaar = true;
		
		String formuleString = getParameter("formule");
		if (formuleString != null && formuleString.equals("false")) 
			formuleZichtbaar = false;
		
		String constructieToolsString = getParameter("constructieTools");
		if (constructieToolsString != null && constructieToolsString.equals("false")) 
			constructieTools = false;
	
		String alleenOppervlaktesString = getParameter("alleenOppervlaktes");
		if (alleenOppervlaktesString != null && alleenOppervlaktesString.equals("true")) 
			alleenOppervlaktes = true;
		
		av = new AlgebraVeld(breedte, hoogte);
		av.setLocation(0,0);
		av.zetVarWaardeZichtbaar(varWaardeZichtbaar);
		av.zetOppWaardeZichtbaar(oppWaardeZichtbaar);
		av.zetFormuleZichtbaar(formuleZichtbaar);
		av.zetConstructieTools(constructieTools);
		av.zetAlleenOppervlaktes(alleenOppervlaktes);
		getContentPane().add(av);
		
		Figuur.zetGeslotenVeld(!constructieTools || alleenOppervlaktes);
		Figuur.zetVeldSizes(breedte, hoogte);
		
		cp = new ControlPanel(av);
		cp.setLayout(null);
		cp.setBounds(1, hoogte - 41, breedte - 2, 40);
		if (constructieTools) 
			av.add(cp, 0);
			//getContentPane().add(cp, 0);
		
		lh = new LineaalHor(breedte, hoogte);
		lh.addActionListener(this);
		if (constructieTools) 
			av.add(lh, 0);
			//getContentPane().add(lh, 0);
			
		
		lv = new LineaalVer(breedte, hoogte);
		lv.addActionListener(this);
		if (constructieTools) 
			av.add(lv, 0);
			//getContentPane().add(lv, 0);
		
		fiButton = new FIButton("Geometrische Algebra",new String[]{"","versie-info: 20070227",
													"auteurs: Gerard Koolstra, Peter Boon",
													"          en Martin Kindt",
													"programmeur: Peter Boon",
													"Freudenthal Instituut",
													"www.fi.uu.nl",""});
		//fiButton.setBounds(2,18,15,20);
		fiButton.setBounds(cp.getSize().width - 5 - 15,18,15,20);
		cp.add(fiButton);
	}
	
	public Hashtable getDefaultParameters()
    {
    	Hashtable h = new Hashtable();
    	h.put("varWaarde","false");
    	h.put("oppWaarde","false");
    	h.put("formule","true");
    	h.put("constructieTools","true");
    	h.put("alleenOppervlaktes","false");
    	
    	return h;
    }
	
	public void setSingleComponent()
	{	fiButton.setVisible(false);	
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
	
	public double getScore()
	{	return 0;
	}
	
	public String getState()
	{	return av.getState();
	}
    
    public void setState(String s)
    {	av.setState(s);
    }
    
    public void start()
	{	sessionStartTime = System.currentTimeMillis();
		if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
			//
			//api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//
			
			
			
		}
	
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
			//api.LMSSetValue("USER_GROUP","UG_TEACHER");
		}
	}
	
	public InteractiePanel getInteractiePanel()
	{
		//return new InteractiePanelAdapter(this);
		return new GAInteractiePanel();
	}

    public boolean hasEditMode()
    {	return false;
    }

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }

    public Parameter[] getEditableParameters()
    {	
    	Parameter[] parameters = new Parameter[5]; 
		DataType type = null;
		Parameter param = null;	

		type = new ScormBoolean();
		param = new Parameter("varWaarde", "Variabelewaarden zichtbaar", type);
		parameters[0] = param;
		
		type = new ScormBoolean();
		param = new Parameter("oppWaarde", "Oppervlaktewaarde zichtbaar", type);
		parameters[1] = param;
		
		type = new ScormBoolean();
		param = new Parameter("formule", "Formule voor oppervlakte", type);
		parameters[2] = param;
		
		type = new ScormBoolean();
		param = new Parameter("constructieTools", "Constructie-toolbox", type);
		parameters[3] = param;
		
		type = new ScormBoolean();
		param = new Parameter("alleenOppervlaktes", "Alleen oppervlaktes", type);
		parameters[4] = param;
		
		return parameters;
		
    }

    public Parameter[] getAllParameters()
    {	return null;
    }
	
	public void actionPerformed(ActionEvent e)
	{	String command = e.getActionCommand();
		int modifier = e.getModifiers();
		if (command.equals("maakBasis"))
		{	av.zetBasis(modifier);
		}
	}
}

