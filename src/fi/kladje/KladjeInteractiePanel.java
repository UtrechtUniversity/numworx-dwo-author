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
		  cirkelDefault, cirkelRollover, cirkelSelected, tekstDefault, tekstRollover, tekstSelected,
		  selecterenDefault, selecterenRollover, selecterenSelected;;
	ImageIcon lijnDefaultIcon, lijnRolloverIcon, lijnSelectedIcon, rechthoekDefaultIcon, rechthoekRolloverIcon, rechthoekSelectedIcon,
			  cirkelDefaultIcon, cirkelRolloverIcon, cirkelSelectedIcon, tekstDefaultIcon, tekstRolloverIcon, tekstSelectedIcon,
			  selecterenDefaultIcon, selecterenRolloverIcon, selecterenSelectedIcon;
	
	Image lijnCursor, rechthoekCursor, cirkelCursor, tekstCursor, selecterenCursor;
	ImageIcon lijnCursorIcon, rechthoekCursorIcon, cirkelCursorIcon, tekstCursorIcon, selecterenCursorIcon; 
	
	Image roteerLinksom, roteerRechtsom, vergroot, verklein;
	ImageIcon roteerLinksomIcon, roteerRechtsomIcon, vergrootIcon, verkleinIcon;
	
	protected static Color bgColor = new Color(220, 220, 220);
	
//	Color[] kleuren = {Color.black, Color.white, Color.gray, Color.lightGray, Color.red,     Color.orange,
//			   Color.yellow,Color.green, Color.cyan, Color.blue,      Color.magenta, Color.pink};

	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
	                   Color.green, Color.cyan, Color.blue, Color.magenta};
	
	KladjeVeld kladjeVeld;
	int bottomHeight = 30;
	int offSet = 5;
	
	JToggleButton tekenButton, lijnButton, rechthoekButton, cirkelButton, tekstButton, selecterenButton;
	ButtonGroup tekenGumGroup;
	JButton undoButton, wisButton;
	JToggleButton[] kleurKeuzeButtons;
	ButtonGroup kleurKeuzeGroup;

	JButton roteerLinksomButton, roteerRechtsomButton, vergrootButton, verkleinButton;
	
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
	int ruitjessize = 20;
	boolean lijnTekenen = true;
	boolean rechthoekTekenen = true;
	boolean cirkelTekenen = true;
	boolean tekstTekenen = true;
//	boolean selecteren = true;
	
	boolean roteren = true;
	boolean schalen = true;
	
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
		
		
		imageURL = Kladje.class.getResource("resources/teken_lijn_default.gif");
		if (imageURL != null) 
		{
			lijnDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_lijn_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_lijn_rollover.gif");
		if (imageURL != null) 
		{
			lijnRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_lijn_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_lijn_selected.gif");
		if (imageURL != null) 
		{
			lijnSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_lijn_selected.gif");
		}
		

		imageURL = Kladje.class.getResource("resources/teken_rechthoek_default.gif");
		if (imageURL != null) 
		{
			rechthoekDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_rechthoek_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_rechthoek_rollover.gif");
		if (imageURL != null) 
		{
			rechthoekRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_rechthoek_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_rechthoek_selected.gif");
		if (imageURL != null) 
		{
			rechthoekSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_rechthoek_selected.gif");
		}

		imageURL = Kladje.class.getResource("resources/teken_cirkel_default.gif");
		if (imageURL != null) 
		{
			cirkelDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_cirkel_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_cirkel_rollover.gif");
		if (imageURL != null) 
		{
			cirkelRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_cirkel_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_cirkel_selected.gif");
		if (imageURL != null) 
		{
			cirkelSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_cirkel_selected.gif");
		}

		imageURL = Kladje.class.getResource("resources/teken_tekst_default.gif");
		if (imageURL != null) 
		{
			tekstDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_tekst_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_tekst_rollover.gif");
		if (imageURL != null) 
		{
			tekstRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_tekst_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_tekst_selected.gif");
		if (imageURL != null) 
		{
			tekstSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_tekst_selected.gif");
		}
		
		imageURL = Kladje.class.getResource("resources/teken_selecteren_default.gif");
		if (imageURL != null) 
		{
			selecterenDefaultIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_selecteren_default.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_selecteren_rollover.gif");
		if (imageURL != null) 
		{
			selecterenRolloverIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_selecteren_rollover.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_selecteren_selected.gif");
		if (imageURL != null) 
		{
			selecterenSelectedIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_selecteren_selected.gif");
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

		imageURL = Kladje.class.getResource("resources/teken_lijn_cursor.gif");
		if (imageURL != null) 
		{
			lijnCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_lijn_lijncursor.gif");
		}

		imageURL = Kladje.class.getResource("resources/teken_rechthoek_cursor.gif");
		if (imageURL != null) 
		{
			rechthoekCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_rechthoek_cursor.gif");
		}
		
		imageURL = Kladje.class.getResource("resources/teken_cirkel_cursor.gif");
		if (imageURL != null) 
		{
			cirkelCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_cirkel_cursor.gif");
		}

		imageURL = Kladje.class.getResource("resources/teken_tekst_cursor.gif");
		if (imageURL != null) 
		{
			tekstCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_tekst_cursor.gif");
		}
		imageURL = Kladje.class.getResource("resources/teken_selecteren_cursor.gif");
		if (imageURL != null) 
		{
			selecterenCursorIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading teken_selecteren_cursor.gif");
		}
		
		imageURL = Kladje.class.getResource("resources/roteer-links.gif");
		if (imageURL != null) 
		{
			roteerLinksomIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading roteer-links.gif");
		}
		imageURL = Kladje.class.getResource("resources/roteer-rechts.gif");
		if (imageURL != null) 
		{
			roteerRechtsomIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading roteer-rechts.gif");
		}
		imageURL = Kladje.class.getResource("resources/zoominknop.gif");
		if (imageURL != null) 
		{
			vergrootIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading zoominknop.gif");
		}
		imageURL = Kladje.class.getResource("resources/zoomuitknop.gif");
		if (imageURL != null) 
		{
			verkleinIcon = new ImageIcon(imageURL);
		}
		else 
		{
			System.out.println("Error reading zoomuitknop.gif");
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
		if (tekstDefaultIcon != null)
			tekstDefault = tekstDefaultIcon.getImage();
		if (tekstRolloverIcon != null)
			tekstRollover = tekstRolloverIcon.getImage();
		if (tekstSelectedIcon != null)
			tekstSelected = tekstSelectedIcon.getImage();
		if (selecterenDefaultIcon != null)
			selecterenDefault = selecterenDefaultIcon.getImage();
		if (selecterenRolloverIcon != null)
			selecterenRollover = selecterenRolloverIcon.getImage();
		if (selecterenSelectedIcon != null)
			selecterenSelected = selecterenSelectedIcon.getImage();
		
		
		tekenCursor = tekenCursorIcon.getImage();
		gumCursor = gumCursorIcon.getImage();
		if (lijnCursorIcon != null)
			lijnCursor = lijnCursorIcon.getImage();
		if (rechthoekCursorIcon != null)
			rechthoekCursor = rechthoekCursorIcon.getImage();
		if (cirkelCursorIcon != null)
			cirkelCursor = cirkelCursorIcon.getImage();
		if (tekstCursorIcon != null)
			tekstCursor = tekstCursorIcon.getImage();
		if (selecterenCursorIcon != null)
			selecterenCursor = selecterenCursorIcon.getImage();
				
		if (roteerLinksomIcon != null)
			roteerLinksom = roteerLinksomIcon.getImage();
		if (roteerRechtsomIcon != null)
			roteerRechtsom = roteerRechtsomIcon.getImage();
		if (vergrootIcon != null)
			vergroot = vergrootIcon.getImage();
		if (verkleinIcon != null)
			verklein = verkleinIcon.getImage();
		
		
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
		int ruitjessize = 20;
		if (b.containsKey("ruitjessize"))
			ruitjessize = ((Integer) b.get("ruitjessize")).intValue();
		zetRuitjes(ruitjes,ruitjessize);

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
		boolean tekstTekenen = true;
		if (b.containsKey("tekstTekenen"))
			tekstTekenen = ((Boolean) b.get("tekstTekenen")).booleanValue();
		zetTekstTekenen(tekstTekenen);

		boolean roteren = true;
		if (b.containsKey("roteren"))
			roteren = ((Boolean) b.get("roteren")).booleanValue();
		zetRoteren(roteren);
		boolean schalen = true;
		if (b.containsKey("schalen"))
			schalen = ((Boolean) b.get("schalen")).booleanValue();
		zetSchalen(schalen);
		
/*		
		// backwards-compatibility
		Vector stateVector = new Vector();
		Vector gwtStateVector = new Vector();
		if (b.containsKey("gwtpixels"))
		{	gwtStateVector = (Vector) b.get("gwtpixels");
			if (gwtStateVector.size() > 0)
				kladjeVeld.setGWTState(gwtStateVector);
		}	
		else if (b.containsKey("pixels"))
		{	stateVector = (Vector) b.get("pixels");
			if (stateVector.size() > 0)
				kladjeVeld.setState(stateVector);
		}
*/
		kladjeVeld.setState(b);
		
	}
	
	public void setState(Hashtable b)
	{
/*		
		Vector stateVector = new Vector();
		Vector gwtStateVector = new Vector();
		if (b.containsKey("gwtpixels"))
		{	gwtStateVector = (Vector) b.get("gwtpixels");
			if (gwtStateVector.size() > 0)
				kladjeVeld.setGWTState(gwtStateVector);
		}	
		else if (b.containsKey("pixels"))
		{	stateVector = (Vector) b.get("pixels");
			if (stateVector.size() > 0)
				kladjeVeld.setState(stateVector);
		}	
*/
		kladjeVeld.setState(b);
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
		int ruitjessize = 20;
		if (b.containsKey("ruitjessize"))
			ruitjessize = ((Integer) b.get("ruitjessize")).intValue();
		zetRuitjes(ruitjes,ruitjessize);

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
		boolean tekstTekenen = true;
		if (b.containsKey("tekstTekenen"))
			tekstTekenen = ((Boolean) b.get("tekstTekenen")).booleanValue();
		zetTekstTekenen(tekstTekenen);
		
		boolean roteren = true;
		if (b.containsKey("roteren"))
			roteren = ((Boolean) b.get("roteren")).booleanValue();
		zetRoteren(roteren);
		boolean schalen = true;
		if (b.containsKey("schalen"))
			schalen = ((Boolean) b.get("schalen")).booleanValue();
		zetSchalen(schalen);
		
		
		
/*		
		Vector stateVector = new Vector();
		Vector gwtStateVector = new Vector();
		if (b.containsKey("gwtpixels"))
		{	gwtStateVector = (Vector) b.get("gwtpixels");
			if (gwtStateVector.size() > 0)
				kladjeVeld.setGWTState(gwtStateVector);
		}	
		else if (b.containsKey("pixels"))
		{	stateVector = (Vector) b.get("pixels");
			if (stateVector.size() > 0)
				kladjeVeld.setState(stateVector);
		}	
*/
		kladjeVeld.setState(b);

	}
	
	public Hashtable getState()
	{
		Hashtable h = kladjeVeld.getState();

//		Vector stateVector = kladjeVeld.getState();
//		h.put("pixels", stateVector);
		
		//Vector gwtStateVector = kladjeVeld.getGWTState();
		//h.put("gwtpixels", gwtStateVector);
		
		return h;
		
	}
	
	public Hashtable getEditState()
	{
		//Hashtable h = new Hashtable();
		Hashtable h = kladjeVeld.getState();
		
		h.put("kleurkeuze", new Boolean(kleurkeuze));
		h.put("lijnen", new Boolean(lijnen));
		h.put("ruitjes", new Boolean(ruitjes));
		h.put("ruitjessize", new Integer(ruitjessize));
		h.put("lijnTekenen", new Boolean(lijnTekenen));
		h.put("rechthoekTekenen", new Boolean(rechthoekTekenen));
		h.put("cirkelTekenen", new Boolean(cirkelTekenen));
		h.put("tekstTekenen", new Boolean(tekstTekenen));
		
		h.put("roteren", new Boolean(roteren));
		h.put("schalen", new Boolean(schalen));
		
//		Vector stateVector = kladjeVeld.getState();
//		h.put("pixels", stateVector);

		//Vector gwtStateVector = kladjeVeld.getGWTState();
		//h.put("gwtpixels", gwtStateVector);
		
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
		kladjeVeld.zetRuitjes(ruitjes, 20);
	}
	
	public void zetRuitjes(boolean b, int size)
	{
		ruitjes = b;
		ruitjessize = size;
		if (ruitjes)
			lijnen = false;
		kladjeVeld.zetLijnen(lijnen);
		kladjeVeld.zetRuitjes(ruitjes, size);
		
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
	
	public void zetTekstTekenen(boolean b)
	{
		tekstTekenen = b;
		tekstButton.setVisible(tekstTekenen);
		
		layoutBottom();
	}
	
	public void zetRoteren(boolean b)
	{
		roteren = b;
		//KladjeVeld.roteren = b;
		kladjeVeld.zetRoteren(b);
		
		layoutBottom();
		if (kladjeVeld.mouseMode == kladjeVeld.selecteren)
		{
			//setRotateScaleButtons(true);
		}
		else
		{
			//setRotateScaleButtons(false);
		}
	}

	public void zetSchalen(boolean b)
	{
		schalen = b;
		//KladjeVeld.schalen = b;
		kladjeVeld.zetSchalen(b);
		
		layoutBottom();

		if (kladjeVeld.mouseMode == kladjeVeld.selecteren)
		{
			//setRotateScaleButtons(true);
		}
		else
		{
			//setRotateScaleButtons(false);
		}
		
	}
	
	public void layoutBottom()
	{
// dit kan wel iets eenvoudiger, vgl Grafiek3DTest	
		
		int currentX = tekenButton.getLocation().x + tekenButton.getSize().width + offSet;
		int currentY = getSize().height - offSet - 20; 
		
		if (lijnTekenen)
		{
			lijnButton.setLocation(currentX, currentY);
			currentX += lijnButton.getSize().width + offSet;
			
		}
		
		if (rechthoekTekenen)
		{
			rechthoekButton.setLocation(currentX, currentY);
			currentX += rechthoekButton.getSize().width + offSet;
			
		}
		
		if (cirkelTekenen)
		{
			cirkelButton.setLocation(currentX, currentY);
			currentX += cirkelButton.getSize().width + offSet;
			
		}
		
		if (tekstTekenen)
		{
			tekstButton.setLocation(currentX, currentY);
			currentX += tekstButton.getSize().width + offSet;
			
		}
		
		selecterenButton.setLocation(currentX, currentY);
		currentX += selecterenButton.getSize().width + 3 * offSet;
		
		undoButton.setLocation(currentX, currentY);
		currentX += undoButton.getSize().width + 2 * offSet;
		
		wisButton.setLocation(currentX, currentY);
		currentX += wisButton.getSize().width + 3 * offSet;
		
		for (int i = 0; i < kleurKeuzeButtons.length; i++)
		{	kleurKeuzeButtons[i].setLocation(currentX + 20 * i, currentY); 
		}
		
		if (roteren)
		{
			roteerLinksomButton.setLocation(currentX, currentY);
			currentX += roteerLinksomButton.getSize().width + offSet;
			roteerRechtsomButton.setLocation(currentX, currentY);
			currentX += roteerRechtsomButton.getSize().width + offSet;
			
		}
		
		if (schalen)
		{
			vergrootButton.setLocation(currentX, currentY);
			currentX += vergrootButton.getSize().width + offSet;
			verkleinButton.setLocation(currentX, currentY);
			currentX += verkleinButton.getSize().width + offSet;
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
//System.out.println("klip set bounds " + b + " " + h);		
		
		
		if (kladjeVeld == null) 
		{	//kladjeVeld = new KladjeVeld(b - 2 * offSet, h - offSet - bottomHeight);
			//kladjeVeld.setLocation(offSet, offSet);
			kladjeVeld = new KladjeVeld(b, h - bottomHeight);
			kladjeVeld.setLocation(0, 0);
			add(kladjeVeld);
//System.out.println("kladjeVeld created");
			tekenGumGroup = new ButtonGroup();

			tekenButton = new JToggleButton(new ImageIcon(penDefault), true);
			tekenButton.setRolloverIcon(new ImageIcon(penRollover));
			tekenButton.setSelectedIcon(new ImageIcon(penSelected));
			tekenButton.setBorder(null);
			tekenButton.setBounds(2 * offSet, getSize().height - offSet - 20, 20, 20);
			add(tekenButton);
			tekenButton.addActionListener(new TekenGumAL());

/*			
			gumButton = new JToggleButton(new ImageIcon(gumDefault), false);
			gumButton.setRolloverIcon(new ImageIcon(gumRollover));
			gumButton.setSelectedIcon(new ImageIcon(gumSelected));
			gumButton.setBorder(null);
			gumButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
					            getSize().height - offSet - 20, 20, 20);
			add(gumButton);
			gumButton.addActionListener(new TekenGumAL());
*/			
			
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
			lijnButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
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
			
			if (tekstDefault != null && tekstRollover != null && tekstSelected != null)
			{
				tekstButton = new JToggleButton(new ImageIcon(tekstDefault), false);
				tekstButton.setRolloverIcon(new ImageIcon(tekstRollover));
				tekstButton.setSelectedIcon(new ImageIcon(tekstSelected));
				
			}
			else
			{
				tekstButton = new JToggleButton("T");
			}
			tekstButton.setBorder(null);
			tekstButton.setBounds(cirkelButton.getLocation().x + cirkelButton.getSize().width + offSet, 
		                           getSize().height - offSet - 20, 20, 20);
			add(tekstButton);
			tekstButton.addActionListener(new TekenGumAL());
			
			if (selecterenDefault != null && selecterenRollover != null && selecterenSelected != null)
			{
				selecterenButton = new JToggleButton(new ImageIcon(selecterenDefault), false);
				selecterenButton.setRolloverIcon(new ImageIcon(selecterenRollover));
				selecterenButton.setSelectedIcon(new ImageIcon(selecterenSelected));
				
			}
			else
			{
				selecterenButton = new JToggleButton("S");
			}
			selecterenButton.setBorder(null);
			selecterenButton.setBounds(tekstButton.getLocation().x + tekstButton.getSize().width + offSet, 
		                           getSize().height - offSet - 20, 20, 20);
			add(selecterenButton);
			selecterenButton.addActionListener(new TekenGumAL());
			
			
			tekenGumGroup.add(tekenButton);
//			tekenGumGroup.add(gumButton);
			tekenGumGroup.add(lijnButton);
			tekenGumGroup.add(rechthoekButton);
			tekenGumGroup.add(cirkelButton);
			tekenGumGroup.add(tekstButton);
			tekenGumGroup.add(selecterenButton);
			

			undoButton = new JButton(Kladje.rb.getString("terugTekst"));
			int width = theFM.stringWidth(Kladje.rb.getString("terugTekst")) + 35;
			undoButton.setFont(theFont);
			undoButton.setBounds(selecterenButton.getLocation().x + selecterenButton.getSize().width + 3 * offSet,
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
			
			kleurKeuzeGroup = new ButtonGroup(); 
			kleurKeuzeButtons = new JToggleButton[kleuren.length];
			for (int i = 0; i < kleuren.length; i++)
			{	final Color buttonColor = kleuren[i];
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
				kleurKeuzeButtons[i].setBounds(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                getSize().height - 20 - offSet, 20 , 20);
				add(kleurKeuzeButtons[i]);
				kleurKeuzeButtons[i].addActionListener(this);
				//noSetBounds = true;
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
				kleurKeuzeGroup.add(kleurKeuzeButtons[i]);
			}
			kleurKeuzeButtons[0].setSelected(true);
			
			
			roteerLinksomButton = new JButton(roteerLinksomIcon);
			roteerLinksomButton.setBounds(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet,
					getSize().height - 20 - offSet, 20 , 20);
			roteerLinksomButton.setVisible(false);
			add(roteerLinksomButton);
			roteerLinksomButton.addActionListener(this);
			
			roteerRechtsomButton = new JButton(roteerRechtsomIcon);
			roteerRechtsomButton.setBounds(roteerLinksomButton.getLocation().x + roteerLinksomButton.getSize().width + offSet,
					getSize().height - 20 - offSet, 20 , 20);
			roteerRechtsomButton.setVisible(false);
			add(roteerRechtsomButton);
			roteerRechtsomButton.addActionListener(this);
			
			vergrootButton = new JButton(vergrootIcon);
			vergrootButton.setBounds(roteerRechtsomButton.getLocation().x + roteerRechtsomButton.getSize().width + offSet,
					getSize().height - 20 - offSet, 20 , 20);
			vergrootButton.setVisible(false);
			add(vergrootButton);
			vergrootButton.addActionListener(this);
			
			verkleinButton = new JButton(verkleinIcon);
			verkleinButton.setBounds(vergrootButton.getLocation().x + vergrootButton.getSize().width + offSet,
					getSize().height - 20 - offSet, 20 , 20);
			verkleinButton.setVisible(false);
			add(verkleinButton);
			verkleinButton.addActionListener(this);
			
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
			kladjeVeld.zetRuitjes(ruitjes,ruitjessize);
			
			
		}
		else
		{	//kladjeVeld.setSize(b - 2 * offSet, h - offSet - bottomHeight);
			kladjeVeld.setSize(b, h - bottomHeight);
			tekenButton.setLocation(2 * offSet, getSize().height - offSet - 20);
//			gumButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
//		            			  getSize().height - offSet - 20);
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
			
			roteerLinksomButton.setSize(roteerLinksomButton.getSize().width, getSize().height - 20 - offSet);
			roteerRechtsomButton.setSize(roteerRechtsomButton.getSize().width, getSize().height - 20 - offSet);
			vergrootButton.setSize(vergrootButton.getSize().width, getSize().height - 20 - offSet);
			verkleinButton.setSize(verkleinButton.getSize().width, getSize().height - 20 - offSet);
			
			kladjeVeld.zetLijnen(lijnen);
			kladjeVeld.zetRuitjes(ruitjes,ruitjessize);
			
			zetLijnTekenen(lijnTekenen);
			zetRechthoekTekenen(rechthoekTekenen);
			zetCirkelTekenen(cirkelTekenen);
			zetTekstTekenen(tekstTekenen);

System.out.println("kladjeVeld sized");		
		}
		
	}
	
	public void setRotateScaleButtons(boolean b)
	{
		if (!roteren && !schalen)
			return;
		
		if (b)
		{
			// positie wordt gezet in layoutBottom
			if (roteren)
			{	roteerLinksomButton.setVisible(true);
				roteerRechtsomButton.setVisible(true);
			}
			else
			{	roteerLinksomButton.setVisible(false);
				roteerRechtsomButton.setVisible(false);
			}
			if (schalen)
			{	vergrootButton.setVisible(true);
				verkleinButton.setVisible(true);
			}
			else
			{	vergrootButton.setVisible(false);
				verkleinButton.setVisible(false);
			}
			
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setVisible(false);
			}
		}
		else
		{
			roteerLinksomButton.setVisible(false);
			roteerRechtsomButton.setVisible(false);
			vergrootButton.setVisible(false);
			verkleinButton.setVisible(false);
			
			
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
		}
	}
	
	class TekenGumAL implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (tekenButton.isSelected())
			{
				setRotateScaleButtons(false);			
				
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
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
/*			
			else if (gumButton.isSelected())
			{
				kladjeVeld.mouseMode = kladjeVeld.gummen;
				kladjeVeld.hideTekstVeld(true);
				//kladjeVeld.selecteerRechthoek = null;
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
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
*/			
			else if (lijnButton.isSelected())
			{
				
				setRotateScaleButtons(false);
				
				kladjeVeld.mouseMode = kladjeVeld.lijnTekenen;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.repaint();

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
				
				setRotateScaleButtons(false);
				
				kladjeVeld.mouseMode = kladjeVeld.rechthoekTekenen;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.repaint();
				
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
				
				setRotateScaleButtons(false);
				
				kladjeVeld.mouseMode = kladjeVeld.cirkelTekenen;
				kladjeVeld.hideTekstVeld(true);
				kladjeVeld.repaint();
				
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
				{	error = true;
				}
				if (!error)
				{	kladjeVeld.setCursor(circleCursor);
				}
				else
					kladjeVeld.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				// cursor
			}
			else if (tekstButton.isSelected())
			{
				
				setRotateScaleButtons(false);
				
				kladjeVeld.mouseMode = kladjeVeld.tekstTekenen;
				kladjeVeld.hideTekstVeld(true);
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
				
				//setRotateScaleButtons(true);				

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
	{	return 0;
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
			kladjeVeld.wis(true);
		}
		
		if (e.getSource() == undoButton)
		{
			kladjeVeld.undo();
		}
		
		if (e.getSource() == roteerLinksomButton)
		{
			kladjeVeld.rotateObjectSelected(- kladjeVeld.rotateStep);
		}
		
		if (e.getSource() == roteerRechtsomButton)
		{
			kladjeVeld.rotateObjectSelected(kladjeVeld.rotateStep);
		}

		if (e.getSource() == vergrootButton)
		{
			kladjeVeld.scaleObjectSelected(kladjeVeld.scaleUpStep);
		}

		if (e.getSource() == verkleinButton)
		{
			kladjeVeld.scaleObjectSelected(kladjeVeld.scaleDownStep);
		}

	}
}
