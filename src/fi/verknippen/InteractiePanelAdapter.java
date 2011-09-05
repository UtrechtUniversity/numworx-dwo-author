package fi.verknippen;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Component;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

import javax.swing.JPanel;

import fi.beans.scorm.*;
import fi.beans.wiskopdrbeans.*;
import fi.dwo.parameters.domain.*;
import fi.dwo.parameters.gui.*;

public class InteractiePanelAdapter extends Panel implements InteractiePanel, AppletStub, AppletContext
{
	private ScormAppletIF applet;
	private Hashtable launchData;
	private boolean initiated;
	
	int scoreMax = 10;
	int score = 0;
	
	public InteractiePanelAdapter(ScormAppletIF applet)
	{
		this.applet = applet;
		((Applet) applet).setStub(this);
		setLayout(new BorderLayout());
		add("Center", (Component) applet);
		//launchData = new Hashtable();
		launchData = ((WiskOpdrParamEditApplet) getApplet()).getDefaultParameters();
	}
	
	public ScormAppletIF getApplet()
	{
		return applet;
	}
	
	public void resetApplet()
	{
		
	}
	
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
	{
		Hashtable appletLaunchData = null;
		String appletEditState = null;
		
		if (h.containsKey("appletLaunchData")) 
			appletLaunchData = (Hashtable) h.get("appletLaunchData");
		if (h.containsKey("appletEditState")) 
			appletEditState = (String) h.get("appletEditState");
		
		launchData = appletLaunchData;
		start();
		applet.setState(appletEditState);
		
		
	}
	
	public void setState(Hashtable h)
	{
		String appletState = null;
		
		if (h.containsKey("appletState")) 
			appletState = (String) h.get("appletState");
		
		applet.setState(appletState);
	
	}
	
	public void setLaunchData(Hashtable h)
	{
		launchData = h;
	}
	
	public Hashtable getLaunchData()
	{
		return launchData;
	}
	
	public void setEditState(Hashtable h)
	{
		
		Hashtable appletLaunchData = null;
		String appletEditState = null;
		
		if (h.containsKey("appletLaunchData")) 
			appletLaunchData = (Hashtable) h.get("appletLaunchData");
		if (h.containsKey("appletEditState")) 
			appletEditState = (String) h.get("appletEditState");
		
		launchData = appletLaunchData;
		
		restart();
		applet.setState(appletEditState);
		
		
		
	}
	
	public Hashtable getState()
	{
		String appletState = applet.getState();
		
		Hashtable h = new Hashtable();
		h.put("appletState", appletState);
		
		return h;
	}
	
	public Hashtable getEditState()
	{
		Hashtable appletLaunchData = launchData;
		String appletEditState = null;
		if (initiated) 
			appletEditState = applet.getState();
		
		Hashtable h = new Hashtable();
		h.put("appletLaunchData", appletLaunchData);
		
		h.put("scoreMax", new Integer(scoreMax));
		
		if (appletEditState != null)
			h.put("appletEditState", appletEditState);
		
		return h;
	}
	
	public InteractieEditPanel getEditPanel()
	{
		return new InteractieEditPanelAdapter(this);
		//return null;
	}
	public void setBounds(int x, int y, int b, int h)
	{	
		super.setBounds(x,y,b,h);
		//((Component)applet).setBounds(0,0,b,h);
		
	}
	public void wis()
	{
		
	}
	public void zetMaat()
	{
	
	}
	public int geefAsHoogte()
	{
		return 0;
	}
	public int getIpId()
	{
		return 0;
	}
	public String getIpExpString()
	{
		return null;
	}
	public int getScore()
	{
//System.out.println("getScore() " + score);	    
		return score;
	}
	public int getScoreMax()
	{
		return scoreMax;
	}
	public boolean isCorrect()
	{	if (applet == null)
			return true;
		
		if (((Verknippen) applet).taakNummer == 1)
		{	
			if (((Verknippen) applet).opdrachten[0].drawingPanel != null)
			{    
/*				
				boolean ok = ((Verknippen) applet).opdrachten[0].drawingPanel.figureIsRectangle;
			    if (ok)
			        score = 10;
			    else 
			        score = 0;
*/			        
				return ((Verknippen) applet).opdrachten[0].drawingPanel.figureIsRectangle;
			}
			else 
				return true;
		
		}
		else	
			return true;
	}
	public boolean isFout()
	{
		return false;
	}
	public void zetMode(int mode)
	{
	
	}
	public void zetNagekeken(boolean b)
	{
	
	}
    public void stop()
	{
	
	}
    public void start()
	{
    	if (!initiated)
		{
			((Applet)applet).setSize(getSize());
	    	((Applet)applet).init();
	    	((Applet)applet).start();
	    	((WiskOpdrParamEditApplet) applet).setSingleComponent();
	    	initiated = true;
		}
	}
    public void restart()
	{
    	if (!initiated)
		{
			((Applet) applet).setSize(getSize());
	    	((Applet) applet).init();
	    	((Applet) applet).start();
	    	((WiskOpdrParamEditApplet) applet).setSingleComponent();
	    	initiated = true;
		}
    	else 
    	{	
    		String className = applet.getClass().getName();
    		Locale locale = ((Applet) applet).getLocale();
    		try
    		{	Class c = Class.forName(className);
    	    	Constructor cc = c.getDeclaredConstructor(new Class[] { Locale.class } );
    	    	Object o = cc.newInstance(new Object[] { locale } );
    	    	remove((Component) applet);
    	    	applet = (ScormAppletIF) o;
    	    	((Applet) applet).setStub(this);
    	    	add("Center", (Component) applet );
    	    	((Applet) applet).setSize(getSize());
    	    	((Applet) applet).init();
    	    	((Applet) applet).start();
    	    	((WiskOpdrParamEditApplet) applet).setSingleComponent();
    	    	initiated = true;
    		}
    		catch(Exception e)
    		{	
    		}
    	}
	}
    public void destroy()
	{
	
	}
    public void opnieuw()
	{	((Verknippen) applet).opnieuwAction();
		score = 0;
		produceAction("changed");
	
	}
    public void kijkNa()
	{
        
//System.out.println("kijkNa() - 1");        
        if (((Verknippen) applet).opdrachten[0].drawingPanel != null)
        {    
        	boolean ok = ((Verknippen) applet).opdrachten[0].drawingPanel.figureIsRectangle;
        	if (ok)
        		score = 10;
        	else 
        		score = 0;
         
//System.out.println("kijkNa() - 2");         
        } 
	
	}
    public void kijkNa(int stapNr)
	{
	
	}
    //public void addActionListener(ActionListener al)
	//{
	
	//}
	public void actionPerformed(ActionEvent e)
	{   
	
	}
	
	//ActionProducer
	private ActionListener actionListener = null;
	
	public void addActionListener(ActionListener al) 
 	{	actionListener = AWTEventMulticaster.add(actionListener, al);
 	}
 	
 	public void removeActionListener(ActionListener al)
 	{	actionListener = AWTEventMulticaster.remove(actionListener, al);
 	}	
 	
 	public void produceAction(String command)
 	{	if (actionListener != null)
 		{	kijkNa();
 	        actionListener.actionPerformed(new ActionEvent(this, 0, command));
 		    
 		}
 	}
 	//end ActionProducer
	
	
	// AppletStub methodes
    public boolean isActive(){return true;}
    public URL getDocumentBase(){return null;}
	public URL getCodeBase(){return null;}
	
	public String getParameter(String name)
	{
		String value = null;
        if (launchData != null)
        {	
        	value = (String) launchData.get(name);
if (name.equals("taaknummer"))        
System.out.println("ld not null tn = " + value);        
        	
        }	
		return value;
	}
    
	public void appletResize( int width, int height ){}
    public AppletContext getAppletContext(){return this;}
    	
    // AppletContext methodes
    public AudioClip getAudioClip( URL url ){return null;}
    public Image getImage( URL url )
    {	Toolkit tk = Toolkit.getDefaultToolkit();
		try
		{	ImageProducer prod = (ImageProducer) url.getContent();
		    return tk.createImage( prod );
		}
		catch ( IOException e )
		{
			return null;
		}
	}
	public Applet getApplet( String name ){return null;}
    public Enumeration getApplets(){return null;}
    public void setStream(String s, InputStream is){}
    public InputStream getStream(String s){return null;}
    public Iterator getStreamKeys(){return null;}
	public void showDocument( URL url ){}
    public void showDocument( URL url, String target ){}
    public void showStatus( String status ){}
}
