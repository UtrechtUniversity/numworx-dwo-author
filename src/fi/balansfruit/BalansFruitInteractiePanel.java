package fi.balansfruit;

import java.awt.*;
import java.applet.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import fi.beans.lwmobjects_swing.LWMBufferPanel;
import fi.beans.lwmobjects_swing.LWMButton;
import fi.beans.lwmobjects_swing.LWMComponent;
import fi.beans.lwmobjects_swing.LWMContainer;
import fi.beans.lwmobjects_swing.LWMMouseHandler;
import fi.beans.lwmobjects_swing.MovePermissions;
import fi.beans.wiskopdrbeans.*;
import fi.beans.appletutil.*;
import fi.beans.stringutils.StringUtils;

import javax.swing.*;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;


public class BalansFruitInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel, ActionListener, 
																  MouseListener,CBookAware
{
	
	public static int MAXAANTAL = 20;			// max aantal per soort
	
	public static int OFFSETY = 40;			// verticale offset van balansplaatje
	public static int EVENWICHTY = 151;		// hoogte van SchaalContainer in evenwicht
	public static int VERSCHIL = 30;			// absolute verschil in hoogte wanneer uit evenwicht
									// wordt bepaald door de balans-plaatjes
	public static int VOORRAADY = 260;		// offset + hoogte plaatje
	public static int TOTAALHOOG = 360;
	public static int TOTAALBREED = 500;
	public static int SCHAALLINKSX = 23;		// x-pos linker schaalcontainer (y=0)
	public static int SCHAALRECHTSX = 304;	// idem rechts
	public static int SCHAALBREED = 174;
	public static Color ACHTERGROND = new Color(255, 255, 255);	// rekenweb-geel
	
	// Componenten
	private LWMBufferPanel hetBlad;
	protected FruitObject[] fruitObjects = new FruitObject[100];
	protected int aantalFruitObjects;
	protected BalansFruit main;
	protected SchaalContainer links;
	protected SchaalContainer rechts;
	protected LWMContainer voorraad;
	private LWMButton schoon;
	
	private LWMMouseHandler mouseHandler;
	
	MovePermissions mp;					// movePermissions for fruit only
		
	// Images
	private int aantalSoorten = 8;
	private int[] aantalStuks;
	private Image[] fruitplaatje;
	private double[] gewichten;
	private Image balansLinks;
	private Image balansEvenwicht;
	private Image balansRechts;
	private Image wisknopImage;
	
	private Applet applet;
	protected Hashtable defaultParamValues, launchData;
	
	private boolean fixedOptie=false;
	private boolean bewaarOptie=false;
	private boolean resetOptie=false;
	private boolean viewEquation=false;
	private boolean showEenheden = true;
	
	//private JPanel afdekPanel;
	private JButton resetButton;
	private JTextField textArea; 
	private JLabel vergelijkingLabel;
	private JLabel messageLabel;
	
	private String huidigeVergelijking = "0=0";
	
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	boolean resetting = false;
	boolean editMode = false;
	
	protected String[] imageNames = {
			"ananas.gif",
			"appel.gif",
			"banaan.gif",
			"citroen.gif",
			"peer.gif",
			"perzik.gif",
			"sinaasappel.gif",
			"tomaat.gif",
			"x.gif",
			"y.gif",
			"1.gif",
			"1g.gif",
			"5g.gif",
			"10g.gif",
			"50g.gif",
			"100g.gif",
			"500g.gif",
			"1leeg.gif",
			"balanslinks.gif",
			"balansgoed.gif",
			"balansrechts.gif",
			"reseticon.gif"};
	
	protected static Hashtable images;
	
	public BalansFruitInteractiePanel()
	{
		//if(images==null)
		//{	
		images = new Hashtable();
		BalansFruitApplet.loadImages(images,imageNames);
		//}
		
		defaultParamValues = makeDefaultParamValues(0);
		launchData = defaultParamValues;
		
		loadParameters();

		// Opbouw applet met Panel
		setLayout(null);					// Geen layout-manager
		hetBlad = new LWMBufferPanel(TOTAALBREED, TOTAALHOOG);
		add(hetBlad);
		hetBlad.setBounds(0, 0, TOTAALBREED, TOTAALHOOG);
		
		// de Containers maken
		voorraad = new LWMContainer(new Color(225, 208, 127), TOTAALBREED, 90);
		voorraad.setKeepInside(true);
		voorraad.setMovable(false);
		voorraad.setGravity(LWMContainer.GOSOUTH);
		
		links = new SchaalContainer(SCHAALBREED, EVENWICHTY);
			// NB: setMovable(false), keepInside(true), gravity(GOSOUTH) zit in constructor
		rechts = new SchaalContainer(SCHAALBREED, EVENWICHTY);
		
		main = new BalansFruit(links, rechts, voorraad, TOTAALBREED, TOTAALHOOG);
		main.initRoot();	
		
		// De containers plaatsen
		main.setBounds(0, 0, TOTAALBREED, TOTAALHOOG);
		hetBlad.add(main);
		main.addLWMComponent(voorraad, 0, VOORRAADY);		
		main.addLWMComponent(links, SCHAALLINKSX, 0);
		main.addLWMComponent(rechts, SCHAALRECHTSX, 0);
		main.addActionListener(this);
		
		// de schoon-knop
		//schoon = new LWMButton(getImage("minibalansgoed"), "", 50, 25);
		//schoon.addActionListener(main);
		//main.addLWMComponent(schoon, 225, 10);
		
		mouseHandler = new LWMMouseHandler(0, 0, TOTAALBREED, TOTAALHOOG);
		add(mouseHandler);
		mouseHandler.addLWMContainer(voorraad);
		mouseHandler.addLWMContainer(links);
		mouseHandler.addLWMContainer(rechts);
		
		// MovePermissions-object maken voor FruitObjecten: mogen naar links, rechts en voorraad
		mp = new MovePermissions();
		mp.addPermission(voorraad);
		mp.addPermission(links);
		mp.addPermission(rechts);
			
		balansLinks = getImage("balanslinks.gif");
		balansEvenwicht = getImage("balansgoed.gif");
		balansRechts = getImage("balansrechts.gif");
		//wisknopImage = getImage("minibalansgoed.gif");
		
		// load images, add fruitobjects
		//Thread t = new Thread(this);
		//t.start();
		
		setSize(TOTAALBREED, TOTAALHOOG); 
		setVisible(true);
		
		//afdekPanel = new JPanel();
		//afdekPanel.setBounds(0,0, TOTAALBREED, TOTAALHOOG);
		//afdekPanel.setOpaque(false);
		//afdekPanel.addMouseListener(this);
		//afdekPanel.setVisible(false);
		
		resetButton = new JButton();
		resetButton.setIcon(new ImageIcon(getImage("reseticon.gif")));
		resetButton.setBounds(2,2,20,20);
		resetButton.addActionListener(this);
		
		textArea = new JTextField();
		textArea.setBounds(0,0,200,20);
		//add(textArea,0);
		
		vergelijkingLabel = new JLabel("",JLabel.CENTER);
		vergelijkingLabel.setBounds(0,0,500,20);
		vergelijkingLabel.setAlignmentX(CENTER_ALIGNMENT);
		vergelijkingLabel.setFont(new Font("SansSerif", Font.PLAIN,18));
		add(vergelijkingLabel,0);
		vergelijkingLabel.setVisible(false);
		
		messageLabel = new JLabel("",JLabel.CENTER);
		messageLabel.setBounds(0,20,500,20);
		messageLabel.setAlignmentX(CENTER_ALIGNMENT);
		messageLabel.setFont(new Font("SansSerif", Font.PLAIN,18));
		add(messageLabel,0);

		
	}
	
	
	public static Image getImage(String name)
	{	return(Image)images.get(name);
	}
	
	protected void loadParameters()
	{	aantalSoorten = imageNames.length-4;

		fruitplaatje = new Image[aantalSoorten];
		aantalStuks = new int[aantalSoorten];
		gewichten = new double[aantalSoorten];
		
		String paramAantal = null;
		String paramGewicht = null;
		String paramPlaatje = null;
		
		for (int i = 0; i < aantalSoorten; i++)
		{	try 
			{	aantalStuks[i] = 0;
				paramAantal = getParameter("aantal" + i);
				int as = 0;
				if(paramAantal!=null) as = Integer.parseInt(paramAantal);
				aantalStuks[i] = Math.max(0, Math.min(as, MAXAANTAL));	// limit: avoid 'array index out of bounds'
			}	catch ( NumberFormatException ex )
			{ 	// Foutmelding??
			}
			try 
			{	gewichten[i] = 0;
				paramGewicht = getParameter("gewicht" + i); 
				Double d = new Double(0);
				if(paramGewicht!=null) d = Double.valueOf(paramGewicht);
				gewichten[i] = d.doubleValue();
			}	catch ( NumberFormatException ex )
			{ 	// Foutmelding??
			}
			paramPlaatje = imageNames[i];
			if(paramAantal!=null && !paramAantal.equals("0"))fruitplaatje[i] = getImage(paramPlaatje);
			
			//System.out.println(""+aantalStuks[i]);
			//System.out.println(""+gewichten[i]);
			//System.out.println(""+paramPlaatje);
			//System.out.println("");
			
		}
	}
	
	private Hashtable makeDefaultParamValues(int variant)
	{	
		
		Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#FFFFFF");
		/*
		if(variant==0)
		{	
			h.put("aantal1","5");
			h.put("gewicht1","2.0");
			
			h.put("aantal3","5");
			h.put("gewicht3","1.0");
			
			h.put("aantal4","5");
			h.put("gewicht4","3.0");
			
		}
		if(variant==1)
		{	h.put("aantalSoorten","3");
		
			h.put("aantal1","5");
			h.put("gewicht1","3.0");
			h.put("plaatje1","perzik.gif");
			
			h.put("aantal2","5");
			h.put("gewicht2","4.0");
			h.put("plaatje2","appel.gif");
			
			h.put("aantal3","5");
			h.put("gewicht3","5.0");
			h.put("plaatje3","peer.gif");
		}
		if(variant==2)
		{	h.put("aantalSoorten","3");
		
			h.put("aantal1","5");
			h.put("gewicht1","4.0");
			h.put("plaatje1","appel.gif");
			
			h.put("aantal2","5");
			h.put("gewicht2","3.0");
			h.put("plaatje2","sinaasappel.gif");
			
			h.put("aantal3","5");
			h.put("gewicht3","5.0");
			h.put("plaatje3","ananas.gif");
		}
		if(variant==3)
		{	h.put("aantalSoorten","3");
		
			h.put("aantal1","5");
			h.put("gewicht1","2.0");
			h.put("plaatje1","perzik.gif");
			
			h.put("aantal2","5");
			h.put("gewicht2","3.0");
			h.put("plaatje2","tomaat.gif");
			
			h.put("aantal3","5");
			h.put("gewicht3","4.0");
			h.put("plaatje3","banaan.gif");
			
		}
		*/		
		return h;
	}
	
	public String getParameter(String name)
	{	String value = null;
		if(launchData!=null) value = (String)launchData.get(name);
		//if(value!=null) value = (String)defaultParamValues.get(name);
		return value;
	}
	
	public void zetBalansWaardeX(double xWaarde)
	{	
		
		gewichten[8] = xWaarde;
		gewichten[10] = 1;
		gewichten[17] = 1;
		//System.out.println("zetBalansX:" +xWaarde);
		run();
	}
	
	public void zetBalansVergelijking(String s)
	{	huidigeVergelijking = "";		
		String stringXLinks = "";
		String string1Links = "";
		String stringXRechts = "";
		String string1Rechts = "";
		
		int indexMin = s.indexOf("-");
		
		if(indexMin==-1)
		{
			String[] delen = StringUtils.split(s,"=");
			int indexPlusLinks = delen[0].indexOf("+");
			int indexPlusRechts = delen[1].indexOf("+");
			if(indexPlusLinks>-1)
			{	stringXLinks = delen[0].substring(0,delen[0].indexOf("x"));
				string1Links = delen[0].substring(indexPlusLinks+1);
			}
			else if(delen[0].indexOf("x")>-1) 
			{	stringXLinks = delen[0].substring(0,delen[0].indexOf("x"));
			}
			else string1Links = delen[0];
			
			if(indexPlusRechts>-1)
			{	stringXRechts = delen[1].substring(0,delen[1].indexOf("x"));
				string1Rechts = delen[1].substring(indexPlusRechts+1);
			}
			else if(delen[1].indexOf("x")>-1) 
			{	stringXRechts = delen[1].substring(0,delen[1].indexOf("x"));
			}
			else string1Rechts = delen[1];
		}
		
		
		//textArea.setText(stringXLinks + string1Links + stringXRechts + string1Rechts);
		
		int aantalXLinks = "".equals(stringXLinks) ? 0 : Integer.parseInt(stringXLinks);
		int aantal1Links = "".equals(string1Links) ? 0 : Integer.parseInt(string1Links);
		int aantalXRechts = "".equals(stringXRechts) ? 0 : Integer.parseInt(stringXRechts);
		int aantal1Rechts = "".equals(string1Rechts) ? 0 : Integer.parseInt(string1Rechts);
		
		
		
		int tussenruimteLinks = Math.min(20,20*8/(aantalXLinks+aantal1Links==0 ? 1 : aantalXLinks+aantal1Links));
		int tussenruimteRechts = Math.min(20,20*8/(aantalXRechts+aantal1Rechts==0 ? 1 : aantalXRechts+aantal1Rechts));
		
		main.maakWeegschaalLeeg();
		main.setBalance();
		
		
		if(aantalXLinks==0 && aantal1Links==0 && aantalXRechts==0 && aantal1Rechts==0
				|| gewichten[8]<0 || Math.rint(gewichten[8])-gewichten[8]!=0 ) 
		{	messageLabel.setText("No balance view possible");
			//add(afdekPanel,0);
			mouseHandler.fixed = true;
		}
		else
		{	messageLabel.setText("");
			if(!fixedOptie)
				mouseHandler.fixed = false;
				//remove(afdekPanel); 
			
			for(int i=0 ; i<aantalXLinks; i++)
		    {	links.addLWMComponent(fruitObjects[i],i*tussenruimteLinks,0);
		    }
			for(int i=aantalXLinks ; i<aantalXLinks + aantalXRechts; i++)
		    {	rechts.addLWMComponent(fruitObjects[i],(i-aantalXLinks)*tussenruimteRechts,0);
		    }
			for(int i=0 ; i<aantal1Links; i++)
		    {	links.addLWMComponent(fruitObjects[i+aantalStuks[8]],(i+aantalXLinks)*tussenruimteLinks,0);
		    }
			for(int i=aantal1Links ; i<aantal1Links + aantal1Rechts; i++)
		    {	rechts.addLWMComponent(fruitObjects[i+aantalStuks[8]],(i+aantalXRechts-aantal1Links)*tussenruimteRechts,0);
		    }
			main.setBalance();
		}
		huidigeVergelijking = s;
	}
	
	public String geefBalansVergelijking()
	{
		
//System.out.println("geefBalansVerg");

		int aantalXLinks = 0;
		int aantalXRechts = 0;
		int aantal1Links = 0;
		int aantal1Rechts = 0;
		for(int i=0 ; i<aantalStuks[8]; i++)
	    {	if(fruitObjects[i].getParent()==links) aantalXLinks++;
	    }
		for(int i=0 ; i<aantalStuks[8]; i++)
	    {	if(fruitObjects[i].getParent()==rechts) aantalXRechts++;
	    }
		for(int i=aantalStuks[8] ; i<aantalStuks[8] + aantalStuks[10] + aantalStuks[17]; i++)
	    {	if(fruitObjects[i].getParent()==links) aantal1Links++;
	    }
		for(int i=aantalStuks[8] ; i<aantalStuks[8] + aantalStuks[10] + aantalStuks[17]; i++)
	    {	if(fruitObjects[i].getParent()==rechts) aantal1Rechts++;
	    }
		
		String xLinks = aantalXLinks==1 ? "x" : ""+aantalXLinks;
		if(aantalXLinks==0) xLinks = "";
		else if(aantalXLinks!=1)xLinks = xLinks+"x";
		String plusString = aantalXLinks==0 ? "" : "+";
		String links1 = aantal1Links==0 ? "" : plusString+aantal1Links;
		String xRechts = aantalXRechts==1 ? "x" : ""+aantalXRechts;
		if(aantalXRechts==0) xRechts = "";
		else if(aantalXRechts!=1)xRechts = xRechts+"x";
		plusString = aantalXRechts==0 ? "" : "+";
		String rechts1 = aantal1Rechts==0 ? "" : plusString+aantal1Rechts;
		if((xLinks+links1).equals(""))links1="0";
		if((xRechts+rechts1).equals(""))rechts1="0";
		if(main.getBalanceStatus()==-1)
			return xLinks+links1+">"+xRechts+rechts1;
		if(main.getBalanceStatus()==1)
			return xLinks+links1+"<"+xRechts+rechts1;
		return xLinks+links1+"="+xRechts+rechts1;
	}
	
	public void zetEenheden(boolean b)
	{	
		showEenheden = b;
		
		for(int i=0 ; i<aantalFruitObjects; i++)
			voorraad.addLWMComponent(fruitObjects[i],fruitObjects[i].getLocation().x,fruitObjects[i].getLocation().y);
	
		main.setBalance();
		if(b)
		{	imageNames[11] = "1g.gif";
			imageNames[12] = "5g.gif";
			imageNames[13] = "10g.gif";
			imageNames[14] = "50g.gif";
			imageNames[15] = "100g.gif";
			imageNames[16] = "500g.gif";
		}
		else
		{	imageNames[11] = "1blok.gif";
			imageNames[12] = "5blok.gif";
			imageNames[13] = "10blok.gif";
			imageNames[14] = "50blok.gif";
			imageNames[15] = "100blok.gif";
			imageNames[16] = "500blok.gif";
		}
	
		images = new Hashtable();
		BalansFruitApplet.loadImages(images,imageNames);
		
		
		for(int i = 11; i < 17; i++)
		fruitplaatje[i] = getImage(imageNames[i]);
		run();
		
	}		
			

	public void zetFixedOptie(boolean b)
	{
		fixedOptie = b;
		
		mouseHandler.fixed = fixedOptie;
		
		repaint();

	}

	public void zetResetOptie(boolean b)
	{
		resetOptie = b;
		if(resetOptie)add(resetButton,0);
		else remove(resetButton);
		
		repaint();

	}

	public void zetViewEquation(boolean b)
	{
		
//System.out.println("zetViewEquation " + b);

		viewEquation = b;
		vergelijkingLabel.setVisible(viewEquation);
		
		if (viewEquation)
			vergelijkingLabel.setText(this.geefBalansVergelijking());
		//if(viewEquation)add(vergelijkingLabel,0);
		//else remove(vergelijkingLabel);
		
		repaint();

	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		launchData = h;
		loadParameters();
		//Thread t = new Thread(this);
		//t.start();
		run();
		
		int aantalFruitObjects=0;
		int[] stukFruitX=null;
		int[] containerNr=null;
		boolean fixedOptie=false;
		boolean bewaarOptie=false;
		boolean resetOptie=false;
		boolean viewEquation=false;
		boolean showEenheden = true;
		
		if(h.containsKey("aantalFruitObjects")) aantalFruitObjects = ((Integer)h.get("aantalFruitObjects")).intValue();
		if(h.containsKey("stukFruitX")) stukFruitX = (int[])h.get("stukFruitX");
		if(h.containsKey("containerNr")) containerNr = (int[])h.get("containerNr");
		if(h.containsKey("fixedOptie")) fixedOptie = ((Boolean)h.get("fixedOptie")).booleanValue();
		if(h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if(h.containsKey("resetOptie")) resetOptie = ((Boolean)h.get("resetOptie")).booleanValue();
		if(h.containsKey("viewEquation")) viewEquation = ((Boolean)h.get("viewEquation")).booleanValue();
		if(h.containsKey("showEenheden")) showEenheden = ((Boolean)h.get("showEenheden")).booleanValue();
		
		this.aantalFruitObjects = aantalFruitObjects;
		this.showEenheden = showEenheden;
		zetEenheden(showEenheden);
		
		
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	if(containerNr[i]==0) fruitObjects[i].setLocation(stukFruitX[i],fruitObjects[i].getLocation().y);
	    	if(containerNr[i]==1) 
	    	{	links.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
	    	if(containerNr[i]==2) 
	    	{	rechts.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
   	    }
		main.setBalance();
		
		//this.fixedOptie=fixedOptie;
		this.bewaarOptie=bewaarOptie;
		//this.resetOptie=resetOptie;
		//this.viewEquation=viewEquation;
		this.showEenheden = showEenheden;
		
		//if(fixedOptie)add(afdekPanel,0);
		//else remove(afdekPanel);
		
		//if(resetOptie)add(resetButton,0);
		//else remove(resetButton);
		
		//if(viewEquation)add(vergelijkingLabel,0);
		//else remove(vergelijkingLabel);
		
		zetFixedOptie(fixedOptie);
		zetResetOptie(resetOptie);
		zetViewEquation(viewEquation);
		
	}
	
	public void setState(Hashtable h)
	{
		
//System.out.println("setState " + bewaarOptie + " " + resetting);

		if(!bewaarOptie && !resetting)return;
		
		int aantalFruitObjects=0;
		int[] stukFruitX=null;
		int[] containerNr=null;
		boolean showEenheden = true;
		
		if(h.containsKey("aantalFruitObjects")) aantalFruitObjects = ((Integer)h.get("aantalFruitObjects")).intValue();
		if(h.containsKey("stukFruitX")) stukFruitX = (int[])h.get("stukFruitX");
		if(h.containsKey("containerNr")) containerNr = (int[])h.get("containerNr");
		if(h.containsKey("showEenheden")) showEenheden = ((Boolean)h.get("showEenheden")).booleanValue();
		
		this.aantalFruitObjects = aantalFruitObjects;
		this.showEenheden = showEenheden;
		
		zetEenheden(showEenheden);
		
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	if(containerNr[i]==0) 
	    	{	voorraad.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    		//fruitObjects[i].setLocation(stukFruitX[i],fruitObjects[i].getLocation().y);
//System.out.println("voorraad");	    	
	    	}
	    	if(containerNr[i]==1) 
	    	{	links.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
//System.out.println("links");	    	
	    	}
	    	if(containerNr[i]==2) 
	    	{	rechts.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
//System.out.println("rechts");	    	
	    	}
   	    }
		
		main.setBalance();
		
		
	}
	
	public void setEditState(Hashtable h)
	{
		launchData = h;
		loadParameters();
		//Thread t = new Thread(this);
		//t.start();
		run();
		
		int aantalFruitObjects=0;
		int[] stukFruitX=null;
		int[] containerNr=null;
		boolean showEenheden = true;
		
		boolean fixedOptie=false;
		boolean resetOptie=false;
		boolean viewEquation=false;
		boolean bewaarOptie = false;
		
		if(h.containsKey("aantalFruitObjects")) aantalFruitObjects = ((Integer)h.get("aantalFruitObjects")).intValue();
		if(h.containsKey("stukFruitX")) stukFruitX = (int[])h.get("stukFruitX");
		if(h.containsKey("containerNr")) containerNr = (int[])h.get("containerNr");
		if(h.containsKey("showEenheden")) showEenheden = ((Boolean)h.get("showEenheden")).booleanValue();

		if(h.containsKey("fixedOptie")) fixedOptie = ((Boolean)h.get("fixedOptie")).booleanValue();
		if(h.containsKey("bewaarOptie")) bewaarOptie = ((Boolean)h.get("bewaarOptie")).booleanValue();
		if(h.containsKey("resetOptie")) resetOptie = ((Boolean)h.get("resetOptie")).booleanValue();
		if(h.containsKey("viewEquation")) viewEquation = ((Boolean)h.get("viewEquation")).booleanValue();
		
		this.aantalFruitObjects = aantalFruitObjects;
		this.showEenheden = showEenheden;
		zetEenheden(showEenheden);
		
		this.bewaarOptie=bewaarOptie;
		
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	if(containerNr[i]==0) fruitObjects[i].setLocation(stukFruitX[i],fruitObjects[i].getLocation().y);
	    	if(containerNr[i]==1) 
	    	{	links.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
	    	if(containerNr[i]==2) 
	    	{	rechts.addLWMComponent(fruitObjects[i],stukFruitX[i],fruitObjects[i].getLocation().y);
	    	}
   	    }
		
		main.setBalance();
		
		zetFixedOptie(fixedOptie);
		zetResetOptie(resetOptie);
		zetViewEquation(viewEquation);

	}
	
	
	
	public Hashtable getState()
	{	
		int aantalFruitObjects;
		int[] stukFruitX;
		int[] containerNr;
		boolean showEenheden = true;
	    
		aantalFruitObjects = this.aantalFruitObjects;
		showEenheden = this.showEenheden;
	    
		stukFruitX = new int[aantalFruitObjects];
		containerNr = new int[aantalFruitObjects];
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	stukFruitX[i] = fruitObjects[i].getLocation().x;
	    	int contNr = 0;
	    	boolean opLinks = fruitObjects[i].getParent()==links;
	    	boolean opRechts = fruitObjects[i].getParent()==rechts;
	    	if(opLinks) contNr=1;
	    	if(opRechts) contNr=2;
	    	containerNr[i] = contNr;
	    }
	     			    
	    Hashtable h = new Hashtable();
	    h.put("aantalFruitObjects", new Integer(aantalFruitObjects));
	    h.put("stukFruitX", stukFruitX);
	    h.put("containerNr", containerNr);
	    h.put("showEenheden", new Boolean(showEenheden));
	    
	    if(bewaarOptie) return h;
	    else return new Hashtable();
	}
	
	public Hashtable getEditState()
	{	
		int aantalFruitObjects;
		int[] stukFruitX;
		int[] containerNr;
		boolean showEenheden;
		
	    
		//run();
		
		aantalFruitObjects = this.aantalFruitObjects;
		showEenheden = this.showEenheden;
	    
		stukFruitX = new int[aantalFruitObjects];
		containerNr = new int[aantalFruitObjects];
		for(int i=0 ; i<aantalFruitObjects; i++)
	    {	stukFruitX[i] = fruitObjects[i].getLocation().x;
	    	int contNr = 0;
	    	boolean opLinks = fruitObjects[i].getParent()==links;
	    	boolean opRechts = fruitObjects[i].getParent()==rechts;
	    	if(opLinks) contNr=1;
	    	if(opRechts) contNr=2;
	    	containerNr[i] = contNr;
	    }
		
	     	
		launchData.put("aantalFruitObjects", new Integer(aantalFruitObjects));
		launchData.put("stukFruitX", stukFruitX);
		launchData.put("containerNr", containerNr);
		launchData.put("showEenheden", new Boolean(showEenheden));
		return launchData;
	}
	
	public InteractieEditPanel getEditPanel(){return new BalansFruitInteractieEditPanel();}
		
	public void setBounds(int x, int y, int b, int h)
	{	//if(afdekPanel!=null) afdekPanel.setBounds(x,y,b,h);
		super.setBounds(x,y,b,h);
	}
	
	public void start(){}
	
	public void stop(){}
	
	public void wis(){}
	
	public void zetMaat(){}
	
	public int geefAsHoogte(){return 0;}
	
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
	
	public int getScore(){return 0;}
	
	public int getScoreMax(){return 0;}
	
	public boolean isCorrect(){return true;}
	
	public boolean isFout(){return false;}
	
	public void zetMode(int mode){}
	
	public void zetNagekeken(boolean b){}
	
	//public void stop(){}
	
	//public void start(){}
	
	public void destroy(){}
	
	public void opnieuw(){}
	
	public void kijkNa(){}
	
	public void kijkNa(int stapNr){}
	
	
	public void actionPerformed(ActionEvent e)
	{	
		if(e.getSource()==main)
		{
			
//System.out.println("main is source");

			vergelijkingLabel.setText(this.geefBalansVergelijking());

			
			
			if(!geefBalansVergelijking().equals("0=0") &&  !geefBalansVergelijking().equals(huidigeVergelijking))
			{	sendCommand("balansvergelijking");
				
			}
			if(main.getBalanceStatus()==0 && !geefBalansVergelijking().equals(huidigeVergelijking) && !huidigeVergelijking.equals(""))
			{
				sendCommand("maakStap");
				huidigeVergelijking = geefBalansVergelijking();
				
			}
			
		}
		else if ((e.getSource() == resetButton) && !editMode)
		{	
//System.out.println("resetButton");

			if (launchData == null)
			{	run();
//System.out.println("launchData == null");			
			}
			else
			{	resetting = true;
				setState(launchData);
				resetting = false;
//System.out.println("launchData not null");			
			}
		
		}
	}
	
	
	
	public void zetBreedte(int b){}
	
	public void zetHoogte(int h){}
	
	public void run()
	{	/*MediaTracker tracker = new MediaTracker(this);
		int id = 1;
		tracker.addImage(balansLinks, id);
		tracker.addImage(balansEvenwicht, id);
		tracker.addImage(balansRechts, id);
		tracker.addImage(wisknopImage, id);
		for (int i = 0; i < aantalSoorten; i++)
		{	tracker.addImage(fruitplaatje[i], id);
		}
		try 
		{	tracker.waitForID(id);
		} catch (InterruptedException e) 
		{	System.out.println("tracker zooit"); 
		}*/
		main.setImages(balansEvenwicht, balansLinks, balansRechts);
		createAndAddFruitObjecten();
		main.jeKanStarten();
	}
	
	private void createAndAddFruitObjecten()
	{	int pw = 0;
		int randomx;
		// create array
		//FruitObject[][] stukfruit = new FruitObject[aantalSoorten][MAXAANTAL] ;
		main.maakVoorraadLeeg();
		// loop over soorten
		aantalFruitObjects = 0;
		for ( int soortnr = 0; soortnr<aantalSoorten; soortnr++ )
		{	// loop over stuks
			// positioning: verdeel breedte in 'aantalStuks' gelijke delen en plaats steeds een exemplaar
			// van elk soort in een deel. Breedte van deel, laat rechts wat ruimte over:
			
			//System.out.println(""+aantalStuks[soortnr]);
			//System.out.println(""+gewichten[soortnr]);
			//System.out.println("");
			
			if(aantalStuks[soortnr]>0) pw = (TOTAALBREED-60)/aantalStuks[soortnr];
			for ( int stuknr = 0; stuknr<aantalStuks[soortnr] ; stuknr++ )
			{	
				if(aantalStuks[soortnr]>0)
				{	FruitObject fruitObject = new FruitObject(fruitplaatje[soortnr], gewichten[soortnr] );
					fruitObject.setPermissions(mp);
					fruitObject.addLWMListener(main);
					//stukfruit[soortnr][stuknr] = new FruitObject(fruitplaatje[soortnr], gewichten[soortnr] );
					//stukfruit[soortnr][stuknr].setPermissions(mp);
					//stukfruit[soortnr][stuknr].addLWMListener(main);	
					randomx = (int)(Math.random()*pw);				// random 0..pw-1
					voorraad.addLWMComponent(fruitObject, stuknr*pw+randomx, 0);
					fruitObjects[aantalFruitObjects] = fruitObject;
					aantalFruitObjects++;	
				}// gravity will pull it down!
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{	
	}
	public void mouseReleased(MouseEvent e) 
 	{	
 	}  
	public void mouseEntered(MouseEvent e)
	{}
	public void mouseExited(MouseEvent e)
	{}
	public void mouseClicked(MouseEvent e)
	{}
	
	// ActionProducer
		private ActionListener actionListener = null;

		public void addActionListener(ActionListener l)
		{
			actionListener = AWTEventMulticaster.add(actionListener, l);
		}

		public void removeActionListener(ActionListener l)
		{
			actionListener = AWTEventMulticaster.remove(actionListener, l);
		}

		public void produceAction(String command)
		{
			if (actionListener != null)
			{
				actionListener.actionPerformed(new ActionEvent(this, 0, command));
			}
		}
		//

		private void sendCommand(String command)
		{
			if(command.equals("balansvergelijking"))
			{	
				String balansString = geefBalansVergelijking();
				cbookEventHandler.fire(command,command, balansString);
			
			}
			if(command.equals("maakStap"))
			{	
				cbookEventHandler.fire(command,command);
			
			}
		}
		@Override
		public void acceptCBookEvent(CBookEvent event) {
			String command = event.getCommand();
			if(command.equals("balansvergelijking"))
			{
		 		String vergelijkingString = (String)event.getParameter("balansvergelijking");
				zetBalansVergelijking(vergelijkingString);
			}
			if(command.equals("zetOplossing"))
			{
		 		double xWaarde = ((Double)event.getParameter("zetOplossing")).doubleValue();
		 		zetBalansWaardeX(xWaarde);
			}
			if(command.equals("parameterwaarde"))
			{
				Map map = (Map)event.getParameters();
				if(map!=null)
				{	String name = (String)map.get("name");
					double xWaarde = ((Double)map.get("value")).doubleValue();
					for(int i=0 ; "x".equals(name) && i<aantalStuks[8]; i++)
				    {	fruitObjects[i].setWeight(xWaarde);
				    }
					main.setBalance();
				}
			}
		}


		@Override
		public void addCBookEventListener(CBookEventListener listener,
				String command) {
			System.out.println("addCBookEventListener: "+listener.toString() +"+"+command);
			cbookEventHandler.addCBookEventListener(listener, command);
			
		}

		@Override
		public void removeCBookEventListener(CBookEventListener listener,
				String command) {
			cbookEventHandler.removeCBookEventListener(listener, command);
			
		}


		@Override
		public String[] getSendCmds() {
			String[] commands = {"balansvergelijking", "maakStap"};
			return commands;
		}


		@Override
		public String[] getAcceptedCmds() {
			String[] commands = {"balansvergelijking", "zetOplossing", "parameterwaarde"};
			return commands;
		}
	
}

