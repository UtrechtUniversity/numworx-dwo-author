package fi.kladje;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import fi.beans.appletutil.AppletUtil;
import fi.beans.copyright.*;
import fi.beans.scorm.*;
import fi.beans.base64code.*;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

/**
 * Kladje widget.
 * 
 * @author Peter Boon
 *
 */

public class Kladje extends fi.beans.mainframe.JApplet implements WiskOpdrApplet, ScormAppletIF, ActionListener
{
	// taal
	protected static ResourceBundle rb;
	protected static String langArg;
	
	// scorm	
	protected SCORM12APIInterface api;
	boolean scormed = false;
	boolean reviewMode = false;	
	
	public static boolean isExperimental;
	public static boolean isPremium;
	
	public static Color colorBlue1 = new Color(49,71,112);
	public static Color colorBlue2 = new Color(38,115,182);
	public static Color colorBlue3 = new Color(120,150,202);
	public static Color colorBlue4 = new Color(180,195,228);
	public static Color colorBlue5 = new Color(211,229,244);
	public static Color colorBlue6 = new Color(229,240,249);
	
	public static Color colorGray1 = new Color(206,207,208);
	public static Color colorGray2 = new Color(221,223,225);
	public static Color colorGray3 = new Color(237,239,241);
	
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
	
	JToggleButton tekenButton, lijnButton, rechthoekButton, cirkelButton, tekstButton, selecterenButton;
	ButtonGroup tekenGumGroup;
	JButton undoButton, wisButton;
	//JButton[] kleurKeuzeButtons;
	JToggleButton[] kleurKeuzeButtons;
	ButtonGroup kleurKeuzeGroup;
	
	// parametrisatie
	boolean kleurkeuze = true;
	boolean lijnen = false;
	boolean ruitjes = false;
	boolean lijnTekenen = true;
	boolean rechthoekTekenen = true;
	boolean cirkelTekenen = true;
	boolean tekstTekenen = true;
	
	// copyright
	FIButton fiButton;
	
	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;

	// plaatjes binnenhalen
	AppletUtil au;	
	Image penDefault, penRollover, penSelected, gumDefault, gumRollover, gumSelected,
		  lijnDefault, lijnRollover, lijnSelected, rechthoekDefault, rechthoekRollover, rechthoekSelected,
		  cirkelDefault, cirkelRollover, cirkelSelected, tekstDefault, tekstRollover, tekstSelected,
		  selecterenDefault, selecterenRollover, selecterenSelected;
	Image tekenCursor, gumCursor, lijnCursor, rechthoekCursor, cirkelCursor, tekstCursor, selecterenCursor;


	public Kladje()
	{	
		langArg = "nl";
		Locale language = new Locale (langArg, "");
		rb = ResourceBundle.getBundle("fi.kladje.text.Text", language);	
		//isExperimental = isExperimental();  WONT WORK: no setStub yet!
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
	
        setEnvironment(); // setStub is nu gezet.
		
		System.out.println(Color.orange.toString());		
		
		getContentPane().setLayout(null);

		au = new AppletUtil(this);
		MediaTracker tr = new MediaTracker(this);
		
//		penDefault  = au.getImage("resources/teken_penknop_default.gif");
//		tr.addImage(penDefault, 0);
//		penRollover  = au.getImage("resources/teken_penknop_rollover.gif");
//		tr.addImage(penRollover, 0);
//		penSelected  = au.getImage("resources/teken_penknop_selected.gif");
//		tr.addImage(penSelected, 0);
//		
//		gumDefault  = au.getImage("resources/teken_gumknop_default.gif");
//		tr.addImage(gumDefault, 0);
//		gumRollover  = au.getImage("resources/teken_gumknop_rollover.gif");
//		tr.addImage(gumRollover, 0);
//		gumSelected  = au.getImage("resources/teken_gumknop_selected.gif");
//		tr.addImage(gumSelected, 0);
//		
//		lijnDefault  = au.getImage("resources/teken_lijn_default.gif");
//		tr.addImage(lijnDefault, 0);
//		lijnRollover  = au.getImage("resources/teken_lijn_rollover.gif");
//		tr.addImage(lijnRollover, 0);
//		lijnSelected  = au.getImage("resources/teken_lijn_selected.gif");
//		tr.addImage(lijnSelected, 0);
//		
//		rechthoekDefault  = au.getImage("resources/teken_rechthoek_default.gif");
//		tr.addImage(rechthoekDefault, 0);
//		rechthoekRollover  = au.getImage("resources/teken_rechthoek_rollover.gif");
//		tr.addImage(rechthoekRollover, 0);
//		rechthoekSelected  = au.getImage("resources/teken_rechthoek_selected.gif");
//		tr.addImage(rechthoekSelected, 0);
//		
//		cirkelDefault  = au.getImage("resources/teken_cirkel_default.gif");
//		tr.addImage(cirkelDefault, 0);
//		cirkelRollover  = au.getImage("resources/teken_cirkel_rollover.gif");
//		tr.addImage(cirkelRollover, 0);
//		cirkelSelected  = au.getImage("resources/teken_cirkel_selected.gif");
//		tr.addImage(cirkelSelected, 0);
//				
//		tekstDefault  = au.getImage("resources/teken_tekst_default.gif");
//		tr.addImage(tekstDefault, 0);
//		tekstRollover  = au.getImage("resources/teken_tekst_rollover.gif");
//		tr.addImage(tekstRollover, 0);
//		tekstSelected  = au.getImage("resources/teken_tekst_selected.gif");
//		tr.addImage(tekstSelected, 0);
//		
//		selecterenDefault  = au.getImage("resources/teken_selecteren_default.gif");
//		tr.addImage(selecterenDefault, 0);
//		selecterenRollover  = au.getImage("resources/teken_selecteren_rollover.gif");
//		tr.addImage(selecterenRollover, 0);
//		selecterenSelected  = au.getImage("resources/teken_selecteren_selected.gif");
//		tr.addImage(selecterenSelected, 0);
		
		penDefault  = au.getImage("resources/teken_penknop_up.png");
		tr.addImage(penDefault, 0);
		penRollover  = au.getImage("resources/teken_penknop_up.png");
		tr.addImage(penRollover, 0);
		penSelected  = au.getImage("resources/teken_penknop_down.png");
		tr.addImage(penSelected, 0);
		
		gumDefault  = au.getImage("resources/teken_gumknop_up.png");
		tr.addImage(gumDefault, 0);
		gumRollover  = au.getImage("resources/teken_gumknop_up.png");
		tr.addImage(gumRollover, 0);
		gumSelected  = au.getImage("resources/teken_gumknop_down.png");
		tr.addImage(gumSelected, 0);
		
		lijnDefault  = au.getImage("resources/teken_lijn_up.png");
		tr.addImage(lijnDefault, 0);
		lijnRollover  = au.getImage("resources/teken_lijn_up.png");
		tr.addImage(lijnRollover, 0);
		lijnSelected  = au.getImage("resources/teken_lijn_down.png");
		tr.addImage(lijnSelected, 0);
		
		rechthoekDefault  = au.getImage("resources/teken_rechthoek_up.png");
		tr.addImage(rechthoekDefault, 0);
		rechthoekRollover  = au.getImage("resources/teken_rechthoek_up.png");
		tr.addImage(rechthoekRollover, 0);
		rechthoekSelected  = au.getImage("resources/teken_rechthoek_down.png");
		tr.addImage(rechthoekSelected, 0);
		
		cirkelDefault  = au.getImage("resources/teken_cirkel_up.png");
		tr.addImage(cirkelDefault, 0);
		cirkelRollover  = au.getImage("resources/teken_cirkel_up.png");
		tr.addImage(cirkelRollover, 0);
		cirkelSelected  = au.getImage("resources/teken_cirkel_down.png");
		tr.addImage(cirkelSelected, 0);
				
		tekstDefault  = au.getImage("resources/teken_tekst_up.png");
		tr.addImage(tekstDefault, 0);
		tekstRollover  = au.getImage("resources/teken_tekst_up.png");
		tr.addImage(tekstRollover, 0);
		tekstSelected  = au.getImage("resources/teken_tekst_down.png");
		tr.addImage(tekstSelected, 0);
		
		selecterenDefault  = au.getImage("resources/teken_selecteren_up.png");
		tr.addImage(selecterenDefault, 0);
		selecterenRollover  = au.getImage("resources/teken_selecteren_up.png");
		tr.addImage(selecterenRollover, 0);
		selecterenSelected  = au.getImage("resources/teken_selecteren_down.png");
		tr.addImage(selecterenSelected, 0);
		
		tekenCursor  = au.getImage("resources/tekencursor.gif");
		//tekenCursor  = au.getImage("resources/teken_pen_cursor.gif");
		tr.addImage(tekenCursor, 0);
		gumCursor  = au.getImage("resources/gumcursor.gif");
		//gumCursor  = au.getImage("resources/teken_gum_cursor.gif");
		tr.addImage(gumCursor, 0);
		
		lijnCursor  = au.getImage("resources/teken_lijn_cursor.gif");
		tr.addImage(lijnCursor, 0);
		rechthoekCursor  = au.getImage("resources/teken_rechthoek_cursor.gif");
		tr.addImage(rechthoekCursor, 0);
		cirkelCursor  = au.getImage("resources/teken_cirkel_cursor.gif");
		tr.addImage(cirkelCursor, 0);
		tekstCursor  = au.getImage("resources/teken_tekst_cursor.gif");
		tr.addImage(tekstCursor, 0);
		selecterenCursor  = au.getImage("resources/teken_selecteren_cursor.gif");
		tr.addImage(selecterenCursor, 0);

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
		
		if (lijnen && ruitjes)
			lijnen = false;
		
		String lijnTekenenString = getParameter("lijntekenen");
		if (lijnTekenenString != null && (lijnTekenenString.equals("no") || lijnTekenenString.equals("false")))
			lijnTekenen = false;	
		String rechthoekTekenenString = getParameter("rechthoektekenen");
		if (rechthoekTekenenString != null && (rechthoekTekenenString.equals("no") || rechthoekTekenenString.equals("false")))
			rechthoekTekenen = false;	
		String cirkelTekenenString = getParameter("cirkeltekenen");
		if (cirkelTekenenString != null && (cirkelTekenenString.equals("no") || cirkelTekenenString.equals("false")))
			cirkelTekenen = false;	
		String tekstTekenenString = getParameter("teksttekenen");
		if (tekstTekenenString != null && (tekstTekenenString.equals("no") || tekstTekenenString.equals("false")))
			tekstTekenen = false;
		
		
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
/*
		gumButton = new JToggleButton(new ImageIcon(gumDefault), false);
		gumButton.setRolloverIcon(new ImageIcon(gumRollover));
		gumButton.setSelectedIcon(new ImageIcon(gumSelected));
		gumButton.setBorder(null);
		gumButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
				            getSize().height - offSet - 20, 20, 20);
		getContentPane().add(gumButton);
		gumButton.addActionListener(new TekenGumAL());
*/		
		//lijnButton = new JToggleButton("/");
		lijnButton = new JToggleButton(new ImageIcon(lijnDefault), false);
		lijnButton.setRolloverIcon(new ImageIcon(lijnRollover));
		lijnButton.setSelectedIcon(new ImageIcon(lijnSelected));
		lijnButton.setBorder(null);
		lijnButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	                        getSize().height - offSet - 20, 20, 20);
		getContentPane().add(lijnButton);
		lijnButton.addActionListener(new TekenGumAL());
		
		//rechthoekButton = new JToggleButton("#");
		rechthoekButton = new JToggleButton(new ImageIcon(rechthoekDefault), false);
		rechthoekButton.setRolloverIcon(new ImageIcon(rechthoekRollover));
		rechthoekButton.setSelectedIcon(new ImageIcon(rechthoekSelected));
		rechthoekButton.setBorder(null);
		rechthoekButton.setBounds(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	                              getSize().height - offSet - 20, 20, 20);
		getContentPane().add(rechthoekButton);
		rechthoekButton.addActionListener(new TekenGumAL());

		//cirkelButton = new JToggleButton("o");
		cirkelButton = new JToggleButton(new ImageIcon(cirkelDefault), false);
		cirkelButton.setRolloverIcon(new ImageIcon(cirkelRollover));
		cirkelButton.setSelectedIcon(new ImageIcon(cirkelSelected));
		cirkelButton.setBorder(null);
		cirkelButton.setBounds(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
	                           getSize().height - offSet - 20, 20, 20);
		getContentPane().add(cirkelButton);
		cirkelButton.addActionListener(new TekenGumAL());
		
		tekstButton = new JToggleButton(new ImageIcon(tekstDefault), false);
		tekstButton.setRolloverIcon(new ImageIcon(tekstRollover));
		tekstButton.setSelectedIcon(new ImageIcon(tekstSelected));
		tekstButton.setBorder(null);
		tekstButton.setBounds(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
	                          getSize().height - offSet - 20, 20, 20);
		getContentPane().add(tekstButton);
		tekstButton.addActionListener(new TekenGumAL());
		
		selecterenButton = new JToggleButton(new ImageIcon(selecterenDefault), false);
		selecterenButton.setRolloverIcon(new ImageIcon(selecterenRollover));
		selecterenButton.setSelectedIcon(new ImageIcon(selecterenSelected));
		selecterenButton.setBorder(null);
		selecterenButton.setBounds(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
	                               getSize().height - offSet - 20, 20, 20);
		getContentPane().add(selecterenButton);
		selecterenButton.addActionListener(new TekenGumAL());
		
		
		tekenGumGroup.add(tekenButton);
//		tekenGumGroup.add(gumButton);
		tekenGumGroup.add(lijnButton);
		tekenGumGroup.add(rechthoekButton);
		tekenGumGroup.add(cirkelButton);
		tekenGumGroup.add(tekstButton);
		tekenGumGroup.add(selecterenButton);

		undoButton = new JButton(rb.getString("terugTekst"));
		int width = theFM.stringWidth(rb.getString("terugTekst")) + 35;
		undoButton.setFont(theFont);
		undoButton.setBounds(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
				             getSize().height - 20 - offSet, width, 20);
		getContentPane().add(undoButton);
		undoButton.addActionListener(this);
		
		wisButton = new JButton(rb.getString("wisTekst"));
		width = theFM.stringWidth(rb.getString("wisTekst")) + 35;
		wisButton.setFont(theFont);
		wisButton.setBounds(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
				            getSize().height - 20 - offSet, width, 20);
		getContentPane().add(wisButton);
		wisButton.addActionListener(this);
		
		kleurKeuzeGroup = new ButtonGroup();
		//kleurKeuzeButtons = new JButton[kleuren.length];
		kleurKeuzeButtons = new JToggleButton[kleuren.length];
		for (int i = 0; i < kleuren.length; i++)
		{	final Color buttonColor = kleuren[i];
			//kleurKeuzeButtons[i] = new JButton()
			kleurKeuzeButtons[i] = new JToggleButton()
			{	public void paintComponent(Graphics g)
				{	//g.setColor(buttonColor);
					if (isSelected())
					{	g.setColor(buttonColor);
						g.fillRect(0, 0, getWidth(), getHeight());
						//g.setColor(buttonColor);
						//g.fillRect(3, 3, getWidth() - 6, getHeight() - 6);
						g.setColor(Color.white);
						g.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 2);
						g.drawLine(0, getHeight() - 2, getWidth() - 2, getHeight() - 2);
						g.drawLine(0, 1, getWidth() - 1, 1);
						g.drawLine(1, 0, 1, getHeight() - 1);
						
					}
					else
					{	g.setColor(buttonColor);
						g.fillRect(0, 0, getWidth(), getHeight());
					}	
				}
			};
			kleurKeuzeGroup.add(kleurKeuzeButtons[i]);
			
			kleurKeuzeButtons[i].setBounds(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
					                getSize().height - 20 - offSet, 20 , 20);
			getContentPane().add(kleurKeuzeButtons[i]);
			kleurKeuzeButtons[i].addActionListener(this);
			kleurKeuzeButtons[i].setVisible(kleurkeuze);
		}
		kleurKeuzeButtons[0].setSelected(true);
		
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
		kladjeVeld.zetRuitjes(ruitjes,20);
		
		layoutBottom();
		
//System.out.println("pcs = " + Toolkit.getDefaultToolkit().getBestCursorSize(24, 24));		
		
	} // init	

	public void layoutBottom()
	{
		if (lijnTekenen && rechthoekTekenen && cirkelTekenen && tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			                    getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			tekstButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
		                            getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
		}
		else if (!lijnTekenen && rechthoekTekenen && cirkelTekenen && tekstTekenen)
		{	
			rechthoekButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			                    getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			tekstButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
								    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                         				 getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (lijnTekenen && !rechthoekTekenen && cirkelTekenen && tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			cirkelButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			tekstButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
					//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}
		else if (lijnTekenen && rechthoekTekenen && !cirkelTekenen && tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			  getSize().height - offSet - 20);
			tekstButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);

			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (lijnTekenen && rechthoekTekenen && cirkelTekenen && !tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			  getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);

			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		
		else if (!lijnTekenen && !rechthoekTekenen && cirkelTekenen && tekstTekenen)
		{	
			cirkelButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			tekstButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			

		}
		else if (!lijnTekenen && rechthoekTekenen && !cirkelTekenen && tekstTekenen)
		{
			rechthoekButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	       			                    getSize().height - offSet - 20);
			tekstButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
		}
		else if (!lijnTekenen && rechthoekTekenen && cirkelTekenen && !tekstTekenen)
		{
			rechthoekButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
	       			                    getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
		}
		
		else if (lijnTekenen && !rechthoekTekenen && !cirkelTekenen && tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                       			   getSize().height - offSet - 20);
			tekstButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}
		
		else if (lijnTekenen && !rechthoekTekenen && cirkelTekenen && !tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                       			   getSize().height - offSet - 20);
			cirkelButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}
		else if (lijnTekenen && rechthoekTekenen && !cirkelTekenen && !tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                       			   getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
                         getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}

		else if (lijnTekenen && !rechthoekTekenen && !cirkelTekenen && !tekstTekenen)
		{
			lijnButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (!lijnTekenen && rechthoekTekenen && !cirkelTekenen && !tekstTekenen)
		{
			rechthoekButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (!lijnTekenen && !rechthoekTekenen && cirkelTekenen && !tekstTekenen)
		{
			cirkelButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		
		else if (!lijnTekenen && !rechthoekTekenen && !cirkelTekenen && tekstTekenen)
		{
			tekstButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			selecterenButton.setLocation(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		
		else if (!lijnTekenen && !rechthoekTekenen && !cirkelTekenen && !tekstTekenen)
		{
			selecterenButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
                    getSize().height - offSet - 20);
			undoButton.setLocation(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		
		
		lijnButton.setVisible(lijnTekenen);
		rechthoekButton.setVisible(rechthoekTekenen);
		cirkelButton.setVisible(cirkelTekenen);
		tekstButton.setVisible(tekstTekenen);

		
	}
	
	class TekenGumAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			if (tekenButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.tekenen;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.repaint();
				
				boolean error = false;
				Cursor drawCursor = null;
				try
				{	
					drawCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(tekenCursor,
							new Point(10, 10), "TEKEN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(drawCursor);
				}
			}
/*			
			else if (gumButton.isSelected())
			{
				//kladjeVeld.mouseMode = kladjeVeld.gummen;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.mouseMode = kladjeVeld.gummen;
				kladjeVeld.repaint();
				
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
*/			
			else if (lijnButton.isSelected())
			{
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.mouseMode = kladjeVeld.lijnTekenen;
				kladjeVeld.repaint();
				
				boolean error = false;
				Cursor lineCursor = null;
				try
				{	
					lineCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(lijnCursor,
							new Point(10, 10), "LIJN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(lineCursor);
				}
			}
			
			else if (rechthoekButton.isSelected())
			{
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.mouseMode = kladjeVeld.rechthoekTekenen;
				kladjeVeld.repaint();
				
				boolean error = false;
				Cursor rectangleCursor = null;
				try
				{	
					rectangleCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(rechthoekCursor,
							new Point(10, 10), "RECHTHOEK_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(rectangleCursor);
				}
			}
			
			else if (cirkelButton.isSelected())
			{
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.mouseMode = kladjeVeld.cirkelTekenen;
				kladjeVeld.repaint();
				
				
				boolean error = false;
				Cursor circleCursor = null;
				try
				{	
					circleCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(cirkelCursor,
							new Point(10, 10), "CIRKEL_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(circleCursor);
				}
			}

			else if (tekstButton.isSelected())
			{
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.mouseMode = kladjeVeld.tekstTekenen;
				kladjeVeld.repaint();
				
				boolean error = false;
				Cursor textCursor = null;
				try
				{	
					textCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(tekstCursor,
							new Point(10, 10), "TEKST_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.textCursor = textCursor;
					kladjeVeld.setCursor(textCursor);
				}
			}

			else if (selecterenButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.selecteren;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.selecteerRechthoek = null;
				kladjeVeld.resetSelectedObject();
				kladjeVeld.resetSelectedObjects();
				kladjeVeld.repaint();
				
				boolean error = false;
				Cursor selectCursor = null;
				try
				{	
					selectCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(selecterenCursor,
							new Point(10, 10), "SELECTEREN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.selectCursor = selectCursor;
					kladjeVeld.setCursor(selectCursor);
				}
			}
			
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < kleurKeuzeButtons.length; i++)
		{	if (e.getSource() == kleurKeuzeButtons[i])
			{	
				kladjeVeld.zetDrawingColor(i);
			}
		}
		
		if (e.getSource() == wisButton)
		{
			kladjeVeld.wis(true);
		}
		
		if (e.getSource() == undoButton)
		{
			kladjeVeld.undo();
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
		
//System.out.println("Kladje setState");		
		
		// decodeer de string
		Object o = StringCodeObject.decodeStringToObject(s);
		
		Hashtable gegevens = (Hashtable) o;
		
		kladjeVeld.setState(gegevens,true);
		
	}

	public String getState()
	{	
//System.out.println("Kladje getState");

		Hashtable gegevens = kladjeVeld.getState();
		
	    // codeer deze gegevens tot een string
	    String s = StringCodeObject.encodeObjectToString(gegevens);

	    return s;
	}

	public double getScore()
	{	return 0.5;
	}
	
	public InteractiePanel getInteractiePanel()
	{
	  setEnvironment(); // setStub is nu gezet.
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
    	Parameter[] parameters = new Parameter[7];
		
		DataType type = new ScormString();
		DataType btype = new ScormBoolean();
		
		Parameter param = new Parameter("kleurkeuze", "Optie kleurkeuze", btype);
		parameters[0] = param;
		
		param = new Parameter("lijnen", "kladje met lijnen", btype);
		parameters[1] = param;
		
		param = new Parameter("ruitjes", "kladje met ruitjes", btype);
		parameters[2] = param;
		
		param = new Parameter("lijntekenen", "optie lijnen tekenen", btype);
		parameters[3] = param;
		
		param = new Parameter("rechthoektekenen", "optie rechthoeken tekenen", btype);
		parameters[4] = param;
		
		param = new Parameter("cirkeltekenen", "optie cirkels tekenen", btype);
		parameters[5] = param;
		
		param = new Parameter("teksttekenen", "optie tekst tekenen", btype);
		parameters[6] = param;

		
		return parameters;
    }

    public Parameter[] getAllParameters()
    {	return null;
    }

    
    /**
     * Bepaal of we in test modus draaien.
     * De parameter "dwo_env" is "test".
     * Bepaal of we in premium modus draaien.
     * De parameter "abo_type" is "premium".
     * Nota Bene, setStub moet gezet zijn, anders NPE in getParameter
     * @see java.applet.Applet#getParameter(String)
     */
    private void setEnvironment() {
    		isExperimental = "test".equals(getParameter("dwo_env"));
    		isPremium = "premium".equals(getParameter("abo_type"));    		
    		System.out.println("features: " + isExperimental + ", " + isPremium);
    }

}
