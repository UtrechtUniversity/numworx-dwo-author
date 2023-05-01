package fi.spot_problems_dwo;

import java.awt.AWTEventMulticaster;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;

import javax.swing.*;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

import fi.spot_problems_dwo.wiskopdr.AntwoordFormuleVak;


public class SPInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							         ActionListener, CBookAware
									
{
	
	Image foutKruis, goedKrul, halfKrul;
	ImageIcon foutKruisIcon, goedKrulIcon, halfKrulIcon;
	
	//JPanel dummyPanel;
	JScrollPane scrollPane;
	
	DrawingContainer drawCon;
	
	AntwoordFormuleVak antwoordVak;
	int antwoordVakBreedte = 380;
	int antwoordVakHoogte = 100;
	int aVHoogte = 100;
	
	String[][] antwoorden = 
	{
		{"2n+1", "4n+1", "n^2",	   "4n",		 "4n+1",		"3n+1",	"4n+2",   "4n",		     "4n+3", "5n+2", "4n+1", "2n+2"},	
		{"4n-3", "2n+2", "n(n+1)", "n(n+1)/2",	 "n(n+1)/2",	"n^2",	"6n^2",   "n^2+4",       "2n^2", "8n+1", "n^2",  "3n+6"} ,
		{"4n^2", "6n+3", "8n+3",   "(3n^2-n)/2", "(n^2+n+2)/2",	"2^n",	"4n^2+2", "n^2+(n-1)^2", "n^3",  "2n^3", "3^n",  "(2n^3+3n^2+n)/6"}
	};
	
	int level = 1;
	int level1Keuze = 1;
	int level2Keuze = 1;
	int level3Keuze = 1;
	
	//boolean correct = true;
	
    boolean ingevuld;
	private boolean nagekeken;
	private int mode;
	
	boolean kijkNaActief = true;
	int score = 0;
	int scoreMax = 10;
	boolean antwoordVakZichtbaar = true;
	
	
	boolean noSetBounds = false;	
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	
	public SPInteractiePanel()
	{
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		java.net.URL imageURL = Spot_Problems_dwo.class.getResource("resources/goedkrul.gif");
		if (imageURL != null) 
		{
		    goedKrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul.gif.");
		}
		imageURL = Spot_Problems_dwo.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutKruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.gif");
		}
		imageURL = Spot_Problems_dwo.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
			halfKrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.gif");
		}
		goedKrul = goedKrulIcon.getImage();
		foutKruis = foutKruisIcon.getImage();
		halfKrul = halfKrulIcon.getImage();

		
	}

	
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		
//System.out.println("zetOpdracht");

		//edit state
		int level = 1;
		int level1Keuze = 1;
		int level2Keuze = 1;
		int level3Keuze = 1;

		if (b.containsKey("level"))
			level = ((Integer) b.get("level")).intValue();
		if (b.containsKey("level1Keuze"))
			level1Keuze = ((Integer) b.get("level1Keuze")).intValue();
		if (b.containsKey("level2Keuze"))
			level2Keuze = ((Integer) b.get("level2Keuze")).intValue();
		if (b.containsKey("level3Keuze"))
			level3Keuze = ((Integer) b.get("level3Keuze")).intValue();
		
		zetLevel1Keuze(level1Keuze);
		zetLevel2Keuze(level2Keuze);
		zetLevel3Keuze(level3Keuze);
		// HIER
		zetLevel(level);
//if (level == 1)		
//System.out.println("antwoord = " + antwoorden[level - 1][level1Keuze - 1]);
//else if (level == 1)		
//System.out.println("antwoord = " + antwoorden[level - 1][level2Keuze - 1]);
//else if (level == 1)		
//System.out.println("antwoord = " + antwoorden[level - 1][level3Keuze - 1]);
		
		boolean antwoordVakZichtbaar = true;
		if (b.containsKey("antwoordVakZichtbaar"))
			antwoordVakZichtbaar = ((Boolean) b.get("antwoordVakZichtbaar")).booleanValue();
		zetAntwoordVak(antwoordVakZichtbaar);

		boolean kijkNaActief = true;
		if (b.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);
		
		
		// state
		String antwoord = "";
		if (b.containsKey("antwoord"))
			antwoord = (String) b.get("antwoord");
		if (!antwoord.equals(""))
		{	antwoordVak.zetAntwoord(antwoord);
			if (kijkNaActief)
				antwoordVak.kijkNa();
		}
		
		Vector userSpots = new Vector();
		if (b.containsKey("userSpots"))
			userSpots = (Vector) b.get("userSpots");
		drawCon.setUserSpots(userSpots);
		

	}
	
	public void setState(Hashtable b)
	{
//System.out.println("setState");		
		if (b.containsKey("nagekeken"))	nagekeken = ((Boolean) b.get("nagekeken")).booleanValue();
		if (b.containsKey("ingevuld"))	ingevuld = ((Boolean) b.get("ingevuld")).booleanValue();
		
		String antwoord = "";
		if (b.containsKey("antwoord"))
			antwoord = (String) b.get("antwoord");
		if (!antwoord.equals(""))
		{	antwoordVak.zetAntwoord(antwoord);
			if (kijkNaActief)
				antwoordVak.kijkNa();
		}
		Vector userSpots = new Vector();
		if (b.containsKey("userSpots"))
			userSpots = (Vector) b.get("userSpots");
		drawCon.setUserSpots(userSpots);
		
		if(ingevuld && (mode==0 || nagekeken)) kijkNa();

	}
	
	public void setEditState(Hashtable b)
	{
		//edit state
		int level = 1;
		int level1Keuze = 1;
		int level2Keuze = 1;
		int level3Keuze = 1;

		if (b.containsKey("level"))
			level = ((Integer) b.get("level")).intValue();
		if (b.containsKey("level1Keuze"))
			level1Keuze = ((Integer) b.get("level1Keuze")).intValue();
		if (b.containsKey("level2Keuze"))
			level2Keuze = ((Integer) b.get("level2Keuze")).intValue();
		if (b.containsKey("level3Keuze"))
			level3Keuze = ((Integer) b.get("level3Keuze")).intValue();
		
		zetLevel1Keuze(level1Keuze);
		zetLevel2Keuze(level2Keuze);
		zetLevel3Keuze(level3Keuze);
		// HIER
		zetLevel(level);

		boolean antwoordVakZichtbaar = true;
		if (b.containsKey("antwoordVakZichtbaar"))
			antwoordVakZichtbaar = ((Boolean) b.get("antwoordVakZichtbaar")).booleanValue();
		zetAntwoordVak(antwoordVakZichtbaar);

		boolean kijkNaActief = true;
		if (b.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) b.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);
		
		// state
		String antwoord = "";
		if (b.containsKey("antwoord"))
			antwoord = (String) b.get("antwoord");
		if (!antwoord.equals(""))
		{	antwoordVak.zetAntwoord(antwoord);
			if (kijkNaActief)
				antwoordVak.kijkNa();
		}
		Vector userSpots = new Vector();
		if (b.containsKey("userSpots"))
			userSpots = (Vector) b.get("userSpots");
		drawCon.setUserSpots(userSpots);

			
	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();
		
		h.put("antwoord", antwoordVak.geefAntwoord());
		h.put("userSpots", drawCon.getUserSpots());
		h.put("nagekeken", new Boolean(nagekeken));
		h.put("ingevuld", new Boolean(ingevuld));
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		// edit state
		h.put("level", new Integer(level));
		h.put("level1Keuze", new Integer(level1Keuze));
		h.put("level2Keuze", new Integer(level2Keuze));
		h.put("level3Keuze", new Integer(level3Keuze));
		
		h.put("kijkNaActief", new Boolean(kijkNaActief));
		h.put("antwoordVakZichtbaar", new Boolean(antwoordVakZichtbaar));
		
		// state
		h.put("antwoord", antwoordVak.geefAntwoord());
		h.put("userSpots", drawCon.getUserSpots());		
		
		return h;
	}
	
	public void zetLevel(int level)
	{	this.level = level;
		if (level == 1)
		{	zetLevel1Keuze(level1Keuze);
		}
		else if (level == 2)
		{	zetLevel2Keuze(level2Keuze);
			
		}
		else if (level == 3)
		{	zetLevel3Keuze(level3Keuze);
			
		}
			
	}
	
	public void zetLevel1Keuze(int l1Keuze)
	{	level1Keuze = l1Keuze;
		if (drawCon != null)
			drawCon.initDWOProblem((level - 1) * 100 + level1Keuze - 1);
		if (antwoordVak != null)
			antwoordVak.zetJuisteAntwoord("$f" + Spot_Problems_dwo.rb.getString("aantalTekst") + "=" + 
									  	  antwoorden[level - 1][level1Keuze - 1] + "@");
		
	}
	
	public void zetLevel2Keuze(int l2Keuze)
	{	level2Keuze = l2Keuze;
		if (drawCon != null)
			drawCon.initDWOProblem((level - 1) * 100 + level2Keuze - 1);
		if (antwoordVak != null)
			antwoordVak.zetJuisteAntwoord("$f" + Spot_Problems_dwo.rb.getString("aantalTekst") + "=" + 
										  antwoorden[level - 1][level2Keuze - 1] + "@");
		
	}
	
	public void zetLevel3Keuze(int l3Keuze)
	{	level3Keuze = l3Keuze;
		if (drawCon != null)
			drawCon.initDWOProblem((level - 1) * 100 + level3Keuze - 1);
		if (antwoordVak != null)
			antwoordVak.zetJuisteAntwoord("$f" + Spot_Problems_dwo.rb.getString("aantalTekst") + "=" + 
				                          antwoorden[level - 1][level3Keuze - 1] + "@");
		
	}
	
	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;
		if (antwoordVak != null)
			antwoordVak.zetKijkNaActief(kijkNaActief);
		
	}
	
	public void zetAntwoordVak(boolean b)
	{
		antwoordVakZichtbaar = b;
		if (b)
			aVHoogte = antwoordVakHoogte;
		else
			aVHoogte = 0;
		setBounds(getLocation().x,getLocation().y,getSize().width,getSize().height);
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new SPInteractieEditPanel();
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		//if ((getLocation().x == x) && (getLocation().y == y) &&
		//	(getSize().width == b) && (getSize().height == h))
		//	return;
		
//System.out.println("spip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		
		
		if (scrollPane == null)
		{  	
			drawCon = new DrawingContainer(this);
			drawCon.setSize(530, 320);
			drawCon.setPreferredSize(new Dimension(530, 320));
			drawCon.initialize();
		
			scrollPane = new JScrollPane(drawCon);
    		scrollPane.setBounds(0, 0, b, h - aVHoogte - 10);
    		scrollPane.setBorder(BorderFactory.createEmptyBorder());
    		scrollPane.setPreferredSize(new Dimension(b, h - aVHoogte - 10));
    		add(scrollPane);
    		
    		if (antwoordVak != null)
    			remove(antwoordVak);
    		
    		if (antwoordVakZichtbaar)
    		{
    			antwoordVak = new AntwoordFormuleVak();
    			antwoordVak.zetPlaatjes2(goedKrul, foutKruis, halfKrul);
    			antwoordVak.zetScrollOptie(false);
    			antwoordVak.zetStappen(false);
    			antwoordVak.zetJuisteAntwoord("$f" + Spot_Problems_dwo.rb.getString("aantalTekst") + "=" + antwoorden[0][0] + "@");
    		
    			if (b > antwoordVakBreedte)
    				antwoordVak.setBounds((b - antwoordVakBreedte) / 2, scrollPane.getSize().height + 10, 
    						antwoordVakBreedte, antwoordVakHoogte);
    			else
    				antwoordVak.setBounds(0, scrollPane.getSize().height + 1, b, antwoordVakHoogte);

    			antwoordVak.addActionListener(this);

    			add(antwoordVak);
    		}	
   			
//System.out.println("created");			
		}
		else
		{	
//			int aVHoogte = antwoordVakHoogte;
//			if (!antwoordVakZichtbaar)	
//				aVHoogte = 0;
//			antwoordVak.setVisible(antwoordVakZichtbaar);
			
			if (antwoordVak != null )
				antwoordVak.setVisible(antwoordVakZichtbaar);
			
    		scrollPane.setSize(b, h - antwoordVakHoogte - 1);
    		scrollPane.setPreferredSize(new Dimension(b, h - antwoordVakHoogte - 1));

    		if (antwoordVak != null && antwoordVakZichtbaar)
    		{	if (b > antwoordVakBreedte)
    				antwoordVak.setBounds((b - antwoordVakBreedte) / 2, scrollPane.getSize().height + 1, 
    						               antwoordVakBreedte, antwoordVakHoogte);
    			else
    				antwoordVak.setBounds(0, scrollPane.getSize().height + 1, b, antwoordVakHoogte);
    		}
		}
		
	}
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax(){
		if(kijkNaActief)return scoreMax;
		else return 0;
	}
	
	public boolean isCorrect()
	{	if (!kijkNaActief)
			return true;
		return 
			score == scoreMax;
	}
	
	public boolean isFout()
	{	if (!kijkNaActief)
			return false;
		return score == 0;
	}
	
	public void zetMode(int mode)
    {   this.mode = mode;
    	if (kijkNaActief)    
    		zetKijkNaActief(mode == 0 || mode == 1);
    }

	
	public void zetNagekeken(boolean b)
	{	if (ingevuld) 
			nagekeken = b;
	}
	
    public void stop()
    { kijkNa();
    }
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {	
    	if (!kijkNaActief)
			return;
    	boolean correct = antwoordVak.isCorrect();
		ingevuld = antwoordVak.isIngevuld();
		if (correct)
		{	score = scoreMax;
			drawCon.showAllPatterns();
		}
		else
			score = 0;
		
		produceAction("changed");
	}
    
    public void kijkNa(int stapNr)
    {}
    
    private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener al) 
 	{	actionListener = AWTEventMulticaster.add(actionListener, al);
 	}
 	
 	public void removeActionListener(ActionListener al)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, al);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	//kijkNa();
 	        actionListener.actionPerformed(new ActionEvent(this, 0, command));
 		    
 		}
 	}
 	//end ActionProducer

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == antwoordVak)
		{	
			kijkNa();
			
			
		}
	}


	@Override
	public void acceptCBookEvent(CBookEvent event) {
		String command = event.getCommand();
		if(command.startsWith("action.showAllPatterns"))
		{	drawCon.showAllPatterns();
		}
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getAcceptedCmds() {
		String[] sendCommands = {
				"action.showAllPatterns"};
		return sendCommands;
	}


	@Override
	public String getLocalizedCmd(String cmd) {
		String locString = Spot_Problems_dwo.rb.getString(CBA_PREFIX + cmd);
		if(locString==null)
			locString = cmd;
		return locString;
	}
}
