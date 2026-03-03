package fi.heks;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;

import fi.heks.scobjects.*;
import fi.heks.vectortek.*;

public class HeksInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							                               ActionListener
									
{
	protected static Color bgColor = new Color(255, 255, 255);
	
	
	//JPanel paginaHolder;
	int bottomHeight = 0;
	int offSet = 5;
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int score = 0;
	int scoreMax = 10;
	
	ImageIcon opnieuwNLIcon, opnieuwENIcon;
	Image opnieuwNLImage, opnieuwENImage;
	
	InputStream inStream;
	
	boolean noSetBounds = false;
	boolean forceBounds = false;
	

	int paginaNummer = 1;
	ScPanel currentPagina;
	
	// opties Pagina 3 (23)
	boolean alleenErin = false;
	boolean alleenEruit = false;
	
	Tekening blokjePlus;
	Hashtable<String,Object> blokjePlusHash;
	Tekening blokjePlus24;
	Hashtable<String,Object> blokjePlus24Hash;
	Tekening blokjeMin;
	Hashtable<String,Object> blokjeMinHash;
	Tekening blokjeMin24;
	Hashtable<String,Object> blokjeMin24Hash;
	Tekening beginPot;
	Hashtable<String,Object> beginEindPotHash;
	Tekening potErin;
	Hashtable<String,Object> potErinHash;
	Tekening potEruit;
	Hashtable<String,Object> potEruitHash;
	Tekening emmer;
	Hashtable<String,Object> emmerHash;
	Tekening emmerKlein;
	Hashtable<String,Object> emmerKleinHash;
	Tekening vloer;
	Hashtable<String,Object> vloerHash;
	Tekening vloer23;
	Hashtable<String,Object> vloer23Hash;
	Tekening potinhoud;
	Hashtable<String,Object> potinhoudHash;
	Tekening potinhoud23;
	Hashtable<String,Object> potinhoud23Hash;
	Tekening potinhoud24;
	Hashtable<String,Object> potinhoud24Hash;
	Tekening pot;
	Hashtable<String,Object> potHash;
	Tekening pot23;
	Hashtable<String,Object> pot23Hash;
	Tekening pot24;
	Hashtable<String,Object> pot24Hash;

		
	public HeksInteractiePanel()
	{
		setLayout(null);
		setBackground(bgColor);
//System.out.println("constr heip bg = " + getBackground().toString());		
		setOpaque(true);
		
//System.out.println("klip " + getBackground().toString());		
		
		
		// echte initiatie vind pas plaats na setBounds
		
		
		// fonts	    
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		java.net.URL opnieuwNLURL = Heks.class.getResource("resources/opnieuwknop.gif");
		if (opnieuwNLURL != null) 
		{
			opnieuwNLIcon = new ImageIcon(opnieuwNLURL);
		}
		else 
		{
			System.out.println("Error reading opnieuwknop.gif.");
		}

		java.net.URL opnieuwENURL = Heks.class.getResource("resources/againKnop.gif");
		if (opnieuwENURL != null) 
		{
			opnieuwENIcon = new ImageIcon(opnieuwENURL);
		}
		else 
		{
			System.out.println("Error reading againknop.gif.");
		}
		
		opnieuwNLImage = opnieuwNLIcon.getImage();
		opnieuwENImage = opnieuwENIcon.getImage();

		blokjePlus = new Tekening(350, 80, 65, 65, this, "blokjePlus.gif");
		blokjePlusHash = blokjePlus.getState();
		blokjePlus24 = new Tekening(300, 30, 40, 40, this, "blokjePlus.gif");
		blokjePlus24Hash = blokjePlus24.getState();
		blokjeMin = new Tekening(350, 150, 65, 65, this, "blokjeMin.gif");
		blokjeMinHash = blokjeMin.getState();
		blokjeMin24 = new Tekening(300, 80, 40, 40, this, "blokjeMin.gif");
		blokjeMin24Hash = blokjeMin24.getState();
		beginPot = new Tekening(35, 85, 90, 65, this, "potzwart.gif");
		beginEindPotHash = beginPot.getState();
		potErin = new Tekening(30, 170, 100, 90, this, "potErin.gif");
		potErinHash = potErin.getState();
		potEruit = new Tekening(30, 260, 100, 90, this, "potEruit.gif");
		potEruitHash = potEruit.getState();
		emmer = new Tekening(400, 25, 140, 155, this, "emmer.gif");
		emmerHash = emmer.getState();
		emmerKlein = new Tekening(0, 0, 90, 105, this, "emmer.gif");
		emmerKleinHash = emmerKlein.getState();
		vloer = new Tekening(-10, 450, 430, 175, this, "vloer.gif");
		vloerHash = vloer.getState();
		vloer23 = new Tekening(-10, 430, 530, 175, this, "vloer.gif");
		vloer23Hash = vloer23.getState();
		potinhoud = new Tekening(20, 305, 375, 300, this, "inhoudnieuw.gif");
		potinhoudHash = potinhoud.getState();
		potinhoud23 = new Tekening(22, 240, 475, 335, this, "inhoudnieuw.gif");
		potinhoud23Hash = potinhoud23.getState();
		potinhoud24 = new Tekening(50, 60, 215, 155, this, "inhoudnieuw.gif");
		potinhoud24Hash = potinhoud24.getState();
		pot = new Tekening(20, 280, 380, 330, this, "potnieuw.gif");
		potHash = pot.getState();
		pot23 = new Tekening(20, 200, 480, 390, this, "potnieuw.gif");
		pot23Hash = pot23.getState();
		pot24 = new Tekening(50, 40, 220, 180, this, "potnieuw.gif");
		pot24Hash = pot24.getState();
		
	}

	public InputStream getStream(String resource) 
	{
		try 
		{
			InputStream in = Heks.class.getResourceAsStream(resource);
			if (in != null)
				return in;
		} catch (SecurityException sex) 
		{
			System.err.println(sex);
		}
		return null;
	}	
	
	public void paintComponent(Graphics g)
	{
		g.setColor(bgColor);
		g.fillRect(0, 0, getSize().width, getSize().height - 1);
		//g.setColor(Color.black);
		//g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		
		
	}
	
	public void zetPagina(int nummer)
	{
		
		if (nummer == paginaNummer)
			return;
		
//System.out.println("zetPagina " + paginaNummer + " wordt " + nummer);		
		paginaNummer = nummer;
		if (currentPagina != null)
		{	
//System.out.println("cp != null");			
			currentPagina.stop();
			noSetBounds = true;
			currentPagina.setVisible(false);
		}	
		//remove(currentPagina);
		currentPagina = null;
		
		// hier wisselen
		forceBounds = true;
		noSetBounds = false;
		setBounds(getLocation().x, getLocation().y, getSize().width, getSize().height);
	}

	public void zetAlleenErin()
	{
System.out.println("zetAlleenErin()");		
		if (paginaNummer == 3)
		{
			alleenErin = true;
			alleenEruit = false;
			
			Pagina23Panel p23 = (Pagina23Panel) currentPagina;
			p23.zetAlleenErin();
		}
	}

	public void zetAlleenEruit()
	{
		if (paginaNummer == 3)
		{
			alleenErin = false;
			alleenEruit = true;
			
			Pagina23Panel p23 = (Pagina23Panel) currentPagina;
			p23.zetAlleenEruit();

		}
	}
	
	public void zetKeuzeErinEruit()
	{
		if (paginaNummer == 3)
		{
			alleenErin = false;
			alleenEruit = false;
			
			Pagina23Panel p23 = (Pagina23Panel) currentPagina;
			p23.zetKeuzeErinEruit();

		}
	}
	
	

	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		System.out.println("heip zetOpdracht");		
		int paginaNummer = 1;
		if (b.containsKey("paginanummer"))
			paginaNummer = ((Integer) b.get("paginanummer")).intValue();
		zetPagina(paginaNummer);

//System.out.println("zetOpdr " + paginaNummer);

//if (currentPagina != null)		
//System.out.println("zetOpdr zetPagina != null");
//else
//System.out.println("zetOpdr zetPagina == null");

		boolean alleenErin = false;
		if (b.containsKey("alleenerin"))
			alleenErin = ((Boolean) b.get("alleenerin")).booleanValue();
		boolean alleenEruit = false;
		if (b.containsKey("alleeneruit"))
			alleenEruit = ((Boolean) b.get("alleeneruit")).booleanValue();

		if (currentPagina != null)
		{
//System.out.println("curPag != null");			
			if (alleenErin)
				zetAlleenErin();
			else if (alleenEruit)
				zetAlleenEruit();
			
			currentPagina.setState(b);
			
		}
		else
		{
//System.out.println("curPag == null");			
		}
		
	}
	
	public void setState(Hashtable b)
	{
		
System.out.println("heip setState");

		if (currentPagina != null)
			currentPagina.setState(b);
	}
	
	public void setEditState(Hashtable b)
	{
System.out.println("heip setEditState");

		int paginaNummer = 1;
		if (b.containsKey("paginanummer"))
			paginaNummer = ((Integer) b.get("paginanummer")).intValue();
		zetPagina(paginaNummer);
		boolean alleenErin = false;
		if (b.containsKey("alleenerin"))
			alleenErin = ((Boolean) b.get("alleenerin")).booleanValue();
		boolean alleenEruit = false;
		if (b.containsKey("alleeneruit"))
			alleenEruit = ((Boolean) b.get("alleeneruit")).booleanValue();

		if (currentPagina != null)
		{			
System.out.println("curPag != null");			
			if (alleenErin)
				zetAlleenErin();
			else if (alleenEruit)
				zetAlleenEruit();
			
			currentPagina.setState(b);
			
		}

	}
	
	public Hashtable getState()
	{
System.out.println("heip getState");		
		Hashtable h = new Hashtable();
		if (currentPagina != null)
			h = currentPagina.getState();
		
		return h;

		
	}
	
	public Hashtable getEditState()
	{
System.out.println("heip getEditState");		

		Hashtable h = getState();
		
		h.put("paginanummer", new Integer(paginaNummer));
		h.put("alleenerin", new Boolean(alleenErin));
		h.put("alleeneruit", new Boolean(alleenEruit));
		
		h.put("blokjeplushash", blokjePlusHash);
		h.put("blokjeplus24hash", blokjePlus24Hash);
		h.put("blokjeminhash", blokjeMinHash);
		h.put("blokjemin24hash", blokjeMin24Hash);
		h.put("begineindpothash", beginEindPotHash);
		h.put("poterinhash", potErinHash);
		h.put("poteruithash", potEruitHash);
		h.put("emmerhash", emmerHash);
		h.put("emmerkleinhash", emmerKleinHash);
		
		h.put("vloerhash", vloerHash);
		h.put("vloer23hash", vloer23Hash);
		h.put("potinhoudhash", potinhoudHash);
		h.put("potinhoud23hash", potinhoud23Hash);
		h.put("potinhoud24hash", potinhoud24Hash);
		h.put("pothash", potHash);
		h.put("pot23hash", pot23Hash);
		h.put("pot24hash", pot24Hash);
		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new HeksInteractieEditPanel();
	}
		
	
	public void setBounds(int x, int y, int b, int h)
	{
		
		if (noSetBounds)
		{	noSetBounds = false;
System.out.println("noSetBounds");		
			return;
		}
		
		if (h == 1)
			return;
		
		if ((getLocation().x == x) && (getLocation().y == y) &&
			(getSize().width == b) && (getSize().height == h) && !forceBounds)
		{	
			return;
		}
		else
		{
			forceBounds = false;
			
System.out.println("forceBounds");			
		}
		
		int oldWidth = getSize().width;
		int oldHeight = getSize().height;
			
		super.setBounds(x, y, b, h);
System.out.println("heip set bounds " + b + " " + h);		
		
		
		if (currentPagina == null) 
		{	
			if (paginaNummer == 1)
			{	currentPagina = new Pagina21Panel(0, 0, 785, 660, this);
//System.out.println("pagina 1");			
			}
			else if (paginaNummer == 2)
			{	currentPagina = new Pagina22Panel(0, 0, 785, 660, this);
//System.out.println("pagina 2");			
			}
			else if (paginaNummer == 3)
			{	currentPagina = new Pagina23Panel(0, 0, 785, 660, this);
//System.out.println("pagina 3");			
			}
			else if (paginaNummer == 4)
			{	currentPagina = new Pagina24Panel(0, 0, 3*266/2 -5, 3*151/2 -5, this);
				System.out.println("pagina 4");			
			}
			
			double sx = ((1.0 * getSize().width) / currentPagina.getSize().width);
			double sy = ((1.0 * getSize().height) / currentPagina.getSize().height);
			double schaal = Math.min(sx, sy)* 0.95;

			currentPagina.schaal(schaal);
			ImageButton opnieuwKnop = null; 
			if (paginaNummer == 1)
			{	Pagina21Panel pagina = (Pagina21Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.eindPot.getLocation().y + 100);
			
			}
			else if (paginaNummer == 2)
			{	Pagina22Panel pagina = (Pagina22Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.eindPot.getLocation().y + 100);
			}
			if (paginaNummer == 3)
			{	Pagina23Panel pagina = (Pagina23Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.emmer.getLocation().y + 100);
			}
	
			
			add(currentPagina);
//System.out.println("currentPagina created w = " + currentPagina.getSize().width + " h = " + currentPagina.getSize().height);	
//System.out.println("sx = " + UF.format(sx,2) + " sy = " + UF.format(sy,2) + " s = " + UF.format(schaal,2));
			currentPagina.start();
		}
		else
		{	
			currentPagina.stop();
			
			double sx = ((1.0 * getSize().width) / currentPagina.relb);
			double sy = ((1.0 * getSize().height) / currentPagina.relh);
			double schaal = Math.min(sx, sy)* 0.95;

			currentPagina.schaal(schaal);
			ImageButton opnieuwKnop = null; 
			if (paginaNummer == 1)
			{	Pagina21Panel pagina = (Pagina21Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.eindPot.getLocation().y + 100);
			}
			else if (paginaNummer == 2)
			{	Pagina22Panel pagina = (Pagina22Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.eindPot.getLocation().y + 100);
			}
			if (paginaNummer == 3)
			{	Pagina23Panel pagina = (Pagina23Panel) currentPagina;
				opnieuwKnop = pagina.opnieuwKnop;
				opnieuwKnop.setLocation(opnieuwKnop.getLocation().x, pagina.emmer.getLocation().y + 100);
			}

//System.out.println("currentPagina scaled w = " + currentPagina.getSize().width + " h = " + currentPagina.getSize().height);
//System.out.println("sx = " + UF.format(sx,2) + " sy = " + UF.format(sy,2) + " s = " + UF.format(schaal,2));

			currentPagina.start();
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
	
	public int getScoreMax()
	{	return 0;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
	public void actionPerformed(ActionEvent e)
	{
		
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}
}
