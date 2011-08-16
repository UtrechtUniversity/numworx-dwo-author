package fi.algebrapijlenopdr;

import java.awt.*;
import java.awt.event.*;
import fi.algebrapijlenopdr.schuifobjects.*;

import javax.swing.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.reflect.Constructor;

public class AlgebraSchuifVeld extends SchuifVeld 
							   implements ItemListener, MouseListener, MouseMotionListener, ActionListener
{	
	private JButton wisKnop, heenKnop, terugKnop;
	InvulPanel ip;
	JCheckBox grafiekCheckbox, tabelCheckbox;
	AlgebraSchuifComponent[] schuifcomponenten;
	GrafiekComponent grafiekComponent;
	int aantalSc;
	private boolean selecterenMogelijk;
	private boolean selecterenBezig;
	private boolean selectieGemaakt;
	private Rectangle clip;
	private JPopupMenu popup;
	//private ClipPaster clipPaster;
	private Frame clipFrame;
	boolean links = false;
	private Hashtable editmodeState;
	
	boolean fixed = false;
	private Panel hidePanel;
	
	public ZoomStateHolder zoomStateHolder;
	private Font font;
	
	//private Button kopieerKnop;
	private boolean buttonsAdded;
	
	boolean toolkit = true;
	boolean alleenInvullen = false;
	boolean isDemo = false;
	
	boolean brugklas = false;
	boolean terugHeen = true;
	boolean tabelOptie = true;
	boolean grafiekOptie = true;
	
	boolean scrollOptie = true;
	boolean zoomOptie = true; 
	
//	int origX;
//	int origBreedte;
	
	public AlgebraSchuifVeld(int x, int y, int b, int h)
	{	super(x, y, b, h);
	
//		origX = x;
//		origBreedte = b;
	
		addMouseListener(this);
		addMouseMotionListener(this);
		
		font = new Font("SansSerif", Font.PLAIN,12);
		if (AlgebraPijlenOpdr.rb.getLocale().toString().equals("en"))
			font = new Font("SansSerif", Font.PLAIN,11);
		
		zoomStateHolder = new ZoomStateHolder(this);
		
		ip = new InvulPanel(this, 8, 399, 90, 45);
		ip.setBackground(Color.lightGray);
		ip.setVisible(false);
		add(ip);
		
		grafiekCheckbox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("grafiekLabel"));
		grafiekCheckbox.addItemListener(this);
		grafiekCheckbox.setFont(font);
		grafiekCheckbox.setBounds(8, 340, 100, 20);
		//grafiekCheckbox.setBounds(8, 370, 100, 20);
		grafiekCheckbox.setBackground(Color.lightGray);
		add(grafiekCheckbox, 0);
		
		tabelCheckbox = new JCheckBox(AlgebraPijlenOpdr.rb.getString("tabelLabel"));
		tabelCheckbox.addItemListener(this);
		tabelCheckbox.setFont(font);
		tabelCheckbox.setBounds(8, 320, 80, 20);
		//tabelCheckbox.setBounds(8, 350, 80, 20);
		tabelCheckbox.setBackground(Color.lightGray);
		add(tabelCheckbox, 0);
		
		wisKnop = new JButton(AlgebraPijlenOpdr.rb.getString("wisKnopLabel"));
		wisKnop.setFont(font);
		wisKnop.setBounds(18, 370, 70, 20);
		//wisKnop.setBounds(18, 400, 70, 20);
		wisKnop.addActionListener(this);
		add(wisKnop,0);
		
		terugKnop = new JButton(AlgebraPijlenOpdr.rb.getString("terugKnopLabel"));
		terugKnop.setBounds(10, 290, 86, 20);
		terugKnop.setFont(font);
		terugKnop.addActionListener(this);
		add(terugKnop, 0);
		
		heenKnop = new JButton(AlgebraPijlenOpdr.rb.getString("heenKnopLabel"));
		//heenKnop.setBounds(58, 290, 45, 20);
		heenKnop.setBounds(10, 320, 86, 20);
		heenKnop.setFont(font);
		heenKnop.addActionListener(this);
		//add(heenKnop, 0);
		
		add(schuiflaag, 0);
		
		maakStapel();
		
		grafiekComponent = new GrafiekComponent(this, 500, 200, 200, 130);
		grafiekComponent.isStapel = false;
		
		popup = new JPopupMenu();
					
		JMenuItem mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup2Label1"));
		mi.addActionListener(this);
		popup.add(mi);
		
		popup.addSeparator();
		
		mi = new JMenuItem(AlgebraPijlenOpdr.rb.getString("popup2Label2"));
		mi.addActionListener(this);
		popup.add(mi);
		add(popup);
		popup.setEnabled(false);
		
		hidePanel = new Panel();
		hidePanel.setBackground(Color.white);
		hidePanel.setBounds(1, 1, 110, h - 2);
		hidePanel.setVisible(false);
		add(hidePanel, 0);
		
		//kopieerKnop = new Button("Kopieer vorige opdracht");
		//kopieerKnop.setBounds(120,370,160,20);
		//kopieerKnop.addActionListener(this);
		//add(kopieerKnop);
	}
	
	public void setFixed(boolean b)
	{	fixed = b;
		hidePanel.setVisible(b);
		terugKnop.setVisible(!b);
		heenKnop.setVisible(!b);
		wisKnop.setVisible(!b);
		tabelCheckbox.setVisible(!b);
		grafiekCheckbox.setVisible(!b);
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

/*			
			if (!buttonsAdded)
			{	grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x + getLocation().x , 
											grafiekCheckbox.getLocation().y + getLocation().y);
				tabelCheckbox.setLocation(tabelCheckbox.getLocation().x + getLocation().x , 
										  tabelCheckbox.getLocation().y + getLocation().y);
				wisKnop.setLocation(wisKnop.getLocation().x + getLocation().x , wisKnop.getLocation().y + getLocation().y);
				terugKnop.setLocation(terugKnop.getLocation().x + getLocation().x , terugKnop.getLocation().y + getLocation().y);
				heenKnop.setLocation(heenKnop.getLocation().x + getLocation().x , heenKnop.getLocation().y + getLocation().y);
				ip.setLocation(ip.getLocation().x + getLocation().x , ip.getLocation().y + getLocation().y);
				
				getParent().add(grafiekCheckbox, 0);
				getParent().add(tabelCheckbox, 0);
				getParent().add(wisKnop, 0);
				getParent().add(terugKnop, 0);
				getParent().add(heenKnop, 0);
				getParent().add(ip, 0);
				buttonsAdded = true;
			}
*/			
			terugKnop.setVisible(terugHeen);
			heenKnop.setVisible(terugHeen);
			grafiekCheckbox.setVisible(grafiekOptie);
			tabelCheckbox.setVisible(tabelOptie);
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
			
			terugKnop.setVisible(false);
			heenKnop.setVisible(false);
			grafiekCheckbox.setVisible(false);
			tabelCheckbox.setVisible(false);
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

			
			terugKnop.setVisible(false);
			heenKnop.setVisible(false);
			grafiekCheckbox.setVisible(false);
			tabelCheckbox.setVisible(false);
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
				((schuifcomponenten[i] instanceof OmkeringSchuifComponent) ||
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
	
	public void zetTerugHeen(boolean b)
	{
		terugHeen = b;
		terugKnop.setVisible(b);
		heenKnop.setVisible(b);
		
	}
	
	public void zetTabelOptie(boolean b)
	{
		tabelOptie = b;
		tabelCheckbox.setVisible(b);
	}

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
	{	int aantalSc = 0;
		String[] classNames = null;
		int[] posX = null;
		int[] posY = null;
		Hashtable[] scStates = null;
		boolean[][] connections = null;
		int[] graphConnections = null;
		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
		boolean links = false;
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
		connections = new boolean[aantalSc][aantalSc];
		for (int i = 0; i < aantalSc; i++)
	    {	for (int j = 0; j < aantalSc; j++)
			{	connections[i][j] = schuifcomponenten[j].pijlIn1 != null && 
	    							schuifcomponenten[j].pijlIn1.zender == schuifcomponenten[i];
			}
	    }
	    
//	    tabel = tabelCheckbox.getState();
		tabel = tabelCheckbox.isSelected();
//	    grafiek = grafiekCheckbox.getState();
		grafiek = grafiekCheckbox.isSelected();
		
	    expressie = ip.isExpr();
	    links = this.links;
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
	    h.put("tabel", new Boolean(tabel));
	    h.put("grafiek", new Boolean(grafiek));
	    h.put("expressie", new Boolean(expressie));
	    h.put("links", new Boolean(links));
	    h.put("zoomStateHolderState", zoomStateHolderState);
	    
	    h.put("toolkit", new Boolean(toolkit));
	    h.put("alleenInvullen", new Boolean(alleenInvullen));
	    h.put("isDemo", new Boolean(isDemo));
	    
	    h.put("brugklas", new Boolean(brugklas));
	    h.put("terugHeen", new Boolean(terugHeen));
	    h.put("tabelOptie", new Boolean(tabelOptie));
	    h.put("grafiekOptie", new Boolean(grafiekOptie));
	    
	    h.put("scrollOptie", new Boolean(scrollOptie));
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
		boolean[][] connections = null;
		int[] graphConnections = null;
		boolean tabel = false;
		boolean grafiek = false;
		boolean expressie = false;
		boolean links = false;
		Hashtable zoomStateHolderState = null;
		
		try
		{
			aantalSc = ((Integer) h.get("aantalSc")).intValue();
			classNames = (String[]) h.get("classNames");
			posX = (int[]) h.get("posX");
			posY = (int[]) h.get("posY");
			scStates = (Hashtable[]) h.get("scStates");
			connections = (boolean[][]) h.get("connections");
			graphConnections = (int[]) h.get("graphConnections");
			tabel = ((Boolean) h.get("tabel")).booleanValue();
			grafiek = ((Boolean) h.get("grafiek")).booleanValue();
			expressie = ((Boolean) h.get("expressie")).booleanValue();
			links = ((Boolean) h.get("links")).booleanValue();
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
		      	int breedte = 50;
		      	int hoogte = 20;
		      	if (classNames[i].equals("fi.algebrapijlenopdr.GrafiekComponent"))
		      	{	breedte = 210;
		      		hoogte = 220;
		      		Object o = cc.newInstance(
		      			new Object[] {this, new Integer(posX[i]), new Integer(posY[i]), new Integer(breedte), new Integer(hoogte)});
		      	   	schuifcomponenten[i] = (AlgebraSchuifComponent) o;
		      	   	grafiekComponent = (GrafiekComponent)schuifcomponenten[i];
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
	    		schuifcomponenten[i].setState(scStates[i]);
	    	// bij een GrafiekComponent lukt dit niet omdat die bij setState de parent nodig heeft en die heeft ie nog niet
	    }
	    
		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{	Pijl p = new Pijl(this);
			if (schuifcomponenten[i].isStapel) 
			{	schuifcomponenten[i].zetLinks(links);
				p.zetLinks(links);
			}	
			schuifcomponenten[i].voegPijlToe(p);
			add(schuifcomponenten[i]);
		}
		
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	for(int j = 0; j < aantalSc; j++)
			{	if (connections[i][j]) 
				{	Pijl p = schuifcomponenten[i].pijlUit[schuifcomponenten[i].aantalPu - 1];
					schuifcomponenten[j].verbind(p);
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
	    		((UitvoerSchuifComponent) schuifcomponenten[i]).zetScroll(true);
	    		schuifcomponenten[i].zetVeranderd(20);
	    	}
	    }
	    
	    for (int i = 0; i < aantalSc; i++)
	    {	if (schuifcomponenten[i] instanceof GrafiekComponent)
	    	{	schuifcomponenten[i].setState(scStates[i]);
	    		schuifcomponenten[i].zetVeranderd(20);
	    	}
	    }
	    
//	    tabelCheckbox.setState(tabel);
//	    grafiekCheckbox.setState(grafiek);
	    
	    tabelCheckbox.setSelected(tabel);
	    grafiekCheckbox.setSelected(grafiek);	    
	    
	    //ip.zetExpressie(expressie); // deze optie niet aanwezig
	    this.links = links;
	    if (links)
		{	for (int i = 0; i < aantalSc; i++)
			{	if (schuifcomponenten[i].isStapel)
				{	//schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x+10, schuifcomponenten[i].getLocation().y);
					schuifcomponenten[i].zetLinks(true);
				}
			}
		}
	    
	    
	    Enumeration en = zoomStateHolder.keys();
		while(en.hasMoreElements())
		{	String key = (String) en.nextElement();
			setZoomStates(key, zoomStateHolder.getZoomState(key));
		}
/*		
		if (!buttonsAdded && toolkit)
		{	grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x + getLocation().x , 
										grafiekCheckbox.getLocation().y + getLocation().y);
			tabelCheckbox.setLocation(tabelCheckbox.getLocation().x + getLocation().x , 
									  tabelCheckbox.getLocation().y + getLocation().y);
			wisKnop.setLocation(wisKnop.getLocation().x + getLocation().x , wisKnop.getLocation().y + getLocation().y);
			terugKnop.setLocation(terugKnop.getLocation().x + getLocation().x , terugKnop.getLocation().y + getLocation().y);
			heenKnop.setLocation(heenKnop.getLocation().x + getLocation().x , heenKnop.getLocation().y + getLocation().y);
			ip.setLocation(ip.getLocation().x + getLocation().x , ip.getLocation().y + getLocation().y);
			
			getParent().add(grafiekCheckbox, 0);
			getParent().add(tabelCheckbox, 0);
			getParent().add(wisKnop, 0);
			getParent().add(terugKnop, 0);
			getParent().add(heenKnop, 0);
			getParent().add(ip, 0);
			buttonsAdded = true;
		}
*/		
//		boolean toolkit = true;
//		if (h.containsKey("toolkit"))
//			toolkit = ((Boolean) h.get("toolkit")).booleanValue();
//		this.toolkit = toolkit;
		
		zetToolkit(toolkit);
//System.out.println("toolkit " + toolkit);		
		
//		boolean alleenInvullen = false;
//		if (h.containsKey("alleenInvullen"))
//			alleenInvullen = ((Boolean) h.get("alleenInvullen")).booleanValue();
		//this.alleenInvullen = alleenInvullen;
		
		zetAlleenInvullen(alleenInvullen);
//System.out.println("alleenInvullen " + alleenInvullen);

//		boolean isDemo = false;
//		if (h.containsKey("isDemo"))
//			isDemo = ((Boolean) h.get("isDemo")).booleanValue();
		//this.isDemo = isDemo;
		
		zetIsDemo(isDemo);
//System.out.println("isDemo " + isDemo);		

//System.out.println("b = " + getSize().width);
//System.out.println("ob = " + origBreedte);

		boolean brugklas = false;
		if (h.containsKey("brugklas"))
			brugklas = ((Boolean) h.get("brugklas")).booleanValue();
		zetBrugklas(brugklas);
		
		boolean terugHeen = true;
		if (h.containsKey("terugHeen"))
			terugHeen = ((Boolean) h.get("terugHeen")).booleanValue();
		zetTerugHeen(terugHeen);
		
		boolean tabelOptie = true;
		if (h.containsKey("tabelOptie"))
			tabelOptie = ((Boolean) h.get("tabelOptie")).booleanValue();
		zetTabelOptie(tabelOptie);
		
		boolean grafiekOptie = true;
		if (h.containsKey("grafiekOptie"))
			grafiekOptie = ((Boolean) h.get("grafiekOptie")).booleanValue();
		zetGrafiekOptie(grafiekOptie);

		boolean scrollOptie = true;
		if (h.containsKey("scrollOptie"))
			scrollOptie = ((Boolean) h.get("scrollOptie")).booleanValue();
		zetScrollOptie(scrollOptie);
		
		boolean zoomOptie = true;
		if (h.containsKey("zoomOptie"))
			zoomOptie = ((Boolean) h.get("zoomOptie")).booleanValue();
		zetZoomOptie(zoomOptie);
		
//System.out.println("setState end");		
//System.out.println("b = " + getSize().width);
		
		tekenOpnieuw();
			
    }
	
	public void paint(Graphics g)
	{	super.paint(g);
		if ((selecterenBezig || selectieGemaakt) && clip != null)
		{	int b = clip.getSize().width;
			int h = clip.getSize().height;
			int x = clip.getLocation().x;
			int y = clip.getLocation().y;
			g.setColor(Color.red);
			g.drawRect(x,y,b,h);
		}
	}
	
	public void maakStapel()
	{	aantalSc = 0;
		int b = 50;
		int h = 20;
		schuifcomponenten = new AlgebraSchuifComponent[200];
		schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this, 20, 45, b, h);
//		((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.getState());
		((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.isSelected());
		((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetScroll(true);
		aantalSc++;
		schuifcomponenten[aantalSc] = new OptelSchuifComponent(this, 20, 110, b, h);
		aantalSc++;
		schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this, 20, 135, b, h);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this, 20, 160, b, h);	
		aantalSc++;
		schuifcomponenten[aantalSc] = new DeelSchuifComponent(this, 20, 185, b, h);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new OmkeringSchuifComponent(this, 20, 210, b, h);		
		aantalSc++;
		schuifcomponenten[aantalSc] = new WortelSchuifComponent(this, 20, 235, b, h);			
		aantalSc++;
		schuifcomponenten[aantalSc] = new MachtSchuifComponent(this, 20, 260, b, h);			
		aantalSc++;
		
		int max = aantalSc;
		for (int i = 0; i < max; i++)
		{	schuifcomponenten[i].zetLinks(links);
			Pijl p = new Pijl(this);
			p.zetLinks(links);
			schuifcomponenten[i].voegPijlToe(p);
			add(schuifcomponenten[i]);
		}

/*		
		add(grafiekCheckbox, 0);
		add(tabelCheckbox, 0);
		add(wisKnop, 0);
		add(terugKnop, 0);
		add(heenKnop, 0);
*/		
	}
	
	public void tekenAchtergrond(Graphics g)
	{	Dimension dd = getSize();				
		g.setColor(Color.white);
//g.setColor(Color.orange);		
		g.fillRect(0, 0, dd.width, dd.height);
		if (fixed)
		{	
//System.out.println("fixed");			
			g.setColor(getParent().getBackground());//(Color.white);
			g.fillRect(0, 0, dd.width, dd.height);
			hidePanel.setBackground(getParent().getParent().getBackground());
			return;
		}
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, 110, dd.height);
		g.setColor(Color.black);
		g.drawLine(110, 0, 110, dd.height - 1);
		g.drawRect(0, 0, dd.width - 1, dd.height - 1);
//System.out.println("achtergrond w = " + dd.width);		
		
		//g.drawString(Integer.toString(aantalSc),20,10);
		//g.drawString(Integer.toString(schuiflaag.getComponentCount()),50,10);
		//g.drawString(Integer.toString(getComponentCount()),80,10);
		
		//int intveranderd=0,intresized=0;
		//if(veranderd)intveranderd=1;
		//if(resized)intresized=1;
		///g.drawString(Integer.toString(intveranderd),80,25);
		//g.drawString(Integer.toString(intresized),80,40);
		
		//g.drawString("Invoer",35,25);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		String s = AlgebraPijlenOpdr.rb.getString("invoerVakLabel");
		int lengte = fm.stringWidth(s);
		g.drawString(s, 55 - lengte / 2, 35);
		
		s = AlgebraPijlenOpdr.rb.getString("bewerkingenLabel");
		lengte = fm.stringWidth(s);
		g.drawString(s, 55 - lengte / 2, 100);
	}
	
	public void zetSchuiver(SchuifComponent sc)
	{	schuiflaag.add(sc, 0);
		AlgebraSchuifComponent asc = (AlgebraSchuifComponent) sc;
		for (int i = 0; i <asc.aantalPu; i++)
		{	if (asc.pijlUit[i] != null)
				schuiflaag.add(asc.pijlUit[i]);
		}
		if (asc.pijlIn1 != null)
			schuiflaag.add(asc.pijlIn1);
		if (asc.pijlIn2 != null)
			schuiflaag.add(asc.pijlIn2);
		if (asc instanceof GrafiekComponent)
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
			for (int i = 0; i < gsc.aantalPijlenIn; i++)
			{	schuiflaag.add(gsc.pijlenIn[i]);
			}
		}
		tekenOpnieuw();
	}
	
	public void losSchuiver(SchuifComponent sc)
	{	add(sc);//, 0);
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
		{	GrafiekComponent gsc = (GrafiekComponent)asc;
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
		{	schuifcomponenten[aantalSc] = new UitvoerSchuifComponent(this ,x, y, b, h);
		  	//((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.getState());
		  	((UitvoerSchuifComponent) schuifcomponenten[aantalSc]).zetTabelAan(tabelCheckbox.isSelected());
		}
		else if (asc instanceof OptelSchuifComponent)
		{	schuifcomponenten[aantalSc] = new OptelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof AftrekSchuifComponent)
		{	schuifcomponenten[aantalSc] = new AftrekSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof VermenigvuldigSchuifComponent)
		{	schuifcomponenten[aantalSc] = new VermenigvuldigSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof DeelSchuifComponent)
		{	schuifcomponenten[aantalSc] = new DeelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof OmkeringSchuifComponent)
		{	schuifcomponenten[aantalSc] = new OmkeringSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof WortelSchuifComponent)
		{	schuifcomponenten[aantalSc] = new WortelSchuifComponent(this ,x, y, b, h);
		}
		else if (asc instanceof MachtSchuifComponent)
		{	schuifcomponenten[aantalSc] = new MachtSchuifComponent(this ,x, y, b, h);
		}
		else 
			return;
		schuifcomponenten[aantalSc].zetLinks(links);
		Pijl p = new Pijl(this);
		p.zetLinks(links);
		schuifcomponenten[aantalSc].voegPijlToe(p);
		add(schuifcomponenten[aantalSc]);
		aantalSc++;

/*		
		if (!buttonsAdded)
		{	grafiekCheckbox.setLocation(grafiekCheckbox.getLocation().x + getLocation().x , 
				                        grafiekCheckbox.getLocation().y + getLocation().y);
			tabelCheckbox.setLocation(tabelCheckbox.getLocation().x + getLocation().x , tabelCheckbox.getLocation().y + getLocation().y);
			wisKnop.setLocation(wisKnop.getLocation().x + getLocation().x , wisKnop.getLocation().y + getLocation().y);
			terugKnop.setLocation(terugKnop.getLocation().x + getLocation().x , terugKnop.getLocation().y + getLocation().y);
			heenKnop.setLocation(heenKnop.getLocation().x + getLocation().x , heenKnop.getLocation().y + getLocation().y);
			ip.setLocation(ip.getLocation().x + getLocation().x , ip.getLocation().y + getLocation().y);
			
//System.out.println("wis x = " + wisKnop.getLocation().x);			
			
			getParent().add(grafiekCheckbox,0);
			getParent().add(tabelCheckbox,0);
			getParent().add(wisKnop,0);
			getParent().add(terugKnop,0);
			getParent().add(heenKnop,0);
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
	{	for (int i = 0; i < aantalSc; i++)
		{	if (schuifcomponenten[i]instanceof UitvoerSchuifComponent)
			{	boolean b = !ip.isExpr();
				((UitvoerSchuifComponent) schuifcomponenten[i]).zetToonWaarde(b);
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
//		origBreedte = b;
//System.out.println("setSize w = " + b + " h = " + h);		
	}
	
	public void zetTabellen(int beginwaarde, int selectnummer, String varN, double schaalFactorX)
	{	/*for(int i=0 ; i<aantalSc ; i++)
			{	if(schuifcomponenten[i]instanceof UitvoerSchuifComponent)
				{	((UitvoerSchuifComponent)schuifcomponenten[i]).zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
				}
				if(schuifcomponenten[i]instanceof GrafiekComponent)
				{	((GrafiekComponent)schuifcomponenten[i]).zetTabel(beginwaarde, selectnummer, varN, schaalFactorX);
				}
			}*/
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
		{	if (editmodeState == null)
			{	links = false;
				int n = aantalSc;
				for (int i = 0; i < n; i++)
				{	verwijder(schuifcomponenten[0]);
				}
/*				
				grafiekCheckbox.setLocation(8, 340);
				tabelCheckbox.setLocation(8, 320);
				wisKnop.setLocation(18, 370);
				terugKnop.setLocation(8, 290);
				heenKnop.setLocation(58, 290);
*/				
/*				
				grafiekCheckbox.setLocation(8, 370);
				tabelCheckbox.setLocation(8, 350);
				wisKnop.setLocation(18, 400);
				terugKnop.setLocation(18, 290);
				heenKnop.setLocation(18, 320);				
*/				
				maakStapel();
//				if (grafiekCheckbox.getState())
				if (grafiekCheckbox.isSelected())
				{	schuifcomponenten[aantalSc] = grafiekComponent;
					aantalSc++;
					add(grafiekComponent);
				}
				zoomStateHolder = new ZoomStateHolder(this);
			}
			else
			{	setState(editmodeState);
			}
		}
		else if (e.getSource() == terugKnop)
		{	if (!links)
			{	links = true;
				for (int i = 0; i < aantalSc; i++)
				{	if (schuifcomponenten[i].isStapel)
					{	schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x + 10, 
														 schuifcomponenten[i].getLocation().y);
						schuifcomponenten[i].zetLinks(true);
					}
				}
				terugKnop.setText(AlgebraPijlenOpdr.rb.getString("heenKnopLabel"));
				tekenOpnieuw();
			}
		
			else // links
			{
				links = false;
				for (int i = 0; i < aantalSc; i++)
				{	if (schuifcomponenten[i].isStapel)
					{	schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x - 10, 
														 schuifcomponenten[i].getLocation().y);
						schuifcomponenten[i].zetLinks(false);
					}
				}
				terugKnop.setText(AlgebraPijlenOpdr.rb.getString("terugKnopLabel"));
				tekenOpnieuw();
			}
			
		}
/*	
		else if (e.getSource() == heenKnop)
		{	if (links)
			{	links = false;
				for (int i = 0; i < aantalSc; i++)
				{	if (schuifcomponenten[i].isStapel)
					{	schuifcomponenten[i].setLocation(schuifcomponenten[i].getLocation().x - 10, 
														 schuifcomponenten[i].getLocation().y);
						schuifcomponenten[i].zetLinks(false);
					}
				}
				tekenOpnieuw();
			}
		}
*/		
		else if (((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup2Label1")))
		{	selecterenMogelijk = true;
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR ));
		}
		else if(((MenuItem)e.getSource()).getLabel().equals(AlgebraPijlenOpdr.rb.getString("popup2Label2")))
		{	//if(clipFrame!=null)clipFrame.dispose();
			//clipFrame = new Frame("clip");
			//clipFrame.setLayout(new FlowLayout());
			//ClipCanvas cv = new ClipCanvas(geefImage());
			//cv.zetClip(clip);
			//clipFrame.add(cv);
			//clipFrame.pack();
			//clipFrame.show();
			//clipFrame.setSize(0,0);
			//requestFocus();
			//clipPaster = new ClipPaster(clipFrame);
			//ClipPaster.pasteToClipboard(clipPaster);
			//selectieGemaakt = false;
			//tekenOpnieuw();
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{	if (e.getSource() == grafiekCheckbox)
		{	//boolean b = grafiekCheckbox.getState();
			boolean b = grafiekCheckbox.isSelected();
			if (b)
			{	grafiekComponent = new GrafiekComponent(this, 500, 200, 210, 220);
				schuifcomponenten[aantalSc] = grafiekComponent;
				aantalSc++;
				add(grafiekComponent);
			}
			else
			{	verwijder(grafiekComponent);
			}
			tekenOpnieuw();
		}
		else if (e.getSource() == tabelCheckbox)
		{	//boolean b = tabelCheckbox.getState();
			boolean b = tabelCheckbox.isSelected();
			for (int i = 0; i < aantalSc; i++)
			{	if (schuifcomponenten[i] instanceof UitvoerSchuifComponent)
				{	((UitvoerSchuifComponent) schuifcomponenten[i]).zetTabelAan(b);
				}
			}
			grafiekComponent.zetVeranderd(20);
			tekenOpnieuw();
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	requestFocus();
		if (e.getModifiers() == e.BUTTON3_MASK && e.getX() > 100)
		{	//if(popup.isEnabled())popup.show(this,e.getX(),e.getY());
			
		}
		else if (selecterenMogelijk)
		{	selecterenBezig = true;
			selecterenMogelijk = false;
			clip = new Rectangle(e.getX(),e.getY(), 0, 0);
		}
		else if (selectieGemaakt && !clip.contains(e.getX(), e.getY()))
		{	selectieGemaakt = false;
			tekenOpnieuw();
		}
	}	
	public void mouseDragged(MouseEvent e)
	{	if (selecterenBezig)
		{	int b = clip.getSize().width;
			int h = clip.getSize().height;
			int x = clip.getLocation().x;
			int y = clip.getLocation().y;
			if (e.getX() - x > 0 && e.getY() - y > 0)
			{	clip = new Rectangle(x, y, e.getX() - x, e.getY() - y);
				this.tekenOpnieuw();
			}
		}
	}
	public void mouseReleased(MouseEvent e)
	{	if (selecterenBezig)
		{	selectieGemaakt = true;
			selecterenBezig = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR ));
		}
	}
	public void mouseMoved(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}
	public void mouseClicked(MouseEvent e)
	{}
	public void mouseEntered(MouseEvent e)
	{}
}
