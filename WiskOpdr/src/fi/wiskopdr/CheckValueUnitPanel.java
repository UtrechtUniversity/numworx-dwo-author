package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.formuleobjects.*;
import fi.wiskopdr.expressies.*;
import fi.beans.wiskopdrbeans.*;
import fi.beans.iconan.Iconan;
import fi.beans.stringutils.*;

public class CheckValueUnitPanel extends JPanel implements InteractiePanel, ActionListener, CBookAware
{
	//static Image GOEDKRUL,FOUTKRUIS, HALFKRUL;
	
    private int mode;
    
    private boolean ingevuld;
    private boolean nagekeken;
    
    private boolean correct;
    private boolean fout;
    
    private int attemptsCount;
	private Vector attempts;
    
    private boolean checkSamen = false;
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
	private InteractiePanel[] ipValueList;
	
	private int aantalValueObjects;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	
	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;
	
	private boolean check;
	private boolean teltMee;
	
	private String answer = "";
	private boolean view = false;
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
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
	
	
	public CheckValueUnitPanel()
	{	
		setLayout(null);
		setOpaque(false);
		
		attempts = new Vector();
		
		checkButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		checkButton.setBounds(0,5,100,26);
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
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		int aantalValueObjects = 0;
		int scoreMax = 0;
		boolean checkSamen = false;
		String formuleString = "$f@";
		String[] formuleStrings = null;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean view = false;
		boolean[][] logObjectives = null;
		String knopImageString = "";
		
        if(h.containsKey("aantalValueObjects")) aantalValueObjects = ((Integer)h.get("aantalValueObjects")).intValue();
        if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("checkSamen")) checkSamen = ((Boolean)h.get("checkSamen")).booleanValue();
	    if(h.containsKey("formuleString")) formuleString = (String)h.get("formuleString");
	    if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("view")) view = ((Boolean)h.get("view")).booleanValue();
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
	    this.aantalValueObjects = aantalValueObjects;
        this.scoreMax = scoreMax;
        this.checkSamen = checkSamen;
        this.formuleString = formuleString;
        this.formuleStrings = formuleStrings;
        this.logOption = logOption;
        this.logID = logID;
        this.check = check;
        this.teltMee = teltMee;
        this.view = view;
        this.logObjectives = logObjectives;
        
        ipValueList = new InteractiePanel[aantalValueObjects];
        for(int i=0 ; i<ipValueList.length ; i++)
        {   ipValueList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
            ipValueList[i].addActionListener(this);
        }
        
       
       	if(knopImageString!=null && !"".equals(knopImageString))
       	{  	Iconan iconman = new Iconan(WiskOpdr.applet, this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
			Image knopImage = iconman.getImage(knopImageString);
	    	checkButton.setPopupButtonImage(knopImage);
		    int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
			iconman.dispose();
	    }
	}
	
	public void setState(Hashtable h)
	{
	    boolean ingevuld = false;
	    boolean nagekeken = false;
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
       
	    if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
	    if(h.containsKey("nagekeken")) nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
	    if(h.containsKey("attempts"))attempts = OpdrNavStruct.toVector(h.get("attempts"));
	    if(h.containsKey("attemptsCount")) attemptsCount = ((Number)h.get("attemptsCount")).intValue();
	    if(h.containsKey("errorCount")) errorCount = ((Number)h.get("errorCount")).intValue();
        
        this.ingevuld = ingevuld;
        this.nagekeken = nagekeken;
        this.attempts = attempts;
        this.attemptsCount = attemptsCount;
	    this.errorCount = errorCount;
        
        if(ingevuld && (mode==0 || mode==1 || nagekeken)) kijkNa();
	}
	
	public void setEditState(Hashtable h)
	{
		String knopImageString = null;
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		if(knopImageString!=null && !"".equals(knopImageString))
		{   Iconan iconman = new Iconan(WiskOpdr.applet, this, TekstImageVak.getImageMap(), TekstImageVak.getImageCache());
			Image knopImage = iconman.getImage(knopImageString);
	    	checkButton.setPopupButtonImage(knopImage);
	    	int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
			iconman.dispose();
	    }
     }
	
	public Hashtable getState()
	{   
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
        h.put("ingevuld", new Boolean(ingevuld));
        h.put("nagekeken", new Boolean(nagekeken));
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
		System.out.println(s);
	}
	
	public Hashtable getEditState()
	{
		return null;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new CheckValueUnitEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void wis()
	{
		ipValueList = null;
		aantalValueObjects = 0;
		
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
	
	public void zetMaat(){
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
		if(mode==1)
			return Math.max(0, score-errorCount*2);
	    return score;
	}
	
	public int[][] getScoreObjectives()
	{	if(logObjectives==null)return null;
		int[][] scoreObjectives = new int[logObjectives.length][];
		for(int i =0; i<logObjectives.length; i++)
			scoreObjectives[i] = new int[logObjectives[i].length];
		for(int i=0 ; i<logObjectives.length ; i++)
			for(int j = 0; j<logObjectives[i].length; j++)
			{	if(logObjectives[i][j] && mode==1)
					scoreObjectives[i][j] = Math.max(0, score - errorCount*2);
				else if(logObjectives[i][j]) 
					scoreObjectives[i][j] = score;
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
    { 	score = 0;
		correct = false;
    }
    
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
    
    public void kijkNa(boolean show)
    {
        if(huidigIC!=null) huidigIC.setVisible(false);
        
        boolean juist = true;
        answer = "";
        
        correct = false;
        fout = true;
        score = 0;
        
        ipValueList = new InteractiePanel[aantalValueObjects];
        for(int i=0 ; i<ipValueList.length ; i++)
        {   ipValueList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
            ipValueList[i].addActionListener(this);
        }
        
        if(checkSamen)
        {
        	if(formuleStrings!=null)
        	{
        		
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
        				
        			}
        				
        			boolean stapJuist = true;
        			v[h] = FormuleParser.parseVergelijking(formuleString);
        			for(int i=0 ; i<aantalValueObjects ; i++)
    		        {   
    	        		Expressie e = ((TekstVakPanel)ipValueList[i]).geefObjectWaarde();
    	        		if(e!=null) 
    	        		{	ingevuld = true;
    	        			v[h] = v[h].substitueer(e, "V?("+(i+1)+")");
    	        		}
    	        		else if (((TekstVakPanel)ipValueList[i]).objectNullWaarde())
    	        		{	
    	        		}
    	        		else 
    	        		{	stapJuist = false;
    	        			break;
    	        		}
    		        }
        			
        			String[][] tekenParen = {{"<","<"},{"<","\u2264"},{"\u2264","<"},{"\u2264","\u2264"},{">",">"},{"\u2265",">"},{">","\u2265"},{"\u2265","\u2265"}};
        			
        			boolean[] stappenJuist = new boolean[v[h].geefAantal()];
        			for(int k=0 ; k<stappenJuist.length ; k++)
        			{
        				stappenJuist[k] = false;
        				if(v[h].geefVergelijking(k).geefVergTeken().equals(">") 
        						|| v[h].geefVergelijking(k).geefVergTeken().equals("<")
        						|| v[h].geefVergelijking(k).geefVergTeken().equals("\u2265") //groter dan of gelijk aan
        						|| v[h].geefVergelijking(k).geefVergTeken().equals("\u2264")
        						|| v[h].geefVergelijking(k).geefVergTeken().equals("~")) //kleiner dan of gelijk aan
            			{	Expressie expL = v[h].geefVergelijking(k).geefExpLinks();
            				Expressie expR = v[h].geefVergelijking(k).geefExpRechts();
            				if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(k).geefVergTeken().equals("<"))
            					stappenJuist[k] = expL.geefWaarde() < expR.geefWaarde()-0.000000001;
            				else if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(k).geefVergTeken().equals(">"))
            					stappenJuist[k] = expL.geefWaarde() > expR.geefWaarde()+0.000000001;
            				else if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(k).geefVergTeken().equals("\u2264"))
            					stappenJuist[k] = expL.geefWaarde() < expR.geefWaarde()+0.000000001;
            				else if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(k).geefVergTeken().equals("\u2265"))
            					stappenJuist[k] = expL.geefWaarde() > expR.geefWaarde()-0.000000001;
            				else if(v[h].geefVergelijking(k).geefVergTeken().equals("~"))
            				{	Expressie e1 = expR.kind2.kind1;
            					Expressie e2 = expL;
            					Expressie e3 = expR.kind2.kind2;
            					if(e1.isWaarde() && e2.isWaarde() && e3.isWaarde())
            					{
            						if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 0)) //{"<","<"}
            							stappenJuist[k] = e1.geefWaarde() < e2.geefWaarde()-0.000000001 && e2.geefWaarde() < e3.geefWaarde()-0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 1)) //{"<","\u2264"}
            							stappenJuist[k] = e1.geefWaarde() < e2.geefWaarde()-0.000000001 && e2.geefWaarde() < e3.geefWaarde()+0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 2)) //{"\u2264","<"}
            							stappenJuist[k] = e1.geefWaarde() < e2.geefWaarde()+0.000000001 && e2.geefWaarde() < e3.geefWaarde()-0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 3)) //{"\u2264","\u2264"}
            							stappenJuist[k] = e1.geefWaarde() < e2.geefWaarde()+0.000000001 && e2.geefWaarde() < e3.geefWaarde()+0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 4)) //{">",">"}
            							stappenJuist[k] = e1.geefWaarde() > e2.geefWaarde()+0.000000001 && e2.geefWaarde() > e3.geefWaarde()+0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 5)) //{"\u2265",">"}
            							stappenJuist[k] = e1.geefWaarde() > e2.geefWaarde()-0.000000001 && e2.geefWaarde() > e3.geefWaarde()+0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 6)) //{">","\u2265"}
            							stappenJuist[k] = e1.geefWaarde() > e2.geefWaarde()+0.000000001 && e2.geefWaarde() > e3.geefWaarde()-0.000000001;
            						else if(Algebra.isGelijkDouble(expR.kind1.geefWaarde(), 7)) //{"\u2265","\u2265"}
            							stappenJuist[k] = e1.geefWaarde() > e2.geefWaarde()-0.000000001 && e2.geefWaarde() > e3.geefWaarde()-0.000000001;
            					}
            				}
                				
            			}
            			else stappenJuist[k] = v[h].geefVergelijking(k).isOplossing(new BasisExpressie(1.212131415),"q");
        				
        				if(k==0)
        					stapJuist = stappenJuist[k];
        				else
        					stapJuist = stapJuist || stappenJuist[k];
        			}
        			
        			
        			/*
        			if(v[h].geefAantal()==1 && (v[h].geefVergelijking(0).geefVergTeken().equals(">") || v[h].geefVergelijking(0).geefVergTeken().equals("<")))
        			{	// een nog zwakke manier om ongelijkheden te checken als de expressies nummeriek zijn en bij een enkelvoudige vergelijking
        				Expressie expL = v[h].geefVergelijking(0).geefExpLinks();
        				Expressie expR = v[h].geefVergelijking(0).geefExpRechts();
        				if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(0).geefVergTeken().equals("<"))
        					stapJuist = expL.geefWaarde() < expR.geefWaarde()-0.000000001;
        				else if(expL.isWaarde() && expR.isWaarde() && v[h].geefVergelijking(0).geefVergTeken().equals(">"))
        					stapJuist = expL.geefWaarde() > expR.geefWaarde()+0.000000001;
        			}
        			else stapJuist = v[h].isOplossing(new BasisExpressie(1.212131415),"q");
        			*/
        			juist = juist && stapJuist;
        			
        			if(!juist && locationStrings==null) break;
        			
        			if(locationStrings!=null){
        				for(int i=0 ; i<locationStrings.length ; i++){
            				int location = Integer.parseInt(locationStrings[i].trim());
            				((TekstVakPanel)ipValueList[location-1]).zetGoedFout(stapJuist);
            			}
        			}
        			
		        }
        	}
        	else
        	{	VergelijkingMeerv v = FormuleParser.parseVergelijking(formuleString);
	        	for(int i=0 ; i<aantalValueObjects ; i++)
		        {   
	        		Expressie e = ((TekstVakPanel)ipValueList[i]).geefObjectWaarde();
	        		if(e!=null) 
	        		{	v = v.substitueer(e, "V?("+(i+1)+")");
	        		}
	        		else 
	        		{	juist = false;
	        			break;
	        		}
		        }
	        	juist = v.isOplossing(new BasisExpressie(1.212131415),"q");
	        	System.out.println(v.toString());
        	}
        }
        else
        {
        	for(int i=0 ; i<aantalValueObjects ; i++)
	        {   boolean stapJuist = ((TekstVakPanel)ipValueList[i]).ipObjectIsCorrect();
	        	juist = juist && stapJuist;
	        	if(view)((TekstVakPanel)ipValueList[i]).zetGoedFout(stapJuist);
		    }
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
        
        if(show || mode==0 || mode==1)produceAction("changed");
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
        else for(int i=0 ; i<ipValueList.length ; i++)
        {   if(e.getSource()== ipValueList[i] && (e.getActionCommand().equals("changed")))
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

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener, String command) {
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
		return new String[] { "action.setNotEditable" };
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
}
