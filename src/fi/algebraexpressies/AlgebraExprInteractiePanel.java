package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;

import fi.algebraexpressies.schuifobjects.*;
import fi.algebraexpressies.expressies_ap.Expressie;
import fi.algebraexpressies.expressies_ap.FormuleParser_ap;
import fi.algebraexpressies.expressies_ap.Algebra;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.reflect.Constructor;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class AlgebraExprInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
																  ActionListener				
{	
	AlgebraExpressies applet;
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
	
	
	public AlgebraExprInteractiePanel()
	{	
		
		setLayout(null);
		// echte initiatie vind pas plaats na setBounds
		
		java.net.URL imageURL = AlgebraExpressies.class.getResource("resources/goedkrul.gif");
		if (imageURL != null) 
		{
		    goedkrulIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading goedkrul.gif.");
		}		
		imageURL = AlgebraExpressies.class.getResource("resources/foutkruis.gif");
		if (imageURL != null) 
		{
			foutkruisIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading foutkruis.");
		}
		imageURL = AlgebraExpressies.class.getResource("resources/goedkrulhalf.gif");
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
		if (h.containsKey("kijkNaActief"))
			kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
		zetKijkNaActief(kijkNaActief);

		if (h.containsKey("docentExpressieStrings"))
			docentExpressieStrings = (Vector) h.get("docentExpressieStrings");

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();
		
		algebraSchuifVeld.setState(h);
	}
	
	public void setEditState(Hashtable h)
	{	if (algebraSchuifVeld != null)
		{
			if (h.containsKey("docentExpressiesString"))
				docentExpressieStrings = (Vector) h.get("docentExpressieStrings");
		
			if (h.containsKey("kijkNaActief"))
				kijkNaActief = ((Boolean) h.get("kijkNaActief")).booleanValue();
			zetKijkNaActief(kijkNaActief);

			if (h.containsKey("scoreMax"))
				scoreMax = ((Integer) h.get("scoreMax")).intValue();
		
			algebraSchuifVeld.setEditModeState(h);
		}	
	}
	
	public Hashtable getState()
	{	
		return algebraSchuifVeld.getState();
	}
	
	public Hashtable getEditState()
	{	Hashtable h = algebraSchuifVeld.getState();
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
/*	
	public void zetTerugHeen(boolean b)
	{
		//algebraSchuifVeld.zetTerugHeen(b);
	}
*/
/*	
	public void zetTabelOptie(boolean b)
	{
		//algebraSchuifVeld.zetTabelOptie(b);
	}
*/
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
	
	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;
		if (algebraSchuifVeld != null)
			algebraSchuifVeld.zetKijkNaActief(kijkNaActief);

	}
	
	public InteractieEditPanel getEditPanel()
	{	return new AlgebraExprInteractieEditPanel();
	}	
	
	public void setBounds(int x, int y, int b, int h)
	{	
//System.out.println("apoip set bounds");
		
		if (h == 1)
			return;
		
		super.setBounds(x, y, b, h);
		if (algebraSchuifVeld == null) 
		{	algebraSchuifVeld = new AlgebraSchuifVeld(0, 0, b, h);
			add(algebraSchuifVeld, 0);
			
			algebraSchuifVeld.zetPlaatjes(goedkrul, foutkruis, halfkrul);
			algebraSchuifVeld.kijkNaKnop.addActionListener(this);
			
			
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

	
    public void stop(){}
    
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
			Vector varNamen = Algebra.geefVarN(llgExp);
			String llgExpStrC = new String(llgExpStr);
			for (int i = 0; i < varNamen.size(); i++)
			{
				String varNaam = (String) varNamen.elementAt(i);
				llgExpStrC = llgExpStr.replaceAll(varNaam, "x");
			}
			//String llgExpStrC = llgExpStr.replaceAll(uvs.geefBronDefaultVarnaam(), "x");
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
