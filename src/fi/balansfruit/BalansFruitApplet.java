package fi.balansfruit;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.applet.*;
// import java.awt.image.*;
import java.util.*;
// import java.applet.AudioClip;
// import fi.balansfruit.tekst.*;
import fi.beans.appletutil.AppletUtil;
import fi.beans.base64code.StringCodeObject;
import fi.beans.lwmobjects_swing.*;
import fi.beans.scorm.Parameter;
import fi.beans.scorm.SCORM12APIInterface;
import fi.beans.scorm.Scorm;
import fi.beans.scorm.ScormAppletIF;
import fi.beans.scorm.ScormEditComponentIF;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;


/**
 * BalansFruitApplet: eerste versie van 'Balans met fruit'
 *
 * Aanwijzingen voor de html van de applet:
 *
 * Standaardparameters:
 * De eerste vier zijn verplicht en MOGEN niet veranderd worden.
 * <APPLET 
 * 	CODEBASE="../.." 	NB: in fi structuur wordt dit iets anders (.... javaclasses)
 *	CODE="fi/balansfruit/BalansFruitApplet.class" 
 *	WIDTH=520		NB: afmetingen niet veranderen!
 *	HEIGHT=360>
 * Eigen parameters:
 * De taal-parameter van de applet is vooralsnog "nl" of "en" 
 * nu niet relevant: balansfruit heeft geen tekst!
 * OPM HUUB: de taal parameter wordt NIET ingelezen
 * 	<PARAM NAME=language VALUE="nl">
 * Daarna het aantal soorten fruit, vul het juiste getal in (nu 3):
 * 	<PARAM NAME=aantalSoorten VALUE="3">			
 * Daarna het aantal stuks (voor elke soort hetzelfde), vul het juiste getal in (nu 5):
 * OPM HUUB: de parameter aantalStuks wordt NIET gebruikt
 *           kies aantal per soort     
 * 	<PARAM NAME=aantalStuks VALUE="5">			
 * Tenslotte het fruit zelf:
 * Gebruik de parameters gewichtx, aantalx, x loopt van 1 tot en met aantalSoorten.
 * Bij aantalx zet je het aantal stuks, vul getal 1..5 in (nu 4):
 * 	<PARAM NAME=aantal1 VALUE="4">			
 * Bij gewichtx zet je het gewicht (mag kommagetal zijn):
 * 	<PARAM NAME=gewicht1 VALUE="3">
 * Het is de bedoeling dat de gebruikte plaatjes in een directory 'plaatjes' bij de html 
 * komen te staan. Geef ze op zonder pad:
 * OPM HUUB: DIT IS NU VERANDERD, ZET DE PLAATJES OOK IN resources	
 * 	<PARAM NAME=plaatje1 VALUE="perzik.gif">
 * NB: het boek-plaatje is vast en staat in de directory 'resources' bij de applet-code.
 *
 * @author Paul Bergervoet
 *
 * @version 1, 29 augustus 2000, 13 maart 2007
 */

public class BalansFruitApplet extends Applet implements  Runnable , WiskOpdrApplet
{	// Constants
	public static int MAXAANTAL = 10;			// max aantal per soort
	
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
	private FruitObject[][] stukfruit;
	private BalansFruit main;
	private SchaalContainer links;
	private SchaalContainer rechts;
	private LWMContainer voorraad;
	private LWMButton schoon;
	
	MovePermissions mp;					// movePermissions for fruit only
		
	// Images
	private int aantalSoorten;
	private int[] aantalStuks;
	private Image[] fruitplaatje;
	private double[] gewichten;
	private Image balansLinks;
	private Image balansEvenwicht;
	private Image balansRechts;
	private Image wisknopImage;
	
	private Hashtable defaultParamValues, launchData;
	
	protected static ResourceBundle rb;
	protected SCORM12APIInterface api;
	private TextField textField;
	
	public static Applet applet;
	
	// ResourceBundle voor internationalisatie
	// static ResourceBundle rb;

	
	public BalansFruitApplet()
	{	Locale language = new Locale ("nl", "");
		applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	public BalansFruitApplet(Locale language)
	{	
		applet=this;
		//rb = ResourceBundle.getBundle("fi.fruitbalanceapplet.text.Text",language);
	}
	
	
	
/**
 * Init van BalansFruitApplet.
 * 
 */
	
 
	public void init()
	{	// get the language parameter, default: "nl", other value: "en"
		// String langArg;
		// Locale language;
		// langArg = getParameter("language");
		// if ( langArg == null ) langArg = "nl";		
		// language = new Locale (langArg, "");
		// rb = ResourceBundle.getBundle("fi.balansfruit.tekst.Tekst", language);
		
		
		
		String launchDataString = super.getParameter("launchData");
		
		if(launchDataString!=null)
		{	Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable)o;
		}
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		String variantString = super.getParameter("variant");
		int variant = 0;
		if(variantString!=null) variant = Integer.parseInt(variantString);
		
		defaultParamValues = makeDefaultParamValues(variant);
		
		
		URL codeBase = getCodeBase();	
		balansLinks = getImage(codeBase,"fi/balansfruit/resources/balanslinks.gif");
		balansEvenwicht = getImage(codeBase,"fi/balansfruit/resources/balansgoed.gif");
		balansRechts = getImage(codeBase,"fi/balansfruit/resources/balansrechts.gif");
		wisknopImage = getImage(codeBase,"fi/balansfruit/resources/minibalansgoed.gif");
		loadParameters();

		// Opbouw applet met Panel
		setLayout(null);					// Geen layout-manager
		hetBlad = new LWMBufferPanel(TOTAALBREED, TOTAALHOOG);
		hetBlad.setBackground(Color.white);
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
		
		// de schoon-knop
		schoon = new LWMButton(wisknopImage, "", 50, 25);
		schoon.addActionListener(main);
		main.addLWMComponent(schoon, 225, 10);
		
		// MovePermissions-object maken voor FruitObjecten: mogen naar links, rechts en voorraad
		mp = new MovePermissions();
		mp.addPermission(voorraad);
		mp.addPermission(links);
		mp.addPermission(rechts);
				
		// load images, add fruitobjects
		Thread t = new Thread(this);
		t.start();
		
		setSize(TOTAALBREED, TOTAALHOOG); 
		setVisible(true);
		
		//Test-textfield
		textField = new TextField();
		textField.setBounds(50,100,200,25);
		//add(textField);
		
		//BalansFruitInteractiePanel p = new BalansFruitInteractiePanel(this);
		//add(p,0);
		
	
	}
	
	public static void loadImages(Hashtable images,String[] imageNames)
	{	//AppletUtil au = new AppletUtil(applet);
		MediaTracker tr = new MediaTracker(applet);
		Image[] image = new Image[imageNames.length];
		for(int i=0 ; i<imageNames.length ; i++)
		{	image[i] = makeImage("resources/" + imageNames[i]);
			tr.addImage(image[i], 0);
		}
		try{tr.waitForAll();} catch(Exception e) {};
		for(int i=0 ; i<imageNames.length ; i++)
		{	images.put(imageNames[i], image[i]);
		}
		
	}
	
	private static Image makeImage(String resourceName)
	{	byte[] buffer;
		try 
		{
		    InputStream in = applet.getClass().getResourceAsStream(resourceName);
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		     buffer = new byte[1024]; 
		    int len;
		    do { 
			len = in.read(buffer);
	//System.out.println("read " + len);
		        if(len > 0) bos.write(buffer, 0, len);
		    }
		    while(len > 0);
		    buffer = bos.toByteArray();
		    in.close();
		    bos.close();
		    	    
		}  catch(Exception e)
		{   e.printStackTrace();
		    return null;
		}
		return applet.getToolkit().createImage(buffer); 
	}
	
	private Hashtable makeDefaultParamValues(int variant)
	{	
		
		Hashtable h = new Hashtable();
		h.put("language","nl");
		h.put("bgcolor","#FFFFFF");
		if(variant==0)
		{	h.put("aantalSoorten","3");
		
			h.put("aantal1","6");
			h.put("gewicht1","1.0");
			h.put("plaatje1","citroen.gif");
			
			h.put("aantal2","6");
			h.put("gewicht2","2.0");
			h.put("plaatje2","appel.gif");
			
			h.put("aantal3","6");
			h.put("gewicht3","3.0");
			h.put("plaatje3","peer.gif");
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
				
		return h;
	}
	
	public String getParameter(String name)
	{	String value = null;
        value = super.getParameter(name);
		if(value==null && launchData!=null)value = (String)launchData.get(name);
		if(value==null) value = (String)defaultParamValues.get(name);
		return value;
	}
	
	public String getState()
	{	String text = null;
	
		//vraag de gegevens op die de state bepalen
	    text = textField.getText();
	    
	    Hashtable h = new Hashtable();
	    
	    //voeg de gegeven toe aan de hashtable
	    h.put("text", text);
	      
	    //codeer de hashtable tot string
	    String s = StringCodeObject.encodeObjectToString(h);
	    return s;
	}
	
	public double getScore()
	{	return 0.5;
	}
	
	public void start()
	{	if(api!=null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if(s!=null && !s.equals(""))setState(s);
		}
	
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}
	
	public void stop()
	{	if(api!=null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}
	
	private void createAndAddFruitObjecten()
	{	int pw = 0;
		int randomx;
		// create array
		stukfruit = new FruitObject[aantalSoorten][MAXAANTAL] ;
		// loop over soorten
		for ( int soortnr = 0; soortnr<aantalSoorten; soortnr++ )
		{	// loop over stuks
			// positioning: verdeel breedte in 'aantalStuks' gelijke delen en plaats steeds een exemplaar
			// van elk soort in een deel. Breedte van deel, laat rechts wat ruimte over:
			pw = (TOTAALBREED-60)/aantalStuks[soortnr];
			for ( int stuknr = 0; stuknr<aantalStuks[soortnr]; stuknr++ )
			{	stukfruit[soortnr][stuknr] = 
					new FruitObject(fruitplaatje[soortnr], gewichten[soortnr] );
				stukfruit[soortnr][stuknr].setPermissions(mp);
				stukfruit[soortnr][stuknr].addLWMListener(main);	
				randomx = (int)(Math.random()*pw);				// random 0..pw-1
				voorraad.addLWMComponent(stukfruit[soortnr][stuknr], stuknr*pw+randomx, 0);		
														// gravity will pull it down!
			}
		}
	}

	private void loadParameters()
	{	// Aantal soorten fruit
		aantalSoorten = 0;
		try 
		{ 	aantalSoorten = Integer.parseInt( getParameter("aantalSoorten") );
		} catch ( NumberFormatException ex )
		{ 	// Foutmelding??
		}
	// Array van images aanmaken met juiste afmeting
		fruitplaatje = new Image[aantalSoorten];
		aantalStuks = new int[aantalSoorten];
		gewichten = new double[aantalSoorten];
		
	// plaatjes en bijbehorende gewichten inlezen
		URL docBase = getDocumentBase();
		URL codeBase = getCodeBase();			
		String paramAantal;
		String paramGewicht;
		String paramPlaatje;
		for (int i = 1; i <= aantalSoorten; i++)
		{	try 
			{	aantalStuks[i-1] = 1;
				paramAantal = getParameter("aantal" + i);
				int as = Integer.parseInt(paramAantal);
				aantalStuks[i-1] = Math.max(1, Math.min(as, MAXAANTAL));	// limit: avoid 'array index out of bounds'
			}	catch ( NumberFormatException ex )
			{ 	// Foutmelding??
			}
			try 
			{	gewichten[i-1] = 1;
				paramGewicht = getParameter("gewicht" + i); 
				Double d = Double.valueOf(paramGewicht);
				gewichten[i-1] = d.doubleValue();
			}	catch ( NumberFormatException ex )
			{ 	// Foutmelding??
			}
			paramPlaatje = getParameter("plaatje"+i);
// hier veranderen			
//			fruitplaatje[i-1] = getImage(docBase,"plaatjes/"+paramPlaatje);

			fruitplaatje[i-1] = 
				getImage(codeBase,"fi/balansfruit/resources/" + paramPlaatje);
//	balansLinks = getImage(codeBase,"fi/balansfruit/resources/balanslinks.gif");			
		}
	}

/**
 * Laadt de plaatjes in de achtergrond terwijl de applet er al staat.
 */
	public void run()
	{	MediaTracker tracker = new MediaTracker(this);
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
		{	//System.out.println("tracker zooit"); 
		}
		main.setImages(balansEvenwicht, balansLinks, balansRechts);
		createAndAddFruitObjecten();
		main.jeKanStarten();
	}
	
	public void setState(String s)
	{	//decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		Hashtable h = (Hashtable)o;
		
		//haal de data uit de hashtabel
		String text = (String)h.get("text");
		
	    //herstel de state van het applet
	    textField.setText(text);
	}
	
	
	
	
	
	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
    
    public Parameter[] getEditableParameters()
	{	return null;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }
    
    public InteractiePanel getInteractiePanel()
    {	return new BalansFruitInteractiePanel();
    }
    

}
