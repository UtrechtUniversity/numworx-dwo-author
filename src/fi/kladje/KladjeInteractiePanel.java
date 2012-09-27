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
	
	protected static Color bgColor = new Color(220, 220, 220);
	
//	Color[] kleuren = {Color.black, Color.white, Color.gray, Color.lightGray, Color.red,     Color.orange,
//			   Color.yellow,Color.green, Color.cyan, Color.blue,      Color.magenta, Color.pink};

	Color[] kleuren = {Color.black, Color.lightGray, Color.red, Color.orange,
	                   Color.green, Color.cyan, Color.blue, Color.magenta};
	
	KladjeVeld kladjeVeld;
	int bottomHeight = 30;
	int offSet = 5;
	
	JToggleButton tekenButton, gumButton;
	ButtonGroup tekenGumGroup;
	JButton wisButton;
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
		
		penDefault = penDefaultIcon.getImage();
		penRollover = penRolloverIcon.getImage();
		penSelected = penSelectedIcon.getImage();
		gumDefault = gumDefaultIcon.getImage();
		gumRollover = gumRolloverIcon.getImage();
		gumSelected = gumSelectedIcon.getImage();
		tekenCursor = tekenCursorIcon.getImage();
		gumCursor = gumCursorIcon.getImage();
		
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
	public void setBounds(int x, int y, int b, int h)
	{
		
		System.out.println("klip set bounds " + b + " " + h);
		
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
		
		
		if (kladjeVeld == null) 
		{	kladjeVeld = new KladjeVeld(b - 2 * offSet, h - offSet - bottomHeight);
			kladjeVeld.setLocation(offSet, offSet);
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

			gumButton = new JToggleButton(new ImageIcon(gumDefault), false);
			gumButton.setRolloverIcon(new ImageIcon(gumRollover));
			gumButton.setSelectedIcon(new ImageIcon(gumSelected));
			gumButton.setBorder(null);
			gumButton.setBounds(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
					            getSize().height - offSet - 20, 20, 20);
			add(gumButton);
			gumButton.addActionListener(new TekenGumAL());
			
			tekenGumGroup.add(tekenButton);
			tekenGumGroup.add(gumButton);
			
			wisButton = new JButton(Kladje.rb.getString("wisTekst"));
			int width = theFM.stringWidth(Kladje.rb.getString("wisTekst")) + 35;
			wisButton.setFont(theFont);
			wisButton.setBounds(gumButton.getLocation().x + gumButton.getSize().width + 3 * offSet,
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
		{	kladjeVeld.setSize(b - 2 * offSet, h - offSet - bottomHeight);
		
			tekenButton.setLocation(2 * offSet, getSize().height - offSet - 20);
			gumButton.setLocation(tekenButton.getLocation().x + tekenButton.getSize().width + offSet, 
		            			  getSize().height - offSet - 20);
			wisButton.setLocation(gumButton.getLocation().x + gumButton.getSize().width + 3 * offSet,
		            			  getSize().height - 20 - offSet);
			for (int i = 0; i < kleurKeuzeButtons.length; i++)
			{	kleurKeuzeButtons[i].setLocation(wisButton.getLocation().x + wisButton.getSize().width + 3 * offSet + 20 * i, 
						                         getSize().height - 20 - offSet);
				//kleurKeuzeButtons[i].setVisible(kleurkeuze);
			}
			kladjeVeld.zetLijnen(lijnen);
			kladjeVeld.zetRuitjes(ruitjes);
			

//System.out.println("g3dc sized");		
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
		
		
	}
}
