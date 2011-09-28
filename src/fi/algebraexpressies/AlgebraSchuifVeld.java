package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;

import fi.algebraexpressies.schuifobjects.*;

import javax.swing.*;

public class AlgebraSchuifVeld extends SchuifVeld 
							   implements //ItemListener, 
							              MouseListener, MouseMotionListener,ActionListener
{	

	private ImageIcon GOEDKRUL,FOUTKRUIS, GOEDKRULHALF;	
	private JButton wisKnop;
	InvulPanel ip;
	private JCheckBox grafiekCheckbox;
	AlgebraSchuifComponent[] schuifcomponenten;
	GrafiekComponent grafiekComponent;
	int aantalSc;
	
	private Hashtable editmodeState;
	
	//boolean buttonsAdded;

	public ZoomStateHolder zoomStateHolder;
	private Font font;
	
	boolean toolkit = true;
	boolean alleenInvullen = false;
	boolean isDemo = false;
	
	boolean brugklas = false;
	boolean tabelOptie = true;	
	boolean grafiekOptie = true;
	
	boolean scrollOptie = true;
	boolean zoomOptie = true; 
	
	
	public AlgebraSchuifVeld(int x, int y, int b, int h)
	{	
		super(x,y,b,h);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		font = new Font("SansSerif", Font.PLAIN,12);
		if (AlgebraExpressies.rb.getLocale().toString().equals("en"))
			font = new Font("SansSerif", Font.PLAIN,11);
		
		zoomStateHolder = new ZoomStateHolder(this);
		
		
		ip = new InvulPanel(this, 8, 380, 90, 50); // was 400
		ip.setBackground(Color.lightGray);
		add(ip);
		
		grafiekCheckbox = new JCheckBox(AlgebraExpressies.rb.getString("grafiekLabel"));
		grafiekCheckbox.setFont(font);
		//grafiekCheckbox.addItemListener(this);
		grafiekCheckbox.addActionListener(this);
		grafiekCheckbox.setBounds(8, 320, 80, 20); // was 340
		grafiekCheckbox.setBackground(Color.lightGray);
		add(grafiekCheckbox);
		
		wisKnop = new JButton(AlgebraExpressies.rb.getString("wisKnopLabel"));
		wisKnop.setFont(font);
		wisKnop.setBounds(18, 350, 70, 20); // was 370
		wisKnop.addActionListener(this);
		add(wisKnop);
		
		add(schuiflaag, 0);		
		
		maakStapel();
		
		grafiekComponent = new GrafiekComponent(this, 500, 200, 200, 230);
		grafiekComponent.isStapel = false;		
		
		
	}
	
	public void zetPlaatjes(ImageIcon gk, ImageIcon fk, ImageIcon kh)
	{	GOEDKRUL = gk;
		GOEDKRULHALF = kh;
		FOUTKRUIS = fk;
	}

	public void zetToolkit(boolean b)
	{
		toolkit = b;
		if (toolkit)
		{
			alleenInvullen = false;
			isDemo = false;
			
			if (getLocation().x < 0) 
			{	//setBounds(0, 0, getSize().width - 110, getSize().height);
//System.out.println("toolkit");				
				setLocation(0, 0);
//System.out.println("toolkit");				
				setSize(getSize().width - 110, getSize().height);
			}

			grafiekCheckbox.setVisible(grafiekOptie);
			wisKnop.setVisible(true);
			
			//zetVeranderd();
			tekenOpnieuw();
		}
	}
	
	public void zetAlleenInvullen(boolean b)
	{
		alleenInvullen = b;
		if (alleenInvullen)
		{
			toolkit = false;
			isDemo = false;
			
			if (getLocation().x == 0)
			{	//setBounds(- 110, 0, getSize().width + 110, getSize().height);
//System.out.println("alleenInvullen");				
				setLocation(- 110, 0);
//System.out.println("alleenInvullen");				
				setSize(getSize().width + 110, getSize().height);
			}
			
			grafiekCheckbox.setVisible(false);
			wisKnop.setVisible(false);
			
			//zetVeranderd();
			tekenOpnieuw();
		}
	}
	
	public void zetIsDemo(boolean b)
	{
		isDemo = b;
		if (isDemo)
		{
			toolkit = false;
			alleenInvullen = false;
			
			if (getLocation().x == 0)
			{	//setBounds(- 110, 0, getSize().width + 110, getSize().height);
//System.out.println("isDemo");				
				setLocation(- 110, 0);
				setSize(getSize().width + 110, getSize().height);
			}

			
			grafiekCheckbox.setVisible(false);
			wisKnop.setVisible(false);
			
			//zetVeranderd();
			tekenOpnieuw();
		}
	}
	

	// als b==true, wat te doen met kettingen die niet-brugklas dingen bevatten?
	// voorlopig maar even niets	
	public void zetBrugklas(boolean b)
	{
		brugklas = b;
		for (int i = 0; i < aantalSc; i++)
		{	if ((schuifcomponenten[i].isStapel) &&
				((schuifcomponenten[i] instanceof KwadraatSchuifComponent) ||
				 (schuifcomponenten[i] instanceof WortelSchuifComponent) ||
				 (schuifcomponenten[i] instanceof MachtSchuifComponent))
				)
			{
				schuifcomponenten[i].setVisible(!b);
				for (int pCnt = 0; pCnt < schuifcomponenten[i].aantalPu; pCnt++)
				{	if (schuifcomponenten[i].pijlUit[pCnt] != null)
						schuifcomponenten[i].pijlUit[pCnt].setVisible(!b);
					
				}
			}
		}
		tekenOpnieuw();
	}
/*
	public void zetTabelOptie(boolean b)
	{
		tabelOptie = b;
		tabelCheckbox.setVisible(b);
	}
*/	
	public void zetGrafiekOptie(boolean b)
	{
		grafiekOptie = b;
		grafiekCheckbox.setVisible(b);
	}
	
	public void zetScrollOptie(boolean b)
	{
		scrollOptie = b;
		
		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(scrollOptie);
			}
		}
			
	}

	
	public void zetZoomOptie(boolean b)
	{
		zoomOptie = b;

		for (int i = 0; i < aantalSc; i++)
		{
			if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetZoomInTabel(zoomOptie);
			}
		}
		
		tekenOpnieuw();
		
	}
	
	public Hashtable getState()
	{	
		
		int aantalSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		//boolean[][] connections = null;
		int[][] connections = null;
		int[] graphConnections = null;
//		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
//		boolean links = false;
		Hashtable zoomStateHolderState = null;

		aantalSc = this.aantalSc;
		classNames = new String[aantalSc];
		posX = new int[aantalSc];
		posY = new int[aantalSc];
		scStates = new Hashtable[aantalSc];
		for (int i = 0; i < aantalSc; i++)
	    {	classNames[i] = schuifcomponenten[i].getClass().getName();
	    	posX[i] = schuifcomponenten[i].getLocation().x;
	    	posY[i] = schuifcomponenten[i].getLocation().y;
	    	scStates[i] = schuifcomponenten[i].getState();
	    }
	    
		connections = new int[aantalSc][aantalSc];
		for (int i = 0; i < aantalSc; i++)
	    {	for (int j = 0; j < aantalSc; j++)
			{	if (schuifcomponenten[j].pijlIn1 != null && 
	    			schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i])
	    			connections[i][j] = 1;
	    		if (schuifcomponenten[j].pijlIn2 != null && 
	    			schuifcomponenten[j].pijlIn2.zender == schuifcomponenten[i])
	    			connections[i][j] = 2;	
			
				//connections[i][j] = schuifcomponenten[j].pijlIn1 != null && 
	    		//					schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i];
			}
	    }
	    
//	    tabel = tabelCheckbox.getState();
//		tabel = tabelCheckbox.isSelected();

//	    grafiek = grafiekCheckbox.getState();
		grafiek = grafiekCheckbox.isSelected();
		
	    expressie = ip.isExpr();
//	    links = this.links;
	    zoomStateHolderState = zoomStateHolder.getState();
	    	
	    graphConnections = new int[10];
	    for (int i = 0; i < 10; i++)
		{	graphConnections[i] = -1;
		}
		if (grafiek)
	    {	for (int i = 0; i < 10; i++)
			{	Pijl p = grafiekComponent.pijlenIn[i];
				for (int j = 0; j < aantalSc; j++)
		   		{	if (p != null && schuifcomponenten[j] == p.zender)
		   				graphConnections[i] = j;
				}
			}
	    }
		
		Hashtable h = new Hashtable();
		
		
	    h.put("aantalSc", new Integer(aantalSc));
	    h.put("classNames", classNames);
	    h.put("posX", posX);
	    h.put("posY", posY);
	    h.put("scStates", scStates);
	    h.put("connections", connections);
	    h.put("graphConnections", graphConnections);
//	    h.put("tabel", new Boolean(tabel));
	    h.put("grafiek", new Boolean(grafiek));
	    h.put("expressie", new Boolean(expressie));
//	    h.put("links", new Boolean(links));
	    h.put("zoomStateHolderState", zoomStateHolderState);
	    
	    h.put("toolkit", new Boolean(toolkit));
	    h.put("alleenInvullen", new Boolean(alleenInvullen));
	    h.put("isDemo", new Boolean(isDemo));
	    
	    h.put("brugklas", new Boolean(brugklas));
	    h.put("grafiekOptie", new Boolean(grafiekOptie));
	    
	    h.put("zoomOptie", new Boolean(zoomOptie));
	    
	    return h;
	}
	
	public void setEditModeState(Hashtable h)
	{	editmodeState = h;
		setState(h);
	}
		
    public void setState(Hashtable h)
    {	
    	
    	
//System.out.println("setState start");
//System.out.println("b = " + getSize().width);
//System.out.println("ob = " + origBreedte);

boolean toolkit = true;
if ((h != null) && h.containsKey("toolkit"))
	toolkit = ((Boolean) h.get("toolkit")).booleanValue();
this.toolkit = toolkit;

boolean alleenInvullen = false;
if ((h != null) && h.containsKey("alleenInvullen"))
	alleenInvullen = ((Boolean) h.get("alleenInvullen")).booleanValue();
this.alleenInvullen = alleenInvullen;

boolean isDemo = false;
if ((h != null) && h.containsKey("isDemo"))
	isDemo = ((Boolean) h.get("isDemo")).booleanValue();
this.isDemo = isDemo;

    	int aantalSc = 0;
    	String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		int[][] connections = null;
		int[] graphConnections = null;
//		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
//		boolean links = false;
		Hashtable zoomStateHolderState = null;
	
		
		try
		{
			aantalSc = ((Integer) h.get("aantalSc")).intValue();
			classNames = (String[]) h.get("classNames");
			posX = (int[]) h.get("posX");
			posY = (int[]) h.get("posY");
			scStates = (Hashtable[]) h.get("scStates");
			connections = (int[][]) h.get("connections");
			graphConnections = (int[]) h.get("graphConnections");
//			tabel = ((Boolean) h.get("tabel")).booleanValue();
			grafiek = ((Boolean) h.get("grafiek")).booleanValue();
			expressie = ((Boolean) h.get("expressie")).booleanValue();
//			links = ((Boolean) h.get("links")).booleanValue();
			zoomStateHolderState = (Hashtable) h.get("zoomStateHolderState");
		}
		catch(Exception ex)
		{	return;
		}
		
		zoomStateHolder.setState(zoomStateHolderState);
		
		int n = this.aantalSc;
		for (int i = 0; i < n; i++)
		{	verwijder(schuifcomponenten[0]);
		}
		
		this.aantalSc = aantalSc;
		
		schuifcomponenten = new AlgebraSchuifComponent[200];
		for (int i = 0; i < aantalSc; i++)
	    {	try
		    {  	Class c = Class.forName(classNames[i]);
		    
		      	Constructor cc = c.getDeclaredConstructor(
		      			new Class[] { AlgebraSchuifVeld.class, int.class, int.class, int.class, int.class } );
		      	int breedte = 40;
		      	int hoogte = 30;
		      	if (classNames[i].equals("fi.algebraexpressies.GrafiekComponent"))
		      	{	breedte = 200;
		      		hoogte = 230;
		      		Object o = cc.newInstance(
		      			new Object[] {this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte)});
		      	   	schuifcomponenten[i] = (AlgebraSchuifComponent) o;
		      	   	grafiekComponent = (GrafiekComponent) schuifcomponenten[i];
		      	   	
		      	}
		      	else
		      	{	Object o = cc.newInstance(
		      			new Object[] {this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte)});
		      		schuifcomponenten[i] = (AlgebraSchuifComponent) o;
		      	}
		    }
	      	catch(Exception e)
	      	{	
	      	}
	    }
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	if (!(schuifcomponenten[i] instanceof GrafiekComponent)) 
	    	{	schuifcomponenten[i].setState(scStates[i]);
	    		// bij een GrafiekComponent lukt dit niet omdat die bij setState de parent nodig heeft en die heeft ie nog niet
//IS DIT ZO?/	    			
	    	}
	    }
	    
		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{	
			if (!(schuifcomponenten[i] instanceof GrafiekComponent))
			{	
				Pijl p = new Pijl(this);
				//if (schuifcomponenten[i].isStapel) 
				//{	schuifcomponenten[i].zetLinks(links);
				//	p.zetLinks(links);
				//}	
				schuifcomponenten[i].voegPijlToe(p);
			}	
			add(schuifcomponenten[i]);
		}
		
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	for(int j = 0; j < aantalSc; j++)
			{	if (connections[i][j] == 1) 
				{	Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
					schuifcomponenten[j].verbind(p, true);
					p.zetVerbonden(schuifcomponenten[j]);
				}
				if (connections[i][j] == 2) 
				{	Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
					schuifcomponenten[j].verbind(p, false);
					p.zetVerbonden(schuifcomponenten[j]);
				}
			}
	    }
	    
	    
	    if (grafiek)
	    {  	for (int i = 0; i < 10; i++)
			{	if (graphConnections[i] != -1)
				{	Pijl p = schuifcomponenten[graphConnections[i]].pijlUit[schuifcomponenten[graphConnections[i]].aantalPu - 1];
					grafiekComponent.verbind(p, i);
					p.zetVerbonden(grafiekComponent);
				}
		    }
		}
		
		for (int i = 0; i < aantalSc; i++)
	    {	schuifcomponenten[i].setState(scStates[i]);
	    }
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	schuifcomponenten[i].zetVeranderd(20);
	    	if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
	    	{	((UitvoerSchuifComponent) schuifcomponenten[i]).zetToonWaarde(!expressie);
//	    		((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(true);
	    		schuifcomponenten[i].zetVeranderd(20);
	    	}
	    }
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	if (schuifcomponenten[i] instanceof GrafiekComponent)
	    	{	schuifcomponenten[i].setState(scStates[i]);
	    		schuifcomponenten[i].zetVeranderd(20);
	    	}
	    }

	    
//	    tabelCheckbox.setSelected(tabel);
	    grafiekCheckbox.setSelected(grafiek);	    
	    
	    ip.zetExpressie(expressie);

	    Enumeration en = zoomStateHolder.keys();
		while(en.hasMoreElements())
		{	String key = (String) en.nextElement();
			setZoomStates(key, zoomStateHolder.getZoomState(key));
		}
	    
		zetToolkit(toolkit);
//System.out.println("toolkit " + toolkit);		
		
		zetAlleenInvullen(alleenInvullen);
//System.out.println("alleenInvullen " + alleenInvullen);

		zetIsDemo(isDemo);
//System.out.println("isDemo " + isDemo);		

//System.out.println("b = " + getSize().width);
//System.out.println("ob = " + origBreedte);

		boolean brugklas = false;
		if (h.containsKey("brugklas"))
			brugklas = ((Boolean) h.get("brugklas")).booleanValue();
		zetBrugklas(brugklas);
		
		boolean grafiekOptie = true;
		if (h.containsKey("grafiekOptie"))
			grafiekOptie = ((Boolean) h.get("grafiekOptie")).booleanValue();
		zetGrafiekOptie(grafiekOptie);

		boolean zoomOptie = true;
		if (h.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		zetZoomOptie(zoomOptie);
		
//System.out.println("setState end");		
//System.out.println("b = " + getSize().width);
		
		tekenOpnieuw();
			
    }
	
	public void maakStapel()
	{	aantalSc = 0;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this, 30, 55, 40, 30); // was 80			
		aantalSc++;
		schuifcomponenten[aantalSc] = new OptelSchuifComponent(this, 7, 130, 40, 30); // was 150			
		aantalSc++;
		schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this, 54, 130, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this, 7, 170, 40, 30); // was 190	
		aantalSc++;
		schuifcomponenten[aantalSc] = new DeelSchuifComponent(this, 54, 170, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new KwadraatSchuifComponent(this, 7, 210, 40, 30); // was 230		
		aantalSc++;
		schuifcomponenten[aantalSc] = new WortelSchuifComponent(this, 54, 210, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new MachtSchuifComponent(this, 7, 250, 40, 30); // was 270			
		aantalSc++;
		
		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{	schuifcomponenten[i].voegPijlToe(new Pijl(this));
			add(schuifcomponenten[i]);
		}
		
		//add(grafiekCheckbox, 0);
		//add(wisKnop, 0);
		//add(ip, 0);
	}
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();				
		g.setColor(Color.white);
		g.fillRect(0, 0, dd.width, dd.height);
		if (toolkit)
		{	
			g.setColor(Color.lightGray);
			g.fillRect(0, 0, 100, dd.height);
			g.setColor(Color.black);
			g.drawLine(100, 0, 100, dd.height - 1);
			g.drawRect(0, 0, dd.width - 1, dd.height - 1);
		}
		
		//g.drawString(Integer.toString(aantalSc),20,10);
		//g.drawString(Integer.toString(schuiflaag.getComponentCount()),50,10);
		//g.drawString(Integer.toString(getComponentCount()),80,10);
		
		//int intveranderd=0,intresized=0;
		//if(veranderd)intveranderd=1;
		//if(resized)intresized=1;
		///g.drawString(Integer.toString(intveranderd),80,25);
		//g.drawString(Integer.toString(intresized),80,40);
		
		//g.drawString("Invoer",35,25);
		
		//g.drawString(AlgebraExpressies.rb.getString("invoerVakLabel"),35,80);
		//g.drawString(AlgebraExpressies.rb.getString("bewerkingenLabel"),20,145);
		
		String s = null;
		FontMetrics fm = g.getFontMetrics();
		int lengte = 0;
		
		s = AlgebraExpressies.rb.getString("invoerVakLabel");
		lengte = fm.stringWidth(s);
		if (lengte > 100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize()-1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s, 50 - lengte / 2, 50); // was 80
		
		s = AlgebraExpressies.rb.getString("bewerkingenLabel");
		lengte = fm.stringWidth(s);
		if (lengte > 100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize() - 1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s, 50 - lengte / 2, 125); // was 145
		
	}
	
	public void zetSchuiver(SchuifComponent sc)
	{	schuiflaag.add(sc, 0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i < asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
			schuiflaag.add(asc.pijlUit[i]);
		}
		if (asc.pijlIn1 != null)
			schuiflaag.add(asc.pijlIn1);
		if (asc.pijlIn2 != null)
			schuiflaag.add(asc.pijlIn2);
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent) asc;
			for (int i=0; i< gsc.aantalPijlenIn; i++)
			{	schuiflaag.add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
	
	public void losSchuiver(SchuifComponent sc)
	{	add(sc); //,0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i < asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
				add(asc.pijlUit[i]);
		}
		if (asc.pijlIn1 != null)
			add(asc.pijlIn1);
		if (asc.pijlIn2 != null)
			add(asc.pijlIn2);
		
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent) asc;
			for (int i = 0; i < gsc.aantalPijlenIn; i++)
			{	add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
	
	public void zetStapel(AlgebraSchuifComponent asc)
	{	int x = asc.getLocation().x;
		int y = asc.getLocation().y;
		int b = asc.getSize().width;
		int h = asc.getSize().height;
		//if(asc instanceof InvoerSchuifComponent)
		//{ schuifcomponenten[aantalSc] = new InvoerSchuifComponent(this ,x,y,b,h);
		//}
		if (asc instanceof UitvoerSchuifComponent)
		{    schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof OptelSchuifComponent)
		{    schuifcomponenten[aantalSc] = new OptelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof AftrekSchuifComponent)
		{    schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof VermenigvuldigSchuifComponent)
		{    schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof DeelSchuifComponent)
		{    schuifcomponenten[aantalSc] = new DeelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof KwadraatSchuifComponent)
		{    schuifcomponenten[aantalSc] = new KwadraatSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof WortelSchuifComponent)
		{    schuifcomponenten[aantalSc] = new WortelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof MachtSchuifComponent)
		{    schuifcomponenten[aantalSc] = new MachtSchuifComponent(this ,x, y, b, h);
		}
		else 
			return;
		schuifcomponenten[aantalSc].voegPijlToe(new Pijl(this));
		add(schuifcomponenten[aantalSc]);
		aantalSc++;
		
/*		
		if(!buttonsAdded)
		{	getParent().add(grafiekCheckbox,0);
			getParent().add(wisKnop,0);
			getParent().add(ip,0);
			buttonsAdded = true;
		}
*/		
	}
	
	
	
	public void verwijder(AlgebraSchuifComponent sc)
	{	for (int i = 0; i < aantalSc; i++)
		{	if (schuifcomponenten[i] == sc)
			{	if (sc instanceof GrafiekComponent)
				{	GrafiekComponent gsc = (GrafiekComponent) sc;
					while(gsc.aantalPijlenIn > 0)
					{	Pijl p = gsc.pijlenIn[0];
						gsc.maakLos(gsc.pijlenIn[0]);
						p.zender.verwijderPijl();
						p.pijlTerug();
					}
				}
				if (sc.pijlIn1 != null)
				{	Pijl p = sc.pijlIn1;
					sc.maakLos(sc.pijlIn1);
					p.zender.verwijderPijl();
					p.pijlTerug();
				}
				if (sc.pijlIn2 != null)
				{	Pijl p = sc.pijlIn2;
					sc.maakLos(sc.pijlIn2);
					p.zender.verwijderPijl();
					p.pijlTerug();
					
				}	
				for (int k = 0; k < sc.aantalPu; k++)
				{	if (sc.pijlUit[k].ontvanger != null)
					{	AlgebraSchuifComponent as = sc.pijlUit[k].ontvanger;
						as.maakLos(sc.pijlUit[k]);
						as.zetVeranderd(20);
					}					
					remove(sc.pijlUit[k]);
				}
				remove(sc);
				for (int j = i; j < aantalSc; j++)
				{	schuifcomponenten[j] = schuifcomponenten[j + 1];
				}
				aantalSc--;
				tekenOpnieuw();
				return;
			}
		}
	}
	
	public void zetVeranderd()
	{	for (int i = 0; i <aantalSc; i++)
		{	if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
			{	boolean b = !ip.isExpr();
				((UitvoerSchuifComponent)schuifcomponenten[i]).zetToonWaarde(b);
			}
		}
		tekenOpnieuw();
	}

	public void setBounds(int x, int y, int b, int h)
	{
		if ((getLocation().x == x) && (getLocation().y == y) &&
			(getSize().width == b) && (getSize().height == h))
		{	
//System.out.println("setBounds return");			
			return;
		}
		
		int oldX = getLocation().x;
		if ((oldX < 0) && !toolkit)
		{
			super.setBounds(x, y, b - oldX, h);
//System.out.println("setBounds x = " + x + " y = " + y + " w = " + b + " h = " + h);
			
		}
		else
		{
			super.setBounds(x, y, b, h);
//System.out.println("setBounds x = " + x + " y = " + y + " w = " + b + " h = " + h);
			
		}

//		zetToolkit(toolkit);
//		zetAlleenInvullen(alleenInvullen);
//		zetIsDemo(isDemo);
	}
	
	public void setSize(int b, int h)
	{	
		
		if ((getSize().width == b) && (getSize().height == h))
			return;
		
		for (int i = 0; i < getComponentCount(); i++)
		{	if (this.getComponent(i)instanceof Pijl)
			{	getComponent(i).setSize(b, h);
			}
		}
		super.setSize(b, h);
	}
	
	public void setZoomStates(String varnaam, ZoomState zoomState)
	{	for (int i = 0; i < aantalSc; i++)
			{	if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
				{	((UitvoerSchuifComponent) schuifcomponenten[i]).setZoomState(varnaam, zoomState);
					
				}
				if (schuifcomponenten[i] instanceof GrafiekComponent)
				{	((GrafiekComponent) schuifcomponenten[i]).setZoomState(varnaam, zoomState);
				}
			}
		tekenOpnieuw();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if (e.getSource() == wisKnop)
		{	
			if (editmodeState == null)
			{	
				int n = aantalSc;
				for (int i = 0; i < n; i++)
				{	verwijder(schuifcomponenten[0]);
				}
				maakStapel();
				if (grafiekCheckbox.isSelected())
				{	schuifcomponenten[aantalSc] = grafiekComponent;
					aantalSc++;
					add(grafiekComponent);
				}
				zoomStateHolder = new ZoomStateHolder(this);				
			}
			else
				setState(editmodeState);
		}
		else if (e.getSource() == grafiekCheckbox)
		{	boolean b = grafiekCheckbox.isSelected();
			if (b)
			{	grafiekComponent = new GrafiekComponent(this, 500, 200, 200, 230);
				schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
			else
			{	verwijder(grafiekComponent);
			}
			tekenOpnieuw();
		}
		
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		start = true;
	}	
	public void mouseDragged(MouseEvent e)
	{}
	public void mouseReleased(MouseEvent e)
	{}
	public void mouseMoved(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}
	public void mouseClicked(MouseEvent e)
	{}
	public void mouseEntered(MouseEvent e)
	{}
}
