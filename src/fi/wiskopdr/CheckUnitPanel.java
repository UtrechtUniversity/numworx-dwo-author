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

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.VergelijkingMeerv;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.opdrnav.OpdrNavStruct;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.beans.iconan.Iconan;
import fi.beans.stringutils.StringUtils;
import fi.beans.wiskopdrbeans.*;

public class CheckUnitPanel extends JPanel implements InteractiePanel, ActionListener, CBookAware
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
    
    private boolean multiSelections;
    private boolean randomizePositions;
    private boolean checkFormule;
    private String[] formuleStrings = null;
    
    private int score;
    private int errorCount;
    private int scoreMax=10;
    
	static int GOED = 1;
	static int FOUT = 0;
	static int HALF = 2;
	static int GEEN = 3;
	
	private FormuleButton checkButton;
	private InteractiePanel[] ipList;
	
	private boolean[] juisteSelecties;
	
	private ImageComponent goedIC, foutIC, halfIC, huidigIC;
	
	/*private static String[] imageNames = 
	{	"goedkrul.gif",
		"goedkrulhalf.gif",
		"foutkruis.gif",
		"goedkrul_en.gif",
		
	};*/
	
	//private static Hashtable images;
		
	
	
	private boolean logOption;
	private String logID;
	
	private boolean[][] logObjectives;
	private boolean[][][] logMisconceptions;
	private int[][] possibleMisconceptions;
	private int[][] measuredMisconceptions;
	
	private boolean check;
	private boolean teltMee;
	
	 private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
			
	/*public static void zetPlaatjes(Image gk, Image fk, Image hk)
	{	GOEDKRUL = gk;
		FOUTKRUIS = fk;
		HALFKRUL = hk;
	}*/
	
	public CheckUnitPanel()
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
		
		if(WiskOpdr.misconceptions != null && WiskOpdr.misconceptions.length>0)
	     {	 possibleMisconceptions = new int[WiskOpdr.misconceptions.length][];
	     	 measuredMisconceptions = new int[WiskOpdr.misconceptions.length][];
		     for(int i=0 ; i<WiskOpdr.misconceptions.length ; i++)
		     {	 possibleMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		     	 measuredMisconceptions[i] = new int[WiskOpdr.misconceptions[i].length];
		    	 for(int j=0 ; j<WiskOpdr.misconceptions[i].length ; j++)
			     {  possibleMisconceptions[i][j] = 0;
			     	measuredMisconceptions[i][j] = 0;
			     }
		     }
	     }
		
		
	}
	
	/*public static Image getImage(String name)
	{	return(Image)images.get(name);
	}*/
	
	public void randomizePositions()
	{
		Vector v = new Vector();
		randomizedPositions = new Point[juisteSelecties.length];
		for(int i=0 ; i<ipList.length ; i++)
		{	if(!(ipList[i] instanceof TekstVakPanel) || !((TekstVakPanel)ipList[i]).isZwevend())return;
			v.addElement(((TekstVakPanel)ipList[i]).geefLocatie());
		}
		for(int i=0 ; i<ipList.length ; i++)
		{	int r = (int)((ipList.length-i)*Math.random());
			Point p = (Point)(v.elementAt(r));
			if(!positionsRandomized) randomizedPositions[i] = p;
			((TekstVakPanel)ipList[i]).zetLocatie(p.x, p.y);
			v.removeElementAt(r);
		}
		positionsRandomized = true;
		(((TekstInteractiePanelVak)((Component)ipList[0]).getParent()).getTekstVak()).layoutTekst();
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
	    boolean[] juisteSelecties = null;
	    int scoreMax = 0;
		boolean randomizePositions = false;
		boolean multiSelections = false;
		boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		boolean checkFormule = false;
		String[] formuleStrings = null;
		boolean[][] logObjectives = null;
		String knopImageString = "";
		boolean[][][] logMisconceptions = null;
       
        if(h.containsKey("juisteSelecties")) juisteSelecties = (boolean[])h.get("juisteSelecties");
        if(h.containsKey("scoreMax")) scoreMax = ((Integer)h.get("scoreMax")).intValue();
	    if(h.containsKey("randomizePositions")) randomizePositions = ((Boolean)h.get("randomizePositions")).booleanValue();
	    if(h.containsKey("multiSelections")) multiSelections = ((Boolean)h.get("multiSelections")).booleanValue();
	    if(h.containsKey("logOption")) logOption = ((Boolean)h.get("logOption")).booleanValue();
		if(h.containsKey("logID")) logID = (String)h.get("logID");
		if(h.containsKey("check")) check = ((Boolean)h.get("check")).booleanValue();
		if(h.containsKey("teltMee")) teltMee = ((Boolean)h.get("teltMee")).booleanValue();
		if(h.containsKey("checkFormule")) checkFormule = ((Boolean)h.get("checkFormule")).booleanValue();
		if(h.containsKey("formuleStrings")) formuleStrings = (String[])h.get("formuleStrings");
		if(h.containsKey("logObjectives")) logObjectives = (boolean[][])h.get("logObjectives");
		if(h.containsKey("logMisconceptions")) logMisconceptions = (boolean[][][])h.get("logMisconceptions");
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		for(int i=0 ; formuleStrings!=null && i<formuleStrings.length ; i++)
        {	try{
				formuleStrings[i] = FormuleParser.randomizeString(formuleStrings[i], randomVars, randomValues);
	    	}
	    	catch(Exception e){	}
        }
		
		if(juisteSelecties==null) return;
	   
        
        this.juisteSelecties = juisteSelecties;
        this.scoreMax = scoreMax;
        this.randomizePositions = randomizePositions;
        this.multiSelections = multiSelections;
        this.logOption = logOption;
        this.logID = logID;
        this.check = check;
        this.teltMee = teltMee;
        this.checkFormule = checkFormule;
        this.formuleStrings = formuleStrings;
        this.logObjectives = logObjectives;
        this.logMisconceptions = logMisconceptions;
        
        if(possibleMisconceptions != null && logMisconceptions!=null)
        {
        	for(int i=0 ; i<logMisconceptions.length ; i++)
        	{	for( int j=0 ; logMisconceptions[i]!=null && j<logMisconceptions[i].length && j<possibleMisconceptions.length ; j++)
        		{	for( int k=0 ; k<logMisconceptions[i][j].length && k<possibleMisconceptions[j].length; k++)
            		{	if(logMisconceptions[i][j][k])
	        				possibleMisconceptions[j][k] = 1;
            		}
        		}
        	}
        }
        
        ipList = new InteractiePanel[juisteSelecties.length];
        for(int i=0 ; i<ipList.length ; i++)
        {   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
	        if(ipList[i]==null) {
	    		JOptionPane.showMessageDialog(this, "Selectie-unit fout.\nNiet alle selectieobjecten zijn aanwezig.\nSelectieobject met ID="+(i+1)+" kan niet gevonden worden.");
	    	}
	        else
             ipList[i].addActionListener(this);
        }
        
        if(randomizePositions && !positionsRandomized) randomizePositions();
        
        if(knopImageString!=null && !"".equals(knopImageString))
       	{  	Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
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
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
// TODO controleer of dit zo goed is!
		if(h.containsKey("randomizedPositionsX"))
        {
        	int[] randomizedPositionsX = OpdrNavStruct.toIntArray(h.get("randomizedPositionsX"));
        	int[] randomizedPositionsY = OpdrNavStruct.toIntArray(h.get("randomizedPositionsY"));
        	randomizedPositions = new Point[randomizedPositionsX.length];
        	for (int i = 0; i < randomizedPositionsY.length; i++) {
				int x = randomizedPositionsX[i];
				int y = randomizedPositionsY[i];
				randomizedPositions[i] = new Point(x,y);
			}
        } else
// oude opslag:
	    if(h.containsKey("randomizedPositions")) randomizedPositions = (Point[])h.get("randomizedPositions");
	    if(h.containsKey("ingevuld")) ingevuld = ((Boolean)h.get("ingevuld")).booleanValue();
	    if(h.containsKey("nagekeken")) nagekeken = ((Boolean)h.get("nagekeken")).booleanValue();
	    if(h.containsKey("attempts"))attempts = OpdrNavStruct.toVector(h.get("attempts"));
	    if(h.containsKey("attemptsCount")) attemptsCount = ((Number)h.get("attemptsCount")).intValue();
	    if(h.containsKey("errorCount")) errorCount = ((Number)h.get("errorCount")).intValue();
        
        this.randomizedPositions = randomizedPositions;
        this.ingevuld = ingevuld;
        this.nagekeken = nagekeken;
        this.attempts = attempts;
        this.attemptsCount = attemptsCount;
	    this.errorCount = errorCount;
        
        if(randomizePositions) 
        {   for(int i=0 ; i<ipList.length ; i++)
	        {   
	        	Point p = randomizedPositions[i];
	        	if(p != null)
	        		((TekstVakPanel)ipList[i]).zetLocatie(p.x, p.y);
	        }
	        (((TekstInteractiePanelVak)((Component)ipList[0]).getParent()).getTekstVak()).layoutTekst();
        }
        
        if(ingevuld && (mode==0 || nagekeken)){
        	kijkNa();
        }
	}
	
	public void setEditState(Hashtable h)
	{
		String knopImageString = null;
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
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
	
	public Hashtable getState()
	{   
	    Point[] randomizedPositions = null;
	    boolean ingevuld = false;
	    boolean nagekeken = false;
	    Vector attempts = new Vector();
	    int attemptsCount = 0;
		int errorCount = 0;
		
	    
	    randomizedPositions = this.randomizedPositions;
	    ingevuld = this.ingevuld;
	    nagekeken = this.nagekeken;
	    attempts = this.attempts;
	    attemptsCount = this.attemptsCount;
	    errorCount = this.errorCount;

	    if(!("MW".equals(WiskOpdr.deployVariant) || "GR".equals(WiskOpdr.deployVariant))) kijkNa(false);
		if(logOption)
		{	
	    	Hashtable logMap = new Hashtable();
			
	    	String logString = "";
			String[] options = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z"};
			for(int i=0 ; i<ipList.length ; i++)
	        {   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
	            if(((TekstVakPanel)ipList[i]).isIpSelected() && i<options.length) logString = logString + options[i];
	        }
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
        h.put("attempts", attempts);
        h.put("attemptsCount", new Integer(attemptsCount));
        h.put("errorCount", new Integer(errorCount));
        return h;
	}
	
	public Hashtable getEditState()
	{
		return null;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new CheckUnitEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		super.setBounds(x,y,b,h);
	}
	
	public void setAttempt()
	{
		String goedFout = "";
		if(huidigIC == goedIC && huidigIC.isVisible())goedFout = "goed";
		if(huidigIC == halfIC && huidigIC.isVisible())goedFout = "half";
		if(huidigIC == foutIC && huidigIC.isVisible())goedFout = "fout";
		
		String logString = "";
		String[] options = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z"};
		for(int i=0 ; i<ipList.length ; i++)
        {   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
            if(((TekstVakPanel)ipList[i]).isIpSelected() && i<options.length) logString = logString + options[i];
        }
		
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
	
	public void wis()
	{
		ipList = null;
		juisteSelecties = null;
		
	    if(huidigIC!=null)
	    {	huidigIC.setVisible(false);
	    	
	    }
	   
	    correct = false;
	    score = 0;
	    errorCount = 0;
	    attemptsCount = 0;
	    nagekeken = false;
	    ingevuld = false;
	    
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
	
	public int[][] getMeasuredMisconceptions()
	{	return measuredMisconceptions;
	}
	
	public int[][] getPossibleMisconceptions()
	{	return possibleMisconceptions;
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
    	if(correct && cbookEventHandler.hasListeners("action.correct"))
    		cbookEventHandler.fire("action.correct");
    	if(fout && cbookEventHandler.hasListeners("action.false"))
    		cbookEventHandler.fire("action.false");
    	if(fout && errorCount>0  && cbookEventHandler.hasListeners("action.false_2"))
    		cbookEventHandler.fire("action.false_2");
    }
    
    public void kijkNa(boolean show)
    {
        if(huidigIC!=null) huidigIC.setVisible(false);
        
        boolean juist = true;
        ingevuld = false;
        
        correct = false;
        fout = true;
        score = 0;
        
        if(checkFormule)
        {
        	if(formuleStrings!=null)
        	{
        		
        		VergelijkingMeerv[] v = new VergelijkingMeerv[formuleStrings.length];
        		for(int h=0 ; h<formuleStrings.length ; h++)
		        {
        			boolean stapJuist = true;
        			v[h] = FormuleParser.parseVergelijking(formuleStrings[h]);
        			if(v[h]==null)
        			{	juist = false;
        				break;
        			}
        			
        			ipList = new InteractiePanel[juisteSelecties.length];
        	        for(int i=0 ; i<ipList.length ; i++)
        	        {   
        	        	ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
        	        	if(ipList[i]==null) {
        		    		JOptionPane.showMessageDialog(this, "Selectie-unit fout.\nNiet alle selectieobjecten zijn aanwezig.\nSelectieobject met ID="+(i+1)+" kan niet gevonden worden.");
        		    		break;
        	        	}
        	        	
        	        	Expressie e = ((TekstVakPanel)ipList[i]).isIpSelected() ? ((TekstVakPanel)ipList[i]).geefObjectWaarde() : new BasisExpressie(0);
    	        		if(e!=null) 
    	        		{	v[h] = v[h].substitueer(e, "V?("+(i+1)+")");
    	        		}
    	        		else 
    	        		{	stapJuist = false;
    	        			break;
    	        		}
        	        	ingevuld = ingevuld || ((TekstVakPanel)ipList[i]).isIpSelected();
        	        }
        			
        			
        			
        			//System.out.println(v[h].toString());
        			stapJuist = v[h].isOplossing(new BasisExpressie(1.212131415),"q");
        			juist = juist && stapJuist;
        			if(!juist) break;
		        }
        	}
        	else juist = false;
        	
        }
        else
        {   ipList = new InteractiePanel[juisteSelecties.length];
	        for(int i=0 ; i<ipList.length ; i++)
	        {   ipList[i] = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanel(i+1);
	        	boolean selectieJuist = ((TekstVakPanel)ipList[i]).isIpSelected() == juisteSelecties[i];
	            juist = juist && selectieJuist;
	            if(measuredMisconceptions!=null && logMisconceptions!=null && ((TekstVakPanel)ipList[i]).isIpSelected())
	            {	for( int j=0 ; logMisconceptions[i]!=null && j<logMisconceptions[i].length && j<measuredMisconceptions.length ; j++)
	        		{	for( int k=0 ; logMisconceptions[i][j]!=null && k<logMisconceptions[i][j].length && k<measuredMisconceptions[j].length; k++)
	            		{	if(logMisconceptions[i][j][k])
	        					measuredMisconceptions[j][k] = 1;
	            		}
	        		}
	            }
	            ingevuld = ingevuld || ((TekstVakPanel)ipList[i]).isIpSelected();
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
        
        System.out.println("kijkna Show="+show);
        
        if(ingevuld && show)produceAction("changed");
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
        else for(int i=0 ; i<ipList.length ; i++)
        {   if(e.getSource()== ipList[i] && (e.getActionCommand().equals("select") || e.getActionCommand().equals("deselect")))
            {   
                if(huidigIC!=null)huidigIC.setVisible(false);
                correct = false;
                score = 0;
                
                if(!multiSelections && e.getActionCommand().equals("select"))
                {	
                	for(int j=0 ; j<ipList.length ; j++)
                	{	if(i!=j) ((TekstVakPanel)ipList[j]).setIpSelected(false);
                    }
                }
                
                produceAction("changed");    
            }
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		String localizedCmd = WiskOpdr.rb.getString(CBA_PREFIX + cmd);
		if(localizedCmd==null)
			return cmd;
		return localizedCmd;
	}
}
