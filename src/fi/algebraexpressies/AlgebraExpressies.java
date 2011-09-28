package fi.algebraexpressies;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.algebraexpressies.text.*;

import java.applet.Applet;
import fi.beans.mainframe.*;
import fi.beans.scorm.*;
import fi.beans.base64code.StringCodeObject;
import fi.beans.copyright.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;


/**
 * @author Peter Boon
 */

public class AlgebraExpressies extends JApplet implements ScormAppletIF, WiskOpdrApplet
{
	private SCORM12APIInterface api;
	
	private FIButton fiButton;
	protected static ResourceBundle rb;
	private String langArg;
	
	boolean scormed = false;
	
	static Image goedkrul, foutkruis, halfkrul;	
	
	AlgebraSchuifVeld as;
		
	public static void main(String[] args)    
	{	int width = 780;
        int height = 550;
        AlgebraExpressies algebraExpressies = new AlgebraExpressies();
        ScormMainFrame mf = new ScormMainFrame(algebraExpressies, width, height);
        mf.setTitle("AlgebraExpressies Scormed");
		mf.pack();
		mf.show();
		
		algebraExpressies.setLocation(mf.getInsets().left, mf.getInsets().top);
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);
		
		
	}
	
	public AlgebraExpressies()
	{	Locale language = new Locale ("nl", "");
		rb = ResourceBundle.getBundle("fi.algebraexpressies.text.Text",language);
		
//System.out.println("constr 1");

	}
	
	public AlgebraExpressies(Locale language)
	{	rb = ResourceBundle.getBundle("fi.algebraexpressies.text.Text",language);

//System.out.println("constr 2");	

	}
	
	public void init()
	{	
		
		try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		getContentPane().setLayout(null);
				
		String langArg = getParameter("language");
		if (langArg == null) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.algebraexpressies.text.Text",language);
		
		fiButton = new fi.beans.copyright.FIButton("Info", 
								new String[]{"Algebra Expressies",
											 "versie-info: 20110928",
											 "auteur: Peter Boon",
											 "programmeur: Peter Boon",
											 "Freudenthal Instituut",
											 "www.fi.uu.nl",""});
		
		as = new AlgebraSchuifVeld(0, 0, getSize().width, getSize().height);
		as.setVisible(false);
		getContentPane().add(as);
		as.setVisible(true);
		as.tekenOpnieuw();
				
		fiButton.setBounds(0,0, 20, 30);
		as.add(fiButton);
	}
	
	public double getScore()
	{	return 0.5;
	}
	
	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
//System.out.println("get state");			
		}
	}

	public void stopSco()
	{	stop();
		api = null;
	}
	
	public String getState()
	{	
		Hashtable h = as.getState();
		
	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(h);
		
		return s;
	}
	
	public void setState(String s)
	{
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		// cast
		Hashtable h = (Hashtable) o;
		
		as.setEditModeState(h);
		
	}
	
	public void start()
	{	Thread pauze = new Thread()
			{	public void run()
				{	try
    				{   sleep(100);
					}
    				catch(InterruptedException e)    
					{ }
					as.tekenOpnieuw();
				}
			};
		pauze.start();
		if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	setState(s);
//System.out.println("set state");			
			}
		}
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
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		if(as!=null)
		{	as.setSize(b,h);
			as.tekenOpnieuw();
		}
	}
	
	public InteractiePanel getInteractiePanel()
    { 	return new AlgebraExprInteractiePanel();	
    }
	
}

	












