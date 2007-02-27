package fi.geomalgebra;

import java.awt.Polygon;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import fi.geomalgebra.text.*;
import java.applet.Applet;

import fi.beans.mainframe.*;
import fi.beans.copyright.*;
import fi.beans.scorm.*;

/**
 * @author Peter Boon
 */

public class GeomAlgebra extends Applet implements  ScormAppletIF, ActionListener
{	
	private FIButton fiButton;
	private int breedte, hoogte;
	private Image im ;
  	private Graphics gIm ;
	
	protected static ResourceBundle rb;
	private ControlPanel cp;
	private LineaalHor lh;
	private LineaalVer lv;
	private AlgebraVeld av;
	private String langArg;
	 
	public static void main(String[] args)    
	{	int width = 800;
        int height = 600;
		MainFrame mf = new MainFrame(new GeomAlgebra(),width, height);
		mf.setTitle("Geometrische Algebra 2d");
		mf.pack();
		mf.show();
		mf.setSize(width, height);
	}
	
	public void init()
	{	hoogte = getSize().height;
		breedte = getSize().width;
		
		setLayout(null);
		
		String langArg = getParameter("language");
		if (langArg == null) langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.geomalgebra.text.Text",language);
		
		av = new AlgebraVeld(breedte,hoogte);
		av.setLocation(0,0);
		add(av);
		
		
		
		cp = new ControlPanel(av);
		cp.setLayout(null);
		cp.setBounds(1,hoogte-41,breedte-2,40);
		add(cp);
		
		lh = new LineaalHor(breedte, hoogte);
		lh.addActionListener(this);
		add(lh);
		lv = new LineaalVer(breedte, hoogte);
		lv.addActionListener(this);
		add(lv);
		
		fiButton = new FIButton("Geometrische Algebra",new String[]{"","versie-info: 20050815",
													"auteurs: Gerard Koolstra, Peter Boon",
													"          en Martin Kindt",
													"programmeur: Peter Boon",
													"Freudenthal Instituut",
													"www.fi.uu.nl",""});
		fiButton.setBounds(2,18,15,20);
		cp.add(fiButton);
	}
	
	public String getState()
	{	return null;
	}
    
    public void setState(String state)
    {
    }
    
    public void stopSco()
    {
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
	
	public void actionPerformed(ActionEvent e)
	{	String command = e.getActionCommand();
		int modifier = e.getModifiers();
		if(command.equals("maakBasis"))
		{	av.zetBasis(modifier);
		}
	}
}

