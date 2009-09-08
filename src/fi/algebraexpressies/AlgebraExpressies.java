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

/**
 * @author Peter Boon
 */

public class AlgebraExpressies extends Applet implements ScormAppletIF
{	
	private fi.beans.copyright.FIButton fiButton;
	protected static ResourceBundle rb;
	private String langArg;
	
	AlgebraSchuifVeld as;
		
	public static void main(String[] args)    
	{	int width = 780;
        int height = 550;
        fi.beans.mainframe.MainFrame mf = new fi.beans.mainframe.MainFrame(new AlgebraExpressies(), width, height);
		mf.pack();
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);
		
		mf.show();
	}
	
	public void init()
	{	setLayout(null);
				
		String langArg = getParameter("language");
		if ( langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.algebraexpressies.text.Text",language);
		
		fiButton = new fi.beans.copyright.FIButton("Algebra Expressies",new String[]{"","versie-info: 20060911",
											"auteur: Peter Boon",
											"programmeur: Peter Boon",
											"Freudenthal Instituut",
											"www.fi.uu.nl",""});
		as = new AlgebraSchuifVeld(0,0,getSize().width, getSize().height);
		as.setVisible(false);
		add(as);
		as.setVisible(true);
		as.tekenOpnieuw();
				
		fiButton.setBounds(0,0,20,30);
		as.add(fiButton);
	}
	
	public void stopSco()
	{
	}
	
	public String getState()
	{	return null;
	}
	
	public void setState(String s)
	{
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
}

	












