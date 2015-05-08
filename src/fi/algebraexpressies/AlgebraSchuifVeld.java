package fi.algebraexpressies;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import fi.algebraexpressies.schuifobjects.*;
import fi.beans.base64code.StringCodeObject;

import javax.swing.*;

public class AlgebraSchuifVeld extends SchuifVeld 
							   implements //ItemListener, 
							              MouseListener, MouseMotionListener,ActionListener
{	

	Image GOEDKRUL,FOUTKRUIS, GOEDKRULHALF;	
	private JButton wisKnop;
	InvulPanel ip;
	private JCheckBox grafiekCheckbox;
	AlgebraSchuifComponent[] schuifcomponenten;
	GrafiekComponent grafiekComponent;
	int aantalSc;
	JButton kijkNaKnop;
	
	private Hashtable editmodeState;

	private JPopupMenu popup;
	JMenuItem copyItem; 
	
	boolean frozen = false;
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
	
	AlgebraExprInteractiePanel aeip;
	
	
	public AlgebraSchuifVeld(int x, int y, int b, int h, AlgebraExprInteractiePanel aeip)
	{	
		super(x,y,b,h);
		
		this.aeip = aeip;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		font = new Font("SansSerif", Font.PLAIN,12);
		if (AlgebraExpressies.rb.getLocale().toString().equals("en"))
			font = new Font("SansSerif", Font.PLAIN,11);
		
		zoomStateHolder = new ZoomStateHolder(this);
		
		
		ip = new InvulPanel(this, 8, 335, 90, 50); // was 380
		ip.setBackground(new Color(210, 210, 210));
		add(ip);
		
		grafiekCheckbox = new JCheckBox(AlgebraExpressies.rb.getString("grafiekLabel"));
		grafiekCheckbox.setFont(font);
		//grafiekCheckbox.addItemListener(this);
		grafiekCheckbox.addActionListener(this);
		grafiekCheckbox.setBounds(8, 275, 80, 20); // was 320
		grafiekCheckbox.setBackground(new Color(210, 210, 210));
		add(grafiekCheckbox);
		
		wisKnop = new JButton(AlgebraExpressies.rb.getString("wisKnopLabel"));
		wisKnop.setFont(font);
		wisKnop.setBounds(18, 305, 70, 20); // was 350
		wisKnop.addActionListener(this);
		add(wisKnop);
		
		kijkNaKnop = new JButton(AlgebraExpressies.rb.getString("kijkNaTekst"));
		kijkNaKnop.setFont(font);
//		kijkNaKnop.setBounds(5, 385, 90, 20);
		kijkNaKnop.setBounds(110 + (getSize().width - 110 - 90) / 2, getSize().height - 30, 90, 20);		
//		kijkNaKnop.addActionListener(this);
		kijkNaKnop.setVisible(false);
		add(kijkNaKnop,0);
		
		
		add(schuiflaag, 0);		
		
		maakStapel();
		
		grafiekComponent = new GrafiekComponent(this, 400, 200, 200, 230);
		grafiekComponent.isStapel = false;		
		
		popup = new JPopupMenu();
		
		//JMenuItem mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup2Label1"));
		copyItem = new JMenuItem(AlgebraExpressies.rb.getString("kopieerTekst"));
		copyItem.addActionListener(this);
		popup.add(copyItem);
		if (veldIsLeeg())
			copyItem.setEnabled(false);
		
		popup.addSeparator();
		
		//mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup2Label2"));
		JMenuItem mi = new JMenuItem(AlgebraExpressies.rb.getString("plakTekst"));
		mi.addActionListener(this);
		popup.add(mi);
		if ((AlgebraExpressies.clipBoard == null) || AlgebraExpressies.clipBoard.equals(""))
			mi.setEnabled(false);
		
		
		add(popup);
		
		layoutKnoppen();
	}

	public void layoutKnoppen()
	{	int currentY = 275;
		if (brugklas)
			currentY = 195;
		if (grafiekOptie)
		{	grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x, currentY);
			currentY += 30;
		}
		wisKnop.setLocation(wisKnop.getLocation().x, currentY);
		ip.setLocation(ip.getLocation().x, currentY + 30);
	}	
	
	public void disableElements(boolean b)
	{
//		terugKnop.setEnabled(!b);
//		tabelCheckbox.setEnabled(!b);
		ip.setEnabled(!b);
		grafiekCheckbox.setEnabled(!b);
		wisKnop.setEnabled(!b);
		kijkNaKnop.setEnabled(!b);
		frozen = b;
	}
	
	public boolean isEindUVS(UitvoerSchuifComponent uvs)
	{	if (uvs.isStapel)
			return false;
		if ((uvs.geefUitvoer(0) == null) && (uvs.geefVerborgenUitvoer(0) == null))
			return false;
		// dit gebeurt niet?
		if (uvs.pijlUit[0] == null)
			return true;
		boolean einde = true;
		for (int pCnt = 0; pCnt < uvs.pijlUit.length; pCnt++)
		{	if ((uvs.pijlUit[pCnt] != null) && uvs.pijlUit[pCnt].vast &&
				!(uvs.pijlUit[pCnt].ontvanger instanceof GrafiekComponent))
				einde = false;
			
		}
		return einde;
	}
	
	public Vector vindExpressieUVS()
	{	Vector result = new Vector();
		
		for (int sCnt = 0; sCnt < aantalSc; sCnt++)
		{
			if (schuifcomponenten[sCnt] instanceof UitvoerSchuifComponent)
			{	
	
				UitvoerSchuifComponent uvs = (UitvoerSchuifComponent) schuifcomponenten[sCnt];
				if (//(uvs.pijlIn1 != null) &&
					//!uvs.isStapel &&	
					//((uvs.pijlUit[0] == null) || 
					 //(!uvs.pijlUit[0].actief && (!uvs.pijlUit[0].vast || 
					  //(uvs.pijlUit[0].vast && (uvs.pijlUit[0].ontvanger instanceof GrafiekComponent)))))
					isEindUVS(uvs)	
				   ) 	
				{
//System.out.println("pijlen OK");

					result.addElement(uvs);
				}
				
			}
		}
	
		return result;
	}
	
	
	public boolean veldIsLeeg()
	{	int veldCnt = 0;
		for (int vCnt = 0; vCnt < aantalSc; vCnt++)
		{
			if (!schuifcomponenten[vCnt].isStapel && !(schuifcomponenten[vCnt] instanceof GrafiekComponent))
			{
				veldCnt++;
			}
		}
		
		return (veldCnt == 0);
	}
	
	public int getAantalVeldSc()
	{
		int veldCnt = 0;
		for (int vCnt = 0; vCnt < aantalSc; vCnt++)
		{
			if (!schuifcomponenten[vCnt].isStapel)
			{
				veldCnt++;
			}
		}
		
		return veldCnt;
	}
	public void maakVeldLeeg()
	{
		for (int vCnt = (aantalSc - 1); vCnt >= 0; vCnt--)
		{
			if (!schuifcomponenten[vCnt].isStapel)
				verwijder(schuifcomponenten[vCnt]);
		}
	}
	
	public void zetPlaatjes(Image gk, Image fk, Image kh)
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
		
		layoutKnoppen();
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
		
		layoutKnoppen();
		tekenOpnieuw();
		
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
	
	public void zetKijkNaActief(boolean b)
	{
		kijkNaKnop.setVisible(b);
		
	}
	
	public Hashtable getState()
	{	
		int aantalSc = 0;
		String[] classNames = null;
		List<String> classNamesList = new ArrayList<String>();
		int[] posX = null;
		int[] posY = null;
		List<Integer> posXList = new ArrayList<Integer>();
		List<Integer> posYList = new ArrayList<Integer>();
		Hashtable[] scStates = null;
		List<Map<String,Object>> scStatesList = new ArrayList<Map<String,Object>>();
		int[][] connections = null;
		int[] graphConnections = null;
		List<Integer> connectionsList = new ArrayList<Integer>();
		List<Integer> graphConnectionsList = new ArrayList<Integer>();
		boolean grafiek = false;
		boolean expressie = false;
		Hashtable zoomStateHolderState = null;

		aantalSc = this.aantalSc;
		classNames = new String[aantalSc];
		posX = new int[aantalSc];
		posY = new int[aantalSc];
		scStates = new Hashtable[aantalSc];
		for (int i = 0; i < aantalSc; i++)
	    {	classNames[i] = schuifcomponenten[i].getClass().getName();
	    	classNamesList.add(classNames[i]);
	    	posX[i] = schuifcomponenten[i].getLocation().x;
	    	posXList.add(new Integer(posX[i]));
	    	posY[i] = schuifcomponenten[i].getLocation().y;
	    	posYList.add(new Integer(posY[i]));
	    	scStates[i] = schuifcomponenten[i].getState();
	    	scStatesList.add(scStates[i]);
	    }
	    
		connections = new int[aantalSc][aantalSc];
		for (int i = 0; i < aantalSc; i++)
	    {	for (int j = 0; j < aantalSc; j++)
			{	int result = 0;
	    		if (schuifcomponenten[j].pijlIn1 != null && 
	    			schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i])
	    		{	connections[i][j] = 1;
	    			result = 1;
	    		}	
	    		if (schuifcomponenten[j].pijlIn2 != null && 
	    			schuifcomponenten[j].pijlIn2.zender == schuifcomponenten[i])
	    		{	connections[i][j] = 2;	
	    			result = 2;
	    		}	
	    		connectionsList.add(new Integer(result));	
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
			graphConnectionsList.add(new Integer(-1));
		}
		if (grafiek)
	    {	for (int i = 0; i < 10; i++)
			{	Pijl p = grafiekComponent.pijlenIn[i];
				for (int j = 0; j < aantalSc; j++)
		   		{	if (p != null && schuifcomponenten[j] == p.zender)
		   			{	graphConnections[i] = j;
		   				graphConnectionsList.set(i, new Integer(j));
		   			}
				}
			}
	    }
		
		Hashtable h = new Hashtable();
		
		
	    h.put("aantalSc", new Integer(aantalSc));
	    h.put("classNames", classNames);
	    h.put("classNamesList", classNamesList);
	    h.put("posX", posX);
	    h.put("posXList", posXList);
	    h.put("posY", posY);
	    h.put("posYList", posYList);
	    h.put("scStates", scStates);
	    h.put("scStatesList", scStatesList);
	    h.put("connections", connections);
	    h.put("connectionsList", connectionsList);
	    h.put("graphConnections", graphConnections);
	    h.put("graphConnectionsList", graphConnectionsList);
	    h.put("grafiek", new Boolean(grafiek));
	    h.put("expressie", new Boolean(expressie));
	    h.put("zoomStateHolderState", zoomStateHolderState);
	    
	    h.put("toolkit", new Boolean(toolkit));
	    h.put("alleenInvullen", new Boolean(alleenInvullen));
	    h.put("isDemo", new Boolean(isDemo));
	    
	    h.put("brugklas", new Boolean(brugklas));
	    h.put("grafiekOptie", new Boolean(grafiekOptie));
	    
	    h.put("zoomOptie", new Boolean(zoomOptie));
	    
	    return h;
	}
	
	public void copy()
	{
	//System.out.println("copy");		
		Hashtable h = getCopyTable();
		String s = StringCodeObject.encodeObjectToString(h);
		AlgebraExpressies.clipBoard = s;
			
	//if ((s != null) && !s.equals(""))
	//System.out.println("s not null");	
	}
		
	public Hashtable getCopyTable()
	{	int aantalSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		//boolean[][] connections = null;
		int[][] connections = null;
		int[] graphConnections = null;
		//	boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
		//	boolean links = false;
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
	    //h.put("tabel", new Boolean(tabel));
	    h.put("grafiek", new Boolean(grafiek));
	    h.put("expressie", new Boolean(expressie));
	    //h.put("links", new Boolean(links));
	    h.put("zoomStateHolderState", zoomStateHolderState);
		
		
		return h;
	}
	
	public void setEditModeState(Hashtable h)
	{	editmodeState = h;
		setState(h);
	}
		
    public void setState(Hashtable h)
    {	
    	
    	
//System.out.println("ASV setState");
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
		{	
//System.out.println("exception");

			return;
		}
		
		zoomStateHolder.setState(zoomStateHolderState);
		
		int n = this.aantalSc;
		for (int i = 0; i < n; i++)
		{	
//System.out.println("te verwijderen " + i);			
			verwijder(schuifcomponenten[0]);
//System.out.println("verwijderd " + i);		
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
		
//System.out.println("aantalSc = " + aantalSc);	    
	    for (int i = 0; i < aantalSc; i++)
	    {	for(int j = 0; j < aantalSc; j++)
			{	
//System.out.println("[" + i + "]["+ j + "] = " + connections[i][j]);	    	
	    		if (connections[i][j] == 1) 
				{	
//System.out.println("[" + i + "]["+ j + "] = 1");				
					Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
					schuifcomponenten[j].verbind(p, true);
					p.zetVerbonden(schuifcomponenten[j]);
				}
				if (connections[i][j] == 2) 
				{	
//System.out.println("[" + i + "]["+ j + "] = 2");					
					Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
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
	    
		copyItem.setEnabled(!veldIsLeeg());
		
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
	
    public void paste()
    {
    	if ((AlgebraExpressies.clipBoard != null) && !AlgebraExpressies.clipBoard.equals(""))
    	{
    		
//System.out.println("paste");    		
    		Object o = StringCodeObject.decodeStringToObject(AlgebraExpressies.clipBoard);
    		if (o == null)
    			return;
    		Hashtable h = (Hashtable) o;
    		setPasteTable(h);
//System.out.println("o not null");    		
    	}
    }
    
    public void setPasteTable(Hashtable h)
    {	
    	int aantalPasteSc = 0;
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
			aantalPasteSc = ((Integer) h.get("aantalSc")).intValue();
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

System.out.println("aantalPasteSc = " + aantalPasteSc);

/*		
		int n = this.aantalSc;
		for (int i = 0; i < n; i++)
		{	verwijder(schuifcomponenten[0]);
		}
*/		
		int aantalStapels = aantalSc;

System.out.println("aantalStapels = " + aantalStapels);
		
		//schuifcomponenten = new AlgebraSchuifComponent[200];
		for (int i = 0; i < aantalPasteSc; i++)
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
		      	   	schuifcomponenten[aantalStapels + i] = (AlgebraSchuifComponent) o;
		      	   	grafiekComponent = (GrafiekComponent) schuifcomponenten[aantalStapels + i];
		      	   	aantalSc++;
		      	   	
		      	}
		      	else
		      	{	Object o = cc.newInstance(
		      			new Object[] {this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte)});
		      		schuifcomponenten[aantalStapels + i] = (AlgebraSchuifComponent) o;
		      		aantalSc++;
		      	}
		    }
	      	catch(Exception e)
	      	{	
	      	}
	    }
	    
	    for (int i = 0; i < (aantalSc - aantalStapels); i++)
	    {	if (!(schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent)) 
	    	{	schuifcomponenten[aantalStapels + i].setState(scStates[i]);
	    		// bij een GrafiekComponent lukt dit niet omdat die bij setState de parent nodig heeft en die heeft ie nog niet
//IS DIT ZO?/	    			
	    	}
	    }
	    
		int max = aantalSc;
		for (int i = 0; i < (max - aantalStapels); i++)
		{	
			if (!(schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent))
			{	
				Pijl p = new Pijl(this);
				//if (schuifcomponenten[i].isStapel) 
				//{	schuifcomponenten[i].zetLinks(links);
				//	p.zetLinks(links);
				//}	
				schuifcomponenten[aantalStapels + i].voegPijlToe(p);
			}	
			add(schuifcomponenten[aantalStapels + i]);
		}
		
	    
	    for (int i = 0; i < (aantalSc - aantalStapels); i++)
	    {	for(int j = 0; j < (aantalSc - aantalStapels); j++)
			{	if (connections[i][j] == 1) 
				{	Pijl p = schuifcomponenten[aantalStapels + i].pijlUit[schuifcomponenten[aantalStapels + i].aantalPu - 1];
					schuifcomponenten[aantalStapels + j].verbind(p, true);
					p.zetVerbonden(schuifcomponenten[aantalStapels + j]);
				}
				if (connections[i][j] == 2) 
				{	Pijl p = schuifcomponenten[aantalStapels + i].pijlUit[schuifcomponenten[aantalStapels + i].aantalPu - 1];
					schuifcomponenten[aantalStapels + j].verbind(p, false);
					p.zetVerbonden(schuifcomponenten[aantalStapels + j]);
				}
			}
	    }
	    
	    
	    if (grafiek)
	    {  	for (int i = 0; i < 10; i++)
			{	if (graphConnections[i] != -1)
				{	Pijl p = schuifcomponenten[aantalStapels + graphConnections[i]].pijlUit[schuifcomponenten[aantalStapels + graphConnections[i]].aantalPu - 1];
					grafiekComponent.verbind(p, i);
					p.zetVerbonden(grafiekComponent);
				}
		    }
		}
		
		for (int i = 0; i < (aantalSc - aantalStapels); i++)
	    {	schuifcomponenten[aantalStapels + i].setState(scStates[i]);
	    }
	    
	    for (int i = 0; i < (aantalSc - aantalStapels); i++)
	    {	schuifcomponenten[aantalStapels + i].zetVeranderd(20);
	    	if (schuifcomponenten[aantalStapels + i] instanceof UitvoerSchuifComponent)
	    	{	((UitvoerSchuifComponent) schuifcomponenten[aantalStapels + i]).zetToonWaarde(!expressie);
//	    		((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(true);
	    		schuifcomponenten[aantalStapels + i].zetVeranderd(20);
	    	}
	    }
	    
	    for (int i = 0; i < (aantalSc - aantalStapels); i++)
	    {	if (schuifcomponenten[aantalStapels + i] instanceof GrafiekComponent)
	    	{	schuifcomponenten[aantalStapels + i].setState(scStates[i]);
	    		schuifcomponenten[aantalStapels + i].zetVeranderd(20);
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
    	
		for (int i = (aantalSc - 1); i >= aantalStapels; i--)
	    {	if (schuifcomponenten[i].isStapel)
	    		verwijder(schuifcomponenten[i]);
	    }

		tekenOpnieuw();
    }
    
	public void maakStapel()
	{	aantalSc = 0;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this, 30, 30, 40, 30); // was 55			
		aantalSc++;
		schuifcomponenten[aantalSc] = new OptelSchuifComponent(this, 7, 100, 40, 30); // was 130			
		aantalSc++;
		schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this, 54, 100, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this, 7, 140, 40, 30); // was 170	
		aantalSc++;
		schuifcomponenten[aantalSc] = new DeelSchuifComponent(this, 54, 140, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new KwadraatSchuifComponent(this, 7, 180, 40, 30); // was 210		
		aantalSc++;
		schuifcomponenten[aantalSc] = new WortelSchuifComponent(this, 54, 180, 40, 30);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new MachtSchuifComponent(this, 7, 220, 40, 30); // was 250			
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
			//g.setColor(Color.lightGray);
			g.setColor(new Color(210, 210, 210));
			g.fillRect(0, 0, 100, dd.height);
			//g.setColor(Color.black);
			g.setColor(Color.gray);
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
		g.setColor(Color.black);
		s = AlgebraExpressies.rb.getString("invoerVakLabel");
		lengte = fm.stringWidth(s);
		if (lengte > 100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize()-1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s, 50 - lengte / 2, 25); // was 50
		
		s = AlgebraExpressies.rb.getString("bewerkingenLabel");
		lengte = fm.stringWidth(s);
		if (lengte > 100) 
		{	g.setFont(new Font(g.getFont().getName(),Font.PLAIN, g.getFont().getSize() - 1));
			fm = g.getFontMetrics();
			lengte = fm.stringWidth(s);
		}
		g.drawString(s, 50 - lengte / 2, 95); // was 125
		
	}
/*	
	public void zetSchuiver(SchuifComponent sc)
	{	add(schuiflaag, 0);
		schuiflaag.add(sc, 0);
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
*/
/*	
	public void losSchuiver(SchuifComponent sc)
	{	add(sc, 0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i < asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
				add(asc.pijlUit[i], 0);
		}
		if (asc.pijlIn1 != null)
			add(asc.pijlIn1, 0);
		if (asc.pijlIn2 != null)
			add(asc.pijlIn2, 0);
		
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent) asc;
			for (int i = 0; i < gsc.aantalPijlenIn; i++)
			{	add(gsc.pijlenIn[i], 0);
			}
		}
		tekenOpnieuw();
	}
*/	
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
		
		copyItem.setEnabled(!veldIsLeeg());
		
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
						//if (verander)
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
				
				copyItem.setEnabled(!veldIsLeeg());
				
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
			{	grafiekComponent = new GrafiekComponent(this, getSize().width - 100, 200, 200, 230);
				schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
			else
			{	verwijder(grafiekComponent);
			}
			tekenOpnieuw();
		}
		else if ((e.getSource() instanceof JMenuItem) &&
				 ((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("kopieerTekst")))
		{	if (!veldIsLeeg())
			{
				copy();
			}
		}
		else if ((e.getSource() instanceof JMenuItem) &&
				 ((JMenuItem)e.getSource()).getText().equals(AlgebraExpressies.rb.getString("plakTekst")))
		{	if ((AlgebraExpressies.clipBoard != null) && !AlgebraExpressies.clipBoard.equals(""))
			{	maakVeldLeeg();  
			
				paste();
			}
		}	
	
		
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		start = true;
		if (e.getModifiers() == e.BUTTON3_MASK && e.getX() > 100)
		{	if (popup.isEnabled())
				popup.show(this, e.getX(), e.getY());
			
		}
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
