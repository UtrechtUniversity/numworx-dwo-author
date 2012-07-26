package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;

import fi.algebrapijlenopdr.expressies_ap.*;
import fi.algebrapijlenopdr.schuifobjects.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.reflect.Constructor;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class AlgebraPijlenOpdrInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
																		ActionListener			
{	
	AlgebraPijlenOpdr applet;
	ImageIcon goedkrulIcon, foutkruisIcon, halfkrulIcon;
	Image goedkrul, foutkruis, halfkrul;
	
	private AlgebraSchuifVeld algebraSchuifVeld;

	boolean kijkNaActief = false;

	int score = 0;
    int scoreMax = 10;

    boolean ingevuld;
	private boolean nagekeken;
	private int mode;
	
	Vector docentExpressieStrings = new Vector();
	Vector docentExpressies = new Vector();
	
    Vector listeners = new Vector();
    
	public AlgebraPijlenOpdrInteractiePanel()
	{	
		
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		java.net.URL imageURL = AlgebraPijlenOpdr.class.getResource("resources/goedkrul.gif");
		if (imageURL != null) 
		{
		    goedkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul.gif.");
		}
		imageURL = AlgebraPijlenOpdr.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutkruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.");
		}
		imageURL = AlgebraPijlenOpdr.class.getResource("resources/goedkrulhalf.gif");
		if (imageURL != null) 
		{
			halfkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrulhalf.");
		}
		
		goedkrul = goedkrulIcon.getImage();
		foutkruis = foutkruisIcon.getImage();
		halfkrul = halfkrulIcon.getImage();
	
//System.out.println("APO-IPa");	
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{	
		if (h.containsKey("docentExpressieStrings"))
			docentExpressieStrings = (Vector) h.get("docentExpressieStrings");
		
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		
		algebraSchuifVeld.setEditModeState(h);
	}
	
	public void setState(Hashtable h)
	{	
		if (h.containsKey("nagekeken"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		
		if (h.containsKey("ingevuld"))
			ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();
		
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);

		if (h.containsKey("docentExpressieStrings"))
			docentExpressieStrings = (Vector) h.get("docentExpressieStrings");

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		
		if (h.containsKey("kijkNaActief"))
			nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
		
		algebraSchuifVeld.setState(h);
		if(ingevuld && (mode==0 || nagekeken)) kijkNa();
	}
	
	public void setEditState(Hashtable h)
	{	
		if (algebraSchuifVeld != null)
		{
			if (h.containsKey("docentExpressiesString"))
				docentExpressieStrings = (Vector) h.get("docentExpressieStrings");
			
			if (h.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
			zetKijkNaActief(kijkNaActief);

			if (h.containsKey("scoreMax"))
				scoreMax = ((Integer) h.get("scoreMax")).intValue();
			
			
			algebraSchuifVeld.setEditModeState(h);
//System.out.println("as not null");		
		}
		else
		{
//System.out.println("as null");			
		}
	}
	
	public Hashtable getState()
	{	
		boolean nagekeken = false;
		boolean ingevuld = false;
	    
		nagekeken = this.nagekeken;
		ingevuld = this.ingevuld;
		
		Hashtable h = algebraSchuifVeld.getState();
		h.put("nagekeken",new Boolean(nagekeken));
		h.put("ingevuld",new Boolean(ingevuld));
		
		return h;
	}
	
	public Hashtable getEditState()
	{	
		Hashtable h = algebraSchuifVeld.getState();
		
		h.put("kijkNaActief", new Boolean(kijkNaActief));
		h.put("scoreMax", new Integer(scoreMax));
		
		return h;
	}
	
	public void zetToolkit(boolean b)
	{
		algebraSchuifVeld.zetToolkit(b);
	}
	
	public void zetAlleenInvullen(boolean b)
	{
		algebraSchuifVeld.zetAlleenInvullen(b);
	}
	
	public void zetIsDemo(boolean b)
	{
		algebraSchuifVeld.zetIsDemo(b);
	}
	
	public void zetBrugklas(boolean b)
	{
		algebraSchuifVeld.zetBrugklas(b);
	}
	
	public void zetTerugHeen(boolean b)
	{
		algebraSchuifVeld.zetTerugHeen(b);
	}
	
	public void zetTabelOptie(boolean b)
	{
		algebraSchuifVeld.zetTabelOptie(b);
	}

	public void zetGrafiekOptie(boolean b)
	{
		algebraSchuifVeld.zetGrafiekOptie(b);
	}

	public void zetScrollOptie(boolean b)
	{
		algebraSchuifVeld.zetScrollOptie(b);
	}

	public void zetZoomOptie(boolean b)
	{
		algebraSchuifVeld.zetZoomOptie(b);
	}
	
	public void zetBeginExpressie(Expressie exp)
	{
		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetBeginExpressie(exp);
	}
	
	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;
		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetKijkNaActief(kijkNaActief);

	}
	public InteractieEditPanel getEditPanel()
	{	//return new AlgebraPijlenOpdrInteractiePanel();
		return new AlgebraPijlenOpdrInteractieEditPanel();
	}	
	
	public void setBounds(int x, int y, int b, int h)
	{	
//System.out.println("apoip set bounds " + b + " " + h);
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		if (algebraSchuifVeld == null) 
		{	algebraSchuifVeld = new AlgebraSchuifVeld(0, 0, b, h);
			add(algebraSchuifVeld, 0);
			algebraSchuifVeld.zetPlaatjes(goedkrul, foutkruis, halfkrul);
			algebraSchuifVeld.kijkNaKnop.addActionListener(this);
			//algebraSchuifVeld.zetKijkNaActief(kijkNaActief);
//System.out.println("as created");			
		}
		else
		{	algebraSchuifVeld.setSize(b, h);
			algebraSchuifVeld.kijkNaKnop.setLocation(110 + (getSize().width - 110 - 90) / 2, getSize().height - 30);
//System.out.println("as sized");		
		}
		algebraSchuifVeld.start();
		
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void zetBreedte(int b)
	{	algebraSchuifVeld.setSize(b, algebraSchuifVeld.getSize().height);
	
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void zetHoogte(int h)
	{	algebraSchuifVeld.setSize(algebraSchuifVeld.getSize().width, h);
	
		algebraSchuifVeld.tekenOpnieuw();
	}
	
	public void disableElements(boolean b)
	{
		algebraSchuifVeld.disableElements(b);
	}
	
	public void wis(){}
	
	public void zetMaat(){}
	
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
	
	public int getScoreMax()
	{	return scoreMax;
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
    {
    	kijkNa();
    }
    
    public void start()
    {	algebraSchuifVeld.tekenOpnieuw();
    }
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void maakDocentExpressies()
    {	docentExpressies.removeAllElements();
    	for (int i = 0; i < docentExpressieStrings.size(); i++)
    	{	String text = (String) docentExpressieStrings.elementAt(i);
    		String formuleText = "$f" + text + "@";
    		Expressie exp = FormuleParser_ap.geefExpressie(formuleText);
    		docentExpressies.addElement(exp);
    	}
    	
    }
    
    public void kijkNa()
    {	if (!kijkNaActief)
    		return;
  
    	ingevuld = !algebraSchuifVeld.veldIsLeeg();
    	
    	if (!ingevuld)
    		return;
    	
    	maakDocentExpressies();
    	
    	// geen opdracht, alles goed
    	if (docentExpressies.size() == 0)
    	{	score = scoreMax;
    		fireChangeEvent();
    		return;
    	}
    	
    	Vector leerlingExpressieUVS = algebraSchuifVeld.vindExpressieUVS();
//System.out.println("llgUVS = " + leerlingExpressieUVS.size());    	
//System.out.println("docS = " + docentExpressieStrings.size());
//System.out.println("docE = " + docentExpressies.size());    

		int hits = 0;
		// hier zijn er docent expressies
		for (int lCnt = 0; lCnt < leerlingExpressieUVS.size(); lCnt++)
		{	UitvoerSchuifComponent uvs = (UitvoerSchuifComponent) leerlingExpressieUVS.elementAt(lCnt);
			Expressie llgExp = null;
			if (uvs.geefUitvoer(0) != null)
				llgExp = uvs.geefUitvoer(0);
			else if (uvs.geefVerborgenUitvoer(0) != null)
				llgExp = uvs.geefVerborgenUitvoer(0);

			String llgExpStr = llgExp.toString();
//System.out.println(llgExpStr);
//System.out.println(uvs.geefBronDefaultVarnaam());
			String llgExpStrC = llgExpStr.replaceAll(uvs.geefBronDefaultVarnaam(), "x");
//System.out.println(llgExpStrC);
			llgExp = FormuleParser_ap.geefExpressie("$f" + llgExpStrC + "@");

			boolean correct = false;
			if (llgExp != null)
			{	
				
				for (int dCnt = 0; dCnt < docentExpressies.size(); dCnt++)
				{	Expressie docExp = (Expressie) docentExpressies.elementAt(dCnt);
//System.out.println("docExp = " + docExp.toString());
//System.out.println("llgExp = " + llgExp.toString());
					if (Algebra.isGelijkwaardig(docExp, llgExp))
					{	hits++;
						correct = true;
					}
				}
				
				if (correct)
				{	uvs.pijlUit[0].im = goedkrul;
				}
				else
				{
					uvs.pijlUit[0].im = foutkruis;
				}
			}
		}	
//System.out.println("hits = " + hits);
		
		int scorePerExpressie = scoreMax / docentExpressies.size();
		if (hits == 0)
			score = 0;
		else if (hits == docentExpressies.size())
			score = scoreMax;
		else
			score = hits * scorePerExpressie;
		
/*		
		// leerlingExpressieUVS.size() - hits is aantal foute expressies
		if (leerlingExpressieUVS.size() >= docentExpressies.size())
			score = Math.max(0, scoreMax - (leerlingExpressieUVS.size() - hits));
		else
			score = Math.max(0, scoreMax - (docentExpressies.size() - leerlingExpressieUVS.size() - hits));
*/			

		algebraSchuifVeld.tekenOpnieuw();
		
		fireChangeEvent();

/*
    	//fire actionEvent
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
*/		
    	
    }
    
    public void fireChangeEvent()
    {	ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
    	
    }
    public void kijkNa(int stapNr)
    {	kijkNa();
    }
    
    public void addActionListener(ActionListener al)
    {	listeners.addElement(al);
    }
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == algebraSchuifVeld.kijkNaKnop)
		{
			kijkNa();
		}
	}
}
