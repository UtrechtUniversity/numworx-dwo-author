package nl.numworx.geogebra3;


import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageProducer;
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
import java.util.Vector;

import javax.swing.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceManager;

import fi.wiskopdr.ImageComponent;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.ResourceManagerClient;
import v3.geogebra.GeoGebraApplet;

public class Geogebra3Panel extends JLayeredPane implements  ActionListener, InteractiePanel, InteractieEditPanel, AppletStub, AppletContext, ResourceManagerClient, CBookAware
{
	public static int OEFENEN = 0;
	public static int OEFENEN_STRAFPUNTEN = 1;
	public static int ZELFTOETS = 2;
	public static int EINDTOETS = 3;

	private String[] randomVars;
	private Hashtable randomValues;

	private String yAsNaam = "y";

	//private JDialog frame;
	private GeoGebraApplet geogebraApplet;
	
	private Hashtable parameters;
	private boolean editapplet;
	private boolean showAlgebraView = true;
	private boolean showToolBarView = true;
	private int showAlgebraViewShift = WiskOpdr.mac?(-261):(-257);
	private int showToolBarViewShift = 0;//WiskOpdr.mac?(-52):(-49);
	
	private boolean bewaarOptie;
	private boolean alsTool;
	private boolean border;
	private boolean nakijken;
	private boolean checkExternal;
	private boolean nakijkenGemaakteObjecten;
	private String[] geogebraCheckObjects;
	private Hashtable existingObjects;
	private int aantalExistingObjects;
	private int[] geogebraCheckScores;
	
	private JPanel borderPanel;
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
    private int foutStraf = 2;
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
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
			
	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/
	
	public Geogebra3Panel(){
		this(false);
	}
	
	public Geogebra3Panel(boolean editapplet){	
		this.editapplet = editapplet;
		showAlgebraView = editapplet;
		showToolBarView = editapplet;
		
		setLayout(null);
		
		attempts = new Vector();
				
		p = new JPanel(){			
			public void paintComponent(Graphics g)
			{	int x = getBounds().x;
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
		};
		p.setLayout(null);
		p.setOpaque(false);
		add(p);
		
		borderPanel = new JPanel();
		borderPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		setLayer(borderPanel, JLayeredPane.PALETTE_LAYER.intValue());
		borderPanel.setOpaque(false);
		borderPanel.setVisible(false);
		add(borderPanel);
			
		resetButton = new FormuleButton("reseticon");
		resetButton.setSize(15,16);
		resetButton.addActionListener(this);
		setLayer(resetButton, JLayeredPane.PALETTE_LAYER.intValue());
		add(resetButton);
		
		checkButton = new JButton(WiskOpdr.rb.getString("nakijkKnopLabel"));
		checkButton.setSize(80,20);
		checkButton.addActionListener(this);
		checkButton.setVisible(false);
		add(checkButton,0);
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		goedIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		goedIC.setVisible(false);
		add(goedIC,0);
		
		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		halfIC.setVisible(false);
		add(halfIC,0);
		
		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY());
		foutIC.setVisible(false);
		add(foutIC,0);
	}
	
	public void refreshGeogebra(){
		if(geogebraApplet==null)
		{	final GeoGebraApplet deze = 
			geogebraApplet = new GeoGebraApplet();
			p.removeAll();
			geogebraApplet.setStub( this );
			//if(alsTool)makeDefaultParamValues(1);
			//else 
			    if(editapplet) makeDefaultParamValues(2);
			//else makeDefaultParamValues(0);
			Thread thread = new Thread(){
				public void run(){	
					deze.init();	
					deze.start();
				}
			};
			if(editapplet) p.add(geogebraApplet);
			else if(alsTool) p.add(geogebraApplet);
			else p.add(geogebraApplet.getContentPane());
			thread.start();
		}
		setBounds(getBounds().x,getBounds().y,getBounds().width,getBounds().height);
	}
	
	public void repaintGeogebra(){
		if(editapplet)geogebraApplet.repaint();
		else if(alsTool) geogebraApplet.repaint();
		else geogebraApplet.getContentPane().repaint();
	}
	
	public String getParameter(String name){	
		String value = null;
        if(parameters!=null) try{value = (String)parameters.get(name);} catch(Exception e){}
        if(value==null) try{value = ((Boolean)parameters.get(name)).toString();} catch(Exception e){}
		return value;
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
		//System.out.println(s);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==resetButton)
		{ {	
				setGGBfile(launchState);
				//for(int i=0 ; i<randomVars.length ; i++){	
				//	geogebraApplet.setValue("dwo_"+randomVars[i],((Integer)randomValues.get(randomVars[i])).intValue());
				//}
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
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		if (h==null)
			return;
		
		String state = null;
		byte[] ggbFile = null;
		boolean alsTool = false;
		boolean bewaarOptie = false;
		boolean border = false;
		boolean showResetIcon = true;
		boolean file = false;
		String fileUrl = null;
		boolean geogebraNieuw = false;
		Hashtable geogebraParams = new Hashtable();
		boolean nakijken = false;
		boolean checkExternal = false;
		int scoreMax = 10;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean nakijkenGemaakteObjecten = false;
	    String[] geogebraCheckObjects = null;
	    int[] geogebraCheckScores = null;
	        
		
		if (h.containsKey("state"))
			state = (String)h.get("state");
		if (h.containsKey("ggbFile"))
			ggbFile = (byte[])h.get("ggbFile");
		if (h.containsKey("alsTool"))
			alsTool = ((Boolean)h.get("alsTool")).booleanValue();
		if (h.containsKey("bewaarOptie"))
			bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if (h.containsKey("border"))
			border = ((Boolean)h.get("border")).booleanValue();
		if (h.containsKey("showResetIcon"))
			showResetIcon = ((Boolean)h.get("showResetIcon")).booleanValue();
		if (h.containsKey("file"))
			file = ((Boolean)h.get("file")).booleanValue();
		if (h.containsKey("fileUrl"))
			fileUrl = (String)h.get("fileUrl");
		if (h.containsKey("geogebraNieuw"))
			geogebraNieuw = ((Boolean)h.get("geogebraNieuw")).booleanValue();
		if (h.containsKey("geogebraParams"))
			geogebraParams = (Hashtable)h.get("geogebraParams");
		if (h.containsKey("nakijken"))
			nakijken = ((Boolean) h.get("nakijken")).booleanValue();
		if (h.containsKey("checkExternal"))
			checkExternal = ((Boolean) h.get("checkExternal")).booleanValue();
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if (h.containsKey("logOption"))
	    	logOption = ((Boolean)h.get("logOption")).booleanValue();
		if (h.containsKey("logID"))
			logID = (String)h.get("logID");
		if (h.containsKey("check"))
			check = ((Boolean)h.get("check")).booleanValue();
		if (h.containsKey("teltMee"))
			teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if (h.containsKey("nakijkenGemaakteObjecten"))
			nakijkenGemaakteObjecten = ((Boolean) h.get("nakijkenGemaakteObjecten")).booleanValue();
        if (h.containsKey("geogebraCheckObjects"))
        	geogebraCheckObjects = (String[]) h.get("geogebraCheckObjects");
        if (h.containsKey("geogebraCheckScores"))
        	geogebraCheckScores = (int[]) h.get("geogebraCheckScores");
       
        this.alsTool = alsTool;
		this.bewaarOptie = bewaarOptie;
		this.border = border;
		this.parameters = geogebraParams;
		this.nakijken = nakijken;
		this.checkExternal = checkExternal;
		this.scoreMax = scoreMax;
	    this.logOption = logOption;
	    this.logID = logID;
	    this.check = check;
	    this.teltMee = teltMee;
	    this.nakijkenGemaakteObjecten = nakijkenGemaakteObjecten;
	    this.geogebraCheckObjects = geogebraCheckObjects;
	    this.geogebraCheckScores = geogebraCheckScores;
	    
		checkButton.setVisible(nakijken && (mode == OEFENEN || mode == OEFENEN_STRAFPUNTEN) && !checkExternal);
        
		if (alsTool) 
		{
			showAlgebraView = true;
			showToolBarView = true;
		}
		borderPanel.setVisible(border);
		resetButton.setVisible(showResetIcon);
		
		if (geogebraNieuw)
			showAlgebraViewShift = -1;
		
		refreshGeogebra();
		
		try
		{
			if (alsTool)
			{	
				if (Boolean.TRUE.equals(file) && fileUrl != null)
				{
					ResourceContainer unit = rm().getInstanceContainer();
					URL u; //u = unit.open(fileUrl).getURL();
					u = new URL(unit.getURL(), fileUrl);
					openGGBfile(u);
				}
				else if(ggbFile != null && hasLoadGGBfile)
					setGGBfile(ggbFile);
				else
					setGGBXML(state);
				
				for (int i=0 ; i<randomVars.length ; i++)
				{	
					String varClean = StringUtils.replaceStr(randomVars[i],"?(","");
					varClean = StringUtils.replaceStr(varClean,")","");
					geogebraApplet.setValue("dwo_"+varClean,((Integer)randomValues.get(randomVars[i])).intValue());
				}
			}
			else
			{	
				if (Boolean.TRUE.equals(file) && fileUrl != null)
				{
					ResourceContainer unit = rm().getInstanceContainer();
					URL u = new URL(unit.getURL(), fileUrl);
					openGGBfile(u);
				}
				else if (ggbFile != null && hasLoadGGBfile)
					setGGBfile(ggbFile);
				else
				{
					if (state == null)
						state = "";
					if (!geogebraNieuw)
						state = StringUtils.replaceStr(state,"<show algebraView=\"false\"", "<show algebraView=\"true\"");
					setGGBXML(state);
				}
				
				for (int i=0; i<randomVars.length; i++)
				{	
					String varClean = StringUtils.replaceStr(randomVars[i],"?(","");
					varClean = StringUtils.replaceStr(varClean,")","");
					geogebraApplet.setValue("dwo_"+varClean,((Number)randomValues.get(randomVars[i])).intValue());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		launchState = getGGBfile(); // voor reset
		
		//existingObjects = new Hashtable();
        //String[] existingObjectNames = geogebraApplet.getAllObjectNames();
        //for (int i = 0; i < existingObjectNames.length; i++) {
        //    existingObjects.put(existingObjectNames[i], geogebraApplet.getValueString(existingObjectNames[i]));
        //}
		String[] existingObjectNames = geogebraApplet.getAllObjectNames();
		aantalExistingObjects = existingObjectNames.length;
        
		this.randomVars = randomVars;
		this.randomValues = randomValues;
		
		repaintGeogebra();
	}

	private void openGGBfile(URL u)
	{
		try
		{
			geogebraApplet.openFile(u.toExternalForm());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setGGBXML(String state)
	{
		try
		{
			geogebraApplet.setXML(state);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// set suspend state (recover the state in which a student left it when finishing the activity)
	public void setState(Hashtable h)
	{	
		if (h==null)
			return;
		
		String state = null;
		//byte[] ggbFile = null;
		boolean ingevuld = false;
		boolean nagekeken = false;
		Vector attempts = new Vector();
		int attemptsCount = 0;
		//int errorCount = 0;
	        
		if (h.containsKey("state"))
			state = (String)h.get("state");
		//if(h.containsKey("ggbFile")) ggbFile = (byte[])h.get("ggbFile");
		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
	    if (h.containsKey("nagekeken"))
	    	nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
	    if (h.containsKey("attempts"))
	    	attempts = OpdrNavStruct.toVector(h.get("attempts"));
	    if (h.containsKey("attemptsCount"))
	    	attemptsCount = ((Number)h.get("attemptsCount")).intValue();
	    if (h.containsKey("errorCount"))
	    	errorCount = ((Number)h.get("errorCount")).intValue();
        
		if (bewaarOptie)
		{	
			//if(ggbFile != null && hasLoadGGBfile)
			//	setGGBfile(ggbFile);
			//else {
				setGGBXML(state);
			//}
		}
		repaint();
		if (ingevuld && (mode == OEFENEN || nagekeken))
			kijkNa();
	}
	
	// get the suspend state (the state in which a student left it when finishing the activity)
	public Hashtable getState()
	{
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

	    if (!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant)))
	    	kijkNa(false);
		if (logOption)
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
        
        if (bewaarOptie)
        {	
			//if(hasLoadGGBfile) h.put("ggbFile", getGGBfile());
			state = geogebraApplet.getXML();
			h.put("state", state);
		}
		 return h;
	}
	
	// set launch state in the editor
	public void setEditState(Hashtable h)
	{
		if (h == null)
			return;
		
		String state = null;
		byte[] ggbFile = null;
		boolean alsTool = false;
		boolean bewaarOptie = false;
		boolean border = false;
		boolean showResetIcon = false;
		boolean file = false;
		String fileUrl = null;
		boolean geogebraNieuw = false;
		Hashtable geogebraParams = new Hashtable();
		
		if (h.containsKey("state"))
			state = (String)h.get("state");
		if (h.containsKey("ggbFile"))
			ggbFile = (byte[]) h.get("ggbFile");
		if (h.containsKey("alsTool"))
			alsTool = ((Boolean)h.get("alsTool")).booleanValue();
		if (h.containsKey("bewaarOptie"))
			bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if (h.containsKey("border"))
			border = ((Boolean)h.get("border")).booleanValue();
		if (h.containsKey("showResetIcon"))
			showResetIcon = ((Boolean)h.get("showResetIcon")).booleanValue();
		if (h.containsKey("file"))
			file = ((Boolean)h.get("file")).booleanValue();
		if (h.containsKey("fileUrl"))
			fileUrl = (String)h.get("fileUrl");
		if (h.containsKey("geogebraNieuw"))
			geogebraNieuw = ((Boolean)h.get("geogebraNieuw")).booleanValue();
		if (h.containsKey("geogebraParams"))
			geogebraParams = (Hashtable)h.get("geogebraParams");
        
		this.alsTool = alsTool;
		this.bewaarOptie = bewaarOptie;
		this.border = border;
		if (!editapplet)
			this.parameters = geogebraParams;
		
		if (alsTool)
		{	
			showAlgebraView = true;
			showToolBarView = true;
		}
		
		if (!editapplet)
		{
			borderPanel.setVisible(border);
			resetButton.setVisible(showResetIcon);
		}
		
		if (geogebraNieuw)
			showAlgebraViewShift = -1;
		refreshGeogebra();

		if (Boolean.TRUE.equals(file) && fileUrl != null)
		{
			ResourceContainer unit = rm().getInstanceContainer();
			try
			{
				URL u; //u = unit.open(fileUrl).getURL();
				u = new URL(unit.getURL(), fileUrl);
				openGGBfile(u);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (ggbFile != null && hasLoadGGBfile)
		{
			setGGBfile(ggbFile);
		} 
		else if (editapplet)
		{	
			setGGBXML(state);
		}
		else if (alsTool)
		{	
			setGGBXML(state);
		}
		else
		{
			if (!geogebraNieuw)
				state = StringUtils.replaceStr(state,"<show algebraView=\"false\"", "<show algebraView=\"true\"");
			setGGBXML(state);
		}
		
		repaintGeogebra();
		
		try
		{
			if (WiskOpdr.lookAndFeel != null)
				UIManager.setLookAndFeel(WiskOpdr.lookAndFeel);
		}
		catch (UnsupportedLookAndFeelException ex)
		{
		}
		//System.out.println("lookandfeel"+WiskOpdr.lookAndFeel);
		
	}
	
	public void kijkNa(boolean show)
	{
		if (!nakijken)
			return;
		if (huidigIC != null)
			huidigIC.setVisible(false);

		// ingevuld = false;

		correct = false;
		fout = true;
		score = 0;

		String[] allObjectNames = geogebraApplet.getAllObjectNames();
		if (nakijkenGemaakteObjecten && allObjectNames.length < aantalExistingObjects)
		{
			huidigIC = foutIC;
			correct = false;
			fout = true;
			score = 0;
			if (show && check)
				huidigIC.setVisible(true);
		}

		else if (nakijkenGemaakteObjecten)
		{

			String[] objectNames = new String[allObjectNames.length - aantalExistingObjects];
			for (int i = 0; i < objectNames.length; i++)
			{

				objectNames[i] = allObjectNames[i + aantalExistingObjects];
			}
			String[] objectTypes = new String[objectNames.length];
			String[] valueStrings = new String[objectNames.length];
			String[] objectStrings = new String[objectNames.length];

			String[] checkObjects = new String[geogebraCheckObjects.length];
			for (int j = 0; j < checkObjects.length; j++)
			{
				checkObjects[j] = geogebraCheckObjects[j];
			}

			int matches = 0;
			for (int i = 0; i < objectNames.length; i++)
			{
				objectTypes[i] = geogebraApplet.getObjectType(objectNames[i]);
				valueStrings[i] = geogebraApplet.getValueString(objectNames[i]);

				objectStrings[i] = StringUtils.replaceStr(valueStrings[i], objectNames[i] + "(x)", "");
				objectStrings[i] = StringUtils.replaceStr(objectStrings[i], objectNames[i], "");
				objectStrings[i] = StringUtils.replaceStr(objectStrings[i], " ", "");
				objectStrings[i] = objectStrings[i].substring(1);
				boolean match = false;

				int matchNr = -1;
				for (int j = 0; j < checkObjects.length; j++)
				{
					match = checkObjects[j].equals(objectStrings[i]);
					if (!match)
					{
						String command = "checkDWO=" + checkObjects[j] + "==" + objectNames[i];
						boolean check = false;
						if (!objectTypes[i].equals("boolean"))
						{
							check = geogebraApplet.evalCommand(command);
							match = geogebraApplet.getValue("checkDWO") == 1.0;

						}
					}
					if (match)
					{
						if (show)
							geogebraApplet.setColor(objectNames[i], 0, 180, 0);
						score += geogebraCheckScores[j];
						matches++;
						matchNr = j;
						break;
					}
				}
				if (match && checkObjects.length > 0)
				{
					String[] checkObjectsRes = new String[checkObjects.length - 1];
					int teller = 0;
					for (int k = 0; k < checkObjects.length; k++)
					{
						if (k != matchNr)
						{
							checkObjectsRes[teller] = checkObjects[k];
							teller++;
						}
					}
					checkObjects = checkObjectsRes;
				}
			}

			if (matches == geogebraCheckObjects.length)
			{
				huidigIC = goedIC;
				correct = true;
				fout = false;
				score = scoreMax;
			}
			else if (matches > 0)
			{
				huidigIC = halfIC;
				correct = false;
				fout = false;

			}
			else
			{
				huidigIC = foutIC;
				correct = false;
				fout = true;
				score = 0;
			}
			if (show && check)
				huidigIC.setVisible(true);
		}
		else
		{
			boolean juist = true;

			double geogebraCorrect = geogebraApplet.getValue("checkDWO");
			String type = geogebraApplet.getValueString("A");
			// System.out.println(type+" "+geogebraCorrect);
			if (geogebraCorrect != 1.0)
				juist = false;

			if (juist)
			{
				huidigIC = goedIC;
				correct = true;
				fout = false;
				score = scoreMax;
			}
			else
			{
				huidigIC = foutIC;
				correct = false;
				fout = true;
				score = 0;
			}
			if (show && check)
				huidigIC.setVisible(true);
		}
		
		if (ingevuld && show)
			produceAction("changed");
	}   
	    
	
	public Hashtable getEditState() {		
		Hashtable h = new Hashtable();
		h.put("geogebraNieuw", new Boolean (true));
		//String state = null;
		//if(editapplet) state = geogebraApplet.getXML();
		//else if(alsTool) geogebraApplet.getXML();
		//else state = geogebraApplet.getXML();
		if(hasLoadGGBfile) h.put("ggbFile", getGGBfile());
		//if(state!=null)h.put("state", state);
	    return h;
	}
	
	public InteractieEditPanel getEditPanel(){	
		return newEditPanel(getInstanceId());
	}
	
	public void zetBreedte(int b){	
	}
	
	public void zetHoogte(int h){	
	}
	
	public void setBounds(int x, int y, int b, int h){	
	    super.setBounds(x,y,b,h);
	    borderPanel.setBounds(0,0,b,h-(nakijken?25:0));
		p.setBounds(0,0,b,h-(nakijken?25:0));
		resetButton.setLocation(b-24,4);
		if(parameters!=null && parameters.containsKey("showToolBar") && "true".equals(parameters.get("showToolBar")))
			resetButton.setLocation(getWidth()-40,4);
		
		checkButton.setLocation(b/2-40,h-20);
		goedIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		halfIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		foutIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
        
		int corr = 0;
		int corrP = 0;
		if(!showAlgebraView) 
		{	corr = showAlgebraViewShift;
			corrP = 1-showToolBarViewShift;
		}
		if(geogebraApplet==null)return;
		
		if(editapplet)geogebraApplet.setBounds(corr,-corrP,b-corr+corrP,h+2*+corrP-(nakijken?25:0));
		else if(alsTool) geogebraApplet.setBounds(corr,-corrP,b-corr+corrP,h+2*+corrP-(nakijken?25:0));
		else geogebraApplet.getContentPane().setBounds(corr,-corrP,b-corr+corrP,h+2*+corrP+1-(nakijken?25:0));
	}
	
	public void setSize(int b, int h){	
	    super.setSize(b,h);
        
	    borderPanel.setSize(b,h-(nakijken?25:0));
	    p.setSize(b,h-(nakijken?25:0));
		resetButton.setLocation(b-24,4);
		if(parameters!=null && parameters.containsKey("showToolBar") && "true".equals(parameters.get("showToolBar")))
			resetButton.setLocation(getWidth()-40,4);
		
		checkButton.setLocation(b/2-40,h-20);
		goedIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		halfIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
		foutIC.setLocation(checkButton.getX() + checkButton.getWidth(),checkButton.getY()-5);
        
		
		int corr = 0;
		int corrP = 0;
		if(!showAlgebraView){	
			corr = showAlgebraViewShift;
			corrP = 1-showToolBarViewShift;
		}
		if(geogebraApplet==null)return;
		
		if(editapplet)geogebraApplet.setSize(b-corr+corrP,h+2*+corrP-(nakijken?25:0));
		else if(alsTool) geogebraApplet.setSize(b-corr+corrP,h+2*+corrP-(nakijken?25:0));
		else geogebraApplet.getContentPane().setSize(b-corr+corrP,h+2*+corrP+1-(nakijken?25:0));
	}
	
	public void resize(){	
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte(){return 10;}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore()
	{
		if (mode == OEFENEN_STRAFPUNTEN)
			return Math.max(0, score - errorCount * foutStraf);

		return score;
	}
	
	public int[][] getScoreObjectives()
	{	return null;
	}
	
	public int getScoreMax(){
		if(nakijken)return scoreMax;
		else return 0;
	}
	
	public boolean isCorrect()
	{
		if (nakijken)
			return correct;
		else
			return true;
	}
	
	public boolean isFout()
	{
		if (nakijken)
			return fout;
		else
			return false;
	}
	
	public void zetMode(int mode)
	{
		this.mode = mode;
		checkButton.setVisible(nakijken && (mode == OEFENEN || mode == OEFENEN_STRAFPUNTEN) && !checkExternal);
	}
	
	public void zetNagekeken(boolean b)
	{
		if (ingevuld)
			nagekeken = b;
	}
	
    public void stop(){
    	//if(frame!=null) frame.setVisible(false);
    	kijkNa();
    	
    }
    
    public void start(){
    	try {
			if(WiskOpdr.lookAndFeel != null)
				UIManager.setLookAndFeel(WiskOpdr.lookAndFeel);
			} catch (UnsupportedLookAndFeelException e) {
		}
	}
    
    public void destroy(){
    	/**/
    	Thread thread = new Thread(){
			public void run(){	
				geogebraApplet.stop();	
				//geogebraApplet.destroy();
				p.remove(geogebraApplet);
				geogebraApplet = null;
				//System.gc();
				//System.out.println("destroyed");
			}
		};
		thread.start();
    }
    
    public void opnieuw(){}
    
    public void kijkNa()
    {
    	kijkNa(true);
    	if(correct && cbookEventHandler.hasListeners("action.correct"))
    		cbookEventHandler.fire("action.correct");
    	if(fout && cbookEventHandler.hasListeners("action.false"))
    		cbookEventHandler.fire("action.false");
    	if(fout && errorCount>0 && cbookEventHandler.hasListeners("action.false_2"))
    		cbookEventHandler.fire("action.false_2");
    }
    
    public void kijkNa(int stapNr){
    	kijkNa();
    }
    
    //public void addActionListener(ActionListener al){}
    
    public InteractiePanel getInteractiePanel()	{
		return this;
	}
	
    // AppletStub methodes
    public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){
		URL codeBase = null;
		//try {
		//	new URL(WiskOpdr.applet.getCodeBase().toString() + "/jars");
		//}catch(Exception e){}
		//return WiskOpdr.applet.getCodeBase();
		try {
			//codeBase = new URL("http://www.fi.uu.nl/dwo/jars/");
			//codeBase = new URL("http://dwo.fi.uu.nl/dwo/jars/");
// FIXME dit kan vast beter: maak altijd de codebase van dwo gelijk!
// Codebase voor de geogebra jars is niet altijd gelijk aan de codebase van de applet
// standaard: ws.fisme.science.uu.nl/javaclasses/ 
// geogebra:  ws.fisme.science.uu.nl/dwo/jars/
// bij dme.colorado.edu, dwoapp.appspot.com en noordhoff (Henk) zijn ze wel gelijk!
			codeBase = WiskOpdr.applet.getCodeBase();
			if("ws.fisme.science.uu.nl".equals(codeBase.getHost()))
					codeBase = new URL("http://ws.fisme.science.uu.nl/dwo/jars/");
		} catch (MalformedURLException e) {
		}
		return codeBase;
	}
	public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    	
    // AppletContext methodes
    public AudioClip getAudioClip( URL url ){return null;}
    public Image getImage( URL url )
    {	Toolkit tk = Toolkit.getDefaultToolkit();
		try
		{	ImageProducer prod = (ImageProducer) url.getContent();
		    return tk.createImage( prod );
		}
		catch ( IOException e )
		{
			return null;
		}
	}
	public Applet getApplet( String name ){return null;}
    public Enumeration getApplets(){return null;}
    public void setStream(String s, InputStream is){}
    public InputStream getStream(String s){return null;}
    public Iterator getStreamKeys(){return null;}
	public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}
	
    
    // aanpassingen Wim load/save ggb files
	private static final String BASE64 = "base64://" ;
	private static final String UTF8 = "UTF-8";
	private static boolean hasLoadGGBfile = false; // hoe te bepalen?
	private static Method  loadGGBfile;
	static {
		try {
			hasLoadGGBfile = null != (loadGGBfile =
			GeoGebraApplet.class.getMethod("loadGGBfile" , new Class[] { byte[].class } ));
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// expected...
		}
		
		
	}
    private byte[] getGGBfile() 
    {
    	if(!hasLoadGGBfile)
			try {
				return geogebraApplet.getXML().getBytes(UTF8);
			} catch (UnsupportedEncodingException e) {
				return geogebraApplet.getXML().getBytes();
			}
    	return geogebraApplet.getGGBfile();
    }
    
    private void setGGBfile(byte[] state)
    {
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
		else
			try {
				setGGBXML(new String(state, UTF8));
			} catch (UnsupportedEncodingException e) {
				setGGBXML(new String(state));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
    	
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
	ResourceManagerFactory factory;
	@Override
	public void setFactory(ResourceManagerFactory factory) {
		this.factory = factory;
	}
	
	ResourceManager rm() {
		return factory.getResourceManager();
	}

	String id;
	@Override
	public void setInstanceId(String id) {
		this.id = id;
	}

	@Override
	public String getInstanceId() {
		return id;
	}

	@Override
	public String getClassName() {
		return getClass().getName();
	}

	public static InteractieEditPanel newEditPanel(String id) {
		return new Geogebra3EditPanel(id);
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
		
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = {"action.correct",
				"action.false",
				"action.false_2"};
		return commands;
	}

	@Override
	public String[] getAcceptedCmds() {
		return null;
		//return new String[] { "action.setNotEditable" };
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}

}
