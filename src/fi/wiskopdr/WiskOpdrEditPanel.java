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
import fi.wiskopdr.tekstobjects.ShareAction;
import fi.wiskopdr.tekstobjects.TekstImageVak;
	

public class WiskOpdrEditPanel extends JPanel 
{
	private Hashtable launchData;
	private String launchDataString;
	private OpdrNavStructEdit onsEdit;
	private fi.wiskopdr.opdrnav.MyOpdrEditContainer opdrContainer;
	
	public WiskOpdrEditPanel(String launchDataString, WiskOpdr applet)
	{	setLayout(null);
		setOpaque(false);
		super.setSize(700,300);
		
		this.launchDataString = launchDataString;
		launchData = null;
		if (launchDataString != null) {
			Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable) o;
		}
		if (launchData == null)
			launchData = applet.makeDefaultParamValues(0);

		ShareAction.init((String)launchData.get(ShareAction.SHARE_MAP));
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
//		String langArg = getParameter("language");
//        if ( langArg == null || langArg.equals("")) langArg = "en";
//        Locale language = new Locale (langArg, "");
//        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
		
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	    opdrContainer =  new fi.wiskopdr.opdrnav.MyOpdrEditContainer(700,300); 
		onsEdit = new OpdrNavStructEdit(opdrContainer,0,0,700, 300, launchData);
		onsEdit.removeMouseListener(onsEdit);
		opdrContainer.setPlainEditor(this);
		add(opdrContainer);

	}
	
	public WiskOpdrEditPanel(Hashtable launchData, WiskOpdr applet)
	{	setLayout(null);
		setOpaque(false);
		super.setSize(700,300); 
		
		this.launchData = launchData;
		ShareAction.init((String)launchData.get(ShareAction.SHARE_MAP));
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
		String langArg = getParameter("language");
        if ( langArg == null || langArg.equals("")) langArg = "en";
        Locale language = new Locale (langArg, "");
        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
		
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	    opdrContainer =  new fi.wiskopdr.opdrnav.MyOpdrEditContainer(700,300); 
		onsEdit = new OpdrNavStructEdit(opdrContainer,0,0,700, 300, launchData);
		onsEdit.removeMouseListener(onsEdit);
		opdrContainer.setPlainEditor(this);
		add(opdrContainer);
	}
	
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		if (opdrContainer!=null)opdrContainer.setSize(b,h);
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	public Hashtable getLaunchData()
    {   if(!WiskOpdr.launchDataChanged)return launchData;
		Hashtable h = onsEdit.getEditState();
    	String language = getParameter("language");
    	String bgcolor = getParameter("bgcolor");
    
    	if(language!=null) h.put("language",language);
    	if(bgcolor!=null) h.put("bgcolor",bgcolor);
    	String imageMapString = TekstImageVak.getImageMapString();
		if(imageMapString!=null)h.put(TekstImageVak.IMAGE_MAP, imageMapString);
		String shareMapString = ShareAction.getSharedLaunchData();
		if(shareMapString != null) h.put(ShareAction.SHARE_MAP, shareMapString);
    	return h;
	}
	
	public String getText()
	{	if(!WiskOpdr.launchDataChanged)
			return launchDataString;
		launchData = getLaunchData();
		launchDataString = StringCodeObject.encodeObjectToString(launchData);
		return launchDataString;
	}
	
	public Component getComponent()
	{   return this;
	} 
	
	public void end()
    {   if(onsEdit!=null) onsEdit.destroy();
	}
	
    public void reset()
    {   if(onsEdit!=null)onsEdit.stop();
	}	
    
}
