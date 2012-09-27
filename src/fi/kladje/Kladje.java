package fi.kladje;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class Kladje extends JApplet implements WiskOpdrApplet, ScormAppletIF, ActionListener
{
	// taal
	protected static ResourceBundle rb;
	protected static String langArg;
	
	// scorm	
	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	public static void main(String[] args)    
	{	int width = 500;
        int height = 450;
        Kladje kladje = new Kladje();
        kladje.scormed = true;
		ScormMainFrame mf = new ScormMainFrame(kladje, width, height);
		mf.setTitle("Kladje Scormed");
		mf.pack();
		mf.show();

		kladje.setLocation(mf.getInsets().left, mf.getInsets().top);		
		
		int framebreedte = width + mf.getInsets().left + mf.getInsets().right;
		int framehoogte = height + mf.getInsets().top + mf.getInsets().bottom;
		mf.setSize(framebreedte, framehoogte);

	}
	
	protected static Color bgColor = new Color(220, 220, 220);
	
	//Color[] kleuren = {Color.black, Color.white, Color.gray, Color.lightGray, Color.red,     Color.orange,
	//				   Color.yellow,Color.green, Color.cyan, Color.blue,      Color.magenta, Color.pink};

	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
			   		   Color.green, Color.cyan, Color.blue, Color.magenta};
	
	
	KladjeVeld kladjeVeld;
	int bottomHeight = 30;
	int offSet = 5;
	
	JToggleButton tekenButton, gumButton;
	ButtonGroup tekenGumGroup;
	JButton wisButton;
	JButton[] kleurKeuzeButtons;
	
	// parametrisatie
	boolean kleurkeuze = true;
	boolean lijnen = false;
	boolean ruitjes = false;
	
	// copyright
	FIButton fiButton;
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	// plaatjes binnenhalen
	AppletUtil au;	
	Image penDefault, penRollover, penSelected, gumDefault, gumRollover, gumSelected;
	Image tekenCursor, gumCursor;

	

	public Kladje()
	{	
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.kladje.text.Text", language);	
	}
	
	public Kladje(Locale language)
	{	
		langArg = language.getLanguage();
		rb = ResourceBundle.getBundle("fi.kladje.text.Text", language);	
	}
	
	public void init() 
	{	try
		{	api = Scorm.findAPI(this);
		}
		catch(Exception e){}
		
		getContentPane().setLayout(null);

		au = new AppletUtil(this);
		MediaTracker tr = new MediaTracker(this);		
		penDefault  = au.getImage("resources/teken_penknop_default.gif");
		tr.addImage(penDefault, 0);
		penRollover  = au.getImage("resources/teken_penknop_rollover.gif");
		tr.addImage(penRollover, 0);
		penSelected  = au.getImage("resources/teken_penknop_selected.gif");
		tr.addImage(penSelected, 0);
		gumDefault  = au.getImage("resources/teken_gumknop_default.gif");
		tr.addImage(gumDefault, 0);
		gumRollover  = au.getImage("resources/teken_gumknop_rollover.gif");
		tr.addImage(gumRollover, 0);
		gumSelected  = au.getImage("resources/teken_gumknop_selected.gif");
		tr.addImage(gumSelected, 0);
		tekenCursor  = au.getImage("resources/tekencursor.gif");
		tr.addImage(tekenCursor, 0);
		gumCursor  = au.getImage("resources/gumcursor.gif");
		tr.addImage(gumCursor, 0);
	    try
	    {	tr.waitForAll();
	    } 
	    catch(Exception e) {}		

		// fonts	    
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
	
		//instelling taal
		langArg = getParameter("language");
		if (langArg == null) 
			langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.kladje.text.Text",language);
		
		//instelling achtergrondkleur
		String kleurcode = getParameter("bgcolor");
		if (kleurcode != null)
			bgColor = new Color(Integer.parseInt(kleurcode.substring(1), 16));
		getContentPane().setBackground(bgColor);
		
		//parametrisatie
		String kleurkeuzeString = getParameter("kleurkeuze");
		if (kleurkeuzeString != null && (kleurkeuzeString.equals("no") || kleurkeuzeString.equals("false")))
			kleurkeuze = false;	
		String lijnenString = getParameter("lijnen");
		if (lijnenString != null && (lijnenString.equals("yes") || lijnenString.equals("true")))
			lijnen = true;	
		String ruitjesString = getParameter("ruitjes");
		if (ruitjesString != null && (ruitjesString.equals("yes") || ruitjesString.equals("true")))
			ruitjes = true;	
		
		
		//Fi-logo, copyright
		fiButton = new FIButton("Info", new String[]
			{	"Kladje",
				"versie-info: 20120930",
				"auteurs: Peter Boon, Huub Nilwik",
				"programmeur: Huub Nilwik",
				"Freudenthal Instituut",
				"www.fi.uu.nl",
				""
			});
		fiButton.setBounds(getSize().width - offSet - 20, getSize().height - offSet - 25, 20, 30);
		
		getContentPane().add(fiButton);

		tekenGumGroup = new ButtonGroup();

		tekenButton = new JToggleButton(new ImageIcon(penDefault), true);
		tekenButton.setRolloverIcon(new ImageIcon(penRollover));
		tekenButton.setSelectedIcon(new ImageIcon(penSelected));
		tekenButton.setBorder(null);
		tekenButton.setBounds(2 * offSet, getSize().height - offSet - 20, 20, 20);
		getContentPane().add(tekenButton);
		tekenButton.addActionListener(new TekenGumAL());

		gumButton = new JToggleButton(new ImageIcon(gumDefault), false);
		gumButton.setRolloverIcon(new ImageIcon(gumRollover));
		gumButton.setSelectedIcon(new ImageIcon(gumSelected));
		gumButton.setBorder(null);
		gumButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
				            getSize().height - offSet - 20, 20, 20);
		getContentPane().add(gumButton);
		gumButton.addActionListener(new TekenGumAL());
		
		tekenGumGroup.add(tekenButton);
		tekenGumGroup.add(gumButton);
		
		wisButton = new JButton(rb.getString("wisTekst"));
		int width = theFM.stringWidth(rb.getString("wisTekst")) + 35;
		wisButton.setFont(theFont);
		wisButton.setBounds(gumButton.getLocation().x + gumButton.getSize().width + 3 * offSet,
				            getSize().height - 20 - offSet, width, 20);
		getContentPane().add(wisButton);
		wisButton.addActionListener(this);
		
		
		kleurKeuzeButtons = new JButton[kleuren.length];
		for (int i = 0; i < kleuren.length; i++)
		{	final Color buttonColor = kleuren[i];
			kleurKeuzeButtons[i] = new JButton()
			{	public void paintComponent(Graphics g)
				{	g.setColor(buttonColor);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			kleurKeuzeButtons[i].setBounds(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
					                getSize().height - 20 - offSet, 20 , 20);
			getContentPane().add(kleurKeuzeButtons[i]);
			kleurKeuzeButtons[i].addActionListener(this);
			kleurKeuzeButtons[i].setVisible(kleurkeuze);
		}
		
		kladjeVeld = new KladjeVeld(getSize().width - 2 * offSet, getSize().height - offSet - bottomHeight);
		kladjeVeld.setLocation(offSet, offSet);
		
		getContentPane().add(kladjeVeld);
		
		boolean error = false;
		Cursor drawCursor = null;
		try
		{	
			drawCursor = Toolkit.getDefaultToolkit().
				createCustomCursor(tekenCursor,
					new Point(10, 10), "TEKEN_CURSOR");
		}
		catch (IndexOutOfBoundsException ioobe)
		//catch (HeadlessException he)
		{	error = true;
		}
		if (!error)
		{	kladjeVeld.setCursor(drawCursor);
		}
		
		kladjeVeld.zetLijnen(lijnen);
		kladjeVeld.zetRuitjes(ruitjes);
		
	} // init	

	
	class TekenGumAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (tekenButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.tekenen;
				
				boolean error = false;
				Cursor drawCursor = null;
				try
				{	
					drawCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(tekenCursor,
							new Point(10, 10), "TEKEN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(drawCursor);
				}
			}
			
			if (gumButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.gummen;
				
				boolean error = false;
				Cursor deleteCursor = null;
				try
				{	
					deleteCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(gumCursor,
							new Point(10, 10), "GUM_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(deleteCursor);
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < kleurKeuzeButtons.length; i++)
		{	if (e.getSource() == kleurKeuzeButtons[i])
			{	kladjeVeld.zetDrawingColor(i);
			}
		}
		
		if (e.getSource() == wisButton)
		{
			kladjeVeld.wis();
		}

	}
	
	// scormgebeuren

	public void start()
	{	if (api != null)
		{	String s = api.LMSGetValue("cmi.suspend_data");
			if (s != null && !s.equals(""))
			{	setState(s);
//System.out.println("api setState");			
			}
		}
		else
		{
//System.out.println("api not setState");			
		}
	}
	
	public void stopSco()
	{	stop();
		api = null;
	}

	public void stop()
	{	if (api != null)
		{	String s = getState();
			String d = new Double(getScore()).toString();
			api.LMSSetValue("cmi.core.score.raw",d);
			api.LMSSetValue("cmi.suspend_data",s);
		}
	}

	public void setState(String s)
	{	
		
System.out.println("Kladje setState");		
		
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		
		Hashtable gegevens = (Hashtable) o;
		
		Vector stateVector = new Vector();
		
		if (gegevens.containsKey("pixels"))
			stateVector = (Vector) gegevens.get("pixels");
		
		if (stateVector.size() > 0)
			kladjeVeld.setState(stateVector);
	}

	public String getState()
	{	
System.out.println("Kladje getState");

		//Object gegevens = new Object();
		Hashtable gegevens = new Hashtable();
		
		Vector stateVector = kladjeVeld.getState();
		
		gegevens.put("pixels", stateVector);
		
	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(gegevens);

	    return s;
	}

	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{	
		return new KladjeInteractiePanel();
	}
	

	public boolean hasEditMode()
	{	return false;
	}

    public ScormEditComponentIF getEditComponent(Hashtable launchdata)
    {	return null;
    }
    
    public Parameter[] getEditableParameters()
	{	
    	Parameter[] parameters = new Parameter[3];
		
		DataType type = new ScormString();
		DataType btype = new ScormBoolean();
		
		Parameter param = new Parameter("kleurkeuze", "Optie kleurkeuze", btype);
		parameters[0] = param;
		
		param = new Parameter("lijnen", "kladje met lijnen", btype);
		parameters[1] = param;
		
		param = new Parameter("ruitjes", "kladje met ruitjes", btype);
		parameters[2] = param;
		
		return parameters;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }


}
