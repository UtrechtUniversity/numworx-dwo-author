/*  
	Doorzien DWO versie 20060919
*/

package fi.doorziendwo;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.swing.*;

// plaatjes, i.h.b. in de scormtest
import fi.beans.appletutil.AppletUtil;

// scorm
import fi.beans.scorm.*;
import fi.beans.base64code.*;
import fi.beans.tekstobjects.*;	
import fi.beans.wiskopdrbeans.*;

// main applet class with start button
public class DoorzienDWO extends JApplet implements ScormAppletIF, WiskOpdrApplet
{   // attributes

	protected static ResourceBundle rb;
	protected static String langArg;
	public static Locale language;
	
	
    // scorm
    protected SCORM12APIInterface api;
    private long sessionStartTime;
	private Hashtable defaultParamValues, launchData;
	private ScormEditComponentIF scormEditComponent;
	
	private TekstArea tekstArea;
    
	boolean scormTest = true;

    AppletUtil au;
    
    public static Applet applet;
    
    // the langage lookup table
    // NOTE: static so it can be accessed from other compilation units
    //static Table textTable;

    // parametrization strings
    String figureString = null;
    String toolString = null;
    
	// images right tool bar top to bottom
   // static Image rotate, wireFrame, solid,
   //       zoomIn, zoomInOff, zoomOut, zoomOutOff, 
    //      conDraw, figure 
          ;
    // images top toolbar left to right
    /*
    static Image drawLine, drawLineOff,
          deleteLine, deleteLineOff,  
          lengLines, lengLinesOff,
          shortLines, shortLinesOff,
          drawPlane, drawPlaneOff,
          parPlane, parPlaneOff,
          deletePlane, deletePlaneOff,
          planesFilled, planesFilledOff, planesEmpty,
          transPlane, transPlaneOff, noTransPlane,
          rotPlane, rotPlaneOff, noRotPlane,
          showCut, showCutOff, hideCut,
          cut, cutOff, glue,
          undo, undoOff,
          redo, redoOff
          ;  
          
    Image epnLogo;  */        
    // button to start the main applet frame
    JButton startButton;
    // flag to indicate if main applet frame has started
	boolean frameStarted = false;
	// main applet frame
	DoorzienFrame doorzienFrame = null;
	
	// alleen voor de DWO-test, zorg dat deze waarden
	// gelijk zijn aan die van de applet-html
	public static int startWidth = 780;
	public static int startHeight = 480;
	
    // versions
    public static final int EPN = 0;
    public static final int FI = 1;
    public static int version = EPN;
	
	
	int offSet = 15;

	Font theFont;
	FontMetrics theFM;
	
	// viewers
	ViewPanel viewPanel, viewPanelExample;
	
	public DoorzienDWO()
	{	//textTable = new Table("nl");
	
		language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.doorziendwo.text.Text",language);	
	
		applet = this;
		
	}
	
	public DoorzienDWO(Locale language)
	{	//textTable = new Table(lang.toString());
	
		this.language = language;
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.doorziendwo.text.Text", language);	
		applet = this;
		
	}
	
	public void init()
	{   defaultParamValues = makeDefaultParamValues(0);
		String launchDataString = super.getParameter("launchData");
		if (launchDataString != null)
		{	Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable) o;
		}
		
			
		// scorm
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e)
		{}
	
		// compatibiliteit
		au = new AppletUtil(this);
		
		setLayout(null);

// later via bgColor?				
		setBackground(Color.lightGray);

		
        // get language parameter if any (nl is default)
        langArg = getParameter("language");
        // create language lookup table
        //textTable = new Table(langArg);
        
        // read all parameter strings here
        figureString = getParameter("figures");
        toolString = getParameter("tools");

        /*
        // find images
        rotate = au.getImage("resources/rotate.gif");	    
		wireFrame = au.getImage("resources/wireframe.gif");	    
		solid = au.getImage("resources/solid.gif");	    
		zoomIn = au.getImage("resources/zoomin.gif");	    
		zoomInOff = au.getImage("resources/zoominoff.gif");	    		
		zoomOut = au.getImage("resources/zoomout.gif");	    
		zoomOutOff = au.getImage("resources/zoomoutoff.gif");	    		
		conDraw = au.getImage("resources/condraw.gif");	    				
        figure = au.getImage("resources/figure.gif");	    						

		drawLine = au.getImage("resources/drawline.gif");	    						
		drawLineOff = au.getImage("resources/drawlineoff.gif");	    								
		deleteLine = au.getImage("resources/deleteline.gif");	    						
		deleteLineOff = au.getImage("resources/deletelineoff.gif");	    								
		lengLines = au.getImage("resources/lenglines.gif");	    								
		lengLinesOff = au.getImage("resources/lenglinesoff.gif");	    										
		shortLines = au.getImage("resources/shortlines.gif");	    								
		shortLinesOff = au.getImage("resources/shortlinesoff.gif");	    										
		
		drawPlane = au.getImage("resources/drawplane.gif");	    										
		drawPlaneOff = au.getImage("resources/drawplaneoff.gif");	    										
		parPlane = au.getImage("resources/parplane.gif");	    												
		parPlaneOff = au.getImage("resources/parplaneoff.gif");	    												
		deletePlane = au.getImage("resources/deleteplane.gif");	    										
		deletePlaneOff = au.getImage("resources/deleteplaneoff.gif");	    										
		planesFilled = au.getImage("resources/planesfilled.gif");	    										
		planesFilledOff = au.getImage("resources/planesfilledoff.gif");	    										
        planesEmpty = au.getImage("resources/planesempty.gif");	    												
		transPlane = au.getImage("resources/transplane.gif");	    												
		transPlaneOff = au.getImage("resources/transplaneoff.gif");	    												
		noTransPlane = au.getImage("resources/notransplane.gif");	    														
		rotPlane = au.getImage("resources/rotplane.gif");	    												
		rotPlaneOff = au.getImage("resources/rotplaneoff.gif");	    												
		noRotPlane = au.getImage("resources/norotplane.gif");	    														
		showCut = au.getImage("resources/showcut.gif");	    												
		showCutOff = au.getImage("resources/showcutoff.gif");	    												
		hideCut = au.getImage("resources/hidecut.gif");	    												
		cut = au.getImage("resources/cut.gif");	    												
		cutOff = au.getImage("resources/cutoff.gif");	    												
		glue = au.getImage("resources/glue.gif");	    														
		undo = au.getImage("resources/undo.gif");	    																
		undoOff = au.getImage("resources/undooff.gif");	    																		
		redo = au.getImage("resources/redo.gif");	    																
		redoOff = au.getImage("resources/redooff.gif");	    																		
		
		epnLogo = au.getImage("resources/EPNlogo.gif");	    																				
		*/
		Color bgcolor = new Color(230, 240, 255);
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			bgcolor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		getContentPane().setBackground(bgcolor);
		
		if (getParent() instanceof ScormEditMainFrame)
		{	scormEditComponent = getEditComponent(defaultParamValues);
			((ScormEditMainFrame)getParent()).setScormEditComponent(scormEditComponent);
			getContentPane().add(scormEditComponent.getComponent(), 0);
			scormEditComponent.getComponent().setSize(getSize().width, getSize().height);
			
		} 
		else
		{	
			viewPanel = new ViewPanel(300, 15, startWidth - 315, startHeight - 65);
			viewPanel.setApplet(this);
			viewPanel.setBackground(getBackground());
			getContentPane().add(viewPanel);
			
			String editModeState = getParameter("editModeState");
			viewPanel.setState(editModeState);
			
			boolean example = false;
			String exampleString = getParameter("example");
			if (exampleString != null && exampleString.equals("true")) 
				example = true;
			
			boolean draaibaar = false;
			String draaibaarString = getParameter("draaibaar");
			if (draaibaarString != null && draaibaarString.equals("true")) 
				draaibaar = true;
			
			if (example)
			{	viewPanelExample = new ViewPanel(15, 15, 260, 200);
				getContentPane().add(viewPanelExample);
				viewPanelExample.setApplet(this);
				viewPanelExample.setBordered(false);
				viewPanelExample.setMouse(draaibaar);
				viewPanelExample.setChangeable(false);	
				viewPanelExample.setBackground(getContentPane().getBackground());
				
				String exampleState = getParameter("exampleState");
				viewPanelExample.setState(exampleState);
			}
			
			tekstArea = new TekstArea();
			if (example) 
				tekstArea.setBounds(15,230,280,250);
			else 
				tekstArea.setBounds(15,15,280,250);
			tekstArea.setText(getParameter("tekst"));		
			getContentPane().add(tekstArea);
			tekstArea.resize();	
			
			AppletUtil au = new AppletUtil(this);
			Image uitleg = null;
			//if(langArg.equals("nl"))uitleg = au.getImage("resources/help.gif");
			//else uitleg = au.getImage("resources/help_en.gif");
			uitleg = au.getImage("resources/help.gif");
			MediaTracker tr = new MediaTracker(this);
			tr.addImage(uitleg, 0);
			try
			{	tr.waitForAll();
			} 
			catch(Exception e) {}
			
			UitlegButton uitlegButton = new UitlegButton(rb.getString("uitlegButtonViewerText"), uitleg);
			uitlegButton.setBounds(630, getSize().height - 30, 90, 20);
			uitlegButton.setFrameBackground(getBackground());
			getContentPane().add(uitlegButton, 0);
		}
	} // init
	
	public void setBackground(Color c)
	{
		if (viewPanel != null)
			viewPanel.setBackground(c);
		if (viewPanelExample != null)
			viewPanelExample.setBackground(c);
		super.setBackground(c);
		
	}
	
	public static void loadImages(Hashtable images,String[] imageNames)
	{	//AppletUtil au = new AppletUtil(applet);
		MediaTracker tr = new MediaTracker(applet);
		Image[] image = new Image[imageNames.length];
		for (int i = 0; i < imageNames.length; i++)
		{	image[i] = makeImage("resources/" + imageNames[i]);
			tr.addImage(image[i], 0);
		}
		try 
		{	tr.waitForAll();
		}
		catch(Exception e) {};
		
		for (int i = 0; i < imageNames.length; i++)
		{	images.put(imageNames[i], image[i]);
		}
		
	}
	
	private static Image makeImage(String resourceName)
	{	byte[] buffer;
		try 
		{
		    InputStream in = applet.getClass().getResourceAsStream(resourceName);
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    buffer = new byte[1024]; 
		    int len;
		    do 
		    { 
		    	len = in.read(buffer);
	//System.out.println("read " + len);
		        if(len > 0) 
		        	bos.write(buffer, 0, len);
		    }
		    while (len > 0);
		    buffer = bos.toByteArray();
		    in.close();
		    bos.close();
		    	    
		}  
		catch(Exception e)
		{   e.printStackTrace();
		    return null;
		}
		return applet.getToolkit().createImage(buffer); 
	}
	
	public InteractiePanel getInteractiePanel()
	{
		
/*		
		ViewPanel viewPanel = new ViewPanel(0, 0, 500, 500);
		String editModeState = "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";
		
		viewPanel.setState(editModeState);
		
		return viewPanel;
*/		
		
		return new DoorzienInteractiePanel();
	}

	//public void update(Graphics g)
	//{	paint(g);
	//}

	
	// scormTest
	public static void main(String[] args)    
	{	
		DoorzienDWO doorzienDWO = new DoorzienDWO();
		doorzienDWO.scormTest = true;
		ScormMainFrame mf = new ScormMainFrame(doorzienDWO, doorzienDWO.startWidth, startHeight);
	//	ScormEditMainFrame mf = new ScormEditMainFrame(doorzienDWO, doorzienDWO.startWidth, startHeight);
		mf.setTitle("Doorzien Scormed");
		mf.pack();
		mf.show();
		// groter voor Frame insets
		mf.setSize(doorzienDWO.startWidth + doorzienDWO.offSet - 7, 
				   doorzienDWO.startHeight + 2 * doorzienDWO.offSet);
	}


	// scorm
	public void start()
	{	sessionStartTime = System.currentTimeMillis();
		if (api != null)
		{	
//System.out.println("API found");		
			String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	
//System.out.println("suspenddata not null");						
				setState(s);
			}
			
			//
			api.LMSSetValue("cmi.launch_data",StringCodeObject.encodeObjectToString(defaultParamValues));
			//
		}
	
	}
	
	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			String t = getSessionTime();
			api.LMSSetValue("cmi.core.session_time",t);
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}

	public void stopSco()
	{	if (api != null)
		{	stop();
			api = null;
		}
	
	}

	public double getScore()
	{	return 0;
	}

	public String getSessionTime()
	{	long sessionTime = System.currentTimeMillis() - sessionStartTime;
		String s = "";
		int hours = (int) sessionTime / 3600000;
		int minutes = (int) sessionTime / 60000 - hours * 60;
		int seconds = (int) sessionTime / 1000 - hours * 3600 - minutes * 60;
		if (hours < 10) 
			s += "0";
		s += hours;
		s += ":";
		if (minutes < 10)
			s += "0";
		s += minutes;
		s += ":";
		if (seconds < 10)
			s += "0";
		s += seconds;
		return s;
	}
	
	
	public void setState(String s)
	{	viewPanel.setState(s);
	}
	
	public String getState()
	{	
		// haal hier nog even mat uit de viewer
		// misschien was het object nog gedraaid
		viewPanel.scormedObject3D.mat = Matrix3D.copy(viewPanel.viewPanel3D.mat);
		String s = StringCodeObject.encodeObjectToString(viewPanel.scormedObject3D);
	    return s;
	}

	public Hashtable getDefaultParamValues(int variant)
	{	return makeDefaultParamValues(variant);
	}
	
	public String getParameter(String name)
	{	String value = null;
		if (launchData != null)
			value = (String) launchData.get(name);
		if (value == null)	
			value = super.getParameter(name);
		if (value == null) 
			value = (String) defaultParamValues.get(name);
		return value;
	}


	private Hashtable makeDefaultParamValues(int variant)
	{	Hashtable h = new Hashtable();
		/*h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		h.put("editModeState","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");
		h.put("example","true");
		h.put("exampleState","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");
		h.put("tekst","Maak de bouwplaat die je hierboven ziet. Doe dat door de kubus hiernaast in \"Doorzien\" te bewerken.");
		*/
		
		/*
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		h.put("editModeState","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");
		h.put("example","true");
		h.put("draaibaar","true");
		h.put("exampleState","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");		
		h.put("tekst","De figuur hierboven heet de \"stompe kubus\". Die naam zegt eigenlijk al dat je deze figuur kunt krijgen door de scherpe randen en punten van de kubus af te snijden. Alleen moet dat wel gebeuren op een speciale manier.\n\nProbeer deze figuur te maken met Doorzien.");
		*/
		
		h.put("language","nl");
		h.put("bgcolor","#DDEEFF");
		h.put("editModeState","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");
		h.put("example","true");
		h.put("exampleState","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");		
		h.put("tekst","De kubus hiernaast kun je ronddraaien met de muis, maar je kunt er ook een andere figuur mee maken met behulp van het programma \"Doorzien\".\n\nKlik op de knop om het programma te starten. Je kunt dan vlakken en lijnen toevoegen, maar ook stukken van de figuur afsnijden en bouwplaten maken.");
		

		return h;					
	}

	public boolean hasEditMode()
	{	return true;
	}
	
	public ScormEditComponentIF getEditComponent(Hashtable launchData)
	{	ScormEditComponent sec = new ScormEditComponent(launchData);
		sec.setApplet(this);
		return sec;
	}
	
	
	public Parameter[] getEditableParameters()
	{	return null;
	}
	
	public Parameter[] getAllParameters()
	{	return null;

	}
	

	public void restart()
	{   frameStarted = false;
		startButton.setEnabled(true);
	}    
	
	
}
    

class ScormedObject3D implements Serializable
{	// attributes
	// from DrawingPanel
	int mode;
	// haal dit uit theObjectGroup via leftMostLeaf()
	// Object3D origObject;
	ObjectGroup3D theObjectGroup;
	int numLines;
	int numPlanes;
	boolean filled;
	boolean planesFilled;
	double lengthFactor;
	boolean letters;
	
	ObjectGroup3D theCutObjectGroup;
	// nodig!
	boolean figureCut;
	String volumeString;
	boolean oldPlanesFilled;
	Plane3D planeChoosen;
	
	// null of niet
	ObjectGroup3D theFoldOutGroup;
	boolean flattened;
	Matrix3D oldPos;
	boolean oldFilled;
	double angle;
    FoldOutTreeNode theFoldOutTreeRoot;	
    Facet3D theStartFacet;
	
	
	// attributes from Panel3d
	int projection;
	Matrix3D mat;
	int paintType;
	double zoomFactor;
	boolean showInside;
	
	// default constructor
		
}

//class DummyPanel extends Panel
//{	
//	public void update(Graphics g)
//	{	paint(g);
//	}
//}
