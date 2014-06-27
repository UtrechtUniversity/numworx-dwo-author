package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Image;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.beans.wiskopdrbeans.*;
import fi.beans.iconan.Iconan;
import fi.beans.stringutils.*;

public class CheckSleepUnitPanel extends JPanel implements InteractiePanel, ActionListener
{
	//static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;
	
    private int mode;
    
    private boolean ingevuld;
    private boolean nagekeken;
    
    private boolean correct;
    private boolean fout;
    
    private int attemptsCount;
	private Vector attempts;
    
    private Point[] randomizedPositions;
    private boolean positionsRandomized;
    
    private boolean snapToTarget;
    private boolean randomizePositions;
    
    private int acceptedMarge;
    private Point[] positions;
    
    private boolean checkFormule = false;
	private String formuleString = "$f@";
	private String[] formuleStrings = null;
    
	private int errorCount;
    private int score;
    private int scoreMax=10;
    
	static int GOED = 1;
	static int FOUT = 0;
	static int HALF = 2;
	static int GEEN = 3;
	
	private FormuleButton checkButton;
	private InteractiePanel[] ipListSleep;
	private InteractiePanel[] ipListDoel;
	
	private int aantalSleepObjects;
	private int aantalDoelObjects;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	
	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;
	
	private boolean check;
	private boolean teltMee;
	
	private String answer;
	
	private boolean relocate;
	private boolean view = false;
	private boolean verzamelDoel;
	
	/*private static String[] imageNames = 
	{	"goedkrul.gif",
		"goedkrulhalf.gif",
		"foutkruis.gif",
		"goedkrul_en.gif",
		
	};*/
	
	//private static Hashtable images;
		
	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/
	
	public CheckSleepUnitPanel()
	{	
		setLayout(null);
		setOpaque(false);
		
		attempts = new Vector();
		
		checkButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		checkButton.setBounds(0,5,80,20);
		checkButton.addActionListener(this);
		add(checkButton);
		
		setSize(checkButton.getWidth()+30, 25);
		
		/*if(images==null)
		{	images = new Hashtable();
			WiskOpdr.loadImages(images,imageNames);
		}*/
		
		goedIC = new ImageComponent(WiskOpdr.GOEDKRUL);
		//if(WiskOpdr.language.toString().equals("en"))goedIC = new ImageComponent(getImage("goedkrul_en.gif"));
		goedIC.setLocation(checkButton.getWidth(),0);
		goedIC.setVisible(false);
		add(goedIC);
		
		halfIC = new ImageComponent(WiskOpdr.HALFKRUL);
		halfIC.setLocation(checkButton.getWidth(),0);
		halfIC.setVisible(false);
		add(halfIC);
		
		foutIC = new ImageComponent(WiskOpdr.FOUTKRUIS);
		foutIC.setLocation(checkButton.getWidth(),0);
		foutIC.setVisible(false);
		add(foutIC);
		
		
	}
	
	/*public static Image getImage(String name)
	{	return(Image)images.get(name);
	}*/
	
	public void randomizePositions()
	{
		Vector v = new Vector();
		randomizedPositions = new Point[aantalSleepObjects];
		for(int i=0 ; i<ipListSleep.length ; i++)
		{	if(!(ipListSleep[i] instanceof TekstVakPanel) || !((TekstVakPanel)ipListSleep[i]).isZwevend())return;
			v.addElement(((TekstVakPanel)ipListSleep[i]).geefLocatie());
		}
		for(int i=0 ; i<ipListSleep.length ; i++)
		{	int r = (int)((ipListSleep.length-i)*Math.random());
			Point p = (Point)(v.elementAt(r));
			if(!positionsRandomized) randomizedPositions[i] = p;
			((TekstVakPanel)ipListSleep[i]).zetLocatie(p.x, p.y);
			v.removeElementAt(r);
		}
		positionsRandomized = true;
		for(int i=0 ; i<ipListSleep.length ; i++)
	    { 	((TekstVakPanel)ipListSleep[i]).setStartSleep(randomizedPositions[i].x, randomizedPositions[i].y);
	    }
	       
        
		(((TekstInteractiePanelVak)((Component)ipListSleep[0]).getParent()).getTekstVak()).layoutTekst();
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		int aantalSleepObjects = 0;
		int aantalDoelObjects = 0;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean snapToTarget = false;
		int acceptedMarge = 10;
		boolean checkFormule = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean relocate = false;
		boolean view = false;
		boolean verzamelDoel = false;
		boolean[][] logObjectives = null;
		String knopImageString = "";
		
		if(h.containsKey("aantalSleepObjects")) aantalSleepObjects = ((Integer)h.get("aantalSleepObjects")).intValue();
        if(h.containsKey("aantalDoelObjects")) aantalDoelObjects = ((Integer)h.get("aantalDoelObjects")).intValue();
        if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("snapToTarget")) snapToTarget = ((Boolean)h.get("snapToTarget")).booleanValue();
	    if(h.containsKey("acceptedMarge")) acceptedMarge = ((Integer)h.get("acceptedMarge")).intValue();
	    if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
	    if(h.containsKey("formuleString")) formuleString = (String)h.get("formuleString");
	    if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("relocate")) relocate = ((Boolean)h.get("relocate")).booleanValue();
		if(h.containsKey("view")) view = ((Boolean)h.get("view")).booleanValue();
		if(h.containsKey("verzamelDoel")) verzamelDoel = ((Boolean)h.get("verzamelDoel")).booleanValue();
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		for(int i=0 ; formuleStrings!=null && i<formuleStrings.length ; i++)
        {	try{
				formuleStrings[i] = FormuleParser.randomizeString(formuleStrings[i], randomVars, randomValues);
	    	}
	    	catch(Exception e){	}
        }
		try{
			formuleString = FormuleParser.randomizeString(formuleString, randomVars, randomValues);
    	}
    	catch(Exception e){	}
	    this.aantalSleepObjects = aantalSleepObjects;
        this.aantalDoelObjects = aantalDoelObjects;
        this.scoreMax = scoreMax;
        this.randomizePositions = randomizePositions;
        this.snapToTarget = snapToTarget;
        this.acceptedMarge = acceptedMarge;
        this.checkFormule = checkFormule;
        this.formuleString = formuleString;
        this.formuleStrings = formuleStrings;
        this.logOption = logOption;
        this.logID = logID;
        this.check = check;
        this.teltMee = teltMee;
        this.relocate = relocate;
        this.view = view;
        this.verzamelDoel = verzamelDoel;
        this.logObjectives = logObjectives;
       
        Point[] doelPosities = new Point[aantalDoelObjects];
        ipListDoel = new InteractiePanel[aantalDoelObjects];
        for(int i=0 ; i<ipListDoel.length ; i++)
        {   ipListDoel[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(-(i+1));
            ipListDoel[i].addActionListener(this);
            doelPosities[i] = ((TekstVakPanel)ipListDoel[i]).geefLocatie();
        }
        
        ipListSleep = new InteractiePanel[aantalSleepObjects];
        for(int i=0 ; i<ipListSleep.length ; i++)
        {   ipListSleep[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
            ipListSleep[i].addActionListener(this);
            ((TekstVakPanel)ipListSleep[i]).zetSleepDoelPosities(doelPosities);
            ((TekstVakPanel)ipListSleep[i]).zetSleepdoelMarge(acceptedMarge);
            ((TekstVakPanel)ipListSleep[i]).zetSleepSnap(snapToTarget);
            ((TekstVakPanel)ipListSleep[i]).setRelocate(relocate);
            if(relocate)((TekstVakPanel)ipListSleep[i]).setStartSleep();
        }
        
        
        if(randomizePositions && !positionsRandomized) randomizePositions();
        
    	if(knopImageString!=null && !"".equals(knopImageString))
		{   Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Image knopImage = iconman.getImage(knopImageString);
	    	checkButton.setPopupButtonImage(knopImage);
		    int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
	    }
	}
	
	public void setState(Hashtable h)
	{
	    Point[] randomizedPositions = null;
	    boolean ingevuld = false;
	    boolean nagekeken = false;
	    Point[] positions = null;
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
       
	    if(h.containsKey("randomizedPositions")) randomizedPositions = (Point[])h.get("randomizedPositions");
	    if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
	    if(h.containsKey("nagekeken")) nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
	    if(h.containsKey("positions")) positions = (Point[])h.get("positions");
	    if(h.containsKey("attempts"))attempts = (Vector)h.get("attempts");
	    if(h.containsKey("attemptsCount")) attemptsCount = ((Integer)h.get("attemptsCount")).intValue();
	    if(h.containsKey("errorCount")) errorCount = ((Integer)h.get("errorCount")).intValue();
        
        this.randomizedPositions = randomizedPositions;
        this.ingevuld = ingevuld;
        this.nagekeken = nagekeken;
        this.attempts = attempts;
        this.attemptsCount = attemptsCount;
	    this.errorCount = errorCount;
        
        if(randomizePositions) 
        {   for(int i=0 ; i<ipListSleep.length ; i++)
	        {   
	        	((TekstVakPanel)ipListSleep[i]).setStartSleep(randomizedPositions[i].x, randomizedPositions[i].y);
	           
	        }
	       
        }
        
        for(int i=0 ; i<ipListSleep.length ; i++)
        {   
        	Point p = positions[i];
            ((TekstVakPanel)ipListSleep[i]).zetLocatie(p.x, p.y);
        }
        (((TekstInteractiePanelVak)((Component)ipListSleep[0]).getParent()).getTekstVak()).layoutTekst();
        
        
        if(ingevuld && (mode==0 || mode==1 || nagekeken)) kijkNa();
	}
	
	public void setEditState(Hashtable h)
	{
		String knopImageString = "";
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		if(knopImageString!=null && !"".equals(knopImageString))
		{  Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Image knopImage = iconman.getImage(knopImageString);
	    	checkButton.setPopupButtonImage(knopImage);
		    int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
	    }
    }
	
	public Hashtable getState()
	{   
	    Point[] randomizedPositions = null;
	    boolean ingevuld = false;
	    boolean nagekeken = false;
	    Point[] positions = null;
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
		
	    randomizedPositions = this.randomizedPositions;
	    ingevuld = this.ingevuld;
	    nagekeken = this.nagekeken;
	    
	    attempts = this.attempts;
	    attemptsCount = this.attemptsCount;
	    errorCount = this.errorCount;

	    positions = new Point[ipListSleep.length];
	    for(int i=0 ; i<ipListSleep.length ; i++)
        {   positions[i] = ((TekstVakPanel)ipListSleep[i]).geefLocatie();
        }
	    
	    answer = "";
	    if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(false);
		if(logOption)
		{	
	    	Hashtable logMap = new Hashtable();
			
	    	String logString = answer;
			//String[] options = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z"};
			//for(int i=0 ; i<ipList.length ; i++)
	        //{   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
	        //    if(((TekstVakPanel)ipList[i]).isIpSelected() && i<options.length) logString = logString + options[i];
	        //}
			logMap.put("logAnswer", logString);
			logMap.put("logScore", new Integer(score));
			logMap.put("logMaxScore", new Integer(scoreMax));
			logMap.put("logErrorCount", new Integer(errorCount));
			logMap.put("logAttemptsCount", new Integer(attemptsCount));
			logMap.put("logAttempts", attempts);
			
			WiskOpdr.setLog(logID, logMap);
		}
         
	    Hashtable h = new Hashtable();
        if(randomizedPositions!=null) h.put("randomizedPositions", randomizedPositions);
        h.put("ingevuld", new Boolean(ingevuld));
        h.put("nagekeken", new Boolean(nagekeken));
        h.put("positions", positions);
        h.put("attempts", attempts);
        h.put("attemptsCount", new Integer(attemptsCount));
        h.put("errorCount", new Integer(errorCount));
        
        return h;
	}
	
	public void setAttempt()
	{
		String goedFout = "";
		if(huidigIC == goedIC && huidigIC.isVisible())goedFout = "goed";
		if(huidigIC == halfIC && huidigIC.isVisible())goedFout = "half";
		if(huidigIC == foutIC && huidigIC.isVisible())goedFout = "fout";
		
		String logString = "";
		//String[] options = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z"};
		//for(int i=0 ; i<ipList.length ; i++)
		// {   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
        //    if(((TekstVakPanel)ipList[i]).isIpSelected() && i<options.length) logString = logString + options[i];
        //}
		
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
	
	public Hashtable getEditState()
	{
		return null;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new CheckSleepUnitEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void wis()
	{
		ipListSleep = null;
		aantalSleepObjects = 0;
		
	    if(huidigIC!=null)
	    {	huidigIC.setVisible(false);
	    	
	    }
	   
	    correct = false;
	    score = 0;
	    nagekeken = false;
	    ingevuld = false;
	    errorCount = 0;
	    attemptsCount = 0;
	    attempts = new Vector();
	}
	
	public void zetMaat()
	{
		setSize(checkButton.getWidth()+30, Math.max(25,checkButton.getHeight()+5));
		goedIC.setLocation(checkButton.getWidth(),0);
		halfIC.setLocation(checkButton.getWidth(),0);
		foutIC.setLocation(checkButton.getWidth(),0);
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public int geefAsHoogte()
	{	return checkButton.getHeight()/2+11;
	}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	
	public int getScore()
	{	if(!teltMee) return 0;
	    return score;
	}
	
	public int[][] getScoreObjectives()
	{	if(logObjectives==null)return null;
		int[][] scoreObjectives = new int[logObjectives.length][];
		for(int i =0; i<logObjectives.length; i++)
			scoreObjectives[i] = new int[logObjectives[i].length];
		for(int i=0 ; i<logObjectives.length ; i++)
			for(int j = 0; j<logObjectives[i].length; j++)
		{	if(logObjectives[i][j]) scoreObjectives[i][j] = score;
		}
		return scoreObjectives;
	}
	
	public int getScoreMax()
	{	if(!teltMee) return 0;
	    return scoreMax;
	}

	public boolean isCorrect()
	{	if(!teltMee)return true;
	    return correct;
	}
	
	public boolean isFout()
	{	if(!teltMee)return false;
	    return fout;
	}
	
	public void zetMode(int mode)
    {   this.mode = mode;
    	checkButton.setVisible(mode==0 || mode==1);
    }
	
	public void zetNagekeken(boolean b)
	{	if(ingevuld) nagekeken = b;
	}
	
    public void stop()
    {
        kijkNa();
        //if(ingevuld) produceAction("changed");
    }
    
    public void start(){}
    
    public void destroy(){}
    
    public void opnieuw()
    { 	positionsRandomized = false;
    	if(randomizePositions) randomizePositions();
    	score = 0;
		correct = false;
    }
    
    public void kijkNa()
    {
    	kijkNa(true);
    }
    
    public void kijkNa(boolean show)
    {
        if(huidigIC!=null) huidigIC.setVisible(false);
        
        boolean juist = true;
        answer = "";
        //ingevuld = true;
        
        correct = false;
        fout = true;
        score = 0;
        
        
        
        Point[] doelPosities = new Point[aantalDoelObjects];
        ipListDoel = new InteractiePanel[aantalDoelObjects];
        for(int i=0 ; i<ipListDoel.length ; i++)
        {   ipListDoel[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(-(i+1));
            doelPosities[i] = ((TekstVakPanel)ipListDoel[i]).geefLocatie();
        }
        
        Point[] posities = new Point[aantalSleepObjects];
        ipListSleep = new InteractiePanel[aantalSleepObjects];
        TekstVakPanel[] sleepObjecten = new TekstVakPanel[aantalSleepObjects];
        
        for(int i=0 ; i<ipListSleep.length ; i++)
        {   ipListSleep[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
            ipListSleep[i].addActionListener(this);
            posities[i] = ((TekstVakPanel)ipListSleep[i]).geefLocatie();
            sleepObjecten[i] = (TekstVakPanel)ipListSleep[i];
        }
        
        if(checkFormule)
        {
        	if(formuleStrings!=null)
        	{
        		for(int i=0 ; i<aantalSleepObjects ; i++)
    	        {   ((TekstVakPanel)sleepObjecten[i]).wisGoedFout();
    	        }
        		
        		boolean hasLocationStrings = false;
        		VergelijkingMeerv[] v = new VergelijkingMeerv[formuleStrings.length];
        		for(int h=0 ; h<formuleStrings.length ; h++)
		        {
        			String formuleString = formuleStrings[h];
        			String locationStringTotal = null;
        			String[] locationStrings = null;
        			
        			
        			int indexSC = formuleStrings[h].indexOf(";");
        			if(indexSC>-1){
        				formuleString = formuleStrings[h].substring(0,indexSC) + "@";
        				locationStringTotal = formuleStrings[h].substring(indexSC+1, formuleStrings[h].length()-1);
        				locationStrings = StringUtils.split(locationStringTotal, ",");
        				hasLocationStrings = true;
        			}
        			
        			boolean stapJuist = true;
        			v[h] = FormuleParser.parseVergelijking(formuleString);
        			if(v[h]==null)
        			{	juist = false;
        				break;
        			}
        			for(int i=0 ; i<aantalDoelObjects ; i++)
    		        { 	((TekstVakPanel)ipListDoel[i]).zetSleepObjecten(sleepObjecten);
    	        		Expressie e = ((TekstVakPanel)ipListDoel[i]).geefSleepObjectWaarde();
    	        		if(verzamelDoel) e = ((TekstVakPanel)ipListDoel[i]).geefSleepObjectVerzamelWaarde();
    	        		if(e!=null) 
    	        		{	v[h] = v[h].substitueer(e, "V?("+(i+1)+")");
    	        		}
    	        		else if(locationStrings==null)
    	        		{	stapJuist = false;
    	        			break;
    	        		}
    		        }
        			
        			//System.out.println(v[h].toString());
        			stapJuist = v[h].isOplossing(new BasisExpressie(1.212131415),"q");
        			juist = juist && stapJuist;
        			if(!juist && !hasLocationStrings) break;
        			
        			if(locationStrings!=null){
        				for(int i=0 ; i<locationStrings.length ; i++){
            				int location = Integer.parseInt(locationStrings[i].trim());
            				((TekstVakPanel)ipListDoel[location-1]).zetGoedFoutSleep(stapJuist);
            			}
        			}
		        }
        	}
        	else
        	{	VergelijkingMeerv v = FormuleParser.parseVergelijking(formuleString);
	        	for(int i=0 ; i<aantalDoelObjects ; i++)
		        {   
	        		((TekstVakPanel)ipListDoel[i]).zetSleepObjecten(sleepObjecten);
	        		Expressie e = ((TekstVakPanel)ipListDoel[i]).geefSleepObjectWaarde();
	        		if(e!=null) 
	        		{	v = v.substitueer(e, "V?("+(i+1)+")");
	        		}
	        		else 
	        		{	juist = false;
	        			break;
	        		}
		        }
	        	juist = v.isOplossing(new BasisExpressie(1.212131415),"q");
	        	//System.out.println(v.toString());
        	}
        	// construeer antwoord (brxxx)
			for(int i=0 ; i<aantalDoelObjects ; i++)
	        {   
        		((TekstVakPanel)ipListDoel[i]).zetSleepObjecten(sleepObjecten);
        		Expressie e = ((TekstVakPanel)ipListDoel[i]).geefSleepObjectWaarde();
        		if(e!=null) 
        		{	answer = answer + e.toString();
        		}
	        }
			//System.out.println(answer);
        }
        else
        {
        	for(int i=0 ; i<aantalSleepObjects ; i++)
	        {	((TekstVakPanel)ipListSleep[i]).wisGoedFout();
	        }
        	boolean stapJuist = true;
	        for(int i=0 ; i<aantalDoelObjects ; i++)
	        {   int dx = Math.abs(posities[i].x - doelPosities[i].x);
	        	int dy = Math.abs(posities[i].y - doelPosities[i].y);
	        	
	        	//if(dx*dx + dy*dy > acceptedMarge*acceptedMarge) 
	        	if(dx > acceptedMarge || dy > acceptedMarge) 
	        	{	stapJuist = false;
	        		juist = juist && stapJuist;
	        		if(!view) break;
	        	}
	        	else answer = answer + i + "-" + i + ",";
	        	
	        	
	        	if(view){
    				if(stapJuist) ((TekstVakPanel)ipListSleep[i]).zetGoedFout(stapJuist);
    				else 
    					for(int j=0 ; j<ipListDoel.length ; j++)
	    	            {
	    	        		dx = Math.abs(posities[i].x - doelPosities[j].x);
	    		        	dy = Math.abs(posities[i].y - doelPosities[j].y);
	    		        	//if(dx*dx + dy*dy > acceptedMarge*acceptedMarge) 
	    		        	if(dx < acceptedMarge && dy < acceptedMarge) 
	    		        	{	((TekstVakPanel)ipListSleep[i]).zetGoedFout(false);
	    		        		break;
	    		        	}
	    	            }
    					stapJuist = true;
        		}
	        }
	        //TODO Fout antwoord 'construeren'  (wordt nu niet gedaan)
	        for(int i=aantalDoelObjects ; i<aantalSleepObjects ; i++)
	        {   stapJuist = true;
	        	for(int j=0 ; j<ipListDoel.length ; j++)
	            {
	        		int dx = Math.abs(posities[i].x - doelPosities[j].x);
		        	int dy = Math.abs(posities[i].y - doelPosities[j].y);
		        	//if(dx*dx + dy*dy > acceptedMarge*acceptedMarge) 
		        	if(dx < acceptedMarge && dy < acceptedMarge) 
		        	{	stapJuist = false;
		        		answer = answer + j + "-" + i + ",";
		        		break;
		        	}
	            }
	        	juist = juist && stapJuist;
	        	if(view && !stapJuist)
    				((TekstVakPanel)ipListSleep[i]).zetGoedFout(stapJuist);
	        }
	        if(answer.length()>0 && answer.charAt(answer.length()-1)==',')answer = answer.substring(0,answer.length()-1);
        }
        
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
        
        if(show)produceAction("changed");
    }
    
    public void kijkNa(int stapNr)
    { 	kijkNa();
    }
    
    public void actionPerformed(ActionEvent e)
	{
        if(e.getSource()==checkButton)
        {   kijkNa();
        	if((mode==0 || mode==1) && ingevuld)produceAction("checked");
        	if(fout) errorCount++;
        	attemptsCount++;
			setAttempt();
        }
        else for(int i=0 ; i<ipListSleep.length ; i++)
        {   if(e.getSource()== ipListSleep[i] && (e.getActionCommand().equals("pick")))
            {   
                if(huidigIC!=null)huidigIC.setVisible(false);
                correct = false;
                score = 0;
                ingevuld = true;
                
                
                produceAction("changed");    
            }
        }/**/
            
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
}
