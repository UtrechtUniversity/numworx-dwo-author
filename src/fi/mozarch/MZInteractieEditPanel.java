package fi.mozarch;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.*;

import fi.beans.wiskopdrbeans.*;

public class MZInteractieEditPanel extends JPanel implements InteractieEditPanel,
																	ActionListener	
{	
	int editWidth = 190;
	int editHeight = 500; 
	int mzipBreedte = 500; // startbreedte mzip
	int mzipHoogte = 450; // starthoogte mzip
	
	Font theFont;
	FontMetrics theFM;
	Font theBoldFont;
	FontMetrics theBoldFM;
	
	int offset = 10;
	boolean componentsCreated = false;
	
	boolean noSetBounds = false;	

	protected MZInteractiePanel mzip;

	int scoreMax = 10;
	
	JCheckBox fractielenBox, startFiguurBox;
	JLabel aantalHoekpuntenLabel, aantalPerZijdeLabel, fractielTypeLabel;
	JComboBox aantalHoekpuntenCombo, aantalPerZijdeCombo, fractielTypeCombo;
	
	boolean aantalHoekpuntenComboEnabled = true, aantalPerZijdeComboEnabled = true, 
	        fractielTypeComboEnabled = true;
	
	public MZInteractieEditPanel()
	{
		setLayout(null);
		mzip = new MZInteractiePanel();
		add(mzip);
		
		theFont = new Font("Dialog", Font.PLAIN, 12);
		theFM = getFontMetrics(theFont);
		theBoldFont = new Font("Dialog", Font.BOLD, 12);
		theBoldFM = getFontMetrics(theBoldFont);
		
		
		int width = editWidth - 3 * offset;
		int height = 3 * theFM.getHeight() / 2;
		int currentX = mzip.getSize().width + offset;
		int currentX2 = offset;
		int currentY = offset;
		
		fractielenBox = new JCheckBox(MozArch.rb.getString("fractielenTekst"), false);
		fractielenBox.setFont(theFont);
		fractielenBox.setBackground(Color.white);
		fractielenBox.setBounds(currentX, currentY, width, height);
		add(fractielenBox);
		fractielenBox.addActionListener(this);
		
		currentY += height + 2 * offset;
		
		startFiguurBox = new JCheckBox(MozArch.rb.getString("startFiguurTekst"));
		startFiguurBox.setFont(theFont);
		startFiguurBox.setBackground(Color.white);
		startFiguurBox.setBounds(currentX + offset, currentY, width, height);
		add(startFiguurBox);
		startFiguurBox.addActionListener(this);

		currentY += height;
		
		fractielTypeLabel = new JLabel(MozArch.rb.getString("fractielTypeTekst"));
		fractielTypeLabel.setFont(theFont);
		fractielTypeLabel.setBackground(Color.white);
		width = theFM.stringWidth(fractielTypeLabel.getText());
		fractielTypeLabel.setBounds(currentX + offset, currentY, width, theFM.getHeight());
		fractielTypeLabel.setVisible(false);
		fractielTypeLabel.setEnabled(false);
		add(fractielTypeLabel);

		aantalHoekpuntenLabel = new JLabel(MozArch.rb.getString("aantalHoekpuntenTekst"));
		aantalHoekpuntenLabel.setFont(theFont);
		aantalHoekpuntenLabel.setBackground(Color.white);
		width = theFM.stringWidth(aantalHoekpuntenLabel.getText());
		aantalHoekpuntenLabel.setBounds(currentX + offset, currentY, width, theFM.getHeight());
		aantalHoekpuntenLabel.setEnabled(false);
		add(aantalHoekpuntenLabel);
		
		currentY += height;
		
		aantalHoekpuntenCombo = new JComboBox();
		aantalHoekpuntenCombo.setFont(theFont);
		aantalHoekpuntenCombo.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXXXXX");
		aantalHoekpuntenCombo.addItem("3");
		aantalHoekpuntenCombo.addItem("4");
		aantalHoekpuntenCombo.addItem("6");
		aantalHoekpuntenCombo.addItem("8");
		aantalHoekpuntenCombo.addItem("12");
		aantalHoekpuntenCombo.setBounds(currentX + 3 * offset, currentY, width, height);
		aantalHoekpuntenCombo.setEnabled(false);
		add(aantalHoekpuntenCombo);
		aantalHoekpuntenCombo.addActionListener(this);

		fractielTypeCombo = new JComboBox();
		fractielTypeCombo.setFont(theFont);
		fractielTypeCombo.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXXXXX");
		fractielTypeCombo.addItem("1");
		fractielTypeCombo.addItem("2");
		fractielTypeCombo.addItem("3");
		fractielTypeCombo.setBounds(currentX + 3 * offset, currentY, width, height);
		fractielTypeCombo.setVisible(false);
		fractielTypeCombo.setEnabled(false);
		add(fractielTypeCombo);
		fractielTypeCombo.addActionListener(this);
		
		currentY += height + offset / 2;		
		
		aantalPerZijdeLabel = new JLabel(MozArch.rb.getString("aantalPerZijdeTekst"));
		aantalPerZijdeLabel.setFont(theFont);
		aantalPerZijdeLabel.setBackground(Color.white);
		width = theFM.stringWidth(aantalPerZijdeLabel.getText());
		aantalPerZijdeLabel.setBounds(currentX + offset, currentY, width, theFM.getHeight());
		aantalPerZijdeLabel.setEnabled(false);
		add(aantalPerZijdeLabel);

		currentY += height;

		aantalPerZijdeCombo = new JComboBox();
		aantalPerZijdeCombo.setFont(theFont);
		aantalPerZijdeCombo.setBackground(Color.white);
		width = theFM.stringWidth("XXXXXXXXX");
		aantalPerZijdeCombo.addItem("1");
		aantalPerZijdeCombo.addItem("2");
		aantalPerZijdeCombo.addItem("3");
		aantalPerZijdeCombo.addItem("4");
		aantalPerZijdeCombo.setBounds(currentX + 3 * offset, currentY, width, height);
		aantalPerZijdeCombo.setEnabled(false);
		add(aantalPerZijdeCombo);
		aantalPerZijdeCombo.addActionListener(this);
		
		currentY += height + offset / 2;		
		
		componentsCreated = true;
	}	
	
	public void plaatsComponenten()
	{
		if (componentsCreated)
		{
			fractielenBox.setLocation(mzip.getSize().width + 2 * offset, fractielenBox.getLocation().y);
			
			startFiguurBox.setLocation(mzip.getSize().width + 2 * offset, startFiguurBox.getLocation().y);
			aantalHoekpuntenLabel.setLocation(mzip.getSize().width + 2 * offset, aantalHoekpuntenLabel.getLocation().y);
			fractielTypeLabel.setLocation(mzip.getSize().width + 2 * offset, fractielTypeLabel.getLocation().y);
			aantalHoekpuntenCombo.setLocation(mzip.getSize().width + 5 * offset, aantalHoekpuntenCombo.getLocation().y);
			fractielTypeCombo.setLocation(mzip.getSize().width + 5 * offset, fractielTypeCombo.getLocation().y);
			aantalPerZijdeLabel.setLocation(mzip.getSize().width + 2 * offset, aantalPerZijdeLabel.getLocation().y);
			aantalPerZijdeCombo.setLocation(mzip.getSize().width + 5 * offset, aantalPerZijdeCombo.getLocation().y);
		}
	}
	
	public void setEditState(Hashtable b)
	{
		
System.out.println("mziep setEditState");

		boolean fractielen = false; 
		boolean startFiguur = false;
		int aantalHoekpunten = 3;
		int aantalPerZijde = 1;
		int fractielType = 1;

		if (b.containsKey("appletLaunchData"))
		{
System.out.println("aLD found");
			Hashtable appletLaunchData = (Hashtable) b.get("appletLaunchData");
			
			String fractielenString = "false";
			String startFiguurString = "";
			String aantalHoekpuntenString = "3";
			String aantalPerZijdeString = "1";
			
			if (appletLaunchData.containsKey("fractielen"))
				fractielenString = (String) appletLaunchData.get("fractielen");
			if (fractielenString.equals("true") || fractielenString.equals("yes"))
				fractielen = true;
			if (appletLaunchData.containsKey("startFiguur"))
				startFiguurString = (String) appletLaunchData.get("startFiguur");
			if (startFiguurString.length() > 0)
				startFiguur = true;
			if (fractielen)
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
					startFiguurString = startFiguurString.substring(0, grens);
				boolean error = false;
				try
				{	fractielType = Integer.parseInt(startFiguurString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (fractielType >= 1) && (fractielType <= 3))
				{	// geen actie					
				}
				else
					fractielType = 1;
			}
			else
			{	int grens = startFiguurString.indexOf(',');
				if (grens > 0)
				{	aantalHoekpuntenString = startFiguurString.substring(0, grens);
					aantalPerZijdeString = startFiguurString.substring(grens + 1);
				}
				else
					aantalHoekpuntenString = startFiguurString;
				boolean error = false;
				try
				{	aantalHoekpunten = Integer.parseInt(aantalHoekpuntenString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && ((aantalHoekpunten == 3) || (aantalHoekpunten == 4) || (aantalHoekpunten == 6) ||
						       (aantalHoekpunten == 8) || (aantalHoekpunten == 12)))
				{	// geen actie					
				}
				else
					aantalHoekpunten = 3;
				error = false;
				try
				{	aantalPerZijde = Integer.parseInt(aantalPerZijdeString);
				}
				catch (NumberFormatException nfe)
				{	error = true;
				}
				if (!error && (aantalPerZijde >= 1) && (aantalPerZijde <= 4))
				{	// geen actie					
				}
				else
					aantalPerZijde = 1;
			}

		}
		else
		{
			if (b.containsKey("fractielen"))
				fractielen = ((Boolean) b.get("fractielen")).booleanValue();
			if (b.containsKey("startFiguur"))
				startFiguur = ((Boolean) b.get("startFiguur")).booleanValue();
			if (b.containsKey("fractielType"))
				fractielType = ((Integer) b.get("fractielType")).intValue();
			if (b.containsKey("aantalHoekpunten"))
				aantalHoekpunten = ((Integer) b.get("aantalHoekpunten")).intValue();
			if (b.containsKey("aantalPerZijde"))
				aantalPerZijde = ((Integer) b.get("aantalPerZijde")).intValue();
			
		}
		
		fractielenBox.setSelected(fractielen);
		startFiguurBox.setSelected(startFiguur);
		aantalHoekpuntenComboEnabled = false;
		if (aantalHoekpunten == 3)
			aantalHoekpuntenCombo.setSelectedIndex(0);
		else if (aantalHoekpunten == 4)
			aantalHoekpuntenCombo.setSelectedIndex(1);
		else if (aantalHoekpunten == 6)
			aantalHoekpuntenCombo.setSelectedIndex(2);
		else if (aantalHoekpunten == 8)
			aantalHoekpuntenCombo.setSelectedIndex(3);
		else if (aantalHoekpunten == 12)
			aantalHoekpuntenCombo.setSelectedIndex(4);
		aantalHoekpuntenComboEnabled = true;
		aantalPerZijdeComboEnabled = false;
		aantalPerZijdeCombo.setSelectedIndex(aantalPerZijde - 1);
		aantalPerZijdeComboEnabled = true;
		fractielTypeComboEnabled = false;
		fractielTypeCombo.setSelectedIndex(fractielType - 1);
		fractielTypeComboEnabled = true;
		
		aantalHoekpuntenLabel.setVisible(!fractielenBox.isSelected());
		aantalHoekpuntenCombo.setVisible(!fractielenBox.isSelected());
		aantalPerZijdeLabel.setVisible(!fractielenBox.isSelected());
		aantalPerZijdeCombo.setVisible(!fractielenBox.isSelected());
		fractielTypeLabel.setVisible(fractielenBox.isSelected());
		fractielTypeCombo.setVisible(fractielenBox.isSelected());

		aantalHoekpuntenLabel.setEnabled(startFiguurBox.isSelected());
		aantalHoekpuntenCombo.setEnabled(startFiguurBox.isSelected());
		aantalPerZijdeLabel.setEnabled(startFiguurBox.isSelected());
		aantalPerZijdeCombo.setEnabled(startFiguurBox.isSelected());
		fractielTypeLabel.setEnabled(startFiguurBox.isSelected());
		fractielTypeCombo.setEnabled(startFiguurBox.isSelected());
		
		
		if (b.containsKey("mzipBreedte"))
			mzipBreedte = ((Integer) b.get("mzipBreedte")).intValue();
		if (b.containsKey("mzipHoogte"))
			mzipHoogte = ((Integer) b.get("mzipHoogte")).intValue();
		
		setBounds(getLocation().x, getLocation().y, mzipBreedte + editWidth, Math.max(mzipHoogte, editHeight));
		
		// HIER !!
		mzip.setEditState(b);		
		
	}
	
	public Hashtable getEditState()
	{
System.out.println("mziep getEditState");

		Hashtable h = mzip.getEditState(); 
		
		h.put("scoreMax", new Integer(scoreMax));
		
		h.put("mzipBreedte", new Integer(mzipBreedte));
		h.put("mzipHoogte", new Integer(mzipHoogte));
		
		return h;
		
	}
		
	public void setBounds(int x, int y, int b, int h)
	{
		if (noSetBounds)
		{	noSetBounds = false;
			return;
		}
//System.out.println("mziep setBounds raw " + x + " " + y + " " + b + " " + h);

		if ((h <= 1) || (x < 0) || (b <= 1))
			return;
		
		super.setBounds(x, y, mzipBreedte + editWidth, Math.max(mzipHoogte, editHeight));
		
//System.out.println("mziep setBounds " + x + " " + y + " " + (mzipBreedte + editWidth) + " " + 
//					Math.max(mzipHoogte, editHeight));
	
		if (mzip != null)
			mzip.setBounds(0, 0, mzipBreedte, mzipHoogte);
		
		plaatsComponenten();
		
//System.out.println("setBounds " + x + " " + y + " " + b + " " + h);		
		
	}
	
	public void zetBreedte(int b)
	{	
		mzipBreedte = b;
		
		setBounds(getLocation().x, getLocation().y, mzipBreedte + editWidth, Math.max(mzipHoogte, editHeight));		
		plaatsComponenten();
	}
	
	public void zetHoogte(int h)
	{	
		mzipHoogte = h;
		
		setBounds(getLocation().x, getLocation().y, mzipBreedte + editWidth, Math.max(mzipHoogte, editHeight));		
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
	{	if (e.getSource() == fractielenBox)
		{
			mzip.zetFractielen(fractielenBox.isSelected());
		
			aantalHoekpuntenLabel.setVisible(!fractielenBox.isSelected());
			aantalHoekpuntenCombo.setVisible(!fractielenBox.isSelected());
			aantalPerZijdeLabel.setVisible(!fractielenBox.isSelected());
			aantalPerZijdeCombo.setVisible(!fractielenBox.isSelected());
			fractielTypeLabel.setVisible(fractielenBox.isSelected());
			fractielTypeCombo.setVisible(fractielenBox.isSelected());
			
		
		}
		else if (e.getSource() == startFiguurBox)
		{
			mzip.zetStartFiguur(startFiguurBox.isSelected());
			
			aantalHoekpuntenLabel.setEnabled(startFiguurBox.isSelected());
			aantalHoekpuntenCombo.setEnabled(startFiguurBox.isSelected());
			aantalPerZijdeLabel.setEnabled(startFiguurBox.isSelected());
			aantalPerZijdeCombo.setEnabled(startFiguurBox.isSelected());
			fractielTypeLabel.setEnabled(startFiguurBox.isSelected());
			fractielTypeCombo.setEnabled(startFiguurBox.isSelected());
			
		}
		else if (e.getSource() == aantalHoekpuntenCombo)
		{
			if (!aantalHoekpuntenComboEnabled)
				return;
			
			int index = aantalHoekpuntenCombo.getSelectedIndex();
			if (index == 0)
				mzip.zetAantalHoekpunten(3);
			else if (index == 1)
				mzip.zetAantalHoekpunten(4);
			else if (index == 2)
				mzip.zetAantalHoekpunten(6);
			else if (index == 3)
				mzip.zetAantalHoekpunten(8);
			else if (index == 4)
				mzip.zetAantalHoekpunten(12);
		}
		else if (e.getSource() == aantalPerZijdeCombo)
		{
			if (!aantalPerZijdeComboEnabled)
				return;
			
			int index = aantalPerZijdeCombo.getSelectedIndex();
			
			mzip.zetAantalPerZijde(index + 1);
		}
		else if (e.getSource() == fractielTypeCombo)
		{
			if (!fractielTypeComboEnabled)
				return;
			
			int index = fractielTypeCombo.getSelectedIndex();
			
			mzip.zetFractielType(index + 1);
			
		}
		

	}

}
