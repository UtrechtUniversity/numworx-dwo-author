package fi.wiskopdr;


import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceManager;

import fi.wiskopdr.cbook.WidgetBridge;
import fi.wiskopdr.formuleobjects.EditorContentPanel;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.opdrnav.MyOpdrEditContainer;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.beans.base64code.Base64StringEncoder;
import fi.beans.base64code.StringCodeObject;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.ResourceManagerClient;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import geogebra.GeoGebraApplet;
import geogebra.GeoGebra;

public class GeogebraPanel extends JRootPane implements  ActionListener, InteractiePanel, InteractieEditPanel, ResourceManagerClient
{	
	private static final int MENUBAR_HEIGHT = 25; // afpassen!
	private String[] randomVars;
	private Hashtable randomValues;

	private String yAsNaam = "y";

	private geogebra.GeoGebraPanel geogebraApplet;
	
	private Hashtable parameters;
	private boolean editapplet;
	
	private boolean bewaarOptie;
	private boolean border;
	private boolean nakijken;
	private boolean nakijkenGemaakteObjecten;
	private String[] geogebraCheckObjects;
	private Hashtable existingObjects;
	private int aantalExistingObjects;
	private int[] geogebraCheckScores;
	
	private Border borderPanel;
	private Component menustub;
	private JPanel p;
	
	private FormuleButton resetButton;
	private byte[] launchState;
	
	private int mode;
    
    private boolean ingevuld = true;
    private boolean nagekeken;
    
    private boolean correct;
    private boolean fout;
    
    private int attemptsCount;
	private Vector attempts;
    
	private int score;
    private int errorCount;
    private int scoreMax=10;
    
	static int GOED = 1;
	static int FOUT = 0;
	static int HALF = 2;
	static int GEEN = 3;
	
	//static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;
		
	private JButton checkButton;
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	
	private boolean logOption;
	private String logID;
	
	private boolean check;
	private boolean teltMee;
			
	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/
	
	public GeogebraPanel(){
		this(false);
	}

	public GeogebraPanel(boolean editapplet){	
		super();
		this.editapplet = editapplet;
		Container content = getContentPane();
		content.setLayout(null);
		
		attempts = new Vector();
				
		p = new JPanel(new BorderLayout()){			
			public void paintComponent(Graphics g)
			{
				if(getComponentCount() == 0) {
					int x = getBounds().x;
					int y = getBounds().y;
					int b = getBounds().width;
					int h = getBounds().height;
					g.setColor(WiskOpdr.bgcolor);
					g.fillRect(x,y,b,h);
					g.setColor(Color.gray);
					g.drawRect(x,y,b-1,h-1);
					g.setFont(new Font("SansSerif",Font.BOLD,20));
					g.drawString("Geogebra",20,40);
				}
			}
		};		
		menustub = Box.createVerticalStrut(MENUBAR_HEIGHT);
		p.setOpaque(false);
		content.add(p);
		
		borderPanel = BorderFactory.createLineBorder(Color.gray);
			
		resetButton = new FormuleButton("reseticon");
		resetButton.setSize(15,16);
		resetButton.addActionListener(this);
		getLayeredPane().setLayer(resetButton, JLayeredPane.PALETTE_LAYER.intValue());
		content.add(resetButton);
		
		checkButton = new JButton(WiskOpdr.rb.getString("nakijkKnopLabel"));
		checkButton.setSize(80,20);
		checkButton.addActionListener(this);
		checkButton.setVisible(false);
		content.add(checkButton,0);
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		goedIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		goedIC.setVisible(false);
		content.add(goedIC,0);
		
		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		halfIC.setVisible(false);
		content.add(halfIC,0);
		
		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		foutIC.setVisible(false);
		content.add(foutIC,0);
	}
	
	public void refreshGeogebra(){
System.out.println("begin refresh geogebra");
		if(geogebraApplet==null)
		{
			try {
				geogebraApplet = new geogebra.GeoGebraPanel();
System.out.println("applet = " + geogebraApplet);System.out.flush();
				geogebraApplet.setBackground(WiskOpdr.bgcolor);
				geogebraApplet.setLanguage(WiskOpdr.language);
				geogebraApplet.setBorder(null); // weet niet wat de default is?
			} catch (Exception e) {
				e.printStackTrace();
			}
			p.removeAll();
			if(editapplet) makeDefaultParamValues(2);
			p.add(geogebraApplet, BorderLayout.CENTER);
			p.add(menustub, BorderLayout.NORTH);
		}
//	        "showToolBar",  	OK
//	        "customToolBar",	extra
//	        "showToolBarHelp",	extra
//	        "framePossible",	weh
//	        "showMenuBar",		OK	
//	        "allowRescaling",	weg
//	        "enableShiftDragZoom", must, extra
//	        "enableRightClick",    must, extra
//	        "enableLabelDrags",    must, extra
//	        "enableChooserPopups", must, extra
//	        "errorDialogsActive", extra
//	        "maxIconSize",		OK
//	        "showAlgebraInput"	weg
// 			"allowStyleBar"
//			"showResetIcon"
// welke zijn bool, welke string?
		
		setBounds(getX(),getY(),getWidth(),getHeight()); // layout.
System.out.println("end refresh geogebra");System.out.flush();
	}
	
	private void setGeogebraParameters() {
		Object param;
		System.err.println("begin geogebraparameters");
		try {
			param = parameters.get("showMenuBar");
			if(param != null) geogebraApplet.setShowMenubar( "true".equals(param));

			param = parameters.get("showAlgebraView");
			if(param != null) geogebraApplet.setShowAlgebraView("true".equals(param));
			
			param = parameters.get("showResetIcon");
			if(param != null) geogebraApplet.setShowResetIcon("true".equals(param));
			
			param = parameters.get("allowStyleBar");
			if(param != null) geogebraApplet.setAllowStyleBar("true".equals(param));
			
			param = parameters.get("enableRightClick");
			if(param != null) geogebraApplet.setRightClickEnabled("true".equals(param));
			
			param = parameters.get("customToolBar");
			if(param != null) {
				parameters.put("showToolBar", "true");
				geogebraApplet.setCustomToolbar(param.toString());
			}

			param = parameters.get("showToolBar");
			if(param != null) {
				boolean showToolBarHelp = !"false".equals(parameters.get("showToolBarHelp")); // wat is de default hier, nu true!
				boolean showToolBar = "true".equals(param);
				geogebraApplet.setShowToolbar(showToolBar, showToolBarHelp);
			}
			param = parameters.get("showAlgebraInput");
			if(param != null) geogebraApplet.setShowAlgebraInput("true".equals(param));
			param = parameters.get("errorDialogsActive");
			if(param != null) geogebraApplet.setErrorDialogActive("true".equals(param));
			
			param = parameters.get("enableLabelDrags");
			if(param != null) geogebraApplet.setLabelDragsEnabled("true".equals(param));
			
			param = parameters.get("enableShiftDragZoom");
			if(param != null) geogebraApplet.setShiftDragZoomEnabled("true".equals(param));
//		param = parameters.get("enableChooserPopups");
//		if(param != null) geogebraApplet.setChooserPopupsEnabled("true".equals(param));
			
			param = parameters.get("maxIconSize");
			if(param != null) {
				try {
					geogebraApplet.setMaxIconSize(Integer.parseInt(param.toString()));
				} catch (Exception e) {} // jammer dan.
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("end geogebraparameters");
		
	}
	
	
	public void repaintGeogebra(){
		System.err.println("begin repaintgeogebra");
		try {
			geogebraApplet.repaint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("end repaintgeogebra");
	}
		
	public void makeDefaultParamValues(int type){	
		parameters = new Hashtable();
		parameters.put("language",WiskOpdr.language.getLanguage());
		if(type==0)	{
			parameters.put("showToolBar","true");
			//parameters.put("customToolBar" , "0 | 40 | 41 | 42 ");
			parameters.put("showToolBarHelp","false");
			parameters.put("framePossible","false");
			parameters.put("showMenuBar","true");
			parameters.put("showAlgebraInput","false");
			parameters.put("allowRescaling","true");
			parameters.put("centerimage","true");
			parameters.put("enableShiftDragZoom","false");
			parameters.put("enableRightClick","false");
			parameters.put("enableLabelDrags","false");
			parameters.put("enableChooserPopups","false");
		}
		else if(type==1){	
			parameters.put("showToolBar","true");
			parameters.put("framePossible","false");
			parameters.put("showMenuBar","true");
			parameters.put("showAlgebraView","true");
			parameters.put("showAlgebraInput","true");
		}
		else if(type==2){	
			parameters.put("showToolBar","true");
			parameters.put("framePossible","false");
			parameters.put("showMenuBar","true");
			parameters.put("showAlgebraView","true");
			parameters.put("showAlgebraInput","true");
		}
	}
	
	public void setBackground(Color color){	
		super.setBackground(color);
	}
	
	public void setPopupView(boolean b)	{	
	}
	
	public void setAttempt()
	{
		String goedFout = "";
		if(huidigIC == goedIC && huidigIC.isVisible())goedFout = "goed";
		if(huidigIC == halfIC && huidigIC.isVisible())goedFout = "half";
		if(huidigIC == foutIC && huidigIC.isVisible())goedFout = "fout";
		
		String logString = "";
				
		String s = logString;
		s = s + "   ;   ";
		s = s + goedFout;
		s = s + "   ;   ";
		s = s + "score = " + score;
		s = s + "   ;   ";
		s = s + new Date().toString();
		

		attempts.addElement(s);
		System.out.println(s);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==resetButton)
		{ {	
				setGGBfile(launchState);
				//for(int i=0 ; i<randomVars.length ; i++){	
				//	geogebraApplet.setValue("dwo_"+randomVars[i],((Integer)randomValues.get(randomVars[i])).intValue());
				//}
				// in popup is parent een frame niet een text panel vak.
				((TekstInteractiePanelVak)getParent()).getTekstVak().layoutTekst();
				
			}
		}
		
		if(e.getSource()==checkButton)
		{ 	kijkNa();
	        if((mode==0 || mode==1) && ingevuld)produceAction("checked");
	        if(fout) errorCount++;
	        attemptsCount++;
			setAttempt();
	        //if(logOption
		}
	}
	
	// set launch state after starting the activity. After this, the suspendstate (if any) will be recovered
	/* (non-Javadoc)
	 * @see fi.beans.wiskopdrbeans.InteractiePanel#zetOpdracht(java.util.Hashtable, java.lang.String[], java.util.Hashtable)
	 */
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues){
		if(h==null)return;
		
		String state = null;
		byte[] ggbFile = null;
		boolean bewaarOptie = false;
		boolean border = false;
		boolean showResetIcon = true;
		boolean file = false;
		String fileUrl = null;
		Hashtable geogebraParams = new Hashtable();
		boolean nakijken = false;
		int scoreMax = 10;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean nakijkenGemaakteObjecten = false;
	    String[] geogebraCheckObjects = null;
	    int[] geogebraCheckScores = null;
	        
		
		if(h.containsKey("state")) state = (String)h.get("state");
		if(h.containsKey("ggbFile")) ggbFile = (byte[])h.get("ggbFile");
		if(h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if(h.containsKey("border")) border = ((Boolean)h.get("border")).booleanValue();
		if(h.containsKey("showResetIcon")) showResetIcon = ((Boolean)h.get("showResetIcon")).booleanValue();
		if(h.containsKey("file")) file = ((Boolean)h.get("file")).booleanValue();
		if(h.containsKey("fileUrl")) fileUrl = (String)h.get("fileUrl");
		if(h.containsKey("geogebraParams")) geogebraParams = (Hashtable)h.get("geogebraParams");
		if (h.containsKey("nakijken")) nakijken = ((Boolean) h.get("nakijken")).booleanValue();
		if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if (h.containsKey("nakijkenGemaakteObjecten")) nakijkenGemaakteObjecten = ((Boolean) h.get("nakijkenGemaakteObjecten")).booleanValue();
        if (h.containsKey("geogebraCheckObjects")) geogebraCheckObjects = (String[]) h.get("geogebraCheckObjects");
        if (h.containsKey("geogebraCheckScores")) geogebraCheckScores = (int[]) h.get("geogebraCheckScores");
       
 		this.bewaarOptie = bewaarOptie;
		this.border = border;
		this.parameters = geogebraParams;
		this.nakijken = nakijken;
		this.scoreMax = scoreMax;
	    this.logOption = logOption;
	    this.logID = logID;
	    this.check = check;
	    this.teltMee = teltMee;
	    this.nakijkenGemaakteObjecten = nakijkenGemaakteObjecten;
	    this.geogebraCheckObjects = geogebraCheckObjects;
	    this.geogebraCheckScores = geogebraCheckScores;
	    
	    
	       
		checkButton.setVisible(nakijken && (mode==0 || mode==1));
        
		p.setBorder(border ? borderPanel: null);
		
		resetButton.setVisible(showResetIcon);
		menustub.setVisible(showResetIcon);
		
		refreshGeogebra();
		
		try {	
			if(Boolean.TRUE.equals(file) && fileUrl != null)
			{
				ResourceContainer unit = rm().getInstanceContainer();
				URL u = new URL( unit.getURL(), fileUrl );
				geogebraApplet.openFile(u);
			} else
				if(ggbFile != null)
					setGGBfile(ggbFile);
				else
				{
					geogebraApplet.getGeoGebraAPI().setBase64(state);
				}
				for(int i=0 ; i<randomVars.length ; i++){	
					String varClean = StringUtils.replaceStr(randomVars[i],"?(","");
					varClean = StringUtils.replaceStr(varClean,")","");
					geogebraApplet.getGeoGebraAPI().setValue("dwo_"+varClean,((Number)randomValues.get(randomVars[i])).intValue());
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setGeogebraParameters();
		buildGUI();
		
		
		launchState = getGGBfile(); // voor reset
		
		try {
			String[] existingObjectNames = geogebraApplet.getGeoGebraAPI().getAllObjectNames();
			aantalExistingObjects = existingObjectNames.length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		
		repaintGeogebra();
	}
	
	// set suspend state (recover the state in which a student left it when finishing the activity)
	public void setState(Hashtable h){	
		if(h==null)return;
		
		String state = null;
		//byte[] ggbFile = null;
		boolean ingevuld = false;
		boolean nagekeken = false;
		Vector attempts = new Vector();
		int attemptsCount = 0;
		int errorCount = 0;
	        
		if(h.containsKey("state")) state = (String)h.get("state");
		//if(h.containsKey("ggbFile")) ggbFile = (byte[])h.get("ggbFile");
		if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
	    if(h.containsKey("nagekeken")) nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
	    if(h.containsKey("attempts"))attempts = OpdrNavStruct.toVector(h.get("attempts"));
	    if(h.containsKey("attemptsCount")) attemptsCount = ((Number)h.get("attemptsCount")).intValue();
	    if(h.containsKey("errorCount")) errorCount = ((Number)h.get("errorCount")).intValue();
        
		if(bewaarOptie){	
				try {
					geogebraApplet.getGeoGebraAPI().setXML(state);
					buildGUI();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		repaint();
		if(ingevuld && (mode==0 || nagekeken)) kijkNa();
	}
	
	// get the suspend state (the state in which a student left it when finishing the activity)
	public Hashtable getState()	{
		
		String state = null;
		boolean ingevuld = false;
	    boolean nagekeken = false;
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
		
	    
	    ingevuld = this.ingevuld;
	    nagekeken = this.nagekeken;
	    attempts = this.attempts;
	    attemptsCount = this.attemptsCount;
	    errorCount = this.errorCount;

		kijkNa(false);
		if(logOption)
		{	
	    	Hashtable logMap = new Hashtable();
			
	    	String logString = "";
			logMap.put("logAnswer", logString);
			logMap.put("logScore", new Integer(score));
			logMap.put("logMaxScore", new Integer(scoreMax));
			logMap.put("logErrorCount", new Integer(errorCount));
			logMap.put("logAttemptsCount", new Integer(attemptsCount));
			logMap.put("logAttempts", attempts);
			
			WiskOpdr.setLog(logID, logMap);
		}
         
		Hashtable h = new Hashtable();
        h.put("ingevuld", new Boolean(ingevuld));
        h.put("nagekeken", new Boolean(nagekeken));
        h.put("attempts", attempts);
        h.put("attemptsCount", new Integer(attemptsCount));
        h.put("errorCount", new Integer(errorCount));
        
	    
        if(bewaarOptie)	{	
			try {
				state = geogebraApplet.getGeoGebraAPI().getXML();
				h.put("state", state);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 return h;
	}
	
	// set launch state in the editor
	public void setEditState(Hashtable h){
		if(h==null)return;
		
		String state = null;
		byte[] ggbFile = null;
		boolean bewaarOptie = false;
		boolean border = false;
		boolean showResetIcon = false;
		boolean file = false;
		String fileUrl = null;
		Hashtable geogebraParams = new Hashtable();
		
		if(h.containsKey("state")) state = (String)h.get("state");
		if(h.containsKey("ggbFile")) ggbFile = (byte[]) h.get("ggbFile");
		if(h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if(h.containsKey("border")) border = ((Boolean)h.get("border")).booleanValue();
		if(h.containsKey("showResetIcon")) showResetIcon = ((Boolean)h.get("showResetIcon")).booleanValue();
		if(h.containsKey("file")) file = ((Boolean)h.get("file")).booleanValue();
		if(h.containsKey("fileUrl")) fileUrl = (String)h.get("fileUrl");
		if(h.containsKey("geogebraParams")) geogebraParams = (Hashtable)h.get("geogebraParams");
        
		this.bewaarOptie = bewaarOptie;
		this.border = border;
		if(!editapplet)this.parameters = geogebraParams;
		
		refreshGeogebra();
		
		if(!editapplet){
				p.setBorder(border ? borderPanel: null);
				resetButton.setVisible(showResetIcon);
		}
		
		try {
			if(Boolean.TRUE.equals(file) && fileUrl != null)
			{
				ResourceContainer unit = rm().getInstanceContainer();
				URL u = unit.open(fileUrl).getURL();
				geogebraApplet.openFile(u);
			} else
			if(ggbFile != null)
			{
				setGGBfile(ggbFile);
			} else
			{
				geogebraApplet.getGeoGebraAPI().setBase64(state);
			}
			if(!editapplet)
				setGeogebraParameters();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		buildGUI();
		
		repaintGeogebra();		
	}
	
	 public void kijkNa(boolean show)
	    {
		 	if(!nakijken)return;
	        if(huidigIC!=null) huidigIC.setVisible(false);
	        
	        //ingevuld = false;
	        
	        correct = false;
	        fout = true;
	        score = 0;
	        
	        try {
				String[] allObjectNames = geogebraApplet.getGeoGebraAPI().getAllObjectNames();
				if(nakijkenGemaakteObjecten && allObjectNames.length<aantalExistingObjects){
					huidigIC = foutIC;
				    correct = false;
				    fout = true;
				    score = 0;
				    if(show && check)huidigIC.setVisible(true);
				}
				
      else if(nakijkenGemaakteObjecten){
					
					String[] objectNames = new String[allObjectNames.length-aantalExistingObjects];
					for (int i = 0; i < objectNames.length; i++)
				    {   
					    
					    objectNames[i] = allObjectNames[i+aantalExistingObjects];
					}
					String[] objectTypes = new String[objectNames.length];
					String[] valueStrings = new String[objectNames.length];
					String[] objectStrings = new String[objectNames.length];
					
					String[] checkObjects = new String[geogebraCheckObjects.length];
				    for (int j = 0; j < checkObjects.length; j++){
				        checkObjects[j] = geogebraCheckObjects[j];
				    }
				    
					int matches = 0;
					for (int i = 0; i < objectNames.length; i++) {
						objectTypes[i] = geogebraApplet.getGeoGebraAPI().getObjectType(objectNames[i]);
						valueStrings[i] = geogebraApplet.getGeoGebraAPI().getValueString(objectNames[i]);
				    	
				    	objectStrings[i] = StringUtils.replaceStr(valueStrings[i],objectNames[i]+"(x)","");
				    	objectStrings[i] = StringUtils.replaceStr(objectStrings[i],objectNames[i],"");
				    	objectStrings[i] = StringUtils.replaceStr(objectStrings[i]," ","");
				    	objectStrings[i] = objectStrings[i].substring(1);
				    	boolean match = false;
				    	
				    	int matchNr = -1;
				    	for (int j = 0; j < checkObjects.length; j++){
				    	    match = checkObjects[j].equals(objectStrings[i]);
				    	    if(!match){
				    	        String command = "checkDWO=" + checkObjects[j] + "==" + objectNames[i];
				    	        boolean check = false;
				    	        if(!objectTypes[i].equals("boolean")){
				    	            check = geogebraApplet.getGeoGebraAPI().evalCommand(command);
				    	            match = geogebraApplet.getGeoGebraAPI().getValue("checkDWO")==1.0;
				    	            
				    	        }
				    	    }
				    	    if(match){
				    	        if(show)geogebraApplet.getGeoGebraAPI().setColor(objectNames[i], 0, 180, 0);
				    	        score += geogebraCheckScores[j];
				    	        matches++;
				    	        matchNr = j;
				    	        break;
				    	    }
				        }
				    	if(match && checkObjects.length>0){
				    	    String[] checkObjectsRes = new String[checkObjects.length-1];
				    	    int teller = 0;
				    	    for (int k = 0; k < checkObjects.length; k++){
				    	        if(k!=matchNr){
				    	            checkObjectsRes[teller] = checkObjects[k];
				    	            teller++;
				    	        }
				    	    }   
				    	    checkObjects = checkObjectsRes;
				    	}
				    }
					
					if(matches==geogebraCheckObjects.length)
				    {   huidigIC = goedIC;
				        correct = true;
				        fout = false;
				        score = scoreMax;
				    }
					else if(matches>0)
				    {   huidigIC = halfIC;
				        correct = false;
				        fout = false;
				        
				    }
				    else 
				    {   huidigIC = foutIC;
				        correct = false;
				        fout = true;
				        score = 0;
				    }
				    if(show && check)huidigIC.setVisible(true);
				}
				else{
				    boolean juist = true;
				    
					double geogebraCorrect = geogebraApplet.getGeoGebraAPI().getValue("checkDWO");
				    String type = geogebraApplet.getGeoGebraAPI().getValueString("A");
				    System.out.println(type+" "+geogebraCorrect);
				    if(geogebraCorrect!=1.0)juist = false;
				    
				    if(juist)
				    {   huidigIC = goedIC;
				        correct = true;
				        fout = false;
				        score = scoreMax;
				    }
				    else 
				    {   huidigIC = foutIC;
				        correct = false;
				        fout = true;
				        score = 0;
				    }
				    if(show && check)huidigIC.setVisible(true);
				}
				if(ingevuld && show)produceAction("changed");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
//  @Deprecated	
//	public MyOpdrEditContainer getMyOpdrEditContainer(){	
//		Component parent = this;
//		for(int i=0 ; parent!=null && i<50 ; i++){	
//			if(parent instanceof MyOpdrEditContainer) {	
//				break;
//			}
//			else if(parent instanceof Frame) parent = ((Frame)parent).getOwner();
//			else if(parent instanceof Dialog) parent = ((Dialog)parent).getOwner();
//			else if(parent!=null){	
//				parent = parent.getParent();
//			}
//		}
//		if(parent instanceof MyOpdrEditContainer) return (MyOpdrEditContainer)parent;
//		else return null;
//	}
	
	public Hashtable getEditState() {		
		Hashtable h = new Hashtable();
				
		try {
			if(true) h.put("ggbFile", getGGBfile());
			else h.put("state", geogebraApplet.getGeoGebraAPI().getBase64());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return h;
	}
	
	public InteractieEditPanel getEditPanel(){	
		return newEditPanel(getInstanceId());
	}

	public static InteractieEditPanel newEditPanel(String id) {
		return new GeogebraEditPanel(id);
	}
	
	public void zetBreedte(int b){	
	}
	
	public void zetHoogte(int h){	
	}

// dit is onze layoutmanager
// reset knop rechtsboven
// geogebra panel in het midden (zonder 1 pixel randje)
// checkbutton onder.
// Lijkt op borderlayout met Center-, South- en East-panels echter East ligt bovenop Center
	
// mijn voorstel is om de reset button te integreren met het menu. en altijd de ruimte voor het menu te laten zien.
	
	
	public void setBounds(int x, int y, int b, int h){	
	    super.setBounds(x,y,b,h);
		p.setBounds(0,0,b,h-(nakijken?25:0));
		resetButton.setLocation(b-24,4);
//		if(parameters!=null && parameters.containsKey("showToolBar") && "true".equals(parameters.get("showToolBar")))
//			resetButton.setLocation(getWidth()-40,4);
		
		checkButton.setLocation(b/2-40,h-20);
		goedIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		halfIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		foutIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
        p.invalidate();
		p.validate();
		p.doLayout(); // even uitzoeken welke goed is.
	}
	
	public void setSizex(int b, int h) {
		setBounds(getX(), getY(), b, h);
	}
	
	public void resize(){	
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte(){return 10;}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore(){
		return score;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax(){
		if(nakijken)return scoreMax;
		else return 0;
	}
	
	public boolean isCorrect(){
		if(nakijken)return correct;
		else return true;
	}
	
	public boolean isFout(){
		if(nakijken)return fout;
		else return false;
	}
	
	public void zetMode(int mode){
		this.mode = mode;
		checkButton.setVisible(nakijken && (mode==0 || mode==1));
	}
	
	public void zetNagekeken(boolean b)
	{	if(ingevuld) nagekeken = b;
	}
	
    public void stop(){
    	//if(frame!=null) frame.setVisible(false);
    	kijkNa();
    	try {
			geogebraApplet.getGeoGebraAPI().stopAnimation();
			geogebraApplet.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void start(){
	}
    
    public void destroy() {
  		p.remove(geogebraApplet);
  		geogebraApplet.destroy();
		geogebraApplet = null;
		//System.gc();
		System.out.println("destroyed");
    }
    
    public void opnieuw(){}
    
    public void kijkNa(){
    	kijkNa(true);
    }
    
    public void kijkNa(int stapNr){
    	kijkNa();
    }
    
    //public void addActionListener(ActionListener al){}
    
    public InteractiePanel getInteractiePanel()	{
		return this;
	}
	
	
    
    // aanpassingen Wim load/save ggb files
    // ggb4.0 kent getBase64 en setBase64
	private static boolean hasLoadGGBfile = false; // hoe te bepalen?
	private static Method  loadGGBfile;
	static {
		try {
			hasLoadGGBfile = null != (loadGGBfile =
			geogebra.GeoGebraPanel.class.getMethod("loadGGBfile" , new Class[] { byte[].class } ));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// expected...
		}
		
		
	}

	private byte[] getGGBfile() 
    {
    	try {
			return geogebraApplet.getGeoGebraAPI().getGGBfile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[0];
		}
    }
    
    private void setGGBfile(byte[] state)
    {
		System.err.println("begin setGGBfile");

// aanpassing in openFile, herken base64:// urls
//    	String data = Base64StringEncoder.encode(state);
//    	geogebraApplet.openFile(BASE64 + data);
// aanpassing in applet, extra methode
//    	geogebraApplet.loadGGBfile(state);
    	if(hasLoadGGBfile)
			try {
				loadGGBfile.invoke(geogebraApplet, new Object[] { state } );
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		else {
// GGB 4.0 setbase64
			try {
				String data = Base64StringEncoder.encode(state);
				geogebraApplet.getGeoGebraAPI().setBase64(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("end setGGBfile");
   }
    
  //ActionProducer
    private ActionListener actionListener = null;
    
    public void addActionListener(ActionListener l) 
    {   actionListener = AWTEventMulticaster.add(actionListener,l);
    }
    
    public void removeActionListener(ActionListener l)
    {   actionListener = AWTEventMulticaster.remove(actionListener, l);
    }   
    
    public void produceAction(String command)
    {   if (actionListener != null)
        {   actionListener.actionPerformed( new ActionEvent(this, 0, command) );
        }
    }
    //

	public void buildGUI() {
		System.err.println("begin buildGUI");
		try {
			geogebraApplet.buildGUI();
// om te bepalen aanpassing geogebra nodig.
			boolean menuShown = ((Container) geogebraApplet.getComponent(0)).getComponent(0) instanceof JMenuBar;
			menustub.setVisible(!menuShown && resetButton.isVisible());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("end buildGUI");

	}


	/**
	 * Finalizer. Remove geogebra from KeyboardFocusManager.
	 * Anders lekt elke geogebra instance in het geheugen, via deze referentie.
	 */

	protected void finalize() throws Throwable {
		if(geogebraApplet != null) {
			// Is dit alles? Wat er is?
			geogebraApplet.destroy();
			geogebraApplet = null;
		}
	}

	private String id;
	public void setInstanceId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return id;
	}
    
	public String getClassName() {
		return getClass().getName();
	}

	ResourceManagerFactory factory;
	@Override
	public void setFactory(ResourceManagerFactory factory) {
		this.factory = factory;
	}
	
	ResourceManager rm() {
		return factory.getResourceManager();
	}
}
