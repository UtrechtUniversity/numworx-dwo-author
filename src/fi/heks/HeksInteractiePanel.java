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

public class HeksInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							                               ActionListener
									
{
	protected static Color bgColor = new Color(238, 238, 238);
	
	
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

		java.net.URL opnieuwENURL = Heks.class.getResource("resources/againknop.gif");
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
}
