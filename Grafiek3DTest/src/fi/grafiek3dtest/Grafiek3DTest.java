package fi.grafiek3dtest;

import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;


import fi.beans.appletutil.AppletUtil;
import fi.beans.mainframe.JApplet;
//import fi.beans.ideas.IdeasClient;
//import fi.beans.ideas.IdeasIF;
import fi.beans.openmath.MathematicaLink;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
//deze moet vanwege WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
//deze moet vanwege InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;


public class Grafiek3DTest extends JApplet implements WiskOpdrApplet  
{
	public static ResourceBundle rb;
	public static String langArg;
	public static Locale language = new Locale("nl", "");
	
	public static String deployVariant = "";
	public static boolean mobileVersion = false;
	
	public static MathematicaLink phrasebook;
	
	public static JApplet applet;	
	public static boolean mac = false;
	public static boolean zoefi = false;
	
	public static boolean formTimes = true;
	public static boolean fToets = true;
	
	
	public static Font tekstFont = new Font("SansSerif",Font.PLAIN,12);
	public static Font tekstFontBold = new Font("SansSerif",Font.BOLD,12);
	public static Font tekstFontKlein = new Font("SansSerif",Font.PLAIN,11);
	public static Font titelFont = new Font("SansSerif",Font.BOLD,16);
	
	public static Font formuleFont0 = new Font("TimesRoman",Font.PLAIN,16);
	public static Font formuleFont1 = new Font("TimesRoman",Font.PLAIN,16);
	public static Font formuleFont2 = new Font("TimesRoman",Font.PLAIN,14);
	
	public static Font formuleFont0Mac = new Font("SansSerif",Font.PLAIN,14);
	public static Font formuleFont1Mac = new Font("SansSerif",Font.PLAIN,14);
	public static Font formuleFont2Mac = new Font("SansSerif",Font.PLAIN,12);
	
	Grafiek3DComponent grafiek3DComponent;
	
	Image zoomInImage, zoomUitImage;
	
	String[] imageNames = 
	{	"zoominknop.gif",
		"zoomuitknop.gif",
	};
	Hashtable images;
	
	public Grafiek3DTest(Locale language)
    {   applet = this;
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.grafiek3dtest.text.Text", language);
    }
    
   
	public Grafiek3DTest()
	{	applet = this;
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.grafiek3dtest.text.Text", language);
	}
	
	public void init()
	{
		applet = this;
		
		getContentPane().setLayout(null);
		
		// instelling taal
		String langArg = getParameter("language");
		if (langArg == null)
			langArg = "nl";
		language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.grafiek3dtest.text.Text", language);
		
		try 
		{	phrasebook = new MathematicaLink(new URL("http://www.fi.uu.nl/servlet/mathshell/"));
        } 
		catch (MalformedURLException e) 
		{   // TODO Auto-generated catch block
            e.printStackTrace();
        }	
		
		if (images == null)
		{
			images = new Hashtable();
			loadImages(images, imageNames);
		}
		grafiek3DComponent = new Grafiek3DComponent(0, 0, getSize().width, getSize().height, images, imageNames);
		getContentPane().add(grafiek3DComponent);
		
	}
	
	public void loadImages(Hashtable images, String[] imageNames)
	{	AppletUtil au = new AppletUtil(applet);
		MediaTracker tr = new MediaTracker(applet);
		Image[] image = new Image[imageNames.length];
		for (int i = 0; i < imageNames.length; i++)
		{	image[i] = au.getImage("resources/" + imageNames[i]);
			tr.addImage(image[i], 0);
		}
		try
		{	tr.waitForAll();
		} 
		catch(Exception e)
		{};
		for (int i = 0; i < imageNames.length; i++)
		{	
//if (image[i] != null)
//System.out.println("im " + i + " not null");	
			images.put(imageNames[i], image[i]);
		}
		
	}
/*
	public static void loadImages(Hashtable images,String[] imageNames)
	{	AppletUtil au = new AppletUtil(applet);
		MediaTracker tr = new MediaTracker(applet);
		Image[] image = new Image[imageNames.length];
		for (int i = 0; i < imageNames.length; i++)
		{	image[i] = au.getImage("resources/" + imageNames[i]);
			tr.addImage(image[i], 0);
		}
		try
		{	tr.waitForAll();
		} 
		catch(Exception e)
		{};
		for (int i = 0; i < imageNames.length; i++)
		{	images.put(imageNames[i], image[i]);
		}
		
	}
*/	
	// interface WiskOpdrApplet 
	public InteractiePanel getInteractiePanel()
	{	return new Grafiek3DInteractiePanel();
	}
	
	
}
