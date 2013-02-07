package fi.kladje;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class KladjeInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 450; 
	int klipBreedte = 500; // startbreedte spip
	int klipHoogte = 450; // starthoogte spip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected KladjeInteractiePanel klip;

	int scoreMax = 0;
	
	JCheckBox kleurkeuzeBox;
	ButtonGroup achtergrondGroep;
	JRadioButton blancoButton, lijnenButton, ruitjesButton; 
	JCheckBox lijnTekenenBox, rechthoekTekenenBox, cirkelTekenenBox, tekstTekenenBox;
	
	public KladjeInteractieEditPanel()
	{
		setLayout(null);

		klip = new KladjeInteractiePanel();
		add(klip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 2 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = klip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
	
		kleurkeuzeBox = new JCheckBox(Kladje.rb.getString("kleurkeuzeTekst"), true);
		kleurkeuzeBox.setFont(theFont);
		kleurkeuzeBox.setBackground(Color.white);
		kleurkeuzeBox.setBounds(currentX, currentY, width, height);
		add(kleurkeuzeBox);
		kleurkeuzeBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		achtergrondGroep = new ButtonGroup();
		
		blancoButton = new JRadioButton(Kladje.rb.getString("blancoTekst"), true);
		blancoButton.setFont(theFont);
		blancoButton.setBackground(Color.white);
		blancoButton.setBounds(currentX, currentY, width, height);
		add(blancoButton);
		blancoButton.addActionListener(this);
		achtergrondGroep.add(blancoButton);
		
		currentY += height + offset / 2;		
		
		lijnenButton = new JRadioButton(Kladje.rb.getString("lijnenTekst"), false);
		lijnenButton.setFont(theFont);
		lijnenButton.setBackground(Color.white);
		lijnenButton.setBounds(currentX, currentY, width, height);
		add(lijnenButton);
		lijnenButton.addActionListener(this);
		achtergrondGroep.add(lijnenButton);
		
		currentY += height + offset / 2;		
		
		ruitjesButton = new JRadioButton(Kladje.rb.getString("ruitjesTekst"), false);
		ruitjesButton.setFont(theFont);
		ruitjesButton.setBackground(Color.white);
		ruitjesButton.setBounds(currentX, currentY, width, height);
		add(ruitjesButton);
		ruitjesButton.addActionListener(this);
		achtergrondGroep.add(ruitjesButton);
		
		currentY += height + 2 * offset;
		
		lijnTekenenBox = new JCheckBox(Kladje.rb.getString("lijnTekenenTekst"), true);
		lijnTekenenBox.setFont(theFont);
		lijnTekenenBox.setBackground(Color.white);
		lijnTekenenBox.setBounds(currentX, currentY, width, height);
		add(lijnTekenenBox);
		lijnTekenenBox.addActionListener(this);
		
		currentY += height + offset;
		
		rechthoekTekenenBox = new JCheckBox(Kladje.rb.getString("rechthoekTekenenTekst"), true);
		rechthoekTekenenBox.setFont(theFont);
		rechthoekTekenenBox.setBackground(Color.white);
		rechthoekTekenenBox.setBounds(currentX, currentY, width, height);
		add(rechthoekTekenenBox);
		rechthoekTekenenBox.addActionListener(this);
		
		currentY += height + offset;

		cirkelTekenenBox = new JCheckBox(Kladje.rb.getString("cirkelTekenenTekst"), true);
		cirkelTekenenBox.setFont(theFont);
		cirkelTekenenBox.setBackground(Color.white);
		cirkelTekenenBox.setBounds(currentX, currentY, width, height);
		add(cirkelTekenenBox);
		cirkelTekenenBox.addActionListener(this);
		
		currentY += height + offset;

		tekstTekenenBox = new JCheckBox(Kladje.rb.getString("tekstTekenenTekst"), true);
		tekstTekenenBox.setFont(theFont);
		tekstTekenenBox.setBackground(Color.white);
		tekstTekenenBox.setBounds(currentX, currentY, width, height);
		add(tekstTekenenBox);
		tekstTekenenBox.addActionListener(this);
		
		currentY += height + offset;
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			kleurkeuzeBox.setLocation(klip.getSize().width + offset, kleurkeuzeBox.getLocation().y);
			blancoButton.setLocation(klip.getSize().width + offset, blancoButton.getLocation().y);
			lijnenButton.setLocation(klip.getSize().width + offset, lijnenButton.getLocation().y);
			ruitjesButton.setLocation(klip.getSize().width + offset, ruitjesButton.getLocation().y);
			
			lijnTekenenBox.setLocation(klip.getSize().width + offset, lijnTekenenBox.getLocation().y);
			rechthoekTekenenBox.setLocation(klip.getSize().width + offset, rechthoekTekenenBox.getLocation().y);
			cirkelTekenenBox.setLocation(klip.getSize().width + offset, cirkelTekenenBox.getLocation().y);
			tekstTekenenBox.setLocation(klip.getSize().width + offset, tekstTekenenBox.getLocation().y);
		}
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("kliep setEditState");

		boolean kleurkeuze = true;
		if (b.containsKey("kleurkeuze"))
			kleurkeuze = ((Boolean) b.get("kleurkeuze")).booleanValue();
		kleurkeuzeBox.setSelected(kleurkeuze);
		boolean lijnen = false;
		if (b.containsKey("lijnen"))
			lijnen = ((Boolean) b.get("lijnen")).booleanValue();
		lijnenButton.setSelected(lijnen);
		boolean ruitjes = false;
		if (b.containsKey("ruitjes"))
			ruitjes = ((Boolean) b.get("ruitjes")).booleanValue();
		ruitjesButton.setSelected(ruitjes);
		
		boolean lijnTekenen = true;
		if (b.containsKey("lijnTekenen"))
			lijnTekenen = ((Boolean) b.get("lijnTekenen")).booleanValue();
		lijnTekenenBox.setSelected(lijnTekenen);
		boolean rechthoekTekenen = true;
		if (b.containsKey("rechthoekTekenen"))
			rechthoekTekenen = ((Boolean) b.get("rechthoekTekenen")).booleanValue();
		rechthoekTekenenBox.setSelected(rechthoekTekenen);
		boolean cirkelTekenen = true;
		if (b.containsKey("cirkelTekenen"))
			cirkelTekenen = ((Boolean) b.get("cirkelTekenen")).booleanValue();
		cirkelTekenenBox.setSelected(cirkelTekenen);
		boolean tekstTekenen = true;
		if (b.containsKey("tekstTekenen"))
			tekstTekenen = ((Boolean) b.get("tekstTekenen")).booleanValue();
		tekstTekenenBox.setSelected(tekstTekenen);
		
		
		
		if (b.containsKey("klipBreedte"))
			klipBreedte = ((Integer) b.get("klipBreedte")).intValue();
		if (b.containsKey("klipHoogte"))
			klipHoogte = ((Integer) b.get("klipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
		
		// HIER !!
		klip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("kliep getEditState");

		Hashtable h = klip.getEditState();
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("klipBreedte", new Integer(klipBreedte));
		h.put("klipHoogte", new Integer(klipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("spiep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));
		
//System.out.println("spiep setBounds " + x + " " + y + " " + (spipBreedte + editWidth) + " " + 
//					Math.max(spipHoogte, editHeight));
	
		if (klip != null)
			klip.setBounds(0, 0, klipBreedte, klipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		klipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		klipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, klipBreedte + editWidth, Math.max(klipHoogte, editHeight));		
	}
	
	public void wis()
	{}
    
	public void zetMode(int mode)
	{}
	
    public void stop()
    {}
    
    public void start()
    {}
    
    public void addActionListener(ActionListener al)
    {}
    
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == kleurkeuzeBox)
		{
			klip.zetKleurkeuze(kleurkeuzeBox.isSelected());
		}
		else if (e.getSource() == blancoButton)
		{
			klip.zetLijnen(false);
			klip.zetRuitjes(false);
		}
		else if (e.getSource() == lijnenButton)
		{
			klip.zetLijnen(lijnenButton.isSelected());
		}
		else if (e.getSource() == ruitjesButton)
		{
			klip.zetRuitjes(ruitjesButton.isSelected());
		}
		else if (e.getSource() == lijnTekenenBox)
		{
			klip.zetLijnTekenen(lijnTekenenBox.isSelected());
		}
		else if (e.getSource() == rechthoekTekenenBox)
		{
			klip.zetRechthoekTekenen(rechthoekTekenenBox.isSelected());
		}
		else if (e.getSource() == cirkelTekenenBox)
		{
			klip.zetCirkelTekenen(cirkelTekenenBox.isSelected());
		}
		else if (e.getSource() == tekstTekenenBox)
		{
			klip.zetTekstTekenen(tekstTekenenBox.isSelected());
		}
		

	}

}
