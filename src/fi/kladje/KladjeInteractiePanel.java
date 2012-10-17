package fi.kladje;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import fi.beans.wiskopdrbeans.WiskOpdrApplet;
// deze moet vanwege interface WiskOpdrApplet
import fi.beans.wiskopdrbeans.InteractiePanel;
// deze moet vanwege interface InteractiePanel
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.kladje.Kladje.TekenGumAL;

public class KladjeInteractiePanel extends JPanel implements InteractiePanel, InteractieEditPanel,
							                                ActionListener
									
{
	Image penDefault, penRollover, penSelected, gumDefault, gumRollover, gumSelected;
	ImageIcon penDefaultIcon, penRolloverIcon, penSelectedIcon, gumDefaultIcon, gumRolloverIcon, gumSelectedIcon;
	Image tekenCursor, gumCursor;
	ImageIcon tekenCursorIcon, gumCursorIcon; 
	
	Image lijnDefault, lijnRollover, lijnSelected, rechthoekDefault, rechthoekRollover, rechthoekSelected,
		  cirkelDefault, cirkelRollover, cirkelSelected;
	ImageIcon lijnDefaultIcon, lijnRolloverIcon, lijnSelectedIcon, rechthoekDefaultIcon, rechthoekRolloverIcon, rechthoekSelectedIcon,
			  cirkelDefaultIcon, cirkelRolloverIcon, cirkelSelectedIcon;
	Image lijnCursor, rechthoekCursor, cirkelCursor;
	ImageIcon lijnCursorIcon, rechthoekCursorIcon, cirkelCursorIcon; 
	
	protected static Color bgColor = new Color(220, 220, 220);
	
//	Color[] kleuren = {Color.black, Color.white, Color.gray, Color.lightGray, Color.red,     Color.orange,
//			   Color.yellow,Color.green, Color.cyan, Color.blue,      Color.magenta, Color.pink};

	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
	                   Color.green, Color.cyan, Color.blue, Color.magenta};
	
	KladjeVeld kladjeVeld;
	int bottomHeight = 30;
	int offSet = 5;
	
	JToggleButton tekenButton, gumButton, lijnButton, rechthoekButton, cirkelButton;
	ButtonGroup tekenGumGroup;
	JButton undoButton, wisButton;
	JButton[] kleurKeuzeButtons;

	// fonts
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int score = 0;
	int scoreMax = 10;
	
	
	boolean noSetBounds = false;
	
	boolean kleurkeuze = true;
	boolean lijnen = false;
	boolean ruitjes = false;
	boolean lijnTekenen = true;
	boolean rechthoekTekenen = true;
	boolean cirkelTekenen = true;
	
	
	public KladjeInteractiePanel()
	{
		setLayout(null);
		setBackground(bgColor);
		// echte initiatie vind pas plaats na setBounds
		
		java.net.URL imageURL = Kladje.class.getResource("resources/teken_penknop_default.gif");
		if (imageURL != null) 
		{
			penDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_penknop_rollover.gif");
		if (imageURL != null) 
		{
			penRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_penknop_selected.gif");
		if (imageURL != null) 
		{
			penSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_penknop_selected.gif");
		}

		imageURL = Kladje.class.getResource("resources/teken_gumknop_default.gif");
		if (imageURL != null) 
		{
			gumDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_gumknop_rollover.gif");
		if (imageURL != null) 
		{
			gumRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_gumknop_selected.gif");
		if (imageURL != null) 
		{
			gumSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_gumknop_selected.gif");
		}
		
		
		imageURL = Kladje.class.getResource("resources/lijnteken_default.gif");
		if (imageURL != null) 
		{
			lijnDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading lijnteken_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/lijnteken_rollover.gif");
		if (imageURL != null) 
		{
			lijnRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading lijnteken_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/lijnteken_selected.gif");
		if (imageURL != null) 
		{
			lijnSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading lijnteken_selected.gif");
		}
		

		imageURL = Kladje.class.getResource("resources/rechthoekteken_default.gif");
		if (imageURL != null) 
		{
			rechthoekDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading rechthoekteken_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/rechthoekteken_rollover.gif");
		if (imageURL != null) 
		{
			rechthoekRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading rechthoekteken_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/rechthoekteken_selected.gif");
		if (imageURL != null) 
		{
			rechthoekSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading rechthoekteken_selected.gif");
		}

		imageURL = Kladje.class.getResource("resources/cirkelteken_default.gif");
		if (imageURL != null) 
		{
			cirkelDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading cirkelteken_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/cirkelteken_rollover.gif");
		if (imageURL != null) 
		{
			cirkelRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading cirkelteken_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/cirkelteken_selected.gif");
		if (imageURL != null) 
		{
			cirkelSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading cirkelteken_selected.gif");
		}
		
		imageURL = Kladje.class.getResource("resources/tekencursor.gif");
		if (imageURL != null) 
		{
			tekenCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading tekencursor.gif");
		}
		imageURL = Kladje.class.getResource("resources/gumcursor.gif");
		if (imageURL != null) 
		{
			gumCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading gumcursor.gif");
		}

		imageURL = Kladje.class.getResource("resources/lijncursor.gif");
		if (imageURL != null) 
		{
			lijnCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading lijncursor.gif");
		}

		imageURL = Kladje.class.getResource("resources/rechthoekcursor.gif");
		if (imageURL != null) 
		{
			rechthoekCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading rechthoekcursor.gif");
		}
		
		imageURL = Kladje.class.getResource("resources/cirkelcursor.gif");
		if (imageURL != null) 
		{
			cirkelCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading cirkelcursor.gif");
		}
		
		penDefault = penDefaultIcon.getImage();
		penRollover = penRolloverIcon.getImage();
		penSelected = penSelectedIcon.getImage();
		gumDefault = gumDefaultIcon.getImage();
		gumRollover = gumRolloverIcon.getImage();
		gumSelected = gumSelectedIcon.getImage();
		if (lijnDefaultIcon != null)
			lijnDefault = lijnDefaultIcon.getImage();
		if (lijnRolloverIcon != null)
			lijnRollover = lijnRolloverIcon.getImage();
		if (lijnSelectedIcon != null)
			lijnSelected = lijnSelectedIcon.getImage();
		if (rechthoekDefaultIcon != null)
			rechthoekDefault = rechthoekDefaultIcon.getImage();
		if (rechthoekRolloverIcon != null)
			rechthoekRollover = rechthoekRolloverIcon.getImage();
		if (rechthoekSelectedIcon != null)
			rechthoekSelected = rechthoekSelectedIcon.getImage();
		if (cirkelDefaultIcon != null)
			cirkelDefault = cirkelDefaultIcon.getImage();
		if (cirkelRolloverIcon != null)
			cirkelRollover = cirkelRolloverIcon.getImage();
		if (cirkelSelectedIcon != null)
			cirkelSelected = cirkelSelectedIcon.getImage();
		
		
		tekenCursor = tekenCursorIcon.getImage();
		gumCursor = gumCursorIcon.getImage();
		if (lijnCursorIcon != null)
			lijnCursor = lijnCursorIcon.getImage();
		if (rechthoekCursorIcon != null)
			rechthoekCursor = rechthoekCursorIcon.getImage();
		if (cirkelCursorIcon != null)
			cirkelCursor = cirkelCursorIcon.getImage();
				
		
		
		// fonts	    
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(bgColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
	}
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues)
	{
		boolean kleurkeuze = true;
		if (b.containsKey("kleurkeuze"))
			kleurkeuze = ((Boolean) b.get("kleurkeuze")).booleanValue();
		zetKleurkeuze(kleurkeuze);
		boolean lijnen = false;
		if (b.containsKey("lijnen"))
			lijnen = ((Boolean) b.get("lijnen")).booleanValue();
		zetLijnen(lijnen);
		boolean ruitjes = false;
		if (b.containsKey("ruitjes"))
			ruitjes = ((Boolean) b.get("ruitjes")).booleanValue();
		zetRuitjes(ruitjes);

		boolean lijnTekenen = true;
		if (b.containsKey("lijnTekenen"))
			lijnTekenen = ((Boolean) b.get("lijnTekenen")).booleanValue();
		zetLijnTekenen(lijnTekenen);
		boolean rechthoekTekenen = true;
		if (b.containsKey("rechthoekTekenen"))
			rechthoekTekenen = ((Boolean) b.get("rechthoekTekenen")).booleanValue();
		zetRechthoekTekenen(rechthoekTekenen);
		boolean cirkelTekenen = true;
		if (b.containsKey("cirkelTekenen"))
			cirkelTekenen = ((Boolean) b.get("cirkelTekenen")).booleanValue();
		zetCirkelTekenen(cirkelTekenen);
		
		Vector stateVector = new Vector();
		if (b.containsKey("pixels"))
			stateVector = (Vector) b.get("pixels");
		if (stateVector.size() > 0)
			kladjeVeld.setState(stateVector);
		
	}
	
	public void setState(Hashtable b)
	{
		Vector stateVector = new Vector();
		if (b.containsKey("pixels"))
			stateVector = (Vector) b.get("pixels");
		if (stateVector.size() > 0)
			kladjeVeld.setState(stateVector);

	}
	
	public void setEditState(Hashtable b)
	{
		boolean kleurkeuze = true;
		if (b.containsKey("kleurkeuze"))
			kleurkeuze = ((Boolean) b.get("kleurkeuze")).booleanValue();
		zetKleurkeuze(kleurkeuze);
		boolean lijnen = false;
		if (b.containsKey("lijnen"))
			lijnen = ((Boolean) b.get("lijnen")).booleanValue();
		zetLijnen(lijnen);
		boolean ruitjes = false;
		if (b.containsKey("ruitjes"))
			ruitjes = ((Boolean) b.get("ruitjes")).booleanValue();
		zetRuitjes(ruitjes);

		boolean lijnTekenen = true;
		if (b.containsKey("lijnTekenen"))
			lijnTekenen = ((Boolean) b.get("lijnTekenen")).booleanValue();
		zetLijnTekenen(lijnTekenen);
		boolean rechthoekTekenen = true;
		if (b.containsKey("rechthoekTekenen"))
			rechthoekTekenen = ((Boolean) b.get("rechthoekTekenen")).booleanValue();
		zetRechthoekTekenen(rechthoekTekenen);
		boolean cirkelTekenen = true;
		if (b.containsKey("cirkelTekenen"))
			cirkelTekenen = ((Boolean) b.get("cirkelTekenen")).booleanValue();
		zetCirkelTekenen(cirkelTekenen);
		
		Vector stateVector = new Vector();
		if (b.containsKey("pixels"))
			stateVector = (Vector) b.get("pixels");
		if (stateVector.size() > 0)
			kladjeVeld.setState(stateVector);
		

	}
	
	public Hashtable getState()
	{
		Hashtable h = new Hashtable();

		Vector stateVector = kladjeVeld.getState();
		
		h.put("pixels", stateVector);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		Hashtable h = new Hashtable();
		
		h.put("kleurkeuze", new Boolean(kleurkeuze));
		h.put("lijnen", new Boolean(lijnen));
		h.put("ruitjes", new Boolean(ruitjes));
		h.put("lijnTekenen", new Boolean(lijnTekenen));
		h.put("rechthoekTekenen", new Boolean(rechthoekTekenen));
		h.put("cirkelTekenen", new Boolean(cirkelTekenen));
		
		Vector stateVector = kladjeVeld.getState();
		
		h.put("pixels", stateVector);

		
		return h;
	}
	
	
	
	public InteractieEditPanel getEditPanel()
	{
		return new KladjeInteractieEditPanel();
	}
		
	public void zetKleurkeuze(boolean b)
	{
		kleurkeuze = b;
		for (int i = 0; i < kleurKeuzeButtons.length; i++)
		{	noSetBounds = true;
			kleurKeuzeButtons[i].setVisible(kleurkeuze);
		}
		
		
	}
	
	public void zetLijnen(boolean b)
	{
		lijnen = b;
		if (lijnen)
			ruitjes = false;
		kladjeVeld.zetLijnen(lijnen);
		kladjeVeld.zetRuitjes(ruitjes);
	}
	
	public void zetRuitjes(boolean b)
	{
		ruitjes = b;
		if (ruitjes)
			lijnen = false;
		kladjeVeld.zetLijnen(lijnen);
		kladjeVeld.zetRuitjes(ruitjes);
		
	}
	
	public void zetLijnTekenen(boolean b)
	{
		lijnTekenen = b;
		lijnButton.setVisible(lijnTekenen);
		
		layoutBottom();
	}

	public void zetRechthoekTekenen(boolean b)
	{
		rechthoekTekenen = b;
		rechthoekButton.setVisible(rechthoekTekenen);
		
		layoutBottom();
	}

	public void zetCirkelTekenen(boolean b)
	{
		cirkelTekenen = b;
		cirkelButton.setVisible(cirkelTekenen);
		
		layoutBottom();
	}

	
	public void layoutBottom()
	{
		if (lijnTekenen && rechthoekTekenen && cirkelTekenen)
		{
			lijnButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			                    getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);

			undoButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
		}
		else if (!lijnTekenen && rechthoekTekenen && cirkelTekenen)
		{	
			rechthoekButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	      			                    getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			undoButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (lijnTekenen && !rechthoekTekenen && cirkelTekenen)
		{
			lijnButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			cirkelButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			undoButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
					//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}
		else if (lijnTekenen && rechthoekTekenen && !cirkelTekenen)
		{
			lijnButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	      			               getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
	      			  getSize().height - offSet - 20);

			undoButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		else if (!lijnTekenen && !rechthoekTekenen && cirkelTekenen)
		{	
			cirkelButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	      			                 getSize().height - offSet - 20);
			undoButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			

		}
		else if (!lijnTekenen && rechthoekTekenen && !cirkelTekenen)
		{
			rechthoekButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
	       			                    getSize().height - offSet - 20);

			undoButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + 3 * offSet,
						           getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
		}
		else if (lijnTekenen && !rechthoekTekenen && !cirkelTekenen)
		{
			lijnButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
                       			   getSize().height - offSet - 20);

			undoButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + 3 * offSet,
	      			               getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}


		}
		else if (!lijnTekenen && !rechthoekTekenen && !cirkelTekenen)
		{
			undoButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + 3 * offSet,
	      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}

		}
		
	}
	public void setBounds(int x, int y, int b, int h)
	{
		
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
		
		if (h == 1)
			return;
		
		if ((getLocation().x == x) && (getLocation().y == y) &&
			(getSize().width == b) && (getSize().height == h))
			return;
			
		super.setBounds(x, y, b, h);
System.out.println("klip set bounds " + b + " " + h);		
		
		
		if (kladjeVeld == null) 
		{	//kladjeVeld = new KladjeVeld(b - 2 * offSet, h - offSet - bottomHeight);
			//kladjeVeld.setLocation(offSet, offSet);
			kladjeVeld = new KladjeVeld(b, h - bottomHeight);
			kladjeVeld.setLocation(0, 0);
			add(kladjeVeld);
System.out.println("kladjeVeld created");
			tekenGumGroup = new ButtonGroup();

			tekenButton = new JToggleButton(new ImageIcon(penDefault), true);
			tekenButton.setRolloverIcon(new ImageIcon(penRollover));
			tekenButton.setSelectedIcon(new ImageIcon(penSelected));
			tekenButton.setBorder(null);
			tekenButton.setBounds(2 * offSet, getSize().height - offSet - 20, 20, 20);
			add(tekenButton);
			tekenButton.addActionListener(new TekenGumAL());

			gumButton = new JToggleButton(new ImageIcon(gumDefault), false);
			gumButton.setRolloverIcon(new ImageIcon(gumRollover));
			gumButton.setSelectedIcon(new ImageIcon(gumSelected));
			gumButton.setBorder(null);
			gumButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
					            getSize().height - offSet - 20, 20, 20);
			add(gumButton);
			gumButton.addActionListener(new TekenGumAL());
			
			if (lijnDefault != null && lijnRollover != null && lijnSelected != null)
			{
				lijnButton = new JToggleButton(new ImageIcon(lijnDefault), false);
				lijnButton.setRolloverIcon(new ImageIcon(lijnRollover));
				lijnButton.setSelectedIcon(new ImageIcon(lijnSelected));
				
			}
			else
			{
				lijnButton = new JToggleButton("/");
			}
			lijnButton.setBorder(null);
			lijnButton.setBounds(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
		                        getSize().height - offSet - 20, 20, 20);
			add(lijnButton);
			lijnButton.addActionListener(new TekenGumAL());
			
			
			if (rechthoekDefault != null && rechthoekRollover != null && rechthoekSelected != null)
			{
				rechthoekButton = new JToggleButton(new ImageIcon(rechthoekDefault), false);
				rechthoekButton.setRolloverIcon(new ImageIcon(rechthoekRollover));
				rechthoekButton.setSelectedIcon(new ImageIcon(rechthoekSelected));
				
			}
			else
			{
				rechthoekButton = new JToggleButton("#");
			}
			rechthoekButton.setBorder(null);
			rechthoekButton.setBounds(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
		                              getSize().height - offSet - 20, 20, 20);
			add(rechthoekButton);
			rechthoekButton.addActionListener(new TekenGumAL());

			if (cirkelDefault != null && cirkelRollover != null && cirkelSelected != null)
			{
				cirkelButton = new JToggleButton(new ImageIcon(cirkelDefault), false);
				cirkelButton.setRolloverIcon(new ImageIcon(cirkelRollover));
				cirkelButton.setSelectedIcon(new ImageIcon(cirkelSelected));
				
			}
			else
			{
				cirkelButton = new JToggleButton("o");
			}
			cirkelButton.setBorder(null);
			cirkelButton.setBounds(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
		                           getSize().height - offSet - 20, 20, 20);
			add(cirkelButton);
			cirkelButton.addActionListener(new TekenGumAL());
			
			tekenGumGroup.add(tekenButton);
			tekenGumGroup.add(gumButton);
			tekenGumGroup.add(lijnButton);
			tekenGumGroup.add(rechthoekButton);
			tekenGumGroup.add(cirkelButton);
			
			tekenGumGroup.add(tekenButton);
			tekenGumGroup.add(gumButton);

			undoButton = new JButton(Kladje.rb.getString("terugTekst"));
			int width = theFM.stringWidth(Kladje.rb.getString("terugTekst")) + 35;
			undoButton.setFont(theFont);
			undoButton.setBounds(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
					             getSize().height - 20 - offSet, width, 20);
			add(undoButton);
			undoButton.addActionListener(this);
			
			wisButton = new JButton(Kladje.rb.getString("wisTekst"));
			width = theFM.stringWidth(Kladje.rb.getString("wisTekst")) + 35;
			wisButton.setFont(theFont);
			wisButton.setBounds(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
					            getSize().height - 20 - offSet, width, 20);
			add(wisButton);
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
				add(kleurKeuzeButtons[i]);
				kleurKeuzeButtons[i].addActionListener(this);
				//noSetBounds = true;
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			
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
			
			
		}
		else
		{	//kladjeVeld.setSize(b - 2 * offSet, h - offSet - bottomHeight);
			kladjeVeld.setSize(b, h - bottomHeight);
			tekenButton.setLocation(2 * offSet, getSize().height - offSet - 20);
			gumButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
		            			  getSize().height - offSet - 20);
			lijnButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + offSet, 
      			  getSize().height - offSet - 20);
			rechthoekButton.setLocation(lijnButton.getLocation().x + lijnButton.getSize().width + offSet, 
      			  getSize().height - offSet - 20);
			cirkelButton.setLocation(rechthoekButton.getLocation().x + rechthoekButton.getSize().width + offSet, 
      			  getSize().height - offSet - 20);
			
			
			undoButton.setLocation(cirkelButton.getLocation().x + cirkelButton.getSize().width + 3 * offSet,
      			  getSize().height - 20 - offSet);
			wisButton.setLocation(undoButton.getLocation().x + undoButton.getSize().width + 2 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			kladjeVeld.zetLijnen(lijnen);
			kladjeVeld.zetRuitjes(ruitjes);
			
			zetLijnTekenen(lijnTekenen);
			zetRechthoekTekenen(rechthoekTekenen);
			zetCirkelTekenen(cirkelTekenen);
			

System.out.println("kladjeVeld sized");		
		}
		
	}
	
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
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			else if (gumButton.isSelected())
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
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			else if (lijnButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.lijnTekenen;

				if (lijnCursor == null)
				{	kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
				boolean error = false;
				Cursor lineCursor = null;
				try
				{	
					lineCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(lijnCursor,
							new Point(10, 10), "LIJN_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(lineCursor);
				}
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				// cursor
			}
			
			else if (rechthoekButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.rechthoekTekenen;
				
				if (rechthoekCursor == null)
				{	
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					return;
				}

				boolean error = false;
				Cursor rectangleCursor = null;
				try
				{	
					rectangleCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(rechthoekCursor,
							new Point(10, 10), "RECHTHOEK_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(rectangleCursor);
				}
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				// cursor
			}
			
			else if (cirkelButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.cirkelTekenen;
				
				if (cirkelCursor == null)
				{	
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
				boolean error = false;
				Cursor circleCursor = null;
				try
				{	
					circleCursor = Toolkit.getDefaultToolkit().
						createCustomCursor(cirkelCursor,
							new Point(10, 10), "CIRKEL_CURSOR");
				}
				catch (IndexOutOfBoundsException ioobe)
				//catch (HeadlessException he)
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(circleCursor);
				}
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				// cursor
			}
						
			
		}
	}
	
	
	public void wis()
	{}
	
	public void zetMaat()
	{}
	
	public int geefAsHoogte()
	{	return 0;
	}
	
	public int getIpId()
	{	return 0;
	}
	
	public String getIpExpString()
	{	return null;
	}
	
	public int getScore()
	{	return score;
	}
	
	public int getScoreMax()
	{	return scoreMax;
	}
	
	public boolean isCorrect()
	{	return true;
	}
	
	public boolean isFout()
	{	return false;
	}
	
	public void zetMode(int mode)
	{}
	
	public void zetNagekeken(boolean b)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void destroy()
    {}
    
    public void opnieuw()
    {}
    
    public void kijkNa()
    {}
    
    public void kijkNa(int stapNr)
    {}
    
    public void addActionListener(ActionListener al)
    {}

	public void zetBreedte(int b)
	{}
	
	public void zetHoogte(int h)
	{}
    
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
		
		if (e.getSource() == undoButton)
		{
			kladjeVeld.undo();
		}
		
	}
}
