package fi.wiskopdr;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.swing.*;

import fi.beans.appletutil.*;
import fi.beans.openmath.MathematicaLink;
import fi.beans.scorm.*;
import fi.beans.base64code.*;
import fi.beans.ideas.IdeasClient;
import fi.wiskopdr.opdrnav.*;
import fi.wiskopdr.tekstobjects.TekstImageVak;
	

public class ScormEditComponent extends JPanel implements ScormEditComponentIF
{
	private Hashtable launchData;
	private OpdrNavStructEdit onsEdit;
	//private WiskOpdr starter;
		
	public ScormEditComponent(Hashtable launchData, WiskOpdr applet)
	{	setLayout(null);
		//setBackground(WiskOpdr.bgcolor);
		setBackground(new Color(240,240,240));
		super.setSize(790,520); //voor dwo
		//setState(launchData);
		this.launchData = launchData;
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
		String langArg = getParameter("language");
        if ( langArg == null || langArg.equals("")) langArg = "en";
        Locale language = new Locale (langArg, "");
        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
        if(WiskOpdr.ideas == null)
        {   try 
	        {
	        	WiskOpdr.ideas = new IdeasClient(applet,IdeasClient.IDEAS);
	        	//WiskOpdr.ideas = new fi.servlet.ideas.Ideas();
	        } 
	        catch (Exception e) 
			{	e.printStackTrace();
			}
        }
	    if(WiskOpdr.phrasebook == null)
	    {   try 
		    {  	
	    		WiskOpdr.phrasebook =  new MathematicaLink(applet);   
		    } 
		    catch (Exception e) 
		    {	e.printStackTrace();
			}
	    }
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	       
		onsEdit = new OpdrNavStructEdit(new fi.wiskopdr.opdrnav.MyOpdrEditContainer(),0,0,790, 520, launchData);
		onsEdit.setBackground(getBackground());
		add(onsEdit);
		setApplet(applet);
	}
	
	public void setBackground(Color c)
	{	super.setBackground(c);
		Component[] components = getComponents();
		for(int i=0 ; i<components.length ; i++)
		{
			components[i].setBackground(c);
		}
		
	}
	
	public void setApplet(WiskOpdr applet)
	{	applet.loadImages(new AppletUtil(applet));
		
	}
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		if (onsEdit!=null)onsEdit.setSize(b,h);
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	public Hashtable getLaunchData()
    {   if(! WiskOpdr.launchDataChanged)return launchData;
		Hashtable h = onsEdit.getEditState();
    	String language = getParameter("language");
    	String bgcolor = getParameter("bgcolor");
    
    	if(language!=null) h.put("language",language);
    	if(bgcolor!=null) h.put("bgcolor",bgcolor);
    	if(TekstImageVak.getImageMapString()!=null)h.put(TekstImageVak.IMAGE_MAP, TekstImageVak.getImageMapString());
    	return h;
	}
	
	public Component getComponent()
	{   return this;
	} 
	
	public void setState(Hashtable launchData)
	{	if(launchData.containsKey("launchData"))
		{	String launchDataString = (String)launchData.get("launchData");
			if(launchDataString!=null)
			{	Object o = StringCodeObject.decodeStringToObject(launchDataString);
				launchData = (Hashtable)o;
			}
		}
		this.launchData = launchData;
        //ImageVak.setImageMap(launchData.get(ImageVak.IMAGE_MAP));
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
        
        String langArg = getParameter("language");
        if ( langArg == null || langArg.equals("")) langArg = "en";
        Locale language = new Locale (langArg, "");
        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
		
        /*String aantalActiviteitenString = this.getParameter("aantalActiviteiten");
		int aantalActiviteiten = Integer.parseInt(aantalActiviteitenString);
		int[] aantalOpdrachten = new int[aantalActiviteiten];
		String[] activiteitNamen = new String[aantalActiviteiten];
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	activiteitNamen[i] = this.getParameter("activiteit_"+(i+1));
			String aantalString = this.getParameter("aantalOpdrachten_"+(i+1));
			aantalOpdrachten[i] = Integer.parseInt(aantalString);
		}
		if(onsEdit != null) remove(onsEdit);
		onsEdit = new OpdrNavStruct(aantalActiviteiten,aantalOpdrachten,activiteitNamen,0,0,770, 520, null, true);
		add(onsEdit);
		
		for(int i=0 ; i<aantalActiviteiten ; i++)
		{	for(int j=0 ; j<aantalOpdrachten[i] ; j++)
			{	MyOpdrEditContainer opdrEditContainer = new MyOpdrEditContainer();
				onsEdit.zetOpdrContainer(opdrEditContainer,i,j);
			}
		}*/
        //onsEdit = new OpdrNavStruct(null,new MyOpdrEditContainer(),0,0,770, 520, null, true, launchData);
		//add(onsEdit);
		
		//onsEdit.setEditState(launchData);
        
        this.launchData = launchData;
        if(onsEdit!=null)remove(onsEdit);
		onsEdit = new OpdrNavStructEdit(new fi.wiskopdr.opdrnav.MyOpdrEditContainer(),0,0,790, 520, launchData);
		add(onsEdit);
	}
	
	public void end()
    {   if(onsEdit!=null) onsEdit.destroy();
	}
	
    public void reset()
    {   if(onsEdit!=null)onsEdit.stop();
	}	
    
    public fi.wiskopdr.opdrnav.MyOpdrEditContainer getOpdrEditContainer() {
        return onsEdit.getOpdrEditContainer();
    }
	
}
